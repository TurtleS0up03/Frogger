package sprites;


// FrogSprite.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* A sprite can move left/right, jump and stand still.
   In fact, a sprite doesn't move horizontally at all, so
   the left and right movement requests only change various
   status flags, not its locx value.

   The sprite has looping images for when it is moving
   left or right, and single images for when it is
   standing still or jumping.

   The sprite stores its world coordinate in (xWorld, yWorld).

   Jumping has a rising and falling component. Rising and 
   falling can be stopped by the sprite hitting a brick.

   The sprite's movement left or right can be stopped by hitting 
   a brick.

   A sprite will start falling if it walks off a brick into space.

   Brick queries (mostly about collision detection) are sent
   to the BricksManager object.
*/

import helpers.ImagesLoader;

import java.awt.*;


public class FrogSprite extends Sprite
{
  private static double DURATION = 0.5;  // secs
    // total time to cycle through all the images

 
  private static final int MAX_UP_STEPS = 8;
    // max number of steps to take when rising upwards in a jump


  private int period;    // in ms; the game's animation period

  private boolean isFacingRight, isStill, isFacingUp;

  private enum MoveMode {up, down, left, right, still};
  private MoveMode move;
    /* can be NOT_JUMPING, RISING, or FALLING */
  private int step;   // distance to move vertically in one step
  private int upCount;


  private int xWorld, yWorld;
    /* the current position of the sprite in 'world' coordinates.
       The x-values may be negative. The y-values will be between
       0 and pHeight. */ 
  

  public FrogSprite(int w, int h, ImagesLoader imsLd, int p)
  {
    super(w/2, h/2, w, h, imsLd, "runningRight");
       // standing center screen, facing right

          // the move size is the same as the bricks ribbon

    //brickMan = bm;
    period = p;
    setStep(0,0);     // no movement

    isFacingRight = true;
    isStill = true;

    /* Adjust the sprite's y- position so it is
       standing on the brick at its mid x- psoition. */
    locy = 101;
    xWorld = locx; yWorld = locy;    // store current position

    move = MoveMode.still;
    step = 5;   
                // the jump step is half a brick's height
    upCount = 0;
  }  // end of FrogSprite()


  public void moveLeft()
  /* Request that the sprite move to the left. It doesn't
     actually move, but changes its image and status flags. */
  { if (move == MoveMode.still){
		upCount = 0;
		move = MoveMode.left;
		if(isStill){
			  
		  setImage("runningLeft"); 
		  loopImage(period, DURATION);   // cycle through the images
		  isFacingRight = false;
		  isStill = false;
		  isFacingUp = false;
		}
	  }
  }

  public void moveRight()
  /* Request that the sprite move to the right. It doesn't
     actually move, but changes its image and status flags. */
  { if (move == MoveMode.still){
	  upCount = 0;
	  move = MoveMode.right;
		if(isStill){
			  
		  setImage("runningRight"); 
		  loopImage(period, DURATION);   // cycle through the images
		  isFacingRight = true;  
		  isStill = false;
		  isFacingUp = false;
		}
  	}
  }

  public void stayStill()
  /* Request that the sprite stops. It stops the
     image animation and sets the isStill status flag. */
  { stopLooping(); 
    isStill = true;
  }


  public void moveUp()
  /* The sprite is asked to jump. It sets its vertMoveMode
     to RISING, and changes its image. The y- position
     adjustment is done in updateSprite().
  */
  { if (move == MoveMode.still){
	  upCount = 0;
	  move = MoveMode.up;
		if(isStill){
			  
		  setImage("jumpUp"); 
		  loopImage(period, DURATION);   // cycle through the images
		  isStill = false;
		  isFacingUp = true;
		  isFacingRight = false;
		}
  	}
  } // end of jump()
  
  
  
  
  public void moveDown()
  /* The sprite is asked to jump. It sets its vertMoveMode
     to RISING, and changes its image. The y- position
     adjustment is done in updateSprite().
  */
  { if (move == MoveMode.still){
	  upCount = 0;
	  move = MoveMode.down;
		if(isStill){
			  
		  setImage("jumpDown"); 
		  loopImage(period, DURATION);   // cycle through the images
		  isStill = false;
		  isFacingUp = false;
		  isFacingRight = false;
		}
  	}
  } // end of jump()




  public void updateSprite()
  /* Although the sprite is not moving in the x-direction, we 
     must still update its (xWorld, yWorld) coordinate. Also,
     if the sprite is jumping then its y position must be
     updated with moveVertically(). updateSprite() should
     only be called after collsion checking with willHitBrick()
  */
  {

      if (move == MoveMode.up)
    	  updateGoingUp();// may have moved out into empty space
      if(move == MoveMode.down)
    	  updateGoingDown();
      
      if ( move == MoveMode.left)
    	  updateGoingLeft();
      if ( move == MoveMode.right)
    	  updateGoingRight();
      

    super.updateSprite();
  }  // end of updateSprite()


private void updateGoingUp(){
	if (upCount == MAX_UP_STEPS){
		finishJumping();
	}
	else {
		locy = locy-step;
		upCount++;
	}
}

private void updateGoingDown(){
	if (upCount == MAX_UP_STEPS){
		finishJumping();
	}
	else {
		locy = locy+step;
		upCount++;
	}
	
}

private void updateGoingLeft(){
	if (upCount == MAX_UP_STEPS){
		finishJumping();
	}
	else {
		locx = locx-step;
		upCount++;
	}
}


private void updateGoingRight(){
	if (upCount == MAX_UP_STEPS){
		finishJumping();
	}
	else {
		locx = locx+step;
		upCount++;
	}
}


  private void finishJumping()
  {
    move = MoveMode.still;
    upCount = 0;
    setStepX(0);
    setStepY(0);
    stayStill();
    if (isStill) {    // change to running image, but not looping yet
      if (isFacingRight)
        setImage("runningRight");
      else    // facing left
        setImage("runningLeft");
      if (isFacingUp)
    	  setImage("jumpUp");
      else
    	 setImage("jumpDown"); 
    }
  }  


}  // end of FrogSprite

