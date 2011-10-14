package easywebflow.config;

import java.util.LinkedHashMap;

public class TransitionConfig {

	private String on;
	
	//optional
	private CommandConfig onStartCommand;
	private LinkedHashMap<CommandConfig, String> ifz;		// if String not given then set to CommandConfig.getVariableValue()
	private String to;										// if to not given then set to this.on
	
	public TransitionConfig(TransitionConfigBuilder tcb) {
		super();
		this.on = tcb.getOn();
		this.onStartCommand = tcb.getOnStartCommand();
		this.ifz = tcb.getIfz();
		this.to = tcb.getTo();
	}

	public String getOn() {
		return on;
	}

	public CommandConfig getOnStartCommand() {
		return onStartCommand;
	}

	public LinkedHashMap<CommandConfig, String> getIfz() {
		return ifz;
	}

	public String getTo() {
		return to;
	}
	

	
}
