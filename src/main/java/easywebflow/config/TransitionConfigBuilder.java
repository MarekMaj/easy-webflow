package easywebflow.config;

import java.util.LinkedHashMap;

public class TransitionConfigBuilder {
	
	private String on;
	
	//optional
	private CommandConfig onStartCommand;
	private LinkedHashMap<CommandConfig, String> ifz;
	private String to;
	
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

	public CommandConfig getOnStartCommand() {
		return onStartCommand;
	}

	public TransitionConfigBuilder setOnStartCommand(CommandConfig onStartCommand) {
		this.onStartCommand = onStartCommand;
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
		return to;
	}

	public TransitionConfigBuilder setTo(String to) {
		this.to = to;
		return this;
	}
	
	
}
