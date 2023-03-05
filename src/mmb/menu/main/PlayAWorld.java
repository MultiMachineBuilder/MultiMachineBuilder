/**
 * 
 */
package mmb.menu.main;

import java.awt.EventQueue;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.pploder.events.Event;
import com.pploder.events.SimpleEvent;

import mmb.Main;
import mmb.NN;
import mmb.Nil;
import mmb.engine.MMBUtils;
import mmb.engine.debug.Debugger;
import mmb.engine.files.Save;
import mmb.engine.json.JsonTool;
import mmb.engine.window.FullScreen;
import mmb.engine.worlds.universe.Universe;
import mmb.menu.world.window.WorldWindow;

/**
 * Controls the game loading process
 * @author oskar
 */
public class PlayAWorld {
	private static final Debugger debug = new Debugger("SELECT A SAVE");
	
	/** Triggered when a world finishes loading */
	@NN public final Event<@Nil Throwable> onLoad = new SimpleEvent<>();

	private static void fullPlay0(Save s, WorldWindow ww){
		boolean fail = true;
		try(InputStream in = s.file.getInputStream()) {
			debug.printl("Opened a file");
			String loadedData = IOUtils.toString(in, StandardCharsets.UTF_8);
			debug.printl("Loaded a file");
			Universe world = new Universe();
			@SuppressWarnings("null")
			JsonNode node = JsonTool.parse(loadedData);
			if(node == null || node.isMissingNode()) {
				debug.printl("Failed to parse JSON data");
				EventQueue.invokeLater(ww::dispose); //Control given back to EDT to close unnecessary window
			}
			debug.printl("Parsed file");
			world.load(node);
			debug.printl("Loaded");
			ww.setWorld(s, world);
			fail = false;
		}catch(Exception e) {
			debug.stacktraceError(e, "Failed to load the world");
			MMBUtils.shoot(e);
		}catch(OutOfMemoryError e) {
			debug.stacktraceError(e, "Ran out of memory while loading");
		}catch(Throwable e) {
			debug.printerrl("Fatal error while world loading");
			Main.crash(e);
		}finally {
			if(fail) {
				EventQueue.invokeLater(() -> {
					ww.dispose();
					FullScreen.setWindow(MainMenu.INSTANCE);
				});//Control given back to EDT to close unnecessary window
			}
		}
	}
	private void fullPlay(Save s, WorldWindow ww) {
		try {
			fullPlay0(s, ww);
			onLoad.trigger(null);
		}catch(Throwable e) {
			onLoad.trigger(e);
			MMBUtils.shoot(e);
		}
	}

	/**
	 * Plays a specific world
	 * @param s file to play
	 * @return a newly creates world window
	 */
	public WorldWindow play(Save s) {
		WorldWindow ww = new WorldWindow();
		FullScreen.setWindow(ww);
		new Thread(() -> fullPlay(s, ww)).start();
		return ww;
	}
}
