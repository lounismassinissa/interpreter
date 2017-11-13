
import org.python.util.PythonInterpreter;
import java.io.File;
import java.io.IOException;

public class LedThread extends Thread {



	public LedThread(){

	}

	public void run(){
                Runtime r = Runtime.getRuntime();
                try{
                    r.exec("python led_0_ON.py");
                        
                }catch(IOException e){
                }
	}

}
