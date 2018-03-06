/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.geometry.Insets;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 *
 * @author SS
 */
// class Validator creat methods for validating user information 
public class Validator {

    Paint valueErr = Paint.valueOf("fc8d98");
    Paint valueClean = Paint.valueOf("fefefe");

    // валидира полетата за имена
    public boolean validateName(String name) {
        boolean result = false;
        Matcher matcher;
        String regex = "^[A-Z][a-z]+$";
        if (name != null) {
            Pattern pattern = Pattern.compile(regex);
            matcher = pattern.matcher(name);
            result = matcher.matches();

        }
        return result;
    }
    // валидира полето за имейл

    public boolean validateEmail(String email) {
        boolean result = false;
        Matcher matcher;
        String regex = "^[a-zA-Z0-9._-]+@[a-z]+.{2,20}$";
        if (email != null) {
            Pattern pattern = Pattern.compile(regex);
            matcher = pattern.matcher(email);
            result = matcher.matches();
        }

        return result;
    }

    //валидира полето за парола
    public boolean validatePass(String pass) {
        boolean result = false;
        Matcher matcher;
        String regex = "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$";
        if (pass != null) {
            Pattern pattern = Pattern.compile(regex);
            matcher = pattern.matcher(pass);
            result = matcher.matches();
        } else {
            return result;
        }
        return result;

    }

    // проверява дали полето за парола и това за потвърждение съвпадат
    public boolean confirmPass(String pass, String confirm) {
        boolean result = false;
        char[] passArr = pass.toCharArray();
        char[] confirmArr = confirm.toCharArray();
        if (confirmArr.length == passArr.length) {
            for (int i = 0; i < passArr.length; i++) {
                if (passArr[i] == confirmArr[i]) {
                    result = true;
                } else {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    // изчиства полетата в регистрационната форма
    public void cleanFields(TextField fields[], PasswordField psw, PasswordField confirmPsw) {
        if (fields.length > 0) {
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].getText() != null || fields[i].getText() != " ") {
                    fields[i].setText("");
                    fields[i].setBackground(new Background(new BackgroundFill(valueClean, CornerRadii.EMPTY, Insets.EMPTY)));

                } else {
                    continue;
                }
            }
        }
        if (psw.getText() != null || psw.getText() != " ") {
            psw.setText("");
            psw.setBackground(new Background(new BackgroundFill(valueClean, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        if (confirmPsw.getText() != null || confirmPsw.getText() != " ") {
            confirmPsw.setText("");
            confirmPsw.setBackground(new Background(new BackgroundFill(valueClean, CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }
    // изчиства полетата в логин формата 
    public void cleanFields(TextField fields[], PasswordField psw) {
        if (fields.length > 0) {
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].getText() != null || fields[i].getText() != " ") {
                    fields[i].setText("");
                    fields[i].setBackground(new Background(new BackgroundFill(valueClean, CornerRadii.EMPTY, Insets.EMPTY)));

                } else {
                    continue;
                }
            }
        }
        if (psw.getText() != null || psw.getText() != " ") {
            psw.setText("");
            psw.setBackground(new Background(new BackgroundFill(valueClean, CornerRadii.EMPTY, Insets.EMPTY)));
        }

    }
    //валидира кредитната карта
    public boolean validateCard(String number) {
        boolean result = false;
        Matcher matcher;
        String regex = "^[3-6]\\d{15}$";
        if (number != null) {
            Pattern pattern = Pattern.compile(regex);
            matcher = pattern.matcher(number);
            result = matcher.matches();

        } else {
            result = false;
        }
        return result;
    }
    // валидира токен
    public boolean validateToken(String number) {
        boolean result = false;
        Matcher matcher;
        String regex = "[^3456]\\d{16}$";
        if (number != null) {
            Pattern pattern = Pattern.compile(regex);
            matcher = pattern.matcher(number);
            result = matcher.matches();

        } else {
            result = false;
        }
        return result;
    }
}
