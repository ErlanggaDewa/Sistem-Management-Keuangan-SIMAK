package com.pbo.simak;

import com.pbo.simak.utils.SwitchScene;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class SimakApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        SwitchScene.switchTo("produk.fxml", stage);
//        FXMLLoader fxmlLoader = new FXMLLoader(SimakApplication.class.getResource("register.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        stage.setTitle("Sistem Management Keuangan");
//        stage.setScene(scene);
//        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}