package easywebflow.state;

import java.io.Serializable;
import java.util.LinkedHashMap;

import easywebflow.security.Secured;
import easywebflow.security.SecurityException;

public class StateContext implements Serializable{
	
	private LinkedHashMap<String, State> states;
	private State currentState;
	private String flowName;
	
	public StateContext(){}
	
	public StateContext(LinkedHashMap<String, State> states, String startStateName, String flowName) {
		super();
		this.states = states;
		this.currentState = states.get(startStateName); 
		this.flowName = flowName;
	}

	public void init(StateContext stateContext) {
		// jezeli bedzie flow to lepiej wstrzyknac flowa i go zapytac o nazwe i stad wolac MainFactory
		// nie moge go wstrzyknac ale moge sprawdzuc gdzie zostalem wstrzykniety - NIE! tylko metadata a potrzebowałbym instacji
		this.states = stateContext.states;
		this.currentState = stateContext.currentState; 
		this.flowName = stateContext.flowName;
		
	}
	
	public void enterStartState(){ 
		currentState.onStart();
	}
	
	public String transitionTo(String transitionName) throws SecurityException, IllegalTransitionException{
		// TODO a co z bezpieczeństwem ???? jezeli nie bedzie praw to przeciez nie ma rollback ... 
		// TODO isTransitionLegal ??
		// cala metoda bedzue zabezpieczona jezeli wyrzuci wyjatek bezpieczenstwa nic sie nie stanie 
		// po zalogowaniu zostanie przwywrocna obecna strona i juz 
		
		// when end state then return null
		if (currentState.isFinal()){
			currentState.onExit();
			return null;
		}
		
		// otherwise try to execute transition
		return executeTransition(transitionName);
	}
	
	// TODO if lifecycle will be created do something like transitionTo(StateContext , targetStateName) and change interceptor
	@Secured
	private String executeTransition(String transitionName) throws IllegalTransitionException, SecurityException{
		String targetStateName = null;		
		currentState.onExit();
		
		// when illegal transition throw exception 
		targetStateName = currentState.onTransition(transitionName);
		
		// otherwise enter new state and return its name
		currentState = states.get(targetStateName);	
		currentState.onStart();
		return targetStateName;		
	}
	
	public String getStateNameForTransition(String transitionName) throws IllegalTransitionException{
		return this.currentState.getStateNameForTransition(transitionName);
		
	}
	
	/*test*/
	public String getCurrentStateName(){
		return this.currentState.getName();
	}

	public String getFlowName() {
		return flowName;
	}
	
}
