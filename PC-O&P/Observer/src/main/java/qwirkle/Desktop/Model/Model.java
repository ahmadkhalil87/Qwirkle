package qwirkle.Desktop.Model;

import javafx.scene.media.MediaPlayer;

/**
 * Class responsible for creating an instance of the main menu View and connecting it to the controller.
 * grants also access to the instances created.
 * 
 * @author Youssef Ben ameur
 */

public class Model {

	// Sounds settings variables
	private MediaPlayer mediaPlayerMusic;
	private double musicVol; // 0...1
	private double soundVol; // 0...1
	private double tmpMusicVol;
	private double tmpSfxVol;

	// Client Variables 
	private volatile transient Boolean online = false;

	/**
	 * Constructor 
	 */ 
	public Model() {
		setMusicVol(0.0); // Initial Sound Volume
		setSoundVol(0.0); // Initial Music Volume
	}

	// getter and setter methods for the sound settings variables
	public MediaPlayer getMediaPlayerMusic() {
		return this.mediaPlayerMusic;
	}
	public void setMediaPlayerMusic(MediaPlayer mediaPlayer) {
		this.mediaPlayerMusic = mediaPlayer;
	}
	public void setMusicVol(double volume) {
		this.musicVol = volume;
	}
	public double getMusicVol() {
		return this.musicVol;
	}
	public double getSoundVol() {
		return this.soundVol;
	}
	public void setSoundVol(double volume) {
		this.soundVol = volume;
	}
	public void setTmpMusVol(double volume) {
		this.tmpMusicVol = volume;
	}
	public void setTmpSfxVol(double volume) {
		this.tmpSfxVol = volume;
	}
	public double getTmpMusVol() {
		return this.tmpMusicVol;
	}
	public double getTmpSfxVol() {
		return this.tmpSfxVol;
	}
}
