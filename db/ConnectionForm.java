/*
 -----------------------------------------------------------------------------------
 Project 	 : Projet PRO
 File     	 : ConnectionForm.java
 Author(s)   : R. Combremont, M. Dupraz, I. Ounon, P. Sekley, J. Ayoub 
 Date        : 15.05.2016
 Purpose     : Choose the data base which the application will connect to by making 
 			   the user fill a form. 
 remark(s)   : n/a
 Compiler    : jdk 1.8.0_60
 -----------------------------------------------------------------------------------
 */

package db;


/**
 * Class.
 *
 * @author Jean AYOUB
 * @date 3 juin 2016
 * @version 1.0
 */
public class ConnectionForm {
	
	
	/**
	 * Empty constructor.
	 * 
	 */
	public ConnectionForm() {
		this.connectionName = "";
		this.hostname 		= "";
		this.port           = 0;
		this.username 		= "";
		this.password 		= "";
		ConnectionForm.formFilled = false;
	}
	
	/**
	 * Sets the connection form after being filled.
	 *
	 * @param connectionName
	 * @param hostname
	 * @param port
	 * @param username
	 * @param password
	 */
	public void setConnectionForm(String connectionName, String hostname, int port, String username, String password){
		this.connectionName = connectionName;
		this.hostname 		= hostname;
		this.port 			= port;
		this.username 		= username;
		this.password 		= password;
		
		formFilled = true;
	}
	
	/**
	 * Returns the hostname.
	 *
	 * @return String
	 */
	public String getHostname(){
		return hostname;
	}
	
	
	/**
	 * Returns the port number.
	 *
	 * @return int
	 */
	public int getPort(){
		return port;
	}
	
	
	/**
	 * Returns the user name.
	 *
	 * @return String
	 */
	public String getUsername(){
		return username;
	}
	
	
	/**
	 * Returns the password.
	 *
	 * @return String
	 */
	public String getPassword(){
		return password;
	}
	
	
	/**
	 * Returns the connection name.
	 *
	 * @return String
	 */
	public String getConnectionName(){
		return connectionName;
	}
	
	
	/**
	 * Returns the form status.
	 *
	 * @return boolean
	 */
	public static boolean getFormStatus(){
		return formFilled;
	}
	
	
	/**
	 * Sets the form status.
	 *
	 * @param status
	 */
	public static void setFormStatus(boolean status){
		formFilled = status;
	}
	
	
	/**
	 * Reset all the form data.
	 */
	public void resetConnectionForm(){
		this.connectionName = "";
		this.hostname = "";
		this.port = 0;
		this.username = "";
		this.password = "";
		setFormStatus(false);
	}
	
	
	/** The name of the connection */
	private  String connectionName;
	/** The hostname of the connection */
	private  String hostname;
	/** The connection port */
	private  int 	port;
	/** The username  */
	private  String username;
	/** The password */
	private  String password;
	/** the status of the form to be filled */
	private static boolean formFilled = false;

}
