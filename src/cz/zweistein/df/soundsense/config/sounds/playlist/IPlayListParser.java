package cz.zweistein.df.soundsense.config.sounds.playlist;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface IPlayListParser {

	List<String> parse(String parentFilename, InputStream is) throws IOException;

}
