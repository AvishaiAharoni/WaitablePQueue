package WaitablePQueue;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.Semaphore;

/**
 * a wrapper to the PQueue that is thread safe
 * @author Avishai
 *
 */
public class WaitablePQueue<E> {
	
    private static final int DEFAULT_INITIAL_CAPACITY = 11;
	private PriorityQueue<E> pq;
	private Semaphore sem;

	/**
	 * constructor with default parameters
	 */
	public WaitablePQueue() {
		this(DEFAULT_INITIAL_CAPACITY, null);
	}
	
	/**
	 * constructor with given capacity 
	 */
	public WaitablePQueue(int capacity) {
		this(capacity, null);
	}

	/**
	 * constructor with given comparator
	 */
	public WaitablePQueue(Comparator<? super E> comparator) {
		this(DEFAULT_INITIAL_CAPACITY, comparator);
    }

	/**
	 * constructor with given capacity and given comparator
	 */
	public WaitablePQueue(int Capacity, Comparator<? super E> comparator) {
	   	this.pq = new PriorityQueue<>(Capacity, comparator);
	   	this.sem = new Semaphore(0);
	}
		
	/**
	 * to insert a new event to the queue
	 * @param e - the event to insert to the queue
	 */
	public void enqueue(E e) {
			synchronized (this.pq) {
				this.pq.add(e);
//				System.out.println("insert " + e);
			}
			this.sem.release();
	}
	
	/**
	 * to dqueue the first item from the queue
	 * @return the item that was dequeued
	 * @throws InterruptedException
	 */
	public E dequeue() throws InterruptedException {
		this.sem.acquire();

		synchronized (this.pq) {
			E res = this.pq.poll();
//			System.out.println("dequeue " + res);

			return res;
		}		
	}
	
	/**
	 * to remove a given item from the queue
	 * @param e - the item to be removed
	 * @return 
	 * @throws InterruptedException 
	 */
	public boolean remove(E e) throws InterruptedException {
		boolean res = false;
		
		if (this.sem.tryAcquire()) {
			synchronized (this.pq) {
				if (this.pq.remove(e)) {
					res = true;
				}
				else {
					this.sem.release();
				}
			}
		}
		
		return res;
	}
	
	/**
	 * to get the size in the {@link WaitablePQueue}
	 * @return the size
	 */
	public int getSize() {
		return this.pq.size();
	}
	
	/**
	 * to check if the {@link WaitablePQueue} is empty
	 * @return <code>true</code> if it empty
	 */
	public boolean isEmpty() {
		return this.pq.isEmpty();
	}
}
