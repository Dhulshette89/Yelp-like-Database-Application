CREATE TABLE YELP_USER(YELPING_SINCE DATE NOT NULL,
				VOTED_FUNNY NUMBER(32),
				VOTED_USEFUL NUMBER(32),
				VOTED_COOL NUMBER(32),
				REVIEW_COUNT NUMBER(32),
				USER_NAME VARCHAR2(100) NOT NULL,
				USER_ID VARCHAR2(100) PRIMARY KEY,
				Number_OfFriends Number(4),
				FANS NUMBER(32),
				AVERAGE_STARS NUMBER(5,3),
				USER_TYPE VARCHAR(30) NOT NULL);
                
CREATE TABLE BUSINESS(BUSINESS_ID VARCHAR(200) PRIMARY KEY,
			ADDRESS VARCHAR(400) NOT NULL,
			CITY VARCHAR(150) NOT NULL,
			STATE VARCHAR(150) NOT NULL,
			REVIEW_COUNT NUMBER(30) NOT NULL,
			BUSINESS_NAME VARCHAR(200) NOT NULL,
			STARS NUMBER(5,1) NOT NULL,
			TYPE VARCHAR(200));

CREATE TABLE ATTRIBUTEMAP(BUSINESS_ID VARCHAR(200) NOT NULL,
				ATTRIBUTESS VARCHAR(1500) NOT NULL,
				FOREIGN KEY(BUSINESS_ID) REFERENCES BUSINESS(BUSINESS_ID) ON DELETE CASCADE);
                

CREATE TABLE BUSINESSCAT_MAP(BUSINESS_ID VARCHAR(200) NOT NULL,
				MAINCAT VARCHAR(200) NOT NULL,
				SUBCAT VARCHAR(200),
				FOREIGN KEY(BUSINESS_ID) REFERENCES BUSINESS(BUSINESS_ID) ON DELETE CASCADE);
                
                
CREATE TABLE CHECKIN(BUSINESS_ID VARCHAR(200) NOT NULL,
				TOTCHECKIN NUMBER(30) NOT NULL,
				FOREIGN KEY(BUSINESS_ID) REFERENCES BUSINESS(BUSINESS_ID) ON DELETE CASCADE);
                
CREATE TABLE OPERATING_HOURS(BUSINESS_ID VARCHAR(200) NOT NULL,
			DAY VARCHAR(20) CHECK(DAY IN('Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sunday')),
			OPENHOUR NUMBER(2) NOT NULL,
			OPENMIN VARCHAR(20) NOT NULL,
			CLOSEHOUR NUMBER(2) NOT NULL,
			CLOSEMIN NUMBER(2) NOT NULL,
			FOREIGN KEY(BUSINESS_ID) REFERENCES BUSINESS(BUSINESS_ID) ON DELETE CASCADE);
            

CREATE TABLE REVIEW(VOTESFUNNY NUMBER(30) NOT NULL,
				VOTESUSEFUL NUMBER(30) NOT NULL,
				VOTESCOOL NUMBER(30) NOT NULL,
				USER_ID VARCHAR(150) NOT NULL,
				REVIEW_ID VARCHAR(200) PRIMARY KEY,
				STARS NUMBER(2) NOT NULL,
				RDATE DATE NOT NULL,
				TEXT CLOB NOT NULL,
				BUSINESS_ID VARCHAR(150) NOT NULL,
				FOREIGN KEY(USER_ID) REFERENCES YELP_USER(USER_ID) ON DELETE CASCADE,
				FOREIGN KEY(BUSINESS_ID) REFERENCES BUSINESS(BUSINESS_ID) ON DELETE CASCADE);
                
                

CREATE INDEX attrimap_bindex ON ATTRIBUTEMAP(BUSINESS_ID);

CREATE INDEX attrimap_attrindex ON ATTRIBUTEMAP(ATTRIBUTESS);

CREATE INDEX bid_bmap ON BUSINESSCAT_MAP(BUSINESS_ID);

CREATE INDEX maincat_bmap ON BUSINESSCAT_MAP(MAINCAT);

CREATE INDEX subcatcat_bmap ON BUSINESSCAT_MAP(SUBCAT);

CREATE INDEX businessid_rv ON REVIEW(BUSINESS_ID);

CREATE INDEX businessid_oh ON OPERATING_HOURS(BUSINESS_ID);

CREATE INDEX businessid_ch ON CHECKIN(BUSINESS_ID);