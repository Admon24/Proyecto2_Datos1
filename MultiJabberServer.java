package p2;

import com.csvreader.CsvWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static p2.GFG.build;
import static p2.GFG.postorder;

/**
 * This class is to controll the CSV file and the run method
 * @author Andrés & Adrián
 */
class ServeOneJabber extends Thread {

    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    /**
     * Creates the input and the output of the socket
     * @param s Scoket
     * @throws IOException 
     */
    public ServeOneJabber(Socket s) throws IOException {
        socket = s;

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // Enable auto-flush:
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        
        start(); // Calls run()
    }

    /**
     * Gets the actual date
     * @return the date
     */
    public static String actualDate(){
        Date date=new Date();
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/YYYY");
        
        return formatDate.format(date);
    }
    /**
     * It works for write on the file and export the file
     * @param users List of users
     */
    public static void ExportCSV(List<User> users){
      String outputFile = "Users.csv"; //Name File
      //boolean exists = new File(outputFile).exists(); //verifies if exists

      //If exists a file called like this it erease it
      /*if(exists){
          File usersFile = new File(outputFile);
          usersFile.delete();
      }*/
      try {
          //creates the file
          CsvWriter outputCSV = new CsvWriter(new FileWriter(outputFile, true), ',');
          //Data to identify
          outputCSV.write("");
          outputCSV.write("");
          outputCSV.write("");
          //outputCSV.write("\n");

          outputCSV.endRecord();//stops typing

          //roam the list
          for (User user :users ){
              outputCSV.write(user.getOp());
              outputCSV.write(user.getResult());
              outputCSV.write(user.getDate());

              outputCSV.endRecord();// stop tying on file
          }

          outputCSV.close();//close file
          
      } catch (IOException e) {
      }
    }
    
    @Override
    /**
     * It reads the clients message and makes the server send an answer
     */
    public void run() {
       
        try {
            while (true) {
                List<User> users = new ArrayList<>();
                
                String msg_received = in.readLine(); //Leo todo lo que envíe el socket sc
                
                int string_len = msg_received.length();
                if(string_len > 1){
                    
                    System.out.println("Ciente envía:" + msg_received);
                    msg_received = "(" + msg_received;
                    msg_received += ")";
                    GFG.nptr root = build(msg_received);

                    // Function call
                    postorder(root);
                    System.out.println("\n");

                    out.println("Respuesta: " + GFG.result);
                    
                    String answer= GFG.result + ""; //convert result to string
                    users.add(new User(msg_received, answer, actualDate()));
                    ExportCSV(users);
                    
                }
                
            }

        } catch (IOException e) {
            System.out.println("Error");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }
}
/**
 * This is the main class, it starts the server and keep it on
 * @author Andrés & Adrián 
 */
public class MultiJabberServer {
    static final int PORT = 8080;
/**
 * Starts the server
 * @param args 
 * @throws IOException 
 */
    public static void main(String[] args) throws IOException {
        ServerSocket s = new ServerSocket(PORT);
        System.out.println("Server Started");
        try {
            while (true) {
                // Blocks until a connection occurs:
                Socket socket = s.accept();
                try {
                    new ServeOneJabber(socket);
                } 
                catch (IOException e) {
                    // If it fails, close the socket,
                    // otherwise the thread will close it:
                    socket.close();
                }
            }
        } finally {
            s.close();
        }
    }
}
