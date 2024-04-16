package menu;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;

public class Menu
{
	GamePanel gp;
	KeyHandler keyH;
	Button start;
	Button exit;
	Button settings, fullscreenSetting, scalingSetting, back;
	String state;
	Font font;
	int textSize;
	private BufferedImage background;
	private BufferedImage crosshair;
	private BufferedImage cursor;
	private BufferedImage companion;
	private BufferedImage player;
	
	
	public Menu(GamePanel gp, KeyHandler keyH)
	{
		this.gp = gp;
		this.keyH = keyH;
		init();
		loadMenuImages();
	}
	
	public synchronized void init()
	{
		textSize = (int) (70 * gp.scale);
		font = new Font("RuneScape UF", Font.PLAIN, textSize);
		start = new Button( "Start", "PLAY", gp, keyH);
		exit = new Button("Exit", gp, keyH);
		back = new Button("Back", "BACK", gp, keyH);
		settings = new Button("Settings", "SETTINGS", gp, keyH);
		fullscreenSetting = new Button("Fullscreen", "FULLSCREEN: " + gp.isFullscreen().toUpperCase(), gp, keyH);
		scalingSetting = new Button("Scaling", "SCALING: " + gp.scale, gp, keyH);
		state = "mainmenu";
	}
	
	private synchronized void loadMenuImages()
	{
		try
		{
			background = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/hud/mainmenu.png")));
			crosshair = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/hud/crosshair.png")));
			cursor = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/hud/cursor.png")));
			companion = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/companion/yoshi_walk_down_1.png")));
			player = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Main_still_down_1.png")));
			System.out.println("Success: Successfully loaded menu resources");
		}
		catch (IOException e)
		{
			System.out.println("Error: Couldn't load menu resources");
		}
	}
	
	public synchronized void setState()
	{
		switch (gp.gamestate)
		{
		case 0:
			state = "mainmenu";
			break;
		case 3:
			state = "pause";
			break;
		case 31:
			state = "settings";
			break;
		}
	}
	
	public synchronized void drawCursor(Graphics2D g2) 
	{
		int curWH = gp.tileSize;
		int cursorX = gp.mouseX - curWH / 6;
		int cursorY = gp.mouseY - curWH / 6;
		g2.drawImage(cursor, cursorX, cursorY, curWH, curWH, null);
	}
	
	public synchronized void drawCrosshair(Graphics2D g2)
	{
		int crossWH = gp.tileSize / 2;
		int crosshairX = gp.mouseX - crossWH / 2;
		int crosshairY = gp.mouseY - crossWH / 2;
		g2.drawImage(crosshair, crosshairX, crosshairY, crossWH, crossWH, null);
	}
	
	public synchronized void drawVersion(Graphics2D g2) {
		font = new Font("font", Font.BOLD,16);
		final String version = "v"+gp.game.version;
		g2.setFont(font);
		FontRenderContext frc = new FontRenderContext(new AffineTransform(),true,true); 
		int width = (int) (font.getStringBounds(gp.game.version, frc).getWidth());
		int height = (int) (font.getStringBounds(gp.game.version, frc).getHeight());
		int x = gp.screenWidth/20;
		int y = gp.screenHeight - gp.screenHeight /10 + height/2;
		g2.setColor(Color.darkGray);
		g2.drawString(version, x, y);
	}
	
	public synchronized void update() 
	{
		setState();
		switch(state)
		{
		case "mainmenu":
			settings.update();
			start.update();
			exit.update();
			break;
		case "pause":
			settings.update();
			exit.update();
			back.update();
			break;
		case "settings":
			fullscreenSetting.update();
			scalingSetting.update();
			exit.update();
			back.update();
			break;
		}
	}
	
	public synchronized void draw(Graphics2D g2) 
	{
		setState();
		font = new Font("RuneScape UF", Font.BOLD, textSize);
		g2.setFont(font);
		switch(state) 
		{
		case "mainmenu":
			g2.drawImage(background, gp.screenWidth/2 - background.getWidth()/2,
					gp.screenHeight - background.getHeight(), null);
			
			g2.drawImage(
					companion, 
					(int) (gp.screenWidth/2 - companion.getWidth()*2.5),
					gp.screenHeight - companion.getHeight()*4,
					companion.getWidth()*2,
					companion.getHeight()*2,
					null);
			
			g2.drawImage(
					player, 
					(int) (gp.screenWidth/2 - player.getWidth()*1.5),
					(int) ( gp.screenHeight - player.getHeight() * 5.25 ),
					player.getWidth()*3,
					player.getHeight()*3,
					null);
			
			settings.draw(g2);
			start.draw(g2);
			exit.draw(g2);
			
			//SHow Title
			FontRenderContext frc = new FontRenderContext(new AffineTransform(),true,true); 
			GlyphVector glyphVector = font.createGlyphVector(frc, gp.game.title);
			
			int width = (int) (font.getStringBounds(gp.game.title, frc).getWidth());
			int height = (int) (font.getStringBounds(gp.game.title, frc).getHeight());
			int x = gp.screenWidth/2 - width/2;
			int y = gp.screenHeight/10 + height/2;
			Shape textShape = glyphVector.getOutline(x,y);
			//g2.drawString(gp.game.title, x, y);
			g2.setColor(Color.DARK_GRAY);
	        g2.setStroke(new BasicStroke(8));
	        g2.draw(textShape);

	        g2.setColor(Color.cyan);
	        g2.fill(textShape); // fill the shape
			
	        //Show Version
	        drawVersion(g2);
	        
			drawCursor(g2);
			break;
		case "pause":
			g2.setColor(new Color(0.0F, 0.0F, 0.0F, 0.4F));
			g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
			settings.draw(g2);
			exit.draw(g2);
			back.draw(g2);
			drawCursor(g2);
			break;
		case "settings":
			switch(gp.prvGamestate)
			{
			case 0:
				g2.drawImage(background, gp.screenWidth/2 - background.getWidth()/2,
						gp.screenHeight - background.getHeight(), null);
				g2.setColor(new Color(0.0F, 0.0F, 0.0F, 0.4F));
				g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
				break;
			case 3:
				g2.setColor(new Color(0.0F, 0.0F, 0.0F, 0.4F));
				g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
				break;
			}
			scalingSetting.draw(g2);
			fullscreenSetting.draw(g2);
			exit.draw(g2);
			back.draw(g2);
			drawCursor(g2);
			break;
		}
	}
}
