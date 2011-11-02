package easywebflow.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class TransitionConfigBuilder {
	
	private String on;
	
	//optional
	private ArrayList<CommandConfig> onTransition;
	private ArrayList<CommandConfig> onDecision;
	private LinkedHashMap<CommandConfig, String> ifz;	// if String not given then set to CommandConfig.getVariableValue()
	private String to;									// if to not given then set to this.on
	private String elseTo;
	
	public TransitionConfigBuilder() {
		super();
	}	
	
	//additional method for better flexibility 
	public void addIfz(CommandConfig cc, String s){
		if (this.ifz == null){
			ifz = new LinkedHashMap<CommandConfig, String>();
		}
		ifz.put(cc, s == null ? cc.getVariableValue():s);
	}

	public void addOnTransition(CommandConfig cc){
		if (this.onTransition == null){
			this.onTransition = new ArrayList<CommandConfig>();
		}
		this.onTransition.add(cc);
	}
	
	public void addOnDecision(CommandConfig cc){
		if (this.onDecision == null){
			this.onDecision = new ArrayList<CommandConfig>();
		}
		this.onDecision.add(cc);
	}
	
	//getters and setters
	public TransitionConfig build(){
		return new TransitionConfig(this);
	}

	public String getOn() {
		return on;
	}

	public TransitionConfigBuilder setOn(String on) {
		this.on = on;
		return this;
	}

	public ArrayList<CommandConfig> getOnTransition() {
		return onTransition;
	}

	public TransitionConfigBuilder setOnTransition(ArrayList<CommandConfig> onTransition) {
		this.onTransition = onTransition;
		return this;
	}

	public ArrayList<CommandConfig> getOnDecision() {
		return this.onDecision;
	}

	public TransitionConfigBuilder setOnDecision(ArrayList<CommandConfig> onDecision) {
		this.onDecision = onDecision;
		return this;
	}
	
	public LinkedHashMap<CommandConfig, String> getIfz() {
		return ifz;
	}

	public TransitionConfigBuilder setIfz(LinkedHashMap<CommandConfig, String> ifz) {
		this.ifz = ifz;
		return this;
	}

	public String getTo() {
		return this.to != null ? this.to :this.on;
	}

	public TransitionConfigBuilder setTo(String to) {
		this.to = to;
		return this;
	}
	
	public String getElseTo() {
		return this.elseTo;
	}

	public TransitionConfigBuilder setElseTo(String elseTo) {
		this.elseTo = elseTo;
		return this;
	}	
}
