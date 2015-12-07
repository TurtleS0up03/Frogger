package frogger;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Audio {

	private Clip clip;

	public Audio() {
		try {
			// Open an audio input stream.
			URL url = new URL("http://www.digitpress.com/dpsoundz/frogger.wav");
			;
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
			// Get a sound clip resource.
			clip = AudioSystem.getClip();
			// Open audio clip and load samples from the audio input stream.
			clip.open(audioIn);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	// Stop the music!
	public void stop() {
		if (clip.isRunning()) {
			clip.stop();
		}
	}// EoM

	public Clip getClip() {
		return clip;
	}// EoM

	public void setClip(Clip clip) {
		this.clip = clip;
	}// EoM

}// EoC
