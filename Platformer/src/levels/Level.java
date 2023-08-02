package levels;

public class Level {
	private int[][] lvlData;
	// takes in the indeces for the sprite array
	public Level(int[][] lvlData) {
		this.lvlData = lvlData;
	}
	
	// returns the sprite in the array at the index passed in
	public int getSpriteIndex(int x, int y) {
		// select row for the y index, then select a column for the x index
		return lvlData[y][x]; 
	}
	
	public int[][] getLevelData(){
		return lvlData;
	}

}
