package easywebflow.state;

import easywebflow.command.Command;

public class InvokeTransition extends DecoratedTransition {

	private Command command;
	
	public InvokeTransition(Transition delegate, Command command) {
		super(delegate);
		this.command = command;
	}

	@Override
	public String transition() {
		command.execute();
		return super.transition();
	}
	
	
}
