[] Readme []
Written by Reed McCartney

Files Contained:
CardSwipeSecurity.java
CardSwipeSecurityFrame.java
databaseinfo.txt

All files are required to be in the same folder to run.

[] Application []

CardSwipeSecurity.java contains the Main Method.

This software is meant to emulate a security system requiring card 
swipes for entry to various locations. A log of all activities is maintained in 
a separate postgresql database.

This requires access to a PostgreSQL Database containing a Table called 
access_log. Further details can be found and altered in databaseinfo.txt
Requires a postgresql.jdbc.jar Driver included in the classpath. 

Tested with postgresql-9.4-1200.jdbc41.jar on PostgreSQL 9.4.

[] databaseinfo.text []

The second line of the text file is the URL of the PostgreSQL database used in
the application. 
The third and fourth lines are intended to contain the username and password of 
the database. They should be adjusted as needed. 

[] User Agreement []

Provided 'as is' with no purpose or implied use beyond practice and edification. 
Use at your own discretion. 

