import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import oracle.jdbc.OraclePreparedStatement;

public class ReviewThread {
	List<String> list;
	int id;

	ReviewThread(List<String> list, int id) {
		this.list = list;
		this.id = id;
	}

	public void execute(Connection con) {
		try {
			JSONParser parser = new JSONParser();

			Object obj;
			String strLine;

			int count = 0;
			String insertTableSQL = "INSERT INTO REVIEW"
					+ "(VOTESFUNNY,VOTESUSEFUL,VOTESCOOL,USER_ID,REVIEW_ID,STARS,RDATE,TEXT,BUSINESS_ID)"
					+ "VALUES(?,?,?,?,?,?,?,?,?)";
			//Connection con = Populate.getDBConnection();
			//con.setAutoCommit(false);
			OraclePreparedStatement preparedStmt =(OraclePreparedStatement) con.prepareStatement(insertTableSQL);
			for (String line : list) {
				// java.sql.Clob clob = null;
			//	System.out.println("Inside thread:" + id);
				obj = parser.parse(line);

				JSONObject jsonObject = (JSONObject) obj;

				/*
				 * "text":
				 * "dr. goldberg offers everything i look for in a general practitioner,"
				 */
				
				Map address = ((Map) jsonObject.get("votes"));
				Long funny = null;
				Long useful = null;
				Long cool = null;
				// iterating address Map
				Iterator<Map.Entry> itr1 = address.entrySet().iterator();
				while (itr1.hasNext()) {
					Map.Entry pair = itr1.next();
					if (pair.getKey().equals("funny"))
						funny = (Long) pair.getValue();
					if (pair.getKey().equals("cool"))
						cool = (Long) pair.getValue();
					if (pair.getKey().equals("useful"))
						useful = (Long) pair.getValue();
				}
				String userid = (String) jsonObject.get("user_id");
				String reviewid = (String) jsonObject.get("review_id");
				Long stars = (Long) jsonObject.get("stars");
				String date = (String) jsonObject.get("date");

				String text = (String) jsonObject.get("text");
				String bid = (String) jsonObject.get("business_id");
				/*Clob clob = con.createClob();
				clob.setString(1, text);*/
				preparedStmt.setLong(1, funny);
				preparedStmt.setLong(2, useful);
				preparedStmt.setLong(3, cool);
				preparedStmt.setString(4, userid.trim());
				preparedStmt.setString(5, reviewid.trim());
				preparedStmt.setLong(6, stars);
				preparedStmt.setDate(7, Populate.getCurrentJavaSqlDate(date, 0));

				ByteArrayInputStream inputStream = new ByteArrayInputStream(text.getBytes());
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				preparedStmt.setClob(8, inputStreamReader);
				
				preparedStmt.setString(9, bid);
				preparedStmt.addBatch();


			}
			preparedStmt.executeBatch();
			preparedStmt.close();
			//con.commit();
			//Populate.closeConnection(con);

		} catch (Exception e) {
			// Throwing an exception
			e.printStackTrace();
			System.out.println("Exception is caught");
		}
	}

}
