package easywebflow.config_old;

import java.util.LinkedHashMap;

public class FlowConfig {

	private String flowName;
	private LinkedHashMap<String, StateConfig> states;
	
	public FlowConfig(FlowConfigBuilder fcb) {
		this.flowName = fcb.getFlowName();
		this.states = fcb.getStates();
	}

	public String getFlowName() {
		return flowName;
	}

	public LinkedHashMap<String, StateConfig> getStates() {
		return states;
	}

	
}
