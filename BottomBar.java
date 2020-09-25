import javafx.application.Platform;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Group;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.geometry.Insets;

public class BottomBar extends VBox {

	private ThisMediaPlayer mediaPlayer = null;
	private Button playBtn;
	private Slider timeSlider;
	private Slider volSlider;
	private Label volLabel;
	private ImageView playImg = new ImageView(new Image(getClass().getResourceAsStream("icons/play.png")));
	private ImageView pauseImg = new ImageView(new Image(getClass().getResourceAsStream("icons/pause.png")));
	private ImageView muteImg = new ImageView(new Image(getClass().getResourceAsStream("icons/mute.png")));
	private ImageView volImg = new ImageView(new Image(getClass().getResourceAsStream("icons/vol.png")));

	public void updatePlayBtn() {
		if(mediaPlayer.isPlaying()) {
			System.out.println("paused");
			playBtn.setGraphic(playImg);
		} else {
			System.out.println("playing");
			playBtn.setGraphic(pauseImg);
		}
	}

	public BottomBar(ThisMediaPlayer mediaPlayer) {
		this.mediaPlayer = mediaPlayer;

		// Initialize play btn
		playBtn = new Button();
		playBtn.setGraphic(mediaPlayer.isPlaying() ? pauseImg : playImg);
		playBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				updatePlayBtn();
				if(mediaPlayer.isPlaying()) mediaPlayer.pause();
				else {
					mediaPlayer.play();
					addListenersToTimeSlider();
				}
			}
		});

		Button stopBtn = new Button();
		stopBtn.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("icons/stop.png"))));
		stopBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				if(mediaPlayer.isPlaying()) {
					updatePlayBtn();
					mediaPlayer.stop();
				}
			}
		});

		Button repeatBtn = new Button();
		repeatBtn.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("icons/repeat.png"))));
		repeatBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				mediaPlayer.setCurrentPlayingHalf(0.0);
			}
		});

		timeSlider = new Slider();
		addListenersToTimeSlider();

		volSlider = new Slider(0, 100, 100);
		volSlider.setPadding(new Insets(10, 0, 0, 25));
		volLabel = new Label();
		volLabel.setPadding(new Insets(5, 10, 0, 0));
		volLabel.setGraphic(volImg);
		Group volGroup = new Group(volLabel, volSlider);
		volGroup.setAutoSizeChildren(true);
		addListenersToVolSlider();

		HBox horizontalPanel = new HBox(10);
		Pane spacer = new Pane();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		horizontalPanel.setPadding(new Insets(10, 10, 10, 10));
		horizontalPanel.getChildren().addAll(playBtn, stopBtn, repeatBtn, spacer, volGroup);
		getChildren().addAll(timeSlider, horizontalPanel);
	}

	private void addListenersToTimeSlider() {
		timeSlider.valueProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable obs) {
				if(timeSlider.isPressed()) {
					double cTime = (mediaPlayer.getTotalDuration() * timeSlider.getValue()) / 100;
					mediaPlayer.setCurrentPlayingHalf(cTime);
				}
			}
		});

		mediaPlayer.getCurrentTimeProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable obs) {
				updateValues();
			}
		});
	}

	private void addListenersToVolSlider() {
		volSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> obv, Number oldVal, Number newVal) {
				if(newVal.intValue() > 0)
					volLabel.setGraphic(volImg);
				else
					volLabel.setGraphic(muteImg);
				mediaPlayer.setVolume(newVal.doubleValue() / 100);
			}
		});
	}

	private void updateValues() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				timeSlider.setValue(mediaPlayer.getCurrentPlayingHalf() * 100);
			}
		});
	}
}
