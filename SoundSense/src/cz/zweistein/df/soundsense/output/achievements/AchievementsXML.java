package cz.zweistein.df.soundsense.output.achievements;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;

import cz.zweistein.df.soundsense.util.log.LoggerSource;

public class AchievementsXML {
	private static Logger logger = LoggerSource.logger;
	
	private List<AchievementPattern> achievementPatterns;
	
	public AchievementsXML(String fileName) throws SAXException, IOException {
		this.achievementPatterns = new LinkedList<AchievementPattern>();
		this.loadFile(fileName);
	}
	
	private void loadFile(String fileName) throws SAXException, IOException {
		
		Document doc = null;
		InputSource source = new InputSource(new FileInputStream(fileName));
		DOMParser parser = new DOMParser();
		parser.parse(source);
		doc = parser.getDocument();
		
		NodeList soundTags = doc.getElementsByTagName("achievementPattern");
		
		for (int i = 0; i < soundTags.getLength(); i++) {
			Node achievementPatternNode = soundTags.item(i);
			
			String logPattern = null;
			Node logPatternNode = achievementPatternNode.getAttributes().getNamedItem("logPattern");
			if (logPatternNode != null) {
				logPattern = logPatternNode.getNodeValue();
			}
			
			List<Achievement> soundFiles = new ArrayList<Achievement>();
			
			NodeList achievementPatternElements = achievementPatternNode.getChildNodes();
			for (int j = 0; j < achievementPatternElements.getLength(); j++) {
				Node configNode = achievementPatternElements.item(j);
				String name = configNode.getLocalName();
				
				if ("achievement".equals(name)) {
					Achievement soundFile = this.parseSoundFile(configNode);
					if (soundFile != null) {
						soundFiles.add(soundFile);
					}
				}
			}
			
			if (logPattern == null || logPattern.length() == 0) {
				logger.info("Achievement does not have logPattern, ignoring.");
			} else {
				logPattern.charAt(1);
				this.achievementPatterns.add(new AchievementPattern(logPattern, soundFiles));
				logger.finest("Added achievement for "+logPattern);
			}
		}
		
	}
	
	private Achievement parseSoundFile(Node configNode) {
		String image = configNode.getAttributes().getNamedItem("image").getNodeValue();
		String title = configNode.getAttributes().getNamedItem("title").getNodeValue();
		String description = configNode.getAttributes().getNamedItem("description").getNodeValue();
		
		int triggerAmount = 1;
		if (configNode.getAttributes().getNamedItem("triggerAmount") != null) {
			String triggerAmountText = configNode.getAttributes().getNamedItem("triggerAmount").getNodeValue();
			try {
				triggerAmount = Integer.parseInt(triggerAmountText);
			} catch (NumberFormatException e) {
				logger.info("triggerAmount '"+triggerAmountText+"' is not recognized as a number, using default "+triggerAmount+".");
			}
		}
		
		return new Achievement(triggerAmount, title, description, image);
	}

	public List<AchievementPattern> getAchievementPatterns() {
		return this.achievementPatterns;
	}
	
}
