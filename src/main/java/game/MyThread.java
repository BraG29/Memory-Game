package game;

public class MyThread extends Thread {
	
	public void run() {
		try {
			System.out.println("Sleeping.........");
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
