package items;

import java.awt.image.BufferedImage;

public abstract class Item {
	public int id;
	public int worldX;
	public int screenX;
	public int worldY;
	public int screenY;
	public int width;
	public int height;
	public int damage;
	public int slot;
	public BufferedImage image;
	public int spriteCounter = 0;
	public int spriteNum = 1;
	public boolean inHand;
	
	public abstract void update();
}