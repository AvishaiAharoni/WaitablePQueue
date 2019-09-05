package WaitablePQueue;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;

import org.junit.jupiter.api.Test;


class IntegerComperator<T extends Comparable<T>> implements Comparator<T> {

	IntegerComperator() {};
	
	@Override
    public int compare(T a, T b) {
        int num1 = ((Integer)a).intValue();
        int num2 = ((Integer)b).intValue();
		
        num1 /= 10;
        num1 %= 10;
        num2 /= 10;
        num2 %= 10;
		
		return num1 - num2;
    }
}

class WQTest {

	

	@Test
	void testCtor() {
		
		WaitablePQueue<Integer> q = new WaitablePQueue<>();
		
		WaitablePQueue<Integer> qCapacity = new WaitablePQueue<>(13);
		
		IntegerComperator<Integer> ic = new IntegerComperator();
		
		WaitablePQueue<Integer> qComparator = new WaitablePQueue<>(13, ic);
	}
	
	@Test
	void testSize() {
		
		WaitablePQueue<Integer> q = new WaitablePQueue<>();
		
		WaitablePQueue<Integer> qCapacity = new WaitablePQueue<>(13);
		
		IntegerComperator<Integer> ic = new IntegerComperator<>();
		
		WaitablePQueue<Integer> qComparator = new WaitablePQueue<>(13, ic);
		
		Integer i = 3;
		Integer i2 = 3;
		
		assertEquals(q.getSize(), 0);
		assertEquals(qCapacity.getSize(), 0);
		assertEquals(qComparator.getSize(), 0);
		
		q.enqueue(i);
		qCapacity.enqueue(i);
		qComparator.enqueue(i);
		q.enqueue(i2);
		qCapacity.enqueue(i2);
		qComparator.enqueue(i2);
		
		assertEquals(q.getSize(), 2);
		assertEquals(qCapacity.getSize(), 2);
		assertEquals(qComparator.getSize(), 2);
	}
	
	@Test
	void testEnqueue() {
		
		final int num = 1000000;
		
		WaitablePQueue<Integer> q = new WaitablePQueue<>();
		
		Thread c = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < num; ++i) {
					q.enqueue(i);
				}
			}
		});
		
		Thread b = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < num; ++i) {
					q.enqueue(i);
				}
			}
		}); 
		
		Thread a = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < num; ++i) {
					q.enqueue(i);
				}
			}
		}); 
		
		a.start();
		b.start();
		c.start();
		
		try {
			a.join();
			b.join();
			c.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println(q.getSize());
		assertEquals(q.getSize(), 3000000);
	}
	
	@Test
	void testDeQueueOneThread() {
		WaitablePQueue<Integer> q = new WaitablePQueue<>();
		
		Integer i = 2;
		
		q.enqueue(i);
		
		try {
			assertEquals(q.dequeue(), i);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(q.getSize(), 0);
	}
	
	@Test
	void testDeQueueTwoThreads() {
		WaitablePQueue<Integer> q = new WaitablePQueue<>();
		
		Thread a = new Thread(new Runnable() {
			@Override
			public void run() {
				
				try	{
				    Thread.sleep(700);
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
				
				for (int i = 0; i < 10; ++i) {
					q.enqueue(i);
				}
			}
		});
		
		
		
		a.start();
		
		try {
			q.dequeue();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(q.getSize(), 9);
	}
	
	@Test
	void testDeQueueMultyThread() {
		final int num = 1000001;
		
		WaitablePQueue<Integer> q = new WaitablePQueue<>();
		
		Thread c = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 1; i < num; ++i) {
					if (i % 10 != 0) {
						q.enqueue(i);
					} else {
						try {
							q.dequeue();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		});
		
		Thread b = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 1; i < num; ++i) {
					if (i % 10 != 0) {
						q.enqueue(i);
					} else {
						try {
							q.dequeue();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		});
		
		Thread a = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 1; i < num; ++i) {
					if (i % 10 != 0) {
						q.enqueue(i);
					} else {
						try {
							q.dequeue();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		});
		
		a.start();
		b.start();
		c.start();
		
		try {
			a.join();
			b.join();
			c.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println(q.getSize());
		assertEquals(q.getSize(), 2400000);
	}
	
	@Test
	void testRemove() {
		WaitablePQueue<Integer> q = new WaitablePQueue<>();
		
		Integer i = 2;
		Integer toRemove = 2;
		Integer notToRemove = 4;
		
		q.enqueue(i);
		
		try {
			assertEquals(q.remove(notToRemove), false);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(q.getSize(), 1);
		
		try {
			assertEquals(q.remove(toRemove), true);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(q.getSize(), 0);
	}
	
	@Test
	void testRemoveMultyThread() {
		
		final int num = 10000;
		
		WaitablePQueue<Integer> q = new WaitablePQueue<>();
		
		Thread a = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < num; ++i) {
						q.enqueue(i);
					}
				}
			}
		);
		
		Thread c = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < num; ++i) {
						q.enqueue(i);
					}
				}
			}
		);
		
		Thread b = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < num; ++i) {
						Integer in = i;
						try {
							q.remove(in);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		);
		
		a.start();
		
		try {
			a.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		c.start();
		b.start();
		
		try {
			b.join();
			c.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertEquals(q.getSize(), num);
	}
}
