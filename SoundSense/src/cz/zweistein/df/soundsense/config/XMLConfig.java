package cz.zweistein.df.soundsense.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;

import cz.zweistein.df.soundsense.util.log.LoggerSource;

public class XMLConfig {
	private static Logger logger = LoggerSource.logger;

	public String parseStringAtribute(Node node, String atribute, String defaultValue) {
		String string = defaultValue;
		Node stringNode = node.getAttributes().getNamedItem(atribute);
		if (stringNode != null) {
			string = stringNode.getNodeValue();
		}
		return string;
	}

	public Long parseLongAtribute(Node node, String atribute, Long defaultValue) {
		Long value = defaultValue;
		if (node.getAttributes().getNamedItem(atribute) != null) {
			String numberText = node.getAttributes().getNamedItem(atribute).getNodeValue();
			try {
				value = Long.parseLong(numberText);
			} catch (NumberFormatException e) {
				logger.info(atribute + " '" + numberText + "' is not recognized as a number.");
			}
		}
		return value;
	}

	public Float parseFloatAttribute(Node node, String atribute, Float defaultValue) {
		Float value = null;
		if (node.getAttributes().getNamedItem(atribute) != null) {
			String numberText = node.getAttributes().getNamedItem(atribute).getNodeValue();
			try {
				value = Float.parseFloat(numberText);
			} catch (NumberFormatException e) {
				logger.info(atribute + " '" + numberText + "' is not recognized as a floating point number, using default " + defaultValue + ".");
				return defaultValue;
			}
		}
		return value;
	}
	
	protected boolean parseBooleanTag(String nodeName, Element nodes) {
		Node node = nodes.getElementsByTagName(nodeName).item(0);
		String attributeText = node.getAttributes().getNamedItem("value").getNodeValue();
		return Boolean.parseBoolean(attributeText);
	}

	public boolean parseBooleanAttribute(Node node, String nodeName, boolean defaultValue) {
		boolean result = defaultValue;
		if (node.getAttributes().getNamedItem(nodeName) != null) {
			String booleanText = node.getAttributes().getNamedItem(nodeName).getNodeValue();
			if ("true".equals(booleanText)) {
				result = true;
			} else if ("false".equals(booleanText)) {
				result = false;
			} else {
				logger.fine(nodeName + " '" + booleanText + "' not recognized, using default " + defaultValue + ".");
			}
		}
		return result;
	}

	public Document parseDoc(String fileName) throws SAXException, IOException {

		Document doc = null;
		InputSource source = new InputSource(new FileInputStream(fileName));
		DOMParser parser = new DOMParser();
		parser.parse(source);
		doc = parser.getDocument();

		return doc;
	}

}
