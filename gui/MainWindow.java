/*
 -----------------------------------------------------------------------------------
 Project 	 : Projet PRO
 File     	 : MainWindow.java
 Author(s)   : R. Combremont, M. Dupraz, I. Ounon, P. Sekley, J. Ayoub 
 Date        : 18.03.2016
 Purpose     : Designs and sets the main Window dashboard of the application. 
 remark(s)   : n/a
 Compiler    : jdk 1.8.0_60
 -----------------------------------------------------------------------------------
 */
package gui;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Time;

import data_processing.GenerateFile;
import data_processing.ReceivedData;
import data_processing.ResourceLoader;
import data_processing.UpdateData;
import db.ConnectionForm;
import db.DBConnection;
import db.Data;
import db.Data.Sensor;
import utils.Hours;
import eu.hansolo.enzo.lcd.Lcd;
import eu.hansolo.enzo.lcd.LcdBuilder;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;



/**
 * Class MainWindow represents the application's main window and offers methods 
 * that return field members needed for other use like printing and updating.
 * 
 * This class contains the main method.
 *
 * @author R. Combremont, M. Dupraz, I. Ounon, P. Sekley, J. Ayoub 
 * @date 06.04.2016
 * @version 1.5
 */
public class MainWindow extends Application {
   /* (non-Javadoc)
    * @see javafx.application.Application#start(javafx.stage.Stage)
    */
   @SuppressWarnings("static-access")
   public void start(Stage primaryStage) throws IOException {
	   
	   /**
	    * Setting up the main options for the window
	    */
	   textActiv.setFont  (Font.font("Arial", FontWeight.EXTRA_BOLD, 19));
	   textInactiv.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 19));
	   
	   textAirQualityStatus.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 19));
	   textAirQuality.setFont      (Font.font("Arial", FontWeight.EXTRA_BOLD, 19));

	   final Group rootGroup = new Group();
	   rootGroupCopy 		 = rootGroup;
	   final Scene scene 	 = new Scene(rootGroup, 800, 600, Color.HONEYDEW);

	   primaryStage.setTitle("Station Météo");
	   primaryStage.setResizable(false);
	   primaryStage.setScene(scene);
	   primaryStage.show();
	   primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
    	 @Override
         public void handle(WindowEvent t) {
    		System.exit(0);
            System.out.println("Closing...");
         }
      });

      /**
       * Declaration and definition of all the Text fields.
       */
      final Text text1 = new Text(100,  80, "Météo actuelle");
      final Text text2 = new Text( 60, 350, "Pression atmosphérique");
      final Text text3 = new Text(370,  80, "Humidité");
      final Text text4 = new Text(500, 270, "Statistiques");
      final Text text5 = new Text(600,  80, "Thermomètre");

      text1.setFont(Font.font("Arial", FontWeight.BOLD, 18));
      text2.setFont(Font.font("Arial", FontWeight.BOLD, 18));
      text3.setFont(Font.font("Arial", FontWeight.BOLD, 18));
      text4.setFont(Font.font("Arial", FontWeight.BOLD, 18));
      text5.setFont(Font.font("Arial", FontWeight.BOLD, 18));

      rootGroup.getChildren().add(text1);
      rootGroup.getChildren().add(text2);
      rootGroup.getChildren().add(text3);
      rootGroup.getChildren().add(text4);
      rootGroup.getChildren().add(text5);

      /**
       * This code sets the actual image in place.
       */
      iv.setFitHeight(180);
      iv.setFitWidth(180);
      iv.setX(60);
      iv.setY(100);
      
      /**
       * This code sets the active and inactive image in place
       */
      ivConnect.setX(710);
      ivConnect.setY(10);
      
      ivConnect.setImage(imInactiv);
      rootGroup.getChildren().add(iv);
      rootGroup.getChildren().add(ivConnect);
      rootGroup.getChildren().add(textInactiv);
      rootGroup.getChildren().add(textAirQualityStatus);
      rootGroup.getChildren().add(textAirQuality);

      /**
       * The Menu bars
       */
      final MenuBar menuBar = new MenuBar();
      final Menu menuStation     = new Menu("Station");
      final Menu menuOptions   	 = new Menu("Option");
      final Menu menuAbout     	 = new Menu("A propos");
      final Menu menuCalendar    = new Menu("Calendrier");
      final Menu menuSaveAs      = new Menu("Enregistrer Sous");
      final Menu menuPrevision   = new Menu("Prévision météorologique");
      final Menu menuDuration    = new Menu("Temps de mise à jour des graphes");

      /**
       * The menu items
       */
      final MenuItem miExit       	 = new MenuItem("Quitter");
      final MenuItem miOneday     	 = new MenuItem("Demain");
      final MenuItem miConnection 	 = new MenuItem("Connexion");
      final MenuItem miDisconnection = new MenuItem("Déconnexion");
      final MenuItem miDuration_1 	 = new MenuItem(String.valueOf(
    		  				UpdateData.getDuration1Default().toMinutes()) + "min");
      final MenuItem miDuration_2 	 = new MenuItem(String.valueOf(
    		  				UpdateData.getDuration2().toMinutes()) + "min");
      final MenuItem miDuration_3 	 = new MenuItem(String.valueOf(
    		  				UpdateData.getDuration3().toMinutes()) + "min");
      final MenuItem miDuration_4 	 = new MenuItem(String.valueOf(
    		  				UpdateData.getDuration4().toMinutes()) + "min");
      final MenuItem miDuration_5 	 = new MenuItem(String.valueOf(
    		  				UpdateData.getDuration5().toMinutes()) + "min");
      final MenuItem miDuration_6 	 = new MenuItem(String.valueOf(
    		  				UpdateData.getDuration6().toMinutes()) + "min");
   
      menuStation.getItems().addAll(miConnection, miDisconnection, menuSaveAs, 
    		  																miExit);
      menuOptions.getItems().addAll(menuPrevision, menuDuration);
      menuPrevision.getItems().addAll(miOneday);
      menuDuration.getItems().addAll(miDuration_1, miDuration_2, miDuration_3, 
									 miDuration_4, miDuration_5, miDuration_6);

 
      menuBar.getMenus().addAll(menuStation, menuOptions, menuCalendar , menuAbout);
      ((Group) scene.getRoot()).getChildren().addAll(menuBar);
      
      miDisconnection.setDisable(true);
      
      /** If the user is not connected he can not make a forecast */
      if(!isConnected){
    	  miOneday.setDisable(true);
      }
      
      
      /**
       * This event handler (click on the menu item to change update frequency)
       * will set the frequency to the default value 30seconds.
       */
      miDuration_1.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
        	  	if(UpdateData.getPt() != null) {
        	  		UpdateData.getPt().stop();
        	  		UpdateData.getPt().getChildren().remove(1);
        	  		UpdateData.getTimelineLcs().setPeriod(
        	  							UpdateData.getDurationToStart(), 
        	  							UpdateData.getDuration1Default());
        	  		UpdateData.getPt().getChildren().add(1, 
        	  							UpdateData.getTimelineLcs().getTimeline());
        	  		UpdateData.getPt().play();
      			}
          }
       });
      
      
      /**
       * This event handler (click on the menu item to change update frequency)
       * will set the frequency to 5 minutes.
       */
      miDuration_2.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
        	  	if(UpdateData.getPt() != null) {
        	  		UpdateData.getPt().stop();
        	  		UpdateData.getPt().getChildren().remove(1);
        	  		UpdateData.getTimelineLcs().setPeriod(
        	  							UpdateData.getDurationToStart(), 
        	  							UpdateData.getDuration2());
        	  		UpdateData.getPt().getChildren().add(1, 
        	  							UpdateData.getTimelineLcs().getTimeline());
        	  		UpdateData.getPt().play();
      			}
          }
       });
      
      
      /**
       * This event handler (click on the menu item to change update frequency)
       * will set the frequency to 30 minutes.
       */
      miDuration_3.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
        	  	if(UpdateData.getPt() != null) {
        	  		UpdateData.getPt().stop();
        	  		UpdateData.getPt().getChildren().remove(1);
        	  		UpdateData.getTimelineLcs().setPeriod(
        	  							UpdateData.getDurationToStart(), 
        	  							UpdateData.getDuration3());
        	  		UpdateData.getPt().getChildren().add(1, 
        	  							UpdateData.getTimelineLcs().getTimeline());
        	  		UpdateData.getPt().play();
      			}
          }
       });
      
      
      /**
       * This event handler (click on the menu item to change update frequency)
       * will set the frequency to 1 jour.
       */
      miDuration_4.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
        	  	if(UpdateData.getPt() != null) {
        	  		UpdateData.getPt().stop();
        	  		UpdateData.getPt().getChildren().remove(1);
        	  		UpdateData.getTimelineLcs().setPeriod(
        	  							UpdateData.getDurationToStart(), 
        	  							UpdateData.getDuration4());
        	  		UpdateData.getPt().getChildren().add(1, 
        	  							UpdateData.getTimelineLcs().getTimeline());
        	  		UpdateData.getPt().play();
      			}
          }
       });
      
      
      /**
       * This event handler (click on the menu item to change update frequency)
       * will set the frequency to 2 hours.
       */
      miDuration_5.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
        	  	if(UpdateData.getPt() != null) {
        	  		UpdateData.getPt().stop();
        	  		UpdateData.getPt().getChildren().remove(1);
        	  		UpdateData.getTimelineLcs().setPeriod(
        	  							UpdateData.getDurationToStart(), 
        	  							UpdateData.getDuration5());
        	  		UpdateData.getPt().getChildren().add(1, 
        	  							UpdateData.getTimelineLcs().getTimeline());
        	  		UpdateData.getPt().play();
      			}
          }
       });
      
      
      /**
       * This event handler (click on the menu item to change update frequency)
       * will set the frequency to 4 hours.
       */
      miDuration_6.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
        	  	if(UpdateData.getPt() != null) {
        	  		UpdateData.getPt().stop();
        	  		UpdateData.getPt().getChildren().remove(1);
        	  		UpdateData.getTimelineLcs().setPeriod(
        	  									UpdateData.getDurationToStart(), 
        	  									UpdateData.getDuration6());
        	  		UpdateData.getPt().getChildren().add(1, 
        	  				UpdateData.getTimelineLcs().getTimeline());
        	  		UpdateData.getPt().play();
      			}
          }
       });
      
      
      /**
       * The menu item for the menu About and its content which is the application's
       * copyright and version.
       */
      final MenuItem miAboutInfo = new MenuItem("Info");
      menuAbout.getItems().addAll(miAboutInfo);

      
      /**
       * The dialog box to be shown once pressed on the Info menu item.
       */
      final Dialog<Text> dialogInfo = new Dialog<Text>();
      dialogInfo.getDialogPane().setPrefSize(130, 200);
      dialogInfo.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
      Node closeButtonDialogInfo = dialogInfo.getDialogPane().lookupButton(
              ButtonType.CLOSE);
      closeButtonDialogInfo.managedProperty().bind(
              closeButtonDialogInfo.visibleProperty());

      final Text textDialogInfo = new Text("Station Météo \nVersion 1.0"
              							 + "\n\nCopyrights ©"
              							 + "\nPRO HEIG-VD"
              							 + "\n2016"
              							 + "\n\nR. Combremont"
              							 + "\nM. Dupraz"
              							 + "\nI. Ounon"
              							 + "\nP. Sekley"
              							 + "\nJ. Ayoub");

      textDialogInfo.setFont(Font.font("Verdana", 12));
      textDialogInfo.setFill(Color.STEELBLUE);

      dialogInfo.setGraphic(textDialogInfo);

      /**
       * Shows the dialog box
       */
      miAboutInfo.setOnAction(new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent event) {
            /**
             * Just to make the close button close the dialog box
             */
            closeButtonDialogInfo.setVisible(false);
            dialogInfo.showAndWait();
         }
      });
      
      
      /**
       * Closes the application if clicked
       */
      miExit.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
        	  primaryStage.close();
          }
       });
      
      
      /** 
       * Show the forvasting for the next day by showing a text.  
       */
      miOneday.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {

        	  //Label boxLabel = new Label("Hello");
        	  Text forcastInfo = new Text();

        	  StackPane forcastLayout = new StackPane();
        	  forcastLayout.getChildren().add(forcastInfo);

        	  Scene forcastScene = new Scene(forcastLayout, 500, 100);

        	  Stage forcastStage = new Stage();
        	  forcastStage.setTitle("Prévision de demain");
        	  forcastStage.setScene(forcastScene);

        	  Forcasting forcast = new Forcasting();
        	  int PressureVariation = forcast.makeForcasting();

        	  switch(PressureVariation){ 
        	  
        	  /** No changes for the weather */
        	  case 0:
        		  forcastInfo.setFill(Color.CORNFLOWERBLUE);
        		  forcastInfo.setFont(Font.font(null, FontWeight.EXTRA_BOLD, 20));
        		  forcastInfo.setText("Demain il va faire le même temps "
        		  												+ "qu'aujourd'hui");
        		  break;
        		  
        	  /** The weather is Good */
        	  case 1:
        		  forcastInfo.setFill(Color.CORNFLOWERBLUE);
        		  forcastInfo.setFont(Font.font(null, FontWeight.EXTRA_BOLD, 20));
        		  forcastInfo.setText("Demain il va faire beau");
        		  break;
        		  
              /** The weather is bad */
        	  case 2:
        		  forcastInfo.setFill(Color.RED);
        		  forcastInfo.setFont(Font.font(null, FontWeight.EXTRA_BOLD, 20));
        		  forcastInfo.setText("Demain il va faire mauvais temps");
        		  break;
        		  
              /** The weather is  really bad */
        	  case 3:
        		  forcastInfo.setFill(Color.RED);
        		  forcastInfo.setFont(Font.font(null, FontWeight.BOLD, 20));
        		  forcastInfo.setText("Demain il va faire très mauvais temps");
        		  break;
        	  }

              forcastStage.show();     	  
          }
       });
 

      /**
       * The save as menu event handler
       */
      menuSaveAs.setOnAction(new EventHandler<ActionEvent>() {
    	  @Override
          public void handle(ActionEvent event) {
        	  final GenerateFile myFile = new GenerateFile();

        	  FileChooser fileChooser = new FileChooser();
        	  fileChooser.setTitle("Enregistrer sous ...");
        	  fileChooser.setInitialDirectory(new File(
        			  							System.getProperty("user.home"))); 

        	  FileChooser.ExtensionFilter extFilterPdf = 
        			  new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        	  fileChooser.getExtensionFilters().add(extFilterPdf);

        	  FileChooser.ExtensionFilter extFilterJpeg = 
        		   new FileChooser.ExtensionFilter("JPEG files (*.jpeg)", "*.jpeg");
        	  fileChooser.getExtensionFilters().add(extFilterJpeg);

        	  File file = fileChooser.showSaveDialog(primaryStage);
        	  if (file != null) {
        		  
        		  /** Saves the file into pdf format */
        		  if(fileChooser.getSelectedExtensionFilter()
        				  		.getDescription()
        				  		.equals("PDF files (*.pdf)")) {
        			  try {
        				  myFile.toPDF(getTabPane(),file.toPath().toString());
        				  Alert alert = new Alert(AlertType.INFORMATION);
        				  alert.setTitle("Information Dialog");
        				  alert.setHeaderText(null);
        				  alert.setContentText("Sauvegarde en PDF Réussi !!!");
        				  alert.showAndWait();
        			  } catch (Exception e1) {
        				  e1.printStackTrace();
        			  }
        		  }
        		  /** Savs the file into jpeg format */
        		  if(fileChooser.getSelectedExtensionFilter()
        				  		.getDescription()
        				  		.equals("JPEG files (*.jpeg)")) {
        			  myFile.toJpeg(getTabPane(), file.toPath().toString());
        			  Alert alert = new Alert(AlertType.INFORMATION);
        			  alert.setTitle("Information Dialog");
        			  alert.setHeaderText(null);
        			  alert.setContentText("Sauvegarde en JPEG Résussi !!!");
        			  alert.showAndWait();
        		  }

        	  }
    		  
    	  }

      });

      
      /**
       * Creates a connection to a data base in order to fetch data and display them
       */ 
      miConnection.setOnAction(new EventHandler<ActionEvent>() {
    	  @Override public void handle(ActionEvent e) {
    		  Stage 		stage 			  = new Stage();
    		  GridPane 		root 			  = new GridPane();
    		  HBox 	   		btnPanel 		  = new HBox(12);
    		  Label    		lblTitle 		  = new Label("Connexion");
    		  Label 		lblConnexionName  = new Label("Connection Name:");
    		  TextField 	tfdConnectionName = new TextField();
    		  Label 		lblHostName 	  = new Label("Hostname:");
    		  TextField 	tfdHostname 	  = new TextField();
    		  Label 		lblPort 		  = new Label("Port:");
    		  TextField 	tfdPort 		  = new TextField();
    		  Label 		lblUsername 	  = new Label("Username:");
    		  TextField 	tfdUsername 	  = new TextField();
    		  Label 		lblPassword 	  = new Label("Password:");
    		  Button		btnLogin 		  = new Button("Login");
    		  Button 		btnCancel 		  = new Button("Cancel");
    		  PasswordField pwfPassword 	  = new PasswordField();

    		  stage.setTitle("Connexion à la base de données");
    		  
    		  /** Sets color of the stage */
    		  Background backGroundColor = new Background(
    				  new BackgroundFill(Color.LIGHTSKYBLUE,
    				  CornerRadii.EMPTY,
    				  null ));
    		  root.setBackground(backGroundColor);
    		  /** Bind to avoid errors */
    		  pwfPassword.disableProperty().bind(
    				  						tfdUsername.textProperty().isEmpty());
    		  btnLogin.disableProperty().bind(
    				  				   tfdConnectionName.textProperty().isEmpty()
    				  				   .or(tfdUsername.textProperty().isEmpty())
    				  				   .or(pwfPassword.textProperty().isEmpty()
    				  				   .or(tfdPort.textProperty().isEmpty()
    				  				   .or(tfdHostname.textProperty().isEmpty()))));

    		  /** Title */
    		  lblTitle.setFont(Font.font("System", FontWeight.BOLD, 20));
    		  lblTitle.setTextFill(Color.rgb(80, 80, 180));
    		  root.add(lblTitle, 0, 0, 2, 1);
    		  GridPane.setHalignment(lblTitle, HPos.CENTER);
    		  GridPane.setMargin(lblTitle, new Insets(0, 0, 10,0));

    		  /** Username (label and text-field) */
    		  tfdUsername.setPrefColumnCount(20);
    		  root.add(lblConnexionName, 0, 1);
    		  root.add(tfdConnectionName, 1, 1);
    		  root.add(lblHostName, 0, 2);
    		  root.add(tfdHostname, 1, 2);
    		  root.add(lblPort, 0, 3);
    		  root.add(tfdPort, 1, 3);
    		  root.add(lblUsername, 0, 4);
    		  root.add(tfdUsername, 1, 4);
   
    		  /** Password (label and text-field)*/
    		  pwfPassword.setPrefColumnCount(12);
    		  root.add(lblPassword, 0, 5);
    		  root.add(pwfPassword, 1, 5);
    		  GridPane.setFillWidth(pwfPassword, false);
    		  /** Button panel */
    		  btnPanel.getChildren().add(btnLogin);
    		  btnPanel.getChildren().add(btnCancel);
    		  btnPanel.setAlignment(Pos.CENTER_RIGHT);
    		  root.add(btnPanel, 1, 6);
    		  GridPane.setMargin(btnPanel, new Insets(10, 0, 0,0));

    

    		  /** GridPane properties */
    		  root.setAlignment(Pos.CENTER);
    		  root.setPadding(new Insets(20));
    		  root.setHgap(10);
    		  root.setVgap(15);

    		  stage.setMinWidth(300);
    		  stage.setMinHeight(350);
    		  stage.setScene(new Scene(root));
    		  
    		  /** Sets shadow on mouse event */
    		  btnLogin.addEventHandler(MouseEvent.MOUSE_ENTERED, 
    				  								new EventHandler<MouseEvent>(){
				@Override
				public void handle(MouseEvent event) {
					btnLogin.setEffect(new DropShadow());
				}
    		  });
    		  btnLogin.addEventHandler(MouseEvent.MOUSE_EXITED, 
    				  								new EventHandler<MouseEvent>(){
  				@Override
  				public void handle(MouseEvent event) {
  					btnLogin.setEffect(null);
  				}
      		  });
    		  
    		  btnCancel.addEventHandler(MouseEvent.MOUSE_ENTERED, 
    				  								new EventHandler<MouseEvent>(){
  				@Override
  				public void handle(MouseEvent event) {
  					btnCancel.setEffect(new DropShadow());
  				}
      		  });
    		  btnCancel.addEventHandler(MouseEvent.MOUSE_EXITED, 
    				  								new EventHandler<MouseEvent>(){
    				@Override
    				public void handle(MouseEvent event) {
    					btnCancel.setEffect(null);
    				}
        		  });
    		  
    		  btnLogin.setOnAction(new EventHandler<ActionEvent>(){

				@Override
				public void handle(ActionEvent event) {
					
					 /** We try to get the value of the port which must be an 
					  *  Integer value, if we couldn't we sent an error alert to the 
					  *  user in order to give a valid value.
					  */ 
					DBConnection testConnection = null;
					
					try{
						portNumber = Integer.parseInt(tfdPort.getText());

						connectionForm.setConnectionForm(
								tfdConnectionName.getText(), 
								tfdHostname.getText(), 
								portNumber,
								tfdUsername.getText(), 
								pwfPassword.getText());
						
						/** We try to get a connection to the database */
						System.out.println("Waiting to connect");
						if (connectionForm.getFormStatus() && 
											(MainWindow.getIsConnected()==false)) {
							try{
								testConnection = 
								   new DBConnection(MainWindow.getConnectionForm());
								testConnection.close();
								isConnected = true;
								System.out.println("Connection Successful");

								/** 
								 * Closes the window when login button is clicked.
								 */
								stage.close();

								Alert alert = new Alert(AlertType.CONFIRMATION);
								alert.setTitle("Confirmation");
								alert.setHeaderText(null);
								alert.setContentText("Connexion réussi !");
								alert.show();
								
								/*Update the connectivity icon image */
				        		rootGroup.getChildren().remove(imInactiv);
				        		rootGroup.getChildren().remove(textInactiv);
				        		ivConnect.setImage(imActiv);
				        		rootGroup.getChildren().add(textActiv);
				        		
				        		miDisconnection.setDisable(false);
				        	    miOneday.setDisable(false);
				        	
								/** Starts updating process. */
								@SuppressWarnings("unused")
								UpdateData updateData = new UpdateData();

							}
							catch(SQLException e){
								System.out.println("Connection failed, try again");
								
								/** Resets the connection form for the next try */
								MainWindow.getConnectionForm()
										  .resetConnectionForm();
								
								/** Shows an alert box to the user */
				    			Alert alert = new Alert(AlertType.ERROR);
			          			alert.setTitle("Erreur");
			          			alert.setHeaderText(null);
			          			alert.setContentText("Echec de connexion. "
			          							   + "Veuillez recommencer svp !");
			          			alert.show();
							}
							finally{
								/** Disables the menu connection in order to
								 * disconnect before trying to get connected again.
								 */
								miConnection.setDisable(true);
							}

						}

					}
					/** The port value is not valid and the user have to correct the
					 *  port number.
					 */
					catch(Exception e){
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Erreur Saisie");
						alert.setHeaderText(null);
						alert.setContentText("Le port doit être de valeur "
																	+ "numérique");
						alert.show();
					}						
				}
    			  
    		  });
    		  
    		  /** A click on the cancel button closes the window */
    		  btnCancel.setOnAction(actionEvent -> stage.close());     		  
    		  stage.show();
    	  }
      });
      

      /**
       * The menu item for the menu Calendar and its content which is the Calendar
       * view
       */
      final MenuItem miCalendarShow = new MenuItem("Afficher");
      menuCalendar.getItems().add(miCalendarShow);
      
      
      miCalendarShow.setOnAction(new EventHandler<ActionEvent>(){


    	  public void handle(ActionEvent event) {
    		  /** Date Picker. */
    		  DatePicker dPicker = new DatePicker();
    		  dPicker.setPrefSize(200, 30);
    		  dPicker.setShowWeekNumbers(true);
    		  Stage dateStage = new Stage();
    		  dateStage.setTitle("Calendrier");
    		  HBox hbox = new HBox(dPicker);
    		  Scene scene = new Scene(hbox, 270, 30);
    		  dateStage.setScene(scene);
    		  
    		  Button searchButton = new Button("Chercher");
    		  hbox.getChildren().add(searchButton);
    		  if(!isConnected) {
    			  searchButton.setDisable(true);
    		  }
    		  dPicker.setOnAction(e -> {
    			  LocalDate date = dPicker.getValue();
    			  
    			  /** Event handler when clicked on the search button */
    			  searchButton.setOnAction(new EventHandler<ActionEvent>() {
    				  public void handle(ActionEvent event) {
    					  if (!Data.checkDate(date)){
    						  Alert alert = new Alert(Alert.AlertType.ERROR);
    						  alert.setHeaderText(null);
    						  alert.setContentText("Aucune données recupérée au " 
    								  										+ date);
    						  alert.showAndWait();
    					  }
    					  else {
    						  Button saveButton  = new Button("Enregistrer");
    						  final Stage dialog = new Stage();

    						  HBox hbox = new HBox();
    						  hbox.setAlignment(Pos.BASELINE_RIGHT);
    						  hbox.getChildren().add(saveButton);

    						  SplitPane splitPane1 = new SplitPane();
    						  splitPane1.setOrientation(Orientation.VERTICAL);
    						  SplitPane splitPane2 = new SplitPane();
    						  splitPane2.setOrientation(Orientation.VERTICAL);

    						  /** First the entire Data is stored in a arraylist */
    						  ArrayList<Data> 	dataTemperatureList = 
    								  							new ArrayList<>();
    						  ArrayList<Data> 	dataPressureList 	= 
    								  							new ArrayList<>();
    						  ArrayList<Data> 	dataHumidityList 	= 
    								  							new ArrayList<>();
    						  ArrayList<Data>   dataAirQualityList  = 
    								  							new ArrayList<>();
    						  ArrayList<String> hoursList 		    = 
    								  						Hours.getHoursList();
    						  
    						  /** Then for each hour the average is only kept */
    						  for (int i = 0; i < hoursList.size() - 1; ++i){
    							  ReceivedData data = new ReceivedData(
    									  date,Time.valueOf(hoursList.get(i)), 
    									  		Time.valueOf(hoursList.get(i + 1)));
    							  dataTemperatureList.add(data.getSensorDataAverage(
    									  					Sensor.TEMPERATURE));
    							  dataPressureList.add(data.getSensorDataAverage   (
    									  					Sensor.PRESSURE));
    							  dataHumidityList.add(data.getSensorDataAverage   (
    									  					Sensor.HUMIDITY));
    							  dataAirQualityList.add(data.getSensorDataAverage (
    									  					Sensor.AIR_QUALITY));
    						  }

    						  
    						  /** Setting-up a line chart with the average values 
    						   * for the temperature. 
    						   */
    						  LineChartStat lcTemperature = 
    							(LineChartStat)createLineChart(
    										  		"Température",
    						    		  			"Variation de la température",
    						    		  			"Heures",
    						    		  			"Temperature [°C]",
    						    		  			450,
    						    		  			290,
    						    		  			dataTemperatureList);
    						  
    						  /** Setting-up a line chart with the average values 
    						   * for the humidity.
    						   */
    						  LineChartStat lcHumidity = 
    							(LineChartStat) createLineChart(
    												"Humidité",
    												"Variation de l'humidité",
    												"Heures",
    												"Humidité [%]",
    												450,
    												290,
    												dataHumidityList);

    						  /** Setting-up a line chart with the average values 
    						   * for the pressure. 
    						   */
    						  LineChartStat lcPressure = 
    							(LineChartStat) createLineChart(
    												"Pression",
    												"Variation de la pression",
    												"Heures",
    												"Pression [hPa]",
    												450,
    												290,
    												dataPressureList);
    						  
    						  /** Setting-up a line chart with the average values 
    						   * for the air quality. 
    						   */
    						  LineChartStat lcAirQuality = 
    							(LineChartStat) createLineChart(
    												"Qualité d'air",
    												"Variation de la qualité d'air",
    												"Heures",
    												"indice[0 - 5.5]",
    												450,
    												290,
    												dataAirQualityList);

    						  splitPane1.getItems().addAll(lcTemperature, 
    								  					   lcPressure);
    						  hbox.getChildren().add(splitPane1);
    						  splitPane2.getItems().addAll(lcHumidity, 
    								  					   lcAirQuality);

    						  hbox.getChildren().add(splitPane2);
    						  Group gr = new Group(hbox);
    						  Scene dialogScene = new Scene(gr);
    						  dialog.setTitle("Valeur récupérés au " + date);
    						  dialog.setScene(dialogScene);
    						  dialog.show();
    						  
    						  /** Close the datePicker stage calendar */
    						  dateStage.close();

    						  saveButton.setOnAction(
    								  			new EventHandler<ActionEvent>() {

    							  @Override
    							  public void handle(ActionEvent event) {
    								  final GenerateFile myFile = 
    										  					new GenerateFile();
    								  FileChooser fileChooser = new FileChooser();
    								  fileChooser.setTitle("Enregistrer sous ...");
    								  fileChooser.setInitialDirectory(
    										  new File(System.getProperty(
    												  				"user.home"))); 

    								  FileChooser.ExtensionFilter extFilterJpeg = 
    										  new FileChooser.ExtensionFilter(
    												  		"JPEG files (*.jpeg)", 
    												  		"*.jpeg");
    								  fileChooser.getExtensionFilters().add(
    										  						extFilterJpeg);

    								  File file = fileChooser.showSaveDialog(
    										  						primaryStage);
    								  if (file != null) {
    									  /** Save the file into jpeg format. */
    									  if(fileChooser
    											  .getSelectedExtensionFilter()
    											  .getDescription()
    											  .equals("JPEG files (*.jpeg)")){
    										  myFile.toJpeg(hbox, 
    												  	file.toPath().toString());
    										  Alert alert = new Alert(
    												  		AlertType.INFORMATION);
    										  alert.setTitle("Information Dialog");
    										  alert.setHeaderText(null);
    										  alert.setContentText(
    											 "Sauvegarde en JPEG Résussi !!!");
    										  alert.showAndWait();
    									  }
    								  }
    							  }
    						  });
    					  }
    				  }
    			  });
    		  });
    		  
    		  dateStage.show();
    	  }
    	  /** End of the handle */
      });

      /** Event handler when clicked on the Disconnection menu item */
      miDisconnection.setOnAction(new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
			if(isConnected){
				miConnection.setDisable(false);
				isConnected = false;
				MainWindow.getConnectionForm().resetConnectionForm();
				miDisconnection.setDisable(true);
				resetStation();
				
				/** Updates the connectivity icon image */
				ivConnect.setImage(imInactiv);
        		rootGroup.getChildren().remove(imActiv);
        		rootGroup.getChildren().remove(textActiv);
        		rootGroup.getChildren().add(textInactiv);
			}
		}
      });
      

      /**
       * The Tab Pane and the tabs.
       */
      final TabPane tabPan = new TabPane();
      final Tab tabTemperature = new Tab("Température");
      final Tab tabHumidity = new Tab("Humidité");
      final Tab tabPressure = new Tab("Pression");
      final Tab tabAirQuality = new Tab("Qualité d'air");

      tabPan.getTabs().addAll(tabTemperature, 
    		  				  tabHumidity, 
    		  				  tabPressure, 
    		  				  tabAirQuality);
      tabPan.setLayoutX(350);
      tabPan.setLayoutY(280);
      copyPane = tabPan;

      /**
       * This is an emty tab that could contain initial data.
       */
      ArrayList<Data> dataList = new ArrayList<Data>();
      
     
      /** Creates the temperature line chart of the main dashboard */
      lcsTemperature
              = (LineChartStat) createLineChart("Température",
                      							null,
                      							"Heures",
                      							"Temperature [°C]",
                      							450,
                      							290,
                      							dataList);
      
      /** Creates the humidity line chart of the main dashboard */
      lcsHumidity
              = (LineChartStat) createLineChart("Humidité",
                      							null,
                      							"Heures",
                      							"Humidité [%]",
                      							450,
                      							290,
                      							dataList);

      /** Creates the pressure line chart of the main dashboard */
      lcsPressure
              = (LineChartStat) createLineChart("Pression",
                      							null,
                      							"Heures",
                      							"Pression [hPa]",
                      							450,
                      							290,
                      							dataList);
      
      /** Creates the air quality line chart of the main dashboard */
      lcsAirQuality
              = (LineChartStat) createLineChart("Qualité d'air",
                      							null,
                      							"Heures",
                      							"indice[0 - 5.5]",
                      							450,
                      							290,
                      							dataList);
      
      /** Adding the line charts to the tabs */
      tabTemperature.setContent(lcsTemperature);
      tabHumidity.setContent(lcsHumidity);
      tabPressure.setContent(lcsPressure);
      tabAirQuality.setContent(lcsAirQuality);

      /** It won't be possible to close the line charts */
      tabTemperature.setClosable(false);
      tabHumidity.setClosable(false);
      tabPressure.setClosable(false);
      tabAirQuality.setClosable(false);
      
      /** But it is possible to choose the one to be shown */
      tabPan.getSelectionModel().selectedItemProperty().addListener(
    		    new ChangeListener<Tab>() {
    		        @Override
    		        public void changed(ObservableValue<? extends Tab> ov, 
    		        					Tab t, 
    		        					Tab t1) {

    		        	lcsTemperature.refreshChart();
    		        	lcsHumidity.refreshChart();
    		        	lcsPressure.refreshChart();
    		        	lcsAirQuality.refreshChart();
    		        }
    		    }
    		);
      
      ((Group) scene.getRoot()).getChildren().addAll(tabPan);

      /**
       * The humidity Progress Bar.
       */
      progressTextValue = new Text(430, 150, "%");
      progressTextValue.setFont(Font.font("Arial", FontWeight.BOLD, 20));
      //progressTextValue.setVisible(true);
      pbHumidity.setLayoutX(400);
      pbHumidity.setLayoutY(200);
      pbHumidity.setPrefSize(100, 20);
      pbHumidity.getTransforms().setAll(new Rotate(-90, 0, 0));
      rootGroup.getChildren().add(pbHumidity);
      rootGroup.getChildren().add(progressTextValue);
 
      
      /**
       * Setting-up the temperature LCD.
       */
      lcdTemperature = LcdBuilder.create()
    		  	.prefWidth(220)
    		  	.prefHeight(100)
    		  	.layoutX(560)
    		  	.layoutY(90)
    		  	.decimals(1)
    		  	.styleClass(Lcd.STYLE_CLASS_LIGHTGREEN_BLACK)
    		  	.backgroundVisible(true)
    		  	.valueFont(Lcd.LcdFont.DIGITAL_BOLD)
    		  	.lowerRightTextVisible(true)
    		  	.lowerRightText("PRO-2016")
    		  	.title("Temperature")
    		  	.titleVisible(true)
    		  	.unit("°C")
    		  	.unitVisible(true)
    		   	.build();
      
      rootGroup.getChildren().add(lcdTemperature);
      
      
      /**
       * Setting-up the pressure gauge.
       */
      pressureGauge = new Gauge();
      pressureGauge = GaugeBuilder.create()
    		  			.knobColor(Color.AQUAMARINE)
    		  			.borderPaint(Paint.valueOf("green"))
    		  			.prefSize(310, 200)
    		  			.minValue(0)
    		  			.maxValue(1100)
    		  			.title("Pression")
    		  			.unit("hPa")
    		  			.unit("hPa")
    		  			.shadowsEnabled(true)
    		  			.layoutY(370)
    		  			.build();

      rootGroup.getChildren().add(pressureGauge);
   }
      
 
   
   /**
    * Returns the image for the connection.
 	*
 	* @return ImageView
 	*/
   public static ImageView getIvConnect(){
	   return ivConnect;
   }
         
   
   
   /**
    * This method sets the conectivity icon.
 	*
 	* @param status
 	*/
   public static void updateConnectivityIcon(String status){
	/** Update the connectivity icon image. */
	   if(status.equals("imActiv")){
		   if(MainWindow.getRootGroup().getChildren().contains(textInactiv)){
			   MainWindow.getRootGroup().getChildren().remove(imInactiv);
			   MainWindow.getRootGroup().getChildren().remove(textInactiv);
			   ivConnect.setImage(imActiv);
			   MainWindow.getRootGroup().getChildren().add(textActiv);
		   }
	   }
	   
	   else if(status.equals("imInactiv")){
		   if(MainWindow.getRootGroup().getChildren().contains(textActiv)){
			   MainWindow.getRootGroup().getChildren().remove(imActiv);
			   MainWindow.getRootGroup().getChildren().remove(textActiv);
			   ivConnect.setImage(imInactiv);
			   MainWindow.getRootGroup().getChildren().add(textInactiv);
		   }
	   }
   }
   

  /**
   * Reset the dashboard indicators.
   */
   public static void resetStation(){
	   updateImageView(null);
	   updateLcdTemperature(0.);
	   updatePressureGauge(0.);
	   textAirQualityStatus.setText("");
	   updatePbHumidity(0.);
   }

   /**
    * Updates the actual image view to the one in parameter.
    *
    * @param image
    */
   public static void updateImageView(Image image) {
      iv.setImage(image);
   }
   
   
  /**
   * Updates the connectivity image to the one in parameter.
   *
   * @param image
   */
  public static void updateImageConnect(Image image) {
	  updateImageView(image);
	  
  }
   

  /**
   * Updates the temperature lcd to the value in parameter.
   *
   * @param value
   */
  public static void updateLcdTemperature(double value) {
	  lcdTemperature.setValue(value);
  }
  
  
  /**
   * Updates the pressure gauge to the value in parameter.
   *
   * @param value
   */
  public static void updatePressureGauge(double value) {
	  pressureGauge.setValue(value);
 }
   
   
   /**
    * Updates the humidity progress bar to the value in parameter.
    *
    * @param value
    */
   public static void updatePbHumidity(double value) {
      pbHumidity.setProgress(value/100.);
      double copyValue = pbHumidity.getProgress()*100;
	  String textValue = String.format("%.2f", copyValue);
	  textValue += " %";
      progressTextValue.setText(textValue);
   }

   
  /**
   * Updates the air quality to the value in parameter.
   *
   * @param value
   */
   public static void updateAirQualityText(double value) {
	   if(Double.compare(value, airqualityThreshold) <= 0) {
		   textAirQualityStatus.setText("Bonne");
	   }
	   else{
		   textAirQualityStatus.setText("Mauvaise");
	   }
   }

   
   /**
    *  Adds a data to the line chart passed in parameter.
    *
    * @param lcs
    * @param data
    */
   public static void updateLcs(LineChartStat lcs, Data data) {
      lcs.updateSeries(data);
   }

   
   

   /**
    * Return the temperature line chart.
    *
    * @return LineChartStat
    */
   public static LineChartStat getLcsTemperature() {
	   return lcsTemperature;
   }


   /**
    * Returns the humidity line chart.
    *
    * @return LineChartStat
    */
   public static LineChartStat getLcsHumidity() {
	   return lcsHumidity;
   }


   /**
    * Returns the pressure line chart.
    *
    * @return LineChartStat
    */
   public static LineChartStat getLcsPressure() {
	   return lcsPressure;
   }


   /**
    * Returns the air quality line chart.
    *
    * @return LineChartStat
    */
   public static LineChartStat getLcsAirQuality() {
	   return lcsAirQuality;
   }



   /**
    * Returns the connection form.
    *
    * @return ConnectionForm
    */
   public static ConnectionForm getConnectionForm(){
	   return  connectionForm;
   }

   /**
    * Return the RootGroup copy.
    *
    * @return Group
    */
   public static Group getRootGroup(){
	   return rootGroupCopy;
   }

   
   /**
    * Returns a boolean indicating if the application is connected. 
    *
    * @return boolean
    */
   public static boolean getIsConnected(){
	   return isConnected;
   }



   /**
    * Sets the application connectivity status to true (connected) or false(
    * disconnected).
    *
    * @param status
    */
   public static void setIsConnected(boolean status){
	   isConnected = status;
   }


   

   /**
    * This method creats a line chart graph customized for this application.
    *
    * @param title
    * @param seriesName
    * @param xAxisLabel
    * @param yAxisLabel
    * @param xSize
    * @param ySize
    * @param dataList
    * @return Line Chart
    */
   private LineChart<String, Number> createLineChart(String title,
           											 String seriesName,
           											 String xAxisLabel,
           											 String yAxisLabel,
           											 double xSize,
           											 double ySize,
           											 ArrayList<db.Data> dataList) {
     
	   /** Defining the chart axis */
	   final CategoryAxis xAxis = new CategoryAxis();
	   final NumberAxis   yAxis = new NumberAxis();

	   xAxis.setLabel(xAxisLabel);
       yAxis.setLabel(yAxisLabel);

       LineChartStat lcs = new LineChartStat(title,
              								 seriesName,
              								 xAxis,
              								 yAxis,
              								 dataList);

       lcs.setPrefSize(xSize, ySize);
       lcs.setMaxSize(xSize, ySize);
       lcs.setAnimated(true);
       lcs.setLegendVisible(false);
       xAxis.setAnimated(true);
       yAxis.setAnimated(true);

       return lcs;
   }

   /**
    * This method returns the current tabPane.
    *
    *
    * @return A copy of the actual pane
    */
   private TabPane getTabPane() {
      return copyPane;
   }

   
   /** A copy of the tabPane */
   private 		  TabPane 		 copyPane   = new TabPane();
   /** The actual weather image */
   private static ImageView 	 iv 		= new ImageView();
   /** The connectivity image */
   private static ImageView		 ivConnect  = new ImageView();
   /** The humidity progress bar */
   private static ProgressBar 	 pbHumidity = new ProgressBar();
   /** The text  */
   private static Text   		 progressTextValue;
   /**  */
   private static Lcd 			 lcdTemperature;
   /**  */
   private static Gauge 		 pressureGauge;
   /**  */
   private static LineChartStat  lcsTemperature;
   /**  */
   private static LineChartStat  lcsHumidity;
   /**  */
   private static LineChartStat  lcsPressure;
   /**  */
   private static LineChartStat  lcsAirQuality;
   /**  */
   private static ConnectionForm connectionForm = new ConnectionForm();;
   /**  */
   private static Group          rootGroupCopy  = new Group();
   /**  */
   private static boolean        isConnected    = false;
   /**  */
   private int portNumber;
   /**  */
   private static final double airqualityThreshold = 0.42;
   /** Icon to be place for active and inactive connection */
   private static final Image imActiv = 
		   					new Image(ResourceLoader.load("meteoImages/actif.png"));
   /**  */
   private static final Image imInactiv = 
		   				  new Image(ResourceLoader.load("meteoImages/inactif.png"));
   /** Text to be placed for active connection */
   private static final Text textActiv            = new Text(740, 27, "Actif");
   /** Text to be placed for inactive connection */
   private static final Text textInactiv          = new Text(740, 27, "Inactif");
   /**  */
   private static final	Text textAirQuality       = new Text(310, 27, 
		   														"Qualité d'air : ");
   /**  */
   private static		Text textAirQualityStatus = new Text(440, 27, "");
      


   /**
    * Main method for lunching the user window.
    *
    * @param args
    */
   public static void main(String[] args) {
      Application.launch(MainWindow.class, args);
   }

}
