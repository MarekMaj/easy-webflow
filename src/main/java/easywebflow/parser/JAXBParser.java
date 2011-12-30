package easywebflow.parser;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import easywebflow.config.FlowType;
import easywebflow.configuration.Configuration;

public final class JAXBParser {

	@SuppressWarnings("unchecked")
	public static Object parseFile(String filePath) {
		
		try {
			// create unmarshaller
			JAXBContext jc = JAXBContext.newInstance("easywebflow.config");
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			//Setting the Validation
			SchemaFactory schemaFactory = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
			// get schema from .xsd located in framework-[version].jar
			Schema schema = schemaFactory.newSchema(JAXBParser.class.getClassLoader().getResource("easywebflow-flow.xsd"));
			unmarshaller.setSchema(schema);
			// TODO logging should be introduced
			System.out.println("Loading SCXML file: " + filePath);
			JAXBElement<FlowType> obj = (JAXBElement<FlowType>) unmarshaller.unmarshal(new File(filePath));
			return obj.getValue();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
