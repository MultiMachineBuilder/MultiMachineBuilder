/**
 * 
 */
package mmb.DATA.contents.sound;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import mmb.FILES.AdvancedFile;
import mmb.SOUND.MP3Loader;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class Sounds {
	private static Debugger debug = new Debugger("SOUNDS");
	private static Map<String, Sound> sounds0 = new HashMap<>();
	public static Map<String, Sound> sounds = Collections.unmodifiableMap(sounds0);
	public static Sound getSound(String name) {
		return sounds0.get(name);
	}
	/**
	 * Load the sound data from a file
	 * @param soundData file input stream
	 * @param name sound file name
	 */
	public static void load(InputStream soundData, String name) {
		String ext = AdvancedFile.baseExtension(name)[1];
		Sound loaded = null;
		try {
			switch(ext) {
			case "wav":
				//Wave
				AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(soundData));
				loaded = Sound.load(ais);
				
				load(loaded, name);
				break;
			case "mp1":
			case "mp2":
			case "mp3":
				MP3Loader mp3l = new MP3Loader(soundData);
				mp3l.run();
				loaded = mp3l.getClip();
			break;
			default:
				debug.printl("Unsupported format: "+ext);
			}
		}catch (Exception e) {
			debug.pstm(e, "Failed to load "+ ext.toUpperCase() +" file "+name);
		}
		if(loaded != null) load(loaded, name);
	}
	public static void load(Sound sound, String name) {
		debug.printl("Loading a sound: "+name);
		sounds0.put(name, sound);
	}


}
