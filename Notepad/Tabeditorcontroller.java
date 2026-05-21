package notepad;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class TabEditorController {

    private final TabPane tabPane = new TabPane();
    private final Stage stage;
    int count = 1;

    public TabEditorController(Stage stage) {
        this.stage = stage;
    }

    public BorderPane createUI() {

        BorderPane root = new BorderPane();
        root.setTop(menuBar());
        root.setCenter(tabPane);

        newTab();

        return root;
    }

    MenuBar menuBar() {

        Menu file = new Menu("File");

        MenuItem newFile = new MenuItem("New");
        newFile.setOnAction(e -> newTab());

        MenuItem open = new MenuItem("Open");
        open.setOnAction(e -> openFile());

        MenuItem save = new MenuItem("Save");
        save.setOnAction(e -> saveFile());

        MenuItem rename = new MenuItem("Rename");
        rename.setOnAction(e -> renameTab());

        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(e -> Platform.exit());

        file.getItems().addAll(newFile, open, save, rename, exit);

        Menu edit = new Menu("Edit");

        MenuItem cut = new MenuItem("Cut");
        cut.setOnAction(e -> current().textArea.cut());

        MenuItem copy = new MenuItem("Copy");
        copy.setOnAction(e -> current().textArea.copy());

        MenuItem paste = new MenuItem("Paste");
        paste.setOnAction(e -> current().textArea.paste());

        edit.getItems().addAll(cut, copy, paste);

        return new MenuBar(file, edit);
    }

    void newTab() {
        tabPane.getTabs().add(new TextFileTab("Untitled " + count++));
    }

    void openFile() {
        File file = new FileChooser().showOpenDialog(stage);

        if (file != null) {
            tabPane.getTabs().add(new TextFileTab(file));
        }
    }

    void saveFile() {
        TextFileTab tab = current();

        if (tab.file == null) {
            File file = new FileChooser().showSaveDialog(stage);

            if (file != null) {
                tab.file = file;
            }
        }

        tab.save();
    }

    void renameTab() {

        TextInputDialog dialog = new TextInputDialog(current().getText());
        dialog.setHeaderText("Rename File");

        dialog.showAndWait().ifPresent(name -> {
            current().setText(name);

            if (current().file != null) {
                File old = current().file;
                File renamed = new File(old.getParent(), name);

                old.renameTo(renamed);
                current().file = renamed;
            }
        });
    }

    TextFileTab current() {
        return (TextFileTab) tabPane.getSelectionModel().getSelectedItem();
    }
}
