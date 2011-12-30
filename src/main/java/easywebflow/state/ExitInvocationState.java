package easywebflow.state;

import java.util.ArrayList;

import easywebflow.command.Command;

public class ExitInvocationState extends DecoratedState {
	private ArrayList<Command> cd;
	
	public ExitInvocationState(State state, ArrayList<Command> cd){
		super(state);
		this.cd = cd;
	}
	
	@Override
	public void onExit() {
		//wywolaj akcje: 
		for (Command c:cd){
			c.execute();
		}
		
		super.onExit();
	}

}
