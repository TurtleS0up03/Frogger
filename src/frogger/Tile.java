package frogger;

//CHANGE VARIABLE NAMES WHEN DONE

import org.newdawn.slick.opengl.*;//imports the slick texture interface, needed for texture

import static helpers.Artist.*;

public class Tile {

	private float x, y, width, height;
	private Texture texture;
	private String type;
	
	
	
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
	
	
	

	/**
	 * Constructor used to initialize the variables for a tile (just setting the
	 * variables equal to what they are, nothing special
	 **/
	public Tile(float x, float y, float width, float height, String type) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.type = type;
		this.texture = QuickLoad(type);
	}// EoConstruct
	


	/**
	 * Generate the getters and setters for other classes for Tile parameters
	 */
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}


	public void setType(String type) {
		this.type = type;
	}
	public String getType(){
		return this.type;
	}
}// EoC
