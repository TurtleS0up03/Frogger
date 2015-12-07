package frogger;

//imporwdawn.slick.opengl.Texture;
import static helpers.Artist.BeginSession;
import static helpers.Artist.QuickLoad;

import frogger.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.lwjgl.opengl.Display;

import helpers.Clock;

public class Boot {

	public static Audio frogmusic;

	public Boot() {

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
		frogmusic = new Audio();
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
		frogmusic.stop();
		Display.destroy();
	}// End of boot constructor

	public static void main(String[] args) {
		JFrame frame = new JFrame("LEAGUE OF FROGGER");
		frame.setVisible(true);
		frame.setSize(800, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		frame.add(panel);

		Font font = new Font("Serif", Font.ITALIC, 100);
		panel.setLayout(new GridLayout(4, 1, 0, 3));
		JLabel label = new JLabel("League of Frogger", JLabel.CENTER);
		label.setBackground(Color.GREEN);
		label.setFont(font);
		label.setOpaque(true);
		panel.add(label);

		// Create the button/listener for #1
		JButton button = new JButton("Start Game");
		panel.add(button);
		button.setPreferredSize(new Dimension(200, 100));
		button.addActionListener(new Action1());

		// Create the button/listener for #2
		JButton button2 = new JButton("Multiplayer");
		panel.add(button2);
		button2.setPreferredSize(new Dimension(200, 100));
		button2.addActionListener(new Action2());// note the button2 here
		// instead of button

		// Create the button/listener for #3
		JButton button3 = new JButton("Instructions");
		panel.add(button3);
		button3.setPreferredSize(new Dimension(200, 100));
		button3.addActionListener(new Action3());// note the button3 here
													// instead of button

	}// End of main method

	// Action that is performed for button#1
	public static class Action1 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			new Boot();
		}
	}// EoC

	// Action that is performed for button#2
	public static class Action2 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			new Boot();
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
}// End of Boot class