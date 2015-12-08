package frogger;

import javax.swing.*;
import java.awt.*;
import org.lwjgl.opengl.Display;

public class Frogger 
/*
 * 
 */
{
	

	public Frogger() {
		// TODO Auto-generated constructor stub
	}
	public static FroggerView view = new FroggerView();
	public FroggerModel model = new FroggerModel(view);
	public FroggerController controller = new FroggerController(model.panel);
	

	public static void main(String[] args) {
		
		
		view.Boot(); 
		
		
	}

}
