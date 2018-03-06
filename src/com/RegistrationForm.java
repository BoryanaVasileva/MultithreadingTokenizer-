/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

public class RegistrationForm extends BorderPane {

    private Button btn1;
    private Button btn2;
    private Label labels[];
    private TextField textFields[];
    private PasswordField pswField;
    private Label lblPass;
    private Label lblPassContains;
    private PasswordField pswConf;
    private Label lblConfinrm;
    private GridPane innerGrig;
    private VBox innerGridPaneCenter;
    private int count;
    private CheckBox checkBox;
    private Validator validator;

    // create register form with text field from String array with 2 Password fields for enter and confirm password 
    // add checkbox button for checking user rights and two buttons for registration and clear fields
    public RegistrationForm(String names[], CheckBox chb1) {
        checkBox = chb1;
        innerGridPaneCenter = new VBox();
        innerGridPaneCenter.setAlignment(Pos.CENTER);
        Insets insets = new Insets(25);
        innerGridPaneCenter.setPadding(insets);
        innerGridPaneCenter.setSpacing(14);
        Paint back = Paint.valueOf("#c2c2d6");
        innerGridPaneCenter.setBackground(new Background(new BackgroundFill(back, CornerRadii.EMPTY, Insets.EMPTY)));

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
        lblPass = new Label("Password must contains min 8 characters");
        lblPassContains = new Label("At least 1 capital, 1 lower, 1 digit and 1 special symbol");
        pswField = new PasswordField();
        pswField.setPromptText("Password");
        lblConfinrm = new Label("Confirm password");
        pswConf = new PasswordField();
        pswConf.setPromptText("Confirm password");

        btn1 = new Button("REGISTRATION");
        btn1.setStyle("-fx-base: #cc3399");
        btn2 = new Button("CLEAN");
        btn2.setStyle("-fx-base: #1a8cff");

        innerGridPaneCenter.getChildren().addAll(lblPass, lblPassContains,
                pswField, lblConfinrm, pswConf, checkBox, btn1, btn2);

        this.setPadding(new Insets(14));
        this.setCenter(innerGrig);
        this.setCenter(innerGridPaneCenter);

    }

    // method get first button 
    public Button getButton1() {
        return btn1;
    }
    // method get second button 

    public Button getButton2() {
        return btn2;
    }
    // method get array from all text fields
    
    public TextField[] getFields() {
        return textFields;
    }
    // method get password field
    public PasswordField getPass() {
        return pswField;
    }
    // method get password confirm field
    public PasswordField getPassConf() {
        return pswConf;
    }
    // method get array from password fields in the form
    public PasswordField[] getPassFields() {
        PasswordField passFields[] = {pswField, pswConf};
        return passFields;
    }

}
