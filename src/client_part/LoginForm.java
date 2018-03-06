/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_part;

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
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Bobi
 */
public class LoginForm extends Application {

    Alert al = new Alert(Alert.AlertType.ERROR);
    //------Form elements-------
    Stage stageLog = new Stage();
    String loginFields[] = {"Email"};
    com.LoginForm login = new com.LoginForm(loginFields);
    Scene scLoginForm = new Scene(login, 300, 300);
    Validator validator = new Validator();
    TextField fields[] = login.getFields();

    //----------CLIENT ELEMENTS----------
    private final static int EXIT = 0; //type of message send to server when exit button is clicked
    private final static int LOGIN = 1; //type of message send to server when login button is clicked
    private final static int REGISTER = 2; //type of message send to server when register new token button is clicked
    private final static int GET_CARD = 3; //type of message send to server when get card  by token button is clicked
    private ObjectOutputStream output; // output stream to server
    private ObjectInputStream input; // input stream from server
    private String connectionToServer; // host server for this application
    private Socket clientSocket;
    Stage stage;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        // set actions on two buttons in the form
        login.getButton1().setOnAction(ev -> {
            if (validateForm(ev) == true) {
                String email = fields[0].getText();
                String pass = login.getPass().getText();
                String msd = LOGIN + " " + email + " " + pass;
                sendData(msd);
            } 
        });
        login.getButton2().setOnAction(e->{
            validator.cleanFields(fields, login.getPass());
           
        });
        primaryStage.setTitle("Login form");
        primaryStage.setScene(scLoginForm);
        primaryStage.show();
        Thread thread = new Thread(() -> runClient());
        thread.start();

    }
    
// method validateForm use validator to check fields in form
    public boolean validateForm(ActionEvent e) {
        boolean result = false;
        if (e.getSource() == login.getButton1()) {
            if (validator.validateEmail(fields[0].getText()) == true
                    && validator.validatePass(login.getPass().getText()) == true) {
                result = true;
            } else {
                result = false;
            }

        }
        return result;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

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
            displayMessage("\nCLIENT>>> " + message);
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
                    TextField arr[];
                    PasswordField arr1[];
                    if (login.getButton1().isPressed() == true) {
                        arr = login.getFields();
                        for (int i = 0; i < arr.length; i++) {
                            arr[i].setEditable(editable);
                        }
                        arr1 = login.getPassFields();
                        for (int i = 0; i < arr1.length; i++) {
                            arr[i].setEditable(editable);
                        }
                    }
                });// end call to Platfrom.runLater
    } // end method setTextFieldEditable

    @Override
    public void stop() {
        if(clientSocket!=null){
        closeConnection();
        }
        Platform.exit();
        System.exit(0);
    }
// method proccess answer from server and show alert
    private void proccesAnswer(String message) {
        if (message.equals("Email or password is wrong")
                || message.equals("You don't have rights")) {
            Platform.runLater(() -> {
                al.setContentText(message);
                al.showAndWait();
            });
        } else if (message.equals("Valid login information")) {
            Platform.runLater(() -> {
                stage.close();
                CreditCard logedForm = new CreditCard();
                logedForm.start(stage);
            });
        }

    }

}
