package entities;

import items.ItemManager;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
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

	private synchronized void init()
	{
		getPlayerImage();
		inv = new ItemManager(this.gp, this.keyH);
		worldX = (double) (gp.tileSize * gp.maxWorldCol) / 2;
		worldY = (double) (gp.tileSize * gp.maxWorldRow) / 2;
		screenX = Camera.x - gp.tileSize/2;
		screenY = Camera.y - gp.tileSize/2;
		width = gp.tileSize;
		height = gp.tileSize;
		speed = 1.5 * gp.scale;
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
	
	public synchronized void setScreenPosition()
	{
		screenX = Camera.x - gp.tileSize/2;
		screenY = Camera.y - gp.tileSize/2;
	}

	private synchronized void getPlayerImage()
	{
		try {
			s1u = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Main_still_up_1.png")));
			s2u = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Main_still_up_2.png")));
			u1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Main_walk_up_1.png")));
			u2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Main_walk_up_2.png")));
			s1d = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Main_still_down_1.png")));
			s2d = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Main_still_down_2.png")));
			d1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Main_walk_down_1.png")));
			d2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Main_walk_down_2.png")));
			sl = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Main_still_left.png")));
			l1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Main_walk_left_1.png")));
			l2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Main_walk_left_2.png")));
			sr = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Main_still_right.png")));
			r1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Main_walk_right_1.png")));
			r2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Main_walk_right_2.png")));
			
			/*s1u = ImageIO.read(getClass().getResourceAsStream("/player/Serena_back_still.png"));
			s2u = ImageIO.read(getClass().getResourceAsStream("/player/Serena_back_still.png"));
			u1 = ImageIO.read(getClass().getResourceAsStream("/player/Serena_back_run_1.png"));
			u2 = ImageIO.read(getClass().getResourceAsStream("/player/Serena_back_run_3.png"));
			s1d = ImageIO.read(getClass().getResourceAsStream("/player/Serena_front_still.png"));
			s2d = ImageIO.read(getClass().getResourceAsStream("/player/Serena_front_still.png"));
			d1 = ImageIO.read(getClass().getResourceAsStream("/player/Serena_front_run_1.png"));
			d2 = ImageIO.read(getClass().getResourceAsStream("/player/Serena_front_run_3.png"));
			sl = ImageIO.read(getClass().getResourceAsStream("/player/Serena_still_l.png"));
			l1 = ImageIO.read(getClass().getResourceAsStream("/player/Serena_run_l1.png"));
			l2 = ImageIO.read(getClass().getResourceAsStream("/player/Serena_run_l2.png"));
			sr = ImageIO.read(getClass().getResourceAsStream("/player/Serena_still_r.png"));
			r1 = ImageIO.read(getClass().getResourceAsStream("/player/Serena_run_r1.png"));
			r2 = ImageIO.read(getClass().getResourceAsStream("/player/Serena_run_r2.png"));*/
			
			System.out.println("Success: Successfully loaded player resources");
		} catch (IOException e) {
			System.out.println("Error: Couldn't load player resources");
		}
	}

	private synchronized void move() {
		collision = false;

		try {
			gp.colC.checkTile(this);
		} catch (Exception e) { }
		
		if (!collision && isMoving) {
			if (direction == "up") worldY -= speed;
			else if (Objects.equals(direction, "down")) worldY += speed;
			else if (Objects.equals(direction, "right")) worldX += speed;
			else if (Objects.equals(direction, "left")) worldX -= speed;
			else if (Objects.equals(direction, "upright")) { worldX += speed/2; worldY -= speed * 3/4; }
			else if (Objects.equals(direction, "upleft")) { worldX -= speed/2; worldY -= speed * 3/4; }
			else if (Objects.equals(direction, "downright")) { worldX += speed/2; worldY += speed * 3/4; }
			else if (Objects.equals(direction, "downleft")) { worldX -= speed/2; worldY += speed * 3/4; }
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

	public synchronized void update() {
		move();
		inv.update();
		spriteCounter(4);
	}

	public synchronized void draw(Graphics2D g2) {
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
			case "up" :
				if (spriteNum == 1) image = u1;
				if (spriteNum == 2) image = s1u;
				if (spriteNum == 3) image = u2;
				if (spriteNum == 4) image = s2u;
				break;
			case "upleft" :
				if (spriteNum == 1) image = u1;
				if (spriteNum == 2) image = s1u;
				if (spriteNum == 3) image = u2;
				if (spriteNum == 4) image = s2u;
				break;
			case "upright" :
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