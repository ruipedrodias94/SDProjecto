
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

		//Ligar Server
		Ping p = new Ping();
		p.startPing(args[0]);


	}
}


