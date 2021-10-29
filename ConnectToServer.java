
package p2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectToServer {
    
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    
    public ConnectToServer(){
        
        try {
            
            this.socket = new Socket("localhost", 8080);
            this.inputStream = new DataInputStream(this.socket.getInputStream());
            this.outputStream = new DataOutputStream(this.socket.getOutputStream());
            
        } catch (IOException ex) {
            Logger.getLogger(ConnectToServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String recibeMsg(){
        
        String fromServer = "";
        
        try {
            this.outputStream.writeUTF("msg"); //Aquí se tiene que recibir el mensaje que envía el cliente
            fromServer = this.inputStream.readUTF();
        } catch (IOException ex) {
            Logger.getLogger(ConnectToServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fromServer;
    }
    
    public void stopConnection(){
        
        try { //Aquí hay que decirle que cierre la conexión
            //this.outputStream.writeUTF("msg"); //Aquí se tiene que recibir el mensaje que envía el cliente
            inputStream.close();
            outputStream.close();
            socket.close();
            System.out.println("Client disconnected...");
            
        } catch (IOException ex) {
            Logger.getLogger(ConnectToServer.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
}
