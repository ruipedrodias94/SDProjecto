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

    //Server
    private int serverPort;
    private int serverPort2;

    //DataBase
    private String url;
    private String user;
    private String pass;
    private String SID;
    private String host;
    private String port;

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
        this.setServerPort(Integer.parseInt(props.getProperty("portSecundario")));
        this.setUser( props.getProperty("user"));
        this.setPass( props.getProperty("pass"));
        this.setHost( props.getProperty("host"));
        this.setPort( props.getProperty("port"));
        this.setSID( props.getProperty("SID"));
        this.setUrl("jdbc:oracle:thin:@"+this.getHost()+":"+this.getPort()+":"+this.getSID());
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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
