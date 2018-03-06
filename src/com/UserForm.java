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
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

/**
 *
 * @author Bobi
 */
public class UserForm extends BorderPane {

    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Label labels[];
    private TextField textFields[];
    private GridPane innerGrig;
    private VBox innerGridPaneCenter;
    private int count;
    private Label lblHello;

    public UserForm(String field1) {
        //create user form for adding new credit card or check token or credit card number
        innerGridPaneCenter = new VBox();
        innerGridPaneCenter.setAlignment(Pos.CENTER);
        Insets insets = new Insets(20);
        innerGridPaneCenter.setPadding(insets);
        innerGridPaneCenter.setSpacing(14);
        innerGridPaneCenter.setBackground(new Background(new BackgroundFill(Paint.valueOf("#c2c2d6"), CornerRadii.EMPTY, insets)));
        String names[] = {field1};
        if (names.length > 0) {
            count = names.length;

            labels = new Label[count];
            textFields = new TextField[count];
            for (int i = 0; i < count; i++) {
                labels[i] = new Label(names[i]);
                //  labels[i].setPrefWidth(300);
                textFields[i] = new TextField();
                textFields[i].setPromptText(names[i]);
                textFields[i].setPrefColumnCount(8);
                innerGridPaneCenter.getChildren().addAll(labels[i], textFields[i]);

            }
        }

        btn1 = new Button("Save record");
        btn1.setStyle("-fx-base: #00b3b3");
        btn2 = new Button("Search credit card");
        btn2.setStyle("-fx-base: #cc66ff");
        btn3 = new Button("Search token");
        btn3.setStyle("-fx-base: #cc66ff");
       
        innerGridPaneCenter.getChildren().addAll(btn1, btn2, btn3);

        this.setPadding(new Insets(14));
        this.setCenter(innerGrig);
        this.setCenter(innerGridPaneCenter);

    }

    public Button getButton1() {
        return btn1;
    }

    public Button getButton2() {
        return btn2;
    }

    public Button getButton3() {
        return btn3;
    }

    public TextField[] getFields() {
        return textFields;
    }

    public Label getHello() {
        return lblHello;
    }

}
