package entities;

import java.awt.Color;
import java.awt.Graphics2D;

import main.GamePanel;
import main.KeyHandler;

public class Explosion extends Entity{
	int r;
	double liveTime;
	
	public Explosion(double worldX, double worldY, int r, int damage, GamePanel gp, KeyHandler keyH){
		super(gp, keyH);
		this.worldX = worldX;
		this.worldY = worldY;
		this.r = r;
		this.width = r;
		this.height = r;
		this.damage = damage;
	}
	
	public synchronized void update() {
		
	}
	
	public synchronized void draw(Graphics2D g2)
	{
		int x = gp.getMousePosOnMap().x;
		int y = gp.getMousePosOnMap().y;
		g2.setColor(Color.yellow);
		g2.fillRect(x,y,width,width);
	}
}
