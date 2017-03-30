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
	protected Image image;
	private BufferedImage temp_image;
	
	public Player(int X, int Y,int w, int h, int dirr, int character){
		x=X;
		y=Y;
		height=h;
		width=w;
		dir=dirr;
		setImage(character);
		
	}
	private void setImage(int k){
		Toolkit t = Toolkit.getDefaultToolkit();
		if(k==1)image = t.getImage("Character1.png");
		else if(k==2)image = t.getImage("Character2.png");
		else if(k==3)image = t.getImage("Character3.png");
		else t.getImage("Character1.png"); //a default image if somebody wants a Character that doesnt exist
		//image = image.getScaledInstance(width,height,Image.SCALE_SMOOTH);
	}/*
	public void getRotatedImage(){
		double angle=0;
		if(dir==6) ;//do nothing
		else if(dir==8) angle=Math.toRadians(90);
		else if (dir==2) angle=Math.toRadians(-90);
		else if (dir==4) angle=Math.toRadians(180);
		double sin =Math.sin(angle);
		double cos = Math.cos(angle);
		
		BufferedImage bimg = toBufferedImage(getEmptyImage(width, height));
		Graphics2D g = bimg.createGraphics();

		g.translate((0)/2, (0)/2);
		g.rotate(Math.toRadians(angle), width/2, height/2);
		g.drawRenderedImage(toBufferedImage(img), null);
		g.dispose();

		return toImage(bimg);
	}*/
	private void getRotatedImage(){//8 is up, 6 is right, 4 is left, 2 is down (numpad style)
		temp_image = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		if(dir==6) temp_image= toBufferedImage(image);//do nothing
		else if(dir==8) temp_image=rotate(Math.toRadians(90));
		else if (dir==2) temp_image=rotate(Math.toRadians(-90));
		else if (dir==4) temp_image=rotate(Math.toRadians(-90));
		//return temp;
	}
	private BufferedImage rotate(double rotationRequired){
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, width/2, height/2);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		return op.filter(toBufferedImage(image),null);
	}
	private BufferedImage toBufferedImage(Image img){
		if (img instanceof BufferedImage){
			return (BufferedImage) img;
		}

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	
	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, width, height, null);
	    bGr.dispose();
	
	    // Return the buffered image
	    return bimage;
	}
	public void draw (Graphics g, JFrame jf){
		g.drawImage(temp_image,x,y,jf);
		
	}
	
}

