import java.util.*;
import java.io.*;

public class TwentyFour {
    //Read the whole file into string.
	static class extract_words {
	    List<String> func(String path) {
	    	File file = new File(path);
	        String content=null;
	        Scanner sc=null;
	        List<String> words=new ArrayList<>();
	        
	        try {
			    sc=new Scanner(file);
			    content=new Scanner(file).useDelimiter("\\Z").next();
		    }
		    catch (FileNotFoundException e) {
		        e.printStackTrace();
		    }
	        
	        //Repalce nonalphanumeric characters with white space.
	        content=content.replaceAll("[^a-zA-Z]+", " ");
	        
	        //Change all the characters into lowercase.
	        content=content.toLowerCase();
	        
	        //Add all the words from the string to the list.
	    	Scanner scan=new Scanner(content);
	    	while(scan.hasNext()) {
	    		words.add(scan.next());
	    	}
	    	return words;
	    }	
	}
    
	static class remove_stop_words{
		List<String> func(List<String> words){
	    	Set<String> s_word=new HashSet<>();
	    	File file = new File("../stop_words.txt");
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
	            	s_word.add(s);
	            }
	 	    }
	        
	        //Store the words except for stop words.
	    	List<String> c_word=new ArrayList<>();
	    	for(int i=0; i<words.size(); i++) {
	    		if(!s_word.contains(words.get(i)) && !words.get(i).equals("s")) {
	    			c_word.add(words.get(i));
	    		}
	    	}
	    	return c_word;
		}
	}
    
    //Use Hash Map to record the frequency of each words.
	static class frequency{
		Map<String, Integer> func(List<String> words){
			Map<String, Integer> map=new HashMap<>();
	    	for(String w : words) {
	    		if(!map.containsKey(w)) {
	    			map.put(w, 0);
	    		}
	    		map.put(w, map.get(w)+1);
	    	}
	    	return map;
		}
	}
    
	static class sortByValue {
		List<Map.Entry<String, Integer>> func(Map<String, Integer> hm){
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
	
	static class top25_freqs{
		void func(List<Map.Entry<String, Integer>> word_freqs) {
			for(int i=0; i<25; i++) {
	        	System.out.println(word_freqs.get(i).getKey() + " - " + word_freqs.get(i).getValue());
		    }
		}
	}
    
    static class TFQuarantine{
    	extract_words ew_func;
    	remove_stop_words rsw;
    	frequency fq;
    	sortByValue sbv;
    	top25_freqs t25;
    	TFQuarantine add_ew(extract_words f){
    		ew_func=f;
    		return this;
    	}
    	TFQuarantine add_rsw(remove_stop_words f) {
    		rsw=f;
    		return this;
    	}
    	TFQuarantine add_freq(frequency f) {
    		fq=f;
    		return this;
    	}
    	TFQuarantine add_sbv(sortByValue f) {
    		sbv=f;
    		return this;
    	}
    	TFQuarantine add_t25(top25_freqs f) {
    		t25=f;
    		return this;
    	}
    	void execute(String path) {
    		t25.func(sbv.func(fq.func(rsw.func(ew_func.func(path)))));
    	}
    }
    
    public static void main(String []args) {
        TFQuarantine TFQ=new TFQuarantine();
        TFQ.add_ew(new extract_words()).add_freq(new frequency()).add_rsw(new remove_stop_words()).add_sbv(new sortByValue()).add_t25(new top25_freqs());
        TFQ.execute(args[0]);
    }
}