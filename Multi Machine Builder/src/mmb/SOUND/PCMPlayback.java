/**
 * 
 */
package mmb.SOUND;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Control;
import javax.sound.sampled.Control.Type;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;

/**
 * @author oskar
 *
 */
public class PCMPlayback implements Clip {
	
	public final PCM sound;
	public PCMPlayback(PCM sound) {
		super();
		this.sound = sound;
	}

	public int playbackPosition;
	public int min, max;

	@Override
	public int available() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void drain() {
		// TODO Auto-generated method stub

	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getBufferSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public AudioFormat getFormat() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFramePosition() {
		return playbackPosition;
	}

	@Override
	public float getLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getLongFramePosition() {
		return playbackPosition;
	}

	@Override
	public long getMicrosecondPosition() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isActive() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRunning() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addLineListener(LineListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public Control getControl(Type arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Control[] getControls() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public javax.sound.sampled.Line.Info getLineInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isControlSupported(Type arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOpen() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void open() throws LineUnavailableException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeLineListener(LineListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getFrameLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getMicrosecondLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void loop(int arg0) {
		min = playbackPosition;
		max = arg0;
	}

	@Override
	public void open(AudioInputStream arg0) throws LineUnavailableException, IOException {
	}

	@Override
	public void open(AudioFormat arg0, byte[] arg1, int arg2, int arg3) throws LineUnavailableException {
	}

	@Override
	public void setFramePosition(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLoopPoints(int arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMicrosecondPosition(long arg0) {
		// TODO Auto-generated method stub

	}

}
