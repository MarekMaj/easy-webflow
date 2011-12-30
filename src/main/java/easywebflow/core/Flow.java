package easywebflow.core;


public interface Flow {
	String start();
	String transitionTo(String transitionName);
	void setResult(String resultName, Object result);
	Object get(String expression);
	void setFlowName(String flowName);
	String getFlowName();
	
	boolean compareObjects(String comparedObjectName, String comparedObjectValue);
	Object invoke(String beanName, String methodName, String... paramName);
}
