package mmb.SOUND;

import java.io.*;
import javax.sound.sampled.*;

import mmb.debug.Debugger;

/**
 * @author Bartek unx
 */

public class MP3Loader {
	private static final Debugger debug = new Debugger("MP3");
    private Clip clip;
    private InputStream bitstream;
    private final String name;

    /**
     * This thread decodes MP3 data into PCM and creates clip
     */
    private final Thread decodeThread = new Thread(this::fullRun);

    public MP3Loader(InputStream stream, String name) {
        bitstream = stream;
        this.name = name;
    }

    public MP3Loader(byte[] bytes, String name) {
        bitstream = new ByteArrayInputStream(bytes);
        this.name = name;
    }

    public Clip getClip() {
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
        System.out.println("untilLoad");
    }

    private void fullRun() {
    	try {
            clip = AudioSystem.getClip();
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
            clip.open(din);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            debug.pstm(e, "Failed to load");
        }
    }
}
