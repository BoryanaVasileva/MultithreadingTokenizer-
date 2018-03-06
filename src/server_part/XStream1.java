/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_part;

import com.Tokenizer;
import com.User;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Formatter;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class XStream1 {

    private FileInputStream input;
    private FileOutputStream output;
    private XStream xs;
    private static final String file = "users.xml";
    private static final String fileTok = "tokens.xml";

    public XStream1() {
        xs = new XStream();

    }

    public synchronized void writeToXML(List<User> u) {
        //   xs = new XStream();
        File f = new File(file);

        //Write to a file in the file system
        try {
            if (f.exists() == false && u.size() != 0) {
                output = new FileOutputStream(file);
                for (User user : u) {
                    xs.toXML(u, output);
                    u.remove(user);
                }
                output.close();
                f.setWritable(true);
                f.setReadable(true);
            } else if (f.canWrite() == true) {
                output = new FileOutputStream(file);
                //  for (int i = 0; i < u.size(); i++) {

                xs.toXML(u, output);
                u.clear();

                //  }
                output.close();
                f.setWritable(true);
                f.setReadable(true);

            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();

        } catch (IOException ex) {
            Logger.getLogger(XStream1.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public synchronized List<User> read() {

        List<User> users = new ArrayList<>();
        File f = new File(file);
        this.xs = new XStream(new DomDriver());
        if (f.exists() == true && f.canRead()) {

            try {
                input = new FileInputStream(file);
                User user = new User();
                //   xs.alias("linked-list", User.class);
                users = (List<User>) xs.fromXML(input);
                //   users.add(user);

            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }

        }

        return users;
    }

    public synchronized void writeToXMLTokens(List<Tokenizer> tokens) {
        //   xs = new XStream();
        File f = new File(fileTok);

        //Write to a file in the file system
        try {
            if (f.exists() == false && tokens.size() != 0) {
                output = new FileOutputStream(f);
                for (Tokenizer t : tokens) {
                    xs.toXML(tokens, output);
                    tokens.remove(t);
                }
                output.close();
                f.setWritable(true);
                f.setReadable(true);
            } else if (f.canWrite() == true) {
                output = new FileOutputStream(f);

                xs.toXML(tokens, output);
                tokens.clear();
                output.close();
                f.setWritable(true);
                f.setReadable(true);

            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();

        } catch (IOException ex) {
            Logger.getLogger(XStream1.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public synchronized List<Tokenizer> readTokens() {

        List<Tokenizer> tokens = new LinkedList<>();
        File f = new File(fileTok);
        this.xs = new XStream(new DomDriver());
        if (f.exists() == true && f.canRead()) {

            try {
                input = new FileInputStream(f);
                User user = new User();
                //   xs.alias("linked-list", User.class);
                tokens = (List<Tokenizer>) xs.fromXML(input);
                //   users.add(user);

            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }

        }

        return tokens;
    }
}
