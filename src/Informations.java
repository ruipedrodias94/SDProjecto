import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RMISecurityManager;
import java.util.Properties;

/**
 * Created by ruype_000 on 24/10/2015.
 *
 * Classe que vai ler do ficheiro e mete tudo em variaveis!
 */
public class Informations {

    //RMI
    private int rmiRegistry;
    private String rmiRebind;
    private String rmiIP;

    //Server
    private int serverPort;
    private int serverPort2;
    private String hostPrimario;
    private String hostSecundario;

    //DataBase
    private String url;
    private String user;
    private String pass;
    private String SID;
    private String hostDataBase;
    private String portDataBase;

    public Informations(){

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

        this.setRmiRebind(props.getProperty("rmiRebind"));
        this.setRmiRegistry(Integer.parseInt(props.getProperty("rmiRegistry")));
        this.setServerPort(Integer.parseInt(props.getProperty("portPrimario")));
        this.setServerPort2(Integer.parseInt(props.getProperty("portSecundario")));
        this.setHostPrimario(props.getProperty("hostPrimario"));
        this.setHostPrimario(props.getProperty("hostSecundario"));
        this.setUser( props.getProperty("user"));
        this.setPass( props.getProperty("pass"));
        this.setHostDataBase(props.getProperty("host"));
        this.setPortDataBase(props.getProperty("port"));
        this.setSID( props.getProperty("SID"));
        this.setRmiIP(props.getProperty("rmiIp"));
        this.setUrl("jdbc:oracle:thin:@"+this.getHostDataBase()+":"+this.getPortDataBase()+":"+this.getSID());
    }

    public int getRmiRegistry() {
        return rmiRegistry;
    }

    public void setRmiRegistry(int rmiRegistry) {
        this.rmiRegistry = rmiRegistry;
    }

    public String getRmiRebind() {
        return rmiRebind;
    }

    public void setRmiRebind(String rmiRebind) {
        this.rmiRebind = rmiRebind;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public int getServerPort2() {
        return serverPort2;
    }

    public void setServerPort2(int serverPort2) {
        this.serverPort2 = serverPort2;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getSID() {
        return SID;
    }

    public void setSID(String SID) {
        this.SID = SID;
    }

    public String getHostDataBase() {
        return hostDataBase;
    }

    public void setHostDataBase(String host) {
        this.hostDataBase = host;
    }

    public String getPortDataBase() {
        return portDataBase;
    }

    public void setPortDataBase(String port) {
        this.portDataBase = port;
    }

    public String getHostPrimario() {
        return hostPrimario;
    }

    public void setHostPrimario(String hostPrimario) {
        this.hostPrimario = hostPrimario;
    }

    public String getHostSecundario() {
        return hostSecundario;
    }

    public void setHostSecundario(String hostSecundario) {
        this.hostSecundario = hostSecundario;
    }

    public String getRmiIP() {
        return rmiIP;
    }

    public void setRmiIP(String rmiIP) {
        this.rmiIP = rmiIP;
    }
}