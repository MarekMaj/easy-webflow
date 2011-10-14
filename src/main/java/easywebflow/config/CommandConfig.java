package easywebflow.config;

import java.util.ArrayList;

public class CommandConfig {

	//all optional
	private String beanName;
	private String methodName;
	private ArrayList<String> paramName;
	private String resultName;
	private String variableName;
	private String variableValue;
	
	public CommandConfig(CommandConfigBuilder ccb){
		this.beanName = ccb.getBeanName();
		this.methodName = ccb.getMethodName();
		this.paramName = ccb.getParamName();
		this.resultName = ccb.getResultName();
		this.variableName = ccb.getVariableName();
		this.variableValue = ccb.getVariableValue();
	}

	public String getBeanName() {
		return beanName;
	}

	public String getMethodName() {
		return methodName;
	}

	public ArrayList<String> getParamName() {
		return paramName;
	}

	public String getResultName() {
		return resultName;
	}

	public String getVariableName() {
		return variableName;
	}

	public String getVariableValue() {
		return variableValue;
	}

	
	
}
