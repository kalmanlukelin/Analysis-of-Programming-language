import java.util.*;
import java.io.*;

//04-cookbood.
public class Four {
	static class res{
		String data=null;
		List<String> words=new ArrayList<>();
		Set<String> stop_words=new HashSet<>();
		Map<String, Integer> freq_words=new HashMap<>();
		List<Map.Entry<String, Integer>> res_list=new ArrayList<>();
	}
	
    //Read the whole file into string.
    static void read_file(File file, res r) {
        Scanner sc=null;
        try {
		    sc=new Scanner(file);
		    r.data=new Scanner(file).useDelimiter("\\Z").next();
	    }
	    catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
    }
    
    //Repalce nonalphanumeric characters with white space. 
    static void filter_chars(res r) {
    	r.data=r.data.replaceAll("[^a-zA-Z]+", " ");
    }
    
    //Change all the characters into lowercase.
    static void normalize(res r) {
    	r.data=r.data.toLowerCase();
    }
    
    //Add all the words from the string to the list.
    static void scan(res r){
    	Scanner scan=new Scanner(r.data);
    	while(scan.hasNext()) {
    		r.words.add(scan.next());
    	}
    }
    
    //Get stop words set.
    static void store_stop_words(res r) {
    	File stop_file = new File("../stop_words.txt");
        Scanner sc=null;
        try {
 		    sc=new Scanner(stop_file);
 	    }
 	    catch (FileNotFoundException e) {
 	        e.printStackTrace();
 	    }
        while(sc.hasNextLine()){
            String[] s_words=sc.nextLine().split(",");
            for(String s : s_words) {
            	if(s.equals("")) continue;
            	r.stop_words.add(s);
            }
 	   }
    }
    
    //Remove the stop words in the stop.txt file.
    static void removeStopWords(res r){
    	//Store the words except for stop words.
    	List<String> c_word=new ArrayList<>();
    	for(int i=0; i<r.words.size(); i++) {
    		if(!r.stop_words.contains(r.words.get(i)) && !r.words.get(i).equals("s")) {
    			c_word.add(r.words.get(i));
    		}
    	}
    	r.words=c_word;
    }
    
    //Using Hash Map to record the frequency of each words.
    static void frequency(res r){
    	for(String w : r.words) {
    		if(!r.freq_words.containsKey(w)) {
    			r.freq_words.put(w, 0);
    		}
    		r.freq_words.put(w, r.freq_words.get(w)+1);
    	}
    }
    
    //Function to sort hashmap by values 
    static void sortByValue(res r) 
    {
    	// Create a list from elements of HashMap 
    	r.res_list = new ArrayList<Map.Entry<String, Integer> >(r.freq_words.entrySet());
        
        // Sort the list 
        Collections.sort(r.res_list, new Comparator<Map.Entry<String, Integer> >() { 
            public int compare(Map.Entry<String, Integer> o1,  
                               Map.Entry<String, Integer> o2) 
            { 
                return (o2.getValue()).compareTo(o1.getValue()); 
            } 
        });
    }
    
    public static void main(String []args) {
        // File file = new File("../pride-and-prejudice.txt");
        File file = new File(args[0]);
        res r=new res();
        read_file(file, r);
        filter_chars(r);
        normalize(r);
        scan(r);
        store_stop_words(r);
        removeStopWords(r);
        frequency(r);
        sortByValue(r);
        
        for(int i=0; i<25; i++) {
        	System.out.println(r.res_list.get(i).getKey() + " - " + r.res_list.get(i).getValue());
	    }
    }
}
