/*
 -----------------------------------------------------------------------------------
 Project 	 : Projet PRO
 File     	 : Forcasting.java
 Author(s)   : R. Combremont, M. Dupraz, I. Ounon, P. Sekley, J. Ayoub 
 Date        : 01.06.2016
 Purpose     : Forcast the weather for the next day. 
 remark(s)   : n/a
 Compiler    : jdk 1.8.0_60
 -----------------------------------------------------------------------------------
 */

package gui;

import java.sql.SQLException;

import db.Data;
import db.Data.Sensor;


/**
 * Class.
 *
 * @author R. Combremont, M. Dupraz, I. Ounon, P. Sekley, J. Ayoub
 * @date 01.06.2016
 * @version 1.0
 */
public class Forcasting {
	

    /**
     * Constructor get back the data.
     * 
     */
    public Forcasting()
    {
        try
        {
            /** get the last pressure value */
        	dataActualPressure = Data.getLastData(Sensor.PRESSURE);
            pressureNow = dataActualPressure.getValue();
            
            /** get the pressure value an hour ago */
            pressureHourAgo = Data.getOneHourBeforeValue(Sensor.PRESSURE);
            
        }
        catch(SQLException e)
        {
        	e.printStackTrace();
        }

    }

    
    /**
     * Returns the forcasting for the next day based on the pressure. 
     *
     * @return the difference in the pressure
     */
    public int makeForcasting()
    {
    	int value = -1;
        double pressureDifference;
        pressureDifference = pressureHourAgo - pressureNow;
        
        /** Good weather */
        if(Double.compare(pressureDifference, -1.5) <= 0) 
        	//pressureDifference <= -1.5
        {
            value =  1;
        }
        /** No changes in the weather */
        if(Double.compare(pressureDifference, -1.5) > 0 && Double.compare(
        											pressureDifference, 1.5) < 0)
        	/**pressureDifference > -1.5 && pressureDifference < 1.5 */
        {
            value = 0;
        }
        /** bad weather */
        if(Double.compare(pressureDifference, 1.5) >= 0 && Double.compare(
        											pressureDifference, 2.5) < 0)
        	/**pressureDifference >= 1.5 && pressureDifference < 2.5*/
        {
            value = 2;
        }
        /** really bad weather */
        if(Double.compare(pressureDifference, 2.5) >= 0)
        	/** pressureDifference >= 2.5 */
        {
            value = 3;
        }
        
        return value;
    }
    
    /** The actual pressure */
    private Data dataActualPressure;
    /** The past pressure */
    private double pressureHourAgo;
    /** the pressure of the moment */
    private double pressureNow;
}
