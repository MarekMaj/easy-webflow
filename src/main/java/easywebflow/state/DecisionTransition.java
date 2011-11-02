package easywebflow.state;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import easywebflow.command.Command;

public class DecisionTransition extends DecoratedTransition {

	private ArrayList<Command> commands;
	private LinkedHashMap<Command, String> commandMap;
	private String elseTo;
	
	public DecisionTransition(Transition delegate, ArrayList<Command> commands, LinkedHashMap<Command, String> commandMap, String elseTo) {
		super(delegate);
		this.commands = commands;
		this.commandMap = commandMap;
		this.elseTo = elseTo;
	}
	
	@Override
	public String transition() {
		
		for (Command cc: this.commands){
			cc.execute();
		}
		
		Iterator<Entry<Command, String>> it = commandMap.entrySet().iterator();
		while (it.hasNext()){
			Entry<Command, String> entry = it.next();
			if (entry.getKey().execute()){
				super.transition();
				return entry.getValue();
			}
		}
		
		super.transition();
		return this.elseTo;
	}

		
}
