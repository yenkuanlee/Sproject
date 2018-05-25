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

// Extend HttpServlet class
public class test extends HttpServlet {
 
   //private String message;
   public static Connection connection = null;

   //public void init() throws ServletException {
      // Do required initialization
   //   message = "Hello World";
   //}


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
			String SelectSQL = "select * from UrlMapping;";
			ResultSet rs = statement.executeQuery(SelectSQL);
			while(rs.next()){
				response.getWriter().print(rs.getString("url"));
			}
			//result.put("result",url);
			//result.write(response.getWriter());
			response.getWriter().print(url);
		}
		else{
			response.setStatus(HttpServletResponse.SC_FOUND);//302
			response.setHeader("Location", "http://google.com");
		}
	}catch(Exception e){}
   }

}
