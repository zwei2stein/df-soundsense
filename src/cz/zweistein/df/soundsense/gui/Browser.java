package cz.zweistein.df.soundsense.gui;

public class Browser {

	public static void openURL(String url) {

		java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

		if (!desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {

			throw new UnsupportedOperationException("Desktop doesn't support the browse action");
		}

		try {

			java.net.URI uri = new java.net.URI(url);
			desktop.browse(uri);

		} catch (Exception e) {

			throw new UnsupportedOperationException(e.getMessage());
		}

	}

}
