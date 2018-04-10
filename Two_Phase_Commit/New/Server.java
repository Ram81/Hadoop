import java.io.*;
import java.net.*;
import java.util.*;

class ClientThread extends Thread
{
  	DataInputStream is = null;
  	String line;
  	String destClient="";
  	String name;
  	PrintStream os = null;
  	Socket clientSocket = null;       
  	String clientIdentity; 
  	Server ser;

  	public ClientThread(Server ser,Socket clientSocket)
  	{
    	this.clientSocket=clientSocket;
    	this.ser=ser;
  	}


  	public void run() 
  	{
    	try{
    		is = new DataInputStream(clientSocket.getInputStream());
    		os = new PrintStream(clientSocket.getOutputStream());

    		os.println("Enter your Name: ");
    		name = is.readLine();
    		clientIdentity = name;

    		os.println("Welcome " + name + " to this 2 Phase Application.\nYou will recieve a vote request now");
    		os.println("VOTE_REQUEST\nPlease Enter Commit or Abort to proceed.");
    		for(int i=0;i<(ser.clientList).size();i++){
    			if((ser.clientList).get(i) != this){
    				((ser.clientList).get(i)).os.println("------ A new user " + name + " has joined the application.");
    			}
    		}

    		while(true){
    			line = is.readLine();

    			if(line.equalsIgnoreCase("ABORT") == true){
    				System.out.println("\nFrom '" + clientIdentity + "' : ABORT was recieved.\n\nSince aborted we will not wait for any inputs");
    				System.out.println("\nAborted.");

    				for(int i=0;i<(ser.clientList).size();i++) {
    					((ser.clientList).get(i)).os.println("GLOBAL_ABORT");
    					((ser.clientList).get(i)).os.close();
    					((ser.clientList).get(i)).is.close();
    				}
    				break;
    			}
    			
    			if(line.equalsIgnoreCase("COMMIT") == true){
    				System.out.println("\nFrom '" + clientIdentity + "' : COMMIT was recieved.");

    				if((ser.clientList).contains(this)) {
    					(ser.data).set((ser.clientList).indexOf(this), "COMMIT");
    					for(int j=0;j<(ser.clientList).size();j++) {
    						if(((ser.data).get(j)).equalsIgnoreCase("Not_Sent") == true) {
    							ser.inputFromAll = false;
    							System.out.println("Waiting for inputs from other clients.");
    							break;
    						}
    						ser.inputFromAll = true;
    					}

    					if(ser.inputFromAll) {
    						System.out.println("\n\nCommitted......");
    						for(int i=0;i<(ser.clientList).size();i++) {
    							((ser.clientList).get(i)).os.println("GLOBAL_COMMIT");
    							((ser.clientList).get(i)).os.close();
    							((ser.clientList).get(i)).is.close();
    						}
    						break;
    					}
    				}
    			}
    		}
    		ser.closed = true;
    		clientSocket.close();
    	}
    	catch(Exception e){
    		System.out.println("ClientThread Exception: "+e);
    	}
  	}
  // run ends
}

public class Server {
	boolean closed = false, inputFromAll = false;
	List<ClientThread> clientList;
	List<String> data;

	Server() {
		clientList = new ArrayList<ClientThread>();
		data = new ArrayList<String>();
	}

	public static void main(String args[]) {
		Socket client = null;
		ServerSocket server = null;
		int p_number = 9999;

		Server s = new Server();

		try{
			server = new ServerSocket(p_number);
		}
		catch(Exception e){
			System.out.println("Exception : "+ e);
		}
		
		while(!s.closed) {
			try{
				client = server.accept();
				ClientThread  ct = new ClientThread(s, client);
				s.clientList.add(ct);
				System.out.println("Total Number of Clients: "+ s.clientList.size());
				s.data.add("Not_Sent");
				ct.start();
			}
			catch(Exception e) {
				System.out.println("Exception message : "+e);
			}
		}

		try{
			server.close();
		}
		catch(Exception e){
			System.out.println("Exception Stop: "+e);
		}
	}
}