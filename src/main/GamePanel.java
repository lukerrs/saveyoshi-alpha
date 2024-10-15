package main;

import entities.*;
import menu.Menu;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import tiles.TileManager;

import static java.lang.Thread.sleep;

@SuppressWarnings("unused")
public class GamePanel extends JPanel {
	private static final String Version = "0.1";
	public int originalTileSize = 64;
	public double scale = 1.6;
	public int tileSize = (int) (originalTileSize * scale);
	public int screenWidth;
	public int screenHeight;
	public final int maxWorldCol = 100;
	public final int maxWorldRow = 50;
	public final double worldWidth = maxWorldCol * tileSize;
	public final double worldHeight = maxWorldRow * tileSize;
	public int tickRate = 128;
	public int refreshRate = 540;
	public int gamestate = 0;
	public int mouseX = 0;
	public int mouseY = 0;
	public boolean fullscreen;
	public KeyHandler keyH;
	public TileManager tileM;
	public CollisionChecker colC;
	public Player player;
	public Companion yoshi;
	public Enemy e1;
	public Game game;
	public Camera cam;
	public Menu menu;
	public EntityManager entityManager;

	public SoundPlayer soundP;
	public Dimension screenSize;
	public int prvGamestate;
	public boolean pinkTeint;

	public final Thread gamePanelThread = new Thread(new Runnable() {
		final double updateInterval = ((double) 1000000000 / tickRate);
		double nextUpdateTime = System.nanoTime() + updateInterval;
		public void run() {
			while(!game.closed) {
				update();
				try {
					double remainingTime = nextUpdateTime - System.nanoTime();
					remainingTime /= 1000000;
					if (remainingTime < 0) remainingTime = 0;
					sleep((long) remainingTime);
					nextUpdateTime += updateInterval;
				} catch (InterruptedException ignored) {
				}
			}
		}
	});

	public final Thread drawGraphicsThread = new Thread(new Runnable() {
		final double updateInterval = ((double) 1000000000 / refreshRate);
		final double nextUpdateTime = System.nanoTime() + updateInterval;
		public void run() {
			while(!game.closed) {
					repaint();
			}
		}
	});

	public GamePanel(Game game, boolean fullscreen) {
		this.game = game;
		this.fullscreen = fullscreen;
		init();
	}

	private synchronized void init() {
		setScreenSize();
		setBackground(Color.darkGray);
		setDoubleBuffered(false);
		setFocusable(true);
		requestFocus();
		keyH = new KeyHandler();
		pinkTeint = true;
		menu = new Menu(this,keyH);
		addKeyListener(keyH);
		addMouseListener(keyH);
		addMouseWheelListener(keyH);
		gamePanelThread.start();
		drawGraphicsThread.start();
		cam = new Camera(this,keyH);
		tileM = new TileManager(this);
		colC = new CollisionChecker(this);
		player = new Player(this, keyH);
		yoshi = new Companion(this, keyH);
		//e1 = new Enemy(this, keyH);
		entityManager = new EntityManager(this, keyH);
		soundP = new SoundPlayer();
	}

	public synchronized void setScreenSize()
	{
		if (fullscreen)
		{
			screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
			screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
			screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		}
		else
		{
			screenWidth = 1024;
			screenHeight = 768;
			screenSize = new Dimension(screenWidth,screenHeight);
		}
		setPreferredSize(screenSize);

		try {
			cam.init(); menu.init(); player.setScreenPosition();
		}
		catch ( NullPointerException ignored) { }
	}

	private void switchMode() {

		switch(gamestate) {
		case 0:
			break;
		case 1:
			if(keyH.escPress) { setGamestate(3); keyH.escPress = false; }
			if(keyH.f3Press) { setGamestate(2); keyH.f3Press = false; }
			if(keyH.iPress) { setGamestate(11); keyH.iPress = false; }
			break;
		case 11:
			if(keyH.iPress) { setGamestate(1); keyH.iPress = false; }
			break;
		case 2:
			if(keyH.f3Press) { setGamestate(1); keyH.f3Press = false; }
			break;
		case 3:
			if(keyH.escPress) { setGamestate(1); keyH.escPress = false; }
			break;
		case 31:
			if(keyH.escPress) { setGamestate(3); keyH.escPress = false; }
			break;
		}
	}

	public void update()
	{
		switch (gamestate)
		{
			case 0 :
				menu.update();
				break;
			case 1, 2:
				switchMode();
				tileM.update();
				entityManager.update();
				break;
			case 11 :
				switchMode();
				break;
            case 3, 31:
				switchMode();
				menu.update();
				break;
            case 311:
				break;
			case 999:
				System.exit(0);
				break;
		}
		getMousePosOnScreen();
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
			switch (gamestate)
			{
				case 0 :
					menu.draw(g2);
					break;
				case 1, 2:
					tileM.draw(g2);
					entityManager.draw(g2);
					player.inv.draw(g2);
					menu.drawCrosshair(g2);
					break;
				case 11 :
					tileM.draw(g2);
					entityManager.draw(g2);
					player.inv.draw(g2);
					menu.drawCursor(g2);
					break;
                case 3, 31:
					tileM.draw(g2);
					entityManager.draw(g2);
					menu.draw(g2);
					break;
                case 311:
					break;
			}

			//Pink Teint
			if(pinkTeint)
			{
				g2.setColor(new Color(1.0F, 0.0F, 1.0F, 0.1F));
				g2.fillRect(0, 0, screenWidth, screenHeight);
			}

			//Brightness
			g2.setColor(new Color(0.0F, 0.0F, 0.0F, 0.0F));
			g2.fillRect(0, 0, screenWidth, screenHeight);

			g2.dispose();
	}

	public void getMousePosOnScreen()
	{
		try
		{
			mouseX = getMousePosition().x;
			mouseY = getMousePosition().y;
		} catch (Exception ignore) { }
	}

	public Point getMousePosOnMap()
	{
		int mouseXRelativeToMap = (int) (player.worldX - player.screenX + mouseX);
		int mouseYRelativeToMap = (int) (player.worldY - player.screenY + mouseY);
		return new Point(mouseXRelativeToMap,mouseYRelativeToMap);
	}

	public synchronized void resetWindowSize() throws InterruptedException
	{
		gamestate = 311;
		game.window.remove(this);
		game.window.dispose();
        fullscreen = !fullscreen;
		game.window.setUndecorated(fullscreen);
		setScreenSize();
		game.window.setSize(screenSize);
		int x = Toolkit.getDefaultToolkit().getScreenSize().width / 2 - screenWidth / 2;
		int y = Toolkit.getDefaultToolkit().getScreenSize().height / 2 - screenHeight / 2;
		game.window.setLocation(x, y);
		game.window.add(this);
		game.window.setVisible(true);
		game.window.pack();
		gamestate = 31;
	}

	public void setGamestate(int gamestate)
	{
		this.prvGamestate = this.gamestate;
		this.gamestate = gamestate;
	}

	public String fullscreenToString()
	{
		if (fullscreen) return "true";
		return "false";
	}

	public String teintToString()
	{
		if (pinkTeint) return "true";
		return "false";
	}
}