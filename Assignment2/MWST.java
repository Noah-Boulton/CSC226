/* MWST.java
   CSC 225 - Spring 2012
   Assignment 5 - Template for a Minimum Weight Spanning Tree algorithm
   
   The assignment is to implement the mwst() method below, using any of the algorithms
   studied in the course (Kruskal, Prim-Jarnik or Baruvka). The mwst() method computes
   a minimum weight spanning tree of the provided graph and returns the total weight
   of the tree. To receive full marks, the implementation must run in O(mlog(n)) time
   on a graph with n vertices and m edges.

   This template includes some testing code to help verify the implementation.
   Input graphs can be provided with standard input or read from a file.
   
   To provide test inputs with standard input, run the program with
	java MWST
   To terminate the input, use Ctrl-D (which signals EOF).
   
   To read test inputs from a file (e.g. graphs.txt), run the program with
    java MWST graphs.txt
	
   The input format for both methods is the same. Input consists
   of a series of graphs in the following format:
   
    <number of vertices>
	<adjacency matrix row 1>
	...
	<adjacency matrix row n>
	
   For example, a path on 3 vertices where one edge has weight 1 and the other
   edge has weight 2 would be represented by the following
   
    3
	0 1 0
	1 0 2
	0 2 0
	
   An input file can contain an unlimited number of graphs; each will be processed separately.
   
   B. Bird - 03/11/2012
*/

import java.util.Scanner;
import java.util.*;
import java.io.File;
import java.util.Comparator;

public class MWST{

	private static class Edge {
		int endpoint1;
		int endpoint2;
		int weight;

		public Edge(int endpoint1, int endpoint2, int weight){
			this.endpoint1 = endpoint1;
			this.endpoint2 = endpoint2;
			this.weight = weight;
		}
	}	

	public static class EdgeComparator implements Comparator<Edge> {
		@Override
		public int compare(Edge e, Edge f) {
			if(e == null || f == null){
				System.out.println("Error in comparing edges.");
				System.exit(-1);
			}
			return e.weight - f.weight;
		}
	}
	/* mwst(G)
		Given an adjacency matrix for graph G, return the total weight
		of all edges in a minimum weight spanning tree.
		
		If G[i][j] == 0, there is no edge between vertex i and vertex j
		If G[i][j] > 0, there is an edge between vertices i and j, and the
		value of G[i][j] gives the weight of the edge.
		No entries of G will be negative.
	*/
	static int mwst(int[][] G){
		// If G is not connected then there is not a spanning tree
		if(!isConnected(G)){
			System.out.println("The graph is not connected, no spanning tree exists.");
			System.exit(-1);
		}
		int numVerts = G.length;

		// Int array to store the weights of the MWST
		int[] t = new int[numVerts -1];
		int added = 0;

		// Put all the edges into a PQ
		// Need to keep track of which verticies are connected to the edge
		Comparator<Edge> comparator = new EdgeComparator();
		Queue<Edge> edges = new PriorityQueue(1, comparator);

		// Boolean array to keep track of visited verticies
		boolean[] visited = new boolean[numVerts];

		// Add all the edges connect to vertex 0
		for(int i = 0; i < numVerts; i++){
			if(G[0][i] > 0){
				Edge e = new Edge(0, i, G[0][i]);
				edges.add(e);
			}
		}

		// Mark 0 as visited
		visited[0] = true;

		while(edges.size() > 0 && added < t.length){
			// Remove the min of the priority queue
			Edge e = edges.poll();

			/**Check that both of its end points are not visited
			 * if either endpoint is not visited then add all edges
			 * connected to it to the priority queue
			 */
			if(visited[e.endpoint1] && visited[e.endpoint2]){
				continue;
			} else if(visited[e.endpoint1]){
				t[added] = e.weight;
				added++;
				visit(G, e.endpoint2, visited, edges);
			} else if(visited[e.endpoint2]){
				t[added] = e.weight;
				added++;
				visit(G, e.endpoint1, visited, edges);
			} 
		}
		
		/**Add the weight of each edge in the minimum weight spanning tree
		 * to totalWeight, which will store the total weight of the tree.
		 */
		int totalWeight = 0;
		for(int i = 0; i < t.length; i++){
			totalWeight += t[i];
		}
		
		return totalWeight;
	}

	/**Private helper method to visit a vertex.
	 * Marks the vertex as visited in the boolean array and adds 
	 * all edges connected to it to the priority queue.
	 */
	private static void visit(int[][] G, int e, boolean[] visited, Queue edges){
		visited[e] = true;
		for(int i = 0; i < G.length; i++){
			if(G[e][i] > 0){
				Edge f = new Edge(e, i, G[e][i]);
				edges.add(f);
			}
		}
	}

	/**Private helper method to generate a test graph
	 * of size n.
	 */
	private static int[][] test(int n){
		int[][] G = new int[n][n];
		Random rand = new Random();
		for(int i = 0; i < n; i++){
			for(int j = i +1; j < n; j++){
				if(i == j){
					continue;
				}
				int tmp = rand.nextInt(n);
				G[i][j] = tmp;
				G[j][i] = tmp;
			}
		}
		System.out.println("Random test graph: ");
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				if(G[i][j] > 9){
					System.out.print(G[i][j] + " ");
				} else{
					System.out.print(G[i][j] + "  ");
				}
			}
			System.out.println();
		}
		return G;
	}

	public static void main(String[] args){
		/* Code to test your implementation */
		/* You may modify this, but nothing in this function will be marked */

		int graphNum = 0;
		Scanner s;

		if (args.length > 0){
			//If a file argument was provided on the command line, read from the file
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s\n",args[0]);
		}else{
			//Otherwise, read from standard input
			s = new Scanner(System.in);
			System.out.printf("Reading input values from stdin.\n");
		}
		
		//Read graphs until EOF is encountered (or an error occurs)
		while(true){
			graphNum++;
			if(!s.hasNextInt())
				break;
			System.out.printf("Reading graph %d\n",graphNum);
			int n = s.nextInt();
			int[][] G = new int[n][n];
			int valuesRead = 0;
			for (int i = 0; i < n && s.hasNextInt(); i++){
				G[i] = new int[n];
				for (int j = 0; j < n && s.hasNextInt(); j++){
					G[i][j] = s.nextInt();
					valuesRead++;
				}
			}
			if (valuesRead < n*n){
				System.out.printf("Adjacency matrix for graph %d contains too few values.\n",graphNum);
				break;
			}
			if (!isConnected(G)){
				System.out.printf("Graph %d is not connected (no spanning trees exist...)\n",graphNum);
				continue;
			}
			int totalWeight = mwst(G);
			System.out.printf("Graph %d: Total weight is %d\n",graphNum,totalWeight);		
		}
		int[][] K = test(10);
		int totalWeight = mwst(K);
		System.out.printf("Graph %d: Total weight is %d\n",graphNum,totalWeight);
	}

	/* isConnectedDFS(G, covered, v)
	   Used by the isConnected function below.
	   You may modify this, but nothing in this function will be marked.
	*/
	static void isConnectedDFS(int[][] G, boolean[] covered, int v){
		covered[v] = true;
		for (int i = 0; i < G.length; i++)
			if (G[v][i] > 0 && !covered[i])
				isConnectedDFS(G,covered,i);
	}
	   
	/* isConnected(G)
	   Test whether G is connected.
	   You may modify this, but nothing in this function will be marked.
	*/
	static boolean isConnected(int[][] G){
		boolean[] covered = new boolean[G.length];
		for (int i = 0; i < covered.length; i++)
			covered[i] = false;
		isConnectedDFS(G,covered,0);
		for (int i = 0; i < covered.length; i++)
			if (!covered[i])
				return false;
		return true;
	}
}