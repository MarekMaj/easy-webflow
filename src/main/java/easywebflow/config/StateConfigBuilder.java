package easywebflow.config;

import java.util.ArrayList;
import java.util.HashMap;

public class StateConfigBuilder {
	
	//required
	private String name;
	private HashMap<String, TransitionConfig> transitions;
	// TODO nazwy przejść nie powinny sie powtarzac
	
	//optional
	private String viewName;								//when not given set to 'name'
	private ArrayList<CommandConfig> onStartCommands;
	
	public StateConfigBuilder() {
		super();
	}
	
	public StateConfig build(){
		return new StateConfig(this);
	}
	
	//additional methods for better flexibility 
	public void addTransitions(TransitionConfig tc){
		if (transitions == null){
			transitions = new HashMap<String, TransitionConfig>();
		}
		this.transitions.put(tc.getOn(), tc);
	}
	
	public void addOnStartCommands(CommandConfig cc){
		if (this.onStartCommands == null){
			this.onStartCommands = new ArrayList<CommandConfig>();
		}
		this.onStartCommands.add(cc);
	}

	//getters and setters
	public String getName() {
		return name;
	}

	public StateConfigBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public HashMap<String, TransitionConfig> getTransitions() {
		return transitions;
	}

	public StateConfigBuilder setTransitions(HashMap<String, TransitionConfig> transitions) {
		this.transitions = transitions;
		return this;
	}

	public String getViewName() {
		return viewName;
	}

	public StateConfigBuilder setViewName(String viewName) {
		this.viewName = viewName;
		return this;
	}

	public ArrayList<CommandConfig> getOnStartCommands() {
		return onStartCommands;
	}

	public StateConfigBuilder setOnStartCommands(ArrayList<CommandConfig> onStartCommands) {
		this.onStartCommands = onStartCommands;
		return this;
	}
	
}
