import java.io.*;
import java.util.*;
import com.datastax.driver.core.*;

public class Partitioning {
	public static Cluster cluster;
	public static Session session;
	public static ResultSet rs;
	public static Row rows;

	public static void main(String args[]) {
		try {
			cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
			session = cluster.connect();
			System.out.println("Cluster Created");

			session.execute("CREATE KEYSPACE IF NOT EXISTS Partition WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': 1 }");
			session.execute("USE Partition");

			session.execute("CREATE TABLE IF NOT EXISTS users_1(firstName text, lastName text, age int, city text, email text, id int, PRIMARY KEY(email, id, age))");

			session.execute("INSERT INTO users(firstName, age, city, email, id) VALUES('Raj', 18, 'Pune', 'raj@gmail.com', 1)");
			session.execute("INSERT INTO users(firstName, age, city, email, id) VALUES('Yash', 21, 'Pune', 'yash@gmail.com', 1)");
			session.execute("INSERT INTO users(firstName, age, city, email, id) VALUES('Noobie', 23, 'Pune', 'noobie@gmail.com', 2)");

			rs = session.execute("Select * from users where id = 1 order by age asc");
			for(Row row : rs) {
				System.out.println(row.getString("email") + ", " + row.getString("firstName") + ", " + row.getInt("age") + ", " + row.getString("city"));
			}
		}
		catch(Exception e) {
			System.out.println("Exception :" + e);
		}
		System.out.println("Exceptioe");
	}
}