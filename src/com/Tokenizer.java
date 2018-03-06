/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.util.Random;

/**
 *
 * @author SS
 */
public class Tokenizer {

    private String creditCardNum;

    private String token;
    private Random rand;//= new Random();

    public Tokenizer() {
        creditCardNum = null;
        token = null;
        rand = new Random();

    }
 // constructor create record from credit card number string and token string
    public Tokenizer(String creditCardNum, String token) {
        this.creditCardNum=creditCardNum;
        this.token= token;
        rand = new Random();
    }

    public void setCreditCardNum(String creditCardNum) {
        if (checkCreditCardNum(creditCardNum)) {
            this.creditCardNum = creditCardNum;
        }
    }

    public void setToken(String number) {
        String t = createToken(number);
        int sum = 0;
        for (int i = 0; i < t.length(); i++) {
            sum += Character.getNumericValue(t.charAt(i));
        }
        if (sum % 10 == 0) {
            this.token = t;
        } else {
            t = createToken(number);
            setToken(t);
        }
    }
    //method create token
    private String createToken(String number) {
        char[] arrFtomNum = number.toCharArray();
//        String last4 = number.substring(12, 16);
        StringBuilder result = new StringBuilder();
        int n;
        if (checkCreditCardNum(number) == true) {

            do {
                n = rand.nextInt(10);
            } while (n == 3 || n == 4 || n == 5 || n == 6);
            result.append(n);
            for (int i = 1; i < arrFtomNum.length - 4; i++) {
                do {
                    n = rand.nextInt(10);
                } while (n == arrFtomNum[i]);
                result.append(n);
            }
            result.append(number.substring(12, 16));
        } else {
            String r = null;
        }
        String r = new String(result);
        return result.toString();
    }

    public String getCreditCardNum() {
        return creditCardNum;
    }

    public String getToken() {
        return token;
    }

    private boolean checkCreditCardNum(String number) {
        boolean result = false;
        int arr[] = new int[16];
        int sum = 0;
        
        for (int i = number.length() - 2; i > 0; i = i - 2) {
           arr[i]=Character.getNumericValue(number.charAt(i));
           arr[i]*=2;
    
            if (arr[i] >= 10) {
                arr[i]-= 9;
                sum += arr[i];
            } else {
                sum += arr[i];
            }
        }
        for (int i = number.length() - 2; i > 0; i = i - 2) {
           arr[i]=Character.getNumericValue(number.charAt(i));
                sum += arr[i];
            
        }
        sum = sum * 9;
        if (sum % 10 == 0) {
            result = false;
        } else {
            result = true;
        }
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s-%s", creditCardNum, token);
    }

}
