/*
 -----------------------------------------------------------------------------------
 Project 	 : Projet PRO
 File     	 : UpdateData.java
 Author(s)   : R. Combremont, M. Dupraz, I. Ounon, P. Sekley, J. AYOUB 
 Date        : 22.04.2016
 Purpose     : The main purpose is to keep the application's main board up to 
 			   date.
 remark(s)   : The line charts update frequency can be changed but the other 
 			   indicatores update frequency is fixed to 30 seconds.
 Compiler    : jdk 1.8.0_60
 -----------------------------------------------------------------------------------
 */
package data_processing;

import java.sql.SQLException;
import db.ConnectionForm;
import db.Data;
import db.Data.Sensor;
import gui.MainWindow;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;

/**
 * This class will begin to update all the data on the gui once initialized with 
 * the only empty constructor and it offers Duration variables to change the 
 * duration between each update for the line charts while the other indicators are 
 * updated each 30 seconds.
 *
 * @author R. Combremont, M. Dupraz, I. Ounon, P. Sekley, J. AYOUB
 * @date 22.04.2016
 * @version 1.0
 */
public class UpdateData {
	/**
     * Declaration and definition of all the Images
     */
    /** Daytime sunny/cloudy image */
    final Image imSunnyCloudy = new Image(ResourceLoader.load(
    											"meteoImages/imSunnyCloudy.png"));
    /** Daytime light rain image*/
    final Image imRainLight   = new Image(ResourceLoader.load(
    											"meteoImages/imRainLight.png"));
    /** Daytime heavy rain image */
    final Image imRainHeavy   = new Image(ResourceLoader.load(
    											"meteoImages/imRainHeavy.png"));
    /** Daytime snow image */
    final Image imSnow 		  = new Image(ResourceLoader.load(
    											"meteoImages/imSnow.png"));
    /** Night time clear or cloudy image */
    final Image imNight		  = new Image(ResourceLoader.load(
    											"meteoImages/imNight.png"));
    /** Night time Rain image */
    final Image imNightRain	  = new Image(ResourceLoader.load(
    											"meteoImages/imNightRain.png"));
    /** Night time snowing image */
    final Image imNightSnow	  = new Image(ResourceLoader.load(
    											"meteoImages/imNightSnow.png"));
    
	
	/**
	 * The only constructor that starts the two timelines.
	 */
	public UpdateData() {

		/**
		 * This timeline updates all the indicators on the main dashboard except 
		 * the line charts
		 */
		timelineRealTime = new Timeline(
			      new KeyFrame(DURATION_TO_START, event -> {
					
					try {
						/** 
						 * If the connection is lost it will update the 
						 * connectivity icon 
						 */
						if(lostConnection) {
							MainWindow.updateConnectivityIcon("imInactiv");
						}
						
						MainWindow.getConnectionForm();
						/**
						 * Try to connect if the user filled the connection form
						 */
						if(ConnectionForm.getFormStatus()) {		        			
							MainWindow.setIsConnected(true);
							/**  */
							checkLatestDataRealTime();
							if(lostConnection) {
								MainWindow.updateConnectivityIcon("imActiv");
								lostConnection = false;
							}
						}
						else{
							/** Connection is lost for instance disconnection */
							return;
						}

					} catch (SQLException e) {
						lostConnection = true;
					}

					}),
			      /** Setting the default duration */
			      new KeyFrame(DURATION_1_DEFAULT)
			    );
		/** The timeline wont stop unless we lost the connection or if the 
		 * applicatio is closed 
		 */
		 timelineRealTime.setCycleCount(Timeline.INDEFINITE);
		 
		 /** Initialization of a Timeline for the line charts */
		 timelineLcs = new TimelineLcs();
		 /** This ParallelTransition will enable to start the two timelines at the 
		  * same time
		  */
		 pt 		 = new ParallelTransition();

		 /** Setting the default duration */
		 timelineLcs.setPeriod(DURATION_TO_START, DURATION_1_DEFAULT);

		 pt.getChildren().add(timelineRealTime);
		 pt.getChildren().add(timelineLcs.getTimeline());
		 
		 /**  Starting both timelines */
		 pt.play();
	}
	
	
	/**
	 * Returns the line charts timeline.
	 * @return TimelineLcs
	 */
	public static TimelineLcs getTimelineLcs() {
		return timelineLcs;
	}

	
	/**
	 * Returns the parallel transitions that contains the two timelines.
	 * @return ParallelTransitions
	 */
	public static ParallelTransition getPt() {
		return pt;
	}
	

	/**
	 * Returns the duration to start a timeline.
	 * 0 seconds.
	 * @return Duration
	 */
	public static Duration getDurationToStart() {
		return DURATION_TO_START;
	}


	/**
	 * Returns the default duration.
	 * 30 seconds.
	 * @return Duration
	 */
	public static Duration getDuration1Default() {
		return DURATION_1_DEFAULT;
	}


	/**
	 * Returns the second duration.
	 * 5 minutes.
	 * @return Duration
	 */
	public static Duration getDuration2() {
		return DURATION_2;
	}


	/**
	 * Returns the third duration.
	 * 30 minutes.
	 * @return Duration
	 */
	public static Duration getDuration3() {
		return DURATION_3;
	}


	/**
	 * Returns the fourth duration.
	 * 1 hour.
	 * @return Duration
	 */
	public static Duration getDuration4() {
		return DURATION_4;
	}


	/**
	 * Returns the fifth duration.
	 * 2 hours.
	 * @return Duration
	 */
	public static Duration getDuration5() {
		return DURATION_5;
	}


	/**
	 * Returns the sixht duration..
	 * 4 hours.
	 * @return Duration
	 */
	public static Duration getDuration6() {
		return DURATION_6;
	}


	/**
	 * This method once called will check for the latest data and store it into 
	 * a local variable.
	 *
	 * @throws SQLException
	 */
	private void checkLatestDataRealTime() throws SQLException {
		/**
		 * Storing the latest data for each sensor in a local variable.
		 */
		Data actualTemperature = Data.getLastData(
				Sensor.TEMPERATURE);
		Data actualHumidity    = Data.getLastData(
				Sensor.HUMIDITY);
		Data actualPressure    = Data.getLastData(
				Sensor.PRESSURE);
		Data actualAirQuality  = Data.getLastData(
				Sensor.AIR_QUALITY);
		Data actualRadiancy    = Data.getLastData(
				Sensor.RADIANCY);
		Data actualRain        = Data.getLastData(
				Sensor.RAIN);
		
		
		/**
		 * Storing the latest data value for each sensor in a local variable.
		 */
		double actualTemperatureValue = actualTemperature.getValue();
		double actualHumidityValue    = actualHumidity.getValue();
		double actualPressureValue    = actualPressure.getValue();
		double actualRainValue		  = actualRain.getValue();
		double actualRadiancyValue    = actualRadiancy.getValue();
		double actualAirQualityValue  = actualAirQuality.getValue();
		
		/**
		 * Update/change the value for the different indicators of the main 
		 * dashboard only if there is a real modification.  
		 */
		if (!Double.valueOf(pressure).equals(actualPressureValue)) {
			MainWindow.updatePressureGauge(actualPressureValue);
			pressure = actualPressureValue;
		}
			
		if (!Double.valueOf(humidity).equals(actualHumidityValue)) {	
			MainWindow.updatePbHumidity(actualHumidityValue);
			humidity = actualHumidityValue;
		}
		
		if (!Double.valueOf(temperature).equals(actualTemperatureValue)) {
			MainWindow.updateLcdTemperature(actualTemperatureValue);
			temperature = actualTemperatureValue;
		}
		
		if (!Double.valueOf(airQuality).equals(actualAirQualityValue)) {
			MainWindow.updateAirQualityText(actualAirQualityValue);
			airQuality = actualAirQualityValue;
		}
		

		
		/**
		 * If it's raining or snowing. 
		 */
		if (actualRainValue == 1) {
			/**
			 * If it's day time.
			 */
			if (actualRadiancyValue > 250) {
				
				/**
				 * If it's raining (depending on the temperature).
				 */
				if (Data.getLastData(Sensor.TEMPERATURE).getValue() >= 0){
				if (actualTemperatureValue >= 0)
					MainWindow.updateImageView(imRainLight);
					
				}
				/**
				 * Else it's snowing (below 0 degree).
				 */
				else{
					MainWindow.updateImageView(imSnow);
					
				}
			}
					
			
			/**
			 * Else it's night time.
			 */
			else {
				
				/**
				 * If it's raining (depending on the temperature).
				 */
				if (Data.getLastData(Sensor.TEMPERATURE).getValue() >= 0){
				if (actualTemperatureValue >= 0)
					MainWindow.updateImageView(imNightRain);
					
				}
				/**
				 * Else it's snowing (below 0 degree).
				 */
				else{
					MainWindow.updateImageView(imNightSnow);
					
				}
			}
		}
		
		/**
		 * Else then there is no rain or snow fall.
		 */
		else {
			
			/**
			 * If it's day time.
			 */
			if (actualRadiancyValue > 160) {
				/**
				 * It's sunny / with few clouds.
				 */
				MainWindow.updateImageView(imSunnyCloudy);
				
			}
			
			/**
			 * Then it's night time without any rain or snow fall.
			 */
			else{
				MainWindow.updateImageView(imNight);
				
			}
		}
	}
	
	
	/** The actual pressure */
	private double pressure;
	/** The actual humidity */
	private double humidity;
	/** The actual temperature */
	private double temperature;
	/** The actual air quality */
	private double airQuality;
	/** The timeline for all the main dashboard indicators except the line charts */
	private static Timeline    timelineRealTime;
	/** The timline for the line charts */
	private static TimelineLcs timelineLcs;
	/** The parallel transition for the timelines */
	private static ParallelTransition pt;
	/** The duration to start the update after the timeline is started */
	private static final Duration DURATION_TO_START  = Duration.millis(0);
	/** The default duration (30 seconds) */
	private static final Duration DURATION_1_DEFAULT = Duration.seconds(30);
	/** The second duration (5 minutes) */
	private static final Duration DURATION_2		 = Duration.minutes(5);
	/** The third duration (30 minutes) */
	private static final Duration DURATION_3		 = Duration.minutes(30);
	/** The fourth duration (1 hour) */
	private static final Duration DURATION_4		 = Duration.hours(1);
	/** The fifth duration (2 hours) */
	private static final Duration DURATION_5		 = Duration.hours(2);
	/** The sixth duration (2 hours) */
	private static final Duration DURATION_6		 = Duration.hours(4);
	/** The boolean that indicates if the connection is lost (true) */	
	private static boolean lostConnection = false;
}
