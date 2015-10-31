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

    public ArrayList<String> showCities() throws SQLException, RemoteException;

    /*
    Fazer cautela porque ainda vamos ter que adicionar muitos mais metodos!
     */

    /*================================Metodos_de_Listar=========================================*/
    public void listarProjectos_Actuais() throws RemoteException, SQLException;
    public void listarProjectos_Antigos() throws RemoteException, SQLException;
    public void listarDetalhes_Projecto() throws RemoteException, SQLException;
    public void listarSaldo_Pessoal() throws RemoteException, SQLException;
    public void listarRecompensas_Pessoais() throws RemoteException, SQLException;


    /*================================Metodos_de_Accoes=========================================*/
    public void adicionarRecompensas_Projecto() throws RemoteException, SQLException;
    public void removerRecompensas_Projecto() throws RemoteException, SQLException;
    public void enviarMensagens_Projecto() throws RemoteException, SQLException;
    public void doarDinheiro() throws RemoteException, SQLException;
    public void criarProjecto() throws RemoteException, SQLException;
    public void cancelarProjecto() throws RemoteException, SQLException;
    public void responderMensagens() throws RemoteException, SQLException;


    public void registarConta(String nome_Cliente, String user_Name, String password, int saldo) throws RemoteException, SQLException;
    public void fimProjecto () throws RemoteException, SQLException;


    /*================================Login====================================================*/
    public void login() throws RemoteException, SQLException;

}
