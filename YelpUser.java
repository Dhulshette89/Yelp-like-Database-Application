import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;



public class YelpUser {
	public String getYelping_since() {
		return yelping_since;
	}
	public void setYelping_since(String yelping_since) {
		this.yelping_since = yelping_since;
	}
	public Long getReview_count() {
		return review_count;
	}



	public void setReview_count(Long review_count) {
		this.review_count = review_count;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getUser_id() {
		return user_id;
	}



	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}



	public List<String> getFriends() {
		return friends;
	}



	public void setFriends(List<String> friends) {
		this.friends = friends;
	}



	public Long getFans() {
		return fans;
	}



	public void setFans(Long fans) {
		this.fans = fans;
	}



	public Double getAverage_stars() {
		return average_stars;
	}



	public void setAverage_stars(Double average_stars) {
		this.average_stars = average_stars;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}

	public List<String> getElite() {
		return elite;
	}



	public void setElite(List<String> elite) {
		this.elite = elite;
	}



	public int getCount() {
		return count;
	}



	public void setCount(int count) {
		this.count = count;
	}
	public Long getVoted_funny() {
		return voted_funny;
	}



	public void setVoted_funny(Long voted_funny) {
		this.voted_funny = voted_funny;
	}



	public Long getVoted_cool() {
		return voted_cool;
	}



	public void setVoted_cool(Long voted_cool) {
		this.voted_cool = voted_cool;
	}



	public Long getVoted_useful() {
		return voted_useful;
	}



	public void setVoted_useful(Long voted_useful) {
		this.voted_useful = voted_useful;
	}


	String yelping_since;
	Long review_count;
	String name;
	String user_id;
	Long voted_funny;
	Long voted_cool;
	Long voted_useful;
	List<String> friends=new<String>ArrayList();
	Long fans;
	Double average_stars;
	String type;
	List<String> elite=new<String>ArrayList();
	int count=0;
	
	public String getInsertQuery() {
		// TODO Auto-generated method stub
		
		String s="insert into Yelp_User values(TO_DATE('"+yelping_since+"','YYYY-MM'),'"+voted_funny+"','"+
					voted_useful+"','"+voted_cool+"','"+review_count+"','"+name+"','"+user_id+"','"+
					fans+"','"+average_stars+"','"+type+"')";
		return s;
	}
	
	public static String createUserConstruct()
	{	
		String create="CREATE TABLE YELP_USER"+
				"(YELPING_SINCE DATE NOT NULL,"+
				"VOTED_FUNNY NUMBER(32),"+
				"VOTED_USEFUL NUMBER(32),"+
				"VOTED_COOL NUMBER(32),"+
				"REVIEW_COUNT NUMBER(32),"+
				"USER_NAME VARCHAR2(100) NOT NULL,"+
				"USER_ID VARCHAR2(100) PRIMARY KEY,"+
				"Number_OfFriends Number(4),"+
				"FANS NUMBER(32),"+
				"AVERAGE_STARS NUMBER(5,3),"+
				"USER_TYPE VARCHAR(30) NOT NULL)";
		
		return create;
		
	}
}