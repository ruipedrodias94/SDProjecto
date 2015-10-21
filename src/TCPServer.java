
// TCPServer2.java: Multithreaded server
import java.net.*;
import java.io.*;
import java.util.*;

//Assim que se liga tenta fazer ping	

public class TCPServer {
	public static void main(String args[]) {
		int numero = 0;
		boolean isPrimary = false;
		Properties props = new Properties();
		InputStream inputConfigs = null;
		
		//Ler do Ficheiro das configs
		try {
			inputConfigs = new FileInputStream("clientConf.properties");
			props.load(inputConfigs);
				
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getLocalizedMessage());
		}
		
		
		//Ligar Server
		try {
			int serverPort = Integer.parseInt(props.getProperty("portPrimario"));
			
			System.out.println("A Escuta no Porto : " + serverPort);
			ServerSocket listenSocket = new ServerSocket(serverPort);
			isPrimary = true;
			System.out.println("Servidor Primário à escuta!");
			
			while (true) {
				Socket clientSocket = listenSocket.accept(); // BLOQUEANTE
				new Connection(clientSocket, numero);
				numero++;
			}
		
		
		} catch (IOException e) {
			System.out.println("Impossivel ligar no porto primario:" + e.getMessage());
			System.out.println("Tentativa de ligar como porto secundario...");
		
		//não conseguiu ligar como primario vai tentar ligar como secundario	
		try{

			int serverPort2 = Integer.parseInt(props.getProperty("portSecundario"));
			
			System.out.println("A Escuta no Porto : " + serverPort2);
			ServerSocket listenSocket2 = new ServerSocket(serverPort2);
			isPrimary = false;
			System.out.println("Servidor Secundário à escuta!");
			}
			catch(IOException e2)
			{
				System.out.println("Impossível Ligar como secundario:"+e2.getMessage());
			}
			catch(NumberFormatException ex)
			{
				System.out.println("Erro a ler configuraçoes:"+ex.getMessage());
			}

		}
	}
}

// = Thread para tratar de cada canal de comunicacao com um cliente
class Connection extends Thread {
	DataInputStream in;
	DataOutputStream out;
	Socket clientSocket;
	int thread_number;
	Shared_Clients sc = new Shared_Clients();

	public Connection(Socket aClientSocket, int numero) {
		thread_number = numero;
		sc.addClient(aClientSocket);
		try {
			clientSocket = aClientSocket;
			in = new DataInputStream(clientSocket.getInputStream());
			out = new DataOutputStream(aClientSocket.getOutputStream());
			out.writeUTF("Bem vinda Sua Puta Fraca");
			this.start();
		} catch (IOException e) {
			System.out.println("Connection:" + e.getMessage());
		}
	}

	// =============================
	public void run() {
		String resposta;
		try {
			while (true) {
				// an echo server
				String data = in.readUTF();
				System.out.println("T[" + thread_number + "] Recebeu: " + data);
				resposta = data;
				sc.send_clients(resposta, thread_number);

			}
		} catch (EOFException e) {
			System.out.println("EOF:" + e);
		} catch (IOException e) {
			System.out.println("IO:" + e);
		}
	}
}

class Shared_Clients {
	public static ArrayList<Socket> clientes = new ArrayList<Socket>();
	DataOutputStream out;

	synchronized void addClient(Socket c) {
		clientes.add(c);
		System.out.println("Cliente adicionado");
	}

	synchronized void send_clients(String msg, int clintNmr) {
		

		int i;
		for (i = 0; i < clientes.size(); i++) {

			try {

				if (i == 0) {
					msg = "de cliente " + clintNmr + " : " + msg;
				}
				if(i!=clintNmr){
				out = new DataOutputStream(clientes.get(i).getOutputStream());
				out.writeUTF(msg);
				System.out.println("Enviado de cliente " + clintNmr + " para " + i);
			}

			} catch (IOException e) {
				System.out.println("IO:" + e);
			}
		}
	}
}

class Ping extends Thread
{

}
