package application;

public class UtilitySki {

	public static void interval(int milliSeconds) {
		try {
			Thread.sleep(milliSeconds);			
		} catch(InterruptedException ex) {
			System.out.println("Something went wrong with sleeping thread: " + ex.getMessage());
		}
	}
}
