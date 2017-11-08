import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.regex.Pattern;


public class Main {

	public static void main(String[] args) {
		System.out.println("Working Directory = " + System.getProperty("user.dir"));
		CCL_Client app= new CCL_Client();
		app.connect();

	}

}
