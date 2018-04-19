import java.io.*;
import java.sql.*;
import java.rmi.*;
import java.rmi.Naming.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.util.*;

interface DBInterface extends Remote {
	public String input(String n1, String n2) throws RemoteException;
}

public class Server extends UnicastRemoteObject implements DBInterface {
	String n3;

	public Server() throws RemoteException {
		try {
			System.out.println("Init Server\n");
		}
		catch(Exception e) {
			System.out.println("Constructor Exception :" + e);
		}
	}

	public String input(String n1, String n2) throws RemoteException {
		try {
			n3 = n1.concat(n2);
		}
		catch(Exception e) {
			System.out.println("input Exception :" + e);
		}
		return n3;
	}

	public static void main(String[] args) {
		try {
			Server s = new Server();
			java.rmi.registry.LocateRegistry.createRegistry(1030).rebind("DBserv", s);
		}
		catch(Exception e) {
			System.out.println("Main Exception :" + e);
		}
	}
}