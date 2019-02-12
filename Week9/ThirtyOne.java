import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.*; 

public class ThirtyOne {
	static class Tuple{
		String str;
		int freq;
		Tuple(String s, int f){
			str=s;
			freq=f;
		}
	}
	
	static List<String> partition(String data_str, int nlines) {
		String[] lines=data_str.split("\n");
		List<String> res=new ArrayList<>();
		for(int i=0; i<lines.length; i+=nlines) {
			String tmp="";
			for(int j=i; j<i+nlines && j<lines.length; j++) {
				tmp+=lines[j];
			}
			res.add(tmp);
		}
		return res;
	}
	
	static class split_words {
		
		static List<Tuple> run(String data_str){
			List<Tuple> res=new ArrayList<>();
			List<String> words=_remove_stop_words(_scan(data_str));
			for(String w : words) {
				res.add(new Tuple(w, 1));
			}
			return res;
		}
		
		static List<String> _scan(String data_str) {
			List<String> words=new ArrayList<>();
			Scanner scan=new Scanner(data_str);
	    	while(scan.hasNext()) {
	    		words.add(scan.next());
	    	}
	    	scan.close();
	    	return words;
		}
		
		static List<String> _remove_stop_words(List<String> word_list){
			Set<String> stop_words=_get_stop_words();
			List<String> output=new ArrayList<>();
			for(String w : word_list) {
				if(!stop_words.contains(w)) {
					output.add(w);
				}
			}
			return output;
		}
		
		static Set<String> _get_stop_words(){
			File stop_file = new File("../stop_words.txt");
	    	Set<String> set=new HashSet<>();
	        Scanner sc=null;
	        try {
	 		    sc=new Scanner(stop_file);
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
	        set.add("s");
	        return set;
		}
	}
	
	static Map<Set<Character>, List<Tuple>> regroup(List<List<Tuple>> pair_list) {
		Map<Set<Character>, List<Tuple>> map=new HashMap<>();
		map.put(new HashSet<Character>(Arrays.asList('a', 'b', 'c', 'd','e')), new ArrayList<>());
		map.put(new HashSet<Character>(Arrays.asList('f', 'g', 'h', 'i','j')), new ArrayList<>());
		map.put(new HashSet<Character>(Arrays.asList('k', 'l', 'm', 'n','o')), new ArrayList<>());
		map.put(new HashSet<Character>(Arrays.asList('p', 'q', 'r', 's','t')), new ArrayList<>());
		map.put(new HashSet<Character>(Arrays.asList('u', 'v', 'w', 'x', 'y', 'z')), new ArrayList<>());
		for(List<Tuple> pairs : pair_list) {
			for(Tuple p : pairs) {
				for(Set<Character> set : map.keySet()) {
					if(set.contains(p.str.charAt(0))) {
						map.get(set).add(p);
					}
				}
			}
		}
		return map;
	}
	
	
    static Map<String, Integer> count_words(List<Tuple> map) {    	
    	Map<String, Integer> words_freqs=new HashMap<>();
    	for(Tuple tp : map) {
    		if(!words_freqs.containsKey(tp.str)) {
    			words_freqs.put(tp.str, 0);
    		}
    		words_freqs.put(tp.str, words_freqs.get(tp.str)+1);
    	}
    	return words_freqs;
    }

    static List<Map.Entry<String, Integer>> sortByValue(List<Map<String, Integer>> list_map){
    	Map<String, Integer> map=new HashMap<>();
    	for(Map<String, Integer> m : list_map) map.putAll(m);
    	
        // Create a list from elements of HashMap 
    	List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer> >(map.entrySet());
  
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
        sc.close();
        content=content.replaceAll("[^a-zA-Z]+", " ").toLowerCase();
        return content;
    }
    
    public static void main(String[] args) {
    	List<List<Tuple>> splits=new ArrayList<>();
    	
    	List<String> part=partition(read_file(args[0]), 200);
    	splits=part.stream().map(x -> split_words.run(x)).collect(Collectors.toList());
    	
    	//Reorganizes words alphabetically into five groups.
    	Map<Set<Character>, List<Tuple>> splits_per_word=regroup(splits);
    	
    	//Count the frequency of the words.
    	List<Map<String, Integer>> map_list=new ArrayList<>();
    	map_list=splits_per_word.values().stream().map(x -> count_words(x)).collect(Collectors.toList());
    	
    	List<Map.Entry<String, Integer>> r=sortByValue(map_list);
    	for(int i=0; i<25; i++) {
        	System.out.println(r.get(i).getKey() + " - " + r.get(i).getValue());
	    }
    }
}
