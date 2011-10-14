package easywebflow.state;

import java.util.HashMap;

public class SimpleState implements State {

	private String name;
	private HashMap<String, Transition> transitionMap;
	
	public SimpleState(String name, HashMap<String, Transition> map) {
		this.name = name;
		this.transitionMap = map;
	}

	@Override
	public void onStart() {
	}

	@Override
	public String onTransition(String name) {
		if (!transitionMap.containsKey(name)){
			// TODO rzucaj wyjatek IllegalTransitionException()
			return null;
		}
		return transitionMap.get(name).transition();

	}

	@Override
	public void onExit() {
	}

	@Override
	public String getName() {
		return name;
	}

}
