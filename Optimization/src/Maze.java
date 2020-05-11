import java.util.Random;
public class Maze {
	
	final int size;
	int[][] maze; // 0 -> road; 1 -> wall; 2 -> start; 3 -> end
	Window window;
	final int windowSize = 900;
	
	
	
	Maze(int n){
		this.size = n;
		maze = new int[size][size];
		window = new Window("Maze", windowSize, windowSize);
	}
	
	void draw() {
		int length = windowSize / size;
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				setColor(maze[i][j]);
				window.fillRect(i * length, j * length, length, length);
			}
		}
		window.open();
	}
	
	void setColor(int k) {
		switch(k) {
		case 0: window.setColor(255, 255, 255);
			break;
		case 1: window.setColor(0, 0, 0);
			break;
		case 2: window.setColor(255, 0, 0);
			break;
		case 3: window.setColor(0, 0, 255);
			break;
		default: window.setColor(0, 255, 0);
		}
	}
	
	void generateRandomMaze() {
		boolean[][] visited = new boolean[size][size];
		int x = 0;
		int y = 0;
		visited[x][y] = true;
		int n = 1;
		while(n < size * size) {
			
		}
	}
	
	
}
