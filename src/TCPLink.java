import java.io.IOException;
import java.net.*;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
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

    public TCPLink(Informations info, int port, String host1) throws IOException {
        this.info = info;
        this.port = port;
        host = host1;
        this.start();
    }



    public void run()
    {
            try {


                //Criar socket de ligacao ao cliente
                InetAddress hostAddress = InetAddress.getByName(this.host);
                listenSocket = new ServerSocket(this.port, 500, hostAddress);
                System.out.println("[Ligacao TCP à escuta no host: " + hostAddress + " no porto " + this.port + "]");


                //Tentativa de ligacao ao servidor RMI
                try {

                    System.getProperties().put("java.security.policy", "security.policy");
                    System.setSecurityManager(new RMISecurityManager());
                    clienteRMI = (RMI_DataBase_Interface) LocateRegistry.getRegistry(info.getRmiIP(), info.getRmiRegistry()).lookup(info.getRmiRebind());
                    System.out.println("Cliente RMI ligado!");
                } catch (Exception e) {

                    e.printStackTrace();
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
                    e.printStackTrace();
                    currentThread().join();
                    System.out.print("JOINED");
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            } catch (RemoteException c)
            {
                try {
                    System.out.println("Tentativa Religação ao server RMI...");
                    System.getProperties().put("java.security.policy", "security.policy");
                    System.setSecurityManager(new RMISecurityManager());
                    clienteRMI = (RMI_DataBase_Interface) LocateRegistry.getRegistry(info.getRmiIP(), info.getRmiRegistry()).lookup(info.getRmiRebind());
                    System.out.println("Cliente RMI ligado!");
                } catch (Exception e1) {

                    e1.printStackTrace();
                    System.out.println("Error establishing connection with RMI.\nPlease try again later");
                    System.exit(1);
                }
            }
            catch (IOException e) {
                    e.printStackTrace();


            } finally {
                try {
                    currentThread().join();
                    System.out.print("JOINED");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (listenSocket != null) {
                    try {
                        System.out.print("Socket closed");
                        listenSocket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

            }

    }
}
