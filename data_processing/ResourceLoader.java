/*
 -----------------------------------------------------------------------------------
 Project 	 : Projet PRO
 File     	 : ResourceLoader.java
 Author(s)   : R. Combremont, M. Dupraz, I. Ounon, P. Sekley, J. Ayoub 
 Date        : 24.05.2016
 Purpose     : Load the ressource into an inputStream. 
 remark(s)   : n/a
 Compiler    : jdk 1.8.0_60
 -----------------------------------------------------------------------------------
 */
package data_processing;

import java.io.InputStream;

/**
 * This class offers the possibility to load a resource.
 *
 * @author R. Combremont, M. Dupraz, I. Ounon, P. Sekley, J. Ayoub
 * @date 24.05.2016
 * @version 1.0
 */
final public class ResourceLoader {
	/**
	 * Loads a Resource into an input stream from a given path.
	 *
	 * @param path
	 * @return InputStream
	 */
	public static InputStream load (String path) {
		InputStream input = ResourceLoader.class.getResourceAsStream(path);
		
		if (input == null) {
			input = ResourceLoader.class.getResourceAsStream("/" + path);
		}
		return input;
	}
}
