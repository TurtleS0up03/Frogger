package frogger;


import static helpers.Artist.*;

public class TileGrid {

	public Tile[][] map;

	public TileGrid() {
		map = new Tile[19][15];
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				map[i][j] = new Tile(i * 64, j * 64, 64, 64, TileType.Grass);
			}
		}
	}// EoConstruct

	public TileGrid(int[][] newMap) {
		map = new Tile[19][15];
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				/**
				 * 0 = Grass 1 = Dirt 2 = Water 3 = Road
				 **/
				switch (newMap[j][i]) {
				case 0:
					map[i][j] = new Tile(i * 64, j * 64, 64, 64, TileType.Grass);
					break;
				case 1:
					map[i][j] = new Tile(i * 64, j * 64, 64, 64, TileType.Dirt);
					break;
				case 2:
					map[i][j] = new Tile(i * 64, j * 64, 64, 64, TileType.Water);
					break;
				case 3:
					map[i][j] = new Tile(i * 64, j * 64, 64, 64, TileType.Road);
					break;

				}// End of the switch statement
			}
		}
	}// EoConstruct

	/** MAY NOT ACTUALLY NEED GET AND SET METHODS **/
	// Method for the setters of specific tiles on the grid
	public void SetTile(int xCoord, int yCoord, TileType type) {
		map[xCoord][yCoord] = new Tile(xCoord * 64, yCoord * 64, 64, 64, type);
	}

	// Returns tile to allow enemies to know where frog is
	public Tile GetTile(int xCoord, int yCoord) {
		return map[xCoord][yCoord];
	}

	public void Draw() {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				Tile t = map[i][j];
				DrawQuadTex(t.getTexture(), t.getX(), t.getY(), t.getWidth(),
						t.getHeight());
			}
		}
	}// EoM
}// EoC
