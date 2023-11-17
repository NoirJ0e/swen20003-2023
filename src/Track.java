import javax.sound.sampled.*;

/**
 * Class for managing the music being played in the background
 */
public class Track extends Thread {
    private AudioInputStream stream;
    private Clip clip;

    /**
     * Constructor for Track
     * @param file The file path of the music file
     */
    public Track(String file) {
        try {
            stream = AudioSystem.getAudioInputStream(new java.io.File(file));
            clip = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class,stream.getFormat()));
            clip.open(stream);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Stop playing the music
     */
    public void pause() {
        try {
            clip.stop();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Start to play the music
     */
    public void run(){
        try {
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}