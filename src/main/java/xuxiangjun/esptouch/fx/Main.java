package xuxiangjun.esptouch.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        ResourceBundle res = ResourceBundle.getBundle("strings");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main.fxml"), res);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle(res.getString("title"));
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        MainController control = fxmlLoader.getController();
        control.init();
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        System.out.println("EspTouch stop()");
    }


    public static void main(String[] args) {
        launch(args);
    }
}
