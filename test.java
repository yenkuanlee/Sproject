// Import required java libraries
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.JSONObject;
import org.json.JSONArray;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Random;
import java.util.HashSet;

// Extend HttpServlet class
public class test extends HttpServlet {
 
   //private String message;
   public static Connection connection = null;

   //public void init() throws ServletException {
      // Do required initialization
   //   message = "Hello World";
   //}

  public static String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 8) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }


   public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
	String input = request.getPathInfo() ;
	String url = request.getParameter("url");
	boolean flag = url==null?false:true;

	JSONObject result = new JSONObject();
	try{
		if(flag&&input.equals("/api-create")){
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:/tmp/surl.db");
			Statement statement = null;
			statement = connection.createStatement();
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS UrlMapping(url text, surl text, PRIMARY KEY(url));");
			String SelectSQL = "SELECT surl FROM UrlMapping WHERE url='"+url+"';";
			ResultSet rs = statement.executeQuery(SelectSQL);
			String surl = "init";
			while(rs.next()){
				surl = rs.getString("surl");
			}
			//result.put("result",url);
			//result.write(response.getWriter());
			if(surl.equals("init")){ // db have no this url
				surl = getSaltString();
			}
			HashSet<String> SurlSet = new HashSet<String>();
			SelectSQL = "SELECT surl FROM UrlMapping;";
			rs = statement.executeQuery(SelectSQL);
			while(rs.next()){
				SurlSet.add(rs.getString("surl"));
			}
			while(true){
				if(SurlSet.contains(surl)){
					surl = getSaltString();
				}
				else
					break;
			}
			statement.executeUpdate("INSERT INTO UrlMapping VALUES('"+url+"','"+surl+"');");
			
			response.getWriter().println(input);
			response.getWriter().println(url);
			response.getWriter().println(surl);
		}
		else{
			response.setStatus(HttpServletResponse.SC_FOUND);//302
			response.setHeader("Location", "http://google.com");
		}
	}catch(Exception e){}
   }

}
