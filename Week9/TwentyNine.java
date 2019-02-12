import java.util.*;
import java.io.*;

public class TwentyNine {
	static Queue<Map<String, Integer>> freq_space=new LinkedList<>();
	static Queue<String> word_space=new LinkedList<>();
	static Set<String> stopwords=new HashSet<>();
	static Map<String, Integer> word_freqs=new HashMap<>();
	
    //Get stop words set.
    static void stop_words() {
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
            	stopwords.add(s);
            }
 	    }
        stopwords.add("s");
    }
    
    static void process_words() {
    	Map<String, Integer> map=new HashMap<>();
    	while(!word_space.isEmpty()) {
    		String w=word_space.poll();
    		if(!stopwords.contains(w)) {
        		if(!map.containsKey(w)) {
        			map.put(w, 0);
        		}
        		map.put(w, map.get(w)+1);
    		}
    	}
    	freq_space.offer(map);
    }
    
    static void process_freqs() {
        while(!freq_space.isEmpty()) {
        	Map<String, Integer> m=freq_space.poll();
        	for(Map.Entry<String, Integer> e : m.entrySet()) {
        		if(!word_freqs.containsKey(e.getKey())) {
        			word_freqs.put(e.getKey(), e.getValue());
        		}
        		else {
        			word_freqs.put(e.getKey(), word_freqs.get(e.getKey())+e.getValue());
        		}
        	}
        }
    }
    
    //Push all the words into word_space.
    static void read_file(String path) {
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
        
        content=content.replaceAll("[^a-zA-Z]+", " ").toLowerCase();
    	sc=new Scanner(content);
    	while(sc.hasNext()) {
    		word_space.offer(sc.next());
    	}
    	sc.close();
    }
    
    static class pw_worker extends Thread {
        public void run(){
        	process_words();
        }
    }
    
    static class pf_worker extends Thread {
        public void run(){
        	process_freqs();
        }
    }
    
    //Function to sort hashmap by values 
    static List<Map.Entry<String, Integer>> sortByValue(Map<String, Integer> hm) {
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
        read_file(args[0]);
        stop_words();
        
        List<pw_worker> pw_workers=new ArrayList<>();
        for(int i=0; i<5; i++) {
        	pw_workers.add(new pw_worker());
        }
        
        List<pf_worker> pf_workers=new ArrayList<>();
        for(int i=0; i<5; i++) {
        	pf_workers.add(new pf_worker());
        }
        
        for(pw_worker w : pw_workers){
        	w.start();
        }
        for(pw_worker w : pw_workers){
            try {
            	w.join();
            } catch(InterruptedException e){}
        }
        
        for(pf_worker w : pf_workers) {
        	w.start();
        }
        for(pf_worker w : pf_workers){
            try {
            	w.join();
            } catch(InterruptedException e){}
        }
        
        List<Map.Entry<String, Integer>> r=sortByValue(word_freqs);
        for(int i=0; i<25; i++) {
        	System.out.println(r.get(i).getKey() + " - " + r.get(i).getValue());
	    }
    }
}
