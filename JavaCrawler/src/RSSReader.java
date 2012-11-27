import java.io.*;
import java.lang.*;

public class RSSReader
{
	private InputStream    fis;
	private BufferedReader br;
	private String         line;
	private int 		   category;
	private RSSReaderDOM   reader;
	
	public RSSReader() 
	{
		reader = RSSReaderDOM.getInstance();
		category = 0;
	}
	
	public void ReadFromInput()
	{	
		try{
									// "C://Users//MikaelLaptop//RSSInput.txt" <-- eksempel
		fis = new FileInputStream("C://Users//Mikael//RSSInput.txt"); //Erstatt med ny filplassering
		br = new BufferedReader(new InputStreamReader(fis));
		
		while ((line = br.readLine()) != null) {
		    if (line.contains("--"))
		    {
		    	Character c = new Character(line.charAt(0));
		    	Character d = new Character(line.charAt(1));
		    	String s;
		    	if (!d.toString().equals("-"))
		    		s = c.toString() + d.toString();
		    	else
		    		s = c.toString();
		    	category = Integer.parseInt(s);
		    }
		    else 
		    {
		    	reader.writeNews(line,category);
		    }
		}
		}catch (FileNotFoundException e)
		{
			System.out.println("Finner ikke avlesningsfilen");
			
		}catch(IOException ie)
		{
			System.out.println(ie);
		}

	}
}
