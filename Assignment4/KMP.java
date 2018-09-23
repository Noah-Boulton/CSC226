

/* 

Rahnuma Islam Nishat - 08/02/2014
*/

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;


public class  KMP {
    private static String pattern;
    private static int[][] dfa;
    private static int alpha = 256;
   
	/**Creates DFA for the KMP algorithm 
	 * based on the pattern string
	 */   
	public KMP(String pattern) {  
		this.pattern = pattern;
		int pL = pattern.length();
		dfa = new int[alpha][pL];
		dfa[pattern.charAt(0)][0] = 1;
		for(int i = 0, j = 1; j < pL; j++){
			for(int k = 0; k < alpha; k++){
				dfa[k][j] = dfa[k][i];
			}
			dfa[pattern.charAt(j)][j] = j+1;
			i = dfa[pattern.charAt(j)][i];
		}
    }

    /**Preforms the search on the text string
     * using the DFA created above
     */
    public static int search(String txt) {  
   		int i;
   		int j;
   		int n = txt.length();
   		int m = pattern.length();
   		for(i = 0, j = 0; i <n && j < m; i++) {
   			j = dfa[txt.charAt(i)][j];
   		}
   		if(j == m){
   			return i - m;
   		}
   		else{
   			return n;
   		}
   	}
 
  	public static void main(String[] args) throws FileNotFoundException{
		Scanner s;
		if (args.length > 0){
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.println("Unable to open "+args[0]+ ".");
				return;
			}
			System.out.println("Opened file "+args[0] + ".");
			String text = "";
			while(s.hasNext()){
				text+=s.next()+" ";
			}
			for(int i=1; i<args.length ;i++){
				KMP k = new KMP(args[i]);
				int index = search(text);
				if(index >= text.length())System.out.println(args[i]+ " was not found.");
				else System.out.println("The string \""+args[i]+ "\" was found at index "+index + ".");
			}
		}else{
			System.out.println("usage: java SubstringSearch <filename> <pattern_1> <pattern_2> ... <pattern_n>.");
		}
		
		
	}
}




