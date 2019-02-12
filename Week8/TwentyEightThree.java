import java.util.*;
import java.io.*;

public class TwentyEightThree {
	static class ActiveWFObject extends Thread{
		Queue<String> q;
		
		ActiveWFObject(){
			q=new LinkedList<>();
		}
		
		public void run() {
			while(!q.isEmpty()) {
				func(q.poll());
			}
		}
		
		public void func(String mess) {};
	}
	
	static void send(ActiveWFObject receiver, String messasge) {
		receiver.q.offer(messasge);
	}
	
	static characters chrs=new characters();
	static all_words alds=new all_words();
	static non_stop_words nsw=new non_stop_words();
	static count_and_sort cas=new count_and_sort();
	
	static class characters extends ActiveWFObject{
		public void func(String filename) {
	    	File file=new File(filename);
	        String content=null;
	        Scanner sc=null;
	        try {
			    sc=new Scanner(file);
			    content=new Scanner(file).useDelimiter("\\Z").next();
		    }
		    catch (FileNotFoundException e) {
		        e.printStackTrace();
		    }
	        
	        for(int i=0; i<content.length(); i++) {
	        	send(alds, String.valueOf(content.charAt(i)));
	        }
		}
	}
	
	static class all_words extends ActiveWFObject{
		boolean start_char=true;
		String word;
		public void func(String mess) {
			if(start_char == true) {
				word="";
				if(Character.isLetterOrDigit(mess.charAt(0))) {
					word+=(mess.toLowerCase());
					start_char=false;
				}
			}
			else {
				if(Character.isLetterOrDigit(mess.charAt(0))) {
					word+=(mess.toLowerCase());
				}
				else {
					start_char=true;
					send(nsw, word);
				}
			}
		}
	}
	
	static class non_stop_words extends ActiveWFObject{
        public void func(String mess) {
	    	File file=new File("../stop_words.txt");
	        Scanner sc=null;
	        Set<String> set=new HashSet<>();
	        
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
            
            if(!set.contains(mess) && !mess.equals("s")) {
                send(cas, mess);
            }
        }
	}
	
	static class count_and_sort extends ActiveWFObject{
		Map<String, Integer> map=new HashMap<>();
		int i=1;
		public void func(String mess) {
			if(!map.containsKey(mess)) {
				map.put(mess, 0);
			}
			map.put(mess, map.get(mess)+1);
			
			if((i % 5000) == 0) {
				print();
			}
			i++;
		}
		
		private void print() {
			System.out.println("--------------------");
			
	        // Create a list from elements of HashMap 
	        List<Map.Entry<String, Integer> > list = new ArrayList<Map.Entry<String, Integer> >(map.entrySet()); 
	  
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
		send(chrs, args[0]);
		
		chrs.start();
        try {
        	chrs.join();
        } catch(InterruptedException e){}
        alds.start();
        try {
        	alds.join();
        } catch(InterruptedException e){}
        
        nsw.start();
        try {
        	nsw.join();
        } catch(InterruptedException e){}
        cas.start();
        try {
        	cas.join();
        } catch(InterruptedException e){}
        
        cas.print();
	}
}
