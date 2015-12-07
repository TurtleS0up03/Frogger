package frogger;

import org.newdawn.slick.opengl.Texture;
import static helpers.Artist.*;
import static helpers.Clock.*;

public class Enemy {
	/**
	 * Declaring the variables in reference to the enemies
	 **/
	private int width, height, health;
	private float x, y, speed;
	private Texture texture;
	private Tile startTile;
	private boolean first = true; // temp fix to the first time the method is
									// called

	// Enemy constructor
	public Enemy(Texture texture, Tile startTile, int width, int height,
			float speed) {
		this.texture = texture;
		this.startTile = startTile;
		this.x = startTile.getX();
		this.y = startTile.getY();
		this.width = width;
		this.height = height;
		this.speed = speed;

	}// EoConstruct

	/**
	 * The first update the clocks ignores because we dont need
	 * 
	 * then the clock will calculate the Delta as scheduled
	 **/
	public void Update() {
		if (first)
			first = false;
		else
			x += Delta() * speed;
	}// EoM

	/**
	 * Method is mainly used for just showing what is on the screen where it is
	 **/
	public void Draw() {
		DrawQuadTex(texture, x, y, width, height);
	}// EoM

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

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

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public Tile getStartTile() {
		return startTile;
	}

	public void setStartTile(Tile startTile) {
		this.startTile = startTile;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

}// EoC
