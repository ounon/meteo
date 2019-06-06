/*
 -----------------------------------------------------------------------------------
 Project 	 : Projet PRO
 File     	 : LineChartStat.java
 Author(s)   : R. Combremont, M. Dupraz, I. Ounon, P. Sekley, J. Ayoub 
 Date        : 06.0.2016
 Purpose     : Modelize a specific line chart that responds to the application's 
 			   needs. 
 remark(s)   : n/a
 Compiler    : jdk 1.8.0_60
 -----------------------------------------------------------------------------------
 */
package gui;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;


/**
 * Class represent a personalized Line Chart for different elements 
 * 
 *
 * @author Jean AYOUB
 * @date 6 avr. 2016
 * @version 1.2
 */
public class LineChartStat extends LineChart<String, Number> {
        
	
	/**
	 * Constructor with initial data.
	 * 
	 * @param title
	 * @param seriesName
	 * @param xAxis
	 * @param yAxis
	 * @param dataList
	 */
	public LineChartStat(String 			title, 
						 String 			seriesName, 
						 CategoryAxis 		xAxis, 
						 NumberAxis 		yAxis, 
						 ArrayList<db.Data> dataList) {
		/**
		 *  Creating the chart
		 */
		super(xAxis, yAxis);
		this.setTitle(title);
		this.series.setName(seriesName);
		this.title = title;	
		
		
        /**
         *  Setting the series name.
         */
        series.setName(seriesName);
        
        
        /**
         * Populating the series with data and adding it to the chart
         */
        
        for (db.Data data : dataList) {
        	series.getData().add(
        				new Data<String, Number>(data.getTime(), data.getValue()));
        }
        this.getData().add(series);
	}
	
	
	
	
	
	/**
	 * Constructor without initial data.
	 * 
	 * @param title
	 * @param seriesName
	 * @param xAxis
	 * @param yAxis
	 */
	public LineChartStat(String 	  title, 
						 String 	  seriesName, 
						 CategoryAxis xAxis, 
						 NumberAxis   yAxis) {
		/**
		 *  Creating the chart.
		 */
		super(xAxis, yAxis);
		this.setTitle(title);
		this.series.setName(seriesName);
		this.title = title;

		
	}


	
	/**
	 * Updates the chart series by adding the latest after deleting the first data 
	 * if it exists and if there is less than 12.
	 *
	 * @param data
	 */
	public void updateSeries(db.Data data) {
	
		
		if (series.getData().size() != 0) {
			if (data.getTime().length() == 8) {
				if(data.getTime().substring(0,PRECISION_SEC).equals(
						series.getData().get(series.getData().size() - 1).getXValue())) {
					return;
				}
			}
			
			else {
				if(data.getTime().substring(0,PRECISION_MIN).equals(
						series.getData().get(series.getData().size() - 1).getXValue())) {
						return;
				}
			}
		}
		
		
		if (series.getData().size() >= MAX_SHOWING) {
			series.getData().remove(0, series.getData().size() - MAX_SHOWING);
			series.getData().size();
		}
		
		series.getData().add(
				new XYChart.Data<String, Number> (data.getTime(), data.getValue()));
		
		if (data.getDate() != lastDate.toString()) {
			
			
			lastDate = data.getDate();
			date = ", Dernière mise à jour " + lastDate;
			
			setTitle(title + date);
		}
		
	}
	
	

	/**
	 * Refresh the chart view.
	 *
	 */
	public void refreshChart () {
		this.updateAxisRange();
	}
	
	
	
    /** The chart series */
    private XYChart.Series<String, Number> series = new Series<String, Number>();
    /** The max number of data on the chart at the same time */
    private final  int    MAX_SHOWING 	= 12;
    /** The latest date possible to be shown */
    private static String lastDate 		= "1000-01-01"; 
    /** The date of the last update */
    private static String date 			= "";
    /** the chart title */
    private 	   String title;
    /** The minute precision for the time axis */
    private final  int    PRECISION_MIN = 5;
    /**  The second precision for the time axis */
    private final  int 	  PRECISION_SEC = 8;
   
    
}

