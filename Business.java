import java.util.ArrayList;
import java.util.List;

public class Business {
	
	public static List<String> categoryList=new ArrayList<>();
	
	public static void initiateCat()
	{
		categoryList.add("Active Life");
		categoryList.add("Arts & Entertainment");
		categoryList.add("Automotive");
		categoryList.add("Car Rental");
		categoryList.add("Cafes");
		categoryList.add("Beauty & Spas");
		categoryList.add("Convenience Stores");
		categoryList.add("Dentists");
		categoryList.add("Doctors");
		categoryList.add("Drugstores");
		categoryList.add("Department Stores");
		categoryList.add("Education");
		categoryList.add("Event Planning & Services");
		categoryList.add("Flowers & Gifts");
		categoryList.add("Food");
		categoryList.add("Health & Medical");
		categoryList.add("Home Services");
		categoryList.add("Home & Garden");
		categoryList.add("Hospitals");
		categoryList.add("Hotels & Travel");
		
		categoryList.add("Hardware Stores");
		categoryList.add("Grocery");
		categoryList.add("Medical Centers");
		
		categoryList.add("Nurseries & Gardening");
		categoryList.add("Nightlife");
		
		categoryList.add("Restaurants");
		categoryList.add("Shopping");
		categoryList.add("Transportation");
		
	
		
	}

	public static String createBusiness()
	{	initiateCat();
		
		String create="CREATE TABLE BUSINESS"+
			"(BUSINESS_ID VARCHAR(200) PRIMARY KEY,"+
			"ADDRESS VARCHAR(400) NOT NULL,"+
			"CITY VARCHAR(150) NOT NULL,"+
			"STATE VARCHAR(150) NOT NULL,"+
			"REVIEW_COUNT NUMBER(30) NOT NULL,"+
			"BUSINESS_NAME VARCHAR(200) NOT NULL,"+
			"STARS NUMBER(5,1) NOT NULL,"+
			"TYPE VARCHAR(200))";
	
		return create;

		
	}
	
	public static String createCheckIn()
	{
		String create="CREATE TABLE CHECKIN"+
				"(BUSINESS_ID VARCHAR(200) NOT NULL,"+
				"TOTCHECKIN NUMBER(30) NOT NULL,"+
				"FOREIGN KEY(BUSINESS_ID) REFERENCES BUSINESS(BUSINESS_ID) ON DELETE CASCADE)";
		
			return create;
	}
	public static String createOperatingHours()
	{
		String create="CREATE TABLE OPERATING_HOURS"+
				"(BUSINESS_ID VARCHAR(200) NOT NULL,"+
				"DAY VARCHAR(20) CHECK(DAY IN('Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sunday')),"+
				"OPENHOUR NUMBER(2) NOT NULL,"+
				"OPENMIN VARCHAR(20) NOT NULL,"+
				"CLOSEHOUR NUMBER(2) NOT NULL,"+
				"CLOSEMIN NUMBER(2) NOT NULL,"+
				"FOREIGN KEY(BUSINESS_ID) REFERENCES BUSINESS(BUSINESS_ID) ON DELETE CASCADE)";
		
			return create;
	}
	public static String createBusinessCatMap()
	{
		String create="CREATE TABLE BUSINESSCAT_MAP"+
				"(BUSINESS_ID VARCHAR(200) NOT NULL,"+
				"MAINCAT VARCHAR(200) NOT NULL,"+
				"SUBCAT VARCHAR(200),"+
				"FOREIGN KEY(BUSINESS_ID) REFERENCES BUSINESS(BUSINESS_ID) ON DELETE CASCADE)";
		
			return create;
	}
	
	public static String createAttributeMap()
	{
		String create="CREATE TABLE ATTRIBUTEMAP"+
				"(BUSINESS_ID VARCHAR(200) NOT NULL,"+
				"ATTRIBUTESS VARCHAR(1500) NOT NULL,"+
				"FOREIGN KEY(BUSINESS_ID) REFERENCES BUSINESS(BUSINESS_ID) ON DELETE CASCADE)";
		
			return create;	
		
	}
	
	public static String createCatMap()
	{
		String create="CREATE TABLE CAT_MAP"+
				"(MAINCAT VARCHAR(200) NOT NULL,"+
				"SUBCAT VARCHAR(200) NOT NULL,"+
				"PRIMARY KEY(MAINCAT,SUBCAT))";
		
			return create;
	}
	public static String insertCatmap()
	{
		String create="INSERT INTO CAT_MAP "+
				"SELECT DISTINCT MAINCAT,SUBCAT "+
				"FROM BUSINESSCAT_MAP b "+
				"WHERE b.SUBCAT is not null";
		
			return create;
	}
	public static String insertReview()
	{
		String create="CREATE TABLE REVIEW "+
				"(VOTESFUNNY NUMBER(30) NOT NULL,"+
				"VOTESUSEFUL NUMBER(30) NOT NULL,"+
				"VOTESCOOL NUMBER(30) NOT NULL,"+
				"USER_ID VARCHAR(150) NOT NULL,"+
				"REVIEW_ID VARCHAR(200) PRIMARY KEY,"+
				"STARS NUMBER(2) NOT NULL,"+
				"RDATE DATE NOT NULL,"+
				"TEXT CLOB NOT NULL,"+
				"BUSINESS_ID VARCHAR(150) NOT NULL,"+
				"FOREIGN KEY(USER_ID) REFERENCES YELP_USER(USER_ID) ON DELETE CASCADE,"+
				"FOREIGN KEY(BUSINESS_ID) REFERENCES BUSINESS(BUSINESS_ID) ON DELETE CASCADE)";
		
			return create;
	}
	
	
}
