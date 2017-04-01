//Import of only the libraries that are required for this game:

//import of specific awt libraries
import java.awt.Image; //used in reder and paint
import java.awt.Color; //used in render and paint
import java.awt.Font; //used for the game over message
import java.awt.Toolkit; //used for importing images
import java.awt.Cursor; //to remove the cursor from the screen
import java.awt.Point; //to remove the cursor from the screen
import java.awt.GraphicsEnvironment; //to get screen specifications
import java.awt.GraphicsDevice; //to get screen specifications
import java.awt.image.BufferedImage; //used by render and paint
import java.awt.Graphics;

import java.awt.event.ActionEvent; //used by ActionListener
import java.awt.event.ActionListener; //used to implement ActionListener

import java.awt.event.KeyEvent; //used by keyListener
import java.awt.event.KeyListener; //used to implement KeyListener

//impot of specific swing libraries
import javax.swing.JFrame;
import javax.swing.Timer;


public class LabyrinthDisplay extends JFrame implements ActionListener, KeyListener{
	
	//Main screen parameters
	private static int SCREEN_HEIGHT, SCREEN_WIDTH, SCREEN_REFRESH, SCREEN_BIT_RATE;
	private BufferedImage buffer;
	private Image background;
	private Image wall_Image;
	
	//Vignette parameters (distanceFilter)
	private boolean giveUp = false;
	private boolean MASKING = true; //set to false for testing since it takes 5s to launch the game
	private DistanceFilter Mask;
	private int radius;
	
	//Timer parameters
	private Timer myTimer;
	private int TPS_TIMER_MS;
	private int time;
	private int FPS=0;
	
	//Game parameters such as Maze and player
	private Maze map;
	private boolean [][] MAP;
	private boolean [][] solution;
	private double square_width,square_height;
	private Player player;
	
	//Esthetical details
	private int DIFFICULTY =2;
	private int Character=2;
	private int step;
	
	//Game state parameters
	private boolean [] keysPressed={false,false,false,false};
	private boolean gameWon = false;
	
	//Default Constructor for a standard game
	public LabyrinthDisplay(){
		this(3,2,true);
	}
	//Constructor with Difficulty and Character choosing
	public LabyrinthDisplay(int Diff, int Char){
		this(Diff, Char, true);
	}
	//Full Constructor
	public LabyrinthDisplay(int Diff, int Char, boolean Vignette){
		//set Title of JFrame
		super("LABYRINTH");
		//set Game Parameters
		DIFFICULTY=Diff;
		Character = Char;
		MASKING = Vignette;
		
		getScreenSpecs(); //gets screen parameters
		//SCREEN_HEIGHT=720; //set manually height resolution
		//SCREEN_WIDTH=1024; //set manually width resolution
		setTimer(); //sets refresh rate depending on screen refresh rate
		
		//Maze creation
		map = new Maze (DIFFICULTY);
		MAP = map.maze;
		square_width=(((double)SCREEN_WIDTH)/(16.*((double)DIFFICULTY))); //calculates width of a square in pixels
		square_height=(((double)SCREEN_HEIGHT)/(9.*((double)DIFFICULTY))); //calculates height of a square in pixels
		
		Toolkit t = Toolkit.getDefaultToolkit(); //initiates toolkit for importing images
		
		//JFrame properties
		this.setLayout(null);
		this.setIconImage(t.getImage("Labyrinth.png")); //the icon of the game
		this.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);//Size of window is equal to Screen resolution parameters
		this.setLocationRelativeTo(null);//centered screen
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //set operation on exit
		this.setUndecorated(true);//remove upper bar
		this.setResizable(false); //disables resizing
		
		buffer = new BufferedImage(SCREEN_WIDTH,SCREEN_HEIGHT,BufferedImage.TYPE_INT_RGB); //initiates screen to be displayed

		
		//background = t.getImage("Lab_Background_Pic.jpg");//gets image to be drawn in wallpaper, not used anymore
		wall_Image = t.getImage("Wall1.png"); //gets image for walls
		
		myTimer = new Timer(TPS_TIMER_MS,this);//set timer
		time = 0; //set time
		int startY = map.getStart(); //get Y position of player for begining of the game
		player = new Player(0,startY*SCREEN_HEIGHT/(DIFFICULTY*9),(int)square_width,(int)square_height,6,Character);//initiates Player, 6 means Player starts looking right
		
		step=player.width/2; //Player can move twice to reach a square
		
		radius = (int)(square_width)*3; //only three surounding frames are visible
		
		//if(MASKING)Mask = new DistanceFilter(radius,SCREEN_WIDTH,SCREEN_HEIGHT,DIFFICULTY,step);//required for move(), linear gradient with initial offset
		if(MASKING)Mask = new DistanceFilter(radius,SCREEN_WIDTH,SCREEN_HEIGHT,DIFFICULTY,0); //if using move2(), this is more elegant to look at
		
		//remove cursor from screen
			// Transparent 16 x 16 pixel cursor image.
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

			// Create a new blank cursor.
		Cursor blankCursor = t.createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");

			// Set the blank cursor to the JFrame.
		this.setCursor(blankCursor);
		//render initial frame
		render();
		
		this.setVisible(true);//set visible
		this.addKeyListener(this);//start listening to typing
		
		myTimer.start();//start game
		
	}
	
	private void setTimer(){//same Framerate as your monitor
		TPS_TIMER_MS =(int)((1/(double)SCREEN_REFRESH)*1000/2);//T=1/f * 1000/2 (ms) , division by two to prevent screen flicker due to sync problems
	}
	
	
	private static void getScreenSpecs(){ //gets all screen specs
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();//get graphics environment to analyse
		GraphicsDevice[] devices = env.getScreenDevices();
		int sequence = 1;
		for (GraphicsDevice device : devices) {
            
            SCREEN_WIDTH = device.getDisplayMode().getWidth();
			SCREEN_HEIGHT = device.getDisplayMode().getHeight();
			SCREEN_REFRESH = device.getDisplayMode().getRefreshRate();
			SCREEN_BIT_RATE = device.getDisplayMode().getBitDepth();
        }
		
	}
	public void actionPerformed(ActionEvent e){//the game steps for each frame
		time += TPS_TIMER_MS;
        //simulateGame();
        FPS++;//Frames displayed
        move2();//move the player if the user has pressed a button, relative to the game
        render();//render the display
        repaint();//paint the rendered display
	}
	
	public void paint(Graphics g){ //paints rendered image
		g.drawImage(buffer,0,0,this);
	}
	public void render(){ //render graphics to be painted on the screen for the current state, render is done is sequential layers
		
		Graphics buff = buffer.getGraphics();//start graphics render window
		
		//buff.drawImage(background,0,0,SCREEN_WIDTH,SCREEN_HEIGHT,this);//renders an initial wallpaper
		
		//render maze
		for(int i=0;i<(int)(16*DIFFICULTY);i++){ //paint rectangles in 16:9 aspect ratio
			for(int k=0;k<(int)(9*DIFFICULTY);k++){
				if(giveUp && solution[k][i]==true){
					buff.setColor(new Color(255,255,0,255)); //yellow
					buff.fillRect((int)((double)i*square_width),(int)((double)k*square_height),(int)(square_width),(int)(square_height));
				}
				else if(MAP[k][i]==false){
					//buff.setColor(new Color(0,0,0,127)); //50% transperent Black
					buff.drawImage(wall_Image,(int)((double)i*square_width),(int)((double)k*square_height),(int)(square_width),(int)(square_height),this);
					
				}else{
					buff.setColor(new Color(255,255,255,255)); //0% transparent white
					buff.fillRect((int)((double)i*square_width),(int)((double)k*square_height),(int)(square_width),(int)(square_height));
				}
				
				
			}
		}

		//image character rendering
		buff.drawImage(player.getImage(),player.x,player.y,player.width,player.height,this);

		
		//draw vignette using distanceFilter only if MASKING is enabled and player is still playing
		if(MASKING && !giveUp)buff.drawImage(Mask.getImage(),player.x-SCREEN_WIDTH+(player.width/2),player.y-SCREEN_HEIGHT+(player.height/2),this);
		
		/*
		//FRAMES, to check how many frames are being displayed, for testing purposes
		buff.setColor(Color.GREEN);
		buff.setFont(new Font("Arial", Font.BOLD, 40));
		buff.drawString(Integer.toString(FPS),100,100);
		*/
		//Draw Game over message
		if(gameWon){
			buff.setColor(Color.RED);
			buff.setFont(new Font("Arial", Font.BOLD, SCREEN_HEIGHT/5));
			buff.drawString("You Won!",SCREEN_WIDTH/4,SCREEN_HEIGHT/3);
		}
		
	}

	public void keyReleased(KeyEvent e){//not used
		
	}
	public void keyPressed(KeyEvent e){//ESC, UP, DOWN, RIGHT, LEFT, ENTER USED
		/*
		 * ESC exits the game
		 * 
		 * Moving buttons (W/A/S/D) records the intention to move in an array, which gets reset
		 * after moving, and sets player direction for image display:
		 * 
		 * UP or W moves player up
		 * DOWN or S moves player down
		 * LEFT or A moves player left
		 * RIGHT or D moves player right
		 * 
		 * ENTER when the user quits the game and displays solution (it does not exit the game)
		 * 
		 * The values are stored in arrays, and excecuted on the next frame (to prevent frame lag and drop)
		 * except for ENTER and ESC, where frame fluidity is not of great importance and can be pressed once
		 */
			int key = e.getKeyCode();
		if(key== KeyEvent.VK_ESCAPE ){
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
			giveUp=true;//if enter was pressed, player gave up
			map.solveMaze((int)((double)player.y/square_height),(int)((double)player.x/square_width));//solve Maze for the players current coordinates
			solution = map.solution;//get solution
		}
	}
	public void keyTyped(KeyEvent e){//not used
		
	}
	
	private void move2(){//method used to move if move is possible
		if(keysPressed[0]){//move up
			//System.out.println("UP Pressed!");
			if(player.y>0 && canMove(player.x+2,player.y-step+2) && canMove(player.x+player.width-2,player.y-step+2)){
				 player.y-=step;//player size-1
			 }
		}
		if(keysPressed[1]){//move down
			//System.out.println("DOWN Pressed!");
			if(player.y<(SCREEN_HEIGHT-player.height)&& canMove(player.x+player.width-2,player.y+step+player.height-2) && canMove(player.x+2,player.y+step+player.height-2)) player.y+=step;//switch 20 by player y dimension (to be implemented)
		}
		if(keysPressed[2]){//move left
			//System.out.println("LEFT Pressed!");
			if(player.x>0 && canMove(player.x-step+2,player.y+2) && canMove(player.x-step+2,player.y+player.height-2)) player.x-=step;
		}
		if(keysPressed[3]){//move right and check if player won since the exit is always to the right
			//System.out.println("RIGHT Pressed!");
			if(player.x<(SCREEN_WIDTH-player.width)&& canMove(player.x+step+player.width-2, player.y+2) && canMove(player.x+step+player.width-2, player.y+player.height-2)) player.x+=step;
			if(player.x*1.01>SCREEN_WIDTH-square_width) gameWon=true;
		}
		keysPressed = new boolean[]{false,false,false,false};//reset buttons to not-pressed state
	}

	public boolean canMove(int x, int y){//used by move2(), very efficient
		x=(int)((double)x/square_width);
		y=(int)((double)y/square_height);
		return MAP[y][x];
	}
	public static void main (String args[]) {
		new LabyrinthDisplay();
	}

}

