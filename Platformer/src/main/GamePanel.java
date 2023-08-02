package main;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;
import inputs.KeyboardInputs;
import inputs.MouseInputs;
import static main.Game.GAME_WIDTH;
import static main.Game.GAME_HEIGHT;
// get all the different player actions and methods in the utilz package


public class GamePanel extends JPanel{
	// MouseInputs implements both MouseListener and MouseMotionListener
	private MouseInputs mouseInputs;
	private Game game;

	public GamePanel(Game game) {
		mouseInputs = new MouseInputs(this);
		this.game = game;
		//pass GamePanel into the Keyboard inputs constructor
		addKeyListener( new KeyboardInputs(this));

		setPanelSize();
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
		
	}
	


	
	//set panel size not the window size so it does not include the border
	private void setPanelSize() {
		Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
		setPreferredSize(size);
		System.out.println("size: " + GAME_WIDTH + " : " + GAME_HEIGHT);
		
	}



	
	
	
	// update game logic
	public void updateGame() {

	}
	
	public void paintComponent(Graphics g) {
		// draws each frame
		// JComponent is the superclass of JPanel
		// calling JComponent's paintComponent
		// cleans the surface so you can draw
		super.paintComponent(g); 
		game.render(g);
		
	}
	
	public Game getGame() {
		return game;
	}
	


}
