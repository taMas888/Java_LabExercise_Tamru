package notepad;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage){
        TextFileTab editor = new TextFileTab(stage);
        Scene scene = new Scene(editor.createUI(), 900, 600);
        String css = getClass().getResource("/notepad/styles.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setTitle("Notepad");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
