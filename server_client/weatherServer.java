/*
 -----------------------------------------------------------------------------------
 Project 	 : Projet PRO
 File     	 : WeatherServer.java
 Author(s)   : R. Combremont, M. Dupraz, I. Ounon, P. Sekley, J. Ayoub 
 Date        : 24.05.2016
 Purpose     : A server model for a client-server connection type. 
 remark(s)   : In this application this model is not used.
 Compiler    : jdk 1.8.0_60
 -----------------------------------------------------------------------------------
 */

package server_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class is a server model for a client-server connection type.
 *
 * @author R. Combremont, M. Dupraz, I. Ounon, P. Sekley, J. Ayoub
 * @date 24.05.2016
 * @version 1.0
 */
public class weatherServer {
	/** The connection port  */
	private final int port;
   
   
  /**
   * Constructor.
   * 
   * @param port
   */
   public weatherServer(int port){
      this.port = port;
   }
   
   
   /**
    * Responds for eah new client.
    *
    */
   public void serveClients(){
      System.out.println("Starting the Receptionist Worker on a new thread...");
      new Thread(new ReceptionistWorker()).start();
   }
   
   /**
    * Class.
    *
    * @author Jean AYOUB
    * @date 3 juin 2016
    * @version 1.0
    */
   private class ReceptionistWorker implements Runnable {

      @SuppressWarnings("resource")
	@Override
      public void run() {
         ServerSocket serverSocket = null;
         
         try {
            serverSocket = new ServerSocket(port);
         } catch (IOException ex) {
            System.out.println("Error unable to open a server socket");
         }
         
         while(true){
            try {
               Socket clientSocket = serverSocket.accept();
               System.out.println(" A new client has arrived. Starting a new thread"
               															+ "for it");
               new Thread(new ServantWorker(clientSocket)).start();
            } catch (IOException ex) {
               Logger.getLogger(weatherServer.class.getName()).log(Level.SEVERE, 
            		   													null, ex);
            }
         }
      }
      
      
       
      /**
       * Inner class that will take care of client once there are connected.
       * Here is where to serve clients according to the command.
       *
       * @author R. Combremont, M. Dupraz, I. Ounon, P. Sekley, J. Ayou
       * @date 3 juin 2016
       * @version 1.0
       */
      private class ServantWorker implements Runnable {
         
        /** The socket for the client */
        Socket clientSocket;
        /** A buffered Reader */
        BufferedReader in = null;
        /** A print writer */
        PrintWriter out = null;

         /**
         * Constructor.
         * 
         * @param clientSocket
         */
        private ServantWorker(Socket clientSocket) {
            try {
               this.clientSocket = clientSocket;
               in = new BufferedReader(new InputStreamReader(clientSocket
            		   											.getInputStream()));
               out = new PrintWriter(clientSocket.getOutputStream());
            } catch (IOException ex) {
               System.out.println("Error unable to initialize a servant worker");
            }

         }

         @Override
         public void run() {
            String command;
            boolean shouldRun = true;
            
            try {
               
               
               while ((shouldRun) && (command = in.readLine()) != null) {
                  
                  /* IMPLEMENT HERE WHAT THE SERVER HAS TO SEND TO THE CLIENT */
                  
                  if(command.equalsIgnoreCase("endCommand")){
                     shouldRun = false;
                  }
                  
                  
                  
               }
               System.out.println("Cleaning up resources...");
               clientSocket.close();
               in.close();
               out.close();
               
            } catch (IOException ex) {
               if (in != null) {
                  try {
                     in.close();
                  } catch (IOException ex1) {
                     ex1.getMessage();
                  }
               }
               if (out != null) {
                  out.close();
               }
               if (clientSocket != null) {
                  try {
                     clientSocket.close();
                  } catch (IOException ex1) {
                     ex1.getMessage();
                  }
               }
               ex.getMessage();
            }    
         }
      }  
   }
}