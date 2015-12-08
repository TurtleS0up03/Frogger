package frogger;

import static helpers.Artist.DrawQuadTex;

public class FroggerModel {
	public FrogPanel panel;
	public FroggerController cntrl;
	public FroggerView view;

	public FroggerModel(FroggerView view) {
		this.view = view;
		panel = new FrogPanel(view);
		
	}
	
	

}
