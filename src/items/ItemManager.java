package items;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import main.GamePanel;
import main.KeyHandler;

public class ItemManager {
	private final GamePanel gp;
	private final KeyHandler keyH;
	public Item[] items;
	public Item[] hotbar;
	public Item inHand;
	public int selected;
	private int imageOffset;
	private int hotbarBoxSize;
	

	public ItemManager(GamePanel gp, KeyHandler keyH) {
		this.gp = gp;
		this.keyH = keyH;
		init();
	}

	private synchronized void init() {
		items = new Item[20];
		hotbar = new Item[6];
		selected = 0;
		imageOffset = 7;
		hotbarBoxSize = gp.tileSize / 2;
		hotbar[0] = new Weapon("ak47",gp,keyH);
		hotbar[1] = new Throwable("poison",gp,keyH);
	}

	public synchronized void update() {
		selected += (int) keyH.mouseWheelRotation;
		
		if(selected == hotbar.length) selected = 0;
		else if(selected == -1) selected = hotbar.length - 1;
		
		for (int i = 0; i < hotbar.length; i++)
		{
			if(hotbar[i] != null) {
				hotbar[i].slot = i;
				if(i == selected) {
					hotbar[i].inHand = true;
					inHand = hotbar[i];
				}else {
					hotbar[i].inHand = false;
				}
			}
		}
		
		if(inHand != null) {
			inHand.update();
		}
		
		keyH.mouseWheelRotation = 0;
	}

	public synchronized void draw(Graphics2D g2) {
		switch (gp.gamestate)
		{
			default:
				for (int i = 0; i < hotbar.length; i++)
				{
					int x = gp.screenWidth / 2 - hotbarBoxSize * (hotbar.length)
							/ 2 + i * hotbarBoxSize;
					int y = (int) (gp.screenHeight - hotbarBoxSize * 1.5);
					g2.setColor(Color.lightGray);
					g2.fillRect(x, y, hotbarBoxSize, hotbarBoxSize);
					if (selected == i) {
						g2.setStroke(new BasicStroke(4));
						g2.setColor(Color.red);
						g2.drawRect(x + 2, y + 2,
								hotbarBoxSize - 3, hotbarBoxSize - 3);
					}
					
					g2.setStroke(new BasicStroke(1));
					g2.setColor(Color.white);
					g2.drawRect(x, y, hotbarBoxSize, hotbarBoxSize);
					
					if (hotbar[i] != null) {
						g2.drawImage(
								hotbar[i].image, x + imageOffset,
								y + imageOffset,
								hotbarBoxSize - imageOffset * 2,
								hotbarBoxSize - imageOffset * 2,
								null);
					}

					
				}
				break;
			case 11:
				g2.setStroke(new BasicStroke(0));
				g2.setColor(Color.darkGray);
				g2.fillRect(
						hotbarBoxSize,
						hotbarBoxSize,
						gp.screenWidth - 600,
						gp.screenHeight - 200);
				g2.setColor(Color.white);

				for (int i = 0; i <= 5; i++) {
					for (int y = 0; y < 5; y++){
						if (y != 4) {
							g2.drawRect(
									gp.tileSize + gp.tileSize * 3 + i * gp.tileSize,
									gp.tileSize + y * gp.tileSize,
									gp.tileSize,
									gp.tileSize);
						} else {
							g2.drawRect(
									gp.tileSize + gp.tileSize * 3 + i * gp.tileSize, 
									(int) (gp.tileSize * 1.5 + (y * gp.tileSize)),
									gp.tileSize,
									gp.tileSize);
						}
					}
				}
				break;
		}
	}
}