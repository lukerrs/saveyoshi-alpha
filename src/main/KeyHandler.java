package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class KeyHandler implements KeyListener, MouseListener, MouseWheelListener {
	public boolean upPress;
	public boolean downPress;
	public boolean leftPress;
	public boolean rightPress;
	public boolean iPress;
	public boolean enterPress; 
	public boolean escPress;
	public boolean f3Press;
	public boolean invToggle;
	public boolean f3Toggle;
	public boolean escToggle;
	public boolean m1,m1r;
	public boolean m2;
	public boolean m3;
	public boolean mWheel;
	public int currentkeyCode;
	public double mouseWheelRotation;
	public boolean keyPressed;
	
	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		keyPressed = true;
		currentkeyCode = e.getKeyCode();
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_W) upPress = true;
		if (code == KeyEvent.VK_S) downPress = true;
		if (code == KeyEvent.VK_A) leftPress = true;
		if (code == KeyEvent.VK_D) rightPress = true;
		if (code == KeyEvent.VK_I) iPress = true;
		if (code == KeyEvent.VK_ENTER) enterPress = true;
		if (code == KeyEvent.VK_ESCAPE) escPress = true;
		if (code == KeyEvent.VK_F3) f3Press = true;
	}

	public void keyReleased(KeyEvent e) {
		keyPressed = false;
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_W) upPress = false;
		if (code == KeyEvent.VK_S) downPress = false;
		if (code == KeyEvent.VK_A) leftPress = false;
		if (code == KeyEvent.VK_D) rightPress = false;
		if (code == KeyEvent.VK_I) iPress = false;
		if (code == KeyEvent.VK_ENTER) enterPress = false;
		if (code == KeyEvent.VK_ESCAPE) escPress = false;
		if (code == KeyEvent.VK_F3) f3Press = false;
		
		if (code == KeyEvent.VK_F3) f3Toggle = !f3Toggle;
		if (code == KeyEvent.VK_ESCAPE) escToggle = !escToggle;
		if (code == KeyEvent.VK_I) invToggle = !invToggle;
	}

	public boolean isKeyPressed(int keyCode) {
		return keyPressed && currentkeyCode == keyCode;
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		int code = e.getButton();
		if (code == 1) { m1 = true; m1r = false; }
		if (code == 2) m2 = true;
		if (code == 3) m3 = true;
	}

	public void mouseReleased(MouseEvent e) {
		int code = e.getButton();
		if (code == 1) { m1 = false; m1r = true; }
		if (code == 2) m2 = false;
		if (code == 3) m3 = false;
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public int getKeyCode() {
		return currentkeyCode;
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		mouseWheelRotation = e.getPreciseWheelRotation();
	}
}