package easywebflow.state;

import easywebflow.core.EasyWebflowException;

public class IllegalTransitionException extends EasyWebflowException {
	private String stateName;
	private String transitionName;
	
	public String getStateName() {
		return stateName;
	}

	public String getTransitionName() {
		return transitionName;
	}

	public IllegalTransitionException() {
		super();
	}
	
	public IllegalTransitionException(String transition, String stateName) {
		super();
		this.stateName = stateName;
		this.transitionName = transition;
	}
	
	public IllegalTransitionException(String transition, String stateName, String message) {
		super(message);
		this.stateName = stateName;
		this.transitionName = transition;
	}
	
	public String getMessage(){
		if (this.transitionName != null && this.stateName != null)
			return "Transition " + transitionName +" unreachable from state "+stateName + " - "+super.getMessage();
			
		return super.getMessage();	
	}
	
}
