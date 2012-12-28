package cz.zweistein.df.soundsense.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;

import cz.zweistein.df.soundsense.gui.control.Threshold;
import cz.zweistein.df.soundsense.util.log.LoggerSource;

public class SoundsXML {
	private static Logger logger = LoggerSource.logger;
	
	private List<Sound> sounds;
	private String rootDirectory;
	private boolean ignoreEmptySounds;
	private boolean noWarnAbsolutePath;
	
	private List<String> parsedDirectories;

	private List<IReloadProgressCallback> reloadProgressCallbacks;
	
	public SoundsXML(String directory, boolean ignoreEmptySounds, boolean noWarnAbsolutePath) {
		this.rootDirectory = directory;
		this.sounds = new LinkedList<Sound>();
		this.reloadProgressCallbacks = new LinkedList<IReloadProgressCallback>();
		this.ignoreEmptySounds = ignoreEmptySounds;
		this.noWarnAbsolutePath = noWarnAbsolutePath;
		this.parsedDirectories = new LinkedList<String>();
		
		this.loadDirectory(this.rootDirectory, ignoreEmptySounds);
		for (IReloadProgressCallback reloadProgressCallback: this.reloadProgressCallbacks) {
			reloadProgressCallback.done();
		}
	}

	public SoundsXML(String directory) {
		this(directory, false, false);
	}

	public SoundsXML(String directory, boolean ignoreEmptySounds) {
		this(directory, ignoreEmptySounds, false);
	}
	
	public void reload() {
		this.sounds = new ArrayList<Sound>(this.getSounds().size());
		this.parsedDirectories = new ArrayList<String>(this.parsedDirectories.size());
		this.loadDirectory(this.rootDirectory, this.ignoreEmptySounds);
		for (IReloadProgressCallback reloadProgressCallback: this.reloadProgressCallbacks) {
			reloadProgressCallback.done();
		}
	}
	
	private void loadDirectory(String directory, boolean ignoreEmptySounds) {
		if (!(directory.substring(directory.length()-1).equals("\\") || directory.substring(directory.length()-1).equals("/"))) {
			directory = directory+"/";
		}
		logger.fine("Scanning directory '"+directory+"'.");
		File dir = new File(directory);
		
		if (parsedDirectories.contains(dir.getAbsolutePath())) {
			logger.info("ignoring already parsed directory to prevent infinite loop");
		} else {
			String[] files = dir.list();
			
			if (files == null) {
				logger.info("'"+directory+"' is empty or invalid? Ignoring.");
			} else {
				this.parsedDirectories.add(new String(dir.getAbsolutePath()));
			
				Arrays.sort(files);
				for (int i = 0; i < files.length; i++) {
					
					String fileName = files[i];
					
					if (new File(directory+fileName).isDirectory()) {
						this.loadDirectory(directory+fileName+"/", ignoreEmptySounds);
					} else if (fileName.endsWith(".xml")) {
					
						try {
							logger.info("Loading config "+directory+fileName);
							this.loadFile(directory+fileName, ignoreEmptySounds);
						} catch (Exception e) {
							logger.severe("Failed to load "+ fileName + ": " + e.toString());
						}
					
					} else {
						logger.finest("'"+fileName+"' is not configuration file.");
					}
				}
			}
		}
	}
	
	private void loadFile(String fileName, boolean ignoreEmptySounds) throws SAXException, IOException {
		
		Document doc = null;
		InputSource source = new InputSource(new FileInputStream(fileName));
		DOMParser parser = new DOMParser();
		parser.parse(source);
		doc = parser.getDocument();
		
		NodeList rootElement = doc.getElementsByTagName("sounds");
		String defaultAnsiFormat = null;
		if (rootElement.getLength() > 0) {
			defaultAnsiFormat = parseStringAtribute(rootElement.item(0), "defaultAnsiFormat", null);
		}
		
		NodeList soundTags = doc.getElementsByTagName("sound");
		parseSounds(soundTags, fileName, ignoreEmptySounds, defaultAnsiFormat);
		
		// add check for directory/listing references here?
		NodeList directoryReferences = doc.getElementsByTagName("includeDirectory");
		parseDirectories(directoryReferences);
		
		NodeList listings = doc.getElementsByTagName("includeListing");
		parseListings(listings);
		
		for (IReloadProgressCallback reloadProgressCallback: this.reloadProgressCallbacks) {
			reloadProgressCallback.tick();
		}
		
		// or add them here??
	}

	
	private String parseStringAtribute(Node node, String atribute, String defaultValue) {
		String string = defaultValue;
		Node stringNode = node.getAttributes().getNamedItem(atribute);
		if (stringNode != null) {
			string = stringNode.getNodeValue();
		}
		return string;
	}
	
	private Long parseLongAtribute(Node node, String atribute, Long defaultValue) {
		Long value = defaultValue;
		if (node.getAttributes().getNamedItem(atribute) != null) {
			String numberText = node.getAttributes().getNamedItem(atribute).getNodeValue();
			try {
				value = Long.parseLong(numberText);
			} catch (NumberFormatException e) {
				logger.info(atribute+" '"+numberText+"' is not recognized as a number.");
			}
		}
		return value;
	}
	
	private Float parseFloatAttribute(Node node, String atribute) {
		Float value = null;
		if (node.getAttributes().getNamedItem(atribute) != null) {
			String numberText = node.getAttributes().getNamedItem(atribute).getNodeValue();
			try {
				value = Float.parseFloat(numberText);
			} catch (NumberFormatException e) {
				logger.info(atribute+" '"+numberText+"' is not recognized as a floating point number.");
			}
		}
		return value;
	}
	
	private boolean parseBooleanAttribute(Node node, String nodeName, boolean defaultValue) {
		boolean result = defaultValue;
		if (node.getAttributes().getNamedItem(nodeName) != null) {
			String booleanText = node.getAttributes().getNamedItem(nodeName).getNodeValue();
			if ("true".equals(booleanText)) {
				result = true;
			} else if ("false".equals(booleanText)) {
				result = false;
			} else {
				logger.fine(nodeName+" '"+booleanText+"' not recognized, using default "+defaultValue+".");
			}
		}
		return result;
	}

	private void parseSounds(NodeList soundTags, String fileName, boolean ignoreEmptySounds, String defaultAnsiFormat) {
		for (int i = 0; i < soundTags.getLength(); i++) {
			Node soundNode = soundTags.item(i);
			
			String logPattern = parseStringAtribute(soundNode, "logPattern", null);
			
			if (logPattern == null) {
				logger.info("Sound tag without 'logPattern' attribute encountered.");
			}
			
			String channel = parseStringAtribute(soundNode, "channel", null);

			String ansiFormat = parseStringAtribute(soundNode, "ansiFormat", defaultAnsiFormat);
			
			Loop channelLoop = null;
			if (soundNode.getAttributes().getNamedItem("loop") != null) {
				String loop = soundNode.getAttributes().getNamedItem("loop").getNodeValue();
				if ("true".equals(loop) || "start".equals(loop)) {
					channelLoop = Loop.START_LOOPING;
				} else if ("stop".equals(loop) ) {
					channelLoop = Loop.STOP_LOOPING;
				} else {
					logger.fine("Loop parameter '"+loop+"' not recognized.");
				}
			}
			
			boolean haltOnMatch = parseBooleanAttribute(soundNode, "haltOnMatch", true);
			
			Long delay = parseLongAtribute(soundNode, "delay", null);

			Long concurency = parseLongAtribute(soundNode, "concurency", null);
			
			Long timeout = parseLongAtribute(soundNode, "timeout", null);

			Long propability = parseLongAtribute(soundNode, "propability", null);
			
			long playbackThreshhold = parseLongAtribute(soundNode, "playbackThreshhold", Threshold.FLUFF.getValue());

			List<SoundFile> soundFiles = new ArrayList<SoundFile>();
			
			NodeList soundElements = soundNode.getChildNodes();
			for (int j = 0; j < soundElements.getLength(); j++) {
				Node configNode = soundElements.item(j);
				String name = configNode.getLocalName();
				
				if ("soundFile".equals(name)) {
					SoundFile soundFile = this.parseSoundFile(configNode, fileName);
					if (soundFile != null) {
						soundFiles.add(soundFile);
					}
				}
			}
			
			if (logPattern == null || logPattern.length() == 0) {
				logger.info("Sound does not have logPattern, ignoring.");
			} else if ((soundFiles.size() == 0 && ignoreEmptySounds) && (channelLoop != Loop.STOP_LOOPING)) {
				logger.fine("Sound for '"+logPattern+"' does not contain any soundFiles and is not speech or end of loop, ignoring.");
			} else {
				this.sounds.add(new Sound(fileName, soundFiles, logPattern, ansiFormat, channelLoop, channel, concurency, haltOnMatch, timeout, delay, propability, playbackThreshhold));
				logger.finest("Added sound for "+logPattern);
			}
		}

	}

	private SoundFile parseSoundFile(Node configNode, String fileName) {
		
		Node fileNameAttribute = configNode.getAttributes().getNamedItem("fileName");
		if (fileNameAttribute == null) {
			logger.info("Could not locate 'fileName' attribute of a sound element, please check spelling.");
			return null;
		}
		
		String soundFile = new File(fileName).getParent()+"/"+fileNameAttribute.getNodeValue();
		// validate that wave file exists.
		if (!new File(soundFile).exists()) {
			// we did not find file on relative path respecting location of parent xml, lets see if it is on absolute path
			if (new File(fileNameAttribute.getNodeValue()).exists()) {
				soundFile = fileNameAttribute.getNodeValue();
				if (!this.noWarnAbsolutePath) {
					logger.info("File " + soundFile + " is on absolute path.");
				}
			} else {
				logger.warning("Did not find " + soundFile + ", ignoring.");
				return null;
			}
		}
		
		int weight = 100;
		if (configNode.getAttributes().getNamedItem("weight") != null) {
			String weightText = configNode.getAttributes().getNamedItem("weight").getNodeValue();
			try {
				weight = Integer.parseInt(weightText);
			} catch (NumberFormatException e) {
				logger.info("Weight '"+weightText+"' for '"+soundFile+"' is not recognized as a number, using default "+weight+".");
			}
		}
		
		Float volumeAdjustment = parseFloatAttribute(configNode, "volumeAdjustment");
		if (volumeAdjustment == null) {
			volumeAdjustment = 0f;
		}
		
		Float balanceAdjustment = parseFloatAttribute(configNode, "balanceAdjustment");

		boolean randomBalance = parseBooleanAttribute(configNode, "randomBalance", false);

		return new SoundFile(soundFile, weight, volumeAdjustment, balanceAdjustment, randomBalance);
	}

	private void parseDirectories(NodeList directoryReferences) {
		for (int i = 0; i < directoryReferences.getLength(); i++) {
			Node directoryNode = directoryReferences.item(i);
			
			String directoryReference = parseStringAtribute(directoryNode, "path", null);
			
			if (directoryReference == null) {
				logger.info("Directory reference tag without 'path' attribute encountered.");
			} else {
				loadDirectory(directoryReference,this.ignoreEmptySounds);
			}
		}
	}

	private void parseListings(NodeList listings) throws SAXException, IOException {
		for (int i = 0; i < listings.getLength(); i++) {
			Node listingNode = listings.item(i);
			
			String filePath = parseStringAtribute(listingNode, "filePathAndName", null);
			
			if (filePath == null || !filePath.matches(".+\\.xml")) {
				logger.info("Include listing tag without valid 'filePathAndName' attribute encountered (make sure it ends in '.xml'!).");
			} else {
				logger.info("Loading included config "+filePath);
				loadFile(filePath,this.ignoreEmptySounds);
			}
		}
	}

	public List<Sound> getSounds() {
		return this.sounds;
	}

	public List<String> getXMLFiles() {
		List<String> xmlFiles = new ArrayList<String>();
		for (Sound sound: this.sounds) {
			if (!xmlFiles.contains(sound.getParentFile())) {
				xmlFiles.add(sound.getParentFile());
			}
		}
		return xmlFiles;
	}
	
	public List<Sound> getSoundsByXMLFile(String xmlFile) {
		List<Sound> sounds = new ArrayList<Sound>();
		for (Sound sound: this.sounds) {
			if (xmlFile.equals(sound.getParentFile())) {
				sounds.add(sound);
			}
		}
		return sounds;
	}

	@Override
	public String toString() {
		return this.rootDirectory;
	}

	public void registerReloadProgressCallback(IReloadProgressCallback reloadProgressCallback) {
		this.reloadProgressCallbacks.add(reloadProgressCallback);
	}
	
}
