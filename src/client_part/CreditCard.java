/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_part;

import com.UserForm;
import com.Validator;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Bobi
 */
public class CreditCard extends Application {

    UserForm newCardForm = new UserForm("Credit card number or token");
    Scene CardScene = new Scene(newCardForm, 300, 450);
    Stage stageCredit = new Stage();
    Validator validator = new Validator();
    Alert al = new Alert(Alert.AlertType.INFORMATION);
    //----------CLIENT ELEMENTS----------
    private final static int EXIT = 0; //type of message send to server when exit button is clicked
    private final static int LOGIN = 1; //type of message send to server when login button is clicked
    private final static int REGISTER = 2; //type of message send to server when register new token button is clicked
    private final static int GET_CARD = 3; //type of message send to server when get card  by token button is clicked
    private final static int SAVE_CARD = 4;
    private final static int SEARCH_CARD = 5;
    private final static int SEARCH_TOKEN = 6;
    private ObjectOutputStream output; // output stream to server
    private ObjectInputStream input; // input stream from server
    private String connectionToServer; // host server for this application
    private Socket clientSocket;

    public void start(Stage primaryStage) {
        // ----------- takes text fields in array------
        TextField arr[] = newCardForm.getFields();
        // ---------set actios on the tree buttons in the form ---------
        newCardForm.getButton1().setOnAction(e -> {

            boolean r = validator.validateCard(arr[0].getText());
            if (r == true) {
                String msg = SAVE_CARD + " " + arr[0].getText();
                sendData(msg);
            } else {
                al.setContentText("Invalid credit card");
                al.show();
            }
        });
        newCardForm.getButton2().setOnAction(ev -> {
            if (arr[0].getText() != null && arr[0].getText() != " ") {
                boolean r = validator.validateCard(arr[0].getText());
                if (r == true) {
                    String msg = SEARCH_CARD + " " + arr[0].getText();
                    sendData(msg);
                }else {
                al.setContentText("Invalid credit card");
                al.show();
            }
            }
        });
        newCardForm.getButton3().setOnAction(event -> {
            if (arr[0].getText() != null && arr[0].getText() != " ") {
                //   boolean r1 = validator.validateToken(arr[0].getText());
                if (arr[0].getText().startsWith("3") == false
                        && arr[0].getText().startsWith("4") == false
                        && arr[0].getText().startsWith("5") == false
                        && arr[0].getText().startsWith("6") == false) {
                    String msg = SEARCH_TOKEN + " " + arr[0].getText();
                    System.out.println(msg);
                    sendData(msg);
                } else {
                    System.out.println("Invalid token");
                }
            }
        });
        primaryStage.setTitle("Hello user!");
        primaryStage.setScene(CardScene);
        primaryStage.show();
        Thread thread = new Thread(() -> runClient());
        thread.start();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    // ---------- run client ------------
    public void runClient() {
        try // connect to server, get streams, process connection
        {
            connectToServer(); // create a Socket to make connection
            getStreams(); // get the input and output streams
            processConnection(); // process connection
        } // end try
        catch (EOFException eofException) {
            displayMessage("\nClient terminated connection");
        } // end catch
        catch (IOException ioException) {
            System.out.println("Client IOexception    " + ioException.getMessage());
        } // end catch
        finally {
            closeConnection(); // close connection
        } // end finally
    } // end method runClient

    // connect to server
    private void connectToServer() throws IOException {
        displayMessage("Attempting connection\n");

        // create Socket to make connection to server
        clientSocket = new Socket(InetAddress.getByName(connectionToServer), 12345);
        if (clientSocket == null) {
//            Platform.exit();
//            System.exit(0);
            stop();
        }
        // display connection information
        displayMessage("Connected to: "
                + clientSocket.getInetAddress().getHostName());
    } // end method connectToServer

    // get streams to send and receive data
    private void getStreams() throws IOException {
        // set up output stream for objects
        output = new ObjectOutputStream(clientSocket.getOutputStream());
        output.flush(); // flush output buffer to send header information

        // set up input stream for objects
        input = new ObjectInputStream(clientSocket.getInputStream());

        displayMessage("\nGot I/O streams\n");
    } // end method getStreams

    // process connection with client
    private void processConnection() throws IOException {
        String message = "Connection successful";// message to server
        sendData(message); // send connection successful message

        // enable enterField so server user can send messages
        setTextFieldEditable(true);

        do // process messages sent from client
        {
            try // read message and display it
            {
                message = (String) input.readObject(); // read new message
                displayMessage(message); // display message
                proccesAnswer(message);
            } // end try
            catch (ClassNotFoundException classNotFoundException) {
                displayMessage("\nUnknown object type received");
            } // end catch
            catch (SocketException s) {
                break;
            }
        } while (!message.startsWith("0"));
    } // end method processConnection

// close streams and socket
    private void closeConnection() {
        displayMessage("\nTerminating connection\n");
        setTextFieldEditable(false); // disable enterField

        try {
            if (output != null) {
                output.close(); // close output stream
            }
            if (input != null) {
                input.close(); // close input stream
            }
            if (clientSocket != null) {
                clientSocket.close(); // close socket
            }
        } // end try
        catch (IOException ioException) {
            ioException.printStackTrace();
        } // end catch
    } // end method closeConnection

    // send message to client
    private void sendData(String message) {
        try // send object to client
        {
            output.writeObject(message);
            output.flush(); // flush output to client
            displayMessage(message);
        } // end try
        catch (IOException ioException) {
            displayMessage("\nError writing object");
        } // end catch
    } // end method sendData

    // manipulates displayArea in the event-dispatch thread
    private void displayMessage(final String messageToDisplay) {
        System.out.println("CLIENT>> " + messageToDisplay);
    } // end method displayMessage

    // manipulates enterField in the event-dispatch thread
    private void setTextFieldEditable(final boolean editable) {
        Platform.runLater(()
                -> // sets enterField's editability
                {
                    TextField arr[] = arr = newCardForm.getFields();

                    if (newCardForm.getButton1().isPressed() == true) {
                        arr[0].setEditable(editable);
                    }
                });// end call to Platfrom.runLater
    } // end method setTextFieldEditable

    // close window
    @Override
    public void stop() {
        if (clientSocket != null) {
            closeConnection();
        }
        Platform.exit();
        System.exit(0);
    }
    // show alert message after readin answer message
    private void proccesAnswer(String message) {
        if (message.startsWith("[")) { // if server send list with tokens and card create a new alert for evry record
            String[] messageToArray = message.split(",");

            Platform.runLater(() -> {
                for (int i = 0; i < messageToArray.length; i++) {

                    al.setContentText(messageToArray[i]);
                    al.showAndWait();
                }
            });
        } else {

            Platform.runLater(() -> {
                al.setContentText(message);
                al.showAndWait();

            });
        }

    } // end method process answer
}
