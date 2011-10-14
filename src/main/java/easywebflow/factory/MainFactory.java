package easywebflow.factory;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;

import easywebflow.core.Flow;
import easywebflow.config.FlowConfig;
import easywebflow.parser.PatternRecognizer;
import easywebflow.state.StateContext;

public class MainFactory {
	
	private static StateContextFactory scf = new StateContextFactory();
	
	private static MainFactory instance;
	private static HashMap<String, FlowConfig> flows;
	/*
	public static void main(String[] args) throws FileNotFoundException{ 
		MainFactory main = new MainFactory();
		main.parseConfig();
	}*/

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
		try {
			flows = PatternRecognizer.parseFile(getConfigFilePath());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getConfigFilePath(){
		String WEBINF = "WEB-INF";
		String filePath = "";
		URL url = MainFactory.class.getResource("MainFactory.class");
		String className = url.getFile();

	    filePath = className.substring(className.indexOf('/'), className.indexOf(WEBINF) + WEBINF.length());
	    //System.out.println("czytam z: " + new String(filePath + "/flow"));
	    return new String(filePath + "/flow");
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
