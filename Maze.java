public class Maze{
	public boolean[][] maze;
	
	
	public Maze(int n){
		maze=new boolean [n*9][n*16];
		generate(n);
	}
	
	public void generate(int n){
		for (int i=0; i<maze[0].length; i++){
			maze[0][i]=false;
			maze[maze.length-1][i]=false;
		}
		for (int i=0; i<maze.length; i++){
			maze[i][0]=false;
			maze[i][maze[0].length-1]=false;
		}
		for (int i=1; i<maze.length-1; i++){
			for (int j=1; j<maze[1].length-1; j++){
				double k=Math.random();
				if (k<=0.34) maze[i][j]=false;
				else maze[i][j]=true;
			}
		}
		for (int i=0; i<maze.length; i++){
			if (maze[i][1]){
				maze[i][0]=true;
				break;
			}
		}
		for (int i=0; i<maze.length; i++){
			if (maze[i][maze[i].length-2]){
				maze[i][maze[i].length-1]=true;
				break;
			}
		}
	}
	
	public void draw(){
		for (int i=0; i<maze.length; i++){
			for (int j=0; j<maze[1].length; j++){
				if (maze[i][j]) System.out.print("-");
				else System.out.print("x");
			}
			System.out.println();
		}
	}
					
	
	public static void main (String[] args) {
		Maze mmaze=new Maze(1);
		mmaze.draw();
	}
}

