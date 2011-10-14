package easywebflow.state;

import java.util.LinkedHashMap;



public class StateContext {
	
	private LinkedHashMap<String, State> states;
	private State currentState;
	private String endStateName;
	
	public StateContext(LinkedHashMap<String, State> states, String startStateName, String endStateName) {
		super();
		System.out.println("tworze context");
		for (String s:states.keySet()){
			System.out.println("Dodaje stan o nazwie " + s);
		}
		this.states = states;
		this.currentState = states.get(startStateName); 
		this.endStateName = endStateName;
	}

	public void enterStartState(){
		System.out.println("Wchodze do stanu poczaktowego "+this.currentState.getName()); 
		currentState.onStart();
	}
	
	public String transitionTo(String name){
		
		// TODO co jezeli takie przejscie jest nielegalne 
		currentState.onExit();
		// TODO co jezeli stan końcowy ?? moze i tak koncze obecny stan np. jezeli ze wzgledow bezp mam Exception
		if (currentState.getName().equalsIgnoreCase(endStateName)){
			return null;
		}
		
		String stateName = currentState.onTransition(name);
		// TODO a co z bezpieczeństwem ???? jezeli nie bedzie praw to przeciez nie ma rollback ... 
		// TODO isTransitionLegal ??
		
		currentState = getStateForName(stateName);
		currentState.onStart();
		return stateName;
	}
	
	private State getStateForName(String stateName){
		return states.get(stateName);	
	}
	
	/*test*/
	public String getCurrentStateName(){
		return this.currentState.getName();
	}
	
}
