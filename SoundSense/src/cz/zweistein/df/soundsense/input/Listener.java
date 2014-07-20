package cz.zweistein.df.soundsense.input;

import java.io.BufferedReader;
import java.io.IOException;

public abstract class Listener {

	protected int delay = 50; // miliseconds
	protected BufferedReader br;
	protected boolean streaming;
	protected long startTime = System.currentTimeMillis();

	public String getNextLine() throws IOException {

		while (true) {
			String line = this.br.readLine();
			if (line != null) {
				return line;
			} else {
				if (!this.streaming) {
					this.br.close();
					throw new IOException("Reached end of stream or file, breaking off after " + (System.currentTimeMillis() - startTime) + "ms.");
				}
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}

	}

}
