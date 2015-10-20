
// TCPServer2.java: Multithreaded server
import java.net.*;
import java.io.*;
import java.util.*;

public class TCPServer {
	public static void main(String args[]) {
		int numero = 0;
		
		Properties props = new Properties();
		InputStream input1 = null;
		
		//Ler do Ficheiro das configs
		try {
			input1 = new FileInputStream("clientConf.properties");
			props.load(input1);
				
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getLocalizedMessage());
		}
		
		
		//Ligar Server
		try {
			
			int serverPort = Integer.parseInt(props.getProperty("port"));
			
			System.out.println("A Escuta no Porto : " + serverPort);
			ServerSocket listenSocket = new ServerSocket(serverPort);
			
			System.out.println("Servidor a escuta!");
			
			while (true) {
				Socket clientSocket = listenSocket.accept(); // BLOQUEANTE
				new Connection(clientSocket, numero);
				numero++;
			}
		} catch (IOException e) {
			System.out.println("Listen:" + e.getMessage());
		}
	}
}

// = Thread para tratar de cada canal de comunicacao com um cliente
class Connection extends Thread {
	DataInputStream in;
	Socket clientSocket;
	int thread_number;
	Shared_Clients sc = new Shared_Clients();

	public Connection(Socket aClientSocket, int numero) {
		thread_number = numero;
		sc.addClient(aClientSocket);
		try {
			clientSocket = aClientSocket;
			in = new DataInputStream(clientSocket.getInputStream());
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
		try {
			if (msg.equals("_ALIVE_")) {
				out = new DataOutputStream(clientes.get(clintNmr).getOutputStream());
				out.writeUTF("_IMALIVE_");
				System.out.println("Aqui");
			}
		} catch (IOException e) {
			System.out.println("IO:" + e);
		}

		int i;
		for (i = 0; i < clientes.size(); i++) {

			try {

				if (i == 0) {
					msg = "de cliente " + clintNmr + " : " + msg;
				}
				out = new DataOutputStream(clientes.get(i).getOutputStream());
				out.writeUTF(msg);
				System.out.println("Enviado de cliente " + clintNmr + " para " + i);

			} catch (IOException e) {
				System.out.println("IO:" + e);
			}
		}
	}
}
