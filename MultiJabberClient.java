package p2;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.net.*;
import java.io.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class JabberClientThread extends Thread {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private static int counter = 1;
    public int id = counter++;
    private static int threadcount = 0;
    public String msg_recibido;

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
    BufferedReader lector = null;
    PrintWriter escritor = null;
    
    //historial
    JFrame ventana_historial = null;
    JButton btn_back = null;
    JTextArea area_historial= null;
    JPanel contenedor_areahistorial=null;
    JScrollPane scroll_historial = null;
    JPanel contenedor_btnback = null;

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
    }

    public static int threadCount() {
        return threadcount;
    }

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
    public void run() {
        
        try {
            while(true){
                btn_enviar.addActionListener((ActionEvent e) -> {
                    String enviar_msg = txt_msg.getText(); //Obtengo el mensaje en la caja de texto
                    out.println(enviar_msg); //Envío el mensaje
                    txt_msg.setText(null); //Se limpia la caja de texto
                    area_chat.setText(null); //Limpia la caja del JTextArea
                });
                
                btn_historial.addActionListener((ActionEvent event) ->{
                    //ventana_chat.setVisible(false);
                    ventana_historial = new JFrame("Historial");
                    btn_back = new JButton("Back");
                    area_historial = new JTextArea(10,12);
                    scroll = new JScrollPane(area_historial);
                    contenedor_areahistorial = new JPanel();
                    contenedor_areahistorial.setLayout(new GridLayout(1, 1));
                    contenedor_areahistorial.add(scroll);
                    contenedor_btnback = new JPanel();
                    contenedor_btnback.setLayout(new GridLayout(1, 2));
                    contenedor_btnback.add(btn_back);
                    ventana_historial.setLayout(new BorderLayout());
                    ventana_historial.add(contenedor_areahistorial, BorderLayout.NORTH);
                    ventana_historial.add(contenedor_btnback, BorderLayout.SOUTH);
                    ventana_historial.setSize(350,225);
                    ventana_historial.setVisible(true);
                    ventana_historial.setResizable(false);
                });
                
                /*btn_back.addActionListener((ActionEvent event) -> {
                    ventana_chat.setVisible(true);
                    ventana_historial.setVisible(false);
                });*/
                
                String msg_recibido = in.readLine(); //Leo todo lo que envíe el socket sc
                if(msg_recibido != null){
                    area_chat.append("Servidor envía: " + msg_recibido + "\n"); //Pintamos el mensaje recibido en la ventana
                    System.out.println("Servidor envía: " + msg_recibido);
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

public class MultiJabberClient {
   

    static final int MAX_THREADS = 1;

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
