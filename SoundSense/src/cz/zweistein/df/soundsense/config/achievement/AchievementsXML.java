package cz.zweistein.df.soundsense.config.achievement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
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

	private String achievementsProgressFilename = "achievementsProgress.xml";
	
	public AchievementsXML(String directory) throws SAXException, IOException {
		this.achievementPatterns = new LinkedList<AchievementPattern>();
		this.loadDirectory(directory);
		
		try {
			Properties savedProgress = new Properties();
			savedProgress.loadFromXML(new FileInputStream(new File(achievementsProgressFilename)));
			for (AchievementPattern ap : this.getAchievementPatterns()) {
				long hits = 0;
				
				hits = Long.parseLong(savedProgress.getProperty(ap.getLogPattern(), "0"));
				
				ap.setHits(hits);
			}
		} catch (FileNotFoundException e) {
			// we dont care
		}
	}
	
	public void save() {
		try {
			Properties savedProgress = new Properties();
			for (AchievementPattern ap : this.getAchievementPatterns()) {
				savedProgress.setProperty(ap.getLogPattern(), Long.toString(ap.getHits()));
			}
			savedProgress.storeToXML(new FileOutputStream(new File(achievementsProgressFilename)), null);
		} catch (IOException e) {
			logger.info("Error while saving achievemnt progress: " + e.toString());
		}
	}
	
	private void loadDirectory(String directory) {
		if (!(directory.substring(directory.length()-1).equals("\\") || directory.substring(directory.length()-1).equals("/"))) {
			directory = directory+"/";
		}
		logger.fine("Scanning directory '"+directory+"'.");
		File dir = new File(directory);
		
		String[] files = dir.list();
		
		if (files == null) {
			logger.info("'"+directory+"' is empty or invalid? Ignoring.");
		} else {
		
			Arrays.sort(files);
			for (int i = 0; i < files.length; i++) {
				
				String fileName = files[i];
				
				if (new File(directory+fileName).isDirectory()) {
					this.loadDirectory(directory+fileName+"/");
				} else if (fileName.endsWith(".xml")) {
				
					try {
						logger.info("Loading config "+directory+fileName);
						this.loadFile(directory+fileName);
					} catch (Exception e) {
						logger.severe("Failed to load "+ fileName + ": " + e.toString());
					}
				
				} else {
					logger.finest("'"+fileName+"' is not configuration file.");
				}
			}
			
		}
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
			
			Collections.sort(achievements);
			
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
