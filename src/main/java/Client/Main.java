package Client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    //Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //FXMLLoader loader = new FXMLLoader();
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        //controller = loader.getController();
        primaryStage.setTitle("NetChat");
        Scene scene = new Scene(root, 350, 375);
        primaryStage.setScene(scene);
        primaryStage.show();

       //primaryStage.setOnCloseRequest(event -> {
       //    controller.Dispose();
       //    Platform.exit();
       //    System.exit(0);
       //});
    }

    public static void main(String[] args) {
        launch(args);
    }
}