import java.rmi.*;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by ruype_000 on 21/10/2015.
 */
public interface RMI_DataBase_Interface extends Remote {

    /*
    Esta classe vai servir para meter os m�todos do servidor RMI, para depois um gajo vir aqui buscar.
     */

    public ArrayList<String> showCities() throws SQLException, RemoteException;

    /*
    Fazer cautela porque ainda vamos ter que adicionar muitos mais metodos!
     */

    /*================================Metodos_de_Listar=========================================*/
    public ArrayList<String> listarProjectos_Actuais() throws RemoteException, SQLException;
    public ArrayList<String> listarProjectos_Antigos() throws RemoteException, SQLException;
    public String listarDetalhes_Projecto(int id_Projecto) throws RemoteException, SQLException;
    public String listarSaldo_Pessoal(int id_Cliente) throws RemoteException, SQLException;
    public ArrayList<String> listarRecompensas_Projecto(int id_Projecto) throws RemoteException, SQLException;


    /*================================Metodos_de_Accoes=========================================*/
    public int adicionarRecompensas_Projecto(int id_Projecto, String descricao_Recompensa, int tipo_Recompensa) throws RemoteException, SQLException;
    public int removerRecompensas_Projecto(int id_Projecto, int id_Recompensa) throws RemoteException, SQLException;
    public int enviarMensagens_Projecto(String assunto, String conteudo,  int id_Cliente, int id_Projecto) throws RemoteException, SQLException;    //public void doarDinheiro() throws RemoteException, SQLException;
    public void criarProjecto(String nome_Projecto, String desricao_Projecto, String data, int id_Cliente, int dinheiro_Limite ) throws RemoteException, SQLException;
    public int cancelarProjecto(int id_Projecto) throws RemoteException, SQLException;
    public int responderMensagens(String assunto, String conteudo,  int id_Cliente, int id_Projecto) throws RemoteException, SQLException;    public ArrayList<String> listar_Mensagens_Cliente(int id_Cliente) throws RemoteException, SQLException;
    public ArrayList<String> listar_Mensagens_Projecto(int id_Projecto) throws RemoteException, SQLException;

    public void registarConta(String nome_Cliente, String user_Name, String password, int saldo) throws RemoteException, SQLException;
    public void fimProjecto () throws RemoteException, SQLException;
    public int checkUserName(String nome_Cliente) throws RemoteException, SQLException;
    public int find_Cliente_ID(String userName) throws RemoteException, SQLException;
    public String ler_Mensagem_Cliente(int id_Cliente, int id_Mensagem);
    public String ler_Mensagem_Projecto(int id_Projecto, int id_Mensagem);

    /*================================Login====================================================*/
    public int login(String user_Name, String password) throws RemoteException, SQLException;

}
