import java.util.*;
import java.io.*;

public class Five {
    //Read the whole file into string.
    static String read_file(String path) {
    	File file=new File(path);
        String content=null;
        Scanner sc=null;
        try {
		    sc=new Scanner(file);
		    content=new Scanner(file).useDelimiter("\\Z").next();
	    }
	    catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
        return content;
    }
    
    //Repalce nonalphanumeric characters with white space. 
    static String filter_chars(String str_data) {
    	str_data=str_data.replaceAll("[^a-zA-Z]+", " ");
    	return str_data;
    }
    
    //Change all the characters into lowercase.
    static String normalize(String str_data) {
    	return str_data.toLowerCase();
    }
    
    //Add all the words from the string to the list.
    static List<String> scan(String str_data){
    	List<String> words=new ArrayList<>();
    	Scanner scan=new Scanner(str_data);
    	while(scan.hasNext()) {
    		words.add(scan.next());
    	}
    	return words;
    }
    
    //Get stop words set.
    static Set<String> stop_words(File file) {
    	Set<String> set=new HashSet<>();
        Scanner sc=null;
        try {
 		    sc=new Scanner(file);
 	    }
 	    catch (FileNotFoundException e) {
 	        e.printStackTrace();
 	    }
        while(sc.hasNextLine()){
            String[] stop_words=sc.nextLine().split(",");
            for(String s : stop_words) {
            	if(s.equals("")) continue;
            	set.add(s);
            }
 	   }
       return set;
    }
    
    //Remove the stop words in the stop.txt file.
    static List<String> removeStopWords(List<String> words, String s_path){
    	File stop_file = new File(s_path);
    	Set<String> s_word=stop_words(stop_file);
    	//Store the words except for stop words.
    	List<String> c_word=new ArrayList<>();
    	for(int i=0; i<words.size(); i++) {
    		if(!s_word.contains(words.get(i)) && !words.get(i).equals("s")) {
    			c_word.add(words.get(i));
    		}
    	}
    	return c_word;
    }
    
    //Using Hash Map to record the frequency of each words.
    static Map<String, Integer> frequency(List<String> words){
    	Map<String, Integer> map=new HashMap<>();
    	for(String w : words) {
    		if(!map.containsKey(w)) {
    			map.put(w, 0);
    		}
    		map.put(w, map.get(w)+1);
    	}
    	return map;
    }
    
    //Function to sort hashmap by values 
    static List<Map.Entry<String, Integer>> sortByValue(Map<String, Integer> hm) 
    {
        // Create a list from elements of HashMap 
        List<Map.Entry<String, Integer> > list = new ArrayList<Map.Entry<String, Integer> >(hm.entrySet()); 
  
        // Sort the list 
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() { 
            public int compare(Map.Entry<String, Integer> o1,  
                               Map.Entry<String, Integer> o2) 
            { 
                return (o2.getValue()).compareTo(o1.getValue()); 
            } 
        });
        return list; 
    }
    
    public static void main(String []args) {
        // String f_path="C:\\Users\\USER\\eclipse-workspace\\CS253\\src\\pride-and-prejudice.txt";
        // String s_path="C:\\Users\\USER\\eclipse-workspace\\CS253\\src\\stop_words.txt";
    	String f_path = args[0];
    	String s_path = args[1];
        List<Map.Entry<String, Integer>> r=sortByValue(frequency(removeStopWords(scan(normalize(filter_chars(read_file(f_path)))), s_path)));
        for(int i=0; i<25; i++) {
        	System.out.println(r.get(i).getKey() + " - " + r.get(i).getValue());
	    }
    }
}