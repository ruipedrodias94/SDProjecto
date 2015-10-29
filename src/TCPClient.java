import java.net.*;
import java.io.*;
import java.util.*;

// Tem que que ler comandos do utilizador e imprimir a resposta
// Thread a ler pedidos - feito
// Imprimir resultados - claro que não, né?

public class TCPClient {
	public static void main(String args[]) {

		Socket s = null;

		Informations info = new Informations();




			leSkt l = new leSkt();


	}
}

// Thread para estar constantemente a ler do socket - Para imprimir resultados
class leSkt extends Thread {
	Socket serverSocket;
	DataInputStream in;
    DataOutputStream out;
    Informations info = new Informations();

	public leSkt() {


        this.start();
    }


	public void run() {
        while(true){
		try {
            serverSocket = new Socket(this.info.getHostPrimario(), this.info.getServerPort());
            System.out.println("Cliente ligado ao server no host: " + info.getHostPrimario() + " no porto: " + info.getServerPort());
            in = new DataInputStream(serverSocket.getInputStream());
            EscreveSck es = new EscreveSck(serverSocket);

			while (true) {
				String data = in.readUTF();
				System.out.println(data);

			}
		} catch (EOFException e) {
			System.out.println("EOF:" + e);
		} catch (IOException e) {
            this.info = new Informations();
            try {
                sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            System.out.print("Servidor Primário em baixo.\nTentativa de ligação ao servidor secundário.");
			System.out.println("IO:" + e);


        }
	}}
}

class EscreveSck extends Thread
{
    Socket s;
    DataInputStream in;
    DataOutputStream out;
    Informations info = new Informations();


    public  EscreveSck(Socket s)
    {
        this.s = s;
       this.start();
    }

    public void run()
    {
        String texto = "";
        try {
            this.out = new DataOutputStream(s.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        System.out.println("Introduza texto:");

        // 3o passo
        while (true) {
            // READ STRING FROM KEYBOARD
            try {
                texto = reader.readLine();

            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
            // WRITE INTO THE SOCKET
            try {
                out = new DataOutputStream(s.getOutputStream());
                out.writeUTF(texto);
                out.flush();
            } catch (IOException e) {
                try {
                    out = new DataOutputStream(s.getOutputStream());
                    out.flush();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }

}
