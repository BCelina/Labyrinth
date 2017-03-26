/*
 * DistanceFilter.java
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
	
	public DistanceFilter(int D, int sw, int sh){
		
		SCREEN_WIDTH=sw;
		SCREEN_HEIGHT=sh;
		
		graphics = new BufferedImage(2*SCREEN_WIDTH,2*SCREEN_HEIGHT,BufferedImage.TYPE_INT_ARGB);
		ig2 = graphics.createGraphics();
		for(int i=0;i<2*SCREEN_WIDTH;i++){
			for(int k=0;k<2*SCREEN_HEIGHT;k++){
				double d = distance(i,k);					
				if(d>D){ //set difficulty level here (check multiplier experimentally)
					ig2.setColor(Color.BLACK);
					ig2.drawLine(i,k,i,k);
					//System.out.println(i+" and "+k+" painted black!");
				}else if(d>1 &&d<=D){ //set difficulty level here (check multiplier experimentally)
					ig2.setColor(new Color(0,0,0,(int)(255.*d/((double)D))));
					ig2.drawLine(i,k,i,k);
						//System.out.println(i+" and "+k+" painted black!");
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

