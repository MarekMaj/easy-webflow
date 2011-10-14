package easywebflow.config;

import java.util.LinkedHashMap;

public class FlowConfigBuilder {
	
	private String flowName;
	private LinkedHashMap<String, StateConfig> states;
	
	public FlowConfigBuilder(){
		super();
	}

	public FlowConfig build(){
		return new FlowConfig(this);
	}
	
	//additional method for better flexibility 
	public void addStates(StateConfig sc){
		if (this.states == null){
			this.states = new LinkedHashMap<String, StateConfig>();
		}
		this.states.put(sc.getName(), sc);
	}
	
	//getters and setters
	public String getFlowName() {
		return flowName;
	}

	public FlowConfigBuilder setFlowName(String flowName) {
		this.flowName = flowName;
		return this;
	}

	public LinkedHashMap<String, StateConfig> getStates() {
		return states;
	}

	public FlowConfigBuilder setStates(LinkedHashMap<String, StateConfig> states) {
		this.states = states;
		return this;
	}
	
}
