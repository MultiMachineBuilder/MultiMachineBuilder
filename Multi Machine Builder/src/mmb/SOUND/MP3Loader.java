package mmb.SOUND;

import java.io.*;
import javax.sound.sampled.*;

/**
 * @author Bartek unx
 */

public class MP3Loader {
    private Clip clip;
    private InputStream bitstream;

    /**
     * This thread decodes MP3 data into PCM and creates clip
     */
    private final Thread decodeThread = new Thread(new Runnable() {
        public void run()
        {
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
                e.printStackTrace();
            }
        }
    });

    public MP3Loader(InputStream stream) {
        bitstream = stream;
    }

    public MP3Loader(byte[] bytes) {
        bitstream = new ByteArrayInputStream(bytes);
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

}
