package entities;

import items.ItemManager;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.GamePanel;
import main.KeyHandler;

public abstract class Entity {
	public double worldX, worldY;
	public int screenX;
	public int screenY;
	public int width;
	public int height;
	public double speed;
	public int health;
	public int healthMax;
	public int damage;
	BufferedImage u1, u2, u3, u4;
	BufferedImage d1;
	BufferedImage d2;
	BufferedImage r1;
	BufferedImage r2;
	BufferedImage l1;
	BufferedImage l2;
	BufferedImage s1u;
	BufferedImage s2u;
	public BufferedImage s1d;
	BufferedImage s2d;
	BufferedImage s3d;
	BufferedImage s4d;
	BufferedImage sl;
	BufferedImage sr;
	BufferedImage p1;
	BufferedImage p2;
	BufferedImage p3;
	BufferedImage p4;
	BufferedImage p5;
	BufferedImage p6;
	BufferedImage p7;
	BufferedImage p8;
	public String direction;
	public String old_direction;
	public int spriteCounter = 0;
	public int spriteNum = 1;
	public Rectangle hitbox;
	public boolean collision = false;
	public boolean isMoving = false;
	public ItemManager inv;
	public boolean isAlive;
	public boolean isHostile;
	public boolean collidable;
	final GamePanel gp;
	final KeyHandler keyH;
	public long lastTimeDamaged;
	public boolean drawingHealthbar;

	public Entity(GamePanel gp, KeyHandler keyH)
	{
		this.gp = gp;
		this.keyH = keyH;
		isAlive = true;
		collidable = true;
	}

	protected void drawShadow(Entity entity, Graphics2D g2)
	{
		g2.setColor(new Color(0.0F, 0.0F, 0.0F, 0.3F));
		g2.fillOval(entity.screenX + entity.hitbox.x,
				entity.screenY + entity.hitbox.y + entity.hitbox.height - entity.hitbox.height / 4, entity.hitbox.width,
				entity.hitbox.height / 2);
	}

	public void drawHealthbar(Entity entity, Graphics2D g2)
	{
		if(isAlive)
		{
			int x = (int) (entity.screenX + entity.hitbox.x + entity.hitbox.width/2 - (entity.healthMax / 4)*gp.scale);
			int y = (int) (entity.screenY - 25*gp.scale/2);
			int w = (int) (entity.health*gp.scale/2);
			int wMax = (int) (entity.healthMax*gp.scale/2);
			int h = (int) (10*gp.scale/2);
			
			g2.setStroke(new BasicStroke(2));
			g2.setColor(new Color(1,0,0.5F,0.5F));
			g2.fillRect(x, y, wMax, h);
			
			g2.setStroke(new BasicStroke(2));
			g2.setColor(Color.red);
			g2.fillRect(x, y, w, h);
			
			g2.setStroke(new BasicStroke(2));
			g2.setColor(Color.lightGray);
			g2.drawRect(x, y, wMax, h);
		}
	}
	
	public void takeDamage(int damage)
	{
		health -= damage;
		lastTimeDamaged = System.currentTimeMillis();
	}

	protected void die()
	{
		isAlive = false;
		gp.entityManager.entityList.remove(this);
	}

	protected void spriteCounter(int num) {
		spriteCounter++;
		int time = switch (num) {
            case 2 -> 60;
            case 3 -> 50;
            case 4 -> 40;
            case 8 -> 20;
            default -> throw new IllegalStateException("Unexpected value: " + num);
        };

        if (spriteCounter > time) {
			spriteNum++;
			spriteCounter = 0;
		}

		if (spriteNum > num) {
			spriteNum = 1;
		}
	}

	public abstract void draw(Graphics2D g2);

	public abstract void update();
}