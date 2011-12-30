package easywebflow.factory;

import java.util.HashMap;
import java.util.Set;

import easywebflow.configuration.Configuration;
import easywebflow.core.Flow;
import easywebflow.core.FlowInitializingData;
import easywebflow.config.FlowType;

public class MainFactory {
	
	private final FlowContextFactory fcf = new FlowContextFactory();

	public MainFactory(){}
	/*tworzy nowy flow*/
	//static ? 
	// TODO nie stateContext ale flow Context - potrzebuje jeszcze info o inicjalizacji niektorywch beanow

	public FlowInitializingData getFlowContext(Flow flow, String flowName){
		HashMap<String, FlowType> flows = Configuration.getInstance().getFlows(); 
		return fcf.create(flow, flows.get(flowName));
	}	

	public Set<String> getFlowNames(){
		HashMap<String, FlowType> flows = Configuration.getInstance().getFlows(); 
		return flows.keySet();
	}
	
}
