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

public class SubCatPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public SubCatPanel() 
	{
		
	}
	public List<String> getSubCat(List<String> maincat) throws SQLException
	{	
		
		JSONParser parser = new JSONParser();
		List<String> res=new ArrayList<String>();
		Connection con = null;
		String insertTableSQL;
		if(maincat.isEmpty())
			return new ArrayList<String>();

		try {
			con = getDBConnection();
			if(HW3.decide.equals("ANY"))
			{
			insertTableSQL = "SELECT DISTINCT SUBCAT FROM BUSINESSCAT_MAP WHERE MAINCAT in('";
			Boolean isFirst=false;
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
				insertTableSQL=insertTableSQL+" and SUBCAT is not null order by SUBCAT ASC";
			}
			else
			{
				insertTableSQL = "select distinct subcat from businesscat_map bm where business_id in(";
				Boolean isFirst=false;
				int i=0;
				for(String s:maincat)
				{
					
					if(isFirst)
					{
						insertTableSQL=insertTableSQL+" INTERSECT select business_id from BUSINESSCAT_MAP where maincat='"+s+"'";
						i=1;
					}
					else
					{
						insertTableSQL=insertTableSQL+" select business_id from BUSINESSCAT_MAP where maincat='"+s+"'";
					}
					isFirst=true;
				}
				insertTableSQL=insertTableSQL+") and bm.SUBCAT is not null order by bm.SUBCAT ASC";
			}
			
			
			PreparedStatement preparedStmt = con.prepareStatement(insertTableSQL);
			System.out.println("This is our query:"+insertTableSQL);
			ResultSet rs=preparedStmt.executeQuery();
			
			while (rs.next()) {
				String str=rs.getString("SUBCAT");
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
