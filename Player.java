/*
 * Player.java
 * 
 * Copyright 2017 bedre <bedre@BCAAV15BE>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 * 
 * 
 */

import java.awt.image.*;//Image
import java.awt.*;//Toolkit, to import image
import java.awt.geom.AffineTransform;//Rotation
import javax.swing.*;//draw

public class Player { //implement player dimensions
	
	public int x,y,dir,width,height;
	protected Image image_right,image_left,image_up,image_down,image_current;
	private BufferedImage temp_image;
	
	public Player(int X, int Y,int w, int h, int dirr, int character){
		x=X;
		y=Y;
		height=h;
		width=w;
		if(dirr!=4 || dirr!=6 || dirr!=8 || dirr!=2) dirr=6;
		dir=dirr; //direction numpad style 6 right, 4 left, 2 down, 8 up
		setCharacter(character);
		setCurrentImage();
		
	}
	public Player(int X, int Y,int w, int h, int character){
		this(X,Y,w,h,6,character);
	}
	private void setCharacter(int k){
		if(k==1)setimage("Character1");
		else if(k==2)setimage("Character2");
		else if(k==3)setimage("Character3");
		else setimage("Character1"); //a default image if somebody wants a Character that doesnt exist
		//image = image.getScaledInstance(width,height,Image.SCALE_SMOOTH);
	}
	private void setimage(String s){
		Toolkit t = Toolkit.getDefaultToolkit();
		//get images for all directions
		image_right = t.getImage(s+"_right"+".png");
		image_left = t.getImage(s+"_left"+".png");
		image_up = t.getImage(s+"_up"+".png");
		image_down = t.getImage(s+"_down"+".png");
		
		setCurrentImage();
	}
	private void setCurrentImage(){
		if(dir==6) image_current = image_right;
		else if(dir==4) image_current = image_left;
		else if(dir==2) image_current = image_down;
		else if(dir==8) image_current = image_up;
	}
	public Image getImage(){
		setCurrentImage();
		return image_current;
	}
}

