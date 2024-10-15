package items;

import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import entities.Bullet;
import entities.Enemy;
import entities.Entity;
import main.Camera;
import main.GamePanel;
import main.KeyHandler;
import main.SoundPlayer;

public class Weapon extends Item {
	GamePanel gp;
	KeyHandler keyH;
	String type;
	String model;
	int rpm;
	int magazineSize, bulletsInMag;
	double shootCounter, shootCounterMax;
	boolean reloading, bulletInChamber;
	long reloadTime;
	long keyPressTime;
	SoundPlayer soundP;

	public Weapon(String name, GamePanel gp, KeyHandler keyH) {
		model = name;
		this.gp = gp;
		this.keyH = keyH;
		init();
	}

	private void init() {
		loadImages(model);
		resetWeaponData(model);
		reloading = false;
		bulletInChamber = true;
		shootCounter = 0;
		soundP = new SoundPlayer();
        try {
            soundP.loadfile("res/audio/ak47_shot.wav");
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }
	
	private void checkForInput() {

		if(keyH.isKeyPressed(KeyEvent.VK_R) && !reloading)
		{
			keyPressTime = System.currentTimeMillis();
			reloading = true;
			System.out.println("Reloading...");
		}

		if (reloading) {
			reload();
		}

		if(inHand) {
			if(shootCounter > shootCounterMax && bulletsInMag != 0)
			{
				bulletInChamber = true;
			} else {
				shootCounter++;
			}
		}

		if(keyH.m1 && inHand && bulletInChamber) {
			shootCounter = 0;
			bulletInChamber = false;
			bulletsInMag -= 1;
			fire();
			soundP.play();
		}
	}

	private void loadImages(String name) {
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/weapons/ak47.png"));
			System.out.println("Success: Successfully loaded weapon resources");
		} catch (IOException e) {
			System.out.println("Error: Failed to load weapon resources");
		}
	}
	
	public void fire() {
		double x = Camera.x;
		double y = Camera.y;
		int w = 20;
		double angle = Math.atan2(gp.mouseY - y, gp.mouseX - x);
		System.out.println(angle);
		Bullet b = new Bullet(gp.player.worldX + (double) gp.player.width /2,
				gp.player.worldY + (double) gp.player.height /2,
				damage, 4, angle, gp, keyH);
		System.out.println("SHOT!!!");
		System.out.println("Bullets in mag: " + bulletsInMag);
	}
	
	public void reload() {
		if(System.currentTimeMillis() - keyPressTime >= reloadTime){
			reloading = false;
			bulletsInMag = magazineSize;
			System.out.println("Finished reloading!");
		}
	}

	public void update()
	{
		checkForInput();
	}
	
	public void resetWeaponData(String name) {
		switch(name) {
		case "ak47":
			rpm = 360;
			shootCounterMax = (double) (gp.tickRate * 60) /rpm;
			damage = 10;
			magazineSize = 30;
			bulletsInMag = magazineSize;
			reloadTime = 3000;
			break;
		}
	}

	public void draw() {
	}
}