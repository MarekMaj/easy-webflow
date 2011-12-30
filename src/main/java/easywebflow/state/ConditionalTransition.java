package easywebflow.state;

import easywebflow.command.Command;

public class ConditionalTransition extends DecoratedTransition {

	private Command condition;

	public ConditionalTransition(Transition delegate, Command condition) {
		super(delegate);
		this.condition = condition;
	}

	@Override
	public Boolean isAllowed(String eventName) {
		// check if condition is true
		if (this.delegate.isAllowed(eventName) && condition.execute()){
			return true;
		}
		return false;
	}
	
}
