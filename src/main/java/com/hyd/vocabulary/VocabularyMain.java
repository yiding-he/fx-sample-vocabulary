package com.hyd.vocabulary;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class VocabularyMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        URL fxml = VocabularyMain.class.getResource("/main.fxml");

        primaryStage.setTitle("四级词汇");
        primaryStage.setScene(new Scene(
                FXMLLoader.load(fxml), 600, 400
        ));
        primaryStage.show();
    }
}
