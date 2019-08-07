import java.util.PriorityQueue;
import java.util.Scanner;

public class WeightedMaze {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		while(T-- > 0) {
			int N = sc.nextInt();
			int M = sc.nextInt();
			int[][] maze = new int[N][M];
			for(int j = 0 ; j < N; j++) 
				for(int k = 0; k < M; k++) 
					maze[j][k] = sc.nextInt();
			
			System.out.println(findPath(maze, N, M));
		}
		
		sc.close();
	}
	
	static class Pair implements Comparable<Pair>{
		int dest_row, dest_col;
		int weight;
		
		Pair(int dest_row, int dest_col, int weight){
			this.dest_row = dest_row;
			this.dest_col = dest_col;
			this.weight = weight;
		}

		@Override
		public int compareTo(Pair o) {
			return Integer.compare(this.weight, o.weight);
			//return weight - o.weight;
		}
		
	}
	
	static int findPath(int maze[][], int N, int M) {
		int[][] dist = new int[N][M];
		//boolean[][] visited = new boolean[N][M];
		int[] rOffsets = {0,0,1,-1};
		int[] cOffsets = {1,-1,0,0};
		
		for(int i = 0; i < N; i++)
			for(int j = 0; j < M; j ++)
				dist[i][j] = 1_000_000_000;
		
		dist[0][0] = maze[0][0];
		//visited[0][0] = true;
		
		PriorityQueue<Pair> pq = new PriorityQueue<>();
		pq.add(new Pair(0, 0, maze[0][0]));
		
		while(!pq.isEmpty()) {
			Pair curr = pq.poll();
			if(curr.weight > dist[curr.dest_row][curr.dest_col]) continue;
			if(curr.dest_row == N-1 && curr.dest_col == M-1) return dist[curr.dest_row][curr.dest_col];
			
			for(int i = 0; i < 4; i++) {
				int r = curr.dest_row + rOffsets[i];
				int c = curr.dest_col + cOffsets[i];
				
				if(r >= 0 && c >= 0 && r < N && c < M && dist[r][c] > dist[curr.dest_row][curr.dest_col] + maze[r][c]) {
					dist[r][c] = dist[curr.dest_row][curr.dest_col] + maze[r][c];
					pq.add(new Pair(r, c, maze[r][c]));
					//visited[r][c] = true;
						
					//if(r == N-1 && c == M-1) return dist[r][c];
					
				}
			}
		}
		
		return -1;
	}

}
