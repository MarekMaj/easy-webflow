package easywebflow.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class TransitionConfig {

	private String on;
	
	//optional
	private ArrayList<CommandConfig> onTransition;
	private ArrayList<CommandConfig> onDecision;
	private LinkedHashMap<CommandConfig, String> ifz;
	private String to;
	private String elseTo;
	
	public TransitionConfig(TransitionConfigBuilder tcb) {
		super();
		this.on = tcb.getOn();
		this.onTransition = tcb.getOnTransition();
		this.onDecision = tcb.getOnDecision();
		this.ifz = tcb.getIfz();
		this.to = tcb.getTo();
		this.elseTo = tcb.getElseTo();
	}

	public String getOn() {
		return on;
	}

	public ArrayList<CommandConfig> getOnTransition() {
		return onTransition;
	}

	public ArrayList<CommandConfig> getOnDecision() {
		return onDecision ;
	}
	
	public LinkedHashMap<CommandConfig, String> getIfz() {
		return ifz;
	}

	public String getTo() {
		return to;
	}
	
	public String getElseTo() {
		return elseTo;
	}
	
}
