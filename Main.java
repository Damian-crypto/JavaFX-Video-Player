import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

import javafx.geometry.Insets;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.io.File;

public class Main extends Application {

	private BorderPane root;

	@Override
	public void start(final Stage primaryStage) {
		try {
			root = new BorderPane();
			
			ThisMediaPlayer player = new ThisMediaPlayer(new File("D:/Films/jet_engine_work.mp4"));

			root.setTop(new PlayerMenuBar(primaryStage, player));
			root.setCenter(player);
			root.setBottom(new BottomBar(player));
		} catch(Exception e) {
			System.err.println("Initialization error: " + e.getMessage());
		}

		primaryStage.setScene(new Scene(root, 720, 500));
		primaryStage.setTitle("Simple Video Player");
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
