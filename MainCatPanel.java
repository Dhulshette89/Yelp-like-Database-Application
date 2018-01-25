package gui;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MainCatPanel extends JPanel {

	/**
	 * Create the panel.
	 * @throws SQLException 
	 */
	public MainCatPanel() throws SQLException 
	{
		
		
		setVisible(true);
		
	}
	/*public List<JCheckBox> createCheckbox() throws SQLException
	{
		//List<String> checkList=reviewHelper();
		//ArrayList<JCheckBox> mainCheck = new ArrayList<JCheckBox>();
		//JCheckBox chckbxNewCheckBox = new JCheckBox("New check box");
		add(chckbxNewCheckBox, BorderLayout.CENTER);
		for(String s:checkList)
		{
			JCheckBox cb = new JCheckBox(s);
			mainCheck.add(cb);
			
			System.out.println("here checkbox created");
		}
		return mainCheck;
	}*/
	public static List<String> getMainList() throws SQLException{
		
		List<String> res=new ArrayList<String>();
		Connection con = null;
		

		try {
			con = getDBConnection();
			String insertTableSQL = "SELECT DISTINCT MAINCAT FROM BUSINESSCAT_MAP order by MAINCAT ASC";
			PreparedStatement preparedStmt = con.prepareStatement(insertTableSQL);
			
			ResultSet rs=preparedStmt.executeQuery();
			
			while (rs.next()) {
				String str=rs.getString("MAINCAT");
				res.add(str);
			}

		} finally {
			// Never forget to close database connection
			closeConnection(con);
			
		}
		System.out.println("list created");
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
