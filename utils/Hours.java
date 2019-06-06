/*
 -----------------------------------------------------------------------------------
 Project 	 : Projet PRO
 File     	 : Hours.java
 Author(s)   : R. Combremont, M. Dupraz, I. Ounon, P. Sekley, J. Ayoub 
 Date        : 01.06.2016
 Purpose     : Get the list of hours of the data. 
 remark(s)   : n/a
 Compiler    : jdk 1.8.0_60
 -----------------------------------------------------------------------------------
 */


package utils;

import java.util.ArrayList;


/**
 * Get the list of hours from a data array list.
 *
 * @author R. Combremont, M. Dupraz, I. Ounon, P. Sekley, J. Ayoub
 * @date 01.06.2016
 * @version 1.0
 */
public class Hours {
	/**
	 * Returns the list of hours for the data array list.
	 *
	 * @return the list of hours
	 */
	public static ArrayList<String> getHoursList(){
		ArrayList<String> hoursList = new ArrayList<>();
		final String extension = ":00:00";
		 for (short i = 0; i < 24; i++){
     		 if (i < 10)
     		   hoursList.add( "0" +i + extension);
     		 else
     			 hoursList.add(i + extension);
     	  }
		 return hoursList;
	}
	
}
