package easywebflow.factory;

import java.util.ArrayList;
import java.util.List;

import easywebflow.core.Flow;
import easywebflow.command.*;
import easywebflow.config.InvokationType;
import easywebflow.config.ParamType;

public class CommandFactory {

	public Command create(Flow flow, InvokationType it) {
		String[] params = new String[0];
		List<ParamType> paramList = it.getParam();

		if (!paramList.isEmpty()){
			params = new String[paramList.size()];
			for (int i=0; i < paramList.size(); i++){
				params[i] = new String(paramList.get(i).getBean());
			}
		}

		if (it.getResult() == null ){
			return new SimpleCommand(flow, it.getBean(), it.getMethod(), params);
		}else {
			return new ResultCommand(flow, it.getBean(), it.getMethod(), it.getResult(), params);
		}
	}

	public ArrayList<Command> create(Flow flow, List<InvokationType> invokes) {
		ArrayList<Command> list = new ArrayList<Command>();
		for (InvokationType invoke:invokes){
			list.add(this.create(flow, invoke));
		}
		return list;
	}

	/*method for compareCommand*/
	public Command create(Flow flow, String comparedObject,
			String comparedValue) {
		return new CompareCommand(flow, comparedObject, comparedValue);
	}



}
