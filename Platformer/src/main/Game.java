package main;

public class Game {
	
	private GameWindow gameWindow;
	private GamePanel gamePanel;
	
	public Game() {
		gamePanel = new GamePanel();
		gameWindow = new GameWindow(gamePanel);
		// input focuses on the jpanel
		gamePanel.requestFocus();
		
	}
}
