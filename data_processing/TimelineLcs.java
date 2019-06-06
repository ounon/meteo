/*
 -----------------------------------------------------------------------------------
 Project 	 : Projet PRO
 File     	 : TimelineLcs.java
 Author(s)   : R. Combremont, M. Dupraz, I. Ounon, P. Sekley, J. Ayoub 
 Date        : 26.05.2016
 Purpose     : Update the line charts with different frequencies. 
 remark(s)   : n/a
 Compiler    : jdk 1.8.0_60
 -----------------------------------------------------------------------------------
 */
package data_processing;

import java.sql.SQLException;

import db.Data;
import db.Data.Sensor;
import gui.MainWindow;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Class.
 *
 * @author R. Combremont, M. Dupraz, I. Ounon, P. Sekley, J. Ayoub 
 * @date 26.05.2016
 * @version 1.2
 */
public class TimelineLcs {
	
	/**
	 * This method set the frequency and the initial time
	 *
	 * @param d1
	 * @param d2
	 */
	public void setPeriod(Duration d1, Duration d2) {
		this.timeline = new Timeline(
			      new KeyFrame(d1, event -> {
					try {
						MainWindow.updateLcs(MainWindow.getLcsTemperature(), Data.getLastData(Sensor.TEMPERATURE));
						MainWindow.updateLcs(MainWindow.getLcsHumidity(), Data.getLastData(Sensor.HUMIDITY));
						MainWindow.updateLcs(MainWindow.getLcsPressure(), Data.getLastData(Sensor.PRESSURE));
						MainWindow.updateLcs(MainWindow.getLcsAirQuality(), Data.getLastData(Sensor.AIR_QUALITY));
					} catch (SQLException e) {
						return;
					}
				}),  
				     new KeyFrame(d2)
				    );
			 timeline.setCycleCount(Timeline.INDEFINITE);
	}
	
	
	/**
	 * Launches the timeline.
	 */
	public void start() {
		if (timeline != null)
			timeline.play();
	}
	
	
	/**
	 * Stops the timeline.
	 */
	public void stop() {
		if (timeline != null)
			timeline.stop();
	}
	
	
	/**
	 * Returns the timeline.
	 *
	 * @return Timeline
	 */
	public Timeline getTimeline () {
		return this.timeline;
	}
	
	
	/** The timeline attribut */
	private Timeline  timeline = null;
}
