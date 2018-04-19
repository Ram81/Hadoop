import java.sql.*;
import java.rmi.*;
import java.io.*;
import java.util.*;
import java.rmi.registry.*;

public class Client{
	static String name_1, name_2, name_3;
	public static void main(String args[]) {
		Client c = new Client();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try {
			int ch;
			Registry r1 = LocateRegistry.getRegistry("localhost", 1030);
			DBInterface DI = (DBInterface) r1.lookup("DBserv");

			do {
				name_1 = "abc";
				name_2 = "def";
				System.out.println("Output: " + DI.input(name_1, name_2));
				System.out.println("Enter to continue (0/1): ");
				ch = Integer.parseInt(br.readLine());
			}while(ch!=0);
		}
		catch(Exception e) {
			System.out.println("Exception Occured: " + e);
		}
	}
}