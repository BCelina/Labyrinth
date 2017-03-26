public class Maze{
	public boolean[][] maze;
	
	
	public Maze(int n){
		maze=new boolean [n*9][n*16];
		generate();
	}
	
	public void generate(){
		for (int i=0; i<maze.length; i++){ //building the surrounding walls
			for (int j=0; j<maze[1].length; j++){
				maze[i][j]=false;
			}
		}
		int k=1+(int)(Math.random()*(maze.length-2)); //setting a starting point
		maze[k][0]=maze[k][1]=true;
		construct(k,1);
		addWays();
	}
	
	public void addWays(){
		long count=countWall();
		double hell=2; //-> HERE WE DEFINE THE HELL!!! :D (advised 2)
		while (count>((maze.length-1)*(maze[0].length-1)/hell)){ //setting density of ways
			int k1=1+(int)(Math.random()*(maze.length-2));
			int k2=1+(int)(Math.random()*(maze[0].length-2));
			if (!maze[k1][k2]) {maze[k1][k2]=true; count--;} //adding a random spot
		}
	}
	
	public void construct(int x, int y){
		int temp=(int)(Math.random()*11); //setting the weight of movement 
		/*if ((temp<1)&&(!maze[x][y-1])&&(y-1>0)) {maze[x][y-1]=true; y--;}//left 1
		else */if ((temp<4)&&(!maze[x-1][y])&&(x-1>0)) {maze[x-1][y]=true; x--;}//up 3
		else if ((temp<7)&&(!maze[x+1][y])&&(x+1<maze.length-1)) {maze[x+1][y]=true; x++;}//down 3
		else if ((!maze[x][y+1])&&(y+1<maze[0].length-1)) {maze[x][y+1]=true; y++;}//right 4
		else if ((!maze[x+1][y])&&(x+1<maze.length-1)) {maze[x+1][y]=true; x++;}
		else if ((!maze[x-1][y])&&(x-1>0)) {maze[x-1][y]=true; x--;}
		//else if ((!maze[x][y-1])&&(y-1>0)) {maze[x][y-1]=true; y--;}
		else generate();
		if ((y+1)!=(maze[0].length-1)) construct(x,y); //checking if not arrived on right
		maze[x][y+1]=true; //confirming arrival to the other end
	}
	
	public long countWall(){ //counting the number of walls inside the maze
		long count=0;
		for (int i=1; i<maze.length-1; i++){
			for (int j=1; j<maze[0].length-1; j++){
				if (!maze[i][j]) count++;
			}
		}
		return count;
	}	
	
	public void draw(){ //just drawing
		for (int i=0; i<maze.length; i++){
			for (int j=0; j<maze[1].length; j++){
				if (maze[i][j]) System.out.print("-");
				else System.out.print("x");
			}
			System.out.println();
		}
	}
					
	
	public static void main (String[] args) {
		Maze mmaze=new Maze(5);
		mmaze.draw();
	}
}

