package gui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.json.simple.parser.JSONParser;

public class AttributePanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public AttributePanel() {

	}
	public List<String> getAttribute(List<String> maincat,List<String> subcat) throws SQLException
	{	System.out.println("entered in getattribute");
		JSONParser parser = new JSONParser();
		List<String> res=new ArrayList<String>();
		Connection con = null;
		String insertTableSQL="";
		if(subcat.isEmpty())
		return new ArrayList<String>();

	try {
		con = getDBConnection();
		if(HW3.decide.equals("ANY"))
		{
		
		insertTableSQL = "SELECT DISTINCT ATTRIBUTESS FROM ATTRIBUTEMAP a,BUSINESSCAT_MAP b WHERE b.MAINCAT IN('";
		Boolean isFirst=false;
		Boolean isSecond=false;
		int i=0;
		for(String s:maincat)
		{
			
			if(isFirst)
			{
				insertTableSQL=insertTableSQL+",'"+s+"'";
				i=1;
			}
			else
			{
				insertTableSQL=insertTableSQL+s+"'";
			}
			isFirst=true;
		}
			insertTableSQL=insertTableSQL+")";
		insertTableSQL=insertTableSQL+" AND SUBCAT IN('";
		
		for(String s:subcat)
		{
			
			if(isSecond)
			{
				insertTableSQL=insertTableSQL+",'"+s+"'";
				i=1;
			}
			else
			{
				insertTableSQL=insertTableSQL+s+"'";
			}
			isSecond=true;
		}
			insertTableSQL=insertTableSQL+")";
			insertTableSQL=insertTableSQL+" AND a.BUSINESS_ID=b.BUSINESS_ID ORDER BY a.ATTRIBUTESS ASC";
			
		}
		else
		{
			insertTableSQL ="SELECT DISTINCT ATTRIBUTESS FROM ATTRIBUTEMAP WHERE BUSINESS_ID IN(";
			Boolean isFirst=false;
			for(String s:maincat)
			{
			if(isFirst)
			{
				insertTableSQL=insertTableSQL+"INTERSECT SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE MAINCAT='"+s+"'";
			
			}
			else
			{
				insertTableSQL=insertTableSQL+"SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE MAINCAT='"+s+"'";
			}
			isFirst=true;
		}
		Boolean isSecond=false;
		for(String s:subcat)
		{
				insertTableSQL=insertTableSQL+"INTERSECT SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE SUBCAT='"+s+"'";
		}
		
			insertTableSQL=insertTableSQL+") ORDER BY ATTRIBUTESS ASC";
		}
		
			
		
		PreparedStatement preparedStmt = con.prepareStatement(insertTableSQL);
		System.out.println("This is our query:"+insertTableSQL);
		ResultSet rs=preparedStmt.executeQuery();
		
		while (rs.next()) {
			String str=rs.getString("ATTRIBUTESS");
			res.add(str);
		}

	} finally {
		// Never forget to close database connection
		closeConnection(con);
		
	}
	System.out.println("sub list created");
	return res;
}
public static Connection getDBConnection() {
	Connection dbConnection = null;
	try {
		Class.forName("oracle.jdbc.driver.OracleDriver");
	} catch (ClassNotFoundException e) {
		System.out.println(e.getMessage());
	}
	try {
		dbConnection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:viru", "yelp", "Welcome1");
		return dbConnection;

	} catch (SQLException e) {
		System.out.println(e.getMessage());
	}
	return dbConnection;

}

public static void closeConnection(Connection con) {
	try {
		con.close();
	} catch (SQLException e) {
		System.err.println("Cannot close connection: " + e.getMessage());
	}
}

}
