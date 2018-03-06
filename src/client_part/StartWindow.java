/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_part;

import com.StartPane;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Bobi
 */
public class StartWindow extends Application {
    
    @Override
    public void start(Stage primaryStage) {
   // create object of type StartPane with two buttons and show it
        StartPane st = new StartPane();
       Scene scene = new Scene(st, 250, 250);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
        st.getButton1().setOnAction((event) -> {
            RegisterForm registration = new RegisterForm();
            primaryStage.close();
            Stage stage = new Stage();
            registration.start(stage);
        });
        st.getButton2().setOnAction((event) -> {
        LoginForm login = new LoginForm();
        Stage stage1 = new Stage();
        primaryStage.close();
        login.start(stage1);
       });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
