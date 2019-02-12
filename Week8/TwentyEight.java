import java.util.*;
import java.io.*;

public class TwentyEight {
	static class ActiveWFObject extends Thread{
		Queue<String[]> q;
		
		ActiveWFObject(){
			q=new LinkedList<>();
		}
		
		public void run() {
			while(!q.isEmpty()) {
				dispatch(q.poll());
			}
		}
		
		public void dispatch(String[] message) {};
	}
	
	static void send(ActiveWFObject receiver, String[] messasge) {
		receiver.q.offer(messasge);
	}
	
	static DataStorageManager _storage_manager=new DataStorageManager();
	static StopWordManager _stop_word_manager=new StopWordManager();
	static WordFrequencyManager _word_freq_manager=new WordFrequencyManager();
	
	static class DataStorageManager extends ActiveWFObject{
		static String _data="";
		
		public void dispatch(String[] message) {
			if(message[0].equals("init")) {
				init(message[1]);
			}
			else if(message[0].equals("send_word_freqs")) {
				_process_words();
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
	        
	        //Replace nonalphanumeric characters with white space.
	        content=content.replaceAll("[^a-zA-Z]+", " ");
	        
	        //Change all the characters into lowercase.
	        _data=content.toLowerCase();
		}
		
		void _process_words(){
			List<String> words=new ArrayList<>();
			Scanner sc=new Scanner(_data);
			while(sc.hasNext()) {
	        	send(_stop_word_manager, new String[]{"filter", sc.next()});
	        }
			send(_stop_word_manager, new String[]{"top25"});
		}
	}
	
	static class StopWordManager extends ActiveWFObject {
		Set<String> _stop_words=new HashSet<>();
		
		public void dispatch(String[] message) {
			if(message[0].equals("init")) {
				init();
			}
			else if(message[0].equals("filter")) {
				filter(message[1]);
			}
			else if(message[0].equals("top25")) {
				send(_word_freq_manager, new String[]{"top25"});
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
		
		public void filter(String word) {
			if(!_stop_words.contains(word)) {
				send(_word_freq_manager, new String[]{"word", word});
			}
			
		}
	}
	
	static class WordFrequencyManager extends ActiveWFObject{
		Map<String, Integer> _word_freqs=new HashMap<>();
		
		public void dispatch(String[] message){
			if(message[0].equals("word")) {
				_increment_count(message[1]);
			}
			else if(message[0].equals("top25")) {
				top25();
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
		
		void top25(){
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
	        
	        //print the result.
	        for(int i=0; i<25; i++) {
	        	System.out.println(list.get(i).getKey() + " - " + list.get(i).getValue());
		    }
		}
	}
	
	public static void main(String[] args) {
		send(_storage_manager, new String[]{"init", args[0]});
		send(_stop_word_manager, new String[]{"init"});
		send(_storage_manager, new String[]{"send_word_freqs"});
		
		_storage_manager.start();
        try {
            _storage_manager.join();
        } catch(InterruptedException e){}
        _stop_word_manager.start();
        try {
        	_stop_word_manager.join();
        } catch(InterruptedException e){}
        _word_freq_manager.start();
        try {
        	_word_freq_manager.join();
        } catch(InterruptedException e){}
	}
}

