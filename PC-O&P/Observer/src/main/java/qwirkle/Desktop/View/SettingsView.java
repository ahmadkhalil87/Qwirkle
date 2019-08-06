package qwirkle.Desktop.View;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import qwirkle.Desktop.Controller.SettingsController;
import qwirkle.Desktop.Main.Mainstarter;
import qwirkle.Desktop.Resources.Resources;
import qwirkle.Desktop.View.InGame.GameView;
import qwirkle.Desktop.enumeration.SoundEffects;

/**
 * Panel containing the elements of the Settings menu (i.e. checkBoxed, Sliders,...).
 * 
 * @ author Youssef Ben Ameur
 * @ author Houman Mahtabi
 */

public class SettingsView extends Parent{
	
	  private Scene scene;
	  public static BorderPane setsContainer;
	  public static StackPane rootPane;
	  
	  public static Parent rootParent;
	  
//	  private Label lblMyPlayerName;
//	  private Label lblConnected;

	@SuppressWarnings("restriction")
	public SettingsView(Scene scene){
		
		this.scene=scene;
		
		// Loading Style sheet
		Font.loadFont(Resources.el_font, 16);
		getStylesheets().add(Resources.Css_SettingsView);
		
		// Defining menu title
		Text mainTitle = new Text("Settings");
		mainTitle.setId("Title");
		
		// Defining menu titles & Sliders
		Text gTitle = new Text("Graphic");
		Text sTitle = new Text ("Sound");
		gTitle.setId("Title");
		sTitle.setId("Title");
		
		Label musLabel = new Label("Mus Vol.");
		Slider musSlider = new Slider();
		musSlider.setMin(0);
		musSlider.setMax(100);
		musSlider.setValue(Mainstarter.model.getMusicVol()*100);
		Mainstarter.model.setTmpMusVol(musSlider.getValue()); 		
		
		musSlider.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
	          SoundEffects.BUTTON_CLICK.play(Mainstarter.model.getSoundVol());
	        });
		musSlider.valueProperty().addListener(new ChangeListener<Number>() {
			 
	         @Override
	         public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
	        	double value = (Double) newValue;
	        	Mainstarter.model.setMusicVol(value/100);
	        	Mainstarter.model.getMediaPlayerMusic().setVolume(value/100);
            }
        });
		
		Label sfxLabel = new Label("SFX Vol.");
		Slider sfxSlider = new Slider();
		sfxSlider.setMin(0);
		sfxSlider.setMax(100);
		sfxSlider.setValue(Mainstarter.model.getSoundVol()*100);
		Mainstarter.model.setTmpSfxVol(sfxSlider.getValue()); 		

		sfxSlider.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
	          SoundEffects.BUTTON_CLICK.play(Mainstarter.model.getSoundVol());
	        });
		
		sfxSlider.valueProperty().addListener(new ChangeListener<Number>() {
			 
	         @Override
	         public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
	        	double value = (Double) newValue;
	        	Mainstarter.model.setSoundVol(value/100);	        	
           }        
       });
		
		HBox conMusSettings = new HBox(5);
		conMusSettings.setAlignment(Pos.CENTER);
		conMusSettings.getChildren().addAll(musLabel, musSlider);
		
		HBox conSfxSettings = new HBox(5);
		conSfxSettings.setAlignment(Pos.CENTER);
		conSfxSettings.getChildren().addAll(sfxLabel, sfxSlider);
		
		VBox conSoundMenu = new VBox (10);
		conSoundMenu.setAlignment(Pos.CENTER);
		conSoundMenu.getChildren().addAll(conMusSettings, conSfxSettings);
		
		VBox conSound = new VBox(10);
		conSound.setAlignment(Pos.CENTER);
		conSound.getChildren().addAll(sTitle, createHSeparator(), conSoundMenu, createHSeparator());
		
		// Defining elements & container for Graphic menu
		CheckBox fullScreen = new CheckBox();
		fullScreen.setMinHeight(22);
		fullScreen.setPrefWidth(150);
		fullScreen.setText("Fullscreen");
		fullScreen.setSelected(true);
		
		fullScreen.setOnAction(e -> { 
			SoundEffects.BUTTON_CLICK.play(Mainstarter.model.getSoundVol()); 
		SettingsController.chnageFullscreen(fullScreen.isSelected());
		});
		
		CheckBox showGrid = new CheckBox();
		showGrid.setMinHeight(22);
		showGrid.setPrefWidth(150);
		showGrid.setText("Show Grid");
		
		showGrid.setOnAction(e -> SoundEffects.BUTTON_CLICK.play(Mainstarter.model.getSoundVol()));
		
		VBox cbContainer = new VBox(10);
		cbContainer.setAlignment(Pos.CENTER);
		cbContainer.getChildren().setAll(fullScreen, showGrid);

		VBox gContainer = new VBox(10);
		gContainer.setAlignment(Pos.CENTER);
		gContainer.getChildren().setAll(gTitle, createHSeparator(),
											cbContainer, createHSeparator());
		gContainer.setId("gContainer");
		conSound.setId("sContainer");
		
		// Container for sound and graphic
		HBox settingElemContainer = new HBox(25);
		settingElemContainer.setAlignment(Pos.CENTER);
		settingElemContainer.getChildren().addAll(conSound, createVSeparator(),
												gContainer);
		
		// Defining buttons and their container
		Button btnConfirm = new Button("Confirm");
		Button btnCancel =  new Button("Cancel");
		btnConfirm.setMaxHeight(30);
		btnCancel.setMaxHeight(30);
		
		btnConfirm.setOnAction(e -> { SoundEffects.BUTTON_CLICK.play(Mainstarter.model.getSoundVol());
							SettingsController.confirmed(); 
							});		
		btnCancel.setOnAction(e -> { SoundEffects.BUTTON_CLICK.play(Mainstarter.model.getSoundVol());
							sfxSlider.setValue(Mainstarter.model.getTmpSfxVol()*100);
							musSlider.setValue(Mainstarter.model.getTmpMusVol()*100);
							SettingsController.canceled(Mainstarter.model.getTmpMusVol(),
																Mainstarter.model.getTmpSfxVol());
							});
		
		// button container
		HBox btnContainer = new HBox(15);
		btnContainer.prefWidthProperty().bind(settingElemContainer.widthProperty());
		btnContainer.setAlignment(Pos.CENTER);
		btnContainer.getChildren().addAll(btnConfirm, btnCancel);
		
		// Main container for all items of the menu
		setsContainer = new BorderPane();
		setsContainer.setId("setsContainer");
		setsContainer.setPadding(new Insets (10, 10, 15, 10));
		setsContainer.maxWidthProperty().bind(this.scene.widthProperty().multiply(0.5));
		setsContainer.maxHeightProperty().bind(this.scene.heightProperty().multiply(0.4));
		
		// Positioning the settings controllers in the middle
		setsContainer.setCenter(settingElemContainer);
		BorderPane.setAlignment(settingElemContainer, Pos.CENTER);
		
		// positioning the buttons at the bottom
		setsContainer.setBottom(btnContainer);

		// positioning the title at the top
		setsContainer.setTop(mainTitle);
		BorderPane.setAlignment(mainTitle, Pos.CENTER);
		

		
		// Container to be added to the mainView 
		rootPane = new StackPane();
		rootPane.setId("rootPane");
		
		// Setting (auto scaling) Background Image for Border Pane
	    Image imgBackground = new Image(Resources.bgImage2);
	    BackgroundImage backgroundImage = new BackgroundImage(imgBackground, BackgroundRepeat.NO_REPEAT,
	        BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
	        new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true));
	    Background background = new Background(backgroundImage);
	    rootPane.setBackground(background);
		
		rootPane.getChildren().add(setsContainer);
		rootPane.setAlignment(Pos.CENTER);
		rootPane.prefWidthProperty().bind(this.scene.widthProperty());
		rootPane.prefHeightProperty().bind(this.scene.heightProperty());
		
		//ab hier fänt miene neue Code----------------

		Font.loadFont(Resources.el_font, 16);
		getStylesheets().add(Resources.Css_Mmview);

		this.getChildren().addAll(rootPane);
	}
	

	  private Line createVSeparator() {
		    Line separator = new Line();
		    separator.setEndY(150);
		    separator.setStroke(Color.DARKGREY);
		    return separator;
		  }
	  
	  private Line createHSeparator() {
		    Line separator = new Line();
		    separator.setEndX(250);
		    separator.setStroke(Color.DARKGREY);
		    return separator;
		  }

}
