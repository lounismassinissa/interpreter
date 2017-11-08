

import java.io.PrintStream;

public class Ping extends Thread{
	private PrintStream out;
	public Ping(PrintStream out){
		this.out = out;

	}

	public void run(){

		while(true){


			try {
				out.println(COMMAND.PING);
				Thread.sleep(2000);
			} catch (Exception e) {
				System.out.println("Impossible to send data to server !!!");
			}
		}
	}

}
