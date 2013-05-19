package cz.zweistein.df.soundsense.config.sounds;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cz.zweistein.df.soundsense.config.IReloadProgressCallback;
import cz.zweistein.df.soundsense.config.XMLConfig;
import cz.zweistein.df.soundsense.gui.control.Threshold;
import cz.zweistein.df.soundsense.util.log.LoggerSource;

public class SoundsXML extends XMLConfig {
	private static Logger logger = LoggerSource.LOGGER;

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
		for (IReloadProgressCallback reloadProgressCallback : this.reloadProgressCallbacks) {
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
		for (IReloadProgressCallback reloadProgressCallback : this.reloadProgressCallbacks) {
			reloadProgressCallback.done();
		}
	}

	private void loadDirectory(String directory, boolean ignoreEmptySounds) {
		if (!(directory.substring(directory.length() - 1).equals("\\") || directory.substring(directory.length() - 1).equals("/"))) {
			directory = directory + "/";
		}
		logger.fine("Scanning directory '" + directory + "'.");
		File dir = new File(directory);

		if (parsedDirectories.contains(dir.getAbsolutePath())) {
			logger.info("ignoring already parsed directory to prevent infinite loop");
		} else {
			String[] files = dir.list();

			if (files == null) {
				logger.info("'" + directory + "' is empty or invalid? Ignoring.");
			} else {
				this.parsedDirectories.add(new String(dir.getAbsolutePath()));

				Arrays.sort(files);
				for (int i = 0; i < files.length; i++) {

					String fileName = files[i];

					if (new File(directory + fileName).isDirectory()) {
						this.loadDirectory(directory + fileName + "/", ignoreEmptySounds);
					} else if (fileName.endsWith(".xml")) {

						try {
							logger.info("Loading config " + directory + fileName);
							this.loadFile(directory + fileName, ignoreEmptySounds);
						} catch (Exception e) {
							logger.severe("Failed to load " + fileName + ": " + e.toString());
						}

					} else {
						logger.finest("'" + fileName + "' is not configuration file.");
					}
				}
			}
		}
	}

	private void loadFile(String fileName, boolean ignoreEmptySounds) throws SAXException, IOException {

		Document doc = parseDoc(fileName);

		NodeList rootElement = doc.getElementsByTagName("sounds");
		String defaultAnsiFormat = null;
		boolean strictAttributions = false;
		if (rootElement.getLength() > 0) {
			defaultAnsiFormat = parseStringAtribute(rootElement.item(0), "defaultAnsiFormat", null);
			strictAttributions = parseBooleanAttribute(rootElement.item(0), "strictAttributions", false);
		}

		NodeList soundTags = doc.getElementsByTagName("sound");
		parseSounds(soundTags, fileName, ignoreEmptySounds, defaultAnsiFormat, strictAttributions);

		// add check for directory/listing references here?
		NodeList directoryReferences = doc.getElementsByTagName("includeDirectory");
		parseDirectories(directoryReferences);

		NodeList listings = doc.getElementsByTagName("includeListing");
		parseListings(listings);

		for (IReloadProgressCallback reloadProgressCallback : this.reloadProgressCallbacks) {
			reloadProgressCallback.tick();
		}

		// or add them here??
	}

	private void parseSounds(NodeList soundTags, String fileName, boolean ignoreEmptySounds, String defaultAnsiFormat, boolean strictAttributions) {
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
				} else if ("stop".equals(loop)) {
					channelLoop = Loop.STOP_LOOPING;
				} else {
					logger.fine("Loop parameter '" + loop + "' not recognized.");
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
						if (strictAttributions && soundFile.getAttributions().size() == 0) {
							logger.info("Sound file '" + soundFile.getFileName() + "' in '" + logPattern + "' lacks attributions!");
						}
					}
				}
			}

			if (logPattern == null || logPattern.length() == 0) {
				logger.info("Sound does not have logPattern, ignoring.");
			} else if ((soundFiles.size() == 0 && ignoreEmptySounds) && (channelLoop != Loop.STOP_LOOPING)) {
				logger.fine("Sound for '" + logPattern + "' does not contain any soundFiles and is not speech or end of loop, ignoring.");
			} else {
				this.sounds.add(new Sound(fileName, soundFiles, logPattern, ansiFormat, channelLoop, channel, concurency, haltOnMatch, timeout, delay,
						propability, playbackThreshhold));
				logger.finest("Added sound for " + logPattern);
			}
		}

	}

	private SoundFile parseSoundFile(Node soundFileNode, String fileName) {

		Node fileNameAttribute = soundFileNode.getAttributes().getNamedItem("fileName");
		if (fileNameAttribute == null) {
			logger.info("Could not locate 'fileName' attribute of a sound element, please check spelling.");
			return null;
		}

		String soundFileName = new File(fileName).getParent() + "/" + fileNameAttribute.getNodeValue();
		// validate that wave file exists.
		if (!new File(soundFileName).exists()) {
			// we did not find file on relative path respecting location of
			// parent xml, lets see if it is on absolute path
			if (new File(fileNameAttribute.getNodeValue()).exists()) {
				soundFileName = fileNameAttribute.getNodeValue();
				if (!this.noWarnAbsolutePath) {
					logger.info("File " + soundFileName + " is on absolute path.");
				}
			} else {
				logger.warning("Did not find " + soundFileName + ", ignoring.");
				return null;
			}
		}

		int weight = 100;
		if (soundFileNode.getAttributes().getNamedItem("weight") != null) {
			String weightText = soundFileNode.getAttributes().getNamedItem("weight").getNodeValue();
			try {
				weight = Integer.parseInt(weightText);
			} catch (NumberFormatException e) {
				logger.info("Weight '" + weightText + "' for '" + soundFileName + "' is not recognized as a number, using default " + weight + ".");
			}
		}

		Float volumeAdjustment = parseFloatAttribute(soundFileNode, "volumeAdjustment", null);
		if (volumeAdjustment == null) {
			volumeAdjustment = 0f;
		}

		Float balanceAdjustment = parseFloatAttribute(soundFileNode, "balanceAdjustment", null);

		boolean randomBalance = parseBooleanAttribute(soundFileNode, "randomBalance", false);

		SoundFile soundFile = new SoundFile(soundFileName, weight, volumeAdjustment, balanceAdjustment, randomBalance);
		parseAttributions(soundFile, soundFileNode.getChildNodes());

		return soundFile;
	}

	private void parseDirectories(NodeList directoryReferences) {
		for (int i = 0; i < directoryReferences.getLength(); i++) {
			Node directoryNode = directoryReferences.item(i);

			String directoryReference = parseStringAtribute(directoryNode, "path", null);

			if (directoryReference == null) {
				logger.info("Directory reference tag without 'path' attribute encountered.");
			} else {
				loadDirectory(directoryReference, this.ignoreEmptySounds);
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
				logger.info("Loading included config " + filePath);
				loadFile(filePath, this.ignoreEmptySounds);
			}
		}
	}

	private void parseAttributions(SoundFile soundFile, NodeList soundFileChildren) {
		for (int i = 0; i < soundFileChildren.getLength(); i++) {
			Node attributionNode = soundFileChildren.item(i);

			if ("attribution".equals(attributionNode.getLocalName())) {

				String url = parseStringAtribute(attributionNode, "url", null);
				String license = parseStringAtribute(attributionNode, "license", null);
				String author = parseStringAtribute(attributionNode, "author", null);
				String description = parseStringAtribute(attributionNode, "description", "");
				String note = parseStringAtribute(attributionNode, "note", "");

				if (url == null || "".equals(url)) {
					logger.info("Attribution url is not set for " + soundFile.getFileName());
					continue;
				}
				if (license == null || "".equals(license)) {
					logger.info("Attribution license is not set for " + soundFile.getFileName());
					continue;
				}
				if (author == null || "".equals(author)) {
					logger.info("Attribution author is not set for " + soundFile.getFileName());
					continue;
				}

				Attribution attribuition = new Attribution(url, license, author, description, note);

				soundFile.getAttributions().add(attribuition);

			}
		}
	}

	public List<Sound> getSounds() {
		return this.sounds;
	}

	public List<String> getXMLFiles() {
		List<String> xmlFiles = new ArrayList<String>();
		for (Sound sound : this.sounds) {
			if (!xmlFiles.contains(sound.getParentFile())) {
				xmlFiles.add(sound.getParentFile());
			}
		}
		return xmlFiles;
	}

	public List<Sound> getSoundsByXMLFile(String xmlFile) {
		List<Sound> sounds = new ArrayList<Sound>();
		for (Sound sound : this.sounds) {
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
