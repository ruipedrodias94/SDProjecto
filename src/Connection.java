import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by jorgearaujo on 31/10/15.
 */
// = Thread para tratar de cada canal de comunicacao com um cliente
class Connection extends Thread {
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    int thread_number;
    Shared_Clients sc = new Shared_Clients();
    RMI_DataBase_Interface servidor_rmi;
    ArrayList<String> useres = new ArrayList<>();

    public Connection(Socket aClientSocket, int numero, RMI_DataBase_Interface servidor_rmi) {
        thread_number = numero;
        sc.addClient(aClientSocket);
        this.servidor_rmi = servidor_rmi;
        try {
            clientSocket = aClientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(aClientSocket.getOutputStream());
            out.writeUTF("Bem vindo\n");
            this.start();
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    // =============================
    public void run() {
        String resposta;

        try {
            while (true) {
                // an echo server
                String data = in.readUTF();
                System.out.println("T[" + thread_number + "] Recebeu: " + data);

				/*int num = servidor_rmi.getUsers();

				resposta = String.valueOf(num);*/
                useres = servidor_rmi.showCities();

                for (int i = 0; i < useres.size() ; i++) {
                    resposta = useres.get(i);
                    sc.send_clients(resposta, thread_number);
                }



            }
        } catch (EOFException e) {
            System.out.println("EOF:" + e);
        } catch (IOException e) {
            Informations info = new Informations("Server1.properties");
            System.out.println("IO:" + e);
            System.getProperties().put("java.security.policy", "security.policy");
            System.setSecurityManager(new RMISecurityManager());
            try {
                System.out.println("RMI em baixo... tentativa de religação....");
                this.servidor_rmi = (RMI_DataBase_Interface) LocateRegistry.getRegistry(info.getRmiIP(), info.getRmiRegistry()).lookup(info.getRmiRebind());
            } catch (RemoteException e1) {
                System.out.println("Religação falhada... Tente mais tarde.");
                System.exit(2);
            } catch (NotBoundException e1) {
                e1.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

class Shared_Clients {
    public static ArrayList<Socket> clientes = new ArrayList<Socket>();
    DataOutputStream out;

    synchronized void addClient(Socket c) {
        clientes.add(c);
        System.out.println("Cliente adicionado");
    }

    synchronized void send_clients(String msg, int clintNmr) {


        int i;
        for (i = 0; i < clientes.size(); i++) {

            try {

                if (i == 0) {
                    msg = "de cliente " + clintNmr + " : " + msg;
                }

				/*Para enviar para o mesmo queliente*/

                if(i==clintNmr){
                    out = new DataOutputStream(clientes.get(i).getOutputStream());
                    out.writeUTF(msg);
                    System.out.println("Enviado de cliente " + clintNmr + " para " + i);
                }

            } catch (IOException e) {
                System.out.println("IO:" + e);
            }
        }
    }
}