import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.layout.BorderPane;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;

import java.net.MalformedURLException;

public class ThisMediaPlayer extends BorderPane {

	private Media media;
	private MediaPlayer mediaPlayer;
	private MediaView mediaView;
	private MediaPlayer.Status currentStatus;

	public ThisMediaPlayer(File file) {
		if(!file.equals("")) {
			createMediaPlayer(file);
		}

		widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> obs, Number oldN, Number newN) {
				frameBufferSizeCallback();
			}
		});

		setStyle("-fx-background-color: #696969");
	}

	public void createNew(File file) throws FileNotFoundException {
		if(!file.equals("")) {
			createMediaPlayer(file);
		} else {
			throw new FileNotFoundException("Video file missing.");
		}
	}

	public void clear() {
		currentStatus = MediaPlayer.Status.STOPPED;
		mediaPlayer.stop();
		mediaPlayer.dispose();
		media = null;
		mediaView = null;
	}

	public void pause() {
		if(currentStatus != MediaPlayer.Status.PAUSED) {
			currentStatus = MediaPlayer.Status.PAUSED;
			mediaPlayer.pause();
		} else {
			System.out.println("Already paused!");
		}
	}

	public void play() {
		if(currentStatus != MediaPlayer.Status.PLAYING) {
			currentStatus = MediaPlayer.Status.PLAYING;
			mediaPlayer.play();
		} else {
			System.out.println("Already playing!");
		}
	}

	public void stop() {
		if(currentStatus != MediaPlayer.Status.STOPPED) {
			currentStatus = MediaPlayer.Status.STOPPED;
			mediaPlayer.stop();
		} else {
			System.out.println("Already stopped!");
		}
	}

	public void setVolume(double value) {
		mediaPlayer.setVolume(value);
	}

	public boolean isPlaying() {
		if(currentStatus == MediaPlayer.Status.PLAYING) return true;
		else return false;
	}

	public ReadOnlyObjectProperty<Duration> getCurrentTimeProperty() {
		if(mediaPlayer != null) {
			return mediaPlayer.currentTimeProperty();
		}

		return null;
	}

	public double getCurrentPlayingHalf() {
		if(mediaPlayer != null) {
			return (mediaPlayer.getCurrentTime().toMillis() / mediaPlayer.getTotalDuration().toMillis());
		}

		return 0.0;
	}

	public double getTotalDuration() {
		if(mediaPlayer != null) {
			return mediaPlayer.getTotalDuration().toMillis();
		}

		return 0.0;
	}

	public void setCurrentPlayingHalf(double value) {
		if(mediaPlayer != null) {
			mediaPlayer.seek(new Duration(value));
		}
	}

	private void frameBufferSizeCallback() {
		if(mediaPlayer != null) {
			mediaView.fitWidthProperty().bind(widthProperty());
			mediaView.setSmooth(true);
			mediaView.setPreserveRatio(true);
		}
	}

	private void createMediaPlayer(File file) {
		if(mediaPlayer != null) clear();
		try {
			media = new Media(file.toURI().toURL().toExternalForm());
		} catch(MalformedURLException e) {
			System.err.println("File error: " + e.getMessage());
		}
		mediaPlayer = new MediaPlayer(media);
		mediaView = new MediaView(mediaPlayer);
		frameBufferSizeCallback();
		if(this.getChildren().size() > 0) this.getChildren().clear();
		if(this.getChildren().size() == 0) this.getChildren().add(mediaView);
		play();
	}
}
