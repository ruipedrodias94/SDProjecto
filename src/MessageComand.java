import java.io.Serializable;

/**
 * Created by ruype_000 on 20/10/2015.
 */
public class MessageComand implements Serializable {

    private String comando;
    private String argumentos;


    public String getComando() {
        return comando;
    }

    public void setComando(String comando) {
        this.comando = comando;
    }

    public String getArgumentos() {
        return argumentos;
    }

    public void setArgumentos(String argumentos) {
        this.argumentos = argumentos;
    }
}
