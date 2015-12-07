package frogger;

//CHANGE VARIABLE NAMES WHEN DONE

import org.newdawn.slick.opengl.*;//imports the slick texture interface, needed for texture
import static helpers.Artist.*;

public class Tile {

	private float x, y, width, height;
	private Texture texture;
	private TileType type;

	/**
	 * Constructor used to initialize the variables for a tile (just setting the
	 * variables equal to what they are, nothing special
	 **/
	public Tile(float x, float y, float width, float height, TileType type) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.type = type;
		this.texture = QuickLoad(type.textureName);
	}// EoConstruct

	public void Draw() {
		DrawQuadTex(texture, x, y, width, height);
	}

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

	public TileType getType() {
		return type;
	}

	public void setType(TileType type) {
		this.type = type;
	}
}// EoC
