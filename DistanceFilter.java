import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.lang.Math;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.awt.Graphics2D;

public class DistanceFilter {
	
	private BufferedImage graphics;
	private Graphics2D ig2;
	private static int SCREEN_HEIGHT, SCREEN_WIDTH;
	private int DIFFICULTY;
	
	public DistanceFilter(int D, int sw, int sh, int dif, int step){
		
		SCREEN_WIDTH=sw;
		SCREEN_HEIGHT=sh;
		DIFFICULTY = dif;
		int full_radius=step*2+4;
		//int gradient_radius=; D
		
		graphics = new BufferedImage(2*SCREEN_WIDTH,2*SCREEN_HEIGHT,BufferedImage.TYPE_INT_ARGB);
		ig2 = graphics.createGraphics();
		for(int i=0;i<2*SCREEN_WIDTH;i++){
			for(int k=0;k<2*SCREEN_HEIGHT;k++){
				double d = distance(i,k);					
				if(d>D){ //set difficulty level here (check multiplier experimentally)
					ig2.setColor(Color.BLACK);
					ig2.drawLine(i,k,i,k);
					//System.out.println(i+" and "+k+" painted black!");
				}else if(d>full_radius && d<=D){ //set difficulty level here (check multiplier experimentally)//change 40 with a dynamic value
					int alpha = (int)(255.*(d-full_radius)/(double)D-(double)full_radius);
					if(alpha<0)alpha=0;
					ig2.setColor(new Color(0,0,0,alpha));
					ig2.drawLine(i,k,i,k);
						//System.out.println(i+" and "+k+" painted black! at "+d+" with alpha "+alpha);
				}
			}
		}
	}
	
	public BufferedImage getImage (){
		return graphics;
	}
	public Graphics2D getGraphics (){
		return ig2;
	}
	
	public double distance (int i, int k){//euclidian distance
		int a = (SCREEN_WIDTH)-i;
		int b = (SCREEN_HEIGHT)-k;
		return (Math.sqrt((a*a)+(b*b)));
		
	}
}

