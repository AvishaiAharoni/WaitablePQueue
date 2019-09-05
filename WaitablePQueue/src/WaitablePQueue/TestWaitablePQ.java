package WaitablePQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Comparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class WaitablePQueueTest {
	WaitablePQueue<Integer> queue;

	@BeforeEach
	public void beforeEachTest() throws InterruptedException {

		queue = new WaitablePQueue<>(10, new Comparator<Integer>() {
			@Override
			public int compare(Integer num1, Integer num2) {
				return (num1 - num2);
			}
		}); 
	}

	@Test
	void TestEnqueue() throws InterruptedException {
		int num_of_enqueues = 1000000;

		Thread t_enqueue1 = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < num_of_enqueues; ++i) {
					queue.enqueue(i);
				}
			}
		});

		Thread t_enqueue2 = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < num_of_enqueues; ++i) {
					queue.enqueue(i);
				}
			}
		});

		Thread t_enqueue3 = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < num_of_enqueues; ++i) {
					queue.enqueue(i);
				}
			}
		});

		t_enqueue1.start();
		t_enqueue2.start();
		t_enqueue3.start();

		t_enqueue1.join();
		t_enqueue2.join();
		t_enqueue3.join();

		assertEquals(queue.getSize(), 3 * num_of_enqueues);
	}


	@Test
	void TestDequeue() throws InterruptedException {
		int num_of_enqueues = 1000000;
		int num_of_dequeues = 100000;
		int num_of_loops = 10;
		
		Thread t_enqueue = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < num_of_enqueues; ++i) {
					queue.enqueue(i);
				}
			}
		});

		t_enqueue.start();
		t_enqueue.join();
		
		for (int i = 0; i < num_of_loops; ++i) {
			Thread t_dequeue = new Thread(new Runnable() {
				@Override
				public void run() {
					for (int j = 0; j < num_of_dequeues; ++j) {
						try {
							queue.dequeue();
						} catch (InterruptedException e) { e.printStackTrace(); }
					}
				}
			});

			t_dequeue.start();
			t_dequeue.join();

			assertEquals(queue.getSize(), (num_of_enqueues - ((i + 1) * num_of_dequeues)));
		}
	}

	@Test
	void TestRemove() throws InterruptedException {
		int num_of_enqueues = 1000000;
		int num_of_removes = 100;

		Thread t_enqueue = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < num_of_enqueues; ++i) {
					queue.enqueue(i);
				}
			}
		});

		t_enqueue.start();
		t_enqueue.join();

		for (int i = 0; i < 5; ++i) {
			Thread t_remove = new Thread(new Runnable() {
				@Override
				public void run() {
					for (int j = 0; j < num_of_removes; ++j) {
						try {
							queue.remove(j);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});

			t_remove.start();
			t_remove.join();

			assertEquals(queue.getSize(), (num_of_enqueues - num_of_removes));
		}
	}

	@Test
	void IntegratedTest() throws InterruptedException {
		int num_of_enqueues = 1000000;
		int num_of_removes = 500;
		int num_of_dequeues = 200000;

		Thread t_enqueue = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < num_of_enqueues; ++i) {
					queue.enqueue(i);

				}
			}
		});

		t_enqueue.start();
		t_enqueue.join();

		for (int i = 0; i < 1; ++i) {
			Thread t_remove = new Thread(new Runnable() {
				@Override
				public void run() {
					for (int j = 0; j < num_of_removes; ++j) {
						try {
							queue.remove(j);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});
			t_remove.start();
			t_remove.join();

			assertEquals(queue.getSize(), (num_of_enqueues - num_of_removes));
		}

		for (int i = 0; i < 4; ++i) {
			Thread t_dequeue = new Thread(new Runnable() {
				@Override
				public void run() {
					for (int j = 0; j < num_of_dequeues; ++j) {
						try {
							queue.dequeue();
						} catch (InterruptedException e) { e.printStackTrace(); }
					}
				}
			});
			
			t_dequeue.start();
			t_dequeue.join();

			assertEquals(queue.getSize(), (num_of_enqueues - num_of_removes - ((i + 1) * num_of_dequeues)));
		}
	}
	
	@Test
	void ConcurrentTest() throws InterruptedException {
		int num_of_enqueues = 1000000;
		int num_of_dequeues = 100000;
		int num_of_loops = 10;
		
		for (int i = 0; i < num_of_loops; ++i) {
			
			Thread t_enqueue = new Thread(new Runnable() {
				@Override
				public void run() {
					for (int j = 0; j < num_of_enqueues; ++j) {
						queue.enqueue(j);
					}
				}
			});
			
			Thread t_dequeue = new Thread(new Runnable() {
				@Override
				public void run() {
					for (int j = 0; j < num_of_dequeues; ++j) {
						try {
							queue.dequeue();
						} catch (InterruptedException e) { e.printStackTrace(); }
					}
				}
			});

			t_dequeue.start();
			t_enqueue.start();

			t_dequeue.join();
			t_enqueue.join();

			assertEquals(queue.getSize(), (num_of_enqueues - num_of_dequeues) * (i + 1));
		}
	}
}