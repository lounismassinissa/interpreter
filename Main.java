import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.regex.Pattern;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.FileNotFoundException;


public class Main {

	public static void main(String[] args) {
		System.out.println("Working Directory = " + System.getProperty("user.dir"));
//---------------------------------------------------------------------------------------------------------
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("leds.conf", "UTF-8");
			writer.println("0");
			writer.close();

		} catch (FileNotFoundException e) {
		} catch (UnsupportedEncodingException e) {

		}
//-----------------------------------------------------------------------------------------------------------
		LedManager led = new LedManager();
		led.start();
//-----------------------------------------------------------------------------------------------------------
		CCL_Client app= new CCL_Client();
		app.connect();

	}

}
