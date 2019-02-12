import java.util.*;
import java.io.*;

public class Twelve {
	static void extract_words(String path_to_file, List<String> data) {
		//Read the whole file into string.
		File file = new File(path_to_file);
        String content=null;
        Scanner sc=null;
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
        
        //Put all the strings to the list.
		//List<String> words=new ArrayList<>();
		sc=new Scanner(content);
		while(sc.hasNext()) {
			String d=sc.next();
			if(!d.equals("s")) {
				data.add(d);
			}
        }
	} 
	
    static void load_stop_words(Set<String> _stop_words){
        Scanner sc=null;
        File file=new File("../stop_words.txt");
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
            	_stop_words.add(s);
            }
 	   }
	}
    
    static void increment_count(Map<String, Integer> freqs, String w) {
    	if(!freqs.containsKey(w)) {
    		freqs.put(w, 1);
    	}
    	else {
    		freqs.put(w, freqs.get(w)+1);
    	}
    }
    
	static class data_storage_obj{
		List<String> data=new ArrayList<>();
		private void init(String path_to_file) {
			extract_words(path_to_file, data);
		}
		private List<String> words(){
			return data;
		}
	}
	
	static class stop_words_obj{
		Set<String> stop_words=new HashSet<>();
		private void init() {
			load_stop_words(stop_words);
		}
		boolean is_stop_word(String word) {
			return stop_words.contains(word);
		}
	}
	
	static class word_freqs_obj{
		Map<String, Integer> freqs=new HashMap<>();
		private void incre_cnt(String w) {
			increment_count(freqs, w);
		}
		private List<Map.Entry<String, Integer>> sorted() {
			List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(freqs.entrySet()); 
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
		private void top25() {
			List<Map.Entry<String, Integer>> word_freqs=sorted();
			for(int i=0; i<25; i++) {
	        	System.out.println(word_freqs.get(i).getKey() + " - " + word_freqs.get(i).getValue());
		    }
		}
	}
	
	public static void main(String[] args) {
		data_storage_obj dso=new data_storage_obj();
		stop_words_obj swo=new stop_words_obj();
		word_freqs_obj wfs=new word_freqs_obj();
		dso.init(args[0]);
		swo.init();
		for(String w : dso.words()) {
			if(!swo.is_stop_word(w)) {
				wfs.incre_cnt(w);
			}
		}
		wfs.top25();
	}
}
