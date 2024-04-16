package entities;

import java.awt.Graphics2D;
import java.util.List;
import java.util.Random;

import main.GamePanel;
import main.KeyHandler;

public class EntityManager {
	GamePanel gp;
	KeyHandler keyH;
	List<Entity> entityList;
	int randomCounter;
	int randomIntervalInSeconds;
	int state;
	
	public EntityManager(List<Entity> entityList, GamePanel gp, KeyHandler keyH)
	{
		this.gp = gp;
		this.keyH = keyH;
		this.entityList = entityList;
		state = 0;
	}
	
	public synchronized void newRandomState() {
		Random rnd = new Random();
		switch(rnd.nextInt(1,1))
		{
			case 1:
				state = 1;
				fourEnemysEncirclePlayer();
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				break;
			case 7:
				break;
			case 8:
				break;
		}
	}
	
	public synchronized void fourEnemysEncirclePlayer()
	{
		double x = gp.player.worldX + (double) gp.player.width /2;
		double y = gp.player.worldY + (double) gp.player.height /2;
		Enemy e1 = new Enemy(x + gp.tileSize,y + gp.tileSize, "greyGhost",gp,keyH);
		Enemy e2 = new Enemy(x + gp.tileSize,y - gp.tileSize, "greyGhost",gp,keyH);
		Enemy e3 = new Enemy(x - gp.tileSize,y + gp.tileSize, "greyGhost",gp,keyH);
		Enemy e4 = new Enemy(x - gp.tileSize,y - gp.tileSize, "greyGhost",gp,keyH);
		entityList.add(e1);
		entityList.add(e2);
		entityList.add(e3);
		entityList.add(e4);
		
		if(!e1.isAlive && !e2.isAlive && !e3.isAlive && !e4.isAlive) {
			state = 0;
			entityList.remove(e1);
			entityList.remove(e2);
			entityList.remove(e3);
			entityList.remove(e4);
		}
	}
	
	public synchronized void update()
	{
		randomCounter++;
		if(randomCounter > gp.tickRate * randomIntervalInSeconds)
		{
			fourEnemysEncirclePlayer();
		}
	}
	
	public synchronized void draw(Graphics2D g2)
	{
		
	}
}
