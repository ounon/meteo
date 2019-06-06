/*
 -----------------------------------------------------------------------------------
 Project 	 : Projet PRO
 File     	 : WeatherClient.java
 Author(s)   : R. Combremont, M. Dupraz, I. Ounon, P. Sekley, J. Ayoub 
 Date        : 24.05.2016
 Purpose     : A client model for a client-server connection type. 
 remark(s)   : In this application this model is not used.
 Compiler    : jdk 1.8.0_60
 -----------------------------------------------------------------------------------
 */

package server_client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import utils.readConfiguration;


/**
 * This class is a client model for a client-server connection type. 
 *
 * @author R. Combremont, M. Dupraz, I. Ounon, P. Sekley, J. Ayoub 
 * @date 25.05.2016
 * @version 1.0
 */
public class weatherClient {
   
   /**
    * To stard the connection
 	*
 	*/
	public void start() {
      Socket clientSocket = null;
      BufferedReader reader;
      @SuppressWarnings("unused")
	BufferedWriter writer;
      
      readConfiguration readConf = new readConfiguration();
      
      
      try{
         clientSocket = new Socket(readConf.getServerAddress(), 
        		 										readConf.getServerPort());
         reader = new BufferedReader(new InputStreamReader(
        		 									clientSocket.getInputStream()));
         writer = new BufferedWriter(new OutputStreamWriter(
        		 								clientSocket.getOutputStream()));
         
         /** We wait il the stream is ready to be used */
         while(!reader.ready()){}
         
         /** Do something while we did not get the end command */
         do{
            
            /** FILL HERE WITH WHAT WE WANT TO GET FROM THE SERVER */
            
         } while(!clientSocket.isClosed());
 
      }catch(Exception e){
         e.getMessage();
      }
      
      
   }

}
