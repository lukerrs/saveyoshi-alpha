package main;

public class Camera {
	public static int x,y,w,h;
	GamePanel gp;
	KeyHandler keyH;
	
	Camera(GamePanel gp, KeyHandler keyH)
	{
		this.gp = gp;
		this.keyH = keyH;
		init();
	}
	
	public synchronized void init() {
		w = gp.screenWidth;
		h = gp.screenHeight;
		x = gp.screenWidth/2;
		y = gp.screenHeight/2;
	}
	public static int getCameraX() {System.out.println("xpos of camera:" + x);return x;}
	public static int getCameraY() {System.out.println("ypos of camera:" + y);return y;}
}
