package items;

import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

import entities.Entity;
import entities.Explosion;
import main.GamePanel;
import main.KeyHandler;

public class Throwable extends Item{
	GamePanel gp;
	KeyHandler keyH;
	String type;
	String model;
	int damage;

	public Throwable(String name, GamePanel gp, KeyHandler keyH) {
		model = name;
		this.gp = gp;
		this.keyH = keyH;
		init();
	}

	private void init() {
		loadImages();
		damage = 50;
	}

	private void loadImages() {
		try {
			image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/throwables/poison.png")));
			System.out.println("Success: Successfully loaded throwables resources");
		} catch (IOException e) {
			System.out.println("Error: Failed to load throwables resources");
		}
	}
	
	void toss() {
		int x = gp.getMousePosOnMap().x;
		int y = gp.getMousePosOnMap().y;
		keyH.m1 = false;
		int w = 20;
		Explosion e = new Explosion(x, y, w ,damage, gp,keyH);
		System.out.println("EXPLOSION!!!");
		for(Entity entity: gp.entityManager.entityList) {
			gp.colC.checkDamage(e, entity);
		}
	}

	public void update() {
		if(keyH.m1 && inHand) {
			toss();
			gp.player.inv.hotbar[slot] = null;
			inHand = false;
		}
	}
}
