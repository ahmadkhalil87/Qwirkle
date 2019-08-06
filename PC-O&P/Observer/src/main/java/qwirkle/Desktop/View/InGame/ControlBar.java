package qwirkle.Desktop.View.InGame;

//
import javafx.animation.Animation.Status;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import qwirkle.Desktop.Communication.Messages.gameMessages.BagRequest;
import qwirkle.Desktop.Communication.Messages.gameMessages.LeavingRequest;
import qwirkle.Desktop.Controller.GameController;
import qwirkle.Desktop.Controller.MainMenuController;
import qwirkle.Desktop.Main.Mainstarter;
import qwirkle.Desktop.Resources.Resources;
import qwirkle.Desktop.enumeration.SoundEffects;

/**
 * This class models the control bar in the bottom in the game view.
 *
 * @author Houman Mahtabi
 */

public class ControlBar extends StackPane {

	private int tilesLeft; // The tiles left in the game, will be calculated from deck
	private long timeLeft; // Time left for turn (in SECONDS here!)
	public Timeline timeline; // The Timer

	private Label lblTilesLeft;
	private StringProperty tilesInBag = new SimpleStringProperty();

	public Label lblTimeLeft;

	private StackPane bagContainer;
	private BagView bgView;
	private static boolean bagVisible = false;

	/**
	 * The constructor for the class.
	 *
	 * @param tmpControl The actual GameViewController instance
	 * @param tmpView    The actual GameView instance
	 * @param tmpModel   The actual Model instance
	 */
	public ControlBar() {

		getStylesheets().add(Resources.Css_ContolBar);

		// Label for time left to play current Turn
		this.lblTimeLeft = new Label();
		if (this.timeLeft > 0) {
			this.lblTimeLeft.setText(Long.toString(timeLeft));
		} else {
			this.lblTimeLeft.setText("Time's Up");
		}

		HBox timeContainer = new HBox();
		timeContainer.setAlignment(Pos.CENTER);
		timeContainer.getChildren().add(lblTimeLeft);

		// Settings Button
		Image img5;
		img5 = new Image(Resources.btnSettingsImage);
		ImageView imgButtonSettings = new ImageView(img5);
		imgButtonSettings.setFitWidth(70);
		imgButtonSettings.setFitHeight(70);
		imgButtonSettings.setId("imgButtonSettings");
		imgButtonSettings.addEventHandler( // Show settings window
				MouseEvent.MOUSE_CLICKED, event -> {
					SoundEffects.BUTTON_CLICK.play(Mainstarter.model.getSoundVol());
					// if (view.getTilesInHandSlideIsVisible()==true) {view.slideAllPlayerTiles();}
					GameController.callSettings();
					// view.slideSettigs();
				});

		// Set Button "Exit" Image
		Image img8;
		img8 = new Image(Resources.btnExitImage);
		ImageView imgButtonExit = new ImageView(img8);
		imgButtonExit.setFitWidth(70);
		imgButtonExit.setFitHeight(70);
		imgButtonExit.setId("imgButtonExit");
		imgButtonExit.addEventHandler( // Exit game
				MouseEvent.MOUSE_CLICKED, event -> {
					SoundEffects.BUTTON_CLICK.play(Mainstarter.model.getSoundVol());
					Mainstarter.meClient.setIngame(false);
					Mainstarter.meClient.sendMessage(new LeavingRequest());
					MainMenuController.switchMainMenu();
					Mainstarter.settingsView.rootParent = Mainstarter.mainMenuView;
				});

		// Set Bag Image and stack Bag und TilesLeft together
		this.lblTilesLeft = new Label();
		this.lblTilesLeft.setText(Integer.toString(tilesLeft));
		this.lblTilesLeft.textProperty().bind(tilesInBag);
		this.bgView = new BagView();

		lblTilesLeft.setOnMouseClicked(event -> {
			if (!Mainstarter.gameView.isAnimShowBag()) {
				SoundEffects.BUTTON_CLICK.play(Mainstarter.model.getSoundVol());
				if (!bagVisible) {
					Mainstarter.meClient.sendMessage(new BagRequest());
				} else {
					showBag();
				}
			} else {
				event.consume();
			}
		});

		Image img9;
		img9 = new Image(Resources.BAG_IMAGE);
		ImageView imgBag = new ImageView(img9);
		imgBag.setFitWidth(80);
		imgBag.setFitHeight(70);
		bagContainer = new StackPane();
		bagContainer.getChildren().addAll(imgBag, this.lblTilesLeft);



		HBox rightBtnContainer = new HBox(10);
		rightBtnContainer.getChildren().addAll(imgButtonSettings, imgButtonExit);
		rightBtnContainer.setAlignment(Pos.CENTER_RIGHT);
		HBox.setHgrow(rightBtnContainer, Priority.ALWAYS);

		HBox mainBtnContainer = new HBox(10);
		mainBtnContainer.getChildren().addAll(bagContainer, timeContainer, rightBtnContainer);
		mainBtnContainer.setAlignment(Pos.CENTER);
		mainBtnContainer.setPadding(new Insets(15, 10, 0, 10));

		rightBtnContainer.setPickOnBounds(false);
		mainBtnContainer.setPickOnBounds(false);
		this.setPickOnBounds(false);

		this.setMaxHeight(100);
		this.setStyle("-fx-background-color: transparent;");
		this.setAlignment(Pos.BOTTOM_CENTER);
		this.getChildren().add(mainBtnContainer);
	}

	// --- END CONSTRUCTOR ---

	/**
	 * This Method will start the countdown when players turn starts
	 *
	 * @param id   The ID of the player who makes the turn
	 * @param type The type of the player who makes the turn
	 * @return startTimer() if time is limited, true if not so
	 */
	public boolean startTurn() {
		// this.tilesLeft = control.getTilesLeft();
		this.timeLeft = (GameController.joinedGame.getConfig().getTurnTime() / 1000); // Server uses Seconds
		if (this.timeLeft > 0) {
			this.lblTimeLeft.setText(timeString(this.timeLeft));
			this.lblTimeLeft.setStyle("-fx-text-fill: white;");
			stopTimer();
			return startTimer();
		} else {
			this.lblTimeLeft.setText("Time left: infinite");
			this.lblTimeLeft.setStyle("-fx-text-fill: white;");
			return true;
		}
	}

	/**
	 * This Method updates the view in the controlBar when a player has finished a
	 * turn. It will also stop the countdown.
	 *
	 * @return stopTimer() if time is limited, true if not so
	 */
	public boolean stopTurn() {
		// this.tilesLeft = control.getTilesLeft();
		this.timeLeft = (GameController.joinedGame.getConfig().getTurnTime() / 1000); // Server uses Milliseconds
		if (this.timeLeft > 0) {
			this.lblTimeLeft.setText(timeString(this.timeLeft));
			this.lblTimeLeft.setStyle("-fx-text-fill: white; ");
			return stopTimer();
		} else {
			this.lblTimeLeft.setText("timeup");
			this.lblTimeLeft.setStyle("-fx-text-fill: white; ");
			return true;
		}
	}

	/**
	 * This method will start the timer.
	 *
	 * @return true if game is not paused, false if game is paused
	 */
	public boolean startTimer() {
		this.timeline = new Timeline();
		if (!timeline.getStatus().equals(Status.PAUSED)) {
			timeline.setCycleCount(Timeline.INDEFINITE);
			timeline.getKeyFrames().add(
					new KeyFrame(
							Duration.seconds(1),
							new EventHandler<ActionEvent>(){
								public void handle(ActionEvent event) {
									timeLeft--;
									if (timeLeft >5){
										lblTimeLeft.setStyle("-fx-text-fill: white;, -fx-font-size: 15pt;");
										lblTimeLeft.setText(timeString(timeLeft));
			
									}
									else if ((timeLeft <= 5) && (timeLeft >= 0)) {
										lblTimeLeft.setStyle("-fx-text-fill: red; -fx-font-size: 18pt;");
										lblTimeLeft.setText(timeString(timeLeft));
										SoundEffects.CLOCK_TICK.play(Mainstarter.model.getSoundVol());
									}
									else if (timeLeft <= -1) {
										lblTimeLeft.setStyle("-fx-text-fill: white;, -fx-font-size: 20pt;");
										lblTimeLeft.setText("Time's up");
										SoundEffects.BIKE_HORN.play(Mainstarter.model.getSoundVol());
										timeline.stop();
										
									} 
								}
							}));
			timeline.playFromStart();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method will pause the timer.
	 *
	 * @return true if game is not paused, false if game is paused or timer is not
	 *         active.
	 */
	public boolean pauseTimer() {
		if (timeline != null) {
			if (!timeline.getStatus().equals(Status.PAUSED)) {
				timeline.pause();
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * This method will resume the timer.
	 *
	 * @return true if game is paused, false if game is not paused or timer is not
	 *         active.
	 */
	public boolean resumeTimer() {
		if (timeline != null) {
			if (timeline.getStatus().equals(Status.PAUSED)) {
				timeline.play();
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * This method will stop the timer.
	 *
	 * @return true if timer was active, false otherwise
	 */
	public boolean stopTimer() {
		if (timeline != null) {
			timeline.stop();
			this.timeline = null;
			this.timeLeft = (GameController.joinedGame.getConfig().getTurnTime() / 1000); // Server uses Milliseconds
			return true;
		} else {
			return false;
		}
	}

	public boolean pauseGame() {
		return pauseTimer();
	}

	public boolean resumeGame() {
		return resumeTimer();
	}

	public int getTilesLeft() {
		return this.tilesLeft;
	}

	public long getTimeLeft() {
		return this.timeLeft;
	}

	/**
	 * This method updates the 'Time left' display.
	 *
	 * @param seconds The number of seconds allowed for a turn
	 */
	public void setTimeLeft(long seconds) {
		if (seconds > 0) {
			this.timeLeft = seconds;
			this.lblTimeLeft.setText(timeString(this.timeLeft));
		} else if (seconds == 0) {
			if (GameController.joinedGame != null) {
			this.timeLeft = GameController.joinedGame.getConfig().getTurnTime()/1000;
			}
			// this.lblTimeLeft.setText("timeup");
		} else {
			this.timeLeft = 0;
			 this.lblTimeLeft.setText("timeup");
		}
	}

	/**
	 * This method generates a nice time string from the number of seconds given.
	 *
	 * @param seconds The number of seconds
	 */
	public String timeString(long seconds) {
		long hours = seconds / 3600;
		long minutes = (seconds % 360) / 60;
		seconds %= 60;

		if (hours > 9) {
			return "Time: " + String.format("%02d:%02d:%02d", hours, minutes, seconds);
		} else if (hours > 0) {
			return "Time: " + String.format("%01d:%02d:%02d", hours, minutes, seconds);
		} else {
			// return "timeleft" + String.format("%02d:%02d", minutes, seconds); // this
			// line shows timeleft all the time
			return String.format("%02d:%02d", minutes, seconds);
		}
	}

	public void setBagValue(int tilesCount) {
		setTilesInBag(Integer.toString(tilesCount));
	}

	public StringProperty getTilesInBag() {
		return tilesInBag;
	}

	public void setTilesInBag(String tilesInBag) {
		this.tilesInBag.set(tilesInBag);
	}

	public BagView getBgView() {
		return bgView;
	}

	public void setBgView(BagView bgView) {
		this.bgView = bgView;
	}

	public void showBag() {
		Mainstarter.gameView.setAnimShowBag(true);
		if (!bagVisible) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Mainstarter.gameView.getTopLayer().getChildren().add(bgView);
					AnchorPane.setBottomAnchor(bgView, 50.0);
					AnchorPane.setLeftAnchor(bgView, 3.0);
				}
			});

			Timer animTimer = new Timer();
			animTimer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					if (getBgView().getHeight() < 450) {

						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								getBgView().setPrefHeight(getBgView().getHeight() + 10);
							}
						});
						if (getBgView().getWidth() < 200) {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									getBgView().setPrefWidth(getBgView().getWidth() + 25);
								}
							});
						}
					} else if (getBgView().getHeight() >= 450 && getBgView().getWidth() >= 200) {
						Mainstarter.gameView.setAnimShowBag(false);
						this.cancel();
					}
				}
			}, 500, 10);
			bagVisible = true;
		} else {
			Timer animTimer = new Timer();
			animTimer.scheduleAtFixedRate(new TimerTask() {
				int i = 0;

				@Override
				public void run() {
					if (i < 150 && getBgView().getHeight() > 0) {

						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								getBgView().setPrefHeight(getBgView().getHeight() - 10);
							}
						});

						if (i > 140) {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									getBgView().setPrefWidth(getBgView().getWidth() - 55);
								}
							});
						}
					} else {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								Mainstarter.gameView.getTopLayer().getChildren().remove(bgView);
								Mainstarter.gameView.setAnimShowBag(false);
							}
						});

						this.cancel();
					}
					i++;
				}
			}, 500, 10);
			bagVisible = false;
		}
	}
}
