package notepad;

import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TextFileTab extends Tab {
    TextArea textArea = new TextArea();
    File file;

    public TextFileTab(String name){
        setText(name);
        setContent(textArea);
        textArea.textProperty().addListener((a, b, c ) -> {
            if(!getText().startsWith("*")){
                setText("*" + getText());
            }
        });
    }

    public TextFileTab(File file){
        this(file.getName());
        this.file = file;

        try{
            textArea.setText(Files.readString(file.toPath()));
            setText(file.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save(){
        try {
            if(file != null){
                Files.writeString(file.toPath(), textArea.getText());
                setText(file.getName());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
