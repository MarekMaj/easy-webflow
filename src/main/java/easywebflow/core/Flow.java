package easywebflow.core;


public interface Flow {
	//String transitionTo(String stateName);
	Object invoke(String beanName, String methodName, String... paramName);
	void setResult(String resultName, Object result);
	void setFlowName(String flowName);
	boolean compareObjects(String comparedObjectName, String comparedObjectValue);
	/*test*/
	String transitionTo();
}
