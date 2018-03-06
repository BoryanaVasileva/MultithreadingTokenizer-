/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.io.Serializable;

/**
 *
 * @author Bobi
 */
public class User implements Serializable{
   
    private String name;

    private String email;

    private String pass;

    private boolean rights;

    public User(String email, String pass) {
        this.email = email;
        this.pass = pass;
    }

    public User(String name, String email, String pass, boolean rights) {
        setName(name);
        setEmail(email);
        setPass(pass);
        setRights(rights);
    }

    public User() {
        setName("");
        setEmail("");
        setPass("");
        setRights(false);
    }

    /**
     * Get the value of rights
     *
     * @return the value of rights
     */
    public boolean isRights() {
        return rights;
    }

    /**
     * Set the value of rights
     *
     * @param rights new value of rights
     */
    public void setRights(boolean rights) {
        this.rights = rights;
    }

    /**
     * Get the value of pass
     *
     * @return the value of pass
     */
    public String getPass() {
        return pass;
    }

    /**
     * Set the value of pass
     *
     * @param pass new value of pass
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    /**
     * Get the value of email
     *
     * @return the value of email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the value of email
     *
     * @param email new value of email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of name
     *
     * @param name new value of name
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" + "name=" + name + ", email=" + email + ", pass=" + pass + ", rights=" + rights + '}';
    }
  
}
