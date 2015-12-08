package frogger;


import static helpers.Artist.DrawQuadTex;
import static helpers.Artist.QuickLoad;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import org.newdawn.slick.opengl.Texture;

public class CoinGenerator extends TimerTask {

	// Generate coin.png at random locations on map for 10 extra points. . . .
	private int width, height;
	private float x, y, minX = 0.0f, maxX = 18.0f, minY = 0.0f, maxY = 15.0f;
	private Texture texture;
	private Tile tile;
	// private CoinGenerator coin;
	Tile c = new Tile(0, 0, 64, 64, tile.getType());
	ArrayList<CoinGenerator> coins;
	Random rnx = new Random();
	Random rny = new Random();
	Timer timer = new Timer();

	// Coin Constructor
	public CoinGenerator(Texture texture, Tile tile, int width, int height) {
		this.texture = texture;
		this.tile = tile;
		this.x = tile.getX();
		this.y = tile.getY();
		this.width = width;
		this.height = height;
		coins = new ArrayList<CoinGenerator>();
	}// EoConstruct

	@Override
	/** Method for adding the new coins to the array-list **/
	public void run() {
		x = rnx.nextFloat() * (maxX - minX) + minX;
		y = rny.nextFloat() * (maxY - minY) + minY;

		timer.schedule(new CoinGenerator(QuickLoad("coin"), c, 64, 64), 5000, 0);
	}// EoM

	/**
	 * Method is mainly used for just showing what is on the screen where it is
	 **/
	public void Draw() {
		DrawQuadTex(texture, x, y, width, height);
	}// EoM

	/**
	 * Generated getters and setters for private variables
	 **/
	public float getMinX() {
		return minX;
	}

	public void setMinX(float minX) {
		this.minX = minX;
	}

	public float getMaxX() {
		return maxX;
	}

	public void setMaxX(float maxX) {
		this.maxX = maxX;
	}

	public float getMinY() {
		return minY;
	}

	public void setMinY(float minY) {
		this.minY = minY;
	}

	public float getMaxY() {
		return maxY;
	}

	public void setMaxY(float maxY) {
		this.maxY = maxY;
	}

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

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public Tile getTile() {
		return tile;
	}

	public void setTile(Tile tile) {
		this.tile = tile;
	}
}// EoC
