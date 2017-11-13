
import java.util.LinkedList;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.FileNotFoundException;

public class Sensor {



	public static int BRAODCAST = -1;

	private static int idGen = -1;

	public static LinkedList<String> buffer = new LinkedList<String>();

	public static Receiver com;

	public static String virtualId;

	public static String virtualNbNei;

	public static String[] virtualPos;


	public synchronized static void send(int dist, String data){
		System.out.println("excuting send");
		if(dist == BRAODCAST) com.command(COMMAND.SEND+" "+virtualId+" * "+data);
		else com.command(COMMAND.SEND+" "+virtualId+" "+dist+" "+data);

	}

	public static void addMsgToBuffer(String msg){
		buffer.addFirst(msg);
		Monitor.lock.notify();
	}

	public synchronized static String receive(){
		String msg;

		if(available()){
			msg = buffer.getLast();
			buffer.removeLast();
			return msg;
		}
		return "";
	}

	public static int getId(){
		System.out.println("getId");
		return Integer.valueOf(virtualId);
	}

	public synchronized static boolean available(){
		if(buffer.size()>0) return true;
		else return false;
	}

	public synchronized static void mark(){

		com.mark(COMMAND.MARK+" "+1);
		System.out.println("mark");
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("leds.conf", "UTF-8");
			writer.println("1");
			writer.close();

		} catch (FileNotFoundException e) {
		} catch (UnsupportedEncodingException e) {

		}

	}

	public synchronized static void unmark(){

		com.mark(COMMAND.MARK+" "+0);
		System.out.println("unmark");
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("leds.conf", "UTF-8");
			writer.println("0");
			writer.close();

		} catch (FileNotFoundException e) {
		} catch (UnsupportedEncodingException e) {

		}
		

	}


	public static void delay(int time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public static void wait(int id){
		/*
		Node node = getNodeById(id);
		Consol.display("Waiting ......");

		try {
			node.lock = true;
			while(node.lock){
				Thread.sleep(10);
			}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			*/

	}
	public static void wait(int id, int time){
		/*
		 *
		Node node = getNodeById(id);
		Consol.display("Waiting ......");

		int x = 0;
		try {
		node.lock = true;
			while(x < time && node.lock){
				x = x + 10;
				Thread.sleep(10);
				}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		*/
	}
	// wait version 2
	public static void attendre(){
		System.out.println("attendre");
		try {
			synchronized(Monitor.lock){
				if(!available()) Monitor.lock.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public static void attendre(int time){
		System.out.println("attendre time");
		try {
			synchronized(Monitor.lock){
				if(!available()) Monitor.lock.wait(time);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public static int[] getPos(){
		int[] pos = new int[2];
		pos[0] = Integer.valueOf(virtualPos[0]);
		pos[1] = Integer.valueOf(virtualPos[1]);
		return pos;
	}
	public static int nbNeighbours(){
		return Integer.valueOf(virtualNbNei);
	}



}
