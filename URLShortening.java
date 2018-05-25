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
public class URLShortening extends HttpServlet {
	public static Connection connection = null;

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
	String UrlQuery = request.getQueryString();
	String CheckString = url==null?"ERROR":UrlQuery.substring(0,4); // to avoid some bad word in user url
	boolean flag = false; // default no url
	if(CheckString.equals("url=")){
		flag = true;
		url = UrlQuery.substring(4);
	}

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
			if(surl.equals("init")){ // db have no this url
				surl = getSaltString();
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
			}
			response.getWriter().println("http://140.92.143.82:8787/"+surl);
		}
		else{ // no url param, have to redirect
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:/tmp/surl.db");
                        Statement statement = null;
                        statement = connection.createStatement();
                        String SelectSQL = "SELECT url FROM UrlMapping WHERE surl = '"+input.substring(1)+"';"; // substring to remove slash
                        ResultSet rs = statement.executeQuery(SelectSQL);
			String output = "init";
			while(rs.next()){
                                output = rs.getString("url");
                        }
			response.setStatus(HttpServletResponse.SC_FOUND);//302
			response.setHeader("Location", output);
		}
	}catch(Exception e){}
   }

}
