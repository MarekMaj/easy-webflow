package easywebflow.configuration;

import java.util.HashMap;

import easywebflow.config.FlowType;
import easywebflow.security.SecurityConstraint;

public final class Configuration {

	/* Musze miec, zalozenia:
	 *  - czy uzywam XMl czy mojego formatu
	 *  - sciezke do pliku
	 *  - czy eksport do XML
	 *  - timeout dla flowów!!
	 *  - nazwa paramteru w URL zamiast cid
	 *  - plik config zawsze w pliku WEB-INF aplikacji
	 *  - jezeli bede zwracal juz gotowe pliki config, MainFactory nie musi nic wiedziec o tym skad pochodzi konfiguracja 
	 *  i jest niezalezna od pakietu parser, XMLparser itp OK
	 */  
	
	
	private static final Object configFile = ConfigurationProcessor.processConfigurationFile();
	private static final HashMap<String, String> configAttributes = ConfigurationProcessor.getConfigMap(configFile);
	private static final HashMap<String, FlowType> flows = ConfigurationProcessor.getFlows(configFile);
	private static final HashMap<StateIdentifier, SecurityConstraint> securedStatesMap = ConfigurationProcessor.getSecuredStates(flows);
	private static final HashMap<String, StateIdentifier> viewToStateMap = ConfigurationProcessor.getViewToState(flows);
	private static final HashMap<StateIdentifier, String> stateToViewMap = ConfigurationProcessor.reverseMap(viewToStateMap);
	private static final Configuration instance = new Configuration();
	
	private Configuration(){}
	
	public static Configuration getInstance(){
		return instance;
	}

	public static HashMap<String, String> getConfigAttributes() {
		return configAttributes;
	}

	public static String getConfigAttributeByName(String name) {
		return configAttributes.get(name);
	}
	
	public static HashMap<String, FlowType> getFlows() {
		return flows;
	}

	public static HashMap<StateIdentifier, SecurityConstraint> getSecuredStatesMap() {
		if (configAttributes.get("security").equalsIgnoreCase("false") || securedStatesMap == null){
			return new HashMap<StateIdentifier, SecurityConstraint>();
		}
		return securedStatesMap;
	}

	public static HashMap<String, StateIdentifier> getViewToStateMap() {
		return viewToStateMap;
	}

	public static HashMap<StateIdentifier, String> getStateToViewMap() {
		return stateToViewMap;
	}
	

}
