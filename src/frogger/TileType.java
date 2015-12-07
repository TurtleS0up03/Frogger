package frogger;


public enum TileType {

	// Creating grass and dirt using the constructor
	Grass("grass", true), Dirt("dirt", true), Road("road", true), Water(
			"water", true), Coin("coin", true), Turtle("turtle", true), Kraken(
			"kraken", true), Crocodile("crocodile", true);

	String textureName;
	Boolean buildable;

	// creating the constructor (boolean means whether or not we can build on
	// the tile)
	TileType(String textureName, boolean buildable) {
		this.textureName = textureName;
		this.buildable = buildable;
	}
}// EoC
