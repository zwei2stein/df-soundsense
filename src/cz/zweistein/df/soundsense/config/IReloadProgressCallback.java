package cz.zweistein.df.soundsense.config;

public interface IReloadProgressCallback {

	/**
	 * Called when reload is done.
	 */
	void done();

	/**
	 * Called when progress is made.
	 */
	void tick();

}
