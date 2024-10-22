package main;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundPlayer
{
	private List<Clip> clips;
	private List<AudioInputStream> audioInputStreams;
	private String status;

	private GamePanel gp;

	// to store current position
	private Long currentFrame;
	private Clip clip;


	private AudioInputStream audioInputStream;
	private String filePath;

	// constructor to initialize streams and clip
	public SoundPlayer(GamePanel gp)
	{
		this.gp = gp;
	}


	public void loadfile(List<AudioInputStream> sounds) throws IOException, LineUnavailableException {
		audioInputStream = sounds.get(0);
		clip = AudioSystem.getClip();
		clip.open(audioInputStream);
	}

	@SuppressWarnings("unused")
	private void gotoChoice(int c)
			throws IOException, LineUnavailableException, UnsupportedAudioFileException
	{
		switch (c)
		{
			case 1 -> pause();
			case 2 -> resumeAudio();
			case 3 -> restart();
			case 4 -> stop();
			case 5 -> {
                        System.out.println("Enter time (" + 0 +
                                ", " + clip.getMicrosecondLength() + ")");
                        try (Scanner sc = new Scanner(System.in))
                        {
							long c1 = sc.nextLong();
							jump(c1);
						}
			}

		}

	}

	// Method to play the audio
	public void play()
	{
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(-10.0f); // Reduce volume by 10 decibels
		clip.setFramePosition(0); // Reset the frame position to the beginning
		clip.start();
		status = "play";
	}

	// Method to pause the audio
	public void pause()
	{
		if (status.equals("paused"))
		{
			System.out.println("audio is already paused");
			return;
		}
		this.currentFrame =
		this.clip.getMicrosecondPosition();
		clip.stop();
		status = "paused";
	}

	// Method to resume the audio
	public void resumeAudio() throws UnsupportedAudioFileException,
								IOException, LineUnavailableException
	{
		if (status.equals("play"))
		{
			System.out.println("Audio is already "+
			"being played");
			return;
		}
		clip.close();
		resetAudioStream();
		clip.setMicrosecondPosition(currentFrame);
		this.play();
	}

	// Method to restart the audio
	public void restart() throws IOException, LineUnavailableException,
											UnsupportedAudioFileException
	{
		clip.stop();
		clip.close();
		resetAudioStream();
		currentFrame = 0L;
		clip.setMicrosecondPosition(0);
		this.play();
	}

	// Method to stop the audio
	public void stop() {
		currentFrame = 0L;
		clip.stop();
		clip.close();
	}

	// Method to jump over a specific part
	public void jump(long c) throws UnsupportedAudioFileException, IOException,
														LineUnavailableException
	{
		if (c > 0 && c < clip.getMicrosecondLength())
		{
			clip.stop();
			clip.close();
			resetAudioStream();
			currentFrame = c;
			clip.setMicrosecondPosition(c);
			this.play();
		}
	}

	// Method to reset audio stream
	public void resetAudioStream() throws UnsupportedAudioFileException, IOException,
											LineUnavailableException
	{
		audioInputStream = AudioSystem.getAudioInputStream(
		new File(filePath).getAbsoluteFile());
		clip.open(audioInputStream);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

}
