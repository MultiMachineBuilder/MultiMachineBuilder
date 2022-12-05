/**
 * 
 */
package mmbeng.sound;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import mmbeng.NotFoundException;
import mmbeng.debug.Debugger;
import mmbeng.files.AdvancedFile;
import mmbeng.mods.ModLoader;

/**
 * @author oskar
 *
 */
public class Sounds {
	private Sounds() {}
	private static Debugger debug = new Debugger("SOUNDS");
	
	private static final Map<String, Sound> sounds0 = new HashMap<>();
	/** The list of all sounds */
	public static final Map<String, Sound> sounds = Collections.unmodifiableMap(sounds0);
	/**
	 * @param name
	 * @return
	 */
	public static Sound getSound(String name) {
		Sound s = sounds0.get(name);
		if(s == null) {
			//Cache miss, load classpath
			try(InputStream in = ModLoader.bcl.getResourceAsStream("sound/"+name)) {
				if(in == null) throw new NotFoundException("Could not find sound "+name);
				s = load(in, name);
				if(s == null) throw new NotFoundException("Could not load sound "+name);
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
		try(AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(soundData))) {
			switch(ext) {
			case "wav":
				//Wave
				loaded = Sound.load(ais);
				load(loaded, name);
				break;
			case "mp1":
			case "mp2":
			case "mp3":
			case "ogg":
				//MP1/MP2/MP3
				//Ogg Vorbis
		        AudioFormat baseFormat = ais.getFormat();
		        AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(),
		        16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
		        AudioInputStream ais2 = AudioSystem.getAudioInputStream(targetFormat, ais);
		        loaded = Sound.load(ais2);
				load(loaded, name);
				break;
			default:
				debug.printl("Unsupported format: "+ext);
			}
		}catch (IOException e) {
			debug.pstm(e, "Failed to load "+ ext.toUpperCase() +" file "+name);
			throw e;
		}catch (UnsupportedAudioFileException e) {
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
