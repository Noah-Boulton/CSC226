package SelectMedian;
import java.util.*;
import java.io.*;
/**
 *
 * @author Rahnuma Islam Nishat
 * January 20, 2017
 * CSC 226 - Spring 2017
 */
public class SelectMedian {

       
    public static int LinearSelect(int[] A, int k){
        if(A == null){
        	return -1;
        }
        if(A.length == 1){
        	return A[0];
        }
        int numberOfSequences = (int)Math.ceil(A.length/7.0);
        int[] X = new int[numberOfSequences];

        //Divide the array into sequences of length 7
        int[][] sequences = divide(A, numberOfSequences);
        
        //Select the median of each sequence 
        for(int i = 0; i < numberOfSequences; i++){
          	Arrays.sort(sequences[i]);
            X[i] = sequences[i][sequences[i].length/2];
        }

        //Find the median of the medians and make it the pivot 
        int pivot = LinearSelect(X, (int)Math.ceil(X.length/2.0));

        //Sort the array around the pivot
        int[][] lists = split(A, pivot);
        int[] L = lists[0];
        int[] E = lists[1];
        int[] G = lists[2];
        if(k <= L.length){
        	return LinearSelect(L, k);
        }else if(k <= L.length + E.length){
        	return pivot;
        } else{
        	return LinearSelect(G, k - L.length - E.length);
        }
    }    

    private static int[][] divide(int[] A, int numberOfSequences){
    	int[][] sequences = new int[numberOfSequences][];
        int j = 0;
        for(int i = 0; i < numberOfSequences - 1; i++){
            int[] b = new int[7];
            for(int m = 0; m < 7; j++, m++){
                b[m] = A[j];
            }
            sequences[i] = b;
        }
        int[] b = new int[A.length - j];
        for(int m = 0; j < A.length; j++, m++){
            b[m] = A[j];
        }
        sequences[numberOfSequences-1] = b;
        return sequences;
    }

    private static int[][] split(int[] A, int pivot){
        int l = 0;
    	int e = 0;
    	int g = 0;
    	for(int i = 0; i < A.length; i++){
    		if(A[i] < pivot){
    			l++;
    		} else if(A[i] > pivot){
    			g++;
    		} else{
    			e++;
    		}
    	}
    	int[] L = new int[l];
    	int[] E = new int[e];
    	int[] G = new int[g];
    	l = 0;
    	e = 0;
    	g = 0;
    	for(int i = 0; i < A.length; i++){
    		if(A[i] < pivot){
    			L[l] = A[i];
                l++;
    		} else if(A[i] > pivot){
    			G[g] = A[i];
                g++;
    		} else{
    			E[e] = A[i];
                e++;
    		}
    	}
        int[][] lists = new int[3][];
    	lists[0] = L;
    	lists[1] = E;
    	lists[2] = G;
        return lists;
    }   
    
    public static void main(String[] args) {
        int[] C = {50, 54, 49, 49, 48, 49, 56, 52, 51, 52, 50, 59};
        System.out.println("The median weight is " + LinearSelect(C, C.length/2));
        int[] B = {3, 590, 656, 92, 779, 624, 475, 84, 727, 41};
        System.out.println("The median weight is " + LinearSelect(B, B.length/2));
        int[] D = {1, 2, 4, 5, 3};
        System.out.println("The median weight is " + LinearSelect(D, (int)Math.ceil(D.length/2.0)));
        int[] E = {3, 75, 12, 20};
        System.out.println("The median weight is " + LinearSelect(E, E.length/2));
        int[] F = {75};
        System.out.println("The median weight is " + LinearSelect(F, F.length/2));
        int[] G = {49, 54, 49, 49, 48, 49, 56, 52, 51, 52, 49, 59, 81, 40, 40, 40, 46, 51, 51, 108, 47, 36, 91, 82, 55, 60, 60, 60, 46, 45, 48, 50, 30};
        System.out.println("The median weight is " + LinearSelect(G, G.length/2));
        System.out.println();

        System.out.print("Input a file name to test: ");
        Scanner console = new Scanner(System.in);
        String file = console.next();
        try{
            Scanner s = new Scanner(new File(file));
            int n = 0;
            while(s.hasNextInt()){
                int tmp = s.nextInt();
                n++;
            }
            int[] A = new int[n];
            s = new Scanner(new File(file));
            for(int i = 0; i < A.length; i++){
                A[i] = s.nextInt();
            }
            System.out.println("The median weight is " + LinearSelect(A, A.length/2));
        } catch(FileNotFoundException e){
            System.out.println("File not found");
            System.exit(-1);
        }

    }
    
}
