import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Populate {
	public void insertDefaultCheckin()
	{
		/*executeQuery("CREATE INDEX attrimap_bindex ON ATTRIBUTEMAP(BUSINESS_ID)");
		executeQuery("CREATE INDEX attrimap_attrindex ON ATTRIBUTEMAP(ATTRIBUTESS)");
		//executeQuery("CREATE INDEX maincat_map ON CAT_MAP(MAINCAT)");
		//executeQuery("CREATE INDEX subcat_map ON CAT_MAP(SUBCAT)");
		executeQuery("CREATE INDEX bid_bmap ON BUSINESSCAT_MAP(BUSINESS_ID)");
		executeQuery("CREATE INDEX maincat_bmap ON BUSINESSCAT_MAP(MAINCAT)");
		executeQuery("CREATE INDEX subcatcat_bmap ON BUSINESSCAT_MAP(SUBCAT)");
		executeQuery("CREATE INDEX businessid_rv ON REVIEW(BUSINESS_ID)");
		executeQuery("CREATE INDEX businessid_oh ON OPERATING_HOURS(BUSINESS_ID)");
		//executeQuery("CREATE INDEX businessid_am ON ATTRIBUTEMAP(BUSINESS_ID)");
		executeQuery("CREATE INDEX businessid_ch ON CHECKIN(BUSINESS_ID)");*/
		executeQuery("insert into checkin(BUSINESS_ID,TOTCHECKIN) select distinct business_id,'0' AS TOTCHECKIN "
				+ "from business where business_id not in (select business_id from checkin)");
		System.out.println("Default value '0' inserted in Checkin for remaining businesses");
	}
	public void attributeHelper(Connection con, List<String> list) throws SQLException, ParseException {
		JSONParser parser = new JSONParser();
		String insertTableSQL = "INSERT INTO ATTRIBUTEMAP" + "(BUSINESS_ID,ATTRIBUTESS)" + "VALUES(?,?)";
		PreparedStatement preparedStmt = con.prepareStatement(insertTableSQL);

		for (String str : list) {

			Object obj = parser.parse(str);

			JSONObject jsonObject = (JSONObject) obj;
			String business_id = (String) jsonObject.get("business_id");
			JSONObject attributes = (JSONObject) jsonObject.get("attributes");

			String keyStr = null;
			String keyStr1 = null;
			String keyStr2 = null;

			for (Object key : attributes.keySet()) {
				// based on you key types
				keyStr = (String) key;
				//System.out.println("got this main attribute"+keyStr);
				Object attri = attributes.get(keyStr);
				if(attri != null)
				{
					if (attri instanceof Map) 
					{
						//System.out.println("This attri is a map:"+keyStr);
						
						Iterator<Map.Entry> itr1 = ((HashMap) attri).entrySet().iterator();
						while (itr1.hasNext()) {
							Map.Entry pair = itr1.next();
							//System.out.println(pair.getKey()+":"+pair.getValue());
							keyStr1=(String) pair.getKey();
							Object value=pair.getValue();
							/*if(attri instanceof Boolean)
							{
							if((boolean) value)
							{
								
									String ans = keyStr+"_"+keyStr1;
									preparedStmt.setString(1, business_id);
									preparedStmt.setString(2, ans);
									preparedStmt.addBatch();
								
							}
							else{*/
								String ans = keyStr+"_"+keyStr1+"_"+value;
								preparedStmt.setString(1, business_id);
								preparedStmt.setString(2, ans);
								preparedStmt.addBatch();
							}
							//}
							
					} 
				else {
						/*if(attri instanceof Boolean )
						{
						if((boolean) attri)
						{
							
								String ans = keyStr;
								preparedStmt.setString(1, business_id);
								preparedStmt.setString(2, ans);
								preparedStmt.addBatch();
						}
						}
					else{*/
							String ans = keyStr+"_"+attri;
							preparedStmt.setString(1, business_id);
							preparedStmt.setString(2, ans);
							preparedStmt.addBatch();
						}
						}
					}
				

		}

		
		preparedStmt.executeBatch();
		preparedStmt.close();
	}

	public static void reviewHelper(String FileName) throws SQLException, ParseException, IOException {
		JSONParser parser = new JSONParser();
		System.out.println("Inserting REVIEW data........");
		Connection con = null;
		java.sql.Date sqlDate;
		FileInputStream fstream = null;
		BufferedReader br = null;

		try {
			// FileInputStream fstream = new
			// FileInputStream("C:\\Shweta\\Database\\Assignments\\HW3\\Test_yelp_dataSheet\\yelp_review.json");
			fstream = new FileInputStream(FileName);
			br = new BufferedReader(new InputStreamReader(fstream));
			Object obj;
			String strLine;

			// Statement stmt = con.createStatement();
			int count = 0;
			List<String> list = new ArrayList<String>();
			con = getDBConnection();
			con.setAutoCommit(false);
			while ((strLine = br.readLine()) != null) {
				list.add(strLine);
				if (list.size() > 5000) {
					count++;
					// System.out.println("Start Thread:-" + count);
					ReviewThread object = new ReviewThread(list, count);
					object.execute(con);

					list = new ArrayList<String>();
				}
			}
			if (list.size() > 0) {
				ReviewThread object = new ReviewThread(list, count);
				object.execute(con);
			}
			con.commit();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Never forget to close database connection
			closeConnection(con);
			br.close();
			fstream.close();
		}
		System.out.println("REVIEW data INSERTED");

	}

	public void operatingHourseHelper(Connection con, List<String> list) throws SQLException, ParseException {
		JSONParser parser = new JSONParser();
		String insertTableSQL = "INSERT INTO OPERATING_HOURS"
				+ "(BUSINESS_ID,DAY, OPENHOUR, OPENMIN,CLOSEHOUR,CLOSEMIN)" + "VALUES(?,?,?,?,?,?)";
		PreparedStatement preparedStmt = con.prepareStatement(insertTableSQL);
		for (String str : list) {

			Object obj = parser.parse(str);

			JSONObject jsonObject = (JSONObject) obj;
			String business_id = (String) jsonObject.get("business_id");
			JSONObject hours = (JSONObject) jsonObject.get("hours");
			String day = null;

			for (Object key : hours.keySet()) {
				// based on you key types
				day = (String) key;
				JSONObject timing = (JSONObject) hours.get(day);
				for (Object key1 : timing.keySet()) {
					String close = (String) timing.get("close");
					String open = (String) timing.get("open");
					String[] cparts = close.split(":");
					String[] oparts = open.split(":");
					Long openhour = Long.parseLong(oparts[0]);
					Long openmin = Long.parseLong(oparts[1]);
					Long closehour = Long.parseLong(cparts[0]);
					Long closemin = Long.parseLong(cparts[1]);

					preparedStmt.setString(1, business_id.trim());
					preparedStmt.setString(2, day.trim());
					preparedStmt.setLong(3, openhour);
					preparedStmt.setLong(4, openmin);
					preparedStmt.setLong(5, closehour);
					preparedStmt.setLong(6, closemin);
					preparedStmt.addBatch();
				}

			}
		}
		preparedStmt.executeBatch();
		preparedStmt.close();
	}

	public void checkinHelper(Connection con, List<String> list) throws SQLException, ParseException {
		JSONParser parser = new JSONParser();
		String insertTableSQL = "INSERT INTO CHECKIN" + "(BUSINESS_ID,TOTCHECKIN)"
				+ "VALUES(?,?)";
		PreparedStatement preparedStmt = con.prepareStatement(insertTableSQL);

		for (String str : list) {

			Object obj = parser.parse(str);

			JSONObject jsonObject = (JSONObject) obj;
			String business_id = (String) jsonObject.get("business_id");
			JSONObject checkIns = (JSONObject) jsonObject.get("checkin_info");

			String keyStr = null;
			Long tot=(long) 0;
			for (Object key : checkIns.keySet()) {
				// based on you key types
				keyStr = (String) key;

				Long numberOfCheckin = (Long) checkIns.get(keyStr);
				tot=tot+numberOfCheckin;
				//String[] parts = keyStr.split("-");
				//Long startHour = Long.parseLong(parts[0]);
				//Long day_id = Long.parseLong(parts[1]);

				
				//preparedStmt.setLong(2, startHour);
				//preparedStmt.setLong(3, day_id);
				
			}
			preparedStmt.setString(1, business_id);
			preparedStmt.setLong(2, tot);
			preparedStmt.addBatch();
		}
		preparedStmt.executeBatch();
		preparedStmt.close();
	}

	public void businessCatMapHelper(Connection con, List<String> list) throws SQLException, ParseException {
		JSONParser parser = new JSONParser();
		System.out.println("Entered in a block to insert businesscat_map");
		String insertTableSQL = "INSERT INTO BUSINESSCAT_MAP" + "(BUSINESS_ID,MAINCAT,SUBCAT)" + "VALUES(?,?,?)";
		PreparedStatement preparedStmt1 = con.prepareStatement(insertTableSQL);
		PreparedStatement preparedStmt2 = con.prepareStatement(insertTableSQL);
		for (String str : list) {
			Object obj = parser.parse(str);
			ArrayList<String> mainCategory = new ArrayList<String>();
			ArrayList<String> subCategory = new ArrayList<String>();
			
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray categories = (JSONArray) jsonObject.get("categories");

			String businessid = (String) jsonObject.get("business_id");
			Iterator<String> iterator = categories.iterator();

			while (iterator.hasNext()) {

				String cat = iterator.next();
				// System.out.println("Category:"+cat);
				/*
				 * check if iterator.hasNext() is main category if yes, add the
				 * value in mainCategory ArrayList otherwise add it in
				 * subCategory Arraylist
				 */
				if (Business.categoryList.indexOf(cat) != -1) {
					mainCategory.add(cat);

				} else {
					subCategory.add(cat);
				}

			}

			for (String str1 : mainCategory) {
				// System.out.println("Business ID:" + business_id +
				// "-->MainCategory: " + str);

				if (subCategory.isEmpty()) {

					preparedStmt1.setString(1, businessid.trim());
					preparedStmt1.setString(2, str1.trim());
					preparedStmt1.setString(3, null);
					preparedStmt1.addBatch();
					//System.out.println("came here");

				} else {
					for (String str2 : subCategory) {

						preparedStmt2.setString(1, businessid.trim());
						preparedStmt2.setString(2, str1.trim());
						preparedStmt2.setString(3, str2);
						preparedStmt2.addBatch();
						//System.out.println("came here");

					}
				}
			}

		}
		preparedStmt1.executeBatch();
		preparedStmt2.executeBatch();
		preparedStmt1.close();
		preparedStmt2.close();

	}

	public void businessHelper(Connection con, List<String> list) throws SQLException, ParseException {
		JSONParser parser = new JSONParser();

		String insertTableSQL = "INSERT INTO BUSINESS"
				+ "(BUSINESS_ID,ADDRESS,CITY,STATE,REVIEW_COUNT,BUSINESS_NAME,STARS,TYPE)" + "VALUES(?,?,?,?,?,?,?,?)";

		PreparedStatement preparedStmt = con.prepareStatement(insertTableSQL);

		for (String str : list) {
			Object obj = parser.parse(str);
			ArrayList<String> mainCategory = new ArrayList<String>();
			ArrayList<String> subCategory = new ArrayList<String>();

			JSONObject jsonObject = (JSONObject) obj;
			JSONArray categories = (JSONArray) jsonObject.get("categories");

			String businessid = (String) jsonObject.get("business_id");
			String address = (String) jsonObject.get("full_address");
			Long reviewc = (Long) jsonObject.get("review_count");
			String city = (String) jsonObject.get("city");
			String bname = (String) jsonObject.get("name");
			String state = (String) jsonObject.get("state");
			Double stars = (Double) jsonObject.get("stars");
			String type = (String) jsonObject.get("type");

			preparedStmt.setString(1, businessid.trim());
			preparedStmt.setString(2, address.trim());
			preparedStmt.setString(3, city.trim());
			preparedStmt.setString(4, state.trim());
			preparedStmt.setLong(5, reviewc);
			preparedStmt.setString(6, bname.trim());
			preparedStmt.setDouble(7, stars);
			preparedStmt.setString(8, type.trim());

			preparedStmt.addBatch();

		}
		preparedStmt.executeBatch();
		preparedStmt.close();

	}

	public void userHelper(Connection con, List<String> list) throws ParseException, SQLException {
		JSONParser parser = new JSONParser();
		String insertTableSQL = "INSERT INTO YELP_USER"
				+ "(YELPING_SINCE,VOTED_FUNNY,VOTED_USEFUL,VOTED_COOL,REVIEW_COUNT,USER_NAME,USER_ID,Number_OfFriends,FANS,AVERAGE_STARS,USER_TYPE)"
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?)";

		PreparedStatement preparedStmt = con.prepareStatement(insertTableSQL);

		for (String str : list) {
			Object obj = parser.parse(str);

			JSONObject jsonObject = (JSONObject) obj;
			Long voted_funny = null;
			Long voted_cool = null;
			Long voted_useful = null;

			String memberSince = (String) jsonObject.get("yelping_since");
			Map address = ((Map) jsonObject.get("votes"));

			// iterating address Map
			Iterator<Map.Entry> itr1 = address.entrySet().iterator();
			while (itr1.hasNext()) {
				Map.Entry pair = itr1.next();
				if (pair.getKey().equals("funny"))
					voted_funny = (Long) pair.getValue();
				if (pair.getKey().equals("cool"))
					voted_cool = (Long) pair.getValue();
				if (pair.getKey().equals("useful"))
					voted_useful = (Long) pair.getValue();
			}

			Long reviewCount = (Long) jsonObject.get("review_count");

			String user_id = (String) jsonObject.get("user_id");
			String name = (String) jsonObject.get("name");
			Long fans = (Long) jsonObject.get("fans");
			String type = (String) jsonObject.get("type");
			Double average_stars = (Double) jsonObject.get("average_stars");
			JSONArray friends = (JSONArray) jsonObject.get("friends");
			int numberOfFriend = friends.size();

			preparedStmt.setDate(1, getCurrentJavaSqlDate(memberSince, 1));
			preparedStmt.setLong(2, voted_funny);
			preparedStmt.setLong(3, voted_useful);
			preparedStmt.setLong(4, voted_cool);
			preparedStmt.setLong(5, reviewCount);
			preparedStmt.setNString(6, name.trim());
			preparedStmt.setNString(7, user_id.trim());
			preparedStmt.setLong(8, numberOfFriend);
			preparedStmt.setLong(9, fans);
			preparedStmt.setDouble(10, average_stars);
			preparedStmt.setString(11, type.trim());
			preparedStmt.addBatch();
		}

		preparedStmt.executeBatch();
		preparedStmt.close();

	}

	public void populatenew(String FileName, String tablename) {
		System.out.println("Inserting " + tablename + " Data........");
		// JSONParser parser = new JSONParser();

		Connection con = null;
		java.sql.Date sqlDate;
		FileInputStream fstream = null;
		BufferedReader br = null;
		int t = 0;
		try {

			fstream = new FileInputStream(FileName);
			br = new BufferedReader(new InputStreamReader(fstream));
			Object obj;
			String strLine;
			con = getDBConnection();
			con.setAutoCommit(false);
			Statement stmt = con.createStatement();
			PreparedStatement preparedStmt = null;
			List<String> list = new ArrayList<String>();
			while ((strLine = br.readLine()) != null) {

				list.add(strLine);
				if (list.size() < 5000)
					continue;
				if (tablename.equals("YELP_USER"))
					userHelper(con, list);
				else if (tablename.equals("BUSINESS"))
					businessHelper(con, list);
				else if (tablename.equals("BUSINESSCAT_MAP"))
					businessCatMapHelper(con, list);
				else if (tablename.equals("CHECKIN"))
					checkinHelper(con, list);
				else if (tablename.equals("OPERATING_HOURS"))
					operatingHourseHelper(con, list);
				else if(tablename.equals("ATTRIBUTEMAP"))
					attributeHelper(con,list);
				// else if(tablename.equals("REVIEW"))
				// reviewHelper(con,list);
				list = new ArrayList<String>();

			}
			if (list.size() > 0) {
				if (tablename.equals("YELP_USER"))
					userHelper(con, list);
				else if (tablename.equals("BUSINESS"))
					businessHelper(con, list);
				else if (tablename.equals("BUSINESSCAT_MAP"))
					businessCatMapHelper(con, list);
				else if (tablename.equals("CHECKIN"))
					checkinHelper(con, list);
				else if (tablename.equals("OPERATING_HOURS"))
					operatingHourseHelper(con, list);
				else if(tablename.equals("ATTRIBUTEMAP"))
					attributeHelper(con,list);
				// else if(tablename.equals("REVIEW"))
				// reviewHelper(con,list);
			}

			con.commit();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.err.println("Errors occurs when communicating with the database server: " + e.getMessage());
		} finally {
			// Never forget to close database connection
			closeConnection(con);
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fstream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println(tablename + " data INSERTED");
	}

	public static java.sql.Date getCurrentJavaSqlDate(String s, int t) {
		DateFormat format;
		if (t == 1) {
			format = new SimpleDateFormat("yyyy-mm", Locale.ENGLISH);
		} else
			format = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
		Date date = null;
		try {
			date = format.parse(s);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new java.sql.Date(date.getTime());
	}


	public static void executeQuery(String query) {
		// Connection conn = null;
		Statement stmt = null;
		Connection conn = null;
		System.out.println("Executing:"+query);
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:viru", "yelp", "Welcome1");
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
			// System.out.println("Created table in given database...");
		} catch (SQLException se) {
			// Handle errors for JDBC
			System.out.println(query);
			se.printStackTrace();

		} catch (Exception e) {
			// Handle errors for Class.forName
			System.out.println(query);
			e.printStackTrace();
		} finally {

			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();

			}

		}

	}


	public static void deleteTables() {
		System.out.println("Deleting the data from tables");
		executeQuery("TRUNCATE TABLE REVIEW");
		executeQuery("DELETE FROM OPERATING_HOURS");
		executeQuery("DELETE FROM CHECKIN");
		executeQuery("DELETE FROM BUSINESSCAT_MAP");
		// executeQuery("DROP TABLE CAT_MAP");
		executeQuery("DELETE FROM ATTRIBUTEMAP");
		executeQuery("DELETE FROM BUSINESS");
		executeQuery("DELETE FROM YELP_USER");

		System.out.println("All the data is deleted, we are ready to insert the new data!");
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

	public static void main(String[] args) throws IOException, SQLException, ParseException {
		Populate p = new Populate();
		Business.initiateCat();
		deleteTables();
		//p.executeQuery(Business.createOperatingHours());
		//p.populatenew("yelp_business.json", "OPERATING_HOURS");
		//executeQuery(YelpUser.createUserConstruct());
		p.populatenew("yelp_user.json", "YELP_USER");
		//executeQuery(Business.createBusiness());
		p.populatenew("yelp_business.json", "BUSINESS");
		//executeQuery(Business.createAttributeMap());
		p.populatenew("yelp_business.json", "ATTRIBUTEMAP");
		//executeQuery(Business.createBusinessCatMap());
		p.populatenew("yelp_business.json","BUSINESSCAT_MAP");
		//executeQuery(Business.createCheckIn());
		p.populatenew("yelp_checkin.json","CHECKIN");
		//executeQuery(Business.createOperatingHours());
		p.populatenew("yelp_business.json","OPERATING_HOURS");
		 //executeQuery(Business.insertReview()); 
		 reviewHelper("yelp_review.json");
		//executeQuery(Business.createCatMap());
		// executeQuery(Business.insertCatmap());
		p.insertDefaultCheckin();
		
		
	}
	

}
