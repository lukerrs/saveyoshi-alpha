package entities;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

import main.GamePanel;
import main.KeyHandler;

public class Enemy extends Entity {
	String type;

	/*public Enemy(GamePanel gp, KeyHandler keyH) {
		super(gp, keyH);
		init();
		getEnemyImage();
	}*/
	
	public Enemy(int worldX, int worldY, String type, GamePanel gp, KeyHandler keyH) {
		super(gp, keyH);

		this.worldX = worldX;
		this.worldY = worldY;
		this.type = type;
		init(this.worldX,this.worldY);
	}

	private void init(double worldX, double worldY) {
		getEnemyImage();
		width = gp.tileSize;
		height = gp.tileSize;
		speed = 0.5 * gp.scale;
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
		isHostile = true;
	}

	private void getEnemyImage() {
			List<BufferedImage>  textures = gp.textureCache.get("enemy1");
			p1 = textures.get(0);
			p2 = textures.get(1);
			p3 = textures.get(2);
			p4 = textures.get(3);
			p5 = textures.get(4);
			p6 = textures.get(5);
			p7 = textures.get(6);
			p8 = textures.get(7);
			System.out.println("Success: Successfully loaded enemy resources out of cache");
	}
	private void follow() {
		if (worldX + (double) width / 2 >= gp.player.worldX + (double) gp.player.width / 2) direction = "left";
		else if (worldX + (double) width / 2 <= gp.player.worldX) direction = "right";
		else if (worldY > gp.player.worldY + (double) gp.player.height / 2) direction = "up";
		else if (worldY < gp.player.worldY) direction = "down";
	}

	public void update() {
		if(isAlive) {
			old_direction = direction;
			direction = "stilldown";
			spriteCounter(8);
			if(health <= 0) die();
			follow();

			collision = gp.colC.checkTile(this) || gp.colC.checkEntities(this);

			if (!collision) {
				if (direction == "up") worldY -= speed;
				else if (direction == "down") worldY += speed;
				else if (direction == "right") worldX += speed;
				else if (direction == "left") worldX -= speed;
			}
		}
	}

	public void draw(Graphics2D g2) {
		BufferedImage image = null;
		screenX = (int) (worldX - gp.player.worldX + gp.player.screenX);
		screenY = (int) (worldY - gp.player.worldY + gp.player.screenY);
		if(isAlive) {
			/*switch (direction) {
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
				case "up":
					if (spriteNum == 1) image = u1;
					if (spriteNum == 2) image = s1u;
					if (spriteNum == 3) image = u2;
					if (spriteNum == 4) image = s2u;
					break;
				case "down":
					if (spriteNum == 1) image = d1;
					if (spriteNum == 2) image = s1d;
					if (spriteNum == 3) image = d2;
					if (spriteNum == 4) image = s2d;
					break;
				case "left":
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
				case "leftstill":
					if (spriteNum == 1) image = sl;
					if (spriteNum == 2) image = sl;
					if (spriteNum == 3) image = sl;
					if (spriteNum == 4) image = sl;
			}*/
			if (spriteNum == 1) image = p1;
			if (spriteNum == 2) image = p2;
			if (spriteNum == 3) image = p3;
			if (spriteNum == 4) image = p4;
			if (spriteNum == 5) image = p5;
			if (spriteNum == 6) image = p6;
			if (spriteNum == 7) image = p7;
			if (spriteNum == 8) image = p8;
			g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
		}
	}
}