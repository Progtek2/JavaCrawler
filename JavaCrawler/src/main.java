
public class main {

	/*
	 * 	Code done by Gruppe2 
	 *  Programmeringsteknikk 2
	 * 	2012 @ HIN
	 */
	
	
	public static void main(String[] args) 
	{
		//RSSReaderDOM reader = RSSReaderDOM.getInstance();
		RSSReader leser = new RSSReader();
		
		leser.ReadFromInput();
		
		terminator arnold = new terminator();
		arnold.terminate();
		
	    
	    
	}

}
