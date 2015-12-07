package frogger;

import static helpers.Artist.BeginSession;
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

	// image, bricks map, clips loader information files
	private static final String IMS_INFO = "imsInfo.txt";

	private final static String MIDIS_FILE = "midisInfo.txt";
	// names of the explosion clips
	private static final String[] exploNames = { "explo1", "explo2", "explo3" };

	private static final int MAX_HITS = 20;
	// number of times frog can be hit by a car before the game is over

	private Thread animator; // the thread that performs the animation
	private volatile boolean running = false; // used to stop the animation
												// thread
	private volatile boolean isPaused = false;

	private long period; // period between drawing in _nanosecs_

	private Frogger frogTop;
	// private ClipsLoader clipsLoader;

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

	private int numHits = 0; // the number of times 'frog' has been hit
	
	

	public FrogPanel() {
			
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

			TileGrid grid = new TileGrid(map);
			grid.SetTile(3, 4, grid.GetTile(2, 4).getType());

			// Small Log
			Enemy e1 = new Enemy(QuickLoad("dirt"), grid.GetTile(0, 5), 64, 64, 20);// 5
			// is
			// the
			// speed
			Enemy e2 = new Enemy(QuickLoad("dirt"), grid.GetTile(1, 5), 64, 64, 20);// 5
			// is
			// the
			// speed
			Enemy e3 = new Enemy(QuickLoad("dirt"), grid.GetTile(2, 5), 64, 64, 20);// 5
			// is
			// the
			// speed

			// Long Log
			Enemy e5 = new Enemy(QuickLoad("dirt"), grid.GetTile(0, 3), 64, 64, 30);// 5
			// is
			// the
			// speed
			Enemy e6 = new Enemy(QuickLoad("dirt"), grid.GetTile(1, 3), 64, 64, 30);// 5
			// is
			// the
			// speed
			Enemy e7 = new Enemy(QuickLoad("dirt"), grid.GetTile(2, 3), 64, 64, 30);// 5
			// is
			// the
			// speed
			Enemy e8 = new Enemy(QuickLoad("dirt"), grid.GetTile(3, 3), 64, 64, 30);// 5
			// is
			// the
			// speed
			Enemy e9 = new Enemy(QuickLoad("dirt"), grid.GetTile(4, 3), 64, 64, 30);// 5
			// is
			// the
			// speed

			// Turtles
			Enemy e4 = new Enemy(QuickLoad("turtle"), grid.GetTile(0, 6), 64, 64,
					15);// 5
			// is
			// the
			// speed
			Enemy e00 = new Enemy(QuickLoad("turtle"), grid.GetTile(0, 4), 64, 64,
					35);// 5
			// is
			// the
			// speed
			Enemy e01 = new Enemy(QuickLoad("turtle"), grid.GetTile(0, 2), 64, 64,
					45);// 5
			// is
			// the
			// speed

			// Small Log Waves
			Wave wave1 = new Wave(15, e1);// 10 is how milliseconds till another
			// enemy spawns
			Wave wave2 = new Wave(15, e2);// 10 is how milliseconds till another
			// enemy spawns
			Wave wave3 = new Wave(15, e3);// 10 is how milliseconds till another
			// enemy spawns

			// Turtle Waves
			Wave wave4 = new Wave(13, e4);// 10 is how milliseconds till another
			// enemy spawns
			Wave wave00 = new Wave(13, e00);// 10 is how milliseconds till another
			// enemy spawns
			Wave wave01 = new Wave(5, e01);// 10 is how milliseconds till another
			// enemy spawns

			// Long Log wave
			Wave wave5 = new Wave(15, e5);// 10 is how milliseconds till another
			// enemy spawns
			Wave wave6 = new Wave(15, e6);// 10 is how milliseconds till another
			// enemy spawns
			Wave wave7 = new Wave(15, e7);// 10 is how milliseconds till another
			// enemy spawns
			Wave wave8 = new Wave(15, e8);// 10 is how milliseconds till another
			// enemy spawns
			Wave wave9 = new Wave(15, e9);// 10 is how milliseconds till another
			// enemy spawns

			CoinGenerator c = new CoinGenerator(QuickLoad("coin"), grid.GetTile(0,
					0), 64, 64);
			cgWave wavec = new cgWave(5000, c);
			//frogmusic = new Audio();
			while (!Display.isCloseRequested()) {// While were not hitting the x
				// button
				Clock.update();
				grid.Draw();// Draw the grid
				wave1.Update();// Update the waves
				wave2.Update();// Update the waves
				wave3.Update();// Update the waves
				wave4.Update();// Update the waves
				wave5.Update();// Update the waves
				wave6.Update();// Update the waves
				wave7.Update();// Update the waves
				wave8.Update();// Update the waves
				wave9.Update();// Update the waves
				wave00.Update();// Update the waves
				wave01.Update();// Update the waves
				wavec.Update();

				Display.update();
				Display.sync(80);
			}
			//frogmusic.stop();
			Display.destroy();
		

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
		// clipsLoader = new ClipsLoader(SNDS_FILE);

		// initialise the game entities
		// bricksMan = new BricksManager(PWIDTH, PHEIGHT, BRICKS_INFO,
		// imsLoader);
		// int brickMoveSize = bricksMan.getMoveSize();

		// ribsMan = new RibbonsManager(PWIDTH, PHEIGHT, brickMoveSize,
		// imsLoader);

		frog = new FrogSprite(PWIDTH, PHEIGHT, imsLoader,
				(int) (period / 1000000L)); // in ms

		car = new CarSprite(PWIDTH, PHEIGHT, imsLoader, this, frog);

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
	} // end of JackPanel()

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

	private void startGame()
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
