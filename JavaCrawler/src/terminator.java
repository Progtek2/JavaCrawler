import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;


public class terminator 
{
	private static String dburl = "jdbc:mysql://sandbox.kiiw.org:3306/gruppe2";
	
	public terminator()
	{
		
	}
	
	public void terminate()
	{
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
            
    		java.util.Date today = new java.util.Date();
			Timestamp time =  new java.sql.Timestamp(today.getTime());
			System.err.println("\n---------------------------------------\nChecking for old news to terminate with Arnold.\n");
			
    		while(result.next())
    			{	
    				
    				long newtime = time.getTime() - 86400000;
    				long dbtime = result.getTimestamp("publish_date").getTime();
    				String id = result.getString("id");
    				String pubDate = result.getString("publish_date");
    				
    				if(newtime > dbtime)
    				{
    					String sql_delete_cat = "DELETE from news_category where news_id = '" + id +"'";
    					String sql_delete = "DELETE from news where id = '" + id +"'";
    					
    					PreparedStatement delete_cat_q = conn.prepareStatement(sql_delete_cat);
    					delete_cat_q.executeUpdate();
    					    					
    					PreparedStatement query2 = conn.prepareStatement(sql_delete);
    					query2.executeUpdate();
    					System.out.println("Astalavista id " + id + " with Pubdate: " + pubDate);
    				}
    			}
    		System.err.println("\nOLDER NEWS THEN 24 HOURS TERMINATED!! - I'LL BE BACK!\n");
    		
    		
        }catch (SQLException e)
        {
        	System.out.println(e);
        }
	}

}

