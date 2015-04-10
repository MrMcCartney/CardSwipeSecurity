/*
CardSwipeSecurity.java
By Reed McCartney
April 7, 2015

Uses CardSwipeSecurity.java and databaseinfo.txt
This requires access to a PostgreSQL Database containing a Table called access_log.
	Further details can be found and altered in databaseinfo.txt
	Requires a postgresql.jdbc.jar Driver included in the classpath. 
	Tested with postgresql-9.4-1200.jdbc41.jar

- Contains
Main Method
Method: 	readDatabaseInfo() - called at start, obtains info needed to find the database
Method: 	createLogTable() - unused
Method: 	readLogTable() - attempts to read in all records for the log
Method:		writeLogRecord() - attempts to write a record for a new log entry

This software is meant to emulate a security system requiring card swipes for 
	entry to various locations. A log of all activities is maintained in a 
	separate postgresql database.

Provided 'as is' with no purpose or implied use beyond practice and edification. 
*/

import javax.swing.*;
import java.util.*;
import java.nio.file.*;
import java.io.*;
import java.sql.*;

public class CardSwipeSecurity
{
	private static final String DB_INFO = "databaseinfo.txt";
	static List<Long> logTime = new ArrayList<Long>();
	static List<String> logID = new ArrayList<String>();
	static List<String> logLocation = new ArrayList<String>();
	
	//Information about the PostgreSQL database it uses.
	private static String dbDriver = "org.postgresql.Driver";
	private static String dbUrl;
	private static String dbUser;
	private static String dbPass;
	private static CardSwipeSecurityFrame mainWindow;
	
	public static void main(String[] args)
	{
		readDatabaseInfo();
		parseArgs(args);
		readLogTable();
		mainWindow = new CardSwipeSecurityFrame();
		mainWindow.setVisible(true);
	}
	
	// This method should look through the arguments
	public static void parseArgs(String[] args)
	{
		if (args.length > 0)
		{
			boolean argSetup = false;
			for (int x = 0; x < args.length; ++x)
			{
				switch (args[x])
				{
				case "setup": //If setup is an argument, create a log table.
					if (!argSetup)
					{
						createLogTable();
						argSetup = true;
					}
					break;
				default: // Show a message if an unused argument is found.
					JOptionPane.showMessageDialog(mainWindow,
						"Invalid Argument(s) found: " + args[x],
						"Invalid Argument", JOptionPane.WARNING_MESSAGE);
				}
			}
		}
	}
	
	// This method attempts to read information needed to find the database.
	public static void readDatabaseInfo()
	{
		
		Path databaseFile = Paths.get(DB_INFO);
		BufferedReader fileReader;
		try
		{
			fileReader = new BufferedReader(new FileReader(databaseFile.toString()));
			fileReader.readLine();//Skip the help line
			dbUrl = fileReader.readLine();
			dbUser = fileReader.readLine();
			dbPass = fileReader.readLine();
			
			fileReader.close();
		}
		catch (IOException ex)
		{
			JOptionPane.showMessageDialog(mainWindow,
				"Unable to read 'databaseinfo.txt'.",
				"IO Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
	
	// This method attempts to create a table in the database
	public static void createLogTable()
	{
		Connection c = null;
		Statement stmt = null;
		try
		{
			Class.forName(dbDriver);
			c = DriverManager.getConnection(dbUrl, dbUser, dbPass);
			c.setAutoCommit(false);
			stmt = c.createStatement();
			// Create the table if it doesn't exist.
			// Truncate (remove data.)
			
			
			String sql = "CREATE TABLE IF NOT EXISTS ACCESS_LOG " +
				"(TIME 	BIGINT PRIMARY KEY	NOT NULL , " +
				"ID	TEXT 			NOT NULL , " +
				"LOCATION TEXT			NOT NULL ); " + 
				"TRUNCATE ACCESS_LOG;";
			stmt.executeUpdate(sql);
			c.commit(); // Commit changes or it doesn't happen
			stmt.close();
			c.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(mainWindow,
				e.getClass().getName()+": "+e.getMessage(),
				"Database Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
	
	// This method attempts to read in all log records from the table
	public static void readLogTable()
	{
		Connection c = null;
		Statement stmt = null;
		try
		{
			Class.forName(dbDriver);
			c = DriverManager.getConnection(dbUrl, dbUser, dbPass);
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "SELECT * FROM ACCESS_LOG;";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				logTime.add(rs.getLong("TIME"));
				logID.add(rs.getString("ID"));
				logLocation.add(rs.getString("LOCATION"));
			}
			rs.close();
			stmt.close();
			c.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(mainWindow,
				e.getClass().getName()+": "+e.getMessage(),
				"Database Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
	
	// This method attempts to write a new log record to the table
	public static void writeLogRecord(Long time, String id, String loc)
	{
		Connection c = null;
		Statement stmt = null;
		try
		{
			Class.forName(dbDriver);
			c = DriverManager.getConnection(dbUrl, dbUser, dbPass);
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "INSERT INTO ACCESS_LOG (TIME, ID, LOCATION) " +
				"VALUES ( "+Long.toString(time)+" , '"+id+"' , '"+loc+"' );";
			stmt.executeUpdate(sql);
			stmt.close();
			c.commit(); // Commit changes or it doesn't happen.
			c.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(mainWindow,
				e.getClass().getName()+": "+e.getMessage(),
				"Database Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
}
