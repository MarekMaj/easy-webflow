package easywebflow.factory;

import easywebflow.core.Flow;
import easywebflow.command.*;
import easywebflow.config.CommandConfig;

public class CommandFactory {

	public Command create(Flow flow, CommandConfig cc) {
		
		if (cc.getVariableName() !=null){
			return new CompareCommand(flow, cc.getVariableName(), cc.getVariableValue());
		}
		
		String[] params = new String[cc.getParamName().size()];
		cc.getParamName().toArray(params);
		
		if (cc.getResultName() == null ){
			return new SimpleCommand(flow, cc.getBeanName(), cc.getMethodName(), params);
		}else {
			return new ResultCommand(flow, cc.getBeanName(), cc.getMethodName(), cc.getResultName(), params);
		}
	}



}
