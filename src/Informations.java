import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    private int tentativas;

    //Server
    private int thisPort;
    private int otherPort;
    private String thisHost;
    private String otherHost;
    private int sendUdpPingPort;
    private int receiveUdpPingPort;

    //DataBase
    private String url;
    private String user;
    private String pass;
    private String SID;
    private String hostDataBase;
    private String portDataBase;

    public Informations(String filename){

        Properties props = new Properties();
        FileInputStream file = null;
        try{
            file = new FileInputStream(filename);
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
        this.setThisPort(Integer.parseInt(props.getProperty("thisPort")));
        this.setOtherPort(Integer.parseInt(props.getProperty("otherPort")));
        this.setThisHost(props.getProperty("thisHost"));
        this.setOtherHost(props.getProperty("otherHost"));
        this.setUser(props.getProperty("user"));
        this.setPass(props.getProperty("pass"));
        this.setHostDataBase(props.getProperty("host"));
        this.setPortDataBase(props.getProperty("port"));
        this.setSID(props.getProperty("SID"));
        this.setRmiIP(props.getProperty("rmiIp"));
        this.setUrl("jdbc:mysql://" + getHostDataBase() + ":" + getPortDataBase() + "/?user=" + getUser());
        this.setTentativas(Integer.parseInt(props.getProperty("tentativas")));
        this.setSendUdpPingPort(Integer.parseInt(props.getProperty("sendUdpPingPort")));
        this.setReceiveUdpPingPort(Integer.parseInt(props.getProperty("receiveUdpPingPort")));

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

    public int getThisPort() {
        return this.thisPort;
    }

    public void setThisPort(int serverPort) {
        this.thisPort = serverPort;
    }

    public int getOtherPort() {
        return this.otherPort;
    }

    public void setOtherPort(int serverPort2) {
        this.otherPort = serverPort2;
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

    public String getThisHost() {
        return thisHost;
    }

    public void setThisHost(String hostPrimario) {
        this.thisHost = hostPrimario;
    }

    public String getOtherHost() {
        return this.otherHost;
    }

    public void setOtherHost(String hostSecundario) {
        this.otherHost = hostSecundario;
    }

    public String getRmiIP() {
        return rmiIP;
    }

    public void setRmiIP(String rmiIP) {
        this.rmiIP = rmiIP;
    }

    public int getTentativas() {
        return tentativas;
    }

    public void setTentativas(int tentativas) {
        this.tentativas = tentativas;
    }

    public int getSendUdpPingPort() {return this.sendUdpPingPort;}

    public void setSendUdpPingPort(int udpPingPort) {this.sendUdpPingPort = udpPingPort;}

    public int getReceiveUdpPingPort() {return receiveUdpPingPort;}

    public void setReceiveUdpPingPort(int receiveUdpPingPort) {this.receiveUdpPingPort = receiveUdpPingPort;}
}
