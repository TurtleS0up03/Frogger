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
	
	

	public static void main(String[] args) {
		
		FroggerView view = new FroggerView();
		view.Boot(); 
		FroggerModel model = new FroggerModel(view);
		FroggerController controller = new FroggerController(model.panel);
		
	}

}
