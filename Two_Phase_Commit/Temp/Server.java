import java.io.*;
import java.net.*;
import java.util.*;

class ClientThread extends Thread {
	String name = "", clientIdentitiy = "", line = "";
	Server ser = null;
	Socket clientSocket = null;
	DataInputStream is = null;
	PrintStream os = null;

	public ClientThread(Server s, Socket clientSocket) {
		this.ser = s;
		this.clientSocket = clientSocket;
	}

	public void run() {
		try {
			is = new DataInputStream(clientSocket.getInputStream());
			os = new PrintStream(clientSocket.getOutputStream());

			os.println("Enter Your Name :");
			name = is.readLine();
			clientIdentitiy = name;

			os.println("Welcome '" + name + "'  to two phase commit protocol application.\nYou will recieve a vote request now.");
			os.println("VOTE REQUEST\nPlease Enter Commit or Abort to proceed.");
			for(int i=0;i<(ser.cList).size();i++) {
				if((ser.cList).get(i) != this) {
					((ser.cList).get(i)).os.println("---- A new user '" + name + "' has joined the application.");
				}
			}

			while(true) {
				line =  is.readLine();
				if(line.equalsIgnoreCase("ABORT") == true) {
					System.out.println("\nFrom " + clientIdentitiy + " : ABORT.\nSince Aborted server will not wait for further inputs from clients.");
					System.out.println("\nAborted...");
					for(int i=0;i<(ser.cList).size();i++) {
						((ser.cList).get(i)).os.println("GLOBAL_ABORT");
						((ser.cList).get(i)).os.close();
						((ser.cList).get(i)).is.close();
					}
					break;
				}
				if(line.equalsIgnoreCase("COMMIT") == true) {
					System.out.println("\nFrom " + clientIdentitiy + " : COMMIT.");
					if((ser.cList).contains(this)) {
						(ser.data).set((ser.cList).indexOf(this), "COMMIT");
						for(int i=0;i<(ser.cList).size();i++) {
							if((ser.data).get(i).equalsIgnoreCase("NOT_SENT") == true) {
								ser.inputFromAll = false;
								System.out.println("Waiting for messages from other clients");
								break;
							}
							ser.inputFromAll = true;
						}
					}

					if(ser.inputFromAll) {
						System.out.println("COMMITTED....");
						for(int i=0;i<(ser.cList).size();i++) {
							((ser.cList).get(i)).os.println("GLOBAL_COMMIT");
							((ser.cList).get(i)).os.close();
							((ser.cList).get(i)).is.close();
						}
						break;
					}
				}
					
			}
			ser.closed = true;
			clientSocket.close();
		}
		catch(Exception e) {
			System.out.println("ClientThread Exception : " + e);
		}
	}
}

public class Server {
	List<String> data;
	List<ClientThread> cList;
	boolean closed = false, inputFromAll = false;

	public Server() {
		cList = new ArrayList<ClientThread>();
		data = new ArrayList<String>();
	}

	public static void main(String args[]) {
		Socket clientSocket = null;
		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(9999);
		}
		catch(Exception e) {
			System.out.println("Exception : " + e);
		}
		Server s = new Server();

		while(!s.closed) {
			try {
				clientSocket = serverSocket.accept();
				ClientThread ct = new ClientThread(s, clientSocket);
				(s.cList).add(ct);
				System.out.println("Total Number of Clients : " + (s.cList).size());
				(s.data).add("NOT_SENT");
				ct.start();
			}
			catch(Exception e) {
				System.out.println("Exception :" + e);
			}
		}

		try {
			serverSocket.close();
		}
		catch(Exception e) {
			System.out.println("Error closing :"+e);
		}
	}
}