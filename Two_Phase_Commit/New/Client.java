import java.io.*;
import java.net.*;
import java.util.*;

public class Client implements Runnable {
	public static Socket client = null;
	public static PrintStream os = null;
	public static DataInputStream is = null;
	public static BufferedReader br = null;
	public static boolean closed = false;

	public static void main(String args[]) {
		String host = "localhost";
		int p_number = 9999;

		try{
			client = new Socket(host, p_number);
			br = new BufferedReader(new InputStreamReader(System.in));
			os = new PrintStream(client.getOutputStream());
			is = new DataInputStream(client.getInputStream());
		}
		catch(Exception e){
			System.out.println("Client Exception : "+e);
		}

		if(client!=null && os!=null && is!=null && br!=null){
			try{
				new Thread(new Client()).start();
				while(!closed) {
					String x = br.readLine();
					os.println(x);
				}
				os.close();
				is.close();
				client.close();
			}
			catch(Exception e){
				System.out.println("Client Exception Message: "+e);
			}
		}
		else
			System.out.println("DAmn You");
	}

	public void run() {
		String response;
		try{
			while((response=is.readLine())!=null){
				System.out.println("Server Response : "+response);
				if(response.equalsIgnoreCase("GLOBAL_COMMIT") == true || response.equalsIgnoreCase("GLOBAL_ABORT") == true)
					break;
			}
			closed = true;
		}
		catch(Exception e) {
			System.out.println("Client Thread Exception :"+e);
		}
	}
}
