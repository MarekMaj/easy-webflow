package easywebflow.state;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import easywebflow.command.Command;

public class DecisionTransition extends DecoratedTransition {

	private LinkedHashMap<Command, String> commandMap;
	//private ArrayList<CommanToR> ifData;
	private String elseTo;
	
	public DecisionTransition(Transition delegate, LinkedHashMap<Command, String> commandMap) {
		super(delegate);
		this.commandMap = commandMap;
	}
	
	@Override
	public String transition() {
		super.transition();
		
		Iterator<Entry<Command, String>> it = commandMap.entrySet().iterator();
		while (it.hasNext()){
			Entry<Command, String> entry = it.next();
			if (entry.getKey().execute()){
				return entry.getValue();
			}
		}
		
		return elseTo;
	}

		
}
