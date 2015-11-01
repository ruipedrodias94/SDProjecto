import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by ruype_000 on 21/10/2015.
 */
public class RMI_DataBase {

    public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {

        Informations info = new Informations("Server1.properties");

        try {

            RMI_DataBase_Interface servidor_rmi = new DataBase();
            LocateRegistry.createRegistry(info.getRmiRegistry()).rebind(info.getRmiRebind(), servidor_rmi);
            System.out.println("RMI on e a espera de Receber! ");

            ArrayList<String> cona = servidor_rmi.listarProjectos_Actuais();
            for (int i = 0; i < cona.size(); i++) {
                System.out.println(cona.get(i));
            }


            ArrayList<String> cona1 = servidor_rmi.listarRecompensas_Projecto(6);
            for (int i = 0; i < cona1.size(); i++) {
                System.out.println(cona1.get(i));
            }




        }catch (RemoteException e){
            System.out.println(e.getLocalizedMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }



    }
}
