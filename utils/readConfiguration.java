/*
 -----------------------------------------------------------------------------------
 Project 	 : Projet PRO
 File     	 : readConfiguration.java
 Author(s)   : R. Combremont, M. Dupraz, I. Ounon, P. Sekley, J. Ayoub 
 Date        : 10.05.2016
 Purpose     : Reads the connection configurations. 
 remark(s)   : n/a
 Compiler    : jdk 1.8.0_60
 -----------------------------------------------------------------------------------
 */



package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * Reads the configuration file to get the connection informations.
 *
 * @author R. Combremont, M. Dupraz, I. Ounon, P. Sekley, J. Ayoub 
 * @date 10.05.2016
 * @version 1.0
 */
public class readConfiguration {
   
   /**
    * Reads the configurations
 	*
 	* @throws IOException
 	*/
	public void readConfig() throws IOException{
      try {
         InputStream input = null;
         Properties prop = new Properties();
         
         input = new FileInputStream("configuration.properties");
         
         /** Load properties */
         prop.load(input);
         
         /** Fetch the datas from the configuration file */
         serverAddress = prop.getProperty("serverAddress");
         serverPort = Integer.parseInt(prop.getProperty("serverPort"));
      
         
      } catch (FileNotFoundException ex) {
         System.out.println("Error in loading configurations");
      }
   }
   
	
   /**
    * Returns the server address.
    *
    * @return String
    */
	public String getServerAddress(){
      return serverAddress;
	}
   
   

	/**
	 * Returns the server port
	 *
	 * @return int
	 */
	public int getServerPort(){
      return serverPort;
	}
   
   
    /** Server address */
  	private String serverAddress;
  	/** Server port */
  	private int serverPort;
}
