/*
 * DistanceFilter, Vignette-Filter
 * 
 * Creates a Vignette of size 2*screen_heightX2*screen_width
 * The Vignette is centered in the middle
 * D1 is the Diameter of the circle which is not made darker, the filter can be chosen to be applied from this radius and beyond
 * D2 is the Diameter of the visible vignette (the limit)
 * SCREEN_WIDTH is the screens horizontal resolution
 * SCREEN_HEIGHT is the screens vertical resolution
 * 
 * The gradient is a linear gradient based on the euclidean distance
 */



//Only importing relevant libraries:
//java awt

import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;


public class DistanceFilter {
	
	private BufferedImage graphics;
	private Graphics2D ig2;
	private static int SCREEN_HEIGHT, SCREEN_WIDTH;
	
	
	public DistanceFilter(int D2, int sw, int sh, int D1){
		//Take frame parameters
		SCREEN_WIDTH=sw;
		SCREEN_HEIGHT=sh;

		//initiates graphics
		graphics = new BufferedImage(2*SCREEN_WIDTH,2*SCREEN_HEIGHT,BufferedImage.TYPE_INT_ARGB);
		ig2 = graphics.createGraphics();
		
		//The Vignette
		for(int i=0;i<2*SCREEN_WIDTH;i++){
			for(int k=0;k<2*SCREEN_HEIGHT;k++){
				double d = distance(i,k);	//measures the distance of the current pixel wrt to the middle of the screen				
				//Paints everything BLACK if outside of D2
				if(d>D2){ 
					ig2.setColor(Color.BLACK); 	//sets color to Black
					ig2.drawLine(i,k,i,k); //paints the pixel
				}
				//Paints a dynamic black if in the gradient region
				else if(d>=D1 && d<=D2){  
					int alpha = (int)(255.*(d-D1)/((double)D2-(double)D1));
					if(alpha<0)alpha=0;
					ig2.setColor(new Color(0,0,0,alpha));//Black with a variable transparency (alpha) depending on the distance
					ig2.drawLine(i,k,i,k);
				}
			}
		}
	}
	//Returns a BufferedImage of the Vignette
	public BufferedImage getImage (){
		return graphics;
	}
	//Measures euclidean distance
	public double distance (int i, int k){
		int a = (SCREEN_WIDTH)-i;
		int b = (SCREEN_HEIGHT)-k;
		return (Math.sqrt((a*a)+(b*b)));
		
	}
}

