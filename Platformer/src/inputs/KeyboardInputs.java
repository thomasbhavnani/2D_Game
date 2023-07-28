package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.GamePanel;

public class KeyboardInputs implements KeyListener{

	private GamePanel gamePanel;
	public KeyboardInputs(GamePanel gamePanel) {
		// have the GamePanel access inside the keyboard inputs
		this.gamePanel = gamePanel;
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		
		case KeyEvent.VK_W:
			System.out.println("its W");
			// why does -5 move the rectangle up in the panel?
			gamePanel.changeYDelta(-5);
			break;
		case KeyEvent.VK_A:
			System.out.println("its A");
			gamePanel.changeXDelta(-5);
			break;
		case KeyEvent.VK_S:
			System.out.println("its S");
			gamePanel.changeYDelta(5);
			break;
		case KeyEvent.VK_D:
			System.out.println("its D");
			gamePanel.changeXDelta(5);
			break;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		
	}
	
}

