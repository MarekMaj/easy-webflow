package easywebflow.state;

import java.util.ArrayList;

import easywebflow.command.Command;

public class InvokeTransition extends DecoratedTransition {

	private ArrayList<Command> commands;
	
	public InvokeTransition(Transition delegate, ArrayList<Command> commands) {
		super(delegate);
		this.commands = commands;
	}

	@Override
	public String transition() {
		String s = super.transition();
		for (Command cc: this.commands){
			cc.execute();
		}
		return s;
	}
	
	
}
