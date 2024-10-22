package main;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AudioPlayer {
	private List<AudioInputStream> audioInputStreams;
	private List<Clip> activeClips;
	private String status;
	private GamePanel gp;
	private byte[] audioData; // Store the audio data in memory
	private AudioFormat audioFormat; // Store the audio format
	private Clip preloadedClip;
	private float volume;

	public AudioPlayer(GamePanel gp) {
		this.gp = gp;
		audioInputStreams = new ArrayList<>();
		activeClips = new ArrayList<>();
	}

	public void loadfile(List<AudioInputStream> sounds) throws IOException, LineUnavailableException {
		// Store the audio format
		audioFormat = sounds.get(0).getFormat();

		// Read the entire audio data into memory
		audioData = sounds.get(0).readAllBytes();

		// Close the original input stream
		sounds.get(0).close();
		preloadClip();
	}

	private void preloadClip() {
		try {
			// Create and open a clip immediately after loading the file
			AudioInputStream preloadStream = new AudioInputStream(
					new java.io.ByteArrayInputStream(audioData),
					audioFormat,
					audioData.length / audioFormat.getFrameSize()
			);

			preloadedClip = AudioSystem.getClip();
			preloadedClip.open(preloadStream);

			// Set the volume
			FloatControl gainControl = (FloatControl) preloadedClip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(-50.0f);

			// Play and immediately stop to initialize the audio system
			preloadedClip.start();
			preloadedClip.stop();

			// Close the preloaded clip
			preloadedClip.close();
			preloadStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setVolume(float volume) {
		this.volume = volume;
		// Apply to all active clips
		for(Clip clip : activeClips) {
			if(clip.isOpen()) {
				FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(volume);
			}
		}
	}

	public void play() {
		try {
			// Create a new AudioInputStream from the stored audio data
			AudioInputStream newStream = new AudioInputStream(
					new java.io.ByteArrayInputStream(audioData),
					audioFormat,
					audioData.length / audioFormat.getFrameSize()
			);

			Clip newClip = AudioSystem.getClip();
			newClip.open(newStream);

			// Set volume for the new clip
			FloatControl gainControl = (FloatControl) newClip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(volume);

			// Add to active clips list before playing
			activeClips.add(newClip);

			// Start playing
			newClip.start();

			// Add listener to clean up when done
			newClip.addLineListener(event -> {
				if (event.getType() == LineEvent.Type.STOP) {
					newClip.close();
					activeClips.remove(newClip);
				}
			});

			status = "play";
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Optional: Method to stop all playing clips
	public void stopAll() {
		for (Clip clip : activeClips) {
			clip.stop();
			clip.close();
		}
		activeClips.clear();
	}
}