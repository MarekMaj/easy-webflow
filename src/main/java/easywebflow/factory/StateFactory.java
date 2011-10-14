package easywebflow.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.inject.Inject;

import easywebflow.core.Flow;
import easywebflow.command.Command;
import easywebflow.config.CommandConfig;
import easywebflow.config.StateConfig;
import easywebflow.config.TransitionConfig;
import easywebflow.state.SimpleState;
import easywebflow.state.StartInvocationState;
import easywebflow.state.State;
import easywebflow.state.Transition;

public class StateFactory {

	@Inject
	private CommandFactory cf = new CommandFactory();
	
	@Inject
	private TransitionFactory tf = new TransitionFactory();
	
	public State create(Flow flow, StateConfig sc) {
		HashMap<String, Transition> transitionMap = new HashMap<String, Transition>();
		Iterator<Entry<String, TransitionConfig>> it = sc.getTransitions().entrySet().iterator();
		
		while (it.hasNext()){
			Entry<String, TransitionConfig> entry = it.next();
			transitionMap.put(entry.getKey(), tf.create(flow, entry.getValue()));
		}
		
		State state = new SimpleState(sc.getName(), transitionMap);
		
		if (sc.getOnStartCommands() != null){
			ArrayList<CommandConfig> config = sc.getOnStartCommands();
			ArrayList<Command> command = new ArrayList<Command>();
			
			for (CommandConfig cc:config){
				command.add(cf.create(flow, cc));
			}
			state = new StartInvocationState(state, command);
		}
		
		return state;
	}

}
