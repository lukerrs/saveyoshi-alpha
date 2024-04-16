package entities;

import java.awt.Graphics2D;

import main.GamePanel;
import main.KeyHandler;

public class Bullet extends Entity{
	String type;
	
	public Bullet(double worldX, double worldY, int w, int h, int damage, GamePanel gp, KeyHandler keyH)
	{
		super(gp, keyH);
		this.worldX = worldX;
		this.worldY = worldY;
		this.width = w;
		this.height = h;
		this.damage = damage;
	}
	
	public synchronized void draw(Graphics2D g2)
	{
		int x = gp.getMousePosOnMap().x;
		int y = gp.getMousePosOnMap().y;
		int w = 20;
		g2.fillRect(x,y,w,w);
	}

	public synchronized void update()
	{
		
	}
}

