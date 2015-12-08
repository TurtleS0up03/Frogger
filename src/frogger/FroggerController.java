package frogger;

import static helpers.Artist.QuickLoad;
import helpers.Clock;
import helpers.ImagesLoader;
import helpers.ImagesPlayer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;






import org.lwjgl.opengl.Display;

import sprites.CarSprite;
import sprites.FrogSprite;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class FroggerController {
	public static FrogPanel panel;
	public FroggerController(FrogPanel p) {
		// TODO Auto-generated constructor stub
		this.panel = p;
	}
	
	
	public Tile[][] map;

	public void makeGrid(int mapOrder[][]) {
		map = new Tile[19][15];
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				String type = null;
				if(mapOrder[i][j] == 0){
					type = "grass";
				}else if( mapOrder[i][j] == 2){
					type = "water";
				}else if(mapOrder[i][j] == 3){
					type ="road";
				}
				map[i][j] = new Tile(i * 64, j * 64, 64, 64, type);
			}
		
		map[3][4].setType(map[2][4].getType()); 

		// Small Log
		Enemy e1 = new Enemy(QuickLoad("dirt"), map[0][5], 64, 64, 20);// 5
		// speed
		Enemy e2 = new Enemy(QuickLoad("dirt"), map[1][5], 64, 64, 20);// 5
		// speed
		Enemy e3 = new Enemy(QuickLoad("dirt"), map[2][5], 64, 64, 20);// 5
		// speed
		// Long Log
		Enemy e5 = new Enemy(QuickLoad("dirt"), map[0][3], 64, 64, 30);// 5
		// speed
		Enemy e6 = new Enemy(QuickLoad("dirt"), map[1][3], 64, 64, 30);// 5
		// speed
		Enemy e7 = new Enemy(QuickLoad("dirt"), map[2][3], 64, 64, 30);// 5
		// speed
		Enemy e8 = new Enemy(QuickLoad("dirt"), map[3][3], 64, 64, 30);// 5
		// speed
		Enemy e9 = new Enemy(QuickLoad("dirt"), map[4][3], 64, 64, 30);// 5
		// speed

		// Turtles
		Enemy e4 = new Enemy(QuickLoad("turtle"), map[0][6], 64, 64, 15);// 5
		// speed
		Enemy e00 = new Enemy(QuickLoad("turtle"), map[0][4], 64, 64, 35);// 5
		// speed
		Enemy e01 = new Enemy(QuickLoad("turtle"), map[0][2], 64, 64, 45);// 5
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

		CoinGenerator c = new CoinGenerator(QuickLoad("coin"), map[0][0], 64, 64);
		cgWave wavec = new cgWave(5000, c);
		//frogmusic = new Audio();
		while (!Display.isCloseRequested()) {// While were not hitting the x
			// button
			Clock.update();
			// Draw the grid
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
	


} 
	}// EoConstruct

	

	
	
	
	
	
	
	public static class Action1 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("start button was pusshed :"+ e);
			panel.run();
		}
	}// EoC

	// Action that is performed for button#2
	public static class Action2 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			FroggerView.Boot();
		}
	}// EoC

	// Action that is performed for button#3
	public static class Action3 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFrame frame4 = new JFrame("Frogger Instructions");
			frame4.setVisible(true);
			frame4.setSize(700, 100);

			JLabel label = new JLabel(
					"Use the arrow keys to move your frog across "
							+ "the cars and river, if multiplayer use the keys W,A,S,D");
			JPanel panel = new JPanel();
			frame4.add(panel);
			panel.add(label);
		}
	}


	
	public void FroggerMenu(){

	}

}
