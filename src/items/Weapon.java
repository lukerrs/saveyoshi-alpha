package items;

import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import entities.Bullet;
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
	}
	
	private synchronized void checkForInput() {
		synchronized(gp.gamePanelThread) {
			if((keyH.isKeyPressed(KeyEvent.VK_R) && bulletsInMag == 0))
			{
				keyPressTime = System.currentTimeMillis();
				reloading = true;
				while(reloading)
				{
					reload();
				}
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
				try {
					gp.soundP.loadfile("res/audio/ak47_shot.wav");
					gp.soundP.play();
				}
				catch (Exception e) { e.printStackTrace(); }
			}
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
		int x = gp.getMousePosOnMap().x;
		int y = gp.getMousePosOnMap().y;
		int w = 20;
		Bullet b = new Bullet(x, y, w, w , damage, gp,keyH);
		System.out.println("SHOT!!!");
		System.out.println("Bullets in mag: " + bulletsInMag);
		gp.colC.checkDamage(b, gp.e1);
	}
	
	public synchronized void reload() {
		if(System.currentTimeMillis() == keyPressTime + reloadTime){
			reloading = false;
			bulletsInMag = magazineSize;
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
			shootCounterMax = gp.tickRate * 60/rpm;
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