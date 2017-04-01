import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


public class FirstFrame extends JFrame implements ActionListener {
			
	public int[] tableaudeChoix = new int[2];
	public JComboBox Personnage;
	public JSlider difficulty;	
	public Image background1; // background

	

	public FirstFrame(){
	
		this.setLayout(null);
		this.setTitle("MEZED");
		this.setSize(2000,2000);
		setResizable(false);
		this.setLocation(0,0);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//CONTENEUR PRINCIPAL
		 JPanel conteneur= new JPanel();
		conteneur.setLayout(null);
		conteneur.setBounds(0,0,2000,1000); //occue toute la fenetre
		conteneur.setOpaque(false);
		JLabel Fond = new JLabel(new ImageIcon("background.jpg"));
		Fond.setBounds(0,0,1366,728);
		conteneur.add(Fond);
		
		
		
		//conteneur message 
		
		JPanel conteneurMessage= new JPanel();
		conteneurMessage.setLayout(null);
		conteneurMessage.setBounds(600,50,240,140);// milieu de la fenettre en haut
		JLabel cadre = new JLabel(new ImageIcon("cadre.png"));
		cadre.setBounds(-5,-2,250,150);
		
		JLabel message = new JLabel("GET READY TO BE A-MAZED",JLabel.CENTER);
		message.setBounds(0,0,250,150);// le message ocupe tout le conteneur message
		message.setFont(new Font("Serif", Font.BOLD, 13));
		conteneurMessage.setOpaque(false);

		conteneurMessage.add(cadre);
		conteneurMessage.add(message); // on ajoute le message au conteneur		
				

		//barre de dificultés
				
		
		JPanel conteneurDiff= new JPanel();
		conteneurDiff.setLayout(null);
		conteneurDiff.setBounds(0,200,900,800);// on le place en dessous du conteneur du message et occupe la moitie de la largeur de la fenetre
		
		
		
		JLabel messageDificulty = new JLabel("Dificulty");
		messageDificulty.setBounds(300,10,200,100);
		messageDificulty.setFont(new Font("Serif", Font.BOLD, 20));
		messageDificulty.setForeground(new Color(227,210,25,100));
		conteneurDiff.setBackground(Color.black);
		conteneurDiff.setOpaque(false);
		conteneurDiff.add(messageDificulty);
		
		// un JSlider va servir a renvoyer le niveau de difficulte choisit par que l'utilisateur 


		
		difficulty = new JSlider(JSlider.HORIZONTAL, 0,5 ,3); /*(posición vertical, comienza, termina, donde comienza al iniciar programa)*/
		difficulty.setBackground(Color.black);
		difficulty.setInverted(false); 
		difficulty.setPaintTicks(true); //las rayitas que marcan los números
		difficulty.setMajorTickSpacing(1); // de cuanto en cuanto los números en el slider
		difficulty.setMinorTickSpacing(5); //las rayitas de cuanto en cuanto
		difficulty.setPaintLabels(true); //si se ve los números del slider
		difficulty.setBounds(15,110,500,200);
		

		
		conteneurDiff.add(messageDificulty);
		conteneurDiff.add(difficulty);
		
		
		JPanel conteneurPersonnage= new JPanel();
		conteneurPersonnage.setLayout(null);
		conteneurPersonnage.setBounds(800,200,900,800);// on le place en dessous du conteneur du message et occupe la moitie de la largeur de la fenetre
		
		JLabel messagePersonnage = new JLabel("Choose your favorit character!");
		messagePersonnage.setBounds(10,10,200,100);
		conteneurPersonnage.add(messagePersonnage);
		
		messagePersonnage.setFont(new Font("Serif", Font.BOLD, 20));
		messagePersonnage.setForeground(new Color(227,210,25,100));
		conteneurPersonnage.setOpaque(false);
		
		// un JComboBox va servir au choix du personnage 


		String[] personnagesTab = new String[3];
		ImageIcon[] imagesTab = new ImageIcon[3];
		personnagesTab[0]="Girl";
		imagesTab[0]= new ImageIcon("character1.png");
		personnagesTab[1]= "Boy";
		imagesTab[1]=new ImageIcon("character2.png");
		personnagesTab[2]= "Godzilla";
		imagesTab[2]=new ImageIcon("character3.png");
		Personnage=new JComboBox(personnagesTab);
		Personnage.setBounds(10,100,300,100);
		Personnage.setBackground(Color.black);
		Personnage.addActionListener(this);

		conteneurPersonnage.add(Personnage);

		
	
		conteneur.add(conteneurDiff);
		conteneur.add(conteneurMessage);
		conteneur.add(conteneurPersonnage);
		
		
		setContentPane(conteneur);
		
		this.setVisible(true);
		


    

    }
	
	public void actionPerformed (ActionEvent e){
			
 //personnage choisi par l'utilisteur
			Object X =Personnage.getSelectedItem();
			if(X=="Girl"){
				tableaudeChoix[0]=1;
			}else if(X=="Boy"){
				tableaudeChoix[0]=2;
			}else if(X=="Gotsila"){
				tableaudeChoix[0]=3;
			}
			tableaudeChoix[1]= difficulty.getValue();
			System.out.println("choosen character"+tableaudeChoix[0]+"choosen dificulty"+tableaudeChoix[1]);

	}

	  /**  public void paint(Graphics g){
        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), this);
        g.setOpaque(False);
        super.paint(g);**/
		
		
  
	public static  void main (String args []){
		new FirstFrame();
		
		
	}

}

