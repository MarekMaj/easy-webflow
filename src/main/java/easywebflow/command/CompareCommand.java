package easywebflow.command;

import easywebflow.core.Flow;

public class CompareCommand implements Command {

	private Flow flow;
	private String objectName;
	private String result;			// TODO tutaj równiez inne typy np. boolean ?
	
	public CompareCommand(Flow flow, String objectName, String result) {
		super();
		this.flow = flow;
		this.objectName = objectName;
		this.result = result;
	}

	@Override
	public boolean execute() {
		return flow.compareObjects(objectName, result);
	}

}
