import java.io.*;
import java.util.*;
 
public class TestMain {
	
	
	public static void main(String args[]) {
//		
//		BufferedReader br = new BufferedReader (new InputStreamReader(System.in));
//		Hashtable<Integer, String> testHash = new Hashtable<Integer, String> (100);
//		
//		while(true){
//			try {
//				
//				
//				
//				String input = br.readLine();
//				
//				if( input.compareTo("QUIT") == 0 ) {
//					break;
//				}
//				
//				String[] argsInput = input.split(" ");
//				
//				Integer key = Integer.parseInt(argsInput[0]);
//				String value = argsInput[1];
//				
//				testHash.put(key, value);
//				System.out.println( testHash.size() );
//				System.out.println( testHash.keySet() );
//				System.out.println( testHash.values() );
//				
//				
//				
//				
//			} 
//			
//			catch (Exception e)
//			{
//				System.out.println("Error : " + e );
//			}
//			
//		}
//		
//		
//		Bucket testbucket = new Bucket();
//		boolean flag = testbucket.getCheck();
//		String show;
//		if(flag) {
//			show = "flag is true";
//		} else {
//			show = "flag is false";
//		}
//			
//		System.out.println(show);
//		
//		
//		
//		
//		flag = true;
//		if(flag) {
//			show = "flag is true";
//		} else {
//			show = "flag is false";
//		}
//			
//		System.out.println(show);
//		
//		
//		
//		
//		if(testbucket.getCheck()){
//			show = "class variance is true";
//		} else {
//			show = "class variance is false";
//		}
//
//		System.out.println(show);
//		
//		
//		
//		
//		
//	
		
		
		LinkedList<Integer> list1 = new LinkedList<Integer>();
		list1.add(1);
		list1.add(2);
		list1.add(3);
				
		for(Integer a : list1) {
			for(Integer b : list1) {
				if( b.compareTo(a) != 0) {
					System.out.println(b);
				}
			}
			System.out.println();
			
		}
		
		String a = "가";
		
		
		System.out.println((int)a.charAt(0));
		
		
	
		
		
	}
		
		
	
}



class Bucket {
	private boolean check;
	
	public Bucket (){
		check = false;
	}
	
	public void setCheck(boolean what) {
		check = what;
	}
	
	public boolean getCheck() {
		return check;
	}
	                                    
	                                    
	                                   
	
}