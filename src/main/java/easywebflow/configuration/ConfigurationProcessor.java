package easywebflow.configuration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import easywebflow.config.*;
import easywebflow.parser.JAXBParser;
import easywebflow.security.SecurityConstraint;


public final class ConfigurationProcessor {

	// Suppress default constructor for noninstantiability
	private ConfigurationProcessor(){
		throw new AssertionError();
	}
	
	private static String getMainPath(){
		String WEBINF = "WEB-INF";
		String filePath = "";
		URL url = Configuration.class.getResource("ConfigurationProcessor.class");
		String className = url.getFile();

	    filePath = className.substring(className.indexOf('/'), className.indexOf(WEBINF) + WEBINF.length());
	    return filePath;
	}
	
	static Element processConfigurationFile(){
		Element elem = null;
		try {
			String source = getMainPath() + "/easywebflow-config.xml";
			// TODO logging should be introduced
			System.out.println("Initializing Easy-Webflow Framework from config file: " + source);
			SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
			// get schema from .xsd located in framework-[version].jar
			//System.out.println(Configuration.class.getProtectionDomain().getCodeSource().getLocation())
			Schema schema = factory.newSchema(Configuration.class.getClassLoader().getResource("easywebflow-config.xsd"));
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(source));
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();
			Document doc = builder.parse(source);
			elem = doc.getDocumentElement();
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			System.out.println("zly format");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return elem;
	}
	
	static HashMap<String, String> getConfigMap(Object el){
		Element element = (Element) el;
		HashMap<String, String> map = new HashMap<String, String>();
	
		// if element is missing it is 'true' by default
		map = putElement(element, map, "security", "true");
		map = putElement(element, map, "login-page", "/login.xhtml");
		map = putElement(element, map, "navigation", "true");
		
		return map;
	}
	
	private static HashMap<String, String> putElement(Element element, HashMap<String, String> map, String elementName, String defaultValue){
		// if element is missing it is 'true' by default
		map.put(elementName, (element.getElementsByTagName(elementName).getLength() != 0) 
				? new String(element.getElementsByTagName(elementName).item(0).getTextContent()) 
						: new String(defaultValue));

		return map;
	}
	
	private static HashMap<String, String> getFlowsConfigMap(Element element){
		HashMap<String, String> map = new HashMap<String, String>();
	
		// for all flow elements add location path
		NodeList flows = element.getElementsByTagName("flow");
		for (int i=0; i<flows.getLength(); i++)
			map.put(new String(flows.item(i).getAttributes().getNamedItem("name").getTextContent()), new String(flows.item(i).getAttributes().getNamedItem("location").getTextContent()));
		
		return map;
	}

	static HashMap<String, FlowType> getFlows(Object element){
		HashMap<String, String>configFlows = getFlowsConfigMap((Element)element);
		HashMap<String, FlowType> flows = new HashMap<String, FlowType>();

		try {
			for ( Map.Entry<String,String> flowConfig : configFlows.entrySet()){
				String path = getMainPath() + flowConfig.getValue();
				// TODO parser adapter
				// FlowType flow = (FlowType) (map.get("useXML").equals("false") ? PatternRecognizer.parseFile(path) : JAXBParser.parseFile(path));
				FlowType flow = (FlowType) JAXBParser.parseFile(path);
				// add to hashMap with config name 
				flows.put(flowConfig.getKey(), flow);
;			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flows;	
	}

	@SuppressWarnings("unchecked")
	public static HashMap<StateIdentifier, SecurityConstraint> getSecuredStates(Object config) {
		HashMap<StateIdentifier, SecurityConstraint> sec = new HashMap<StateIdentifier,SecurityConstraint>();
		HashMap<String, FlowType> map = (HashMap<String, FlowType>) config;
		
		for (FlowType ft :map.values()){
			ArrayList<String> flowRoles = null;
			// If flow is secured then all states without specified security attribute are secured with that roles
			if (ft.getDatamodel().getSecured() != null){
				if (ft.getDatamodel().getSecured().getRoleName().isEmpty())
					flowRoles = new ArrayList<String>();
				else
					flowRoles = new ArrayList<String>(ft.getDatamodel().getSecured().getRoleName());
			}
			
			for (StateType st: ft.getState()){
				StateIdentifier sId = new StateIdentifier(ft.getName(), st.getId());
				ArrayList<String> secList = null;
				
				if (st.getDatamodel()!= null && st.getDatamodel().getSecured() != null){
					// if list is empty then state is unavailable for anyone
					if (st.getDatamodel().getSecured().getRoleName().isEmpty())
						secList = new ArrayList<String>();
					// otherwise state is secured and given roles are permitted
					else
						secList = new ArrayList<String>(st.getDatamodel().getSecured().getRoleName());
				}
				
				// state inherits flow constraints if no constraints are specified on state level
				// otherwise flow constraints are omitted 
				// if list is null then state inherits flow constraints
				if (secList == null && flowRoles != null)
					secList = new ArrayList<String>(flowRoles);
				
				
				sec.put(sId, new SecurityConstraint(st.getId(), secList));
			}
		}
		
		return sec;
	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, StateIdentifier> getViewToState(Object config) {
		HashMap<String, FlowType> map = (HashMap<String, FlowType>) config; 
		HashMap<String, StateIdentifier> views = new HashMap<String,StateIdentifier>();
		
		for (FlowType ft: map.values()){
			for (StateType st: ft.getState()){
				// if viewId specified
				if (st.getDatamodel() != null && st.getDatamodel().getViewId() != null)
					views.put(st.getDatamodel().getViewId(), new StateIdentifier(ft.getName(), st.getId()));
				// otherwise viewId = flowName/stateId
				else 
					views.put(new String(ft.getName()+'/'+st.getId()), new StateIdentifier(ft.getName(), st.getId()));
			}
		}
		return views;
	}

	public static HashMap<StateIdentifier, String> reverseMap(HashMap<String, StateIdentifier> reversed) {
		HashMap<String, StateIdentifier> map = reversed; 
		HashMap<StateIdentifier, String> states = new HashMap<StateIdentifier, String>();
		
		for (Map.Entry<String, StateIdentifier> entry: map.entrySet())
			states.put(entry.getValue(), entry.getKey());
			
		return states;	
	}
}
