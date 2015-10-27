
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

    private int FALSE = 0;
    private int TRUE = 1;

    Informations info;

    public synchronized void connectDataBase() throws SQLException{

        info = new Informations();

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
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

    public synchronized ArrayList<String> showCities() throws RemoteException, SQLException{

        ArrayList<String> cona = new ArrayList<>();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(" SELECT * FROM world.city;");
            while (resultSet.next()){
                cona.add(resultSet.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cona;

    }


}
