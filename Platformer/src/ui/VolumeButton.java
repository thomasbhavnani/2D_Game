package ui;



import java.awt.Graphics;
import java.awt.image.BufferedImage;

import utilz.LoadSave;
import static utilz.Constants.UI.VolumeButtons.*;

public class VolumeButton extends PauseButton{

	private BufferedImage[] imgs;
	private BufferedImage slider;
	private int index = 0;
	private boolean mouseOver, mousePressed;
	private int buttonX, minX, maxX;
	private float floatValue = 0f;
	public VolumeButton(int x, int y, int width, int height) {
		super(x + width/2, y, VOLUME_WIDTH, height);
		
		// x boundary of volume slider
		bounds.x -= VOLUME_WIDTH / 2;
		// set volume slider to middle initially
		buttonX = x + width/2;
		
		// x and width for slider
		this.x = x;
		this.width = width;
		
		minX = x + VOLUME_WIDTH / 2 ; 
		maxX = x + width - VOLUME_WIDTH / 2;
		loadImgs();
	}
	
	private void loadImgs() {
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.VOLUME_BUTTONS);
		imgs = new BufferedImage[3];
		for(int i = 0; i < imgs.length; i++)
			imgs[i] = temp.getSubimage(i * VOLUME_DEFAULT_WIDTH, 0, VOLUME_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
		
		slider = temp.getSubimage(3 * VOLUME_DEFAULT_WIDTH, 0, SLIDER_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
	}

	public void update() {
		index = 0;
		if(mouseOver)
			index = 1;
		if(mouseOver)
			index = 2;
	}

	public void draw(Graphics g) {
		g.drawImage(slider, x, y, width, height, null);
		g.drawImage(imgs[index], buttonX - VOLUME_WIDTH / 2, y, VOLUME_WIDTH, height, null);
	}
	
	// changing x positon of sliding button
	public void changeX(int x) {
		if(x < minX)
			buttonX = minX;
		else if (x > maxX)
			buttonX = maxX;
		else
			buttonX = x;
		updateFloatValue();
		// move slider hitbox to the center of the mouse when it drags
		bounds.x = buttonX - VOLUME_WIDTH / 2;
	}
	
	private void updateFloatValue() {
		// used to control the volume based on how far you drag the slider
		float range = maxX - minX;
		float value = buttonX - minX;
		floatValue = value / range;
		
	}

	public void resetBools() {
		mouseOver = false;
		mousePressed = false;
	}

	public boolean isMouseOver() {
		return mouseOver;
	}

	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	public boolean isMousePressed() {
		return mousePressed;
	}

	public void setMousePressed(boolean mousePressed) {
		this.mousePressed = mousePressed;
	}
	
	public float getFloatValue() {
		return floatValue;
	}

}
