
package taxi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * main Class: create a SImulation and execute it.
 * 
 */
public class Main {

  public static void main(String[] args) throws InterruptedException {
    Simulation sim = new Simulation();
    sim.start();
    sim.showStatistics();
    
   
  }
}
