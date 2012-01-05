package easywebflow.factory;

import java.util.ArrayList;
import java.util.Iterator;

import easywebflow.core.Flow;
import easywebflow.config.StateType;
import easywebflow.config.TransitionType;
import easywebflow.state.ExitInvocationState;
import easywebflow.state.SimpleState;
import easywebflow.state.StartInvocationState;
import easywebflow.state.State;
import easywebflow.state.Transition;

public class StateFactory {

	private CommandFactory cf = new CommandFactory();
	private TransitionFactory tf = new TransitionFactory();
	
	public State create(Flow flow, StateType st) {
		// after SCXML integration cannot use hashmap 
		ArrayList<Transition> transitionList = new ArrayList<Transition>();
		Iterator<TransitionType> it = st.getTransition().iterator();
		
		while (it.hasNext()){
			TransitionType tt = it.next();
			transitionList.add(tf.create(flow, tt));
		}
		
		State state = new SimpleState(st.getId(), transitionList);
		
		if (!st.getOnentry().isEmpty()){
			state = new StartInvocationState(state, cf.create(flow, st.getOnentry()));
		}
		
		if (!st.getOnexit().isEmpty()){
			state = new ExitInvocationState(state, cf.create(flow, st.getOnexit()));
		}
		
		return state;
	}

}
