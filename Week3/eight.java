import java.util.*;
import java.io.*;

public class eight {
    //Read the whole file into string.
    static List<Map.Entry<String, Integer>> read_file(String data, Funcs f) {
    	return filter_chars(f.readFile(data), f);
    }
    
    //Repalce nonalphanumeric characters with white space. 
    static List<Map.Entry<String, Integer>> filter_chars(String data, Funcs f) {
    	return normalize(f.filter_chars(data), f);
    }
    
    //Change all the characters into lowercase.
    static List<Map.Entry<String, Integer>> normalize(String data, Funcs f) {
    	return scan(f.normalize(data), f);
    }
    
    //Add all the words from the string to the list.
    static List<Map.Entry<String, Integer>> scan(String data, Funcs f){
    	return removeStopWords(f.scan(data), f);
    }
    
    //Remove the stop words in the stop.txt file.
    static List<Map.Entry<String, Integer>> removeStopWords(List<String> words, Funcs f){
    	return frequency(f.removeStopWords(words), f);
    }
    
    //Using Hash Map to record the frequency of each words.
    static List<Map.Entry<String, Integer>> frequency(List<String> words, Funcs f){
    	return sortByValue(f.frequency(words), f);
    }
    
    //Function to sort hashmap by values 
    static List<Map.Entry<String, Integer>> sortByValue(Map<String, Integer> hm, Funcs f) {
        return f.sortByValue(hm);
    }
    
    static class Funcs{
    	String readFile(String path) {
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
    	
    	String filter_chars(String str_data) {
        	str_data=str_data.replaceAll("[^a-zA-Z]+", " ");
        	return str_data;
        }
    	
    	String normalize(String str_data) {
        	return str_data.toLowerCase();
        }
    	
        List<String> scan(String str_data){
        	List<String> words=new ArrayList<>();
        	Scanner scan=new Scanner(str_data);
        	while(scan.hasNext()) {
        		words.add(scan.next());
        	}
        	return words;
        }
        
        Set<String> stop_words(File file) {
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
        
        List<String> removeStopWords(List<String> words){
        	File stop_file = new File("../stop_words.txt");
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
        
        Map<String, Integer> frequency(List<String> words){
        	Map<String, Integer> map=new HashMap<>();
        	for(String w : words) {
        		if(!map.containsKey(w)) {
        			map.put(w, 0);
        		}
        		map.put(w, map.get(w)+1);
        	}
        	return map;
        }
        
        List<Map.Entry<String, Integer>> sortByValue(Map<String, Integer> hm) 
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
    }
    
    public static void main(String []args) {
        List<Map.Entry<String, Integer>> r=read_file(args[0], new Funcs());
        for(int i=0; i<25; i++) {
        	System.out.println(r.get(i).getKey() + " - " + r.get(i).getValue());
	    }
    }
}