import java.io.IOException;
import java.net.*;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;

/**
 * Created by jorgearaujo on 31/10/15.
 */
public class TCPLink extends Thread{
    int client_number = 0;
    int port;
    String host;
    Informations info;
    RMI_DataBase_Interface clienteRMI = null;
    ServerSocket listenSocket;

    public TCPLink(Informations info) throws IOException {
        this.info = info;
        port = info.getThisPort();
        host = info.getThisHost();
        this.start();
    }



    public void run()
    {
        try {
            if(listenSocket!=null)
            {
                System.out.print("Socket closed");
                listenSocket.close();
            }
            //Criar socket de ligacao ao cliente
            InetAddress hostAddress = InetAddress.getByName(this.host);
            listenSocket = new ServerSocket(this.port,50,hostAddress);
            listenSocket.setReuseAddress(true);
            System.out.println("[Ligacao TCP à escuta no host: " + hostAddress + " no porto " + this.port + "]");


            //Tentativa de ligacao ao servidor RMI
            try {

                System.getProperties().put("java.security.policy", "security.policy");
                System.setSecurityManager(new RMISecurityManager());
                clienteRMI = (RMI_DataBase_Interface) LocateRegistry.getRegistry(info.getRmiIP(), info.getRmiRegistry()).lookup(info.getRmiRebind());
                System.out.println("Cliente RMI ligado!");
            }catch (Exception e)
            {
                System.out.println("Error establishing connection with RMI.\nPlease try again later");
                System.exit(1);
            }

            //Threads de ligacao ao cliente TCP
            while (true) {
                Socket clientSocket = listenSocket.accept(); // BLOQUEANTE
                new Connection(clientSocket, this.client_number, clienteRMI);


                this.client_number++;
            }


        } catch (UnknownHostException e) {
            try {
                currentThread().join();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } catch (IOException e) {
            try {
                currentThread().join();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        finally {
            try {
                currentThread().join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(listenSocket!=null){
                try {
                    System.out.print("Socket closed");
                    listenSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }}

        }
    }
}
