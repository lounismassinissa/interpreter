
import org.python.util.PythonInterpreter;
import java.io.File;
import java.io.IOException;

public class Interpreter extends Thread {


	private PythonInterpreter interp;
	private String script;
	private static Process[] leds =  new Process[8];


	public Interpreter(String script){

		this.script = System.getProperty("user.dir")+File.separator+script;
	}

	public void run(){
		try{
			interp = new PythonInterpreter();
			interp.execfile(script);
		}catch(Exception e){
		}

	}

	public static void onLed(int i){
		if(i >= 0 && i < 8){
			Runtime r = Runtime.getRuntime();
			try{
				leds[i] = r.exec("python led_"+i+"_ON.py");
				
			}catch(IOException e){
			}
		}
	}
	public static void offLed(int i){
		if(i >= 0 && i < 8){
			try{
				leds[i].destroy();
				
			}catch(Exception e){
			}
		}
	}

}
