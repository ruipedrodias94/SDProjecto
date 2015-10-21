import java.net.*;
import java.io.*;
import java.util.*;

// Tem que que ler comandos do utilizador e imprimir a resposta
// Thread a ler pedidos - feito
// Imprimir resultados - claro que não, né?

public class TCPClient {
	public static void main(String args[]) {

		Socket s = null;

		/* Java properties */
		Properties props = new Properties();
		InputStream input1 = null;

		try {
			// Ler do ficheiro de configuracoes!
			input1 = new FileInputStream("clientConf.properties");
			props.load(input1);

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		try {
			// Liga-se ao host através de socket
			String clientHost = (String) props.getProperty("hostPrimario");
			int clientePort = Integer.parseInt(props.getProperty("portPrimario"));
			s = new Socket(clientHost, clientePort);
			new leSkt(s);

			//System.out.println("SOCKET=" + s);
			System.out.println("Socket Connectado! A escuta!");
			
			// 2o passo
			//Mudar isto porque depois vamos trabalhar com mensagens!
			DataInputStream in = new DataInputStream(s.getInputStream());
			DataOutputStream out = new DataOutputStream(s.getOutputStream());

			// ler texto do utilizador para usar para chamar mensagens
			String texto = "";
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
				out.writeUTF(texto);

			}

		} catch (UnknownHostException e) {
			System.out.println("Sock:" + e.getMessage());
		} catch (EOFException e) {
			System.out.println("EOF:" + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO:" + e.getMessage());
		} finally {
			if (s != null)
				try {
					s.close();
				} catch (IOException e) {
					System.out.println("close:" + e.getMessage());
				}
		}
	}
}

// Thread para estar constantemente a ler do socket - Para imprimir resultados
class leSkt extends Thread {
	Socket serverSocket;
	DataInputStream in;

	public leSkt(Socket srv) {
		try {
			serverSocket = srv;
			in = new DataInputStream(serverSocket.getInputStream());
			this.start();
		} catch (IOException e) {
			System.out.println("Connection:" + e.getMessage());
		}
	}

	public void run() {
		try {
			while (true) {
				String data = in.readUTF();
				System.out.println(data);

			}
		} catch (EOFException e) {
			System.out.println("EOF:" + e);
		} catch (IOException e) {
			System.out.println("IO:" + e);
		}
	}
}
