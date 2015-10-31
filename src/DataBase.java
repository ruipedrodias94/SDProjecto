
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.sql.Connection;
import java.util.ArrayList;

/**
 * Created by ruype_000 on 24/10/2015.
 *
 * Classe para ligar a base de dados e para meter aqui os metodos
 */
public class DataBase extends UnicastRemoteObject implements RMI_DataBase_Interface {

    public DataBase() throws RemoteException {
        try{
            connectDataBase();
        }catch (Exception e){
            System.out.println(e.getLocalizedMessage());
        }
    }

    private static final long serialVersionUID = 1L;

    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;
    PreparedStatement preparedStatement = null;

    private int FALSE = 0;
    private int TRUE = 1;

    Informations info;

    public synchronized void connectDataBase() throws SQLException{

        info = new Informations("Server1.properties");

        System.out.println(info.getUrl());
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("[DATABASE] Nao tem a Oracle JDBC Driver instalada!");
            return;
        }

        System.out.println("[DATABASE] Oracle JDBC Driver Instalada!");
        System.out.println(info.getUrl());

        try{
            connection = DriverManager.getConnection(info.getUrl(),info.getUser(), info.getPass());
            System.out.println(info.getUrl());

        }catch (SQLException e){
            System.out.println("Falhou a fazer a connexao a base de dados!");
            System.out.println(e.getLocalizedMessage());
        }

        if (connection != null){
            System.out.println("[DATABASE] Ligada com sucesso!");
        }else{
            System.out.println("[DATABASE] Nao conseguiu ligar!");
        }
    }

    /*
    Metodo da borra s� para testar se esta merda vai realmente buscar os dados
     */

    public synchronized ArrayList<String> showCities() throws RemoteException, SQLException{

        ArrayList<String> cona = new ArrayList<>();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(" SELECT * FROM sys.sys_config");
            while (resultSet.next()){
                cona.add(resultSet.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cona;
    }

    /*
    Listar Projectos actuais
    - Ainda n�o sei o que vai retornar
    - Possivelmente um ArrayList - Por decidir no Diagrama ER
    - Falta decidir quais os argumentos tamb�m
     */

    /*
    Fazer cautela porque ainda vamos ter que adicionar muitos mais metodos!
     */

    public synchronized void listarProjectos_Actuais() throws RemoteException, SQLException{}

    /*
    Listar projectos antigos
     */

    public synchronized void listarProjectos_Antigos() throws RemoteException, SQLException{}

    /*
    Consultar detalhes de um projeto
     */

    public synchronized void listarDetalhes_Projecto() throws RemoteException, SQLException{}

    /*
    Registar Conta
    -A cada conta, dever� ser atribu�do um saldo inicial de 100euro
     */

    public synchronized void registarConta(String nome_Cliente, String user_Name, String password, int saldo) throws RemoteException, SQLException{

        try{

            preparedStatement = connection.prepareStatement("INSERT INTO mydb.cliente(nome_Cliente, user_Name, password, saldo) " +
                    "VALUES (?,?,?,?);");

            //preparedStatement.setInt(1, 1);
            preparedStatement.setString(1, nome_Cliente);
            preparedStatement.setString(2, user_Name);
            preparedStatement.setString(3, password);
            preparedStatement.setInt(4,saldo);

            preparedStatement.executeUpdate();

            System.out.println("QUELIENTE ADICIONADO! PARA BÉNS");
        }catch (SQLException e){
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    /*
    Login
     */

    public synchronized void login() throws RemoteException, SQLException{}

    /*
    Consultar saldo
     */

    public synchronized void listarSaldo_Pessoal() throws RemoteException, SQLException{}

    /*
    Consultar recompensas
     */

    public synchronized void listarRecompensas_Pessoais() throws RemoteException, SQLException{}

    /*
    Doar dinheiro ao projecto
     */

    public synchronized void doarDinheiro() throws RemoteException, SQLException{}

    /*
    Enviar mensagens para o Projecto
     */

    public synchronized void enviarMensagens_Projecto() throws RemoteException, SQLException{}

    /*
    Criar um projecto
     */

    public synchronized void criarProjecto(String nome_Projecto, String desricao_Projecto, String data, int id_Cliente ) throws RemoteException, SQLException{
        //Saldo = 0
        //Estado = 1 ---> Activo
        //Data limite ---> Ainda nao sei
        //Dinheiro angariado = 0
        //id_Cliente ---> Tambem ainda nao sei como meter
        //A data vai ter que ser do tipo 20151030  ---> 30-10-2015 Passamos como string, e no menu pede-se o dia o mes e o ano, tornando dempois numa string

        try{
            preparedStatement = connection.prepareStatement("INSERT INTO mydb.projecto (id_Projecto, nome_Projecto, descricao_Projecto, estado, data_Limite, dinheiro_Angariado) " +
                    "+ VALUES (?,?,?,?,?);");

            preparedStatement.setString(1,nome_Projecto);
            preparedStatement.setString(2,desricao_Projecto);
            //Estado a 1 ---> Activo
            preparedStatement.setInt(3, 1);
            preparedStatement.setString(4,data);
            preparedStatement.setInt(5,id_Cliente);

        }catch (SQLException e){
            System.out.println(e.getLocalizedMessage());
        }
    }

    /*
    Adicionar ou remover recompensas ao projecto
     */

    public synchronized void adicionarRecompensas_Projecto() throws RemoteException, SQLException{}

    public synchronized void removerRecompensas_Projecto() throws RemoteException, SQLException{}

    /*
    Cancelar um projecto
     */

    public synchronized void cancelarProjecto() throws RemoteException, SQLException{}

    /*
    Responder a mensagens de apoiantes
     */

    public synchronized void responderMensagens() throws RemoteException, SQLException{}


    /*
    Fim de um projecto
     */

    public synchronized void fimProjecto() throws RemoteException, SQLException{}



}
