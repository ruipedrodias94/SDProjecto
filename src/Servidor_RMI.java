import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


/**
 * Created by ruype_000 on 21/10/2015.
 */
public class Servidor_RMI extends UnicastRemoteObject implements RMI_Interface {

    private static final long serialVersionUID = 1L;

    public Servidor_RMI() throws RemoteException{
        super();
    }

    /*
    Mandar uma simpes mensagem que recebe
     */

    public String sayHello(String msg){
        return "RMI MANDOU:" + msg;
    }

    public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {

        try {
            //Servidor_RMI servidor_rmi = new Servidor_RMI();
            RMI_Interface servidor_rmi = new Servidor_RMI();
            LocateRegistry.createRegistry(1235).rebind("serverRmi", servidor_rmi);
            System.out.println("Servidor RMI ligado!");

        }catch (RemoteException e){
            System.out.println("PUTA VIDA MERDA CAGALHOES");
            System.out.println(e.getLocalizedMessage());
        }
    }
}
