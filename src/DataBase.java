
import com.sun.xml.internal.bind.v2.TODO;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.sql.Date;

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
            resultSet = statement.executeQuery("SELECT * FROM sys.sys_config");
            while (resultSet.next()){
                cona.add(resultSet.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cona;
    }


    /*
    Fazer cautela porque ainda vamos ter que adicionar muitos mais metodos!
     */

    //FUNCIONAL
    public synchronized ArrayList<String> listarProjectos_Actuais() throws RemoteException, SQLException{
        ArrayList<String> projectos_Actuais = new ArrayList<>();
        String nome_Projecto, data_Final, nome_Cliente, estado = "";
        String string_Final = "";
        int id_Projecto = 0;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT nome_Projecto, data_Limite, nome_Cliente, id_Projecto FROM mydb.projecto, mydb.cliente " +
                    "WHERE projecto.estado = 1 AND projecto.id_Cliente = cliente.id_Cliente;");
            while (resultSet.next()){
                nome_Projecto = resultSet.getString(1);
                data_Final = resultSet.getString(2);
                nome_Cliente = resultSet.getString(3);
                id_Projecto = resultSet.getInt(4);
                estado = "ACTIVO";
                string_Final = "NOME DO PROJECTO: " + nome_Projecto +"\nID DO PROJECTO: "+ String.valueOf(id_Projecto) +"\nDATA LIMITE: " + data_Final + "\nNOME DO ADMIN: "
                        + nome_Cliente + "\nESTADO: " + estado + "\n====================================================";

                projectos_Actuais.add(string_Final);
            }
        }catch (SQLException e){
            System.out.println(e.getLocalizedMessage());
        }

        return projectos_Actuais;
    }

    /*
    Listar projectos antigos
     */

    //FUNCIONAL
    public synchronized ArrayList<String> listarProjectos_Antigos() throws RemoteException, SQLException{
        ArrayList<String> projectos_Actuais = new ArrayList<>();
        String nome_Projecto, data_Final, nome_Cliente, estado = "";
        String string_Final = "";
        int id_Projecto = 0;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT nome_Projecto, data_Limite, nome_Cliente, id_Projecto FROM mydb.projecto, mydb.cliente " +
                    "WHERE projecto.estado = 0 AND projecto.id_Cliente = cliente.id_Cliente;");
            while (resultSet.next()){
                nome_Projecto = resultSet.getString(1);
                data_Final = resultSet.getString(2);
                nome_Cliente = resultSet.getString(3);
                id_Projecto = resultSet.getInt(4);
                estado = "ACTIVO";
                string_Final = "NOME DO PROJECTO: " + nome_Projecto +"\nID DO PROJECTO: "+ String.valueOf(id_Projecto) +"\nDATA LIMITE: " + data_Final + "\nNOME DO ADMIN: "
                        + nome_Cliente + "\nESTADO: " + estado + "\n====================================================";

                projectos_Actuais.add(string_Final);
            }
        }catch (SQLException e){
            System.out.println(e.getLocalizedMessage());
        }

        return projectos_Actuais;
    }

    /*
    Consultar detalhes de um projeto
     */

    //FUNCIONAL
    public synchronized String listarDetalhes_Projecto(int id_Projecto) throws RemoteException, SQLException{
        String string_Final = "", nome_Projecto = "", descricao_Projecto= "";
        int dinheiro_Angariado = 0, dinheiro_Limite = 0;
        Date data_Limite = null;

        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT nome_Projecto, descricao_Projecto, data_Limite, dinheiro_Angariado, dinheiro_Limite" +
                    " FROM mydb.projecto WHERE id_Projecto= " + id_Projecto + ";");
            if(resultSet.next()){
                nome_Projecto = resultSet.getString(1);
                descricao_Projecto = resultSet.getString(2);
                data_Limite = resultSet.getDate(3);
                dinheiro_Angariado = resultSet.getInt(4);
                dinheiro_Limite = resultSet.getInt(5);

                string_Final = "DETALHES DO PROJECTO: \n"
                        + "NOME: " + nome_Projecto + "\n"
                        + "DESCRICAO: " + descricao_Projecto + "\n"
                        + "DATA LIMITE: " + String.valueOf(data_Limite) + "\n"
                        + "DINHEIRO ANGARIADO: " + String.valueOf(dinheiro_Angariado) + "\n"
                        + "DINHEIRO NECESSARIO: " + String.valueOf(dinheiro_Limite)
                        + "\n====================================================";

            }
        }catch (SQLException e){
            System.out.println(e.getLocalizedMessage());
        }

        return string_Final;

    }

    /*
    Registar Conta
    -A cada conta, dever� ser atribu�do um saldo inicial de 100euro
     */

    //FUNCIONAL
    public synchronized void registarConta(String nome_Cliente, String user_Name, String password, int saldo) throws RemoteException, SQLException{

        try{

            preparedStatement = connection.prepareStatement("INSERT INTO mydb.cliente(nome_Cliente, user_Name, password, saldo)" +
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

    //FUNCIONAL
    public synchronized String listarSaldo_Pessoal(int id_Cliente) throws RemoteException, SQLException{
        String saldo = "", nome = "";
        int saldo_Cliente = 0;

        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT nome_Cliente, saldo FROM mydb.cliente WHERE id_Cliente =" + id_Cliente+";");

            while (resultSet.next()){
                nome = resultSet.getString(1);
                saldo_Cliente = resultSet.getInt(2);
                saldo = "O SALDO DO CLIENTE " + nome + " E: " + String.valueOf(saldo_Cliente);
            }
        }catch (SQLException e){
            System.out.println(e.getLocalizedMessage());
        }
        return saldo;

    }

    /*
    Consultar recompensas
     */

    //FUNCIONAL
    public synchronized ArrayList<String> listarRecompensas_Projecto(int id_Projecto) throws RemoteException, SQLException{

        ArrayList<String> recompensas = new ArrayList<>();
        int tipo_Recompensa = 0;
        int id_Recompensa = 0;
        String descricao_Recompensa = "";
        String string_Final = "";

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select tipo_Recompensa, descricao_Recompensa, id_Recompensa from mydb.recompensa, mydb.projecto " +
                    "where Projecto_id_Projecto ="+ id_Projecto + " and projecto.id_Projecto = recompensa.Projecto_id_Projecto;");

            while (resultSet.next()){
                tipo_Recompensa = resultSet.getInt(1);
                descricao_Recompensa = resultSet.getString(2);
                id_Recompensa = resultSet.getInt(3);

                string_Final = "DESCRICAO DA RECOMPENSA: " + descricao_Recompensa
                        + "\nTIPO DA RECOMPENSA: " + String.valueOf(tipo_Recompensa)
                        + "\nID DA RECOMPENSA: " + String.valueOf(id_Recompensa)
                        + "\n====================================================";

                recompensas.add(string_Final);
            }
        }catch (SQLException e){
            System.out.println(e.getLocalizedMessage());
        }

        return recompensas;

    }

    /*
    Doar dinheiro ao projecto
     */

    /*public synchronized void doarDinheiro(int quantidade, int id_Projecto, String id_User) throws RemoteException, SQLException{



        if (quantidade < 10){

        }
    }*/

    /*
    Enviar mensagens para o Projecto
     */

    public synchronized void enviarMensagens_Projecto() throws RemoteException, SQLException{}

    /*
    Criar um projecto
     */

    //FUNCIONAL
    public synchronized void criarProjecto(String nome_Projecto, String desricao_Projecto, String data, int id_Cliente, int dinheiro_Limite ) throws RemoteException, SQLException{
        //Saldo = 0
        //Estado = 1 ---> Activo
        //Data limite ---> Ainda nao sei
        //Dinheiro angariado = 0
        //id_Cliente ---> Tambem ainda nao sei como meter
        //A data vai ter que ser do tipo 20151030  ---> 30-10-2015 Passamos como string, e no menu pede-se o dia o mes e o ano, tornando dempois numa string

        try{
            preparedStatement = connection.prepareStatement("INSERT INTO mydb.projecto (nome_Projecto, descricao_Projecto, estado, data_Limite," +
                    " dinheiro_Angariado, id_Cliente, dinheiro_Limite) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?);");

            preparedStatement.setString(1,nome_Projecto);
            preparedStatement.setString(2,desricao_Projecto);
            preparedStatement.setInt(3, 1);
            preparedStatement.setDate(4, Date.valueOf(data));
            preparedStatement.setInt(5, 0);
            preparedStatement.setInt(6,id_Cliente);
            preparedStatement.setInt(7, dinheiro_Limite);

            preparedStatement.executeUpdate();

            System.out.println("PROJECTO CRIADO COM SUSEXO");

        }catch (SQLException e){
            System.out.println(e.getLocalizedMessage());
        }
    }

    /*
    Adicionar ou remover recompensas ao projecto
     */

    //FUNCIONAL
    public synchronized int adicionarRecompensas_Projecto(int id_Projecto, String descricao_Recompensa, int tipo_Recompensa) throws RemoteException, SQLException{

        try {
            preparedStatement = connection.prepareStatement("INSERT INTO mydb.recompensa (tipo_Recompensa, descricao_Recompensa, Projecto_id_Projecto) " +
                    "VALUES (?, ?, ?);");

            preparedStatement.setInt(1, tipo_Recompensa);
            preparedStatement.setString(2, descricao_Recompensa);
            preparedStatement.setInt(3, id_Projecto);

            preparedStatement.executeUpdate();

        }catch (SQLException e){
            System.out.println(e.getLocalizedMessage());
            return FALSE;
        }

        return TRUE;
    }

    public synchronized int removerRecompensas_Projecto(int id_Projecto, int id_Recompensa) throws RemoteException, SQLException{
        try{

            preparedStatement = connection.prepareStatement("DELETE FROM mydb.recompensa " +
                    "WHERE id_Recompensa= '"+id_Recompensa+"' and Projecto_id_Projecto='"+id_Projecto+"';");

            preparedStatement.executeUpdate();

        }catch (SQLException e){
            System.out.println(e.getLocalizedMessage());
            return FALSE;
        }
        return TRUE;
    }

    /*
    Cancelar um projecto
     */

    //FUNCIONAL
    public synchronized int cancelarProjecto(int id_Projecto) throws RemoteException, SQLException{
        try {
            statement = connection.createStatement();
            //resultSet = statement.executeUpdate("UPDATE mydb.projecto SET estado = 0 where id_Projecto = "+id_Projecto+";");
            statement.executeUpdate("UPDATE mydb.projecto SET estado = 0 where id_Projecto = " + id_Projecto+";");


        }catch (SQLException e){
            System.out.println(e.getLocalizedMessage());
            return FALSE;
        }
        System.out.println("PROJECTO CANCELADO. PODE SER CONSULTADO NOS PROJECTOS ANTIGOS!");
        return TRUE;
    }

    /*
    Responder a mensagens de apoiantes
     */

    public synchronized void responderMensagens() throws RemoteException, SQLException{}


    /*
    Fim de um projecto
     */

    public synchronized void fimProjecto() throws RemoteException, SQLException{}


    //FUNCIONAL
    public synchronized int checkUser(String nome_Cliente) throws RemoteException, SQLException{
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM mydb.cliente WHERE user_Name= '"+nome_Cliente+"';");
            while (resultSet.next()){
                return TRUE;
            }
        }catch (SQLException e){
            System.out.println(e.getLocalizedMessage());

        }
        return FALSE;
    }

    //FUNCIONAL
    public synchronized int find_Cliente_ID(String userName) throws RemoteException, SQLException{
        int id_Cliente = 0;
        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT id_Cliente FROM mydb.cliente WHERE user_Name = '" +userName+"';");

            while (resultSet.next()) {
                id_Cliente = resultSet.getInt(1);
            }


        }catch (SQLException e){
            System.out.println(e.getLocalizedMessage());
        }
        return id_Cliente;
    }


}
