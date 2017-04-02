import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JSlider;
import java.awt.Image;
import javax.swing.ImageIcon ;
import java.awt.Color; //used in render and paint
import java.awt.Font;
import java.awt.Toolkit;

public class Labyrinth extends JFrame implements ActionListener {
	public static int[] tableaudeChoix = new int[4];
	private JButton start;
	public static boolean begin=false;
	private JComboBox Personnage;
	private JComboBox Wall;
	private JSlider difficulty;
	private JSlider visibility;	
	private Image background1;
	private Toolkit t = Toolkit.getDefaultToolkit(); //initializes toolkit for importing images
	
	public Labyrinth(){
		this.setLayout(null);
		this.setTitle("LABYRINTH");
		this.setIconImage(t.getImage("Labyrinth.png")); //the icon of the game
		this.setSize(800,600);
		this.setResizable(false);
		this.setLocationRelativeTo(null);//centered screen
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel conteneur= new JPanel(); //main container
		conteneur.setLayout(null);
		conteneur.setBounds(0,0,800,600); //take all the window
		conteneur.setOpaque(false);
		
		JPanel conteneurMessage= new JPanel();//container for the principal message
		conteneurMessage.setLayout(null);
		conteneurMessage.setBounds(300,20,240,140);// window
		JLabel cadre = new JLabel(new ImageIcon("cadre.png"));// decoration for the message
		cadre.setBounds(-5,-2,250,150);// negative boundaries to compensate a small error on the image
		JLabel message = new JLabel("GET READY TO BE A-MAZED",JLabel.CENTER);//principal message
		message.setBounds(-5,0,250,150);
		message.setFont(new Font("Chaparral Pro Lite", Font.BOLD, 13));//we choose special characters and we adapt the size
		conteneurMessage.setBackground(Color.lightGray);
		conteneurMessage.add(cadre);
		conteneurMessage.add(message);
		
		start = new JButton("Start"); //start button
		start.setBounds(600,80,80,80);
		start.setFont(new Font("Chaparral Pro Lite", Font.BOLD, 13));
		start.setBackground(Color.black);
		start.setForeground(new Color(227,255,255,255));
		start.addActionListener(this);
		
		JPanel conteneurDiff= new JPanel();//container for the difficulty bar and message
		conteneurDiff.setLayout(null);
		conteneurDiff.setBounds(0,170,400,200);
		difficulty = new JSlider(JSlider.HORIZONTAL, 1, 5, 3);
		difficulty.setOpaque(false);
		difficulty.setInverted(false); 
		difficulty.setPaintTicks(true);
		difficulty.setMajorTickSpacing(1);
		difficulty.setMinorTickSpacing(5);
		difficulty.setPaintLabels(true);
		difficulty.setBounds(15,100,400-30,200-100);
		difficulty.setValue(3);
		
		JLabel messageDificulty = new JLabel("Difficulty");
		messageDificulty.setBounds(170,0,200,100);
		messageDificulty.setFont(new Font("Chaparral Pro Lite", Font.BOLD, 20));
		messageDificulty.setForeground(new Color(227,255,255,255));
		conteneurDiff.setBackground(Color.black);
		conteneurDiff.setOpaque(false);
		conteneurDiff.add(messageDificulty);
		conteneurDiff.add(difficulty);
		
		JPanel conteneurVis= new JPanel();//container for the visibility bar and message
		conteneurVis.setLayout(null);
		conteneurVis.setBounds(5,370,400,200);
		visibility= new JSlider(JSlider.HORIZONTAL, 1, 3, 3);
		visibility.setOpaque(false);
		visibility.setInverted(false); 
		visibility.setPaintTicks(true);
		visibility.setMajorTickSpacing(1);
		visibility.setMinorTickSpacing(5);
		visibility.setPaintLabels(true);
		visibility.setBounds(10,75,400-30,200-100);
		visibility.setValue(2);
		
		JLabel messageVisibility = new JLabel("Visibility");
		messageVisibility.setBounds(150,0,200,100);
		messageVisibility.setFont(new Font("Chaparral Pro Lite", Font.BOLD, 20));
		messageVisibility.setForeground(new Color(227,255,255,255));
		conteneurVis.setOpaque(false);
		conteneurVis.add(messageVisibility);
		conteneurVis.add(messageVisibility);
		conteneurVis.add(visibility);
		
		JPanel conteneurPersonnage= new JPanel(); //CHARACTER-we are going to use a JComboBox to catch the user choice of character
		conteneurPersonnage.setLayout(null);
		conteneurPersonnage.setBounds(450,170,400,400);// we're putting it under the container of the message
		conteneurPersonnage.setOpaque(false);
		JLabel messagePersonnage = new JLabel("Choose your favorite character!");
		messagePersonnage.setBounds(10,10,400,100);
		conteneurPersonnage.add(messagePersonnage);
		messagePersonnage.setFont(new Font("Chaparral Pro Lite", Font.BOLD, 20));
		messagePersonnage.setForeground(new Color(227,255,255,255));
		String[] personnagesTab = new String[3]; //setting up the chracaters
		ImageIcon[] imagesTab = new ImageIcon[3];
		personnagesTab[0]="Girl";
		imagesTab[0]= new ImageIcon("Character1_Right.png");
		personnagesTab[1]= "Boy";
		imagesTab[1]=new ImageIcon("Character2_Right.png");
		personnagesTab[2]= "Godzilla";
		imagesTab[2]=new ImageIcon("Character3_Right.png");
		Personnage=new JComboBox(personnagesTab);
		Personnage.setBackground(Color.lightGray);
		Personnage.setBounds(10,100,300,30);
		Personnage.setSelectedIndex(1);
		Personnage.addActionListener(this);
		conteneurPersonnage.add(Personnage);
		
		JPanel conteneurWall= new JPanel(); //WALL-we are going to use a JComboBox to catch the user choice of Wall
		conteneurWall.setLayout(null);
		conteneurWall.setBounds(450,350,400,400);
		conteneurWall.setOpaque(false);
		JLabel messageWall = new JLabel("Choose your favorite wall!");
		messageWall.setBounds(10,10,400,100);
		messageWall.setFont(new Font("Chaparral Pro Lite", Font.BOLD, 20));
		messageWall.setForeground(new Color(227,255,255,255));
		String[] wallTab = new String[5];
		wallTab[0]="1";
		wallTab[1]= "2";
		wallTab[2]= "3";
		wallTab[3]= "4";
		wallTab[4]= "5";
		Wall=new JComboBox(wallTab);
		Wall.setBackground(Color.lightGray);
		Wall.setBounds(10,100,300,30);
		Wall.setSelectedIndex(0);
		Wall.addActionListener(this);
		
		conteneurWall.add(Wall); //we add everything to the principal container
		conteneurWall.add(messageWall);
		conteneur.add(conteneurDiff);
		conteneur.add(conteneurMessage);
		conteneur.add(conteneurVis);
		conteneur.add(conteneurPersonnage);
		conteneur.add(conteneurWall);
		conteneur.add(start);
		setContentPane(conteneur);
		
		JLabel Fond = new JLabel(new ImageIcon("Lab_Background_Pic.jpg")); //background image
		Fond.setBounds(0,0,800,600);
		conteneur.add(Fond);
		this.setVisible(true);
    	}
	
	public void actionPerformed (ActionEvent e){
		if(e.getSource()==start){
			Object X =Personnage.getSelectedItem(); //choosen character
			if(X=="Girl") tableaudeChoix[0]=2;
			else if(X=="Boy") tableaudeChoix[0]=1;
			else if(X=="Godzilla") tableaudeChoix[0]=3;
			tableaudeChoix[1]= difficulty.getValue();//choosen difficulty
			tableaudeChoix[2]=visibility.getValue();//choosen visibility
			Object Y =Wall.getSelectedItem(); //choosen wall
			if(Y=="1") tableaudeChoix[3]=1;
			else if(Y=="2") tableaudeChoix[3]=2;
			else if(Y=="3") tableaudeChoix[3]=3;
			else if(Y=="4") tableaudeChoix[3]=4;
			else if(Y=="5") tableaudeChoix[3]=5;
			begin=true;
		}
	}


	public static  void main (String args []){
		Labyrinth menu = new Labyrinth();
		LabyrinthDisplay game = null;
		while(true){
			if(game==null){
				System.out.print("");
				if(begin){
					int diff =tableaudeChoix[1];
					int chara = tableaudeChoix[0];
					int wall = tableaudeChoix[3];
					int vis = tableaudeChoix[2];
					game = new LabyrinthDisplay(diff,chara,wall,vis);
					
				}
			}
			else{
				System.out.print("");
				if(game.gameWon){
					begin=false;
					game.dispose();
					game=null;
				}
			}
		}
	}

}

