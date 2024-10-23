package entities;

import items.ItemManager;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import javax.imageio.ImageIO;

import main.Camera;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {
	public Player(GamePanel gp, KeyHandler keyH) {
		super(gp, keyH);
		init();
	}

	private void init()
	{
		getPlayerImage();
		inv = new ItemManager(this.gp, this.keyH);
		worldX = (double) (gp.tileSize * gp.maxWorldCol) / 2;
		worldY = (double) (gp.tileSize * gp.maxWorldRow) / 2;
		screenX = Camera.x - gp.tileSize/2;
		screenY = Camera.y - gp.tileSize/2;
		width = gp.tileSize;
		height = gp.tileSize;
		speed = 0.5 * gp.scale;
		health = 150;
		healthMax = health;
		direction = "stilldown";
		old_direction = "stilldown";
		hitbox = new Rectangle();
		hitbox.x = (int) (14 * gp.scale);
		hitbox.y = (int) (32 * gp.scale);
		hitbox.width = (int) (34 * gp.scale);
		hitbox.height = (int) (32 * gp.scale);
	}
	
	public void setScreenPosition()
	{
		screenX = Camera.x - gp.tileSize/2;
		screenY = Camera.y - gp.tileSize/2;
	}

	private void getPlayerImage()
	{
		List<BufferedImage> textures = gp.textureCache.get("player");

		s1u = textures.get(0);
		s2u = textures.get(1);
		u1 = textures.get(2);
		u2 = textures.get(3);
		s1d = textures.get(4);
		s2d = textures.get(5);
		d1 = textures.get(6);
		d2 = textures.get(7);
		sl = textures.get(8);
		l1 = textures.get(9);
		l2 = textures.get(10);
		sr = textures.get(11);
		r1 = textures.get(12);
		r2 = textures.get(13);

		System.out.println("Success: Successfully loaded player resources out of cache");
	}

	private void move() {
		collision = false;

		collision = gp.colC.checkTile(this) || gp.colC.checkEntities(this);
		
		if (!collision && isMoving) {
			if (Objects.equals(direction, "up")) worldY -= speed;
			else if (Objects.equals(direction, "down")) worldY += speed;
			else if (Objects.equals(direction, "right")) worldX += speed;
			else if (Objects.equals(direction, "left")) worldX -= speed;
			else if (Objects.equals(direction, "upright")) { worldX += speed * 2/3; worldY -= speed * 2/3; }
			else if (Objects.equals(direction, "upleft")) { worldX -= speed * 2/3; worldY -= speed * 2/3; }
			else if (Objects.equals(direction, "downright")) { worldX += speed * 2/3; worldY += speed * 2/3; }
			else if (Objects.equals(direction, "downleft")) { worldX -= speed * 2/3; worldY += speed * 2/3; }
		} else {
			isMoving = false;
		}
		
		old_direction = direction;
		
		if ((keyH.upPress || keyH.downPress || keyH.leftPress || keyH.rightPress) && gp.gamestate != 11) {
			if (keyH.upPress) direction = "up";
			if (keyH.downPress) direction = "down";
			if (keyH.upPress && keyH.downPress) direction = "stilldown";
			if (keyH.leftPress) direction = "left";
			if (keyH.rightPress) direction = "right";
			if (keyH.leftPress && keyH.rightPress) direction = "stilldown";
			if (keyH.leftPress && keyH.upPress) direction = "upleft";
			if (keyH.rightPress && keyH.upPress) direction = "upright";
			if (keyH.leftPress && keyH.downPress) direction = "downleft";
			if (keyH.rightPress && keyH.downPress) direction = "downright";
			isMoving = true;
		} else {
			if (Objects.equals(old_direction, "left")) direction = "stillleft";
			if (Objects.equals(old_direction, "right")) direction = "stillright";
			if (Objects.equals(old_direction, "up")) direction = "stillup";
			if (Objects.equals(old_direction, "down")) direction = "stilldown";
			isMoving = false;
		}
	}

	public void update() {
		move();
		inv.update();
		spriteCounter(4);
	}

	public void draw(Graphics2D g2) {
		BufferedImage image = null;
		switch (direction) {
			case "stillright":
				if (spriteNum == 1) image = sr;
				if (spriteNum == 2) image = sr;
				if (spriteNum == 3) image = sr;
				if (spriteNum == 4) image = sr;
				break;
			case "stillup":
				if (spriteNum == 1) image = s1u;
				if (spriteNum == 2) image = s2u;
				if (spriteNum == 3) image = s1u;
				if (spriteNum == 4) image = s2u;
				break;
			case "up", "upright", "upleft":
				if (spriteNum == 1) image = u1;
				if (spriteNum == 2) image = s1u;
				if (spriteNum == 3) image = u2;
				if (spriteNum == 4) image = s2u;
				break;
            case "down", "downleft", "downright":
				if (spriteNum == 1) image = d1;
				if (spriteNum == 2) image = s1d;
				if (spriteNum == 3) image = d2;
				if (spriteNum == 4) image = s2d;
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
				if (spriteNum == 1) image = s1d;
				if (spriteNum == 2) image = s2d;
				if (spriteNum == 3) image = s1d;
				if (spriteNum == 4) image = s2d;
				break;
			case "stillleft" :
				if (spriteNum == 1) image = sl;
				if (spriteNum == 2) image = sl;
				if (spriteNum == 3) image = sl;
				if (spriteNum == 4) image = sl;
		}

		switch (gp.gamestate) {
			case 11 :
				direction = "stilldown";
				g2.drawImage(image, gp.tileSize * 2, gp.tileSize, gp.tileSize * 2, gp.tileSize * 2, null);
				break;
			default :
				drawShadow(this, g2);
				g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
		}
	}
}