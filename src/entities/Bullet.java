package entities;

import java.awt.*;

import main.GamePanel;
import main.KeyHandler;

public class Bullet extends Entity{
	String type;
	double xSpeed;
	double ySpeed;
	
	public Bullet(double worldX, double worldY, int damage,double speed, double angle, GamePanel gp, KeyHandler keyH)
	{
		super(gp, keyH);
		this.worldX = worldX;
		this.worldY = worldY;
		this.width = gp.tileSize/8;
		this.height = width;
		this.damage = damage;
		this.speed = speed * gp.scale;
		this.xSpeed = speed * Math.cos(angle);
		this.ySpeed = speed * Math.sin(angle);
		isAlive = true;
		hitbox = new Rectangle();
		hitbox.x = (int) (0 * gp.scale);
		hitbox.y = (int) (0 * gp.scale);
		hitbox.width = (int) width;
		hitbox.height = (int) height;
		gp.entityManager.entityList.add(this);
	}

	public void die(){
		isAlive = false;
		gp.entityManager.entityList.remove(this);
	}
	
	public void draw(Graphics2D g2)
	{
		screenX = (int) (worldX - gp.player.worldX + gp.player.screenX);
		screenY = (int) (worldY - gp.player.worldY + gp.player.screenY);

		g2.setColor(Color.yellow);
		g2.fillRect(screenX,screenY,width, height);
	}

	public void update()
	{
		for(Entity entity: gp.entityManager.entityList) {
			gp.colC.checkBulletHit(this, entity);
		}

		worldX += xSpeed;
		worldY += ySpeed;

		if(worldX < 0 || worldX > gp.worldWidth || worldY < 0 || worldY > gp.worldHeight){
			this.die();
		}
	}
}

