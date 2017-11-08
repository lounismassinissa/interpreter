
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Pattern;



public class CCL_Client extends Thread {



	private Socket s;
	public Socket dataSocket;
	private int ID; // identifier of the client
	private PrintStream out;
	private BufferedReader in;
	private String server_ip;
	private int count = 0;
	private Receiver receiver;



	private static boolean nextIsMac = false;

	public CCL_Client(){
		System.out.println("---------------------------------------------------");
		System.out.println("           CupCarbon Python Real Node V1           ");
		System.out.println("---------------------------------------------------");
		readConfig();
		this.ID = Integer.valueOf(Config.id);
		this.server_ip = Config.ip_server;

	}


	public void connect(){
		try {
		    System.out.println("Connection to server ........................");
		    s = new Socket(server_ip,8888);
		    out = new PrintStream(s.getOutputStream());
		    in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		    //initCom();
		    out.println(COMMAND.ASK_CONNECTION+" 127.0.0.1 "+Config.id);
		    receiver = new Receiver(this, in, out);

		} catch (IOException e) {
			System.out.println("Server not found !");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				System.out.println("wait reconnecting error ");
			}
			count++;
			if(count < 10) connect();

		}
	}

	public void reconnect(){
		try {

		    s = new Socket(server_ip,8888);
		    out = new PrintStream(s.getOutputStream());
		    in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		    //initCom();
		    out.println(COMMAND.ASK_CONNECTION+" ip "+Config.id);
			this.receiver.updateComStreams(in, out);

		} catch (IOException e) {
			System.out.println("Server not found !");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				System.out.println("wait reconnecting error ");
			}
			count++;
			if(count < 10) connect();

		}

	}

	public void send(String msg){
			out.println(msg);
			System.out.println(msg);
	}

	public void initCom(){



		System.out.println("---------------------------------------------------");
		System.out.println("Send:  "+COMMAND.ASK_CONNECTION+" ip "+Config.id);
		System.out.println("---------------------------------------------------");
	}


	public static String ip(){
		  String s;
	        Process p;
	        try {
	            p = Runtime.getRuntime().exec("ifconfig wlan0");
	            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
	            String[] tab;
	            int i = 0;
	            while ((s = br.readLine()) != null){
	            	s = s.substring(10);
	            	tab = s.split(" ");
	            	for(int j= 0 ; j < tab.length; j++){
	            		if(!tab[j].equals("")){
	            			if(isIP(tab[j])) return tab[j].substring(5);

	            		}
	            	}
	                i++;
	            }
	            p.waitFor();
	            System.out.println ("exit: " + p.exitValue());
	            p.destroy();
	        } catch (Exception e) {}

	        return null;
	}


	public static String mac(){
		  String s;
	        Process p;
	        try {
	            p = Runtime.getRuntime().exec("ifconfig eth0");
	            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
	            String[] tab;
	            int i = 0;
	            while ((s = br.readLine()) != null){
	            	s = s.substring(10);
	            	tab = s.split(" ");
	            	for(int j= 0 ; j < tab.length; j++){
	            		if(!tab[j].equals("")){
	            			if(isMAC(tab[j])) return tab[j];
	            		}
	            	}
	                i++;
	            }
	            p.waitFor();
	            System.out.println ("exit: " + p.exitValue());
	            p.destroy();
	        } catch (Exception e) {}

	        return null;
	}



	public static boolean isIP(String s){

		String sub ;
		String[] tab;

		if(s.substring(0,4).equals("addr")){
			sub = s.substring(5);
			tab = sub.split(Pattern.quote("."));
			if(tab.length == 4) return true;
		}
		return false;

	}
	public static boolean isMAC(String s){

		if(nextIsMac) {
			nextIsMac =false;
			return true;
		}
		else{
			if(s.equals("HWaddr")) nextIsMac = true;

		}

		return false;

	}

	public void  readConfig(){
		try {
			BufferedReader br = new BufferedReader(new FileReader("config"));
			String str;
			String[] tab;
			while((str = br.readLine()) !=null){
				tab = str.split(" ");
				if(tab[0].equals("ip-server")){
					Config.ip_server = tab[1];
				}
				if(tab[0].equals("id")){
					Config.id = tab[1];
				}
				if(tab[0].equals("key")){
					Config.key = tab[1];
				}
			}
		} catch (Exception e) {

			System.out.println("interpreter: Config file note found");
		}
	}




}
