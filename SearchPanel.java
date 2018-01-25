package gui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class SearchPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public SearchPanel() {

	}

	public static DefaultTableModel constructSearchQ() throws SQLException {
		Connection con = null;
		String insertTableSQL = "";
		ResultSet rs;
		DefaultTableModel defaultTableModel;

		try {
			con = getDBConnection();
			if(HW3.decide.equals("ANY"))
			{
				
				insertTableSQL="SELECT DISTINCT b.BUSINESS_ID,b.BUSINESS_NAME,b.ADDRESS,b.CITY,b.STATE,b.STARS,b.REVIEW_COUNT,c.TOTCHECKIN  from business b,checkin c where b.business_id in(";
				insertTableSQL=insertTableSQL+"select business_id from BUSINESSCAT_MAP where maincat IN('";
				Boolean isFirst = false;
				for (String s : HW3.selectMain) {

					if (isFirst) {
						insertTableSQL = insertTableSQL + ",'" + s + "'";
						
					} else {
						insertTableSQL = insertTableSQL + s + "'";
					}
					
					isFirst = true;
				}
				insertTableSQL = insertTableSQL + ")";
				
				if(HW3.selectSub.size() != 0) {
					Boolean isSecond = false;
					insertTableSQL = insertTableSQL +" INTERSECT select business_id from BUSINESSCAT_MAP where subcat IN('";

					for (String s : HW3.selectSub) {

						if (isSecond) {
							insertTableSQL = insertTableSQL + ",'" + s + "'";
							
						} else {
							insertTableSQL = insertTableSQL + s + "'";
						}
						isSecond = true;
					}
					insertTableSQL = insertTableSQL + ")";
				}
				if(HW3.selectAttri.size()!=0)
				{
					Boolean isThird = false;
					insertTableSQL = insertTableSQL + " INTERSECT select business_id from ATTRIBUTEMAP where ATTRIBUTESS IN('";

					for (String s : HW3.selectAttri) {

						if (isThird) {
							insertTableSQL = insertTableSQL + ",'" + s + "'";
							
						} else {
							insertTableSQL = insertTableSQL + s + "'";
						}
						isThird = true;
					}
					insertTableSQL = insertTableSQL + ")";
					
				}
				if(!HW3.selectDay.equals("-SELECT-") || !HW3.selectFrom.equals("-SELECT-") ||!HW3.selectTo.equals("-SELECT-"))
				{	boolean isSet=false;
					insertTableSQL = insertTableSQL + " INTERSECT select business_id from OPERATING_HOURS where";
					
					if(!HW3.selectDay.equals("-SELECT-"))
					{	
						insertTableSQL = insertTableSQL + " DAY='";
						String s=HW3.selectDay;
						insertTableSQL = insertTableSQL +s+"'";
						isSet=true;
					}
					if(!HW3.selectFrom.equals("-SELECT-"))
					{	
						if(isSet)
							insertTableSQL = insertTableSQL + " AND (OPENHOUR*60)+OPENMIN <='";
						else
							insertTableSQL = insertTableSQL + " (OPENHOUR*60)+OPENMIN <='";
						isSet=true;
						String s=HW3.selectFrom;
						String[] from=s.split(":");
						int t1=Integer.valueOf(from[0]);
						int t2=Integer.valueOf(from[1]);
						int check=(t1*60)+t2;
						insertTableSQL = insertTableSQL +check+"'";
						
					}
					if(!HW3.selectTo.equals("-SELECT-"))
					{	
						if(isSet)
							insertTableSQL = insertTableSQL + " AND (CLOSEHOUR*60)+CLOSEMIN >='";
						else
							insertTableSQL = insertTableSQL + " (CLOSEHOUR*60)+CLOSEMIN >='";
						isSet=true;
						String s=HW3.selectTo;
						String[] To=s.split(":");
						int t1=Integer.valueOf(To[0]);
						int t2=Integer.valueOf(To[1]);
						int check=(t1*60)+t2;
						insertTableSQL = insertTableSQL +check+"'";
						
					}
				}
				insertTableSQL = insertTableSQL + ")";
				if(!HW3.selectLoc.equals("-SELECT-"))
				{
					
					String s=HW3.selectLoc;
					String[] loc=s.split(",");
					insertTableSQL = insertTableSQL + " AND b.CITY='"+loc[0]+"' AND b.STATE='"+loc[1]+"'";
					
				}
				insertTableSQL=insertTableSQL+" and b.business_id=c.business_id ORDER BY b.BUSINESS_NAME ASC";
				
			/*if(HW3.selectAttri.size()==0 && HW3.selectDay.equals("-SELECT-") && HW3.selectFrom.equals("-SELECT-")
					&& HW3.selectTo.equals("-SELECT-"))
			{
				insertTableSQL = "SELECT DISTINCT b.BUSINESS_ID,b.ADDRESS,b.CITY,b.STATE,b.STARS,b.REVIEW_COUNT,c.TOTCHECKIN"
						+ " FROM BUSINESS b,BUSINESSCAT_MAP bm,CHECKIN c WHERE b.BUSINESS_ID=bm.BUSINESS_ID AND bm.BUSINESS_ID=c.BUSINESS_ID AND bm.MAINCAT IN('";
				Boolean isFirst = false;
				int i = 0;
				for (String s : HW3.selectMain) {

					if (isFirst) {
						insertTableSQL = insertTableSQL + ",'" + s + "'";
						i = 1;
					} else {
						insertTableSQL = insertTableSQL + s + "'";
					}
					
					isFirst = true;
				}
				insertTableSQL = insertTableSQL + ")";
				if(HW3.selectSub.size() != 0) {
					Boolean isSecond = false;
					insertTableSQL = insertTableSQL + " AND SUBCAT IN('";

					for (String s : HW3.selectSub) {

						if (isSecond) {
							insertTableSQL = insertTableSQL + ",'" + s + "'";
							i = 1;
						} else {
							insertTableSQL = insertTableSQL + s + "'";
						}
						isSecond = true;
					}
					insertTableSQL = insertTableSQL + ")";
				}
				if(!HW3.selectLoc.equals("-SELECT-"))
				{
					
					String s=HW3.selectLoc;
					String[] loc=s.split(",");
					insertTableSQL = insertTableSQL + " AND b.CITY='"+loc[0]+"' AND b.STATE='"+loc[1]+"'";
					
				}
			}
			else if(HW3.selectDay.equals("-SELECT-") && HW3.selectFrom.equals("-SELECT-")
					&& HW3.selectTo.equals("-SELECT-"))
			{
				insertTableSQL = "SELECT DISTINCT b.BUSINESS_ID,b.ADDRESS,b.CITY,b.STATE,b.STARS,b.REVIEW_COUNT,c.TOTCHECKIN"
						+ " FROM BUSINESS b,BUSINESSCAT_MAP bm,CHECKIN c,ATTRIBUTEMAP am WHERE b.BUSINESS_ID=bm.BUSINESS_ID and b.BUSINESS_ID=am.BUSINESS_ID AND bm.BUSINESS_ID=c.BUSINESS_ID AND bm.MAINCAT IN('";
				Boolean isFirst = false;
				int i = 0;
				for (String s : HW3.selectMain) {

					if (isFirst) {
						insertTableSQL = insertTableSQL + ",'" + s + "'";
						i = 1;
					} else {
						insertTableSQL = insertTableSQL + s + "'";
					}
					isFirst = true;
				}
				insertTableSQL = insertTableSQL + ")";
				if(HW3.selectSub.size() != 0) {
					Boolean isSecond = false;
					insertTableSQL = insertTableSQL + " AND SUBCAT IN('";

					for (String s : HW3.selectSub) {

						if (isSecond) {
							insertTableSQL = insertTableSQL + ",'" + s + "'";
							i = 1;
						} else {
							insertTableSQL = insertTableSQL + s + "'";
						}
						isSecond = true;
					}
					insertTableSQL = insertTableSQL + ")";
				}
				if(HW3.selectAttri.size()!=0)
				{
					Boolean isThird = false;
					insertTableSQL = insertTableSQL + " AND am.ATTRIBUTESS IN('";

					for (String s : HW3.selectAttri) {

						if (isThird) {
							insertTableSQL = insertTableSQL + ",'" + s + "'";
							i = 1;
						} else {
							insertTableSQL = insertTableSQL + s + "'";
						}
						isThird = true;
					}
					insertTableSQL = insertTableSQL + ")";
					
				}
				if(!HW3.selectLoc.equals("-SELECT-"))
				{
					
					String s=HW3.selectLoc;
					String[] loc=s.split(",");
					insertTableSQL = insertTableSQL + " AND b.CITY='"+loc[0]+"' AND b.STATE='"+loc[1]+"'";
					
				}
			}
			else if(HW3.selectAttri.size()==0 &&(!HW3.selectDay.equals("-SELECT-") || !HW3.selectFrom.equals("-SELECT-")
					|| !HW3.selectTo.equals("-SELECT-")))
			{
				insertTableSQL = "SELECT DISTINCT b.BUSINESS_ID,b.ADDRESS,b.CITY,b.STATE,b.STARS,b.REVIEW_COUNT,c.TOTCHECKIN"
						+ " FROM BUSINESS b,BUSINESSCAT_MAP bm,CHECKIN c,OPERATING_HOURS o WHERE b.BUSINESS_ID=bm.BUSINESS_ID and b.BUSINESS_ID=o.BUSINESS_ID AND bm.BUSINESS_ID=c.BUSINESS_ID AND bm.MAINCAT IN('";
				Boolean isFirst = false;
				int i = 0;
				for (String s : HW3.selectMain) {

					if (isFirst) {
						insertTableSQL = insertTableSQL + ",'" + s + "'";
						i = 1;
					} else {
						insertTableSQL = insertTableSQL + s + "'";
					}
					isFirst = true;
				}
				insertTableSQL = insertTableSQL + ")";
				if(HW3.selectSub.size() != 0) {
					Boolean isSecond = false;
					insertTableSQL = insertTableSQL + " AND SUBCAT IN('";

					for (String s : HW3.selectSub) {

						if (isSecond) {
							insertTableSQL = insertTableSQL + ",'" + s + "'";
							i = 1;
						} else {
							insertTableSQL = insertTableSQL + s + "'";
						}
						isSecond = true;
					}
					insertTableSQL = insertTableSQL + ")";
				}
				if(!HW3.selectLoc.equals("-SELECT-"))
				{
					
					String s=HW3.selectLoc;
					String[] loc=s.split(",");
					insertTableSQL = insertTableSQL + " AND b.CITY='"+loc[0]+"' AND b.STATE='"+loc[1]+"'";
					
				}
				if(!HW3.selectDay.equals("-SELECT-"))
				{
					String s=HW3.selectDay;
					insertTableSQL = insertTableSQL +" AND o.DAY='"+s+"'";
					
				}
				if(!HW3.selectFrom.equals("-SELECT-"))
				{
					String s=HW3.selectFrom;
					String[] from=s.split(":");
					int t1=Integer.valueOf(from[0]);
					int t2=Integer.valueOf(from[1]);
					int check=(t1*60)+t1;
					insertTableSQL = insertTableSQL +" AND (o.OPENHOUR*60)+o.OPENMIN <='"+check+"'";
					
				}
				if(!HW3.selectTo.equals("-SELECT-"))
				{
					String s=HW3.selectTo;
					String[] To=s.split(":");
					int t1=Integer.valueOf(To[0]);
					int t2=Integer.valueOf(To[1]);
					int check=(t1*60)+t1;
					insertTableSQL = insertTableSQL +" AND (o.CLOSEHOUR*60)+o.CLOSEMIN <='"+check+"'";
					
				}
			}
			else
			{
				insertTableSQL = "SELECT DISTINCT b.BUSINESS_ID,b.ADDRESS,b.CITY,b.STATE,b.STARS,b.REVIEW_COUNT,c.TOTCHECKIN"
						+ " FROM BUSINESS b,BUSINESSCAT_MAP bm,CHECKIN c,OPERATING_HOURS o,ATTRIBUTEMAP am WHERE b.BUSINESS_ID=bm.BUSINESS_ID and b.BUSINESS_ID=o.BUSINESS_ID AND b.BUSINESS_ID=am.BUSINESS_ID AND bm.BUSINESS_ID=c.BUSINESS_ID AND bm.MAINCAT IN('";
				Boolean isFirst = false;
				int i = 0;
				for (String s : HW3.selectMain) {

					if (isFirst) {
						insertTableSQL = insertTableSQL + ",'" + s + "'";
						i = 1;
					} else {
						insertTableSQL = insertTableSQL + s + "'";
					}
					isFirst = true;
				}
				insertTableSQL = insertTableSQL + ")";
				if(HW3.selectSub.size() != 0) {
					Boolean isSecond = false;
					insertTableSQL = insertTableSQL + " AND SUBCAT IN('";

					for (String s : HW3.selectSub) {

						if (isSecond) {
							insertTableSQL = insertTableSQL + ",'" + s + "'";
							i = 1;
						} else {
							insertTableSQL = insertTableSQL + s + "'";
						}
						isSecond = true;
					}
					insertTableSQL = insertTableSQL + ")";
				}
				if(HW3.selectAttri.size()!=0)
				{
					Boolean isThird = false;
					insertTableSQL = insertTableSQL + " AND am.ATTRIBUTESS IN('";

					for (String s : HW3.selectAttri) {

						if (isThird) {
							insertTableSQL = insertTableSQL + ",'" + s + "'";
							i = 1;
						} else {
							insertTableSQL = insertTableSQL + s + "'";
						}
						isThird = true;
					}
					insertTableSQL = insertTableSQL + ")";
					
				}
				if(!HW3.selectLoc.equals("-SELECT-"))
				{
					
					String s=HW3.selectLoc;
					String[] loc=s.split(",");
					insertTableSQL = insertTableSQL + " AND b.CITY='"+loc[0]+"' AND b.STATE='"+loc[1]+"'";
					
				}
				if(!HW3.selectDay.equals("-SELECT-"))
				{
					String s=HW3.selectDay;
					insertTableSQL = insertTableSQL +" AND o.DAY='"+s+"'";
					
				}
				if(!HW3.selectFrom.equals("-SELECT-"))
				{
					String s=HW3.selectFrom;
					String[] from=s.split(":");
					int t1=Integer.valueOf(from[0]);
					int t2=Integer.valueOf(from[1]);
					int check=(t1*60)+t1;
					insertTableSQL = insertTableSQL +" AND (o.OPENHOUR*60)+o.OPENMIN <='"+check+"'";
					
				}
				if(!HW3.selectTo.equals("-SELECT-"))
				{
					String s=HW3.selectTo;
					String[] To=s.split(":");
					int t1=Integer.valueOf(To[0]);
					int t2=Integer.valueOf(To[1]);
					int check=(t1*60)+t1;
					insertTableSQL = insertTableSQL +" AND (o.CLOSEHOUR*60)+o.CLOSEMIN <='"+check+"'";
					
				}
			}*/
			}
			else
			{
				insertTableSQL = "SELECT DISTINCT b.BUSINESS_ID,b.BUSINESS_NAME,b.ADDRESS,b.CITY,b.STATE,b.STARS,b.REVIEW_COUNT,c.TOTCHECKIN FROM BUSINESS b,CHECKIN c"+
								  " WHERE b.BUSINESS_ID=c.BUSINESS_ID AND b.BUSINESS_ID IN(";
				
				Boolean isFirst = false;
				int i = 0;
				for (String s : HW3.selectMain) 
				{

					if (isFirst) {
						insertTableSQL = insertTableSQL+ " INTERSECT SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE MAINCAT='" + s + "'";
						i = 1;
					} else {
						insertTableSQL =insertTableSQL+" SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE MAINCAT='" +s+ "'";
					}
					isFirst = true;
				}
				if(HW3.selectSub.size() != 0) {
					for (String s : HW3.selectSub) {
						insertTableSQL = insertTableSQL
								+ " INTERSECT SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE SUBCAT='" + s + "'";
					}
					
				}
				if(HW3.selectAttri.size()!=0)
				{
					

					for (String s : HW3.selectAttri) {
						insertTableSQL = insertTableSQL
								+ " INTERSECT SELECT BUSINESS_ID FROM ATTRIBUTEMAP WHERE ATTRIBUTESS='" + s + "'";	
					}
				
					
				}
				if(!HW3.selectLoc.equals("-SELECT-"))
				{
					
					String s=HW3.selectLoc;
					String[] loc=s.split(",");
					insertTableSQL = insertTableSQL +" INTERSECT SELECT BUSINESS_ID FROM BUSINESS WHERE CITY='"+loc[0]+"' AND STATE='"+loc[1]+"'";
					
				}
				if(!HW3.selectDay.equals("-SELECT-") || !HW3.selectFrom.equals("-SELECT-") ||!HW3.selectTo.equals("-SELECT-"))
				{	boolean isSet=false;
					insertTableSQL = insertTableSQL + " INTERSECT select business_id from OPERATING_HOURS where";
					
					if(!HW3.selectDay.equals("-SELECT-"))
					{	
						insertTableSQL = insertTableSQL + " DAY='";
						String s=HW3.selectDay;
						insertTableSQL = insertTableSQL +s+"'";
						isSet=true;
					}
					if(!HW3.selectFrom.equals("-SELECT-"))
					{	
						if(isSet)
							insertTableSQL = insertTableSQL + " AND (OPENHOUR*60)+OPENMIN <='";
						else
							insertTableSQL = insertTableSQL + " (OPENHOUR*60)+OPENMIN <='";
						isSet=true;
						String s=HW3.selectFrom;
						String[] from=s.split(":");
						int t1=Integer.valueOf(from[0]);
						int t2=Integer.valueOf(from[1]);
						int check=(t1*60)+t2;
						insertTableSQL = insertTableSQL +check+"'";
						
					}
				}
				insertTableSQL = insertTableSQL + ") ORDER BY b.BUSINESS_NAME ASC";
			}
			PreparedStatement preparedStmt = con.prepareStatement(insertTableSQL);
			System.out.println("This is our search query: " + insertTableSQL);
			rs = preparedStmt.executeQuery();

			/*
			 * defaultTableModel = new DefaultTableModel();
			 * 
			 * ResultSetMetaData meta = rs.getMetaData(); int numberOfColumns =
			 * meta.getColumnCount(); while (rs.next()) { Object [] rowData =
			 * new Object[numberOfColumns]; for (int i = 0; i < rowData.length;
			 * ++i) { rowData[i] = rs.getObject(i+1);
			 * System.out.println("this is result:"+
			 * rs.getObject(i+1).toString());
			 * 
			 * } defaultTableModel.addRow(rowData); }
			 */

			defaultTableModel = new DefaultTableModel();
			
			defaultTableModel.addColumn("BUSINESS_ID");
			defaultTableModel.addColumn("BUSINESS_NAME");
			defaultTableModel.addColumn("ADDRESS");
			defaultTableModel.addColumn("CITY");
			defaultTableModel.addColumn("STATE");
			defaultTableModel.addColumn("STARS");
			defaultTableModel.addColumn("REVIEW_COUNT");
			defaultTableModel.addColumn("TOTCHECKIN");

			// defaultTableModel.addRow(new String[]{"BUSINESS_ID","ADDRESS",)
			List<String> bid = new ArrayList<String>();
			List<String> bname = new ArrayList<String>();
			List<String> badd = new ArrayList<String>();
			List<String> bcity = new ArrayList<String>();
			List<String> bstate = new ArrayList<String>();
			List<Long> bstars = new ArrayList<Long>();
			List<Long> brc = new ArrayList<Long>();
			List<Long> btc = new ArrayList<Long>();
			Integer count=0;
			while (rs.next()) {
				
				String str1 = rs.getString("BUSINESS_ID");
				bid.add(str1);
				String str5=rs.getString("BUSINESS_NAME");
				bname.add(str5);
				String str2 = rs.getString("ADDRESS");
				badd.add(str2);
				String str3 = rs.getString("CITY");
				bcity.add(str3);
				String str4 = rs.getString("STATE");
				bstate.add(str4);
				Long long1 = rs.getLong("STARS");
				bstars.add(long1);
				Long long2 = rs.getLong("REVIEW_COUNT");
				brc.add(long2);
				Long long3 = rs.getLong("TOTCHECKIN");
				btc.add(long3);
				defaultTableModel.addRow(
						new String[] {str1,str5, str2, str3, str4, long1.toString(), long2.toString(), long3.toString() });

			}

			// JTable table=new JTable(defaultTableModel);
			// JOptionPane.showMessageDialog(null, new JScrollPane(table));

		} finally {
			// Never forget to close database connection
			closeConnection(con);

		}
		System.out.println("list created");
		return defaultTableModel;
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
