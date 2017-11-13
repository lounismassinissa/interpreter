
import java.io.IOException;

public class LedManager extends Thread {




	public LedManager(){

		
	}

	public void run(){
		try{
			Runtime r = Runtime.getRuntime();
			Process p;
			while(true){
				p = r.exec("python leds.py");
				p.waitFor();
				System.out.println("restart Runtime");
		    }
		}catch(Exception e){
		}

	}


}
