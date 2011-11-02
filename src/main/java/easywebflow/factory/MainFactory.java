package easywebflow.factory;

import java.util.HashMap;
import java.util.Set;

import easywebflow.configuration.Configuration;
import easywebflow.core.Flow;
import easywebflow.config.FlowConfig;
import easywebflow.state.StateContext;

public class MainFactory {
	
	private static StateContextFactory scf = new StateContextFactory();
	private static MainFactory instance;
	private static HashMap<String, FlowConfig> flows;

	private MainFactory(){
		parseConfig();
	}
	
	public static MainFactory getInstance(){
		if (instance == null){
			instance = new MainFactory();
		}
		return instance;
	}
	
	private static void parseConfig(){
		//flows = PatternRecognizer.parseFile(getConfigFilePath());
		flows = (HashMap<String, FlowConfig>) Configuration.getFlows();
	}

	/*tworzy nowy flow*/
	//static ? 
	public static StateContext getStateContext(Flow flow, String flowName){
		return scf.create(flow, flows.get(flowName));
	}
	
	public static Set<String> getFlowNames(){
		return flows.keySet();
	}
	
}
