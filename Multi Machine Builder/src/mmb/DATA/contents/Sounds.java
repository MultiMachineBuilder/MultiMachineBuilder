/**
 * 
 */
package mmb.DATA.contents;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import mmb.FILES.AdvancedFile;
import mmb.MODS.loader.AddonLoader;
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
	/**
	 * @param name
	 * @return
	 */
	public static Sound getSound(String name) {
		Sound s = sounds0.get(name);
		if(s == null) {
			//Cache miss, load classpath
			try(@SuppressWarnings("null") @Nonnull InputStream in = AddonLoader.bcl.getResourceAsStream("sound/"+name)) {
				s = load(in, name);
				if(s == null) throw new NotFoundException("Could not find sound "+name);
			} catch (IOException e) {
				throw new NotFoundException("Failed to load sound "+name, e);
			}	
		}
		return s;
	}
	/**
	 * Load the sound data from a file
	 * @param soundData file input stream
	 * @param name sound file name
	 * @return the loaded sound, or null if failed
	 * @throws IOException when sound fails to load
	 */
	public static Sound load(InputStream soundData, String name) throws IOException {
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
		}catch (IOException e) {
			debug.pstm(e, "Failed to load "+ ext.toUpperCase() +" file "+name);
			throw e;
		}catch(InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (UnsupportedAudioFileException e) {
			throw new IOException(e);
		}
		if(loaded != null) load(loaded, name);
		return loaded;
	}
	public static void load(Sound sound, String name) {
		debug.printl("Loading a sound: "+name);
		sounds0.put(name, sound);
	}


}
