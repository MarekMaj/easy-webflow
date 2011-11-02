package easywebflow.configuration;

import java.io.*;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.*;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;
import org.w3c.dom.*;
import org.xml.sax.*;

import easywebflow.parser.*;

public class Configuration {

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
	 
	private Configuration(){}
	
	public static Object getFlows(){
		return Configuration.processConfig();
	}
	
	private static String getMainPath(){
		String WEBINF = "WEB-INF";
		String filePath = "";
		URL url = Configuration.class.getResource("Configuration.class");
		String className = url.getFile();

	    filePath = className.substring(className.indexOf('/'), className.indexOf(WEBINF) + WEBINF.length());
	    return filePath;
	}
	
	private static HashMap<String, String> getConfigMap(){
		HashMap<String, String> map = new HashMap<String, String>();
	
		try {
			String source = getMainPath() + "/config.xml";
			System.out.println("Initializing Easy-Webflow Framework from file: " + source);
			SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
			// TODO: zamienic na URI
			Schema schema = factory.newSchema(new File("/dane/work/inz/framework/src/main/resources/config-schema.xsd"));
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(source));
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();
			Document doc = builder.parse(source);
			Element element = doc.getDocumentElement();
			
			map.put("useXML", element.getElementsByTagName("useXML").item(0).getTextContent());
			map.put("configFilePath", element.getElementsByTagName("configFilePath").item(0).getTextContent());
			
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

		return map;
	}
	
	private static Object processConfig(){
		HashMap<String, String> map = getConfigMap();
		Object ret = null;

		try {
			String path = getMainPath() + map.get("configFilePath");
			ret = map.get("useXML").equals("false") ? PatternRecognizer.parseFile(path) : XMLParser.parseFile(path);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;	
	}
	
}
