package mmb.SOUND;

import java.io.*;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import mmb.DATA.contents.Sound;
import mmb.debug.Debugger;

/**
 * @author Bartek unx
 */

public class MP3Loader {
	private static final Debugger debug = new Debugger("MP3");
    private Sound clip;
    private InputStream bitstream;
    /**
     * This thread decodes MP3 data into PCM and creates clip
     */
    private final Thread decodeThread = new Thread(this::fullRun);

    /**
     * Creates a MP3 loader from an input stream
     * @param stream input stream
     */
    public MP3Loader(InputStream stream) {
        bitstream = stream;
    }

    /**
     * Creates a MP3 loader from a byte array
     * @param bytes byte array
     */
    public MP3Loader(byte[] bytes) {
        bitstream = new ByteArrayInputStream(bytes);
    }

    public Sound getClip() {
        return clip;
    }

    /**
     * Starts loader thread and waits for completion
     * @throws InterruptedException
     */
    public void run() throws InterruptedException {
        this.start();
        this.untilLoad();
    }

    /**
     * Starts loader thread
     */
    public void start() {
        decodeThread.start();
    }

    /**
     * Blocks until loader thread completes
     * @throws InterruptedException
     */
    public void untilLoad() throws InterruptedException {
        decodeThread.join();
        debug.printl("untilLoad");
    }

    private void fullRun() {
    	try {
            // http://www.javazoom.net/mp3spi/documents.html
            AudioInputStream in = AudioSystem.getAudioInputStream(bitstream);
            AudioFormat baseFormat = in.getFormat();
            AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false);
            AudioInputStream din = AudioSystem.getAudioInputStream(decodedFormat, in);
            clip = Sound.load(din);
        } catch (UnsupportedAudioFileException | IOException e) {
            debug.pstm(e, "Failed to load");
        }
    }
}
