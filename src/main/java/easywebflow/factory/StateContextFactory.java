package easywebflow.factory;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import easywebflow.core.Flow;
import easywebflow.config.FlowConfig;
import easywebflow.config.StateConfig;
import easywebflow.state.State;
import easywebflow.state.StateContext;

public class StateContextFactory {

	private StateFactory stateFactory = new StateFactory();
	
	public StateContext create(Flow flow, FlowConfig flowConfig) {
		LinkedHashMap<String, State> states = new LinkedHashMap<String, State>();
		Iterator<Entry<String, StateConfig>> it = flowConfig.getStates().entrySet().iterator();

		Entry<String, StateConfig> entry = null;
		while (it.hasNext()){
			entry = it.next();
			states.put(entry.getKey(), stateFactory.create(flow, entry.getValue()));
		}

		return new StateContext(states, states.entrySet().iterator().next().getKey(), entry.getKey());
	}

}
