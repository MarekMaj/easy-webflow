package easywebflow.factory;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.inject.Inject;

import easywebflow.core.Flow;
import easywebflow.command.Command;
import easywebflow.config.CommandConfig;
import easywebflow.config.TransitionConfig;
import easywebflow.state.DecisionTransition;
import easywebflow.state.InvokeTransition;
import easywebflow.state.SimpleTransition;
import easywebflow.state.Transition;

public class TransitionFactory {

	@Inject
	private CommandFactory cf = new CommandFactory();
	
	public Transition create(Flow flow, TransitionConfig tc) {
		Transition transition = new SimpleTransition(tc.getTo());
		
		if(tc.getOnStartCommand() != null){
			transition = new InvokeTransition(transition, cf.create(flow, tc.getOnStartCommand()));
		}
		
		if (tc.getIfz() != null){
			LinkedHashMap<Command, String> ifz = new LinkedHashMap<Command, String>();
			Iterator<Entry<CommandConfig, String>> it = tc.getIfz().entrySet().iterator();

			while (it.hasNext()){
				Entry<CommandConfig, String> entry = it.next();
				ifz.put(cf.create(flow, entry.getKey()), entry.getValue());
			}
			
			transition = new DecisionTransition(transition, ifz);
		}
			
		return transition;
	}

}