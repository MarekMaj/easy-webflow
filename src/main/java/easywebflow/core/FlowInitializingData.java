package easywebflow.core;

import java.util.ArrayList;

import easywebflow.state.StateContext;

public class FlowInitializingData {

	private ArrayList<String> initData;
	private StateContext stateContext;

	public FlowInitializingData(ArrayList<String> initData, StateContext stateContext) {
		super();
		this.initData = initData;
		this.stateContext = stateContext;
	}

	public ArrayList<String> getInitData() {
		return initData;
	}

	public StateContext getStateContext() {
		return stateContext;
	}

}
