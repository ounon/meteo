/*
 -----------------------------------------------------------------------------------
 Project 	 : Projet PRO
 File     	 : Data.java
 Author(s)   : R. Combremont, M. Dupraz, I. Ounon, P. Sekley, J. Ayoub 
 Date        : 02.04.2016
 Purpose     : This class defines the data type needed for this application. 
 remark(s)   : n/a
 Compiler    : jdk 1.8.0_60
 -----------------------------------------------------------------------------------
 */

package db;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import gui.MainWindow;



/**
 * Class representing the data type which is a value at a precise date and time.
 *
 * @author R. Combremont, M. Dupraz, I. Ounon, P. Sekley, J. Ayoub
 * @date 02.04.2016
 * @version 1.1
 */
public class Data {
	
	
	/**
	 * Constructor with the type LocalDateTime.
	 * 
	 * @param dateAndTime
	 * @param value
	 */
	public Data (LocalDateTime dateAndTime, double value) {
		this.dateAndTime = dateAndTime;
		this.value       = value;
		
	}
	

	/**
	 * Constructor with a time precision of minutes.
	 * 
	 * @param year
	 * @param month
	 * @param dayOfMonth
	 * @param hour
	 * @param minute
	 * @param value
	 */
	public Data (int year, int month, int dayOfMonth, int hour, 
										int minute, double value) {
		this.dateAndTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute);
		this.value       = value;
	}
	
	
	
	/**
	 * Constructor with a time precision of seconds.
	 * 
	 * @param year
	 * @param month
	 * @param dayOfMonth
	 * @param hour
	 * @param minute
	 * @param second
	 * @param value
	 */
	public Data (int year, int month, int dayOfMonth, 
					int hour, int minute, int second, double value) {
		this(LocalDateTime.of(year, month, dayOfMonth, hour, minute, second), 
																			value);
	}
	
	
	
	/**
	 * Constructor with a time precision of nano-seconds.
	 * 
	 * @param year
	 * @param month
	 * @param dayOfMonth
	 * @param hour
	 * @param minute
	 * @param second
	 * @param nanoOfSecond
	 * @param value
	 */
	public Data (int year, int month, int dayOfMonth, 
			int hour, int minute, int second, int nanoOfSecond, double value) {
		this(LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, 
															nanoOfSecond), value);
	}
	
	
	
	/**
	 * Returns the DateTime attribut.
	 *
	 * @return LocalDateTime
	 */
	public LocalDateTime getDateTime() {
		return this.dateAndTime;
	}
	
	
	/**
	 * Returns the value attribut. 
	 *
	 * @return double
	 */
	public double getValue() {
		return this.value;
	}
	
	
	/**
	 * Returns the time of the data.
	 *
	 * @return Time
	 */
	public String getTime() {
		String s = dateAndTime.toLocalTime().toString();
		return s;
	}
	
	
	/**
	 * Returns the date of the data.
	 *
	 * @return Date
	 */
	public String getDate() {
		String s = dateAndTime.toLocalDate().toString();
		return s;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return dateAndTime.toString() + ", " + value;
	}
	
	
	/**
	 * Return the Connection.
	 *
	 * @return dbConn
	 */
	public static DBConnection getdbConnection(){
		return dbConn;
	}
	
	
	
	/**
	 * This enum represents the different sensors
	 */
	public enum Sensor {
		
		/**  */
		TEMPERATURE("temperatureSensor"),
		/**  */
		HUMIDITY("humiditySensor"),
		/**  */
		PRESSURE("pressureSensor"),
		/**  */
		RADIANCY("radiancySensor"),
		/**  */
		RAIN("rainSensor"),
		/**  */
		AIR_QUALITY("airQualitySensor");
		
		
		/**
		 * Constructor.
		 * 
		 * @param id
		 */
		private Sensor (String id) {
			this.id = id;
		}
		
		
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		public String toString() {
			return this.id;
		}
		
		/** The name of the sensor is the sensor's id */
		private final String id; 
	}
	
	
	
	
	/**
	 * This method returns the last data for a given Sensor
	 *
	 * @param sensor
	 * @return Data
	 * @throws SQLException
	 */
	public static Data getLastData(Sensor sensor) throws SQLException  {
		final String QUERY   = "CALL lastCapturedValue('" + sensor + "');"; 
		Double 		 value   = 0.;
		Date   		 date    = null;
		Time   		 time    = null;
		int    		 year    = 0, 
					 month   = 0, 
					 day	 = 0, 
					 hours   = 0, 
					 minutes = 0, 
					 seconds = 0;
		
		try{

			DBConnection dbConn = new DBConnection(MainWindow.getConnectionForm());
			ResultSet result = dbConn.executeQuery(QUERY);
			if (result.next()){
				value = result.getDouble("value_");
				date  = result.getDate  ("date_");
				time  = result.getTime  ("time_");
			}
		}

		catch (SQLException sqle){
				throw sqle;
		     }
     
		finally {
			if (dbConn != null)
				dbConn.close();
			}
				
				/**
				 * Parsing the Data 
				 */
				String tempString = date.toString();
				year              = Integer.parseInt(tempString.substring(0, 4));
				if (Integer.parseInt(tempString.substring(5,6)) == 1)
					month = Integer.parseInt(tempString.substring(5,7));
				else
					month = Integer.parseInt(String.valueOf(tempString.substring(6,7)));
				
				if (Integer.parseInt(String.valueOf(tempString.substring(8,9))) != 0)
					day = Integer.parseInt(tempString.substring(8,10));
				else
					day = Integer.parseInt(String.valueOf(tempString.substring(9,10)));
				
				
				tempString = time.toString();
				
				if (Integer.parseInt(tempString.substring(0, 1)) == 0)
					hours = Integer.parseInt(tempString.substring(1, 2));
				else
					hours = Integer.parseInt(tempString.substring(0, 2));

				
				if (Integer.parseInt(tempString.substring(3, 4)) == 0)
					minutes = Integer.parseInt(tempString.substring(4, 5));
				else
					minutes = Integer.parseInt(tempString.substring(3, 5));
				
				
				if (Integer.parseInt(tempString.substring(6, 7)) == 0)
					seconds = Integer.parseInt(tempString.substring(7, 8));
				else
					seconds = Integer.parseInt(tempString.substring(6, 8));	
			
		return new Data(year, month, day, hours, minutes, seconds, value);
	}
	
	
	/**
	 * Return an array liste of the data for a specific day.
	 *
	 * @param sensor
	 * @param searchDate
	 * @return arrayList<Data>
	 */
	public static ArrayList<Data> getValueInDay(Sensor sensor, LocalDate searchDate) {
		final String QUERY   = "CALL capturedValueInDay('" + sensor + "','" + searchDate + "');"; 
		Double 		 value   = 0.;
		Date   		 date    = null;
		Time   		 time    = null;
		int    		 year    = 0, 
					 month   = 0, 
					 day	 = 0, 
					 hours   = 0, 
					 minutes = 0, 
					 seconds = 0;
		ArrayList<Data> listData = new ArrayList<>();
		
		try{
			
			dbConn = new DBConnection(MainWindow.getConnectionForm());
			ResultSet result = dbConn.executeQuery(QUERY);
			if (result.next()){
				value = result.getDouble("value_");
				date  = result.getDate  ("date_");
				time  = result.getTime  ("time_");
			}
		}
		catch (SQLException se){
			System.out.println("An error occurated during the execution!");
			se.printStackTrace();
		}

		finally {
			if (dbConn != null)
				dbConn.close();
		}
				
				String tempString = date.toString();
				year              = Integer.parseInt(tempString.substring(0, 4));
				if (Integer.parseInt(tempString.substring(5,6)) == 1)
					month = Integer.parseInt(tempString.substring(5,7));
				else
					month = Integer.parseInt(String.valueOf(tempString.substring(6,7)));
				
				if (Integer.parseInt(String.valueOf(tempString.substring(8,9))) != 0)
					day = Integer.parseInt(tempString.substring(8,10));
				else
					day = Integer.parseInt(String.valueOf(tempString.substring(9,10)));
				
				
				tempString = time.toString();
				
				if (Integer.parseInt(tempString.substring(0, 1)) == 0)
					hours = Integer.parseInt(tempString.substring(1, 2));
				else
					hours = Integer.parseInt(tempString.substring(0, 2));

				
				if (Integer.parseInt(tempString.substring(3, 4)) == 0)
					minutes = Integer.parseInt(tempString.substring(4, 5));
				else
					minutes = Integer.parseInt(tempString.substring(3, 5));
				
				
				if (Integer.parseInt(tempString.substring(6, 7)) == 0)
					seconds = Integer.parseInt(tempString.substring(7, 8));
				else
					seconds = Integer.parseInt(tempString.substring(6, 8));	
	
			listData.add(new Data(year, month, day, hours, minutes, seconds, value));
			
		return listData;
	}
	
	
	/**
	 * Returns true if the date is available, else false.
	 *
	 * @param searchDate
	 * @return boolean
	 */
	public static boolean checkDate(LocalDate searchDate){
		if (searchDate == null)
			return false;
		final String QUERY   = "CALL checkDate('" + searchDate + "');"; 
		boolean dateFound = false;
		try{
				
				dbConn = new DBConnection(MainWindow.getConnectionForm());
				ResultSet result = dbConn.executeQuery(QUERY);
				//ResultSet result = OpenConnection.getConnectionLink().executeQuery(QUERY);
				if (result.next())
					dateFound = true;
				
		}
		catch (SQLException se){
		      System.out.println("An error occurated during the execution!");
		      se.printStackTrace();
		}

		 finally {
		      if (dbConn != null)
		    	  dbConn.close();
		 }
		 return dateFound;				
	}
	
	
	
	/**
	 * Return the average value for a specific period.
	 *
	 * @param sensor
	 * @param searchDate
	 * @param beginTime
	 * @param endTime
	 * @return Data
	 */
	public static Data averageValue(Sensor sensor, 
									  LocalDate searchDate, 
									  Time beginTime, 
									  Time endTime){
		final String QUERY   = "CALL capturedValueDayByHour('" + sensor + "','" 
									                           + searchDate + "','" 
				                                               + beginTime + "','" 
									                           + endTime + "');"; 
	    double averageValue  = 0.;
		int    		 year    = 0, 
					 month   = 0, 
					 day	 = 0, 
					 hours   = 0, 
					 minutes = 0, 
					 seconds = 0;
		try{
				
				dbConn = new DBConnection(MainWindow.getConnectionForm());
				ResultSet result = dbConn.executeQuery(QUERY);
				//ResultSet result = OpenConnection.getConnectionLink().executeQuery(QUERY);
				if (result.next()){
					averageValue = result.getDouble("AVG(value_)");
					String tempString = searchDate.toString();
					year              = Integer.parseInt(tempString.substring(0, 4));
					if (Integer.parseInt(tempString.substring(5,6)) == 1)
						month = Integer.parseInt(tempString.substring(5,7));
					else
						month = Integer.parseInt(String.valueOf(tempString.substring(6,7)));
					
					if (Integer.parseInt(String.valueOf(tempString.substring(8,9))) != 0)
						day = Integer.parseInt(tempString.substring(8,10));
					else
						day = Integer.parseInt(String.valueOf(tempString.substring(9,10)));
					
					
					tempString = endTime.toString();
					
					if (Integer.parseInt(tempString.substring(0, 1)) == 0)
						hours = Integer.parseInt(tempString.substring(1, 2));
					else
						hours = Integer.parseInt(tempString.substring(0, 2));

					
					if (Integer.parseInt(tempString.substring(3, 4)) == 0)
						minutes = Integer.parseInt(tempString.substring(4, 5));
					else
						minutes = Integer.parseInt(tempString.substring(3, 5));
					
					
					if (Integer.parseInt(tempString.substring(6, 7)) == 0)
						seconds = Integer.parseInt(tempString.substring(7, 8));
					else
						seconds = Integer.parseInt(tempString.substring(6, 8));	
				}
				
		}
		catch (SQLException se){
		      System.out.println("An error occurated during the execution!");
		      se.printStackTrace();
		}

		 finally {
		      if (dbConn != null)
		    	  dbConn.close();
		 }
		 return new Data(year, month, day, hours, minutes, seconds, averageValue);
		
	}
	
	
	
	/**
	 * 
	 *
	 * @param sensor
	 * @param from
	 * @param to
	 * @return the data list for the selected interval.
	 */
	public static ArrayList<Data> getIntervalData (Sensor 		 sensor,
												   LocalDateTime from, 
												   LocalDateTime to) {
		return null;
	}
	
	
	
	/**
	 * Returns the value of a sensor one hour ago. 
	 *
	 * @param sensor
	 * @return the value
	 */
	public static double getOneHourBeforeValue(Sensor sensor){
	    final String QUERY = "CALL capturedValueOneHourBefore('"+ sensor + "');";
	    double value = 0;
	    try{
	      
	      dbConn = new DBConnection(MainWindow.getConnectionForm());
	      ResultSet result = dbConn.executeQuery(QUERY);
	        if (result.next()){
	          value = result.getDouble("value_");
	        }
	          }
	      catch (SQLException se){
	            System.out.println("An error occurated during the execution!");
	            se.printStackTrace();
	      }

	       finally {
	    
	            if (dbConn != null)
	              dbConn.close();
	    
	       }
	         return value;
	  }
	
	

	/** Date and time of the data */
	private 	   LocalDateTime dateAndTime;
	/** Value of the data */
	private 	   double 		 value;
	/** The connection */
	private static DBConnection  dbConn;
}
