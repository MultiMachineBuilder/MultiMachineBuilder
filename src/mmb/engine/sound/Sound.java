/**
 * 
 */
package mmb.engine.sound;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer.Info;

import org.apache.commons.io.IOUtils;

import mmb.annotations.NN;

/**
 * Describes a sound with all required information to play back properly.
 * The class contains methods to read audio from a file and open a clip.
 * @author oskar 
 */
public class Sound {
	/** Raw audio data */
	public final byte[] data;
	/** Array offset for audio data */
	public final int offset;
	/** Array length for audio data */
	public final int length;
	/** Audio format */
	public final AudioFormat format;
	/** Frame length */
	public final long frameLen;
	
	/**
	 * Creates a sound
	 * @param data raw PCM data
	 * @param format audio format
	 * @param frameLen frame count
	 */
	public Sound(byte[] data, AudioFormat format, long frameLen) {
		if(frameLen < -1)
			throw new IllegalArgumentException("The frame count must be positive: "+frameLen);
		this.data = data;
		this.format = format;
		this.frameLen = frameLen;
		this.offset = 0;
		this.length = data.length;
	}
	/**
	 * Creates a sound with a portion of the array
	 * @param data raw PCM data
	 * @param offset offset from beginning of the array
	 * @param length length of the sub-array
	 * @param format audio format
	 * @param frameLen frame count
	 */
	public Sound(byte[] data, int offset, int length, AudioFormat format, long frameLen) {
		if(length < 0)
			throw new IllegalArgumentException("The length must be nonnegative: "+length);
		if(offset < 0)
			throw new IllegalArgumentException("The offset must be nonnegative: "+offset);
		if(frameLen < -1)
			throw new IllegalArgumentException("The frame count must be positive: "+frameLen);
		if(offset+length > data.length)
			throw new IllegalArgumentException("The bounds of the sound ["+offset+","+(offset+length)+") do not fit in the array [0,"+data.length+")");
		this.data = data;
		this.offset = offset;
		this.length = length;
		this.format = format;
		this.frameLen = frameLen;
	}

	//Streams and clips
	/**
	 * Opens a clip using this sound
	 * @param clip the clip to open
	 * @throws IllegalArgumentException if the buffer size does not represent an integral number of sample frames,
	 * or if format is not fully specified or invalid
	 * @throws IllegalStateException if the line is already open
	 * @throws LineUnavailableException if the line cannot be opened due to resource restrictions
	 * @throws SecurityException if the line cannot be opened due to security restrictions
	 */
	public void open(Clip clip) throws LineUnavailableException {
		clip.open(format, data, offset, length);
	}
	/**
	 * Creates an audio input stream for this sound
	 * @return an audio input stream
	 */
	@NN public AudioInputStream newStream() {
		return new AudioInputStream(new ByteArrayInputStream(data, offset, length), format, frameLen);
	}
	/**
	 * Creates a new clip, already open with this sound
	 * @return a new clip object
	 * @throws LineUnavailableException when clip fails to open
	 * @throws IOException when I/O error occurs
	 */
	@NN public Clip newClip() throws LineUnavailableException, IOException {
		Clip clip = AudioSystem.getClip();
		open(clip);
		return clip;
	}
	/**
	 * Creates a new clip, already open with this sound, using info
	 * @param info the info object
	 * @return a new clip object
	 * @throws LineUnavailableException when clip fails to open
	 * @throws IOException when I/O error occurs
	 */
	@NN public Clip newClip(Info info) throws LineUnavailableException, IOException {
		Clip clip = AudioSystem.getClip(info);
		open(clip);
		return clip;
	}
	
	/**
	 * Loads in a sound
	 * @param in input stream
	 * @return a new sound
	 * @throws IOException when I/O error occurs
	 */
	public static Sound load(AudioInputStream in) throws IOException {
		long frames = in.getFrameLength();
		AudioFormat format = in.getFormat();
		byte[] data = IOUtils.toByteArray(in);
		return new Sound(data, format, frames);
	}
}
