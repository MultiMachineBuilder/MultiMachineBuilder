/**
 * 
 */
package mmb.DATA.contents.sound;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.annotation.Nonnull;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer.Info;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.util.ByteBufferBackedInputStream;

/**
 * @author oskar
 * This class contains utilities to create sound buffers
 */
public class Sound {
	public final byte[] data;
	public final int offset;
	public final int length;
	public final AudioFormat format;
	public final long frameLen;
	
	/**
	 * Creates a sound
	 * @param data
	 * @param format
	 * @param frameLen
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
	 * Ope
	 * @param clip the clip to open
	 * @throws IllegalArgumentException if the buffer size does not represent anintegral number of sample frames,
	 * or if format is notfully specified or invalid
	 * @throws IllegalStateException if the line is already open
	 * @throws LineUnavailableException if the line cannot be opened due toresource restrictions
	 * @throws SecurityException if the line cannot be opened due to securityrestrictions
	 */
	public void open(Clip clip) throws LineUnavailableException {
		clip.open(format, data, offset, length);
	}
	@Nonnull public AudioInputStream newStream() {
		return new AudioInputStream(new ByteArrayInputStream(data, offset, length), format, frameLen);
	}
	/**
	 * Creates a new clip, already open
	 * @return a new clip object
	 * @throws LineUnavailableException when clip fails to open
	 * @throws IOException when I/O error occurs
	 */
	@Nonnull public Clip newClip() throws LineUnavailableException, IOException {
		Clip clip = AudioSystem.getClip();
		try(AudioInputStream stream = newStream()){
			clip.open(stream);
		}
		return clip;
	}
	/**
	 * Creates a new clip, already open, usig info
	 * @param info the info object
	 * @return a new clip object
	 * @throws LineUnavailableException when clip fails to open
	 * @throws IOException when I/O error occurs
	 */
	@Nonnull public Clip newClip(Info info) throws LineUnavailableException, IOException {
		Clip clip = AudioSystem.getClip(info);
		try(AudioInputStream stream = newStream()){
			clip.open(stream);
		}
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
	
	//Sub-sounds
	/**
	 * 
	 * @param begin 
	 * @param len
	 * @return
	 */
	/*public Sound subSoundInFrames(int begin, int len) {
		int newOffset = offset+(begin*format.getFrameSize());
		int newLength = len*format.getFrameSize();
	}*/
}
