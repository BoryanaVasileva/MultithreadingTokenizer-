/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_part;

import com.Tokenizer;
import com.User;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Comparator;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

/**
 *
 * @author Bobi
 */
public class Server_c extends Application {
    // ------------ Server elements for GUI ------------
    private TextArea txaCommunication;// display information to user
    private ServerSocket server; // server socket
    private int counter;
    private Button btnSortByTokens;
    private Button btnSortByCreditCard;
    // --------------------------------
    XStream1 xs = new XStream1();

    // set up GUI
    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox root = new VBox();
        Paint back = Paint.valueOf("#c2c2d6");
        root.setBackground(new Background(new BackgroundFill(back, CornerRadii.EMPTY, Insets.EMPTY)));

        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(14));
        root.setSpacing(8);

        txaCommunication = new TextArea();

        txaCommunication.setMaxSize(1.7976931348623157E308, 1.7976931348623157E308);
        btnSortByTokens = new Button("Sort by tokens");
        btnSortByTokens.setStyle("-fx-base: #1a8cff");
        btnSortByTokens.setOnAction(ev -> {
            sortByTokens(ev);
        });
        btnSortByCreditCard = new Button("Sort by credit card");
        btnSortByCreditCard.setStyle("-fx-base: #77b300");
        btnSortByCreditCard.setOnAction(e -> {
            sortByCreditCard(e);

        });
        root.getChildren().addAll(txaCommunication, btnSortByTokens, btnSortByCreditCard);
        Scene scene = new Scene(root, 500, 400);
        // shutdown thread gracefully
        primaryStage.setOnCloseRequest(evt -> stop());
        primaryStage.setTitle("Server");
        primaryStage.setScene(scene);
        primaryStage.show();
        Thread thread = new Thread(() -> runServer());
        thread.start();
    }
    // method sort records in xml with tokens by token and save them in text file
    private void sortByTokens(ActionEvent ev) {
        if (ev.getSource() == btnSortByTokens) {
            List<Tokenizer> listTokens = xs.readTokens(); // read xml file with tokens
            Comparator<Tokenizer> tokenComparator = (t1, t2) -> { //create comaparato
                t1.getToken().compareTo(t2.getToken());
                return 1;

            };
            File txtTokens = new File("tokensList.txt"); //get text file with tokens
            //create sorted list with tokens and credit card
            List<Tokenizer> sortedList = (List<Tokenizer>) listTokens.stream()
                    .sorted(tokenComparator)
                    .map(t -> new Tokenizer(t.getCreditCardNum(), t.getToken()))
                    .collect(Collectors.toList());
            // check list is empry? and write a text file and show records in text area
            if (sortedList.isEmpty() == false) {
                displayMessage("Sorted by tokens \n");
                while (txtTokens.exists() == true) {
                    txtTokens.delete();
                }
                try {
                    Formatter format = new Formatter(txtTokens);

                    for (Tokenizer t : sortedList) {
                        format.format("%s - %s%n", t.getCreditCardNum(), t.getToken());
                        txaCommunication.appendText(t.getToken() + "- " + t.getCreditCardNum() + "\n");
                    }
                    format.close();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Server_c.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                txaCommunication.appendText("Some error \n");
            }

        }
    }
    // sorting xml token file by credit card num,ber and save sorted list in text file
    private void sortByCreditCard(ActionEvent e) {
        if (e.getSource() == btnSortByCreditCard) {
            File byCreditCardTxt = new File("sortedByCreditCards.txt"); //create text file
            List<Tokenizer> listTokens = xs.readTokens(); // read xml
            Comparator<Tokenizer> tokenComparator = (t1, t2) -> { //create comparator
                t1.getCreditCardNum().compareTo(t2.getCreditCardNum());
                return 1;

            };
            List<Tokenizer> sortedList = (List<Tokenizer>) listTokens.stream() //create sorted list
                    .sorted(tokenComparator)
                    .map(t -> new Tokenizer(t.getCreditCardNum(), t.getToken()))
                    .collect(Collectors.toList());
            //check list weather is empty and save the list in text file and show records in text area
            if (sortedList.isEmpty() == false) {
                try {
                    while (byCreditCardTxt.exists() == true) {
                        byCreditCardTxt.delete();
                    }
                    displayMessage("Sorted by credit card number \n");
                    Formatter format = new Formatter(byCreditCardTxt);
                    for (Tokenizer t : sortedList) {
                        format.format("%s - %s%n", t.getCreditCardNum(), t.getToken());
                        txaCommunication.appendText(t.getCreditCardNum() + "- " + t.getToken() + "\n");
                    }
                    format.close();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Server_c.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                txaCommunication.appendText("Some error \n");
            }
        }
    }

    public void runServer() {

        try // set up server to receive connections; process connections
        {
            server = new ServerSocket(12345, 100); // create ServerSocket
            ExecutorService service = Executors.newCachedThreadPool();
            //  String message = "";
            while (true) {
                try {
                    displayMessage("Waiting for connection\n");
                    Socket socket = server.accept(); // allow server to accept connection            
                    displayMessage("Connection " + counter + " received from: "
                            + socket.getInetAddress().getHostName()); // shows the user that a new client has connected to the server
                    ClientTask client = new ClientTask(socket, socket.getInetAddress().getHostName()); // create a new client task
                    service.submit(client); // submit that tast to the executor service

                    //client.run();
                } // end try
                catch (EOFException eofException) {
                    displayMessage("\nServer terminated connection");
                }
            } // end while 
        } // end try
        catch (IOException ioException) {
            ioException.printStackTrace();
        } // end catch
        finally {
            stop();
        }
    } // end method runServer

    private void displayMessage(final String messageToDisplay) {
        Platform.runLater(()
                -> // updates displayArea
                {
                    txaCommunication.appendText(messageToDisplay); // append message
                } // end method run

        ); // end call to Platfrom.runLater
    } // end method displayMessage

    @Override
    public void stop() {
        Platform.exit();
        System.exit(0);
    }

    private class ClientTask implements Runnable {

        private Socket connection; // connection to client
        private int counter = 1; // counter of number of connections 
        private User user;
        private String host;
        private ObjectOutputStream output; // output stream to client
        private ObjectInputStream input; // input stream from client
        // wait for connection to arrive, then display connection info
        private XStream1 xsreamUserVer = new XStream1();
        private List<User> listUsers = new LinkedList<>();
        private File fileUsers = new File("users.xml");
        private File tokensFile = new File("tokens.xml");
        private List<Tokenizer> tokens = new LinkedList<>();

        public ClientTask(Socket connection, String host) {
            this.connection = connection;
            this.host = host;
            try {
                getStreams();
                displayMessage("get streams..");
            } catch (IOException ex) {
                displayMessage("No connection");
            }
        }

        @Override
        public void run() {
            processConnection(); // proccess the contection with the client
            // closeConnection(); // closes the connection and the streams
        }
        // process connection with client

        private synchronized void processConnection() {
            String message = "Server work";// message to client

            sendData(message); // send connection successful message

            // enable enterField so server user can send messages
            setEditableText(true);
            do // process messages sent from client
            {
                try // read message and display it
                {
                    message = (String) input.readObject(); // read new message
                    displayMessage("\n" + message); // display message
                    processMessage(message);
                } // end try
                catch (ClassNotFoundException classNotFoundException) {
                    displayMessage("\nUnknown object type received");
                } catch (IOException ex) {
                    Logger.getLogger(Server_c.class.getName()).log(Level.SEVERE, null, ex);
                } // end catch // end catch // end catch // end catch // end catch // end catch // end catch // end catch

            } while (!message.startsWith("0"));
        } // end method processConnection

        private void getStreams() throws IOException {
            // set up output stream for objects
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush(); // flush output buffer to send header information

            // set up input stream for objects
            input = new ObjectInputStream(connection.getInputStream());

            displayMessage("\nGot I/O streams\n");
        } // end method getStreams

// close streams and socket
        private synchronized void closeConnection() {
            displayMessage("\nTerminating connection\n");
            setEditableText(false); // disable enterField

            try {
                if (output != null) {
                    output.close(); // close output stream
                }
                if (input != null) {
                    input.close(); // close input stream 
                }
                if (connection != null) {
                    connection.close(); // close socket
                }
            } // end try
            catch (IOException ioException) {
            } // end catch
        } // end method closeConnection

        // send message to client
        private synchronized void sendData(String message) {

            try // send object to client
            {

                output.writeObject(message);
                output.flush(); // flush output to client
                displayMessage(message);
            } // end try
            catch (IOException ioException) {
                txaCommunication.appendText("\nError writing object");
            } // end catch // end catch
        } // end method sendData

        // manipulates displayArea in the event-dispatch thread
        private synchronized void processMessage(String message) {
            String[] messageToArray = message.split(" ");
            displayMessage("\n" + message);

            int cnt = 0;
            if (message.startsWith("1")) {
                User user1 = new User(messageToArray[1], messageToArray[2]);
                proccessMessage1(user1);

            } else if (message.startsWith("2") == true) {
                User user = createNewUser(messageToArray);
                displayMessage(user.toString() + "\n");
                proccessMessage2(user);

            } else if (message.startsWith("4")) {
                String num = messageToArray[1];
                // proccessMessage4(num);
                proccessMessage4(num);

            } else if (message.startsWith("5")) {
                String num = messageToArray[1];
                proccessMessage5(num);

            } else if (message.startsWith("6")) {
                String num = messageToArray[1];
                proccessMessage6(num);
            }
        }
        // create new User 
        private User createNewUser(String[] messageToArray) {
            String name = messageToArray[1] + " " + messageToArray[2] + " " + messageToArray[3];
            String email = messageToArray[4];
            String pass = messageToArray[5];
            boolean rights = Boolean.parseBoolean(messageToArray[6]);
            User user = new User(name, email, pass, rights);
            displayMessage("\n Processing ... \n");

            // writeXML(user);
            return user;
        } // end method
        
        public void setEditableText(final boolean b) {
            Platform.runLater(() -> {
                txaCommunication.setEditable(b);
            });
        }
        // method proccess message start with 4 create new token and save it in xml
        public void proccessMessage4(String string) {
            Tokenizer tok = new Tokenizer();
            tok.setCreditCardNum(string);
            tok.setToken(string);

            if (tokensFile.exists() == false) {
                tokens.add(tok);
                xsreamUserVer.writeToXMLTokens(tokens);
                sendData("Tokenizer " + tok.getToken());
                displayMessage("Tokenizer " + tok.getToken());

            } else if (tokensFile.exists() == true) {
                tokens = xsreamUserVer.readTokens();

                Comparator<Tokenizer> compare = (u, v) -> {
                    u.getToken().compareTo(v.getToken());
                    return 0;
                };
                List<Tokenizer> l = tokens.stream()
                        .sorted(compare)
                        .filter(v -> v.getToken().equals(tok.getToken()))
                        .map(v -> new Tokenizer(v.getCreditCardNum(), v.getToken()))
                        .collect(Collectors.toList());
                while (l.isEmpty() == false) {
                    tok.setToken(string);
                }
                if (l.isEmpty() == true) {
                    tokens.add(tok);
                    xsreamUserVer.writeToXMLTokens(tokens);
                    sendData("Tokenizer " + tok.getToken());
                    displayMessage("Tokenizer " + tok.getToken());

                }
            }
        } 
        // end method
        // method proccessing registration message
        private void proccessMessage2(User user) {
            int cnt = 0;
            if (fileUsers.exists() == false) {
                listUsers = new LinkedList<>();
                listUsers.add(user);
                xsreamUserVer.writeToXML(listUsers);
                sendData("Successful registration \n");
                displayMessage("Successful registration \n");

            } else if (fileUsers.exists() == true) {
                listUsers = xsreamUserVer.read();
                for (User u : listUsers) {
                    if (!u.getEmail().equals(user.getEmail())) {
                        continue;
                    } else {
                        cnt++;
                    }
                }

                if (cnt != 0) {
                    displayMessage("This email already exist!");
                    sendData("The email is alredy exist!");
                } else {
                    listUsers.add(user);
                    xsreamUserVer.writeToXML(listUsers);
                    sendData("Successful registration \n");
                    displayMessage("Successful registration \n");
                }
            }

        }// end method registration
        
        // method proccessing login
        private void proccessMessage1(User user1) {
            int cnt = 0;
            if (fileUsers.exists() == true) {
                listUsers = xsreamUserVer.read();
                for (User u : listUsers) {
                    if (user1.getEmail().equals(u.getEmail())
                            && user1.getPass().equals(u.getPass())
                          ) {
                        cnt++;
                        user1.setName(u.getName());
                        user1.setRights(u.isRights());
                    } else {
                        continue;
                    }
                }
                if (cnt == 1 && user1.isRights() == true) {
                    sendData("Valid login information");
                    displayMessage("Valid login information");
                } else if (cnt == 1 && user1.isRights() == false) {
                    sendData("You don't have rights");
                    displayMessage("You don't have rights");
                } else if (cnt != 1) {
                    sendData("Email or password is wrong");
                    displayMessage("Email or password is wrong");
                }

            }
        } // end method 

        // method proccessing search token
        private void proccessMessage5(String num) {
            if (tokensFile.exists() == false) {
                sendData("File tokens.xml does not exist");
                displayMessage("File tokens.xml does not exist");

            } else if (tokensFile.exists() == true) {
                tokens = xsreamUserVer.readTokens();

                Comparator<Tokenizer> compare = (u, v) -> {
                    u.getCreditCardNum().compareTo(v.getCreditCardNum());
                    return 0;
                };
                List<Tokenizer> l = tokens.stream()
                        .sorted(compare)
                        .filter(v -> v.getCreditCardNum().equals(num))
                        //.map(v -> new Tokenizer(v.getCreditCardNum(), v.getToken()))
                        .collect(Collectors.toList());
                if (l.isEmpty() == true) {
                    sendData("This credit card does not exist");
                    displayMessage("This credit card does not exist");

                } else {
                    sendData(l.toString());
                    displayMessage(l.toString());

                }
            }

        }// end method
        
        //method proccessing search credit card number
        private void proccessMessage6(String num) {
            displayMessage("Proccess messege start with 6");
            if (tokensFile.exists() == false) {
                sendData("File tokens.xml does not exist");
                displayMessage("File tokens.xml does not exist");

            } else if (tokensFile.exists() == true) {
                tokens = xsreamUserVer.readTokens();
                Comparator<Tokenizer> compare = (u, v) -> {
                    u.getToken().compareTo(v.getToken());
                    return 1;
                };
                List<Tokenizer> l = tokens.stream()
                        .sorted(compare)
                        .filter(v -> v.getToken().equals(num))
                        .collect(Collectors.toList());
                if (l.isEmpty() == true) {
                    sendData("This token does not exist");
                    displayMessage("This token does not exist");

                } else if (l.isEmpty() == false) {
                    sendData(l.toString());
                    displayMessage(l.toString());

                }
            }

        }
    }
    //end method

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
