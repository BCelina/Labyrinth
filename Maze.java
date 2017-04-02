public class Maze{
	public boolean[][] maze;
	public boolean[][] solution; //solution to send to the visual
	private char[][] sol; //solution to work inside
	
	
	public Maze(int n){
		maze=new boolean [n*9][n*16];
		solution=new boolean [n*9][n*16];
		sol= new char [n*9][n*16];
		generate();
	}
	
	public void generate(){
		for (int i=0; i<maze.length; i++){ //building the surrounding walls
			for (int j=0; j<maze[1].length; j++){
				maze[i][j]=false;
				sol[i][j]='-';
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
		int temp=(int)(Math.random()*9); //setting the weight of movement 
		if ((temp<3)&&(!maze[x-1][y])&&(x-1>0)) {maze[x-1][y]=true; x--;}//up 3
		else if ((temp<6)&&(!maze[x+1][y])&&(x+1<maze.length-1)) {maze[x+1][y]=true; x++;}//down 3
		else if ((!maze[x][y+1])&&(y+1<maze[0].length-1)) {maze[x][y+1]=true; y++;}//right 3
		else if ((!maze[x+1][y])&&(x+1<maze.length-1)) {maze[x+1][y]=true; x++;}
		else if ((!maze[x-1][y])&&(x-1>0)) {maze[x-1][y]=true; x--;}
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
	
	public int getStart(){
		int tmp=0;
		for (int i=0; i<maze.length; i++){
			if (maze[i][0]){
				tmp=i;
				break;
			}
		}
		return tmp;
	}
	
	public void solveMaze(int xx, int yy){ //solving with starting point given by the graphics
		int x=xx;
		int y=yy;
		solve(x,y);
		for (int i=0; i<solution.length; i++){ //transfering data to the boolean array
			for (int j=0; j<solution[1].length; j++){
				if (sol[i][j]=='+') solution[i][j]=true;
			}
		}
	}
	
	public boolean solve(int x, int y){ //find a solution by using backtracking
		if (x<0 || y<0 || x>=maze.length || y>=maze[0].length) return false;
		if ((y==maze[0].length-1)&&(maze[x][y])) {sol[x][y]='+'; return true;}
		if (!maze[x][y] || sol[x][y]=='x' || sol[x][y]=='+') return false;
		sol[x][y]='+'; //mark the cell to the solution
		if (solve(x,y+1)) return true; //try right
		if (solve(x-1,y)) return true; //try up
		if (solve(x+1,y)) return true; //try down
		if (solve(x,y-1)) return true; // try left
		sol[x][y]='x'; //unmark the cell as solution
		return false;
	}
}

