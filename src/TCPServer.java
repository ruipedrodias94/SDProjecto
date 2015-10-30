
// TCPServer2.java: Multithreaded server
import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;

import java.net.*;
import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.*;

//Assim que se liga tenta fazer ping

//Vai servir tamb�m de Cliente RMI

public class TCPServer {
	public static void main(String args[]) throws SQLException {
		int numero = 0;
		boolean isPrimary = false;


		Informations info = new Informations();

		//Ligar Server

		try {

			InetAddress host = InetAddress.getByName(info.getHostPrimario());
			ServerSocket listenSocket = new ServerSocket(info.getServerPort(),50,host);
			System.out.println("A Escuta no Porto : " + info.getServerPort()+" do host: "+info.getHostPrimario());
			isPrimary = true;
			System.out.println("Servidor Primario a escuta!");
			Ping p = new Ping(isPrimary);
			p.ping();


			//Tentativa de ligar o servidor RMI!

			RMI_DataBase_Interface clienteRMI=null;

			try
			{

				System.getProperties().put("java.security.policy", "security.policy");
				System.setSecurityManager(new RMISecurityManager());
				clienteRMI = (RMI_DataBase_Interface) LocateRegistry.getRegistry(info.getRmiIP(),info.getRmiRegistry()).lookup(info.getRmiRebind());
				System.out.println("Cliente RMI ligado!");



			} catch (Exception e){

				System.out.println("Error establishing connection with RMI.\nPlease try again later");
				System.exit(1);

			}

			while (true) {
				Socket clientSocket = listenSocket.accept(); // BLOQUEANTE
				new Connection(clientSocket, numero, clienteRMI);

				numero++;
			}

		} catch (IOException e) {
			System.out.println("Impossivel ligar no porto primario:" + e.getMessage());
			System.out.println("Tentativa de ligar como porto secundario...");

			//não conseguiu ligar como primario vai tentar ligar como secundario
			try{




				InetAddress host2 = InetAddress.getByName(info.getHostSecundario());
				int port2 = Integer.parseInt(String.valueOf(info.getServerPort2()));
				ServerSocket listenSocket2 = new ServerSocket(port2,50,host2);
				System.out.println("A Escuta no Porto : " + port2+" do host: "+info.getHostSecundario());
				isPrimary = false;
				System.out.println("Servidor Secundário à escuta!");
				Ping p2 = new Ping(isPrimary);
				p2.ping();

				RMI_DataBase_Interface clienteRMI=null;

				try
				{

					System.getProperties().put("java.security.policy", "security.policy");
					System.setSecurityManager(new RMISecurityManager());
					clienteRMI = (RMI_DataBase_Interface) LocateRegistry.getRegistry(info.getRmiIP(),info.getRmiRegistry()).lookup(info.getRmiRebind());
					System.out.println("Cliente RMI ligado!");



				} catch (Exception e2){

					System.out.println("Error establishing connection with RMI.\nPlease try again later");
					System.exit(1);

				}

				while (true) {

					Socket clientSocket2 = listenSocket2.accept(); // BLOQUEANTE
					new Connection(clientSocket2, numero, clienteRMI);

					numero++;
				}

			}
			catch(IOException e2)
			{
				System.out.println("Impossível Ligar como secundario\nDemasiados servidores a correr."+e2.getMessage());
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
	RMI_DataBase_Interface servidor_rmi;
	ArrayList<String> useres = new ArrayList<>();

	public Connection(Socket aClientSocket, int numero, RMI_DataBase_Interface servidor_rmi) {
		thread_number = numero;
		sc.addClient(aClientSocket);
		this.servidor_rmi = servidor_rmi;
		try {
			clientSocket = aClientSocket;
			in = new DataInputStream(clientSocket.getInputStream());
			out = new DataOutputStream(aClientSocket.getOutputStream());
			out.writeUTF("Bem vindo\n");
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

				/*int num = servidor_rmi.getUsers();

				resposta = String.valueOf(num);*/
				useres = servidor_rmi.showCities();

				for (int i = 0; i < useres.size() ; i++) {
					resposta = useres.get(i);
					sc.send_clients(resposta, thread_number);
				}



			}
		} catch (EOFException e) {
			System.out.println("EOF:" + e);
		} catch (IOException e) {
			System.out.println("IO:" + e);
		} catch (SQLException e) {
			e.printStackTrace();
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

				/*Para enviar para o mesmo queliente*/

				if(i==clintNmr){
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

//thread que vai estar continuamente a fazer ping entre servidores
class Ping extends Thread{

	Informations info = new Informations();
	boolean isPrimary;



	Ping(boolean isPrimary)
	{
		this.isPrimary = isPrimary;

	}

	public void isPrincipal()
	{
		if(this.isPrimary==true)
		{
			System.out.println("[Este servidor é agora principal]");
		}
		else
		{
			System.out.println("[Este servidor é agora Secundário]");
		}
	}

	public void ping(){

		System.out.println("Iniciando ping...");
		this.start();
	}


	public void run()
	{
		while(true){
		//Se o servidor for primario
		if(this.isPrimary == true)
		{

			DatagramSocket aSocket = null;
			String s;
			try{
				isPrincipal();
				aSocket = new DatagramSocket(this.info.getUdpPingPort());
				System.out.println("Esperando ping requests no porto: " + this.info.getUdpPingPort());
				while(true){
					byte[] buffer = new byte[1000];
					DatagramPacket request = new DatagramPacket(buffer, buffer.length);
					aSocket.receive(request);
					s=new String(request.getData(), 0, request.getLength());
					//System.out.println("Server Recebeu: " + s);
					aSocket.setSoTimeout(1000);
					DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(), request.getAddress(), request.getPort());
					aSocket.send(reply);
				}}
			catch (SocketException e){System.out.println("Socket: " + e.getMessage());
			}catch (IOException e) {
                aSocket.close();
                System.out.println("IO: " + e.getMessage());
                System.out.println("Servidor Secundário em baixo... Em casa de falha não há backup server.");
            }
		}
		else
		{
			//Ping caso secundário
			DatagramSocket aSocket2 = null;
			String texto = "HEARTBEAT";
			isPrincipal();

			try {
				aSocket2 = new DatagramSocket();
				System.out.println("Mandando Ping resquests para o host: "+info.getHostPrimario()+" no porto: "+info.getUdpPingPort());
				while(true)
				{
					byte [] m = texto.getBytes();

					InetAddress aHost = InetAddress.getByName(this.info.getHostPrimario());
					int serverPort = this.info.getUdpPingPort();
					DatagramPacket request = new DatagramPacket(m,m.length,aHost,serverPort);
					aSocket2.send(request);
					byte[] buffer = new byte[1000];
					DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
					aSocket2.setSoTimeout(1000);
					aSocket2.receive(reply);
					//System.out.println("Recebeu: " + new String(reply.getData(), 0, reply.getLength()));
				}
			}
			catch (SocketException e){System.out.println("Socket: " + e.getMessage());
			}catch (IOException e){
                System.out.println("IO: " + e.getMessage());
				System.out.println("Servidor Principal em baixo. Tentativa de Ligação como principal.");
				this.isPrimary = true;
                aSocket2.close();
				try {
					this.info.switchHosts();
				} catch (IOException e1) {
					e1.printStackTrace();
				}


			}
		}
	}}

}