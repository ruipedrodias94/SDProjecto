import java.rmi.*;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by ruype_000 on 21/10/2015.
 */
public interface RMI_DataBase_Interface extends Remote {

    /*
    Esta classe vai servir para meter os métodos do servidor RMI, para depois um gajo vir aqui buscar.
     */

    int getUsers() throws SQLException, RemoteException;
    ArrayList<String> getUserNames() throws SQLException, RemoteException;

}
