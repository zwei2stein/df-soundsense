package cz.zweistein.df.soundsense.config.achievement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cz.zweistein.df.soundsense.config.XMLConfig;
import cz.zweistein.df.soundsense.util.log.LoggerSource;

public class AchievementsXML extends XMLConfig {
	private static Logger logger = LoggerSource.logger;
	
	private List<AchievementPattern> achievementPatterns;
	
	public AchievementsXML(String fileName) throws SAXException, IOException {
		this.achievementPatterns = new LinkedList<AchievementPattern>();
		this.loadFile(fileName);
	}
	
	private void loadFile(String fileName) throws SAXException, IOException {
		
		Document doc = parseDoc(fileName);
		
		NodeList soundTags = doc.getElementsByTagName("achievementPattern");
		
		for (int i = 0; i < soundTags.getLength(); i++) {
			Node achievementPatternNode = soundTags.item(i);
			
			String logPattern = null;
			Node logPatternNode = achievementPatternNode.getAttributes().getNamedItem("logPattern");
			if (logPatternNode != null) {
				logPattern = logPatternNode.getNodeValue();
			}
			
			List<Achievement> achievements = new ArrayList<Achievement>();
			
			NodeList achievementPatternElements = achievementPatternNode.getChildNodes();
			for (int j = 0; j < achievementPatternElements.getLength(); j++) {
				Node configNode = achievementPatternElements.item(j);
				String name = configNode.getLocalName();
				
				if ("achievement".equals(name)) {
					Achievement achievement = this.parseAchievement(configNode, fileName);
					if (achievement != null) {
						achievements.add(achievement);
					}
				}
			}
			
			if (logPattern == null || logPattern.length() == 0) {
				logger.info("Achievement does not have logPattern, ignoring.");
			} else {
				this.achievementPatterns.add(new AchievementPattern(logPattern, achievements));
				logger.finest("Added achievement for "+logPattern);
			}
		}
		
	}
	
	private Achievement parseAchievement(Node configNode, String fileName) {
		String image =  parseStringAtribute(configNode, "image", null);
		if (image != null) {
			image = new File(fileName).getParent() + "/" + image;
		}
		String title = configNode.getAttributes().getNamedItem("title").getNodeValue();
		String description = configNode.getAttributes().getNamedItem("description").getNodeValue();
		
		long triggerAmount = parseLongAtribute(configNode, "triggerAmount", 1l);
		
		return new Achievement(triggerAmount, title, description, image);
	}

	public List<AchievementPattern> getAchievementPatterns() {
		return this.achievementPatterns;
	}
	
}
