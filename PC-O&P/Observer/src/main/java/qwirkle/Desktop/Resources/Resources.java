package qwirkle.Desktop.Resources;

import javafx.scene.text.Font;
import qwirkle.Desktop.entity.Tile;


/**
 * This class defines the paths to the resources used (i.e. Css files, Images,...)
 * 
 * 
 * @author Youssef Ben Ameur
 * @aothor Houman Mahtabi
 *
 */
public class Resources {

	public static final String CSS_Folder = "/CSS";
	public static final String IMAGE_FOLDER = "/Images";
	public static final String Font_Folder = "/Fonts";
	public static final String MUSIC_FOLDER = "/music/";
	
	
	//public static final String Css_Test = 
	//		Resources.class.getResource(CSS_Folder + "/Test.css").toExternalForm();
	  
	public static final String Css_Mmview = 
			Resources.class.getResource(CSS_Folder + "/MainMenuView.css").toExternalForm();
	
	public static final String Css_ContolBar = 
			Resources.class.getResource(CSS_Folder + "/ContolBar.css").toExternalForm();
	
	public static final String Css_SettingsView =
			Resources.class.getResource(CSS_Folder + "/SettingsView.css").toExternalForm();
	
	public static final String Css_SelectGame =
			Resources.class.getResource(CSS_Folder + "/SelectGameView.css").toExternalForm(); 
	
	public static final String Css_SelectServer =
			Resources.class.getResource(CSS_Folder + "/SelectServerView.css").toExternalForm();
	
	public static final String Css_InGame =
			Resources.class.getResource(CSS_Folder + "/inGameView.css").toExternalForm();
	
	public static final String Css_ChatView =
			Resources.class.getResource(CSS_Folder + "/ChatView.css").toExternalForm();
	
	public static final String Css_PlayerAdapter =
			Resources.class.getResource(CSS_Folder + "/PlayerAdapter.css").toExternalForm();
	
	public static final String Css_BagView =
			Resources.class.getResource(CSS_Folder + "/BagView.css").toExternalForm();
	public static final String bagImage =
			Resources.class.getResource(IMAGE_FOLDER + "/bag.png").toExternalForm();
	
	public static final String btnTradeImage =
			Resources.class.getResource(IMAGE_FOLDER + "/btn_trade.png").toExternalForm();
	
	public static final String btnTradeGreyImage =
			Resources.class.getResource(IMAGE_FOLDER + "/btn_trade_grey.png").toExternalForm();
	
	public static final String btnUndoImage =
			Resources.class.getResource(IMAGE_FOLDER + "/btn_undo.png").toExternalForm();
	
	public static final String btnUndoGreyImage =
			Resources.class.getResource(IMAGE_FOLDER + "/btn_undo_grey.png").toExternalForm();
	
	public static final String btnDoneImage =
			Resources.class.getResource(IMAGE_FOLDER + "/btn_done.png").toExternalForm();
	
	public static final String btnDoneGreyImage =
			Resources.class.getResource(IMAGE_FOLDER + "/btn_done_grey.png").toExternalForm();
	
	public static final String btnMenuImage =
			Resources.class.getResource(IMAGE_FOLDER + "/btn_menu.png").toExternalForm();
	
	public static final String btnViewOnImage =
			Resources.class.getResource(IMAGE_FOLDER + "/btn_view_on.png").toExternalForm();
	
	public static final String btnViewOffImage =
			Resources.class.getResource(IMAGE_FOLDER + "/btn_view_off.png").toExternalForm();
	
	public static final String btnSettingsImage =
			Resources.class.getResource(IMAGE_FOLDER + "/btn_settings.png").toExternalForm();
	
	public static final String btnExitImage =
			Resources.class.getResource(IMAGE_FOLDER + "/btn_exit.png").toExternalForm();
	
	public static final String btnBackgroundImage =
			Resources.class.getResource(IMAGE_FOLDER + "/btn_container.png").toExternalForm();
	
	public static final String btnPauseImage =
			Resources.class.getResource(IMAGE_FOLDER + "/btn_pause.png").toExternalForm();
	
	public static final String btnResumeImage =
			Resources.class.getResource(IMAGE_FOLDER + "/btn_resume.png").toExternalForm();
	
	public static final String bgImage =
			Resources.class.getResource(IMAGE_FOLDER + "/Background3.jpg").toExternalForm();
	
	public static final String bgImage2 =
			Resources.class.getResource(IMAGE_FOLDER + "/backgroundTest.jpg").toExternalForm();
	
	public static final String BACKGROUND_PLAYERADAPTER_IMAGE = 
			Resources.class.getResource(IMAGE_FOLDER + "/background_playeradapter.png").toExternalForm();
	
	public static final String PLAYER_ACTIVE_IMG =
			Resources.class.getResource(IMAGE_FOLDER + "/plyer_active_image.png").toExternalForm();
	
	public static final String PLAYER_INACTIVE_IMG =
			Resources.class.getResource(IMAGE_FOLDER + "/player_inactive_image.png").toExternalForm();
	
	public static final String SPECTATOR_BACKGROUND_IMAGE =
			Resources.class.getResource(IMAGE_FOLDER + "/spectator_background_image.png").toExternalForm();
	
	public static final String GAMEBOARD_BACKGROUND_IMAGE =
			Resources.class.getResource(IMAGE_FOLDER + "/gameboard_background_image.png").toExternalForm();
	
	public static final String BAG_IMAGE =
			Resources.class.getResource(IMAGE_FOLDER + "/bag.png").toExternalForm();
	
	public static final String ICON_PLAYER =
			Resources.class.getResource(IMAGE_FOLDER + "/icon_player.png").toExternalForm();
	
	public static final String ICON_POINTS =
			Resources.class.getResource(IMAGE_FOLDER + "/icon_points.png").toExternalForm();
	
//	public static final String LoadingScreen =
//			Resources.class.getResource(Image_Folder + "/LoadingScreen2.jpg").toExternalForm();
	
	public static final String el_font =
			Resources.class.getResource(Font_Folder + "/elianto.ttf").toExternalForm();

//	public static final String ttlBackground =
//			Resources.class.getResource(Image_Folder + "/titlebg.jpg").toExternalForm();

	public static final String AskMe =
			Resources.class.getResource(IMAGE_FOLDER + "/Question.jpg").toExternalForm();
	
	public static final String AskMeText = 
			Resources.class.getResource(IMAGE_FOLDER + "/ToolTip.jpg").toExternalForm();
	
	public static final String MUSIC_WIND_WALKER_THEME =
		      Resources.class.getResource(MUSIC_FOLDER + "wind_walker_theme.mp3").toExternalForm();
	
	public static final String MUSIC_RESONANT_CHAMBER_THEME =
		      Resources.class.getResource(MUSIC_FOLDER + "resonant_chamber.mp3").toExternalForm();
	
	public static final String MUSIC_THE_FISH =
		      Resources.class.getResource(MUSIC_FOLDER + "/the_fish.mp3").toExternalForm();
	
	//public static final String PLAYER_ICON = 
	//	      Resources.class.getResource(MUSIC_FOLDER + "/icon.png").toExternalForm();

	
	/**
	 * With help of this method we can build a path to a specific tile. 
	 * It is also possible to generate all of the card with this method
	 * 	   
	 * @param id The ID of the tile
	 * @return The path of the tile
	 */
	public static String getTileImage(Tile tile) {
		int id=tile.getColor()+tile.getShape()*12;
		return Resources.class.getResource(IMAGE_FOLDER + "/tile_id_" + Integer.toString(id) + ".png").toExternalForm();
	}

}
