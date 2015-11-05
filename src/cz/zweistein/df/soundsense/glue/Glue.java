package cz.zweistein.df.soundsense.glue;

import java.util.ArrayList;
import java.util.List;

import cz.zweistein.df.soundsense.input.Listener;
import cz.zweistein.df.soundsense.output.Procesor;

public class Glue {
	
	public static GlueThread glue(Listener listener, List<Procesor> procesors) {
		GlueThread glue = new GlueThread(listener, procesors);
		new Thread(glue, "ListenerToProcesorsGlue").start();
		return glue;
	}
	
	public static void glue(Listener listener, Procesor procesor) {
		List<Procesor> procesors = new ArrayList<Procesor>(1);
		procesors.add(procesor);
		glue(listener, procesors);
	}

}
