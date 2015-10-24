import java.rmi.*;
import java.sql.SQLException;

/**
 * Created by ruype_000 on 21/10/2015.
 */
public interface RMI_DataBase_Interface extends Remote {

    /*
    Esta classe vai servir para meter os métodos do servidor RMI, para depois um gajo vir aqui buscar.
     */

    public int getUsers() throws SQLException, RemoteException;
}
