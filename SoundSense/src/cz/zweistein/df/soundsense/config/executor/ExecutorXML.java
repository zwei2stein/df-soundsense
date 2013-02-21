package cz.zweistein.df.soundsense.config.executor;

import java.util.List;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cz.zweistein.df.soundsense.config.XMLConfig;
import cz.zweistein.df.soundsense.util.log.LoggerSource;

public class ExecutorXML extends XMLConfig{
	private static Logger logger = LoggerSource.logger;
	
	private List<Executor> executors;

	public ExecutorXML(String fileName) throws SAXException, IOException {
		this.executors = new LinkedList<Executor>();
		this.loadFile(fileName);
	}
	
	private void loadFile(String fileName) throws SAXException, IOException {
		
		Document doc = parseDoc(fileName);
		
		NodeList executorNodes = doc.getElementsByTagName("executor");
		
		for (int i = 0; i < executorNodes.getLength(); i++) {
			Node executorNode = executorNodes.item(i);
			
			String logPattern = parseStringAtribute(executorNode, "logPattern", null);
			if (logPattern == null) {
				logger.info("Executor missing required attribute logPattern.");
				continue;
			}
			
			String call = parseStringAtribute(executorNode, "call", null);
			if (call == null) {
				logger.info("Executor missing required attribute call.");
				continue;
			}
			
			executors.add(new Executor(logPattern, call));
			
		}
		
	}

	public List<Executor> getExecutors() {
		return this.executors;
	}

}
