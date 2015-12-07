package sprites;

import frogger.*;
import helpers.ImagesLoader;

import java.awt.Rectangle;

public class CarSprite extends Sprite {
	// the ball's x- and y- step values are STEP +/- STEP_OFFSET
	private static final int STEP = -10; // moving left
	private static final int STEP_OFFSET = 2;

	private FrogPanel fp;
	private FrogSprite frog;

	public CarSprite(int w, int h, ImagesLoader imsLd, FrogPanel fp,
			FrogSprite frg) {
		super(w, h / 2, w, h, imsLd, "car");
		// the ball is positioned in the middle at the panel's rhs
		this.fp = fp;
		frog = frg;
		initPosition();
	} // end of CarSprite()

	private void initPosition()
	// adjust the car's position and its movement left
	{
		int h = getPHeight() / 2 + ((int) (getPHeight() * Math.random()) / 2);
		// along the lower half of the rhs edge
		if (h + getHeight() > getPHeight())
			h -= getHeight(); // so all on screen

		setPosition(getPWidth(), h);
		setStep(STEP + getRandRange(STEP_OFFSET), 0); // move left
	} // end of initPosition()

	private int getRandRange(int x)
	// random number generator between -x and x
	{
		return ((int) (2 * x * Math.random())) - x;
	}

	public void updateSprite() {
		hasHitJack();
		goneOffScreen();
		super.updateSprite();
	}

	private void hasHitJack()
	/*
	 * If the ball has hit frog, tell JackPanel (which will display an explosion
	 * and play a clip), and begin again.
	 */
	{
		Rectangle frogBox = frog.getMyRectangle();
		frogBox.grow(-frogBox.width / 3, 0); // make frog's bounded box thinner

		if (frogBox.intersects(getMyRectangle())) { // frog collision?
			fp.showExplosion(locx, locy + getHeight() / 2);
			// tell FrogPanel, supplying it with a hit coordinate
			initPosition();
		}
	} // end of hasHitJack()

	private void goneOffScreen()
	// when the ball has gone off the lhs, start it again.
	{
		if (((locx + getWidth()) <= 0) && (dx < 0)) // off left and moving left
			initPosition(); // start the ball in a new position
	} // end of goneOffScreen()

} // end of CarSprite class
