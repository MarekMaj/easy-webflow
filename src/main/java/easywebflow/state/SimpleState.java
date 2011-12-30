package easywebflow.state;

import java.util.List;

public class SimpleState implements State {

	private String name;
	private List<Transition> transitionList;
	
	public SimpleState(String name, List<Transition> list) {
		this.name = name;
		this.transitionList = list;
	}

	@Override
	public void onStart() {
	}

	@Override
	public String getStateNameForTransition(String name)
			throws IllegalTransitionException {
		for (Transition t : this.transitionList){
			if (t.isAllowed(name)){
				return t.getTargetStateName();
			}
		}
		
		// there is no such transition
		throw new IllegalTransitionException(name, this.name);
	}
	
	@Override
	public String onTransition(String name) throws IllegalTransitionException{
		// search for appropriate transistion if present 
		for (Transition t : this.transitionList){
			if (t.isAllowed(name)){
				return t.transition();
			}
		}
		
		// there is no such transition or condition inside transition is false
		throw new IllegalTransitionException(name, this.name);

	}

	@Override
	public void onExit() {
	}

	@Override
	public String getName() {
		return name;
	}

}
