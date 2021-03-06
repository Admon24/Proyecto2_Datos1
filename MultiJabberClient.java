package p2;

import com.csvreader.CsvReader;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * This is the main class, It makes work the client connection
 * @author Andrés & Adrián
 */
class JabberClientThread extends Thread {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private static int counter = 1;
    public int id = counter++;
    private static int threadcount = 0;
    public String msg_received;

    //Creación de objetos
    JFrame ventana_chat = null;
    JButton btn_enviar = null;
    JButton btn_historial=null;
    JTextField txt_msg = null;
    JTextArea area_chat = null;
    JPanel contenedor_areachat = null;
    JPanel contenedor_btntxt = null;
    JScrollPane scroll = null;
    ServerSocket servidor = null;
    Socket sc = null;
    //BufferedReader lector = null;
    //PrintWriter escritor = null;
    
    //historial
    JFrame ventana_historial = null;
    //JButton btn_back = null;
    JTextArea area_historial= null;
    JPanel contenedor_areahistorial=null;
    JScrollPane scroll_historial = null;
    JPanel contenedor_btnback = null;

    /**
     * This class is for create the programs interface
     */
    public void hacerInterfaz() {
        //Aquí se construye la interfaz de usuario
        ventana_chat = new JFrame("Client");
        btn_enviar = new JButton("=");
        btn_historial = new JButton("Historial");
        txt_msg = new JTextField();
        area_chat = new JTextArea(10, 12);
        scroll = new JScrollPane(area_chat);
        contenedor_areachat = new JPanel();
        contenedor_areachat.setLayout(new GridLayout(1, 1));
        contenedor_areachat.add(scroll);
        contenedor_btntxt = new JPanel();
        contenedor_btntxt.setLayout(new GridLayout(1, 2));
        contenedor_btntxt.add(txt_msg);
        contenedor_btntxt.add(btn_enviar);
        contenedor_btntxt.add(btn_historial);
        ventana_chat.setLayout(new BorderLayout());
        ventana_chat.add(contenedor_areachat, BorderLayout.NORTH);
        ventana_chat.add(contenedor_btntxt, BorderLayout.SOUTH);
        ventana_chat.setSize(350, 225);
        ventana_chat.setVisible(true);
        ventana_chat.setResizable(false);
        area_chat.setText("1. No digite letras" + 
                          "\n" +
                          "2.Digite solo números de [0,9]" + 
                          "\n" + 
                          "3. Utilice solo los operandos de +,-,*,/,%");
    }

/*    public static int threadCount() {
        return threadcount;
    }*/

    /**
     * It starts the connection client to server
     * @param addr socket
     */
    public JabberClientThread(InetAddress addr) {
        System.out.println("Making client " + id);
        threadcount++;
        hacerInterfaz();    

        try {
            socket = new Socket(addr, MultiJabberServer.PORT);

        } catch (IOException e) {
            // If the creation of the socket fails, 
            // nothing needs to be cleaned up.
        }
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Enable auto-flush:
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        } catch (IOException e) {
            // The socket should be closed on any 
            // failures other than the socket 
            // constructor:
            try {
                socket.close();
            } catch (IOException e2) {
            }
        } finally {
            //socket = null;
            //out = null;
        }
        
        // Otherwise the socket will be closed by
        // the run() method of the thread.
        start();
    }
    
    @Override
    /**
     * This method keeps the connection between the server and the client
     */
    public void run() {
        
        try {
            while(true){
                btn_enviar.addActionListener((ActionEvent e) -> {
                    area_chat.setText(null);
                    String enviar_msg = txt_msg.getText(); //Obtengo el mensaje en la caja de texto
                    out.println(enviar_msg); //Envío el mensaje
                    txt_msg.setText(null); //Se limpia la caja de texto
                    area_chat.setText(null); //Limpia la caja del JTextArea
                });
                
                btn_historial.addActionListener((ActionEvent event) ->{
                    //ventana_chat.setVisible(false);
                    ventana_historial = new JFrame("Historial");
                    //btn_back = new JButton("Back");
                    area_historial = new JTextArea(10,12);
                    scroll = new JScrollPane(area_historial);
                    contenedor_areahistorial = new JPanel();
                    contenedor_areahistorial.setLayout(new GridLayout(1, 1));
                    contenedor_areahistorial.add(scroll);
                    //contenedor_btnback = new JPanel();
                    //contenedor_btnback.setLayout(new GridLayout(1, 2));
                    //contenedor_btnback.add(btn_back);
                    ventana_historial.setLayout(new BorderLayout());
                    ventana_historial.add(contenedor_areahistorial, BorderLayout.NORTH);
                    //ventana_historial.add(contenedor_btnback, BorderLayout.SOUTH);
                    ventana_historial.setSize(350,225);
                    ventana_historial.setVisible(true);
                    ventana_historial.setResizable(false);
                    
                    //Import data from csv file
                    try{
                        List<User> users = new ArrayList<>();//list to save data from file

                        CsvReader readUser = new CsvReader("Users.csv");
                        readUser.readHeaders();

                        // while it has lines we get data file
                        while (readUser.readRecord()) {
                            String operation = readUser.get(0);
                            String result = readUser.get(1);
                            String date = readUser.get(2);

                            users.add(new User(operation, result , date)); // add info to the list
                            System.out.println(operation);
                            System.out.println(result);
                            System.out.println(date);
                        }
                       //readUser.close();
                        String file = "";
                        for (User user : users){
                            file += user.getOp() + user.getResult() + user.getDate() + "\n";
                        }
                        area_historial.setText("Operation  Result  Date" + "\n" + file);
                    }catch(FileNotFoundException e){
                        e.printStackTrace();
                    }catch(IOException e){
                    }
                });
                
                /*btn_back.addActionListener((ActionEvent event) -> {
                    ventana_chat.setVisible(true);
                    ventana_historial.setVisible(false);
                });*/
                
                String msg_received = in.readLine(); //Leo todo lo que envíe el socket sc
                if(msg_received != null){
                    area_chat.append("Servidor envía: " + msg_received + "\n"); //Pintamos el mensaje recibido en la ventana
                    System.out.println("Servidor envía: " + msg_received);
                }
               
                /*String msg = txt_msg.getText();
                out.println("Client " + id + ": " );
                out.println(msg);
                System.out.println(msg);
                String str = in.readLine(); //Obtiene --> Client " + id + ": " + i
                System.out.println(str);*/
            }
            /*for (int i = 0; i < 1; i++) {

                btn_enviar.addActionListener((ActionEvent e) -> {
                    String enviar_msg = txt_msg.getText(); //Obtengo el mensaje en la caja de texto
                    out.println(enviar_msg); //Envío el mensaje
                    System.out.println(enviar_msg);
                    txt_msg.setText(""); //Se limpia la caja de texto
                });

                String msg_recibido = in.readLine(); //Leo todo lo que envíe el socket sc
                area_chat.append("Servidor envía: " + msg_recibido + "\n"); //Pintamos el mensaje recibido en la ventana
                System.out.println(msg_recibido);

                String msg = txt_msg.getText();
                out.println("Client " + id + ": " + i);
                out.println(msg);
                System.out.println(msg);
                String str = in.readLine(); //Obtiene --> Client " + id + ": " + i
                System.out.println(str);
            }*/

        } catch (IOException e) {
        } finally {
            // Always close it:
            try {
                socket.close();
            } catch (IOException e) {
            }
            threadcount--; // Ending this thread
        }
    }
}
/**
 * This is the main class, it creates the cantity of clients we want
 * @author Andrés & Adrián
 */
public class MultiJabberClient {

    static final int MAX_THREADS = 1;
/**
 * Creates the clients
 * @param args
 * @throws IOException
 * @throws InterruptedException 
 */
    public static void main(String[] args) throws IOException, InterruptedException {
        InetAddress addr = InetAddress.getByName(null);
        int count = 0;
        while (true) {
            if (count < MAX_THREADS) {
                count = new JabberClientThread(addr).id;
                Thread.sleep(400); //Milli seconds
                //System.out.println(Thread.currentThread());
            }

        }
    }
}
