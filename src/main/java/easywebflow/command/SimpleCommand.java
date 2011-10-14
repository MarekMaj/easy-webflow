package easywebflow.command;

import easywebflow.core.Flow;

public class SimpleCommand implements Command {

	private Flow flow;
	private String beanName;
	private String methodName;
	private String[] paramNames;
	
	public SimpleCommand(Flow flow, String beanName, String methodName,
			String... paramNames) {
		super();
		this.flow = flow;
		this.beanName = beanName;
		this.methodName = methodName;
		this.paramNames = paramNames;
	}

	@Override
	public boolean execute() {
		flow.invoke(beanName, methodName, paramNames);
		return true;
	}

}
