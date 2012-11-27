import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class RSSReaderDOM {

  private static String dburl = "jdbc:mysql://sandbox.kiiw.org:3306/gruppe2";
  private static RSSReaderDOM instance = null;
  private static String category;
  private static String last_id;
  
  private RSSReaderDOM() {
  }

  
  public static RSSReaderDOM getInstance() {
    if (instance == null) {
      instance = new RSSReaderDOM();
    }
    return instance;
  }

  
  public void writeNews(String url, int category) {
    try {

      // Lager dokumentet som skal parses
      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      URL u = new URL(url);
      Document doc = builder.parse(u.openStream());

      // Henter ut lista over "overordnete" items
      NodeList nodes = doc.getElementsByTagName("item");
      
      
      
      // og traverserer lista for å hente ut informasjon fra RSS-feeden
      for (int i = 0; i < nodes.getLength(); i++) {

        Element element = (Element) nodes.item(i);
        
        String tittle = getElementValue(element, "title");
        System.out.println("Tittel:" + tittle);
        
        System.out.println("Link:" + getElementValue(element, "link"));
        String link = getElementValue(element, "link");
        
        System.out.println("Publiseringsdato:" + getElementValue(element, "pubDate"));
        
        String pubDate = getElementValue(element, "pubDate");
       
        System.out.println("Forfatter:" + getElementValue(element, "dc:creator"));
        String creator = getElementValue(element, "dc:creator");
        
        System.out.println("Kommentarer:" + getElementValue(element, "wfw:comment"));
        String comment = getElementValue(element, "wfw:comment");
        
        System.out.println("Ingress:" + getElementValue(element, "description"));
        String desc = getElementValue(element, "description");
        
        
        try{
	        Class.forName("com.mysql.jdbc.Driver").newInstance();
	        }
	        catch (IllegalAccessException e)
	        {System.out.println(e);}
			catch(InstantiationException e)
	        {System.out.println(e);}
	        catch(ClassNotFoundException e)
	        {System.out.println(e);}
		
		try
        { 

            Connection conn = DriverManager.getConnection(dburl,"gruppe2","gruppe2"); 
            
            String sql_Q ="SELECT * FROM news";
    		PreparedStatement query = conn.prepareStatement(sql_Q);
    		ResultSet result = query.executeQuery();
            Boolean isUnique = true;
    		
    		while(result.next())
    		{
    			if(tittle.equals(result.getString("title")))
       			{
       				isUnique = false;
        		}
    			
    		}
    		
    		if(isUnique)
            {
    			java.util.Date today_is = new java.util.Date();
    			Timestamp time_is =  new java.sql.Timestamp(today_is.getTime());
    			long newtime = time_is.getTime() - 86400000;
    			long pubtime = this.DateTime(pubDate).getTime();
    			
    			if(newtime > pubtime)
    			{
    				System.err.println("\nToo old for the datebase.\n");
    			}
    			
    			else 
    			{
    				PreparedStatement ps = conn.prepareStatement("INSERT INTO news (title, description, link, author, publish_date, guid)VALUES (?, ?, ?, ?, ?, ?)");
    				ps.setString(1, tittle);
    				ps.setString(2, desc);
    				ps.setString(3, link);
    				ps.setString(4, creator);
    				ps.setTimestamp(5, this.DateTime(pubDate));
    				ps.setString(6, link);
    				ps.executeUpdate();
            	
    				String sql_Q2 ="SELECT id FROM news";
    				PreparedStatement query2 = conn.prepareStatement(sql_Q2);
    				ResultSet result2 = query2.executeQuery();
        		
    				while(result2.next())
    				{
         			
    					last_id = result2.getString("id");
         			
    				}
    				System.out.println("Add`s ID " + last_id + " to category " + category );
        		
    				PreparedStatement ps2 = conn.prepareStatement("INSERT INTO news_category (news_id, category_id)VALUES (?, ?)");
    				ps2.setString(1, last_id);
    				ps2.setInt(2, category);
    				ps2.executeUpdate();
            	
        		
            	
    				System.err.println("\nUpload to MYSql done...\n");
    			}
            }
            else
            {
            	System.err.println("\nExists in MYSql..\n");
            }
            conn.close(); 

        } catch (Exception e) { 
            System.err.println("Got an exception! "); 
            System.err.println(e.getMessage()); 
        }
        
      }//for
    }//try
    catch (Exception ex) {
      doErrorMessage(ex);
    }
  }
                          
  protected String getElementValue(Element parent, String label) {
    return getCharacterDataFromElement((Element) parent.getElementsByTagName(label).item(0));
  }

  
  private String getCharacterDataFromElement(Element e) {
    try {
      Node child = e.getFirstChild();
      if (child instanceof CharacterData) {
        CharacterData cd = (CharacterData) child;
        return cd.getData();
      }
    }
    
    catch (Exception ex) {
      
      if (!(ex.getClass().equals(java.lang.NullPointerException.class)))
        doErrorMessage(ex);
    }
    return "";
  }

 
  private void doErrorMessage(Exception e) {
    System.out.println("========================================================");
    System.out.println("= ClassName: " + this.getClass().getName());
    System.out.println("= Exception: " + e.getClass().getName());
    System.out.println(
        "= -----------------------------------------------------");
    System.out.println("= Message  : " + e.getMessage());
    System.out.println("= Cause    : " + e.getClass());
    System.out.println("========================================================");
    System.out.println("= StackTrace:");
    e.printStackTrace(System.out);
  }


  public Timestamp DateTime(String input)
	{
		java.util.Date inputDate = null;
		SimpleDateFormat inputFormat =
		    new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
		try{
		inputDate = inputFormat.parse(input);
		}
		catch (ParseException e){

			 java.util.Date today = new java.util.Date();
			 return new java.sql.Timestamp(today.getTime());
			 
			}
		Timestamp date = new Timestamp(inputDate.getTime());
		return date;
	}
  
}
