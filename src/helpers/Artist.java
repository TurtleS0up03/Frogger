package helpers;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.io.IOException;
import java.io.InputStream;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Artist {
	
	public static final int WIDTH = 1216, HEIGHT = 960; 
	float width = 50, height = 50, x = 100, y = 100;
		
	public static void BeginSession(){
			
			try{
				Display.setTitle("League of Frogger");//Sets the title of the window

				//Displays the window components 600 by 400 
				Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
				Display.create();
				//If the window cannot open don't blow up the computer instead do...
			}catch (Exception e){
				e.printStackTrace();
				System.out.println(e);
			}
				glMatrixMode(GL_PROJECTION);
				glLoadIdentity();
				glOrtho(0, WIDTH, HEIGHT, 0, 1 ,-1);//Setting up the camera,what the screen will see (hover to see parameters meaning)
				glMatrixMode(GL_MODELVIEW);
				glEnable(GL_TEXTURE_2D);
				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		}//EoM

		/******************************************************
		 Method used for drawing quad's textures to the window
		 same as method above but a little different. Instead of global x,y cordinates(the entire windows cord) 
		 we will be using local cordinates, meaning the x,y cord in 
		 Translate(x, y, 0) is the new top left of our texture
		******************************************************/
		public static void DrawQuadTex(Texture tex, float x, float y, float width, float height){
			tex.bind();					//Binding the first argument tex, (the texture) to opengl
			glTranslatef(x, y, 0);		//Takes x,y,z but since game is two dimensional the last param is zero
			
			//Square texture
			glBegin(GL_QUADS);
			glTexCoord2f(0, 0);			//Top left corner
			glVertex2f(0, 0);			//Vertex of the top left corner of tile
			glTexCoord2f(1, 0);			//Top right corner
			glVertex2f(width, 0);		//Width for x cord to y cord
			glTexCoord2f(1, 1);			//Bottom right corner
			glVertex2f(width, height);	//Width then height of square 
			glTexCoord2f(0, 1);			//Bottom left corner
			glVertex2f(0, height);		//Height of the bottom left cord to the top left cord
			glEnd();
			glLoadIdentity();			//Prevents tiles from going off the window in the while loop(must go after glEnd)	
		}//EoM

		/*******************************************************
		 Method to load the texture we have saved in the resource folder 
		 onto the window, returns a texture when called, textures are 
		 64x64 pixels (either the grass or the dirt texture)
		*******************************************************/
		public static Texture LoadTexture(String path, String fileType){
			Texture tex = null;
			InputStream in = ResourceLoader.getResourceAsStream(path);
			try{
				tex = TextureLoader.getTexture(fileType, in);//file type is .png the path is where they are starting in res folder
			} catch(IOException e){
				e.printStackTrace();
			}
			return tex;
		}
		
		/*******************************************************
		  Instead of typing the dirt and grass filename and the type
		  at every method call in Boot class this method literally types
		  it out for us 
		 *******************************************************/
		public static Texture QuickLoad(String name){
			Texture tex = null;//initializing the texture variable to null
			tex = LoadTexture("res/" + name + ".png", "PNG");//typing out the texture name and filetype for us
			
			return tex;//returning the tex variable
		}//EoM

}//EoC
