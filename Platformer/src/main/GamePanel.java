package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import inputs.KeyboardInputs;
import inputs.MouseInputs;

// get all the different player actions and methods in the utilz package
import static utilz.Constants.PlayerConstants.*;
import static utilz.Constants.Directions.*;

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
		Dimension size = new Dimension(1280, 800);
		setMinimumSize(size);
		setPreferredSize(size);
		setMaximumSize(size);
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
