/*
 -----------------------------------------------------------------------------------
 Project 	 : Projet PRO
 File     	 : ReceivedData.java
 Author(s)   : R. Combremont, M. Dupraz, I. Ounon, P. Sekley, J. Ayoub 
 Date        : 02.06.2016
 Purpose     : This class was created to make the application able to show line 
 			   charts for a specific day while using the average of the data. 
 remark(s)   : n/a
 Compiler    : jdk 1.8.0_60
 -----------------------------------------------------------------------------------
 */


package data_processing;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;

import db.Data;
import db.Data.Sensor;


/**
 * Class that process data by stocking it into local variables and then treat it.
 *
 * This version offers the average for a defined period
 * 
 * @author R. Combremont, M. Dupraz, I. Ounon, P. Sekley, J. Ayoub 
 * @date 02.06.2016
 * @version 1.0
 */
public class ReceivedData {
	
	/**
	 *  The only constructor.
	 * 
	 * @param date
	 * @param beginTime
	 * @param endTime
	 */
	public ReceivedData(LocalDate date, Time beginTime, Time endTime){
		this.date 	   = date;
		this.beginTime = beginTime;
		this.endTime   = endTime;
	}
	
	
	
	/**
	 * Returns the data with an average value of a specific period for a given date.
	 *
	 * @param sensor
	 * @return Data
	 */
	public Data getSensorDataAverage(Sensor sensor) {
		return Data.averageValue(sensor, date, beginTime, endTime);
	}
	
	
	
	/**
	 * Returns an array list of all the rain data 
	 *
	 * @return ArrayList<Data>
	 */
	public ArrayList<Data> getRainData(){
		return Data.getValueInDay(Sensor.RAIN, date);
	}
	
	/** The given date */
	private LocalDate date;
	/** The beginning time */
	private Time beginTime;
	/** The ending time */
	private Time endTime;
}
