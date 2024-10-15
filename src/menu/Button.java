package menu;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;

public class Button
{
	boolean pressable;
	boolean state;
	int screenX;
	int screenY;
	int width;
	int height;
	int textSize;
	int textOffset;
	Font f;
	String name;
	String text;

	BufferedImage exitButton;
	BufferedImage startButton;
	BufferedImage exitButtonPressable;
	BufferedImage startButtonPressable;
	GamePanel gp;
	KeyHandler keyH;

	public Button(String name, GamePanel gp, KeyHandler keyH)
	{
		this.name = name;
		this.text = name;
		this.gp = gp;
		this.keyH = keyH;
		getButtonImage();
		init();
	}
	
	public Button(String name, String text, GamePanel gp, KeyHandler keyH)
	{
		this.name = name;
		this.text = text; 
		this.gp = gp;
		this.keyH = keyH;
		getButtonImage();
		init();
	}

	private synchronized void init()
	{
		textSize = 64;
		textOffset = 10;
		f = new Font("RuneScape UF", Font.BOLD, textSize);
		FontRenderContext frc = new FontRenderContext(new AffineTransform(),true,true);  
		
		switch (name) {
			case "Exit" :
				height = (int) (30*gp.scale);
				width = (int) (30*gp.scale);
				screenX = gp.screenWidth - width - gp.screenWidth /20;
				screenY = gp.screenHeight /20;
				break;
			case "Start", "Back":
				this.width = (int) (f.getStringBounds(text, frc).getWidth());
				this.height = (int)(f.getStringBounds(text, frc).getHeight());
				screenX = gp.screenWidth - width - gp.screenWidth /20;
				screenY = gp.screenHeight * 9/10 - height/2;
				break;
			case "Settings":
				this.width = (int) (f.getStringBounds(text, frc).getWidth());
				this.height = (int)(f.getStringBounds(text, frc).getHeight());
				screenX = gp.screenWidth - width - gp.screenWidth /20;
				screenY = gp.screenHeight * 9/10 - height - height/2 - 4;
				break;
			case "Fullscreen":
				this.width = (int) (f.getStringBounds(text, frc).getWidth());
				this.height = (int)(f.getStringBounds(text, frc).getHeight());
				screenX = gp.screenWidth/20;
				screenY = gp.screenHeight/20;
				state = gp.fullscreen;
				break;
			case "Scaling":
				this.width = (int) (f.getStringBounds(text, frc).getWidth());
				this.height = (int)(f.getStringBounds(text, frc).getHeight());
				screenX = gp.screenWidth/20;
				screenY = gp.screenHeight/20 +height + 5;
				break;
			case "Teint":
				this.width = (int) (f.getStringBounds(text, frc).getWidth());
				this.height = (int)(f.getStringBounds(text, frc).getHeight());
				screenX = gp.screenWidth/20;
				screenY = gp.screenHeight/20 +(height + 5) * 2;
        }
	}

	private void getButtonImage()
	{
		try
		{
			exitButton = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/hud/exitButton.png")));
			startButton = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/hud/startButton.png")));
			exitButtonPressable = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/hud/exitButtonPressable.png")));
			startButtonPressable = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/hud/startButtonPressable.png")));
			System.out.println("Success: Successfully loaded button resources");
		} catch (IOException e) {
			System.out.println("Error: Couldn't load button resources");
		}
	}
	
	public synchronized void update()
	{
        pressable = gp.mouseX > screenX &&
                gp.mouseX < screenX + width &&
                gp.mouseY > screenY &&
                gp.mouseY < screenY + height;

		if (pressable && (keyH.m1 || keyH.m2))
		{
			if(keyH.m1) {
				switch (name)
				{
					case "Exit" :
						switch(gp.gamestate) {
						case 0:
							gp.setGamestate(999);
							break;
						case 3, 31:
							keyH.m1 = false;
							gp.setGamestate(0);
							break;
						}
						break;
					case "Start" :
						keyH.m1 = false;
						gp.setGamestate(1);
						break;
					case "Settings" :
						keyH.m1 = false;
						gp.setGamestate(31);
						break;
					case "Fullscreen":
						try {
							gp.resetWindowSize();
						} catch (InterruptedException e) {
							throw new RuntimeException(e);
						}
						keyH.m1 = false;
						if(gp.entityManager.entityList != null)
						{
							for(int i = 0; i < gp.entityManager.entityList.size(); i++)
								gp.entityManager.entityList.get(i).update();
						}
						gp.repaint();
						break;
					case "Back":
						switch (gp.gamestate)
						{
						default:
							keyH.m1 = false;
							gp.setGamestate(gp.prvGamestate);
							break;
						case 3:
							keyH.m1 = false;
							gp.setGamestate(1);
							break;
						}
					case "Scaling":
						keyH.m1 = false;
						break;
					case "Teint":
						keyH.m1 = false;
						gp.pinkTeint = !gp.pinkTeint;
						text = "PINK TEINT: " + gp.teintToString().toUpperCase();
						init();
						//gp.repaint();
						break;
				}
			}
		}
	}

	public synchronized void draw(Graphics2D g2) 
	{
		switch (name) 
		{
			case "Exit" :
				g2.drawImage(exitButton, screenX, screenY, width, height, null);
				if (pressable) {
					g2.drawImage(exitButtonPressable, screenX, screenY, width, height, null);
				}
				break;
			default:
				g2.setColor(Color.WHITE);
				g2.fillRect(screenX - textOffset, screenY, width + textOffset, height);
				g2.setColor(Color.DARK_GRAY);
				g2.setFont(f);
				g2.drawString(text, screenX - textOffset / 2, screenY + height - textOffset);
				if (pressable)
				{
					g2.setColor(Color.BLACK);
					g2.setStroke(new BasicStroke(4));
					g2.drawRect(screenX - textOffset, screenY, width + textOffset, height);
				}
		}
	}
}