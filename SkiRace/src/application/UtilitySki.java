package application;

public class UtilitySki {

	public static void interval(int speedSimulator) {
	    try {
	        Thread.sleep(10 / speedSimulator); // Non-blocking interval
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }
	}
}
