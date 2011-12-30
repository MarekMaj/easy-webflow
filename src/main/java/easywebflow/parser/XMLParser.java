package easywebflow.parser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import easywebflow.config_old.*;

public final class XMLParser {

	// Suppress default constructor for noninstantiability
	private XMLParser(){
		throw new AssertionError();
	}
	
	public static Object parseFile(String filename) throws IOException {
		return validateXMLFile(filename) ? parseFlows(getRootElement(filename)) : null;
	}

	private static Element getRootElement(String filename) throws IOException{
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();
			Document doc = builder.parse(filename);
			return doc.getDocumentElement();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}

	private static boolean validateXMLFile(String filename) throws IOException{
		Boolean ret = true;
		try {
			SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
			// TODO: zamienic na URI
			Schema schema = factory.newSchema(new File("/dane/work/inz/framework/src/main/resources/flow-schema.xsd"));
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(filename));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			ret = false;
			e.printStackTrace();
		}
		return ret;
	}
	
	private static HashMap<String, FlowConfig> parseFlows(Element root){
		HashMap<String, FlowConfig> map = new HashMap<String, FlowConfig>();
		
		NodeList flows = root.getElementsByTagName("flow");
		for (int i=0; i<flows.getLength(); i++){
			FlowConfig flowConfig = parseFlow((Element) flows.item(i)); 
			map.put(flowConfig.getFlowName(), flowConfig);
		}	
		return map;
	}

	private static FlowConfig parseFlow(Element flow) {
		FlowConfigBuilder fcb = new FlowConfigBuilder();
		fcb.setFlowName(flow.getAttribute("name"));
		
		NodeList states = flow.getElementsByTagName("state");
		for (int i=0; i<states.getLength(); i++){
				StateConfig stateConfig = parseState((Element) states.item(i));
				fcb.addStates(stateConfig);
		}
		return fcb.build();
	}

	private static StateConfig parseState(Element state) {
		StateConfigBuilder scb = new StateConfigBuilder();
		scb.setName(state.getAttribute("name"));
		scb.setViewName(state.hasAttribute("viewName") ? state.getAttribute("viewName"): null);
		
		NodeList transitions = state.getElementsByTagName("transition");
		for (int i=0; i<transitions.getLength(); i++){
			TransitionConfig transitionConfig = parseTransition((Element) transitions.item(i));
			scb.addTransitions(transitionConfig);
		}
		
		NodeList onStart = state.getElementsByTagName("start");
		for (int i=0; i<onStart.getLength(); i++){
			CommandConfig commandConfig = parseCommand((Element) onStart.item(i));
			scb.addOnStartCommands(commandConfig);
		}
		
		return scb.build();
	}

	private static TransitionConfig parseTransition(Element transition) {
		TransitionConfigBuilder tcb = new TransitionConfigBuilder();	
		
		NodeList nodes = transition.getChildNodes();
		for (int i=0; i<nodes.getLength(); i++){
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE){
				String tagName = ((Element) node).getTagName();
				if (tagName.equals("invoke")){
					CommandConfig commandConfig = parseCommand((Element) node);
					tcb.addOnTransition(commandConfig);
				}
				else if (tagName.equals("on")){
					tcb.setOn(node.getFirstChild().getNodeValue());
				}
				else if (tagName.equals("to")){
					tcb.setTo(node.getFirstChild().getNodeValue());
				}
				else if (tagName.equals("decision")){ 
					tcb = parseDecision(tcb, (Element) node);
				}
			}
			
		}
		return tcb.build();
	}

	private static TransitionConfigBuilder parseDecision(TransitionConfigBuilder tcb, Element decision) {
		
		NodeList nodes = decision.getChildNodes();
		for (int i=0; i<nodes.getLength(); i++){
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE){
				String tagName = ((Element) node).getTagName();
				if (tagName.equals("invoke")){
					CommandConfig commandConfig = parseCommand((Element) node);
					tcb.addOnDecision(commandConfig);
				}
				else if (tagName.equals("if")){
					String s = ((Element)node).getElementsByTagName("transitionTo").getLength() !=0 ? 
							((Element)node).getElementsByTagName("transitionTo").item(0).getNodeValue() : null;
					tcb.addIfz(parseIfCommand((Element)node), s);
				}
				else if (tagName.equals("elseTo")){
					tcb.setElseTo(node.getFirstChild().getNodeValue());
				}
			}
		}
		return tcb;
	}

	private static CommandConfig parseCommand(Element command) {
		CommandConfigBuilder ccb = new CommandConfigBuilder();
		
		NodeList nodes = command.getChildNodes();
		for (int i=0; i<nodes.getLength(); i++){
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE){
				String tagName = ((Element) node).getTagName();
				if (tagName.equals("beanName")){
					ccb.setBeanName(node.getFirstChild().getNodeValue());
				}
				else if (tagName.equals("methodName")){
					ccb.setMethodName(node.getFirstChild().getNodeValue());
				}
				else if (tagName.equals("resultName")){
					ccb.setResultName(node.getFirstChild().getNodeValue());
				}
				// TODO??	nazwy metod przy parametrach czy same beany ?
				else if (tagName.equals("param")){
					NodeList paramNodes = node.getChildNodes();
					for (int j=0; j<paramNodes.getLength(); j++){
						Node paramNode = paramNodes.item(j);
						if (paramNode.getNodeType() == Node.ELEMENT_NODE){
							String paramTagName = ((Element) paramNode).getTagName();
							if (paramTagName.equals("beanName")){
								ccb.addParamName(paramNode.getFirstChild().getNodeValue());
							}
						}
					}
				}
			}
		}
		return ccb.build();
	}

	private static CommandConfig parseIfCommand(Element ifCommand) {
		CommandConfigBuilder ccb = new CommandConfigBuilder();
		
		NodeList nodes = ifCommand.getChildNodes();
		for (int i=0; i<nodes.getLength(); i++){
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE){
				String tagName = ((Element) node).getTagName();
				if (tagName.equals("object")){
					ccb.setVariableName(node.getFirstChild().getNodeValue());
				}
				else if (tagName.equals("compareTo")){
					ccb.setVariableValue(node.getFirstChild().getNodeValue());
				}
			}
		}
		return ccb.build();
	}
}
