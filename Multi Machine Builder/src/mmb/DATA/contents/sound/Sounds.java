/**
 * 
 */
package mmb.DATA.contents.sound;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import fr.delthas.javamp3.Sound;
import mmb.DATA.file.AdvancedFile;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class Sounds {
	private static Debugger debug = new Debugger("SOUNDS");
	private static Map<String, Clip> sounds = new Hashtable<String, Clip>();
	
	public static Clip getSound(String name) {
		return sounds.get(name);
	}
	public static void load(InputStream soundData, String name) {
		String ext = AdvancedFile.baseExtension(name)[1];
		Clip loaded = null;
		try {
			switch(ext) {
			case "wav":
				//Wave
				loaded = AudioSystem.getClip();
				AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(soundData));
				loaded.open(ais);
				
				load(loaded, name);
				break;
			case "mp1":
			case "mp2":
			case "mp3":
				try(Sound sound = new Sound(new BufferedInputStream(soundData))) {
					loaded = AudioSystem.getClip();
					byte[] bytes = new byte[0];
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					int read = sound.decodeFullyInto(baos);
					int samples = read / 2;
					loaded.open(new AudioInputStream(soundData, sound.getAudioFormat(), samples));
				}
			break;
			default:
				debug.printl("Unsupported format: "+ext);
			}
		}catch (Exception e) {
			debug.pstm(e, "Failed to load "+ ext.toUpperCase() +" file "+name);
		}
		if(loaded != null) load(loaded, name);
	}
	public static void load(Clip sound, String name) {
		sounds.put(name, sound);
	}

}
