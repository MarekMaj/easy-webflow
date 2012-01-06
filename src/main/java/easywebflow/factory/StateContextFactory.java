package easywebflow.factory;

import java.util.Iterator;
import java.util.LinkedHashMap;

import easywebflow.core.Flow;
import easywebflow.config.FlowType;
import easywebflow.config.StateType;
import easywebflow.state.State;
import easywebflow.state.StateContext;

public class StateContextFactory {

	private StateFactory stateFactory = new StateFactory();
	
	public StateContext create(Flow flow, FlowType flowType) {
		LinkedHashMap<String, State> states = new LinkedHashMap<String, State>();
		Iterator<StateType> it = flowType.getStates().iterator();
		
		// not sure what will be the order of states in collection
		// especially if initial state is first
		StateType state = null;
		while (it.hasNext()){
			state = it.next();
			states.put(state.getId(), stateFactory.create(flow, state));
		}

		// invoke StateContext(LinkedHashMap<String, State> states, String startStateName, String endStateName) {
		return new StateContext(states, flowType.getInitial(), flowType.getName());
	}

}
