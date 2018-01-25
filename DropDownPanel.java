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

public class DropDownPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public DropDownPanel() {

	}

	public List<String> getLocations(List<String> maincat) throws SQLException {
		JSONParser parser = new JSONParser();
		List<String> res = new ArrayList<String>();
		Connection con = null;
		String insertTableSQL;
		if (maincat.isEmpty())
			return new ArrayList<String>();
		try {
			con = getDBConnection();
			if (HW3.decide.equals("ANY")) {
				insertTableSQL = "SELECT DISTINCT CITY,STATE FROM BUSINESS b,BUSINESSCAT_MAP bm WHERE MAINCAT IN('";
				Boolean isFirst = false;
				int i = 0;
				for (String s : maincat) {

					if (isFirst) {
						insertTableSQL = insertTableSQL + ",'" + s + "'";
						i = 1;
					} else {
						insertTableSQL = insertTableSQL + s + "'";
					}
					isFirst = true;
				}
				insertTableSQL = insertTableSQL + ")";
				insertTableSQL = insertTableSQL + "  AND b.BUSINESS_ID=bm.BUSINESS_ID ORDER BY CITY ASC";

			} else {
				Boolean isFirst = false;
				insertTableSQL = "SELECT DISTINCT CITY,STATE FROM BUSINESS b WHERE BUSINESS_ID IN(SELECT BUSINESS_ID FROM BUSINESSCAT_MAP bm WHERE MAINCAT='";
				for (String s : maincat) {

					if (isFirst) {
						insertTableSQL = insertTableSQL
								+ "INTERSECT SELECT BUSINESS_ID FROM BUSINESSCAT_MAP bm WHERE MAINCAT='" + s + "'";

					} else {
						insertTableSQL = insertTableSQL + s + "'";
					}
					isFirst = true;
				}
				insertTableSQL = insertTableSQL + ") ORDER BY CITY ASC";
			}
			PreparedStatement preparedStmt = con.prepareStatement(insertTableSQL);
			System.out.println("This is our query:" + insertTableSQL);
			ResultSet rs = preparedStmt.executeQuery();

			while (rs.next()) {
				String str = rs.getString("CITY");
				String str1 = rs.getString("STATE");
				String t = str + "," + str1;
				res.add(t);
			}

		} finally {
			// Never forget to close database connection
			closeConnection(con);

		}
		System.out.println("location list created");
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

	public List<String> getLocations(List<String> maincat, List<String> subcat) throws SQLException {

		JSONParser parser = new JSONParser();
		List<String> res = new ArrayList<String>();
		Connection con = null;
		String insertTableSQL = "";
		if (subcat.isEmpty())
			return getLocations(maincat);

		try {
			con = getDBConnection();
			if (HW3.decide.equals("ANY")) {
				insertTableSQL = "SELECT DISTINCT CITY,STATE FROM BUSINESSCAT_MAP bm,BUSINESS b WHERE bm.MAINCAT IN('";
				Boolean isFirst = false;
				Boolean isSecond = false;
				int i = 0;
				for (String s : maincat) {

					if (isFirst) {
						insertTableSQL = insertTableSQL + ",'" + s + "'";
						i = 1;
					} else {
						insertTableSQL = insertTableSQL + s + "'";
					}
					isFirst = true;
				}
				insertTableSQL = insertTableSQL + ")";
				insertTableSQL = insertTableSQL + " AND SUBCAT IN('";

				for (String s : subcat) {

					if (isSecond) {
						insertTableSQL = insertTableSQL + ",'" + s + "'";
						i = 1;
					} else {
						insertTableSQL = insertTableSQL + s + "'";
					}
					isSecond = true;
				}
				insertTableSQL = insertTableSQL + ")";

				insertTableSQL = insertTableSQL + " AND bm.BUSINESS_ID=b.BUSINESS_ID ORDER BY CITY ASC";

			} else {
				insertTableSQL = "SELECT DISTINCT CITY,STATE FROM BUSINESS WHERE BUSINESS_ID IN(SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE MAINCAT='";
				Boolean isFirst = false;
				Boolean isSecond = false;
				int i = 0;
				for (String s : maincat) {

					if (isFirst) {
						insertTableSQL = insertTableSQL
								+ " INTERSECT SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE MAINCAT='" + s + "'";
						i = 1;
					} else {
						insertTableSQL = insertTableSQL + s + "'";
					}
					isFirst = true;
				}
				for (String s : subcat) {
					insertTableSQL = insertTableSQL
							+ " INTERSECT SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE SUBCAT='" + s + "'";
				}
				insertTableSQL = insertTableSQL + ") ORDER BY CITY ASC";
			}
			PreparedStatement preparedStmt = con.prepareStatement(insertTableSQL);
			System.out.println("This is our query:" + insertTableSQL);
			ResultSet rs = preparedStmt.executeQuery();

			while (rs.next()) {
				String str = rs.getString("CITY");
				String str1 = rs.getString("STATE");
				String t = str + "," + str1;
				res.add(t);
				System.out.println(t);
			}

		} finally {
			// Never forget to close database connection
			closeConnection(con);

		}
		System.out.println("location list created from main and sub category");
		return res;

	}

	public List<String> getLocations(List<String> maincat, List<String> subcat, List<String> attribute)
			throws SQLException {
		JSONParser parser = new JSONParser();
		List<String> res = new ArrayList<String>();
		Connection con = null;
		String insertTableSQL = "";
		if (attribute.isEmpty())
			return getLocations(maincat, subcat);
		if (subcat.isEmpty())
			return getLocations(maincat);
		try {
			con = getDBConnection();
			if (HW3.decide.equals("ANY")) {
				insertTableSQL = "SELECT DISTINCT b.CITY,b.STATE FROM BUSINESSCAT_MAP bm,ATTRIBUTEMAP am,BUSINESS b WHERE bm.MAINCAT IN('";
				Boolean isFirst = false;
				Boolean isSecond = false;
				Boolean isThird = false;
				int i = 0;
				for (String s : maincat) {

					if (isFirst) {
						insertTableSQL = insertTableSQL + ",'" + s + "'";
						i = 1;
					} else {
						insertTableSQL = insertTableSQL + s + "'";
					}
					isFirst = true;
				}
				insertTableSQL = insertTableSQL + ")";
				insertTableSQL = insertTableSQL + " AND SUBCAT IN('";

				for (String s : subcat) {

					if (isSecond) {
						insertTableSQL = insertTableSQL + ",'" + s + "'";
						i = 1;
					} else {
						insertTableSQL = insertTableSQL + s + "'";
					}
					isSecond = true;
				}
				insertTableSQL = insertTableSQL + ")";

				insertTableSQL = insertTableSQL + " AND am.ATTRIBUTESS IN('";

				for (String s : attribute) {

					if (isThird) {
						insertTableSQL = insertTableSQL + ",'" + s + "'";
						i = 1;
					} else {
						insertTableSQL = insertTableSQL + s + "'";
					}
					isThird = true;
				}
				insertTableSQL = insertTableSQL + ")";
				insertTableSQL = insertTableSQL + " AND bm.BUSINESS_ID=am.BUSINESS_ID AND am.BUSINESS_ID=B.BUSINESS_ID ORDER BY CITY ASC";
			} else {
				insertTableSQL = "SELECT DISTINCT CITY,STATE FROM BUSINESS WHERE BUSINESS_ID IN(";
				Boolean isFirst = false;
				Boolean isSecond = false;
				Boolean isThird = false;
				int i = 0;
				for (String s : maincat) {

					if (isFirst) {
						insertTableSQL = insertTableSQL
								+ " INTERSECT SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE MAINCAT='" + s + "'";
						i = 1;
					} else {
						insertTableSQL = insertTableSQL + " SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE MAINCAT='" + s
								+ "'";
					}
					isFirst = true;
				}
				for (String s : subcat) {
					insertTableSQL = insertTableSQL
							+ " INTERSECT SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE SUBCAT='" + s + "'";

				}
				for (String s : attribute) {
					insertTableSQL = insertTableSQL
							+ "INTERSECT SELECT BUSINESS_ID FROM ATTRIBUTEMAP WHERE ATTRIBUTESS='" + s + "'";

				}
				insertTableSQL = insertTableSQL + ") ORDER BY CITY ASC";
			}

			PreparedStatement preparedStmt = con.prepareStatement(insertTableSQL);
			System.out.println("This is our query:" + insertTableSQL);
			ResultSet rs = preparedStmt.executeQuery();

			while (rs.next()) {
				String str = rs.getString("CITY");
				String str1 = rs.getString("STATE");
				String t = str + "," + str1;
				res.add(t);
			}

		} finally {
			// Never forget to close database connection
			closeConnection(con);

		}
		System.out.println("location list created from main and sub category");
		return res;

	}

	public List<String> getDay(List<String> maincat) throws SQLException {
		JSONParser parser = new JSONParser();
		List<String> res = new ArrayList<String>();
		Connection con = null;
		String insertTableSQL = "";
		if (maincat.isEmpty())
			return new ArrayList<String>();
		try {
			con = getDBConnection();
			if (HW3.decide.equals("ANY")) {
				insertTableSQL = "SELECT DISTINCT o.DAY FROM BUSINESSCAT_MAP b,OPERATING_HOURS o WHERE b.MAINCAT IN('";
				Boolean isFirst = false;
				int i = 0;
				for (String s : maincat) {

					if (isFirst) {
						insertTableSQL = insertTableSQL + ",'" + s + "'";
						i = 1;
					} else {
						insertTableSQL = insertTableSQL + s + "'";
					}
					isFirst = true;
				}
				insertTableSQL = insertTableSQL + ")";
				insertTableSQL = insertTableSQL + " AND b.BUSINESS_ID=o.BUSINESS_ID ORDER BY o.DAY ASC";
			} else {
				insertTableSQL = "SELECT DISTINCT DAY FROM OPERATING_HOURS WHERE BUSINESS_ID IN(";
				Boolean isFirst = false;
				int i = 0;
				for (String s : maincat) {

					if (isFirst) {
						insertTableSQL = insertTableSQL
								+ " INTERSECT SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE MAINCAT='" + s + "'";
						i = 1;
					} else {
						insertTableSQL = insertTableSQL + " SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE MAINCAT='" + s
								+ "'";
					}
					isFirst = true;
				}
				insertTableSQL = insertTableSQL + ") ORDER BY DAY ASC";
			}
			PreparedStatement preparedStmt = con.prepareStatement(insertTableSQL);
			System.out.println("This is our query:" + insertTableSQL);
			ResultSet rs = preparedStmt.executeQuery();

			while (rs.next()) {
				String str = rs.getString("DAY");

				res.add(str);
			}

		} finally {
			// Never forget to close database connection
			closeConnection(con);

		}
		System.out.println("list created");
		return res;
	}

	public List<String> getDay(List<String> maincat, List<String> subcat) throws SQLException {

		JSONParser parser = new JSONParser();
		List<String> res = new ArrayList<String>();
		Connection con = null;
		String insertTableSQL = "";
		if (subcat.isEmpty())
			return getDay(maincat);

		try {
			con = getDBConnection();
			if (HW3.decide.equals("ANY")) {
				insertTableSQL = "SELECT DISTINCT DAY FROM BUSINESSCAT_MAP bm,OPERATING_HOURS o WHERE bm.MAINCAT IN('";
				Boolean isFirst = false;
				Boolean isSecond = false;
				int i = 0;
				for (String s : maincat) {

					if (isFirst) {
						insertTableSQL = insertTableSQL + ",'" + s + "'";
						i = 1;
					} else {
						insertTableSQL = insertTableSQL + s + "'";
					}
					isFirst = true;
				}
				insertTableSQL = insertTableSQL + ")";
				insertTableSQL = insertTableSQL + " AND bm.SUBCAT IN('";

				for (String s : subcat) {

					if (isSecond) {
						insertTableSQL = insertTableSQL + ",'" + s + "'";
						i = 1;
					} else {
						insertTableSQL = insertTableSQL + s + "'";
					}
					isSecond = true;
				}
				insertTableSQL = insertTableSQL + ")";

				insertTableSQL = insertTableSQL + " AND bm.BUSINESS_ID=o.BUSINESS_ID ORDER BY o.DAY ASC";
			} else {
				insertTableSQL = "SELECT DISTINCT DAY FROM OPERATING_HOURS WHERE BUSINESS_ID IN(";
				Boolean isFirst = false;
				Boolean isSecond = false;
				int i = 0;
				for (String s : maincat) {

					if (isFirst) {
						insertTableSQL = insertTableSQL
								+ " INTERSECT SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE MAINCAT='" + s + "'";
						i = 1;
					} else {
						insertTableSQL = insertTableSQL + " SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE MAINCAT='" + s
								+ "'";
						isFirst = true;
					}
				}
				for (String s : subcat) {

					insertTableSQL = insertTableSQL
							+ " INTERSECT SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE SUBCAT='" + s + "'";
				}
				insertTableSQL = insertTableSQL + ") ORDER BY DAY ASC";
			}

			PreparedStatement preparedStmt = con.prepareStatement(insertTableSQL);
			System.out.println("This is our query:" + insertTableSQL);
			ResultSet rs = preparedStmt.executeQuery();

			while (rs.next()) {
				String str = rs.getString("DAY");
				res.add(str);
			}

		} finally {
			// Never forget to close database connection
			closeConnection(con);

		}
		System.out.println("Day list created from main and sub category");
		return res;

	}

	public List<String> getDay(List<String> maincat, List<String> subcat, List<String> attribute) throws SQLException {

		JSONParser parser = new JSONParser();
		List<String> res = new ArrayList<String>();
		Connection con = null;
		String insertTableSQL = "";
		if (attribute.isEmpty())
			return getDay(maincat, subcat);

		try {
			con = getDBConnection();
			if (HW3.decide.equals("ANY")) {
				insertTableSQL = "SELECT DISTINCT o.DAY FROM BUSINESSCAT_MAP bm,ATTRIBUTEMAP am,OPERATING_HOURS o WHERE bm.MAINCAT IN('";
				Boolean isFirst = false;
				Boolean isSecond = false;
				Boolean isThird = false;
				int i = 0;
				for (String s : maincat) {

					if (isFirst) {
						insertTableSQL = insertTableSQL + ",'" + s + "'";
						i = 1;
					} else {
						insertTableSQL = insertTableSQL + s + "'";
					}
					isFirst = true;
				}
				insertTableSQL = insertTableSQL + ")";
				insertTableSQL = insertTableSQL + " AND bm.SUBCAT IN('";

				for (String s : subcat) {

					if (isSecond) {
						insertTableSQL = insertTableSQL + ",'" + s + "'";
						i = 1;
					} else {
						insertTableSQL = insertTableSQL + s + "'";
					}
					isSecond = true;
				}
				insertTableSQL = insertTableSQL + ")";

				insertTableSQL = insertTableSQL + " AND am.ATTRIBUTESS IN('";

				for (String s : attribute) {

					if (isThird) {
						insertTableSQL = insertTableSQL + ",'" + s + "'";
						i = 1;
					} else {
						insertTableSQL = insertTableSQL + s + "'";
					}
					isThird = true;
				}
				insertTableSQL = insertTableSQL + ")";
				insertTableSQL = insertTableSQL + " AND bm.BUSINESS_ID=am.BUSINESS_ID AND am.BUSINESS_ID=o.BUSINESS_ID ORDER BY o.DAY ASC";
			} else {
				insertTableSQL = "SELECT DISTINCT DAY FROM OPERATING_HOURS WHERE BUSINESS_ID IN(";
				Boolean isFirst = false;
				Boolean isSecond = false;
				int i = 0;
				for (String s : maincat) {

					if (isFirst) {
						insertTableSQL = insertTableSQL
								+ " INTERSECT SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE MAINCAT='" + s + "'";
						i = 1;
					} else {
						insertTableSQL = insertTableSQL + " SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE MAINCAT='" + s
								+ "'";
						isFirst = true;
					}
				}
				for (String s : subcat) {

					insertTableSQL = insertTableSQL
							+ " INTERSECT SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE SUBCAT='" + s + "'";
				}
				for (String s : attribute) {

					insertTableSQL = insertTableSQL
							+ " INTERSECT SELECT BUSINESS_ID FROM ATTRIBUTEMAP WHERE ATTRIBUTESS='" + s + "'";
				}
				insertTableSQL = insertTableSQL + ") ORDER BY DAY ASC";
			}
			PreparedStatement preparedStmt = con.prepareStatement(insertTableSQL);
			System.out.println("This is our query:" + insertTableSQL);
			ResultSet rs = preparedStmt.executeQuery();

			while (rs.next()) {
				String str = rs.getString("DAY");
				res.add(str);
			}
		} finally {
			// Never forget to close database connection
			closeConnection(con);

		}
		System.out.println("Day list created from main and sub category");
		return res;

	}

	public List<String> getFrom(List<String> maincat) throws SQLException {
		JSONParser parser = new JSONParser();
		List<String> res = new ArrayList<String>();
		Connection con = null;
		String insertTableSQL = "";
		if (maincat.isEmpty())
			return new ArrayList<String>();
		try {
			con = getDBConnection();
			if (HW3.decide.equals("ANY")) {
				insertTableSQL = "SELECT DISTINCT OPENHOUR,OPENMIN FROM BUSINESSCAT_MAP b,OPERATING_HOURS o WHERE b.MAINCAT IN('";
				Boolean isFirst = false;
				int i = 0;
				for (String s : maincat) {

					if (isFirst) {
						insertTableSQL = insertTableSQL + ",'" + s + "'";
						i = 1;
					} else {
						insertTableSQL = insertTableSQL + s + "'";
					}
					isFirst = true;
				}
				insertTableSQL = insertTableSQL + ")";
				insertTableSQL = insertTableSQL + " AND b.BUSINESS_ID=o.BUSINESS_ID order by o.OPENHOUR,o.OPENMIN ASC";
			} else {
				insertTableSQL = "SELECT DISTINCT OPENHOUR,OPENMIN FROM OPERATING_HOURS WHERE BUSINESS_ID IN(";
				Boolean isFirst = false;
				int i = 0;
				for (String s : maincat) {

					if (isFirst) {
						insertTableSQL = insertTableSQL
								+ " INTERSECT SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE MAINCAT='" + s + "'";
						i = 1;
					} else {
						insertTableSQL = insertTableSQL + " SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE MAINCAT='" + s
								+ "'";
					}
					isFirst = true;
				}
				insertTableSQL = insertTableSQL + ") ORDER BY OPENHOUR ASC";
			}
			PreparedStatement preparedStmt = con.prepareStatement(insertTableSQL);
			System.out.println("This is our query:" + insertTableSQL);
			ResultSet rs = preparedStmt.executeQuery();

			while (rs.next()) {
				String str = rs.getString("OPENHOUR");
				if (str.equals("0"))
					str = "00";
				String str1 = rs.getString("OPENMIN");
				if (str1.equals("0"))
					str1 = "00";
				String t = str + ":" + str1;
				if (t.equals("00:00"))
					t = "24x7";
				res.add(t);
			}

		} finally {
			// Never forget to close database connection
			closeConnection(con);

		}
		System.out.println("list created");
		return res;
	}

	public List<String> getFrom(List<String> maincat, List<String> subcat) throws SQLException {

		JSONParser parser = new JSONParser();
		List<String> res = new ArrayList<String>();
		Connection con = null;
		String insertTableSQL = "";
		if (subcat.isEmpty())
			return getFrom(maincat);

		try {
			con = getDBConnection();
			if (HW3.decide.equals("ANY")) {
				insertTableSQL = "SELECT DISTINCT OPENHOUR,OPENMIN FROM BUSINESSCAT_MAP bm,OPERATING_HOURS o WHERE bm.MAINCAT IN('";
				Boolean isFirst = false;
				Boolean isSecond = false;
				int i = 0;
				for (String s : maincat) {

					if (isFirst) {
						insertTableSQL = insertTableSQL + ",'" + s + "'";
						i = 1;
					} else {
						insertTableSQL = insertTableSQL + s + "'";
					}
					isFirst = true;
				}
				insertTableSQL = insertTableSQL + ")";
				insertTableSQL = insertTableSQL + " AND bm.SUBCAT IN('";

				for (String s : subcat) {

					if (isSecond) {
						insertTableSQL = insertTableSQL + ",'" + s + "'";
						i = 1;
					} else {
						insertTableSQL = insertTableSQL + s + "'";
					}
					isSecond = true;
				}
				insertTableSQL = insertTableSQL + ")";

				insertTableSQL = insertTableSQL + " AND bm.BUSINESS_ID=o.BUSINESS_ID";
			} else {
				insertTableSQL = "SELECT DISTINCT OPENHOUR,OPENMIN FROM OPERATING_HOURS WHERE BUSINESS_ID IN(";
				Boolean isFirst = false;
				int i = 0;
				for (String s : maincat) {

					if (isFirst) {
						insertTableSQL = insertTableSQL
								+ " INTERSECT SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE MAINCAT='" + s + "'";
						i = 1;
					} else {
						insertTableSQL = insertTableSQL + " SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE MAINCAT='" + s
								+ "'";
					}
					isFirst = true;
				}
				for (String s : subcat) {
					insertTableSQL = insertTableSQL
							+ " INTERSECT SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE SUBCAT='" + s + "'";
				}
				insertTableSQL = insertTableSQL + ") ORDER BY OPENHOUR ASC";
			}

			PreparedStatement preparedStmt = con.prepareStatement(insertTableSQL);
			System.out.println("This is our query:" + insertTableSQL);
			ResultSet rs = preparedStmt.executeQuery();

			while (rs.next()) {
				String str = rs.getString("OPENHOUR");
				if (str.equals("0"))
					str = "00";
				String str1 = rs.getString("OPENMIN");
				if (str1.equals("0"))
					str1 = "00";
				String t = str + ":" + str1;
				if (t.equals("00:00"))
					t = "24x7";
				res.add(t);
			}

		} finally {
			// Never forget to close database connection
			closeConnection(con);

		}
		System.out.println("from time list created from main and sub category");
		return res;

	}

	public List<String> getFrom(List<String> maincat, List<String> subcat, List<String> attribute) throws SQLException {

		JSONParser parser = new JSONParser();
		List<String> res = new ArrayList<String>();
		Connection con = null;
		String insertTableSQL = "";
		if (attribute.isEmpty())
			return getFrom(maincat, subcat);

		try {
			con = getDBConnection();
			if (HW3.decide.equals("ANY")) {
				insertTableSQL = "SELECT DISTINCT o.OPENHOUR,o.OPENMIN FROM BUSINESSCAT_MAP bm,ATTRIBUTEMAP am,OPERATING_HOURS o WHERE bm.MAINCAT IN('";
				Boolean isFirst = false;
				Boolean isSecond = false;
				Boolean isThird = false;
				int i = 0;
				for (String s : maincat) {

					if (isFirst) {
						insertTableSQL = insertTableSQL + ",'" + s + "'";
						i = 1;
					} else {
						insertTableSQL = insertTableSQL + s + "'";
					}
					isFirst = true;
				}
				insertTableSQL = insertTableSQL + ")";
				insertTableSQL = insertTableSQL + " AND bm.SUBCAT IN('";

				for (String s : subcat) {

					if (isSecond) {
						insertTableSQL = insertTableSQL + ",'" + s + "'";
						i = 1;
					} else {
						insertTableSQL = insertTableSQL + s + "'";
					}
					isSecond = true;
				}
				insertTableSQL = insertTableSQL + ")";

				insertTableSQL = insertTableSQL + " AND am.ATTRIBUTESS IN('";

				for (String s : attribute) {

					if (isThird) {
						insertTableSQL = insertTableSQL + ",'" + s + "'";
						i = 1;
					} else {
						insertTableSQL = insertTableSQL + s + "'";
					}
					isThird = true;
				}
				insertTableSQL = insertTableSQL + ")";
				insertTableSQL = insertTableSQL + " AND bm.BUSINESS_ID=am.BUSINESS_ID AND am.BUSINESS_ID=o.BUSINESS_ID";
			} else {
				insertTableSQL = "SELECT DISTINCT OPENHOUR,OPENMIN FROM OPERATING_HOURS WHERE BUSINESS_ID IN(";
				Boolean isFirst = false;
				int i = 0;
				for (String s : maincat) {

					if (isFirst) {
						insertTableSQL = insertTableSQL
								+ " INTERSECT SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE MAINCAT='" + s + "'";
						i = 1;
					} else {
						insertTableSQL = insertTableSQL + " SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE MAINCAT='" + s
								+ "'";
					}
					isFirst = true;
				}
				for (String s : subcat) {
					insertTableSQL = insertTableSQL
							+ " INTERSECT SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE SUBCAT='" + s + "'";
				}
				for (String s : attribute) {
					insertTableSQL = insertTableSQL
							+ " INTERSECT SELECT BUSINESS_ID FROM ATTRIBUTEMAP WHERE ATTRIBUTESS='" + s + "'";
				}
				insertTableSQL = insertTableSQL + ") ORDER BY OPENHOUR ASC";
			}
			PreparedStatement preparedStmt = con.prepareStatement(insertTableSQL);
			System.out.println("This is our query:" + insertTableSQL);
			ResultSet rs = preparedStmt.executeQuery();

			while (rs.next()) {
				String str = rs.getString("OPENHOUR");
				if (str.equals("0"))
					str = "00";
				String str1 = rs.getString("OPENMIN");
				if (str1.equals("0"))
					str1 = "00";
				String t = str + ":" + str1;
				if (t.equals("00:00"))
					t = "24x7";
				res.add(t);
			}

		} finally {
			// Never forget to close database connection
			closeConnection(con);

		}
		System.out.println("from time list created from main and sub category");
		return res;

	}

	public List<String> getTo(List<String> maincat) throws SQLException {
		JSONParser parser = new JSONParser();
		List<String> res = new ArrayList<String>();
		Connection con = null;
		String insertTableSQL="";
		if (maincat.isEmpty())
			return new ArrayList<String>();
		try {
			con = getDBConnection();
			if(HW3.decide.endsWith("ANY"))
			{
			insertTableSQL = "SELECT DISTINCT o.CLOSEHOUR,o.CLOSEMIN FROM BUSINESSCAT_MAP b,OPERATING_HOURS o WHERE b.MAINCAT IN('";
			Boolean isFirst = false;
			int i = 0;
			for (String s : maincat) {

				if (isFirst) {
					insertTableSQL = insertTableSQL + ",'" + s + "'";
					i = 1;
				} else {
					insertTableSQL = insertTableSQL + s + "'";
				}
				isFirst = true;
			}
			insertTableSQL = insertTableSQL + ")";
			insertTableSQL = insertTableSQL + " AND b.BUSINESS_ID=o.BUSINESS_ID order by o.CLOSEHOUR,o.CLOSEMIN ASC";
			}
			else {
				insertTableSQL = "SELECT DISTINCT CLOSEHOUR,CLOSEMIN FROM OPERATING_HOURS WHERE BUSINESS_ID IN(";
				Boolean isFirst = false;
				int i = 0;
				for (String s : maincat) {

					if (isFirst) {
						insertTableSQL = insertTableSQL
								+ " INTERSECT SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE MAINCAT='" + s + "'";
						i = 1;
					} else {
						insertTableSQL = insertTableSQL + " SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE MAINCAT='" + s
								+ "'";
					}
					isFirst = true;
				}
				insertTableSQL = insertTableSQL + ") ORDER BY CLOSEHOUR ASC";
			}
			PreparedStatement preparedStmt = con.prepareStatement(insertTableSQL);
			System.out.println("This is our query:" + insertTableSQL);
			ResultSet rs = preparedStmt.executeQuery();

			while (rs.next()) {
				String str = rs.getString("CLOSEHOUR");
				if (str.equals("0"))
					str = "00";
				String str1 = rs.getString("CLOSEMIN");
				if (str1.equals("0"))
					str1 = "00";
				String t = str + ":" + str1;
				if (t.equals("00:00"))
					t = "24x7";
				res.add(t);
			}

		} finally {
			// Never forget to close database connection
			closeConnection(con);

		}
		System.out.println("list created");
		return res;
	}

	public List<String> getTo(List<String> maincat, List<String> subcat) throws SQLException {

		JSONParser parser = new JSONParser();
		List<String> res = new ArrayList<String>();
		Connection con = null;
		String insertTableSQL="";
		if (subcat.isEmpty())
			return getFrom(maincat);

		try {
			con = getDBConnection();
			if(HW3.decide.equals("ANY"))
			{
			insertTableSQL = "SELECT DISTINCT CLOSEHOUR,CLOSEMIN FROM BUSINESSCAT_MAP bm,OPERATING_HOURS o WHERE bm.MAINCAT IN('";
			Boolean isFirst = false;
			Boolean isSecond = false;
			int i = 0;
			for (String s : maincat) {

				if (isFirst) {
					insertTableSQL = insertTableSQL + ",'" + s + "'";
					i = 1;
				} else {
					insertTableSQL = insertTableSQL + s + "'";
				}
				isFirst = true;
			}
			insertTableSQL = insertTableSQL + ")";
			insertTableSQL = insertTableSQL + " AND bm.SUBCAT IN('";

			for (String s : subcat) {

				if (isSecond) {
					insertTableSQL = insertTableSQL + ",'" + s + "'";
					i = 1;
				} else {
					insertTableSQL = insertTableSQL + s + "'";
				}
				isSecond = true;
			}
			insertTableSQL = insertTableSQL + ")";

			insertTableSQL = insertTableSQL + " AND bm.BUSINESS_ID=o.BUSINESS_ID";
			}
			else {
				insertTableSQL = "SELECT DISTINCT CLOSEHOUR,CLOSEMIN FROM OPERATING_HOURS WHERE BUSINESS_ID IN(";
				Boolean isFirst = false;
				int i = 0;
				for (String s : maincat) {

					if (isFirst) {
						insertTableSQL = insertTableSQL
								+ " INTERSECT SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE MAINCAT='" + s + "'";
						i = 1;
					} else {
						insertTableSQL = insertTableSQL + " SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE MAINCAT='" + s
								+ "'";
					}
					isFirst = true;
				}
				for (String s : subcat) {
					insertTableSQL = insertTableSQL
							+ " INTERSECT SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE SUBCAT='" + s + "'";
				}
				insertTableSQL = insertTableSQL + ") ORDER BY CLOSEHOUR ASC";
			}

			PreparedStatement preparedStmt = con.prepareStatement(insertTableSQL);
			System.out.println("This is our query:" + insertTableSQL);
			ResultSet rs = preparedStmt.executeQuery();

			while (rs.next()) {
				String str = rs.getString("CLOSEHOUR");
				if (str.equals("0"))
					str = "00";
				String str1 = rs.getString("CLOSEMIN");
				if (str1.equals("0"))
					str1 = "00";
				String t = str + ":" + str1;
				if (t.equals("00:00"))
					t = "00:00";
				res.add(t);
			}

		} finally {
			// Never forget to close database connection
			closeConnection(con);

		}
		System.out.println("to time list created from main and sub category");
		return res;

	}

	public List<String> getTo(List<String> maincat, List<String> subcat, List<String> attribute) throws SQLException {

		JSONParser parser = new JSONParser();
		List<String> res = new ArrayList<String>();
		Connection con = null;
		String insertTableSQL="";
		if (attribute.isEmpty())
			return getTo(maincat, subcat);

		try {
			con = getDBConnection();
			if(HW3.decide.equals("ANY"))
			{
			insertTableSQL = "SELECT DISTINCT o.CLOSEHOUR,o.CLOSEMIN FROM BUSINESSCAT_MAP bm,ATTRIBUTEMAP am,OPERATING_HOURS o WHERE bm.MAINCAT IN('";
			Boolean isFirst = false;
			Boolean isSecond = false;
			Boolean isThird = false;
			int i = 0;
			for (String s : maincat) {

				if (isFirst) {
					insertTableSQL = insertTableSQL + ",'" + s + "'";
					i = 1;
				} else {
					insertTableSQL = insertTableSQL + s + "'";
				}
				isFirst = true;
			}
			insertTableSQL = insertTableSQL + ")";
			insertTableSQL = insertTableSQL + " AND bm.SUBCAT IN('";

			for (String s : subcat) {

				if (isSecond) {
					insertTableSQL = insertTableSQL + ",'" + s + "'";
					i = 1;
				} else {
					insertTableSQL = insertTableSQL + s + "'";
				}
				isSecond = true;
			}
			insertTableSQL = insertTableSQL + ")";

			insertTableSQL = insertTableSQL + " AND am.ATTRIBUTESS IN('";

			for (String s : attribute) {

				if (isThird) {
					insertTableSQL = insertTableSQL + ",'" + s + "'";
					i = 1;
				} else {
					insertTableSQL = insertTableSQL + s + "'";
				}
				isThird = true;
			}
			insertTableSQL = insertTableSQL + ")";
			insertTableSQL = insertTableSQL + " AND bm.BUSINESS_ID=am.BUSINESS_ID AND am.BUSINESS_ID=o.BUSINESS_ID";
			}
			else {
				insertTableSQL = "SELECT DISTINCT CLOSEHOUR,CLOSEMIN FROM OPERATING_HOURS WHERE BUSINESS_ID IN(";
				Boolean isFirst = false;
				int i = 0;
				for (String s : maincat) {

					if (isFirst) {
						insertTableSQL = insertTableSQL
								+ " INTERSECT SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE MAINCAT='" + s + "'";
						i = 1;
					} else {
						insertTableSQL = insertTableSQL + " SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE MAINCAT='" + s
								+ "'";
					}
					isFirst = true;
				}
				for (String s : subcat) {
					insertTableSQL = insertTableSQL
							+ " INTERSECT SELECT BUSINESS_ID FROM BUSINESSCAT_MAP WHERE SUBCAT='" + s + "'";
				}
				for (String s : attribute) {
					insertTableSQL = insertTableSQL
							+ " INTERSECT SELECT BUSINESS_ID FROM ATTRIBUTEMAP WHERE ATTRIBUTESS='" + s + "'";
				}
				insertTableSQL = insertTableSQL + ") ORDER BY CLOSEHOUR ASC";
			}
			PreparedStatement preparedStmt = con.prepareStatement(insertTableSQL);
			System.out.println("This is our query:" + insertTableSQL);
			ResultSet rs = preparedStmt.executeQuery();

			while (rs.next()) {
				String str = rs.getString("CLOSEHOUR");
				if (str.equals("0"))
					str = "00";
				String str1 = rs.getString("CLOSEMIN");
				if (str1.equals("0"))
					str1 = "00";
				String t = str + ":" + str1;
				if (t.equals("00:00"))
					t = "23:59";
				res.add(t);
			}

		} finally {
			// Never forget to close database connection
			closeConnection(con);

		}
		System.out.println("from time list created from main and sub category");
		return res;

	}

}
