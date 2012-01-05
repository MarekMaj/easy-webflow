package easywebflow.factory;

import easywebflow.core.Flow;
import easywebflow.config.InvokationType;
import easywebflow.config.TransitionType;
import easywebflow.state.ConditionalTransition;
import easywebflow.state.InvokeTransition;
import easywebflow.state.SimpleTransition;
import easywebflow.state.Transition;

public class TransitionFactory {

	private CommandFactory cf = new CommandFactory();
	
	public Transition create(Flow flow, TransitionType tt) {
		Transition transition = new SimpleTransition(tt.getEvent(), tt.getTarget());
		
		// add invokes
		if( !tt.getInvoke().isEmpty()){
			transition = new InvokeTransition(transition, cf.create(flow, tt.getInvoke()));
		}
		
		// add condition rule
		if (tt.getComparedObject()!= null && tt.getComparedValue()!=null){
			transition = new ConditionalTransition(transition, cf.create(flow, tt.getComparedObject() , tt.getComparedValue()));
		}
			
		return transition;
	}

}
