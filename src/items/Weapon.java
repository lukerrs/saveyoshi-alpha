package items;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import entities.Bullet;
import main.Camera;
import main.GamePanel;
import main.KeyHandler;
import main.SoundPlayer;

public class Weapon extends Item {
	private final GamePanel gp;
	private final KeyHandler keyH;
	private final String model;
	private int rpm;
	private int magazineSize, bulletsInMag;
	private double shootCounter, shootCounterMax;
	private boolean reloading, bulletInChamber;
	private long reloadTime;
	private long keyPressTime;
	private SoundPlayer soundP;

	public Weapon(String name, GamePanel gp, KeyHandler keyH) {
		this.model = name;
		this.gp = gp;
		this.keyH = keyH;
		init();
	}

	private void init() {
		loadImages();
		resetWeaponData();
		reloading = false;
		bulletInChamber = true;
		shootCounter = 0;
		soundP = new SoundPlayer(gp);

		try {
			soundP.loadfile(gp.soundCache.get("ak-47"));
		} catch (IOException | LineUnavailableException e) {
			throw new RuntimeException("Failed to load sound file", e);
		}
	}

	private void checkForInput() {
		if (keyH.isKeyPressed(KeyEvent.VK_R) && !reloading) {
			keyPressTime = System.currentTimeMillis();
			reloading = true;
			System.out.println("Reloading...");
		}

		if (reloading) {
			reload();
		}

		if (inHand) {
			if (shootCounter > shootCounterMax && bulletsInMag != 0) {
				bulletInChamber = true;
			} else {
				shootCounter++;
			}
		}

		if (keyH.m1 && inHand && bulletInChamber) {
			fire();
		}
	}

	private void loadImages() {
		try {
			image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/weapons/" + model + ".png")));
			System.out.println("Success: Successfully loaded weapon resources");
		} catch (IOException e) {
			System.out.println("Error: Failed to load weapon resources");
		}
	}

	private void fire() {
		shootCounter = 0;
		bulletInChamber = false;
		bulletsInMag--;

		double x = Camera.x;
		double y = Camera.y;
		double angle = Math.atan2(gp.mouseY - y, gp.mouseX - x);
		new Bullet(gp.player.worldX + (double) gp.player.width / 2,
				gp.player.worldY + (double) gp.player.height / 2,
				damage, 4, angle, gp, keyH);
		System.out.println("SHOT!!! Bullets in mag: " + bulletsInMag);
		soundP.play();
	}

	private void reload() {
		if (System.currentTimeMillis() - keyPressTime >= reloadTime) {
			reloading = false;
			bulletsInMag = magazineSize;
			System.out.println("Finished reloading!");
		}
	}

	public void update() {
		checkForInput();
	}

	private void resetWeaponData() {
		switch (model) {
			case "ak47":
				rpm = 360;
				shootCounterMax = (double) (gp.tickRate * 60) / rpm;
				damage = 10;
				magazineSize = 30;
				bulletsInMag = magazineSize;
				reloadTime = 3000;
				break;
			default:
				throw new IllegalArgumentException("Unknown weapon model: " + model);
		}
	}

	public void draw() {
	}
}