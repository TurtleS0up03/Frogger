package frogger;

import static helpers.Artist.BeginSession;
import static helpers.Artist.DrawQuadTex;
import static helpers.Artist.QuickLoad;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import helpers.Clock;
import helpers.ImagesLoader;
import helpers.ImagesPlayer;
import helpers.ImagesPlayerWatcher;

import javax.swing.JPanel;

import org.lwjgl.opengl.Display;

import sprites.CarSprite;
import sprites.FrogSprite;

import com.sun.j3d.utils.timer.J3DTimer;

public class FrogPanel extends JPanel implements Runnable, ImagesPlayerWatcher {
	
	private static final int PWIDTH = 500; // size of panel
	private static final int PHEIGHT = 360;


	private static final int NO_DELAYS_PER_YIELD = 16;
	/*
	 * Number of frames with a delay of 0 ms before the animation thread yields
	 * to other running threads.
	 */
	private static final int MAX_FRAME_SKIPS = 5;
	// no. of frames that can be skipped in any one animation loop
	// i.e the games state is updated but not rendered

	private static final String[] exploNames = { "explo1", "explo2", "explo3" };
	// names of the explosion clips
	
	private static final int MAX_HITS = 20;
	// number of times frog can be hit by a car before the game is over

	private Thread animator; // the thread that performs the animation
	private volatile boolean running = false; // used to stop the animation
												// thread
	private volatile boolean isPaused = false;

	private long period; // period between drawing in _nanosecs_

	
	private FrogSprite frog; // the sprites
	private CarSprite car;

	private long gameStartTime; // when the game started
	private int timeSpentInGame;

	// used at game termination
	private volatile boolean gameOver = false;
	private int score = 0;

	// for displaying messages
	private Font msgsFont;
	private FontMetrics metrics;

	// off-screen rendering
	private Graphics dbg;
	private Image dbImage = null;

	// to display the title/help screen
	private boolean showHelp;
	private BufferedImage helpIm;

	// explosion-related
	private ImagesPlayer explosionPlayer = null;
	private boolean showExplosion = false;
	private int explWidth, explHeight; // image dimensions
	private int xExpl, yExpl; // coords where image is drawn
	private static final String IMS_INFO = "imsInfo.txt";
	private int numHits = 0; // the number of times 'frog' has been hit
	
	public FroggerView view;

	public FrogPanel(FroggerView view) {
			this.view = view;
			
			BeginSession();

			// Since our map is 20x15 we must create each index for it
			int[][] map = {
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0 },
					{ 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 },
					{ 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 },
					{ 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 },
					{ 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 },
					{ 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 },
					{ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 },
					{ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 },
					{ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 },
					{ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, };
			
			
			FroggerController cntrl = new FroggerController(this);
			
			cntrl.makeGrid(map);
			
			// creates the map grid
			
			//this.period = period;

			setDoubleBuffered(false);
			setBackground(Color.white);
			setPreferredSize(new Dimension(PWIDTH, PHEIGHT));

			setFocusable(true);
			requestFocus(); // the JPanel now has focus, so receives key events

			addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					processKey(e);
				}
			});

			// initialise the loaders
			ImagesLoader imsLoader = new ImagesLoader(IMS_INFO);
			

			frog = new FrogSprite(PWIDTH, PHEIGHT, imsLoader,
					(int) (period / 1000000L)); // in ms

			car = new CarSprite(PWIDTH, PHEIGHT, imsLoader, view, frog);

			// prepare the explosion animation
			explosionPlayer = new ImagesPlayer("explosion",
					(int) (period / 1000000L), 0.5, false, imsLoader);
			BufferedImage explosionIm = imsLoader.getImage("explosion");
			explWidth = explosionIm.getWidth();
			explHeight = explosionIm.getHeight();
			explosionPlayer.setWatcher(this); // report animation's end back here

			// prepare title/help screen
			helpIm = imsLoader.getImage("title");
			showHelp = true; // show at start-up
			isPaused = true;

			// set up message font
			msgsFont = new Font("SansSerif", Font.BOLD, 24);
			metrics = this.getFontMetrics(msgsFont);
		
	}
	private void processKey(KeyEvent e)
	// handles termination, help, and game-play keys
	{
		int keyCode = e.getKeyCode();

		// termination keys
		// listen for esc, q, end, ctrl-c on the canvas to
		// allow a convenient exit from the full screen configuration
		if ((keyCode == KeyEvent.VK_ESCAPE) || (keyCode == KeyEvent.VK_Q)
				|| (keyCode == KeyEvent.VK_END)
				|| ((keyCode == KeyEvent.VK_C) && e.isControlDown()))
			running = false;

		// help controls
		if (keyCode == KeyEvent.VK_H) {
			if (showHelp) { // help being shown
				showHelp = false; // switch off
				isPaused = false;
			} else { // help not being shown
				showHelp = true; // show it
				isPaused = true; // isPaused may already be true
			}
		}

		// game-play keys
		if (!isPaused && !gameOver) {
			// move the sprite and ribbons based on the arrow key pressed
			if (keyCode == KeyEvent.VK_LEFT) {
				frog.moveLeft();

			} else if (keyCode == KeyEvent.VK_RIGHT) {
				frog.moveRight();
			} else if (keyCode == KeyEvent.VK_UP)
				frog.moveUp(); // jumping has no effect on the bricks/ribbons
			else if (keyCode == KeyEvent.VK_DOWN) {
				frog.moveDown();

			}
		}
	} // end of processKey()
	
	
	


	public void showExplosion(int x, int y)
	// called by car sprite when it hits frog at (x,y)
	{
		if (!showExplosion) { // only allow a single explosion at a time
			showExplosion = true;
			xExpl = x - explWidth / 2; // \ (x,y) is the center of the explosion
			yExpl = y - explHeight / 2;

			/*
			 * Play an explosion clip, but cycle through them. This adds
			 * variety, and gets round not being able to play multiple instances
			 * of a clip at the same time.
			 */
			// clipsLoader.play(exploNames[numHits % exploNames.length], false);
			numHits++;
		}
	} // end of showExplosion()

	public void sequenceEnded(String imageName)
	// called by ImagesPlayer when the explosion animation finishes
	{
		showExplosion = false;
		explosionPlayer.restartAt(0); // reset animation for next time

		if (numHits >= MAX_HITS) {
			gameOver = true;
			score = (int) ((J3DTimer.getValue() - gameStartTime) / 1000000000L);
			// clipsLoader.play("applause", false);

		}
	} // end of sequenceEnded()

	public void addNotify()
	// wait for the JPanel to be added to the JFrame before starting
	{
		super.addNotify(); // creates the peer
		startGame(); // start the thread
	}

	void startGame()
	// initialise and start the thread
	{
		if (animator == null || !running) {
			animator = new Thread(this);
			animator.start();
		}
	} // end of startGame()

	// ------------- game life cycle methods ------------
	// called by the JFrame's window listener methods

	public void resumeGame()
	// called when the JFrame is activated / deiconified
	{
		if (!showHelp) // CHANGED
			isPaused = false;
	}

	public void pauseGame()
	// called when the JFrame is deactivated / iconified
	{
		isPaused = true;
	}

	public void stopGame()
	// called when the JFrame is closing
	{
		running = false;
	}

	// ----------------------------------------------

	public void run()
	/* The frames of the animation are drawn inside the while loop. */
	{
		long beforeTime, afterTime, timeDiff, sleepTime;
		long overSleepTime = 0L;
		int noDelays = 0;
		long excess = 0L;

		gameStartTime = J3DTimer.getValue();
		beforeTime = gameStartTime;

		running = true;

		while (running) {
			gameUpdate();
			gameRender();
			paintScreen();

			afterTime = J3DTimer.getValue();
			timeDiff = afterTime - beforeTime;
			sleepTime = (period - timeDiff) - overSleepTime;

			if (sleepTime > 0) { // some time left in this cycle
				try {
					Thread.sleep(sleepTime / 1000000L); // nano -> ms
				} catch (InterruptedException ex) {
				}
				overSleepTime = (J3DTimer.getValue() - afterTime) - sleepTime;
			} else { // sleepTime <= 0; the frame took longer than the period
				excess -= sleepTime; // store excess time value
				overSleepTime = 0L;

				if (++noDelays >= NO_DELAYS_PER_YIELD) {
					Thread.yield(); // give another thread a chance to run
					noDelays = 0;
				}
			}

			beforeTime = J3DTimer.getValue();

			/*
			 * If frame animation is taking too long, update the game state
			 * without rendering it, to get the updates/sec nearer to the
			 * required FPS.
			 */
			int skips = 0;
			while ((excess > period) && (skips < MAX_FRAME_SKIPS)) {
				excess -= period;
				gameUpdate(); // update state but don't render
				skips++;
			}
		}
		System.exit(0); // so window disappears
	} // end of run()

	private void gameUpdate() {
		if (!isPaused && !gameOver) {

			frog.updateSprite();
			car.updateSprite();

			if (showExplosion)
				explosionPlayer.updateTick(); // update the animation
		}
	} // end of gameUpdate()

	private void gameRender() {
		if (dbImage == null) {
			dbImage = createImage(PWIDTH, PHEIGHT);
			if (dbImage == null) {
				System.out.println("dbImage is null");
				return;
			} else
				dbg = dbImage.getGraphics();
		}

		// draw a white background
		dbg.setColor(Color.white);
		dbg.fillRect(0, 0, PWIDTH, PHEIGHT);

		// draw the game elements: order is important
		// ribsMan.display(dbg); // the background ribbons
		// bricksMan.display(dbg); // the bricks
		frog.drawSprite(dbg); // the sprites
		car.drawSprite(dbg);

		if (showExplosion) // draw the explosion (in front of frog)
			dbg.drawImage(explosionPlayer.getCurrentImage(), xExpl, yExpl, null);

		reportStats(dbg);

		if (gameOver)
			gameOverMessage(dbg);

		if (showHelp) // draw the help at the very front (if switched on)
			dbg.drawImage(helpIm, (PWIDTH - helpIm.getWidth()) / 2,
					(PHEIGHT - helpIm.getHeight()) / 2, null);
	} // end of gameRender()

	private void reportStats(Graphics g)
	// Report the number of hits, and time spent playing
	{
		if (!gameOver) // stop incrementing the timer once the game is over
			timeSpentInGame = (int) ((J3DTimer.getValue() - gameStartTime) / 1000000000L); // ns
																							// -->
																							// secs
		g.setColor(Color.red);
		g.setFont(msgsFont);
		g.drawString("Hits: " + numHits + "/" + MAX_HITS, 15, 25);
		g.drawString("Time: " + timeSpentInGame + " secs", 15, 50);
		g.setColor(Color.black);
	} // end of reportStats()

	private void gameOverMessage(Graphics g)
	// Center the game-over message in the panel.
	{
		String msg = "Game Over. Your score: " + score;

		int x = (PWIDTH - metrics.stringWidth(msg)) / 2;
		int y = (PHEIGHT - metrics.getHeight()) / 2;
		g.setColor(Color.black);
		g.setFont(msgsFont);
		g.drawString(msg, x, y);
	} // end of gameOverMessage()

	private void paintScreen()
	// use active rendering to put the buffered image on-screen
	{
		Graphics g;
		try {
			g = this.getGraphics();
			if ((g != null) && (dbImage != null))
				g.drawImage(dbImage, 0, 0, null);
			// Sync the display on some systems.
			// (on Linux, this fixes event queue problems)
			Toolkit.getDefaultToolkit().sync();
			g.dispose();
		} catch (Exception e) {
			System.out.println("Graphics context error: " + e);
		}
	} // end of paintScreen()

		

}
