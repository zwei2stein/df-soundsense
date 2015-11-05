package cz.zweistein.df.soundsense.config.executor;

import java.util.regex.Pattern;

public class Executor {
	
	private String logPattern;
	private Pattern parsedLogPattern;
	private String call;
	
	public Executor(String logPattern, String call) {
		this.logPattern = logPattern;
		this.call = call;
		
		this.parsedLogPattern = Pattern.compile(this.logPattern);
	}
	
	public String getLogPattern() {
		return this.logPattern;
	}
	
	public String getCall() {
		return this.call;
	}

	public Pattern getParsedLogPattern() {
		return this.parsedLogPattern;
	}

}
