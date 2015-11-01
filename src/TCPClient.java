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
        leSkt l;
        Informations info = new Informations("Server1.properties");
        FindPrimaryServer F = new FindPrimaryServer();

        if(F.isPrimaryServer(info.getThisHost(),info.getThisPort())==true)
        {
            System.out.println("Server primario no host " + info.getThisHost()+" e no porto "+info.getThisPort());
            l = new leSkt(info.getThisHost(),info.getThisPort(),info);

        }
        else if(F.isPrimaryServer(info.getOtherHost(),info.getOtherPort())==true)
        {
            System.out.println("Server primario no host " + info.getOtherHost()+" e no porto "+info.getOtherPort());
            l = new leSkt(info.getOtherHost(),info.getOtherPort(),info);
        }
        else
        {
            System.out.println("Nenhum Servidor ligado");
        }

    }
}

// Thread para estar constantemente a ler do socket - Para imprimir resultados
class leSkt extends Thread {
    Socket serverSocket;
    Informations info;
    DataInputStream in = null;
    DataOutputStream out;
    int port;
    String host;


    public leSkt(String host, int port, Informations info) {

        this.serverSocket = null;
        this.start();
        this.host = host;
        this.port = port;
        this.info = info;
    }


    public void run() {
        EscreveSck es=null;
        //Tentativa de ligação ao servidor
        while(true) {

            while(true){
            try {

                serverSocket = new Socket(host, port);
                System.out.println("Cliente ligado ao server no host: " + host + " no porto: " + port);
                in = new DataInputStream(this.serverSocket.getInputStream());
                out = new DataOutputStream(this.serverSocket.getOutputStream());
                es = new EscreveSck(this.serverSocket);


                while (true) {
                    String data = in.readUTF();
                    System.out.println(data);
                }

            } catch (EOFException e) {
                int tentativas = 3;
                System.out.println("Server em baixo... Tentativa de religação");
                while(tentativas>0)
                {
                    try {
                        System.out.println("Tentativa "+(4-tentativas)+"...");
                        serverSocket = new Socket(host, port);
                        System.out.println("Cliente ligado ao server no host: " + host + " no porto: " + port);
                        in = new DataInputStream(this.serverSocket.getInputStream());
                        out = new DataOutputStream(this.serverSocket.getOutputStream());
                        es = new EscreveSck(this.serverSocket);

                    } catch (UnknownHostException e1) {
                        if(tentativas==1){
                            System.out.println("Religação sem sucesso. Falha longa...\n Procurando outro server.");
                        }
                        try {
                            sleep(2000);
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }

                        tentativas--;
                    } catch (IOException e1) {
                        if(tentativas==1){
                            System.out.println("Religação sem sucesso. Falha longa...\n Procurando outro server.");
                        }

                        try {
                            sleep(2000);
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                        tentativas--;



                    }
                }




            } catch (IOException e) {

                try {

                    this.currentThread().sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                System.out.print("Servidor Primário em baixo.\nTentativa de ligação ao servidor secundário.");
                System.out.println("IO:" + e);
                FindPrimaryServer f = new FindPrimaryServer();
                if(f.isPrimaryServer(info.getThisHost(),info.getThisPort())==true)
                {
                    host = info.getThisHost();
                    port = info.getOtherPort();
                }
                else if(f.isPrimaryServer(info.getOtherHost(),info.getOtherPort())==true)
                {
                    host = info.getOtherHost();
                    port = info.getOtherPort();
                }
                else
                {
                    System.out.println("Nenhum server on...");
                    System.exit(1);
                }



            }
        }}
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

            System.out.println("Introduza texto:");

            // 3o passo
            while (true) {
                String texto = "";
                InputStreamReader input = new InputStreamReader(System.in);
                BufferedReader reader = new BufferedReader(input);
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
                    out.flush();

                } catch (Exception e) {


                }
            }
        }
    }

}

class FindPrimaryServer
{

    boolean isPrimaryServer(String host, int port)
    {

        //Ping para encontrar servers
        DatagramSocket aSocket2 = null;
        String texto = "HEARTBEAT";
        int tentativas = 3;
        while(tentativas>0) {
            try {
                aSocket2 = new DatagramSocket();
                int pedidos = 3;
                while (pedidos > 0) {

                    byte[] m = texto.getBytes();
                    InetAddress aHost = InetAddress.getByName(host);
                    int serverPort = port;
                    DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);
                    aSocket2.send(request);
                    byte[] buffer = new byte[1000];
                    DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                    aSocket2.setSoTimeout(1000);
                    //TODO TIMEOUT aqui também
                    aSocket2.receive(reply);
                    pedidos--;
                    //System.out.println("Recebeu: " + new String(reply.getData(), 0, reply.getLength()));
                }
                tentativas--;
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {

                if(tentativas>1)
                {
                    tentativas--;
                }
                else
                    return false;
            }
        }
        return true;
    }
}
