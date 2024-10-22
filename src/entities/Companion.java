package entities;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;

public class Companion extends Entity {
	private Random rd;
	private boolean randomize;
	private int randomCounter;
	private int followCounter;

	public Companion(GamePanel gp, KeyHandler keyH) {
		super(gp, keyH);
		init();
	}

	private void init() {
		getCompanionImage();
		worldX = (double) (gp.tileSize * gp.maxWorldCol) / 2 - gp.tileSize;
		worldY = (double) (gp.tileSize * gp.maxWorldRow) / 2 - gp.tileSize;
		width = gp.tileSize;
		height = gp.tileSize;
		health = 100;
		healthMax = health;
		direction = "stilldown";
		old_direction = "stilldown";
		hitbox = new Rectangle();
		hitbox.x = (int) (19 * gp.scale);
		hitbox.y = (int) (40 * gp.scale);
		hitbox.width = (int) (20 * gp.scale);
		hitbox.height = (int) (24 * gp.scale);
		speed = 1 * gp.scale;
		rd = new Random();
		randomize = true;
		randomCounter = 0;
		followCounter = 0;
	}

	private void getCompanionImage() {
		List<BufferedImage> textures = gp.textureCache.get("yoshi");

		u1 = textures.get(0);
		u2 = textures.get(1);
		s1d = textures.get(2);
		s2d = textures.get(3);
		s3d = textures.get(4);
		s4d = textures.get(5);
		d1 = textures.get(6);
		d2 = textures.get(7);
		sl = textures.get(8);
		l1 = textures.get(9);
		l2 = textures.get(10);
		sr = textures.get(11);
		r1 = textures.get(12);
		r2 = textures.get(13);

		System.out.println("Success: Successfully loaded companion resources out of cache");
	}

	private void randomMovement(int randomInterval) {
		randomCounter++;
		if (randomCounter > randomInterval && randomize) {
			float tempRd = rd.nextFloat(1.0F);
			if (tempRd < 0.5F) {
				switch (rd.nextInt(4)) {
					case 0 :
						direction = "up";
						break;
					case 1 :
						direction = "down";
						break;
					case 2 :
						direction = "left";
						break;
					case 3 :
						direction = "right";
				}
			} else if (tempRd > 0.5F) {
				direction = "stilldown";
			}
			randomCounter = 0;
		} else {
			randomCounter = 0;
		}
	}

	private void follow(int followInterval) {
		followCounter++;
		boolean isInProximity = false;
		if (followCounter > followInterval && !isInProximity ^ !gp.player.isMoving)
		{
			randomize = false;
			if (worldX + (double) width /2 >= gp.player.worldX + gp.player.width + gp.tileSize) direction = "left";
			if (worldX + (double) width /2 <= gp.player.worldX - gp.tileSize) direction = "right";
			if (worldY >= gp.player.worldY + gp.player.height + gp.tileSize) direction = "up";
			if (worldY <= gp.player.worldY - gp.tileSize) direction = "down";
		}
		else if (worldX >= gp.player.worldX - (double) gp.tileSize /2
				&& worldX <= gp.player.worldX + gp.player.width + (double) gp.tileSize /2
				&& worldY >= gp.player.worldY - (double) gp.tileSize /2
				&& worldY <= gp.player.worldY + gp.player.height + (double) gp.tileSize /2)
		{
			isInProximity = true;
			direction = "stilldown";
			randomize = true;
			followCounter = 0;
		}
	}

	public void update() {
		old_direction = direction;
		randomMovement(512);
		follow(1024);
		collision = false;

		collision = gp.colC.checkTile(this) || gp.colC.checkEntities(this);
		
		if (!collision) {
			if (direction == "up") worldY -= speed;
			else if (direction == "down") worldY += speed;
			else if (direction == "right") worldX += speed;
			else if (direction == "left") worldX -= speed;
		}

		screenX = (int) (worldX - gp.player.worldX + gp.player.screenX);
		screenY = (int) (worldY - gp.player.worldY + gp.player.screenY);
		spriteCounter(4);
	}

	public void draw(Graphics2D g2) {
		drawShadow(this, g2);
		BufferedImage image = null;
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
			if (spriteNum == 2) image = u2;
			if (spriteNum == 3) image = u1;
			if (spriteNum == 4) image = u2;
			break;
		case "down" :
			if (spriteNum == 1) image = d1;
			if (spriteNum == 2) image = d2;
			if (spriteNum == 3) image = d1;
			if (spriteNum == 4) image = d2;
			break;
		case "left" :
			if (spriteNum == 1) image = l1;
			if (spriteNum == 2) image = sl;
			if (spriteNum == 3) image = l2;
			if (spriteNum == 4) image = sl;
			break;
		case "right":
			if (spriteNum == 1) image = r1;
			if (spriteNum == 2) image = sr;
			if (spriteNum == 3) image = r2;
			if (spriteNum == 4) image = sr;
			break;
		case "stilldown":
			if (spriteNum == 1) image = s2d;
			if (spriteNum == 2) image = s3d;
			if (spriteNum == 3) image = s1d;
			if (spriteNum == 4) image = s4d;
			break;
		case "leftstill" :
			if (spriteNum == 1) image = sl;
			if (spriteNum == 2) image = sl;
			if (spriteNum == 3) image = sl;
			if (spriteNum == 4) image = sl;
		}
		g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
	}
}