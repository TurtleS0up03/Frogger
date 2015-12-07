package frogger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;




import org.lwjgl.opengl.Display;

import java.awt.*;
import java.awt.event.*;

public class FroggerController {
	
	public FroggerController() {
		// TODO Auto-generated constructor stub
	}
	
	public static class Action1 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			FroggerView.Game();
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
