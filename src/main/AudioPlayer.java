package main;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

public class AudioPlayer {
    private final List<Clip> activeClips;
	private String status;
    private byte[] audioData;
	private AudioFormat audioFormat;
	private float volume;
	private final ExecutorService audioThreadPool;
	private final AtomicBoolean isPreloaded = new AtomicBoolean(false);
	private Clip preloadedClip = null;

	public AudioPlayer() {
        this.audioThreadPool = AudioManager.getInstance().getThreadPool();
		activeClips = new ArrayList<>();
	}

	public void loadfile(List<AudioInputStream> sounds) throws IOException, LineUnavailableException {
		audioFormat = sounds.get(0).getFormat();
		audioData = sounds.get(0).readAllBytes();
		sounds.get(0).close();

		// Start preloading asynchronously
		audioThreadPool.submit(this::preloadClipAsync);
	}

	private void preloadClipAsync() {
		try {
			if (preloadedClip != null) {
				preloadedClip.close();
			}

			AudioInputStream preloadStream = new AudioInputStream(
					new java.io.ByteArrayInputStream(audioData),
					audioFormat,
					audioData.length / audioFormat.getFrameSize()
			);

			preloadedClip = AudioSystem.getClip();
			preloadedClip.open(preloadStream);

			setClipVolume(preloadedClip);

			isPreloaded.set(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setVolume(float volume) {
		this.volume = volume;
		for(Clip clip : activeClips) {
			if(clip.isOpen()) {
				setClipVolume(clip);
			}
		}
	}

	private void setClipVolume(Clip clip) {
		try {
			if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
				FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(volume);
			} else if (clip.isControlSupported(FloatControl.Type.VOLUME)) {
				// Try VOLUME control as fallback
				FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.VOLUME);
				volumeControl.setValue(volume);
			}
			// If neither control is supported, the clip will play at default volume
		} catch (Exception e) {
			// Log the error but continue playing
			System.out.println("Volume control not supported for this clip");
		}
	}

	public void play() {
		audioThreadPool.submit(() -> {
			try {
				Clip clipToUse;

				if (isPreloaded.get() && preloadedClip != null) {
					clipToUse = preloadedClip;
					preloadedClip = null;
					isPreloaded.set(false);
				} else {
					// Fallback to creating a new clip if preloaded isn't ready
					AudioInputStream newStream = new AudioInputStream(
							new java.io.ByteArrayInputStream(audioData),
							audioFormat,
							audioData.length / audioFormat.getFrameSize()
					);
					clipToUse = AudioSystem.getClip();
					clipToUse.open(newStream);
				}

				// Set volume
				try {
					FloatControl gainControl = (FloatControl) clipToUse.getControl(FloatControl.Type.MASTER_GAIN);
					gainControl.setValue(volume);
				} catch (IllegalArgumentException e) {
					// Ignore if volume control isn't available
				}

				// Add to active clips and play
				activeClips.add(clipToUse);
				clipToUse.start();

				// Setup cleanup listener
				clipToUse.addLineListener(event -> {
					if (event.getType() == LineEvent.Type.STOP) {
						clipToUse.close();
						activeClips.remove(clipToUse);
					}
				});

				// Start preloading the next clip
				preloadClipAsync();

				status = "play";
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public void stopAll() {
		for (Clip clip : activeClips) {
			clip.stop();
			clip.close();
		}
		activeClips.clear();
	}
}