package easywebflow.state;

import java.util.ArrayList;

import easywebflow.command.Command;

public class StartInvocationState extends DecoratedState {
	private ArrayList<Command> cd;
	
	public StartInvocationState(State state, ArrayList<Command> cd){
		super(state);
		this.cd = cd;
	}
	
	@Override
	public void onStart() {
		//wywolaj akcje: 
		for (Command c:cd){
			c.execute();
		}
		
		super.onStart();
	}

}
