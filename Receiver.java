
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;




public class Receiver implements Runnable {

	private CCL_Client client;
	private PrintStream out;
	private BufferedReader in;
	private Thread _t;
	private DataReceiver dataReceiver = null;
	private boolean stop = false;
	private Interpreter interpreter;
	private String lastCommand = "";



	public Receiver(CCL_Client c, BufferedReader in, PrintStream out){
		client = c;
		this.in = in;
		this.out = out;
		Sensor.com = this;
		_t = new Thread(this);
	    _t.start();
	}

	public void updateComStreams(BufferedReader in, PrintStream out){
		this.in = in;
		this.out = out;
	}


	@Override
	public void run() {

		try {
			int i = 0;
			while (i < 1){
				String sCurrentLine = "";
				while ((sCurrentLine = in.readLine()) != null) {
				    handler(sCurrentLine);
				}
				i++;
				System.out.println("Server communication error");
				  try {
                                    Thread.sleep(2000);
                                  } catch (InterruptedException e) {
                                    e.printStackTrace();
                                  }
				//this.client.reconnect();
				//this.out.println(this.lastCommand);
			}

			// server deconnected
			// reconnection
		} catch (IOException e) {
			System.out.println("client  "+Config.id+"data reveiver error");
		}

	}

	public void handler(String msg){

		String tab[] = msg.split(" ");


		if(tab[0].compareTo(COMMAND.CONNECTION)==0){
			System.out.println("Connection to server OK");
			//Interpreter.onLed(0);
			Ping ping = new Ping(out);
			ping.start();
		}

		if(tab[0].compareTo(COMMAND.SEND)==0){
			 synchronized(Monitor.lock){
				System.out.println("receive ["+tab[3]+"]");
			    Sensor.addMsgToBuffer(tab[3]);
			    Monitor.lock.notify();
			 }
		}

		if(tab[0].compareTo(COMMAND.SCRIPT)==0){
			display("CMD", msg);

			String ip = tab[1];
			int port = Integer.valueOf(tab[2]);
			try {
				client.dataSocket = new Socket(ip,port);
				dataReceiver = new DataReceiver(this,client.dataSocket);
				this.lastCommand = COMMAND.OK_GET_SCRIPT;
				out.println(COMMAND.OK_GET_SCRIPT);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		if(tab[0].compareTo(COMMAND.RUN_SCRIPT)==0){
			display("CMD", msg);
			String mainScript = tab[1];
			String vId = tab[2];
			String nbNei = tab[3];
			String[] vPos = {tab[4],tab[5]};
			//Interpreter.onLed(1);
			interpreter = new Interpreter(mainScript);

			Sensor.virtualId = vId;
			Sensor.virtualNbNei = nbNei;
			Sensor.virtualPos = vPos;

			interpreter.start();
		}

		if(tab[0].compareTo(COMMAND.STOP_SCRIPT)==0){
			display("CMD", msg);
			try{
			   interpreter.stop();
			}catch(Exception e){
				
			}
		}

		if(tab[0].compareTo(COMMAND.PING)==0){
			//display("CMD", msg);
		}

	}

	public void endOfData(){
		display("CMD", "all file received !");

		try {
			if(dataReceiver != null){
				dataReceiver.close();

				client.dataSocket.close();

				dataReceiver.stop();
			}else{
				display("ERROR", "Object dataReceiver == null ");
			}
		} catch (IOException e) {
			System.out.println("error close dataocket");
		}
		this.lastCommand = COMMAND.ALL_DATA_RECEIVED;
		out.println(COMMAND.ALL_DATA_RECEIVED);
	}

	public void command(String cmd){
		System.out.println("command ["+cmd+"]");
		this.lastCommand = cmd;
		out.println(cmd);
	}
	public void mark(String cmd){
		this.lastCommand = cmd;
		out.println(cmd);
	}

	private void display(String id, String msg){
		System.out.println(id+"   "+msg);

	}

}
