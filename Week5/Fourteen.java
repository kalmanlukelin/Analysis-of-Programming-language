import java.util.*;
import java.io.*;

public class Fourteen {
	static class load_words{
		File file;
		load_words(String path_to_file){
			file=new File(path_to_file);
		}
		
		String load_func() {
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
	}
	
	static class load_stop_words{
		File file;
		load_stop_words(String path_to_file){
			file=new File(path_to_file);
		}
		
		Set<String> load_func() {
			Set<String> stop_words_set=new HashSet<>();
			
			//Read stop file.
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
	            	stop_words_set.add(s);
	            }
	 	    }
	        return stop_words_set;
		}
	}
	
	static class produce_words{
		List<String> produce_words_func(String words_strs){
			List<String> words=new ArrayList<>();
			
	        //Repalce nonalphanumeric characters with white space.
			words_strs=words_strs.replaceAll("[^a-zA-Z]+", " ");
	        
	        //Change all the characters into lowercase.
			words_strs=words_strs.toLowerCase();
	        
	        Scanner sc=new Scanner(words_strs);
			while(sc.hasNext()) {
	        	words.add(sc.next());
	        }
			return words;
		}
	}
	
	static class end_event{
		List<Map.Entry<String, Integer>> end_event_func(List<String> words, Set<String> stop_words) {
			Map<String, Integer> _word_freqs=new HashMap<>();
			
			//Store words frequency in hash map.
			for(String w : words) {
				if(!stop_words.contains(w) && !w.equals("s")) {
					if(_word_freqs.containsKey(w)) {
						_word_freqs.put(w, _word_freqs.get(w)+1);
					}
					else {
						_word_freqs.put(w, 1);
					}
				}
			}
			
			// Create a list from elements of HashMap 
	        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(_word_freqs.entrySet()); 
	  
	        // Sort the list 
	        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() { 
	            public int compare(Map.Entry<String, Integer> o1,  
	                               Map.Entry<String, Integer> o2) 
	            { 
	                return (o2.getValue()).compareTo(o1.getValue()); 
	            } 
	        });
	        /*
	        for(int i=0; i<25; i++) {
	        	System.out.println(list.get(i).getKey() + " - " + list.get(i).getValue());
		    }*/
	        return list;
		}
	}
	
	static class end_z_event{
		int end_z_event_func(List<Map.Entry<String, Integer>> list){
			List<Map.Entry<String, Integer>> z_list;
			int cnt=0;
			for(Map.Entry<String, Integer> me : list) {
				if(me.getKey().indexOf('z') != -1) {
				    cnt+=me.getValue();	
				}
			}
			return cnt;
		}
	}
	
	static class WordFrequecyFramework{
		List<load_words> load_words_handlers=new ArrayList<>();
		List<load_stop_words> load_stop_words_handlers=new ArrayList<>();
		List<produce_words> produce_words_handlers=new ArrayList<>();
		List<end_event> end_event_handlers=new ArrayList<>();
		List<end_z_event> end_z_event_handlers=new ArrayList<>();
		
		void register_for_load_words(load_words handler) {
			load_words_handlers.add(handler);
		}
		
		void register_for_load_stop_words(load_stop_words handler) {
			load_stop_words_handlers.add(handler);
		}
		
		void register_for_produce_words(produce_words handler) {
			produce_words_handlers.add(handler);
		}
		
		void register_for_end_event(end_event handler) {
			end_event_handlers.add(handler);
		}
		
		void register_for_z_end_event(end_z_event handler) {
			end_z_event_handlers.add(handler);
		}
		
		void run() {
			String words=null;
			Set<String> stop_words=null;
			List<String> words_list=null;
			List<Map.Entry<String, Integer>> list=null;
			int z_cnt=0;
			
			for(load_words lw : load_words_handlers) {
				words=lw.load_func();
			}
			for(load_stop_words lsw : load_stop_words_handlers) {
				stop_words=lsw.load_func();
			}
			for(produce_words pw : produce_words_handlers) {
				words_list=pw.produce_words_func(words);
			}
			for(end_event ee : end_event_handlers) {
				list=ee.end_event_func(words_list, stop_words);
			}
			for(end_z_event eze : end_z_event_handlers) {
				z_cnt=eze.end_z_event_func(list);
			}
			
	        for(int i=0; i<25; i++) {
	        	System.out.println(list.get(i).getKey() + " - " + list.get(i).getValue());
		    }
	        
	        System.out.println("words with z - "+z_cnt);
	        
		}
	}
	
	static class DataStorageManager {
		void init(String path_to_file, WordFrequecyFramework wf) {
			load_words le=new load_words(path_to_file);
			produce_words pw=new produce_words();
			wf.register_for_load_words(le);
			wf.register_for_produce_words(pw);
		}
	}
	
	static class StopWordManager {
		void init(WordFrequecyFramework wf) {
			load_stop_words lsw=new load_stop_words("../stop_words.txt");
			wf.register_for_load_stop_words(lsw);
		}
	}
	
	static class WordFrequencyManager{
		void init(WordFrequecyFramework wf) {
			end_event ee=new end_event();
			wf.register_for_end_event(ee);
		}
	}
	
	static class Z_Word_Manager{
		void init(WordFrequecyFramework wf) {
			end_z_event eze=new end_z_event();
			wf.register_for_z_end_event(eze);
		}
	}
	
	public static void main(String[] args) {
		WordFrequecyFramework wf=new WordFrequecyFramework();
		DataStorageManager DSM=new DataStorageManager();
		DSM.init(args[0], wf);
		StopWordManager SWM=new StopWordManager();
		SWM.init(wf);
		WordFrequencyManager WFM=new WordFrequencyManager();
		WFM.init(wf);
		Z_Word_Manager ZWM=new Z_Word_Manager();
		ZWM.init(wf);
		wf.run();
	}
}
