package p2;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
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


class ServeOneJabber extends Thread {

    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    public ServeOneJabber(Socket s) throws IOException {
        socket = s;

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // Enable auto-flush:
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        
        start(); // Calls run()
    }


    public static String actualDate(){
        Date date=new Date();
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/YYYY");
        
        return formatDate.format(date);
    }
    
    public static void ExportCSV(List<User> users){
      String outputFile = "Users.csv"; //Name File
      boolean exists = new File(outputFile).exists(); //verifies if exists

      //If exists a file called like this it erease it
      if(exists){
          File usersFile = new File(outputFile);
          usersFile.delete();
      }
      try {
          //creates the file
          CsvWriter outputCSV = new CsvWriter(new FileWriter(outputFile, true), '.');
          //Data to identify
          outputCSV.write("Opeartion");
          outputCSV.write("Result");
          outputCSV.write("Date");

          outputCSV.endRecord();//stops typing 

          //roam the list
          for (User user :users ){
              outputCSV.write(user.getName());
              outputCSV.write(user.getPhone());
              outputCSV.write(user.getEmail());

              outputCSV.endRecord();// stop ty[ing on file
          }

          outputCSV.close();//close file
      } catch (IOException e) {
      }
    }
    
    public static void ImportCSV(){
        try{
            List<User> users = new ArrayList<User>();//list to save data from file
            
            CsvReader readUser = new CsvReader("Users.csv");
            readUser.readHeaders();
            
            // while it has lines we get data file
            while (readUser.readRecord()) {
                String name = readUser.get(0);
                String phone = readUser.get(1);
                String email = readUser.get(2);
             
                users.add(new User(name, phone , email)); // add info to the list
            }
           // readUser.close();
            
            for (User user : users){
                System.out.println(user.getName()+ " "
                + user.getPhone()+","
                + user.getEmail());
                
            }
        }catch(FileNotFoundException e){
        }catch(IOException e){
        }
    }
    @Override
    public void run() {
       
        try {
            while (true) {
                 List<User> users = new ArrayList<>();
                String msg_recibido = in.readLine(); //Leo todo lo que envíe el socket sc
                
                int string_len = msg_recibido.length();
                if(string_len > 1){
                    
                    System.out.println("Ciente envía:" + msg_recibido);
                    msg_recibido = "(" + msg_recibido;
                    msg_recibido += ")";
                    GFG.nptr root = build(msg_recibido);

                    // Function call
                    postorder(root);
                    System.out.println("\n");


                    out.println("Respuesta: " + GFG.result);
                    String answer= GFG.result+"";
                    users.add(new User(msg_recibido,answer,actualDate()));
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

public class MultiJabberServer {
    
     /*public static String actualDate(){
        
        Date date=new Date();
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/YYYY");
        
        return formatDate.format(date);
    }
    
      public static void ExportCSV(List<User> users){
        String outputFile = "Users.csv";//Name File
        boolean exists = new File(outputFile).exists(); //verifies if exists
        
        //If exists a file called like this it erease it
        if(exists){
            File usersFile = new File(outputFile);
            usersFile.delete();
        }
        try {
            //creates the file
            CsvWriter outputCSV = new CsvWriter(new FileWriter(outputFile, true), '.');
            //Data to identify
            outputCSV.write("Name");
            outputCSV.write("Phone");
            outputCSV.write("Email");
            
            outputCSV.endRecord();//stops typing 
            
            //roam the list
            for (User user :users ){
                outputCSV.write(user.getName());
                outputCSV.write(user.getPhone());
                outputCSV.write(user.getEmail());
                
                outputCSV.endRecord();// stop ty[ing on file
            }
            
            outputCSV.close();//close file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void ImportCSV(){
        try{
            List<User> users = new ArrayList<>();//list to save data from file
            
            CsvReader readUser = new CsvReader("Users.csv");
            readUser.readHeaders();
            
            // while it has lines we get data file
            while (readUser.readRecord()) {
                String name = readUser.get(0);
                String phone = readUser.get(1);
                String email = readUser.get(2);
             
                users.add(new User(name, phone , email)); // add info to the list
            }
           // readUser.close();
            
            for (User user : users){
                System.out.println(user.getName()+ " "
                + user.getPhone()+","
                + user.getEmail());
                
            }
        }catch(FileNotFoundException e){
            e.printStackTrace(); 
        }catch(IOException e){
            e.printStackTrace();
        }
    }*/

    static final int PORT = 8080;

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
