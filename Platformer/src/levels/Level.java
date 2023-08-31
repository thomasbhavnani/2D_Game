package levels;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Crabby;
import main.Game;
import objects.Cannon;
import objects.GameContainer;
import objects.Potion;
import objects.Spike;
import utilz.HelpMethods;
import utilz.LoadSave;
import static utilz.HelpMethods.GetLevelData;
import static utilz.HelpMethods.GetCrabs;
import static utilz.HelpMethods.GetPlayerSpawn;

public class Level {
	
	private BufferedImage img;
	private int[][] lvlData;
	private ArrayList<Crabby> crabs;
	private ArrayList<Potion> potions;
	private ArrayList<GameContainer> containers;
	private ArrayList<Spike> spikes;
	private ArrayList<Cannon> cannons;
	private int lvlTilesWide;
	// difference how many tiles you can see and how many there actually are
	private int maxTilesOffset;
	// how many pixels is the maxTilesOffset
	private int maxLvlOffsetX;
	private Point playerSpawn;
	
	
	// takes in the indeces for the sprite array
	public Level(BufferedImage img) {
		this.img = img;
		createLevelData();
		createEnemies();
		createPotions();
		createContainers();
		createSpikes();
		createCannons();
		calcLvlOffset();
		calcPlayerSpawn();
	}
	
	private void createCannons() {
		cannons = HelpMethods.GetCannons(img);
		
	}

	private void createSpikes() {
		spikes = HelpMethods.GetSpikes(img);
		
	}

	private void createContainers() {
		containers = HelpMethods.GetContainers(img);
		
	}

	private void createPotions() {
		potions = HelpMethods.GetPotions(img);
		
	}

	private void calcPlayerSpawn() {
		playerSpawn = GetPlayerSpawn(img);
		
	}

	private void calcLvlOffset() {
		lvlTilesWide = img.getWidth();
		
		// getting the maximum amount of tiles the level can be offset by
		maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
		
		// getting the x value of the max level offset
		maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
		
	}

	private void createEnemies() {
		crabs = GetCrabs(img);
		
	}

	private void createLevelData() {
		lvlData = GetLevelData(img);
		
	}

	// returns the sprite in the array at the index passed in
	public int getSpriteIndex(int x, int y) {
		// select row for the y index, then select a column for the x index
		return lvlData[y][x]; 
	}
	
	public int[][] getLevelData(){
		return lvlData;
	}
	
	public int getLvlOffset() {
		return maxLvlOffsetX;
	}
	
	public ArrayList<Crabby> getCrabs(){
		return crabs;
	}
	
	public Point getPlayerSpawn() {
		return playerSpawn;
	}
	
	public ArrayList<Potion> getPotions(){
		return potions;
	}
	
	public ArrayList<GameContainer> getContainers(){
		return containers;
	}
	
	public ArrayList<Spike> getSpikes(){
		return spikes;
	}
	
	public ArrayList<Cannon> getCannons(){
		return cannons;
	}

}
