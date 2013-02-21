package cz.zweistein.df.soundsense.output.call;

import java.io.IOException;
import java.util.logging.Logger;

import cz.zweistein.df.soundsense.config.executor.Executor;
import cz.zweistein.df.soundsense.config.executor.ExecutorXML;
import cz.zweistein.df.soundsense.output.Procesor;
import cz.zweistein.df.soundsense.util.log.LoggerSource;

public class ExecutorProcesor extends Procesor {
	private static Logger logger = LoggerSource.logger;
	
	private ExecutorXML executorXML;
	private String baseDir;
	
	public ExecutorProcesor(ExecutorXML executorXML, String baseDir) {
		super();
		this.executorXML = executorXML;
		this.baseDir = baseDir;
	}

	@Override
	public void processLine(String line) {
		for (Executor executor : executorXML.getExecutors()) {
			if (executor.getParsedLogPattern().matcher(line).matches()) {
				
				String call = executor.getCall().replace("${baseDir}", baseDir);
				
				try {
					logger.info("Calling external tool: "+call);
					
					Runtime.getRuntime().exec(call);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
