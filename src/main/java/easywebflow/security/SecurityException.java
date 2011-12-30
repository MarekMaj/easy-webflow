package easywebflow.security;

import easywebflow.core.EasyWebflowException;

public class SecurityException extends EasyWebflowException {

	private String stateName;
	private String flowName;
	
	public String getStateName() {
		return stateName;
	}

	public String getFlowName() {
		return flowName;
	}

	public SecurityException() {
		super();
	}
	
	public SecurityException(String flowName, String stateName) {
		super();
		this.stateName = stateName;
		this.flowName = flowName;
	}
	
	public SecurityException(String flowName, String stateName, String message) {
		super(message);
		this.stateName = stateName;
		this.flowName = flowName;
	}
	
	public String getMessage(){
		if (this.flowName != null && this.stateName != null)
			return "From state: " + flowName +"/"+stateName + " - "+super.getMessage();
			
		return super.getMessage();	
	}
	
}
