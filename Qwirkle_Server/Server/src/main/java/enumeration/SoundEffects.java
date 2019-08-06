package enumeration;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Enumeration contains all the sound effects of the game. this way we separate all of the sound effects from the game code 
 * All sound effect names and the associated wave file have to be defined here.
 * To play a specific sound, simply invoke SoundEffect.SOUND_NAME.play(double volumeLevel). The
 * (optional) static method SoundEffect.init() pre-loads all the sound files, so that the play is
 * not paused while loading the file for the first time. The volumeLevel is a double value between 0
 * (mute) and 1 (maximum volume).<br>
 * Inspired by: https://www.ntu.edu.sg/home/ehchua/programming/java/J8c_PlayingSound.html
 *
 * @author Houman Mahtabi
 */
public enum SoundEffects{
	
	BUTTON_CLICK("/sound/button_click.wav");



  private Clip soundClip; // Each sound effect has its own clip, loaded with its own sound file.

  /**
   * The Constructor to construct each element of the enum with its own sound file.
   *
   * @param soundFileName The file mame of the *.wav file
   */
  SoundEffects(String soundFileName) {
    try {
      // Use URL (instead of File) to read from disk and also from JAR.
      // Important: Do not use getClassLoader() in the next line!!!
      URL url = this.getClass().getResource(soundFileName);
      // Set up an audio input stream piped from the sound file.
      AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
      // Get a clip resource.
      soundClip = AudioSystem.getClip();
      // Open audio clip and load samples from the audio input stream.
      soundClip.open(audioInputStream);
    } catch (UnsupportedAudioFileException exc) {
      exc.printStackTrace();
    } catch (IOException exc) {
      exc.printStackTrace();
    } catch (LineUnavailableException exc) {
      exc.printStackTrace();
    }
  }

  //

  /**
   * This method plays or replays the sound effect from the beginning, by rewinding.
   *
   * @param volLevel The volume level (Range 0...1)
   */
  public void play(double volLevel) {
    if (volLevel > 0) { // Don't play when muted

      if (soundClip.isRunning()) { // Stop the player if it is still running
        soundClip.stop();
      }

      // Adjust volume
      FloatControl volume = (FloatControl) soundClip.getControl(FloatControl.Type.MASTER_GAIN);
      // float volumeValue = volume.getMinimum() + ((volume.getMaximum() - volume.getMinimum()) *
      // volLevel);
      // The above not good, because decibel scala is not linear but logarithmic
      float volumeValue = (float) (Math.log(volLevel) / Math.log(10.0) * 20.0);
      volume.setValue(volumeValue);

      // Rewind to the beginning and start playing
      soundClip.setFramePosition(0);
      soundClip.start();
    }
  }

  /** Optional static method to pre-load all the sound files. */
  public static void init() {
    values(); // calls the constructor for all the elements
  }
}
