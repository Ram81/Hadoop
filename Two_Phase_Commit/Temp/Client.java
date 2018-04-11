import java.net.*;
import java.io.*;
import java.util.*;

public class Client implements Runnable {
	static Socket clientSocket = null;
	static DataInputStream is = null;
	static PrintStream os = null;
	static BufferedReader br = null;
	static boolean closed = false;

	public void run() {
		String response;
		try {
			while((response = is.readLine()) != null) {
				System.out.println("Server Response : " + response);
				if(response.equalsIgnoreCase("GLOBAL_ABORT") || response.equalsIgnoreCase("GLOBAL_COMMIT")) {
					break;
				}
			}
			closed = true;
		}
		catch(Exception e) {
			System.out.println("Response Not Found Exception :" + e);
		}
	}

	public static void main(String args[]) {
		try {
			clientSocket = new Socket("localhost", 9999);
			br = new BufferedReader(new InputStreamReader(System.in));
			is = new DataInputStream(clientSocket.getInputStream());
			os = new PrintStream(clientSocket.getOutputStream());
		}
		catch(Exception e) {
			System.out.println("Clien Exception :" + e);
		}

		if(clientSocket != null && is != null && os != null) {
			try {
				new Thread(new Client()).start();

				while(!closed) {
					os.println(br.readLine());
				}
				os.close();
				is.close();
				clientSocket.close();
			}
			catch(Exception e) {
				System.out.println("Client Exceptioon : "+ e);
			}
		}
		else
			System.out.println("Some Problem in Initializing.");
	}

}