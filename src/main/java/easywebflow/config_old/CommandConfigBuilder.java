package easywebflow.config_old;

import java.util.ArrayList;

public class CommandConfigBuilder {

	// all optional
	private String beanName;
	private String methodName;
	private ArrayList<String> paramName;
	private String resultName;
	private String variableName;
	private String variableValue;
	
	public CommandConfigBuilder() {
		super();
	}
	
	public CommandConfig build(){
		return new CommandConfig(this);
	}
	
	//additional method for better flexibility 
	public void addParamName(String paramName){
		if (this.paramName == null){
			this.paramName = new ArrayList<String>();
		}
		this.paramName.add(paramName);
	}

	// getters and builder setters 
	public String getBeanName() {
		return beanName;
	}

	public CommandConfigBuilder setBeanName(String beanName) {
		this.beanName = beanName;
		return this;
	}

	public String getMethodName() {
		return methodName;
	}

	public CommandConfigBuilder setMethodName(String methodName) {
		this.methodName = methodName;
		return this;
	}

	public ArrayList<String> getParamName() {
		return paramName;
	}

	public CommandConfigBuilder setParamName(ArrayList<String> paramName) {
		this.paramName = paramName;
		return this;
	}

	public String getResultName() {
		return resultName;
	}

	public CommandConfigBuilder setResultName(String resultName) {
		this.resultName = resultName;
		return this;
	}

	public String getVariableName() {
		return variableName;
	}

	public CommandConfigBuilder setVariableName(String variableName) {
		this.variableName = variableName;
		return this;
	}

	public String getVariableValue() {
		return variableValue;
	}

	public CommandConfigBuilder setVariableValue(String variableValue) {
		this.variableValue = variableValue;
		return this;
	}
	
}
