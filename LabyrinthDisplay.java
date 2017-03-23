/*
 * LabyrinthDisplay.java
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
/*
import javax.swing.JFrame;
import java.awt.Toolkit;//used for getScreenSpecs0
import java.awt.Dimension;//used for getScreenSpecs0
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.GraphicsEnvironment;//used for getScreenSpecs
import java.awt.GraphicsDevice;//used for getScreenSpecs1
import java.util.Timer;
import java.awt.event.*;
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
//import java.awt.KeyEventDispatcher;
//import java.awt.KeyboardFocusManager;

public class LabyrinthDisplay extends JFrame implements ActionListener, KeyListener{
	
	private static int SCREEN_HEIGHT, SCREEN_WIDTH, SCREEN_REFRESH, SCREEN_BIT_RATE;
	private BufferedImage buffer;
	private Image background;
	private boolean isFullScreen = false;
	
	private Timer myTimer;
	private int TPS_TIMER_MS;
	private int time;
	private int FPS=0;
	
	public LabyrinthDisplay(){
		//super("LABYRINTH");
		getScreenSpecs();
		setTimer();
		//if(isFullScreen && isFullScreenSupported()) setFullScreenWindow(Window w); //for exclusive full mode - https://docs.oracle.com/javase/tutorial/extra/fullscreen/exclusivemode.html		
		this.setLayout(null);
		//this.setIconImage(Image image); //details
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH); does not require getScreenSpecs
		this.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setUndecorated(true);//remove upper bar
		this.setResizable(false);
		
		buffer = new BufferedImage(SCREEN_WIDTH,SCREEN_HEIGHT,BufferedImage.TYPE_INT_RGB);
		
		Toolkit t = Toolkit.getDefaultToolkit();
		background = t.getImage("Lab_Background_Pic.jpg");
		
		myTimer = new Timer(TPS_TIMER_MS,this);
		time = 0;
		
		this.setVisible(true);
		this.addKeyListener(this);
		
		myTimer.start();
		
	}
	
	private void setTimer(){//same Framerate as your monitor
		TPS_TIMER_MS =(int)((1/(double)SCREEN_REFRESH)*1000);//T=1/f * 1000 (ms)
	}
	
	
	
	/*
	private static void getScreenSpecs0(){ // gets screen resolution
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		SCREEN_WIDTH = (int)screenSize.getWidth();
		SCREEN_HEIGHT = (int)screenSize.getHeight();
		//System.out.println(width+"X"+height);
	}
	*/
	
	private static void getScreenSpecs(){ //gets all screen specs
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] devices = env.getScreenDevices();
		int sequence = 1;
		for (GraphicsDevice device : devices) {
            
            SCREEN_WIDTH = device.getDisplayMode().getWidth();
			SCREEN_HEIGHT = device.getDisplayMode().getHeight();
			SCREEN_REFRESH = device.getDisplayMode().getRefreshRate();
			SCREEN_BIT_RATE = device.getDisplayMode().getBitDepth();
			
			/*
			System.out.println("");
			System.out.println("Screen Number [" + (sequence++) + "]");
            System.out.println("Width       : " + SCREEN_WIDTH);
            System.out.println("Height      : " + SCREEN_HEIGHT);
            System.out.println("Refresh Rate: " + SCREEN_REFRESH);
            System.out.println("Bit Depth   : " + SCREEN_BIT_RATE);
            System.out.println("");
            */
        }
		
	}
	
	public void paint(Graphics g){
		Graphics buff = buffer.getGraphics();
		buff.drawImage(background,0,0,this);
		//FPS
		buff.setColor(Color.GREEN);
		buff.setFont(new Font("Arial", Font.BOLD, 40));
		buff.drawString(Integer.toString(FPS),100,100);
		
		g.drawImage(buffer,0,0,this);
	}




	public void actionPerformed(ActionEvent e){
		time += TPS_TIMER_MS;
        //simulateGame();
        FPS++;
        repaint();
	}
	
	
	
	public void keyReleased(KeyEvent e){
		
	}
	public void keyPressed(KeyEvent e){
		if(e.getKeyCode()== KeyEvent.VK_ESCAPE){
			System.out.println("ESC Pressed!");
			System.exit(0);
		}
		else if(e.getKeyCode()== KeyEvent.VK_UP){
			System.out.println("UP Pressed!");
		}
		else if(e.getKeyCode()== KeyEvent.VK_DOWN){
			System.out.println("DOWN Pressed!");
		}
		else if(e.getKeyCode()== KeyEvent.VK_LEFT){
			System.out.println("LEFT Pressed!");
		}
		else if(e.getKeyCode()== KeyEvent.VK_RIGHT){
			System.out.println("RIGHT Pressed!");
		}
		else if(e.getKeyCode()== KeyEvent.VK_ENTER){
			System.out.println("ENTER Pressed!");
		}
	}
	public void keyTyped(KeyEvent e){
		
	}
	
	public static void main (String args[]) {
		new LabyrinthDisplay();
	}
}

