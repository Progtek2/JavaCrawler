
public class main {

	/*
	 * 	Code done by Gruppe2 
	 *  Programmeringsteknikk 2
	 * 	2012 @ HIN
	 */
	
	
	public static void main(String[] args) 
	{
		
		long startTime = System.currentTimeMillis();
		
		//RSSReaderDOM reader = RSSReaderDOM.getInstance();
		RSSReader leser = new RSSReader();
		
		leser.ReadFromInput();
		
		terminator arnold = new terminator();
		arnold.terminate();
		
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		
		System.out.println("\n----------------------------------\nTime used: " + totalTime / 1000 + " sec");
	    
	    
	}

}
