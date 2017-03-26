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
import java.util.Random;
//import java.awt.KeyEventDispatcher;
//import java.awt.KeyboardFocusManager;

public class LabyrinthDisplay extends JFrame implements ActionListener, KeyListener{
	
	private static int SCREEN_HEIGHT, SCREEN_WIDTH, SCREEN_REFRESH, SCREEN_BIT_RATE;
	private BufferedImage buffer;
	private Image background;
	private boolean isFullScreen = false;
	private final boolean MASKING = true; //set to false for testing since it takes 5s to launch the game
	private DistanceFilter Mask;
	private int radius;
	
	private Timer myTimer;
	private int TPS_TIMER_MS;
	private int time;
	private int FPS=0;
	
	private boolean [][] MAP;
	private Player player;
	
	public LabyrinthDisplay(){
		//super("LABYRINTH");
		getScreenSpecs();
		setTimer();
		generateMap();
		//if(isFullScreen && isFullScreenSupported()) setFullScreenWindow(Window w); //for exclusive full mode - https://docs.oracle.com/javase/tutorial/extra/fullscreen/exclusivemode.html		
		this.setLayout(null);
		//this.setIconImage(Image image); //details
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH); does not require getScreenSpecs
		//SCREEN_WIDTH=800; //testing size
		//SCREEN_HEIGHT=600;
		this.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setUndecorated(true);//remove upper bar
		this.setResizable(false);
		//this.setIgnoreRepaint( true );
		
		buffer = new BufferedImage(SCREEN_WIDTH,SCREEN_HEIGHT,BufferedImage.TYPE_INT_RGB);
		
		Toolkit t = Toolkit.getDefaultToolkit();
		background = t.getImage("Lab_Background_Pic.jpg");//check resizing image to resolution
		
		myTimer = new Timer(TPS_TIMER_MS,this);
		time = 0;
		
		player = new Player(0,0,0);
		
		radius = 5;
		
		//Mask = new DistanceFilter(500,SCREEN_WIDTH,SCREEN_HEIGHT);
		if(MASKING)Mask = new DistanceFilter(100*radius,SCREEN_WIDTH,SCREEN_HEIGHT);
		
		//remove cursor from screen
			// Transparent 16 x 16 pixel cursor image.
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

			// Create a new blank cursor.
		Cursor blankCursor = t.createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");

			// Set the blank cursor to the JFrame.
		this.setCursor(blankCursor);
		
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
	public boolean randomBool(){
		return (Math.random()<0.5);
	}
	
	public void generateMap(){
		//Random random = new Random();
		MAP=new boolean[16][9];
		for(int i=0;i<16;i++){
			for(int k=0;k<9;k++){
				//MAP[i][k]= random.nextBoolean();
				MAP[i][k]= randomBool();
			}
		}
	}
	
	public void paint(Graphics g){ 
		Graphics buff = buffer.getGraphics();
		buff.drawImage(background,0,0,this);//has a fluidity effect without a wallpaper due to partial transparency of the walls
		
		for(int i=0;i<16;i++){ //paint rectangles in 16:9 aspect ratio
			for(int k=0;k<+9;k++){
				if(MAP[i][k]==false){
					//buff.setColor(Color.WHITE);
					buff.setColor(new Color(255,255,255,127)); //50% transparent white
					
				}else{
					//buff.setColor(Color.BLACK);
					buff.setColor(new Color(0,0,0,127)); //50% transperent Black
				}
				double square_width=(((double)SCREEN_WIDTH)/16.);
				double square_height=(((double)SCREEN_HEIGHT)/9.);
				//buff.drawRect((int)((double)i*square_width),(int)((double)k*square_height),(int)(square_width),(int)(square_height));//reduce leftover from redraw, in the case of no background being repainted. Else it makes too bright/dark lines
				buff.fillRect((int)((double)i*square_width),(int)((double)k*square_height),(int)(square_width),(int)(square_height));
			}
		}
		
		//paint player
		buff.setColor(Color.RED);
		buff.fillOval(player.x,player.y,20,20);
		
		//paint dark filer, still being tested...problem is that it is too complex, creates a start delay
		
		
		
		if(MASKING)buff.drawImage(Mask.getImage(),player.x-SCREEN_WIDTH,player.y-SCREEN_HEIGHT,this);
		
		
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
			if(player.y>0) player.y--;
		}
		else if(e.getKeyCode()== KeyEvent.VK_DOWN){
			System.out.println("DOWN Pressed!");
			if(player.y<(SCREEN_HEIGHT-20)) player.y++;//switch 20 by player y dimension (to be implemented)
		}
		else if(e.getKeyCode()== KeyEvent.VK_LEFT){
			System.out.println("LEFT Pressed!");
			if(player.x>0) player.x--;
		}
		else if(e.getKeyCode()== KeyEvent.VK_RIGHT){//switch 20 by player x dimension (to be implemented)
			System.out.println("RIGHT Pressed!");
			if(player.x<(SCREEN_WIDTH-20)) player.x++;
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

