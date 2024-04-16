package entities;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;

public class Enemy extends Entity {
	String type;

	public Enemy(GamePanel gp, KeyHandler keyH) {
		super(gp, keyH);
		init();
		getEnemyImage();
	}
	
	public Enemy(double worldX, double worldY, String type, GamePanel gp, KeyHandler keyH) {
		super(gp, keyH);
		this.worldX = worldX;
		this.worldY = worldY;
		this.type = type;
		init();
		getEnemyImage();
	}

	private synchronized void init() {
		worldX = gp.tileSize * gp.maxWorldCol / 2 + gp.tileSize;
		worldY = gp.tileSize * gp.maxWorldRow / 2 + gp.tileSize;
		width = gp.tileSize;
		height = gp.tileSize;
		speed = 1 * gp.scale;
		health = 150;
		healthMax = health;
		hitbox = new Rectangle();
		hitbox.x = (int) (22 * gp.scale);
		hitbox.y = (int) (10 * gp.scale);
		hitbox.width = (int) (20 * gp.scale);
		hitbox.height = (int) (42 * gp.scale);
		direction = "stilldown";
		old_direction = "stilldown";
		isAlive = true;
	}

	private synchronized void getEnemyImage() {
		try {
			p1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemy/Enemy_1.png")));
			p2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemy/Enemy_2.png")));
			p3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemy/Enemy_3.png")));
			p4 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemy/Enemy_4.png")));
			p5 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemy/Enemy_5.png")));
			p6 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemy/Enemy_6.png")));
			p7 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemy/Enemy_7.png")));
			p8 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemy/Enemy_8.png")));
			System.out.println("Success: Successfully loaded enemy resources");
		} catch (IOException e) {
			System.out.println("Error: Failed to load Enemy resources");
		}
	}

	public synchronized void update() {
		if(isAlive) {
			old_direction = direction;
			direction = "stilldown";
			screenX = (int) (worldX - gp.player.worldX + gp.player.screenX);
			screenY = (int) (worldY - gp.player.worldY + gp.player.screenY);
			spriteCounter(8);
			if(health <= 0) die();
		}
	}

	public synchronized void draw(Graphics2D g2) {
		BufferedImage image = null;
		if(isAlive)
		{
		switch (direction) {
			case "rightstill":
				if (spriteNum == 1) image = sr;
				if (spriteNum == 2) image = sr;
				if (spriteNum == 3) image = sr;
				if (spriteNum == 4) image = sr;
				break;
			case "upstill":
				if (spriteNum == 1) image = s1u;
				if (spriteNum == 2) image = s2u;
				if (spriteNum == 3) image = s1u;
				if (spriteNum == 4) image = s2u;
				break;
			case "up" :
				if (spriteNum == 1) image = u1;
				if (spriteNum == 2) image = s1u;
				if (spriteNum == 3) image = u2;
				if (spriteNum == 4) image = s2u;
				break;
			case "down" :
				if (spriteNum == 1) image = d1;
				if (spriteNum == 2) image = s1d;
				if (spriteNum == 3) image = d2;
				if (spriteNum == 4) image = s2d;
				break;
			case "left" :
				if (spriteNum == 1) image = l1;
				if (spriteNum == 2) image = l2;
				if (spriteNum == 3) image = l1;
				if (spriteNum == 4) image = l2;
				break;
			case "right":
				if (spriteNum == 1) image = r1;
				if (spriteNum == 2) image = r2;
				if (spriteNum == 3) image = r1;
				if (spriteNum == 4) image = r2;
				break;
			case "stilldown":
				if (spriteNum == 1) image = p1;
				if (spriteNum == 2) image = p2;
				if (spriteNum == 3) image = p3;
				if (spriteNum == 4) image = p4;
				if (spriteNum == 5) image = p5;
				if (spriteNum == 6) image = p6;
				if (spriteNum == 7) image = p7;
				if (spriteNum == 8) image = p8;
				break;
			case "leftstill" :
				if (spriteNum == 1) image = sl;
				if (spriteNum == 2) image = sl;
				if (spriteNum == 3) image = sl;
				if (spriteNum == 4) image = sl;
		}
		//drawHealthbar(this,g2);
		g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
		}
	}
}