package WaitablePQueue;

public class Test {

	public static void main(String[] args) {
		
		WaitablePQueue<Integer> wpq = new WaitablePQueue<Integer>();
		
		Thread t1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				int i = 0;
				while (i < 20) {
					wpq.enqueue(i++);
//					System.out.println("insert " + i);
				}
				
			}
		});
		
		Thread t2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				int i = 0;
				while (i < 20) {
					try {
						wpq.dequeue();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					++i;
				}
				
			}
		}); 

		Thread t3 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					wpq.dequeue();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("remove");
			}
		}); 
		
		t2.start();
		t1.start();
//		t3.start();
	}

}
