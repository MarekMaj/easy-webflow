package easywebflow.factory;

import java.util.ArrayList;

import easywebflow.config.FlowType;
import easywebflow.core.Flow;
import easywebflow.core.FlowInitializingData;
import easywebflow.state.StateContext;

public class FlowContextFactory {

	private final StateContextFactory scf = new StateContextFactory();
	
	public FlowInitializingData create(Flow flow, FlowType flowType) {
		// create initializing list of beans that will be injected in flow @PostConstruct
		ArrayList<String> list = new ArrayList<String>();
		for (String beanName : flowType.getDatamodel().getData()){
			list.add(beanName);
		}
		// create state context
		StateContext sc = scf.create(flow, flowType);
		return new FlowInitializingData(list, sc);
	}

}
