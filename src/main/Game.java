package main;

import java.awt.*;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import javax.swing.*;
import javax.imageio.ImageIO;

public class Game {
	public String title;
	public String version;
	public JFrame window;
	public GamePanel gamePanel = new GamePanel(this,false);
	Image icon;
	boolean closed = false;

	public static void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog( new ScrollPane(1), infoMessage,titleBar, JOptionPane.ERROR_MESSAGE);
    }
	
	public static void main(String[] args) {
		new Game("Save Yoshi!");
	}

	public Game(String title)
	{
		init(title);
	}
	
	private void init(String title) {
		this.title = title;
		version = "0.1.4";

		window = new JFrame();
		loadImageFiles();
		window.setIconImage(icon);
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setUndecorated(false);
		window.setResizable(false);
		window.setTitle(title);
		int x = Toolkit.getDefaultToolkit().getScreenSize().width/2 - gamePanel.screenWidth/2;
		int y = Toolkit.getDefaultToolkit().getScreenSize().height/2 - gamePanel.screenHeight/2;
		window.setLocation(x,y);
		window.setVisible(true);
		window.add(gamePanel);
		window.pack();
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
				new BufferedImage(16, 16, 2),new Point(0, 0), "blank cursor");
		window.getContentPane().setCursor(blankCursor);

	}
	
	private void loadImageFiles() {
		try {
			icon = ImageIO.read(Objects.requireNonNull(getClass().getResource("/program/icon.png")));
			System.out.println("Success: Successfully loaded program resources");
		} catch (IOException e) {
			System.out.println("Error: Couldn't load program resources");
		}
	}
	
}