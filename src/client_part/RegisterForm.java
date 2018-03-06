/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_part;

import com.Validator;
import com.RegistrationForm;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;


/**
 *
 * @author Bobi
 */
public class RegisterForm extends Application {

    Alert al = new Alert(Alert.AlertType.INFORMATION);

    CheckBox chbRigths = new CheckBox("User have right");
    String names[] = {"First name", "Second name", "Last name", "Email"};
    RegistrationForm registration = new RegistrationForm(names, chbRigths);
    Scene sceneReg = new Scene(registration, 350, 600, Color.BLACK);
    Stage stageR = new Stage();
    Validator validator = new Validator();

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
// da opitam da premestq validiraneto v tozi klas

    @Override
    public void start(Stage primaryStage) {
        // set actions to buttons 
        registration.getButton1().setOnAction((event) -> {
            TextField arr[] = registration.getFields();

            if (validateRegisterForm(event) == true) {

                String fname = arr[0].getText();
                String sname = arr[1].getText();
                String lname = arr[2].getText();
                String email = arr[3].getText();
                String pass = registration.getPass().getText();
                boolean rights = chbRigths.isSelected();
                String msg =REGISTER + " " + fname + " " + sname + " " + lname + " " + email + " " + pass + " " + rights;
                //  System.out.println(msg);
                System.out.println(msg.startsWith("2"));
                sendData(msg);
            }
        });
        registration.getButton2().setOnAction(e -> {
            clean();
        });
        // start register form
        primaryStage.setTitle("Registration form!");
        primaryStage.setScene(sceneReg);
        primaryStage.setOnCloseRequest(e -> stop());
        primaryStage.show();
        Thread thread = new Thread(() -> runClient());
        thread.start();

    }
// method use validator to check user iinformation before send massege to server
    public boolean validateRegisterForm(ActionEvent e) {
        boolean endResult = false;
        Paint valueErr = Paint.valueOf("fc8d98");
        int count = 0;
        if (e.getSource() == registration.getButton1()) {
            boolean result = false;
            TextField arr[] = registration.getFields();

            String mail = arr[arr.length - 1].getText();
            for (int i = 0; i < arr.length - 1; i++) {
                result = validator.validateName(arr[i].getText());
                if (result == false) {
                    arr[i].setBackground(new Background(new BackgroundFill(valueErr, CornerRadii.EMPTY, Insets.EMPTY)));
                    count++;
                } else {
                    continue;
                }
            }

            if (validator.validateEmail(mail) == false) {
                arr[arr.length - 1].setBackground(new Background(new BackgroundFill(valueErr, CornerRadii.EMPTY, Insets.EMPTY)));
                System.out.println(arr[3].getText());
                count++;

            }
            if (validator.validatePass(registration.getPass().getText()) == false) {
                registration.getPass().setBackground(new Background(new BackgroundFill(valueErr, CornerRadii.EMPTY, Insets.EMPTY)));
                count++;
            }
            if (validator.confirmPass(registration.getPass().getText(), registration.getPassConf().getText()) == false) {
                registration.getPass().setBackground(new Background(new BackgroundFill(valueErr, CornerRadii.EMPTY, Insets.EMPTY)));
                registration.getPassConf().setBackground(new Background(new BackgroundFill(valueErr, CornerRadii.EMPTY, Insets.EMPTY)));

                count++;

            }
            if (count != 0) {
                endResult = false;
            } else {
                endResult = true;

            }
        }
        return endResult;
    } // end method validate

    // method cleans fields in form
    public void clean() {
        validator.cleanFields(registration.getFields(), registration.getPass(), registration.getPassConf());
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
                    TextField arr[];
                    PasswordField arr1[];
                    if (registration.getButton1().isPressed() == true) {
                        arr = registration.getFields();
                        for (int i = 0; i < arr.length; i++) {
                            arr[i].setEditable(editable);
                        }
                        arr1 = registration.getPassFields();
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
// when get answer and proccess it show alert message
    private void proccesAnswer(String message) {

        Platform.runLater(() -> {
            al.setContentText(message);
            al.showAndWait();
        });
    }

}
