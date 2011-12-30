package easywebflow.config_old;

import java.util.ArrayList;
import java.util.HashMap;

public class StateConfig {

	//required
	private String name;
	private HashMap<String, TransitionConfig> transitions;
	// TODO nazwy przejść nie powinny sie powtarzac
	
	//optional
	private String viewName;								//when not given set to 'name'
	private ArrayList<CommandConfig> onStartCommands;
	
	public StateConfig(StateConfigBuilder scb) {
		this.name = scb.getName();
		this.transitions = scb.getTransitions();
		this.viewName = scb.getViewName() == null ? scb.getName(): scb.getViewName();
		this.onStartCommands = scb.getOnStartCommands();
	}

	public String getName() {
		return name;
	}

	public HashMap<String, TransitionConfig> getTransitions() {
		return transitions;
	}

	public String getViewName() {
		return viewName;
	}

	public ArrayList<CommandConfig> getOnStartCommands() {
		return onStartCommands;
	}	

}
