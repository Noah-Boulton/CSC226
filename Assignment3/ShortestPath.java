//package shorteshpath;
/* ShortestPath.java
   CSC 226 - Spring 2017
	  
   This template includes some testing code to help verify the implementation.
   To interactively provnumbere test inputs, run the program with
	java ShortestPath
	
   To conveniently test the algorithm with a large input, create a text file
   containing one or more test graphs (in the format described below) and run
   the program with
	java ShortestPath file.txt
   where file.txt is replaced by the name of the text file.
   
   The input consists of a series of graphs in the following format:
   
	<number of vertices>
	<adjacency matrix row 1>
	...
	<adjacency matrix row n>
	
   Entry A[i][j] of the adjacency matrix gives the weight of the edge from 
   vertex i to vertex j (if A[i][j] is 0, then the edge does not exist).
   Note that since the graph is undirected, it is assumed that A[i][j]
   is always equal to A[j][i].
	
   An input file can contain an unlimited number of graphs; each will be 
   processed separately.


   B. Bird - 08/02/2014
*/

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.PriorityQueue;
import java.util.Stack;

//Do not change the name of the ShortestPath class
public class ShortestPath{
	public static int numVerts;
	public static Vertex[] verticies;
	public static int infinity;
	/* ShortestPath(G)
		Given an adjacency matrix for graph G, calculates and stores the shortest paths to all the
				vertces from the source vertex.
		If G[i][j] == 0, there is no edge between vertex i and vertex j
		If G[i][j] > 0, there is an edge between vertices i and j, and the
		value of G[i][j] gives the weight of the edge.
		No entries of G will be negative.
	*/

	/**This method implements Dijkstra's shortest 
	 * path algorithm using a priority queue.
	 */
	static void ShortestPath(int[][] G, int source){
		numVerts = G.length;           
		verticies = new Vertex[numVerts];
		infinity = 2000 * numVerts;
		PriorityQueue<Vertex> pq = new PriorityQueue<>(numVerts);
		/*Add each edge in the graph to the priority queue*/
		for(int i=0;i<numVerts;i++){
			if(i == source){
				verticies[i] = new Vertex(i, 0);
			}
			else{
				verticies[i] = new Vertex(i, infinity);
			}
			pq.add(verticies[i]);
		}
		/**As long as the queue is not empty, 
		 * remove the min edge and relax the
		 * verticies of the graph.
		 */
		while(pq.size() > 0){
			Vertex current = pq.poll();

			for(int i=0;i<numVerts;i++){
		 		if(G[current.number][i] != 0 && !verticies[i].visited){ 
		 			pq.remove(verticies[i]);
					if(verticies[i].distance > (current.distance + G[current.number][i])){
						verticies[i].distance = current.distance + G[current.number][i];
						verticies[i].parent = current;
					}
		 			pq.add(verticies[i]);
		 		}
		 	}
		 	current.visited = true;
		}  
	}
		
	/**This method prints out the shortest path
	 * from a soruce Vertex to each other Vertex using a stack.
	 */
	static void PrintPaths(int source){
		/**Uses a stack at to store the path of to each Vertex.
		 * Pushes each parent onto the stack and then pops them
		 * off to print out the path.
		 */
		for(int i = 0; i < verticies.length; i++){
			Stack<Vertex> tmp = new Stack<Vertex>();
			if(i == source){
				tmp.push(verticies[source]);
			}
			Vertex temp = verticies[i];
			while(temp.parent != null){
				tmp.push(temp);
				temp = temp.parent;
			}
			if(verticies[i].distance < infinity){
				System.out.print("The path from " + source + " to " + i + " is: " + source);
				while(!tmp.isEmpty()){
					Vertex t = tmp.pop();
					System.out.print(" --> " + t.number);
				}
				System.out.println(" and the total distance is : " + verticies[i].distance);
			} else{
				System.out.println("There is no path from " + source + " to " + verticies[i].number + ".");
			}
		}
	}
		
	/* main()
	   Contains code to test the ShortestPath function. You may modify the
	   testing code if needed, but nothing in this function will be consnumberered
	   during marking, and the testing process used for marking will not
	   execute any of the code below.
	*/
	public static void main(String[] args) throws FileNotFoundException{
		Scanner s;
		if (args.length > 0){
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			s = new Scanner(System.in);
			System.out.printf("Reading input values from stdin.\n");
		}
		
		int graphNum = 0;
		double totalTimeSeconds = 0;
		
		//Read graphs until EOF is encountered (or an error occurs)
		while(true){
			graphNum++;
			if(graphNum != 1 && !s.hasNextInt())
				break;
			System.out.printf("Reading graph %d\n",graphNum);
			int n = s.nextInt();
			int[][] G = new int[n][n];
			int valuesRead = 0;
			for (int i = 0; i < n && s.hasNextInt(); i++){
				for (int j = 0; j < n && s.hasNextInt(); j++){
					G[i][j] = s.nextInt();
					valuesRead++;
				}
			}
			if (valuesRead < n*n){
				System.out.printf("Adjacency matrix for graph %d contains too few values.\n",graphNum);
				break;
			}
			long startTime = System.currentTimeMillis();
			
			ShortestPath(G, 0);
						PrintPaths(0);
			long endTime = System.currentTimeMillis();
			totalTimeSeconds += (endTime-startTime)/1000.0;
			
			//System.out.printf("Graph %d: Minimum weight of a 0-1 path is %d\n",graphNum,totalWeight);
		}
		graphNum--;
		System.out.printf("Processed %d graph%s.\nAverage Time (seconds): %.2f\n",graphNum,(graphNum != 1)?"s":"",(graphNum>0)?totalTimeSeconds/graphNum:0);
	}
}

/**A simple Vertex class that implements a compareTo method
 * to allow the verticies to be compared by distance. 
 */
class Vertex implements Comparable<Vertex>{
	public int distance;
	public int number;
	public Vertex parent;
	public boolean visited;
	
	public Vertex(int number, int distance){
		this.number = number;
		this.distance = distance;
		parent = null;
		visited = false;
	}

	@Override
	public int compareTo(Vertex t) {   
		return distance - t.distance;
	}
}


