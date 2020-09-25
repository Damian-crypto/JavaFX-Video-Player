import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.FileNotFoundException;

public class PlayerMenuBar extends MenuBar {

	File videoFile = null;

	public PlayerMenuBar(Stage stage, ThisMediaPlayer mediaPlayer) {
		Menu fileMenu = new Menu("File");
		MenuItem openItem = new MenuItem("Open", new ImageView(new Image(getClass().getResourceAsStream("icons/open.png"))));

		openItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				mediaPlayer.pause();
				FileChooser fc = new FileChooser();
				fc.setTitle("Open video file");
				fc.getExtensionFilters().addAll(
					new ExtensionFilter("Video File", "*.mp4"),
					new ExtensionFilter("All Files", "*.*")
				);
				videoFile = fc.showOpenDialog(stage);
				if(videoFile != null) {
					try {
						mediaPlayer.createNew(videoFile);
					} catch(FileNotFoundException e) {
						System.err.println("File selecting error: " + e.getMessage());
					}
				} else {
					mediaPlayer.play();
				}
			}
		});
		
		fileMenu.getItems().add(openItem);
		this.getMenus().add(fileMenu);
	}
}
