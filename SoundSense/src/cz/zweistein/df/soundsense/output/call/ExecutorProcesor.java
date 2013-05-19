package cz.zweistein.df.soundsense.output.call;

import java.io.IOException;
import java.util.logging.Logger;

import cz.zweistein.df.soundsense.config.executor.Executor;
import cz.zweistein.df.soundsense.config.executor.ExecutorXML;
import cz.zweistein.df.soundsense.output.Procesor;
import cz.zweistein.df.soundsense.util.log.LoggerSource;

public class ExecutorProcesor extends Procesor {
	private static Logger logger = LoggerSource.LOGGER;

	private ExecutorXML executorXML;
	private String baseDir;

	public ExecutorProcesor(ExecutorXML executorXML, String baseDir) {
		super();
		this.executorXML = executorXML;
		this.baseDir = baseDir;
	}

	@Override
	public void processLine(String line) {
		for (final Executor executor : executorXML.getExecutors()) {
			if (executor.getParsedLogPattern().matcher(line).matches()) {

				new Thread(new Runnable() {

					@Override
					public void run() {
						String call = executor.getCall().replace("${baseDir}", baseDir);
						logger.info("Calling external tool: " + call);
						try {
							Process process = Runtime.getRuntime().exec(call);

							process.waitFor();

							if (process.exitValue() == 0) {
								logger.info("Calling external tool: " + call + " suceeded.");
							} else {
								logger.info("Calling external tool: " + call + " failed: " + process.exitValue());
							}

						} catch (IOException e) {
							logger.info("Calling external tool: " + call + " failed: " + e);
						} catch (InterruptedException e) {
							logger.info("Calling external tool: " + call + " failed: " + e);
						}

					}

				}).start();

			}
		}
	}

}
