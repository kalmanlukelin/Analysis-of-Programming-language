import java.util.*;
import java.io.*;

public class Eleven {
	static String _data="";
	
	static class DataStorageManager {
		List<String> dispatch(String[] message) {
			if(message[0].equals("init")) {
				init(message[1]);
				return null;
			}
			else if(message[0].equals("words")) {
				return _words();
			}
			else {
				throw new java.lang.Error("Message not understood " + message[0]);
			}
		}
		
		void init(String path_to_file) {
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
	        _data=content.toLowerCase();
		}
		
		List<String> _words(){
			List<String> words=new ArrayList<>();
			Scanner sc=new Scanner(_data);
			while(sc.hasNext()) {
	        	words.add(sc.next());
	        }
			return words;
		}
	}
	
	static class StopWordManager {
		Set<String> _stop_words=new HashSet<>();
		boolean dispatch(String[] message) {
			if(message[0].equals("init")) {
				init();
				return false;
			}
			else if(message[0].equals("is_stop_word")) {
				return _is_stop_word(message[1]);
			}
			else {
				throw new java.lang.Error("Message not understood " + message[0]);
			}
		}
		
		void init() {
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
	 	   _stop_words.add("s");
		}
		
		boolean _is_stop_word(String word) {
			return _stop_words.contains(word);
		}
	}
	
	static class WordFrequencyManager{
		Map<String, Integer> _word_freqs=new HashMap<>();
		
		List<Map.Entry<String, Integer>> dispatch(String[] message){
			if(message[0].equals("increment_count")) {
				_increment_count(message[1]);
				return null;
			}
			else if(message[0].equals("sorted")) {
				return _sorted();
			}
			else {
				throw new java.lang.Error("Message not understood " + message[0]);
			}
		}
		
		void _increment_count(String word) {
			if(_word_freqs.containsKey(word)) {
				_word_freqs.put(word, _word_freqs.get(word)+1);
			}
			else {
				_word_freqs.put(word, 1);
			}
		}
		
		List<Map.Entry<String, Integer>> _sorted(){
			// Create a list from elements of HashMap 
	        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer> >(_word_freqs.entrySet()); 
	  
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
	
	static class WordFrequencyController{
		DataStorageManager _storage_manager;
		StopWordManager _stop_word_manager;
		WordFrequencyManager _word_freq_manager;
		
		void dispatch(String[] message){
			if(message[0].equals("init")) {
				init(message[1]);
			}
			else if(message[0].equals("run")) {
				run();
			}
			else {
				throw new java.lang.Error("Message not understood " + message[0]);
			}
		}
		
		void init(String path_to_file) {
			_storage_manager=new DataStorageManager();
			_stop_word_manager=new StopWordManager();
			_word_freq_manager=new WordFrequencyManager();
			_storage_manager.dispatch(new String[]{"init", path_to_file});
			_stop_word_manager.dispatch(new String[]{"init"});
		}
		
		void run(){
			for(String w : _storage_manager.dispatch(new String[]{"words"})) {
				if(!_stop_word_manager.dispatch(new String[]{"is_stop_word", w})) {
					_word_freq_manager.dispatch(new String[]{"increment_count", w});
				}
			}
			List<Map.Entry<String, Integer>> word_freqs=_word_freq_manager.dispatch(new String[]{"sorted"});
	        for(int i=0; i<25; i++) {
	        	System.out.println(word_freqs.get(i).getKey() + " - " + word_freqs.get(i).getValue());
		    }
		}
	}
	
	public static void main(String[] args) {
		WordFrequencyController wfcontroller = new WordFrequencyController();
		wfcontroller.dispatch(new String[]{"init", args[0]});
		wfcontroller.dispatch(new String[]{"run"});
	}
}
