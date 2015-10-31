import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
/**
 * Created by jorgearaujo on 31/10/15.
 */
public class Ping extends Thread{

    Informations info;
    // Por definição o server começa como Secundário
    public static final boolean PRIMARIO = true;
    public static final boolean SECUNDARIO = false;
    boolean serverState= SECUNDARIO;
    TCPLink ligacaoCliente;



    public void isPrincipal()
    {
        if(this.serverState==PRIMARIO)
        {
            System.out.println("[Este servidor é agora Primario]");
        }
        else if(this.serverState==SECUNDARIO)
        {
            System.out.println("[Este servidor é agora Secundário]");
        }
    }

    public void startPing(String filename){
        this.info  = new Informations(filename);
        System.out.println("Iniciando ping...");
        this.start();
    }


    public void run()
    {
        while(true){
            //Se o servidor for primario
            if(this.serverState == PRIMARIO)
            {

                DatagramSocket aSocket = null;
                String s;
                try{
                    isPrincipal();
                    aSocket = new DatagramSocket(this.info.getReceiveUdpPingPort());
                    System.out.println("Esperando ping requests no porto: " + this.info.getReceiveUdpPingPort());
                    this.ligacaoCliente = new TCPLink(this.info);
                    while(true){
                        byte[] buffer = new byte[1000];
                        DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                        aSocket.receive(request);
                        s=new String(request.getData(), 0, request.getLength());
                        //System.out.println("Server Recebeu: " + s);
                        aSocket.setSoTimeout(2000);
                        //TODO Colocar timeout a carregar do ficheiro de configurações
                        DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(), request.getAddress(), request.getPort());
                        aSocket.send(reply);
                    }}
                catch (SocketException e){System.out.println("Socket: " + e.getMessage());
                }catch (IOException e) {
                    aSocket.close();
                    System.out.println("IO: " + e.getMessage());
                    System.out.println("Servidor Secundário em baixo... Em casa de falha não há backup server.");
            }}
            else if(this.serverState==SECUNDARIO)
            {
                //Ping caso secundário
                DatagramSocket aSocket2 = null;
                String texto = "HEARTBEAT";
                isPrincipal();
                int tentativas = 3;
                while(tentativas>0){
                    try {
                        aSocket2 = new DatagramSocket();
                        System.out.println("Mandando Ping resquests para o host: " + info.getOtherHost() + " no porto: " + info.getSendUdpPingPort());
                        TCPLink ligacaoCliente = new TCPLink(this.info);
                        while(true)
                        {

                            byte [] m = texto.getBytes();
                            InetAddress aHost = InetAddress.getByName(this.info.getOtherHost());
                            int serverPort = this.info.getSendUdpPingPort();
                            DatagramPacket request = new DatagramPacket(m,m.length,aHost,serverPort);
                            aSocket2.send(request);
                            byte[] buffer = new byte[1000];
                            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                            aSocket2.setSoTimeout(1000);
                            //TODO TIMEOUT aqui também
                            aSocket2.receive(reply);
                            //System.out.println("Recebeu: " + new String(reply.getData(), 0, reply.getLength()));
                        }
                    }
                    catch (SocketException e){System.out.println("Socket: " + e.getMessage());
                    }catch (IOException e){
                        System.out.println("IO: " + e.getMessage());
                        if(tentativas>1){
                            tentativas--;
                            try {
                                    sleep(1000);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }}

                        else {
                            System.out.println("Sem Resposta. Tentativa de ligação como servidor Primário");
                            this.serverState = PRIMARIO;
                            aSocket2.close();
                            break;
                        }
                    }
                    finally {
                        if(ligacaoCliente!=null){
                        try {
                            System.out.print("joining the thread");
                            ligacaoCliente.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        }
                    }
                }
            }
        }

    }
}