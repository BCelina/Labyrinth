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
import java.awt.Robot;

public class LabyrinthDisplay extends JFrame implements ActionListener, KeyListener{
	
	private static int SCREEN_HEIGHT, SCREEN_WIDTH, SCREEN_REFRESH, SCREEN_BIT_RATE;
	private BufferedImage buffer;
	private Image background;
	private Image wall_Image;
	private boolean isFullScreen = false;
	private final boolean MASKING = false; //set to false for testing since it takes 5s to launch the game
	private DistanceFilter Mask;
	private int radius;
	
	private Timer myTimer;
	private int TPS_TIMER_MS;
	private int time;
	//private long millis = System.currentTimeMillis();
	private int FPS=0;
	private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
	
	
	
	private boolean [][] MAP;
	private double square_width,square_height;
	private Player player;
	private int DIFFICULTY =2;
	private int step;
	private boolean [] keysPressed={false,false,false,false};
	
	public LabyrinthDisplay(){
		//super("LABYRINTH");
		getScreenSpecs();
		setTimer();
		//generateMap();
		Maze map = new Maze (DIFFICULTY);
		MAP = map.maze;
		square_width=(((double)SCREEN_WIDTH)/(16.*((double)DIFFICULTY)));
		square_height=(((double)SCREEN_HEIGHT)/(9.*((double)DIFFICULTY)));
		map.draw();
		this.setLayout(null);
		//this.setIconImage(Image image); //details
		this.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setUndecorated(true);//remove upper bar
		this.setResizable(false);
		//this.setIgnoreRepaint( true );
		
		buffer = new BufferedImage(SCREEN_WIDTH,SCREEN_HEIGHT,BufferedImage.TYPE_INT_RGB);
		
		Toolkit t = Toolkit.getDefaultToolkit();
		background = t.getImage("Lab_Background_Pic.jpg");//check resizing image to resolution
		wall_Image = t.getImage("Wall1.png");
		
		myTimer = new Timer(TPS_TIMER_MS,this);
		time = 0;
		int startY = map.getStart();
		player = new Player(0,startY*SCREEN_HEIGHT/(DIFFICULTY*9),(int)square_width,(int)square_height,0,2);
		
		step=player.width/2;
		
		radius = 2;//check automatic calculation
		
		//Mask = new DistanceFilter(500,SCREEN_WIDTH,SCREEN_HEIGHT);
		if(MASKING)Mask = new DistanceFilter((int)(square_width)*3,SCREEN_WIDTH,SCREEN_HEIGHT,DIFFICULTY,step);
		
		//remove cursor from screen
			// Transparent 16 x 16 pixel cursor image.
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

			// Create a new blank cursor.
		Cursor blankCursor = t.createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");

			// Set the blank cursor to the JFrame.
		this.setCursor(blankCursor);

		render();
		
		this.setVisible(true);
		this.addKeyListener(this);
		
		myTimer.start();
		
	}
	
	private void setTimer(){//same Framerate as your monitor
		TPS_TIMER_MS =(int)((1/(double)SCREEN_REFRESH)*1000);//T=1/f * 1000 (ms)
		//TPS_TIMER_MS=16;
	}
	
	
	private static void getScreenSpecs(){ //gets all screen specs
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] devices = env.getScreenDevices();
		int sequence = 1;
		for (GraphicsDevice device : devices) {
            
            SCREEN_WIDTH = device.getDisplayMode().getWidth();
			SCREEN_HEIGHT = device.getDisplayMode().getHeight();
			SCREEN_REFRESH = device.getDisplayMode().getRefreshRate();
			SCREEN_BIT_RATE = device.getDisplayMode().getBitDepth();
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
		g.drawImage(buffer,0,0,this);
	}
	public void render(){
		
		Graphics buff = buffer.getGraphics();
		//buff.drawImage(background,0,0,this);//has a fluidity effect without a wallpaper due to partial transparency of the walls
		
		
		for(int i=0;i<(int)(16*DIFFICULTY);i++){ //paint rectangles in 16:9 aspect ratio
			for(int k=0;k<(int)(9*DIFFICULTY);k++){
				if(MAP[k][i]==false){
					//buff.setColor(new Color(0,0,0,127)); //50% transperent Black
					buff.drawImage(wall_Image,(int)((double)i*square_width),(int)((double)k*square_height),(int)(square_width),(int)(square_height),this);
					
				}else{
					buff.setColor(new Color(255,255,255,255)); //100% transparent white
					buff.fillRect((int)((double)i*square_width),(int)((double)k*square_height),(int)(square_width),(int)(square_height));
				}
				
				
			}
		}
		
		//paint player
		//red dot
		/*
		buff.setColor(Color.RED);
		buff.fillOval(player.x,player.y,20,20);
		*/
		//paint dark filer, still being tested...problem is that it is too complex, creates a start delay
		
		//image character
		buff.drawImage(player.image,player.x,player.y,player.width,player.height,this);
		
		//rotating image character, it is not workings
		//player.draw(buff,this);

		
		
		if(MASKING)buff.drawImage(Mask.getImage(),player.x-SCREEN_WIDTH+(player.width/2),player.y-SCREEN_HEIGHT+(player.height/2),this);//10 object dimensions
		
		
		//FPS
		buff.setColor(Color.GREEN);
		buff.setFont(new Font("Arial", Font.BOLD, 40));
		buff.drawString(Integer.toString(FPS),100,100);
		
	}



	public void actionPerformed(ActionEvent e){
		time += TPS_TIMER_MS;
        //simulateGame();
        FPS++;
        move();
        render();
        repaint();
	}
	
	
	
	public void keyReleased(KeyEvent e){
		
	}
	public void keyPressed(KeyEvent e){
			int key = e.getKeyCode();
		if(key== KeyEvent.VK_ESCAPE ){
			System.out.println("ESC Pressed!");
			System.exit(0);
		}
		else if(key== KeyEvent.VK_UP || key==KeyEvent.VK_W){
			keysPressed[0]=true;
			player.dir=8;
		}
		else if(key== KeyEvent.VK_DOWN | key==KeyEvent.VK_S){
			keysPressed[1]=true;
			player.dir=2;
		}
		else if(key== KeyEvent.VK_LEFT || key==KeyEvent.VK_A){
			keysPressed[2]=true;
			player.dir=4;
		}
		else if(key== KeyEvent.VK_RIGHT || key==KeyEvent.VK_D){//switch 20 by player x dimension (to be implemented)
			keysPressed[3]=true;
			player.dir=6;
		}
		else if(key== KeyEvent.VK_ENTER){
			System.out.println("ENTER Pressed!");
		}
	}
	public void keyTyped(KeyEvent e){
		
	}
	private void move(){
		if(keysPressed[0]){
			System.out.println("UP Pressed!");
			if(player.y>0&& pixelIsWhite(player.x+1,player.y-step+1) && pixelIsWhite(player.x+player.width-1,player.y-step+1)){
				 player.y-=step;//player size-1
				 System.out.println("Gone up!");
			 }
		}
		if(keysPressed[1]){
			System.out.println("DOWN Pressed!");
			if(player.y<(SCREEN_HEIGHT-player.height)&& pixelIsWhite(player.x+player.width-1,player.y+step+player.height-1) && pixelIsWhite(player.x,player.y+step+player.height-1)) player.y+=step;//switch 20 by player y dimension (to be implemented)
		}
		if(keysPressed[2]){
			System.out.println("LEFT Pressed!");
			if(player.x>0 && pixelIsWhite(player.x-step+1,player.y+1) && pixelIsWhite(player.x-step+1,player.y+player.height-1)) player.x-=step;
		}
		if(keysPressed[3]){//switch 20 by player x dimension (to be implemented)
			System.out.println("RIGHT Pressed!");
			if(player.x<(SCREEN_WIDTH-player.width)&& pixelIsWhite(player.x+step+player.width-10, player.y+1) && pixelIsWhite(player.x+step+player.width-10, player.y+player.height-1)) player.x+=step;
		}
		keysPressed = new boolean[]{false,false,false,false};
		
	}
	public boolean checkCollision(int x, int y){
		boolean ret = false;
		if(!pixelIsWhite(x,y))return true;
		return ret;
	}
	public boolean pixelIsWhite(int x, int y){
		try{
			Robot robot = new Robot();
			Color color = robot.getPixelColor(x,y);
			if(color.equals(new Color(255,255,255))) return true;
		}catch (AWTException e) {
            e.printStackTrace();
        }
		
		return false;
		
	}
	public static void main (String args[]) {
		new LabyrinthDisplay();
	}
}

