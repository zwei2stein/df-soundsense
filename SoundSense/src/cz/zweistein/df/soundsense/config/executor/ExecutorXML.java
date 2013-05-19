package cz.zweistein.df.soundsense.config.executor;

import java.util.Arrays;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cz.zweistein.df.soundsense.config.XMLConfig;
import cz.zweistein.df.soundsense.util.log.LoggerSource;

public class ExecutorXML extends XMLConfig {
	private static Logger logger = LoggerSource.LOGGER;

	private List<Executor> executors;

	public ExecutorXML(String directory) throws SAXException, IOException {
		this.executors = new LinkedList<Executor>();
		this.loadDirectory(directory);
	}

	private void loadDirectory(String directory) {
		if (!(directory.substring(directory.length() - 1).equals("\\") || directory.substring(directory.length() - 1).equals("/"))) {
			directory = directory + "/";
		}
		logger.fine("Scanning directory '" + directory + "'.");
		File dir = new File(directory);

		String[] files = dir.list();

		if (files == null) {
			logger.info("'" + directory + "' is empty or invalid? Ignoring.");
		} else {

			Arrays.sort(files);
			for (int i = 0; i < files.length; i++) {

				String fileName = files[i];

				if (new File(directory + fileName).isDirectory()) {
					this.loadDirectory(directory + fileName + "/");
				} else if (fileName.endsWith(".xml")) {

					try {
						logger.info("Loading config " + directory + fileName);
						this.loadFile(directory + fileName);
					} catch (Exception e) {
						logger.severe("Failed to load " + fileName + ": " + e.toString());
					}

				} else {
					logger.finest("'" + fileName + "' is not configuration file.");
				}
			}

		}
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
