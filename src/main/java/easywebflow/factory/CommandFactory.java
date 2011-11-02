package easywebflow.factory;

import java.util.ArrayList;

import easywebflow.core.Flow;
import easywebflow.command.*;
import easywebflow.config.CommandConfig;

public class CommandFactory {

	public Command create(Flow flow, CommandConfig cc) {
		String[] params = new String[0];
		
		if (cc.getVariableName() !=null){
			return new CompareCommand(flow, cc.getVariableName(), cc.getVariableValue());
		}
		
		if (cc.getParamName() != null){
			params = new String[cc.getParamName().size()];
			cc.getParamName().toArray(params);
		}
		
		if (cc.getResultName() == null ){
			return new SimpleCommand(flow, cc.getBeanName(), cc.getMethodName(), params);
		}else {
			return new ResultCommand(flow, cc.getBeanName(), cc.getMethodName(), cc.getResultName(), params);
		}
	}

	public ArrayList<Command> create(Flow flow, ArrayList<CommandConfig> onTransition) {
		ArrayList<Command> list = new ArrayList<Command>();
		for (CommandConfig cc:onTransition){
			list.add(this.create(flow, cc));
		}
		return list;
	}



}
