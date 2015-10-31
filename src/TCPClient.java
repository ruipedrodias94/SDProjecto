import java.net.*;
import java.io.*;
import java.util.*;

// Tem que que ler comandos do utilizador e imprimir a resposta
// Thread a ler pedidos - feito
// Imprimir resultados - claro que não, né?


public class TCPClient {
    public  static boolean debug = true;
    public static void main(String args[]) throws IOException {

        Socket s =null ;

        leSkt l = new leSkt();
    }
}

// Thread para estar constantemente a ler do socket - Para imprimir resultados
class leSkt extends Thread {
    Socket serverSocket;
    DataInputStream in = null;
    DataOutputStream out;
    Informations info = new Informations("Server1.properties");

    public leSkt() {

        this.serverSocket = null;
        this.start();
    }


    public void run() {
        EscreveSck es=null;
        while(true){
            try {

                serverSocket = new Socket(info.getThisHost(),info.getThisPort());
                System.out.println("Cliente ligado ao server no host: " + info.getThisHost() + " no porto: " + info.getThisPort());
                in = new DataInputStream(this.serverSocket.getInputStream());
                out = new DataOutputStream(this.serverSocket.getOutputStream());
                es = new EscreveSck(this.serverSocket);



                while (true) {
                    String data = in.readUTF();
                    System.out.println(data);
                }
            } catch (EOFException e) {
                System.out.println("EOF:" + e);
            } catch (IOException e) {
                System.out.println("Waiting to join-----------------------");
                try {
                    es.join();
                    System.out.println("joined thread-----");
                } catch (InterruptedException e1) {
                    if (TCPClient.debug)
                        e1.printStackTrace();
                }


                try {

                    this.currentThread().sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                System.out.print("Servidor Primário em baixo.\nTentativa de ligação ao servidor secundário.");
                System.out.println("IO:" + e);



            } }
    }
}

class EscreveSck extends Thread
{
    Socket s;
    DataInputStream in;
    DataOutputStream out = null;
    Informations info = new Informations("Server1.properties");


    public  EscreveSck(Socket s)
    {
        this.s = s;
        this.start();
    }

    public void run()
    {
        while(true){
            String texto = "";
            InputStreamReader input = new InputStreamReader(System.in);
            BufferedReader reader = new BufferedReader(input);
            System.out.println("Introduza texto:");

            // 3o passo
            while (true) {
                // READ STRING FROM KEYBOARD
                try {
                    texto = reader.readLine();

                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                }
                // WRITE INTO THE SOCKET
                try {
                    //out = new DataOutputStream(s.getOutputStream());
                    this.out = new DataOutputStream(s.getOutputStream());
                    out.writeUTF(texto);

                } catch (Exception e) {

                    break;
                }
            }
            break;
        }
    }

}
