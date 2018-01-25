package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class ResultPanel extends JPanel {
	DefaultTableModel defaultTableModel;
	  //adding scrollPane helps to make table much flexible for any number of rows that goes beyond the frame space.
	    JScrollPane tablePane;
	    JScrollPane sp1;
	/**
	 * Create the panel.
	 */
	public ResultPanel() 
	{
		
	}
	public void popReviews(String s) throws SQLException
	{
		
		List<String> res=new ArrayList<String>();
		Connection con = null;
		

		try {
			con = getDBConnection();
			String insertTableSQL = "SELECT r.RDATE,r.STARS,r.TEXT,r.USER_ID,r.VOTESUSEFUL FROM REVIEW r WHERE r.BUSINESS_ID='"+s+"'";
			PreparedStatement preparedStmt = con.prepareStatement(insertTableSQL);
			
			ResultSet rs=preparedStmt.executeQuery();
			defaultTableModel = new DefaultTableModel();
			  defaultTableModel.addColumn("ReviewDate");
			  defaultTableModel.addColumn("Stars");
			  defaultTableModel.addColumn("Review_Text");
			  defaultTableModel.addColumn("UserID");
			  defaultTableModel.addColumn("Useful_Votes");
			 
			  //defaultTableModel.addRow(new String[]{"BUSINESS_ID","ADDRESS",)
		       
			while (rs.next()) {
				String str1=rs.getString("RDATE");
				String str2=rs.getString("STARS");
				String str3=rs.getString("TEXT");
				String str4=rs.getString("USER_ID");
				Long long1=rs.getLong("VOTESUSEFUL");
				defaultTableModel.addRow(new String[]{str1,str2,str3,str4,long1.toString()});
				
			}

			JTable table=new JTable(defaultTableModel)
			{
			    public String getToolTipText( MouseEvent e )
			    {
			        int row = rowAtPoint( e.getPoint() );
			        int column = columnAtPoint( e.getPoint() );

			        Object value = getValueAt(row, column);
			        return value == null ? null : value.toString();
			    }
			};
			//table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			//TableColumnAdjuster tca = new TableColumnAdjuster(table);
			//tca.adjustColumns();
			JScrollPane scrollPane=new JScrollPane(table);
			scrollPane.setViewportView(table);
			JFrame t=new JFrame();
			t.add(scrollPane);
			//t.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			t.setTitle("Reviews for Business: "+s);
			t.setBounds(200, 200, 450, 300);
			t.setVisible(true);
			//JOptionPane.showMessageDialog(null,scrollPane,"A plain message",
				 //   JOptionPane.PLAIN_MESSAGE);
			
			
		} finally {
			// Never forget to close database connection
			closeConnection(con);
			
		}
		System.out.println("review table created");
		//return res;
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
