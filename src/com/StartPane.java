/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;

/**
 *
 * @author SS
 */
public class StartPane extends BorderPane {

    /* private Label labels[];
    private TextField fileds[];
    private PasswordField passwords[];
     */
    private Button btn1;
    private Button btn2;
    private GridPane innerGrig;
    private HBox innerGridPaneCenter;

    public StartPane() {
        //create window with two buttons
        btn1 = new Button("REGISTRATION");
        btn1.setStyle("-fx-base: #cc3399");
        btn2 = new Button("LOG IN");
        btn2.setStyle("-fx-base: #77b300");
        innerGridPaneCenter = new HBox();
        innerGridPaneCenter.setAlignment(Pos.CENTER);
        innerGridPaneCenter.setSpacing(14);
        innerGridPaneCenter.getChildren().addAll(btn1, btn2);
        Paint back = Paint.valueOf("#c2c2d6");
        BackgroundFill fill = new BackgroundFill(back, CornerRadii.EMPTY, Insets.EMPTY);
        innerGridPaneCenter.setBackground(new Background(fill));
        this.setPadding(new Insets(14));
        this.setCenter(innerGrig);
        this.setCenter(innerGridPaneCenter);
    }
   
    //method get first button
    public Button getButton1() {
        return btn1;
    }
    //method get second button
    public Button getButton2() {
        return btn2;
    }
}
