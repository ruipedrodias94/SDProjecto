import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;


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
        return "RMI MANDOU: " + msg;
    }

    public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {

        Properties props = new Properties();
        FileInputStream file = null;

        try{
            file = new FileInputStream("clientConf.properties");
            props.load(file);
            System.out.println("Leu do ficheiro de configuracoes com sucesso!");

        } catch (FileNotFoundException e) {
            System.out.println("Nao encontrou o ficheiro de configuracoes!");
            System.out.println(e.getLocalizedMessage());
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }

        try {
            //Servidor_RMI servidor_rmi = new Servidor_RMI();
            int rmiregistry = Integer.parseInt(props.getProperty("rmiRegistry"));
            String rmirebind = (String) props.getProperty("rmiRebind");

            RMI_Interface servidor_rmi = new Servidor_RMI();
            LocateRegistry.createRegistry(rmiregistry).rebind(rmirebind, servidor_rmi);
            System.out.println("Servidor RMI ligado!");

        }catch (RemoteException e){
            System.out.println("PUTA VIDA MERDA CAGALHOES");
            System.out.println(e.getLocalizedMessage());
        }
    }
}
