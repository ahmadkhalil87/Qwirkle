package qwirkle.Desktop.Controller;


import javafx.scene.Scene;
import qwirkle.Desktop.Main.Mainstarter;
import qwirkle.Desktop.View.SettingsView;

/**
 * This class controls the functionalities of the settings
 * 
 * @author Houman Mahtabi
 *
 */

public class SettingsController {

	private static Scene scene;

	//Constructor
	public SettingsController(Scene scene){
		this.scene=scene;
	}

	//Changes the size of the mainStage after clicking the fullScreen Checkbox
	public static void chnageFullscreen(Boolean bool){
		if (bool){
			Mainstarter.mainStage.setFullScreen(true);
		}
		else{
			Mainstarter.mainStage.setFullScreen(false);
		}
	}


	public static void canceled(double tmpMusicVol, double tmpSfxVol)
	{
		Mainstarter.model.setMusicVol(tmpMusicVol);
		Mainstarter.model.getMediaPlayerMusic().setVolume(tmpMusicVol);
		Mainstarter.model.setSoundVol(tmpSfxVol);
		if (Mainstarter.settingsView.rootParent.equals(Mainstarter.mainMenuView)) {
			MainMenuController.switchMainMenu();
		}else {
			Mainstarter.gameView.rootPane.getChildren()
							.remove(Mainstarter.settingsView.setsContainer);
			scene.setRoot(Mainstarter.gameView);
		}
	}
	public static void confirmed()
	{
		Mainstarter.model.setTmpMusVol(Mainstarter.model.getMediaPlayerMusic().getVolume());
		Mainstarter.model.setTmpSfxVol(Mainstarter.model.getSoundVol());
		Mainstarter.model.setMusicVol(Mainstarter.model.getMediaPlayerMusic().getVolume());
		Mainstarter.model.setSoundVol(Mainstarter.model.getSoundVol());
		if (Mainstarter.settingsView.rootParent.equals(Mainstarter.mainMenuView)) {
			MainMenuController.switchMainMenu();
		}else {
			Mainstarter.gameView.rootPane.getChildren()
							.remove(Mainstarter.settingsView.setsContainer);
			scene.setRoot(Mainstarter.gameView);
		}
	}



}
