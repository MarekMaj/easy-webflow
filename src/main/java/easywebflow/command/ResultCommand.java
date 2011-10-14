package easywebflow.command;

import easywebflow.core.Flow;

public class ResultCommand implements Command {

	private Flow flow;
	private String beanName;
	private String methodName;
	private String resultName;
	private String[] paramNames;
	
		
	public ResultCommand(Flow flow, String beanName, String methodName,
			String resultName, String... paramNames) {
		super();
		this.flow = flow;
		this.beanName = beanName;
		this.methodName = methodName;
		this.resultName = resultName;
		this.paramNames = paramNames;

	}

	@Override
	public boolean execute() {
		Object object = flow.invoke(beanName, methodName, paramNames);
		flow.setResult(resultName, object);
		return true;
	}

}
