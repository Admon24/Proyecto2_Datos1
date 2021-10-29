package p2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
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

    @Override
    public void run() {
        try {
            while (true) {
                
                String msg_recibido = in.readLine(); //Leo todo lo que envíe el socket sc
                
                if(msg_recibido != null){
                    System.out.println("Ciente envía:" + msg_recibido);
                    msg_recibido = "(" + msg_recibido;
                    msg_recibido += ")";
                    GFG.nptr root = build(msg_recibido);

                    // Function call
                    postorder(root);
                    System.out.println("\n");


                    out.println("Respuesta: " + GFG.result);
                }
                else{
                    System.out.println("Error");
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
