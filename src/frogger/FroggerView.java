package frogger;

import static helpers.Artist.BeginSession;
import static helpers.Artist.QuickLoad;
import helpers.Clock;
import helpers.ImagesLoader;
import helpers.ImagesPlayer;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.lwjgl.opengl.Display;

import sprites.CarSprite;
import sprites.FrogSprite;

public class FroggerView extends JFrame{
	private FroggerController cntrl;
	private FroggerModel model;
	private FrogPanel panel;
	
	private static final int PWIDTH = 500; // size of panel
	private static final int PHEIGHT = 360;
	private static final String IMS_INFO = "imsInfo.txt";
	private long period; 

	
	public FroggerView() {
		
	}
	
	public static void Game(FrogPanel panel){
		panel.run();
	}
	
	public static void Boot() {
		
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
		button.addActionListener(new FroggerController.Action1());

		// Create the button/listener for #2
		JButton button2 = new JButton("Multiplayer");
		panel.add(button2);
		button2.setPreferredSize(new Dimension(200, 100));
		button2.addActionListener(new FroggerController.Action2());// note the button2 here
		// instead of button

		// Create the button/listener for #3
		JButton button3 = new JButton("Instructions");
		panel.add(button3);
		button3.setPreferredSize(new Dimension(200, 100));
		button3.addActionListener(new FroggerController.Action3());// note the button3 here
													// instead of button

	}
	
	
}
