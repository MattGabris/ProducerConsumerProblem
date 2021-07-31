/* CS471 Project Problem 1: Producer-Consumer Project 
 * @author Matthew Gabris
 * @date 27 July 2021
 * 
 * git repo : https://github.com/MattGabris/ProducerConsumerProblem.git
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.util.Scanner;

import java.util.concurrent.Semaphore;

/**
 *
 * @author Phoenix
 * Run until there have been 10,000 items entered -> Tracked by totalInserted
 */
public class PRODCONS {
	//Global variables
	static int MAXBUFFSIZE;
	static int totalItems;
	static int maxProducers;
	static int maxConsumers;
	static ArrayList<BufferItem> sharedBuffer = new ArrayList<BufferItem>();
	
	/**
	 * Default constructor
	 */
	public PRODCONS() {
		totalItems = sharedBuffer.size();
	}
	
	/**
	 * method to insert item into buffer 
	 * @param item
	 * @return 	true if success
	 * 			false if failure
	 */
	public static boolean insertItem(BufferItem item) {
		boolean success = true;
		//semaphore/mutex lock here
		if (sharedBuffer.size() != MAXBUFFSIZE) {
			try {
				sharedBuffer.add(item);
			} catch (Exception e) {
				System.out.println("There was an error inserting the item");
				return !success;
			}
		}
		//semaphore/mutex unlock here
		totalItems++;
		return success;
	}
	
	/**
	 * method to remove item from buffer 
	 * @param item
	 * @return 	true if success
	 * 			false if failure
	 */
	public static boolean removeItem() {
		return false;
	}
	
	/**
	 * // example on how to access items
	 * PRODCONS test = new PRODCONS();
	 * BufferItem BuffA = test.sharedBuffer.get(0);
	 * System.out.println(BuffA.DATE);
	 * // example on creating buffer items
	 * System.out.println("Creating new Buffer value; enter date, store id, register, sale amount");
	 * Scanner a = new Scanner(System.in);
	 * String date = a.next();
	 * int storeid = a.nextInt();
	 * int register = a.nextInt();
	 * float saleamount = a.nextFloat();
	 * BufferItem item = new BufferItem(date, storeid, register, saleamount);
	 * insertItem(item);
	 */
	public static void main(String[] args) {
		System.out.println("How many producers are there?");
		Scanner a = new Scanner(System.in);
		maxProducers = a.nextInt();
		System.out.println(maxProducers + " producers");
		System.out.println("How many consumers are there?");
		maxConsumers = a.nextInt();
		System.out.println(maxProducers + " consumers");
		System.out.println("Max buffer size?");
		MAXBUFFSIZE = a.nextInt();
		if (MAXBUFFSIZE > 10000)
			MAXBUFFSIZE = 10000;
		System.out.println(maxProducers + " buff size");
		Producer prod = new Producer(sharedBuffer);
		for(int i = 0; i < maxProducers; i++)
			prod.start();
		
		
	}

}


/**
 * Buffer class
 * Simple object class to house the buffer information in the producer-consumer project
 * 
 * Date stored in DD/MM/20
 * Store IDs are in the 1 to p range (where p is the number of producers)
 * The register numbers range from 1-6 for any store
 * Sale amount ranges from 0.5-999.99
 * 
 */
class BufferItem {
	// if these variables are made private, will need getter and setter methods; i think these are global because this is a shared buffer between the consumer and the producer???
	String DATE;
	int STOREID;
	int REGISTER;
	float SALEAMT;
	
	// default constructor
	public BufferItem() {
		String DATE 	= "DD/MM/YY";
		int STOREID 	= -1;
		int REGISTER 	= -1;
		float SALEAMT	= -1.0f; // f needs to be there when saving any float data
	}
	// overloaded constructor
	public BufferItem(String DATE, int STOREID, int REGISTER, float SALEAMT) {
		this.DATE 		= DATE;
		this.STOREID 	= STOREID;
		this.REGISTER 	= REGISTER;
		this.SALEAMT 	= SALEAMT;
	}
	
	public static BufferItem generateBufferItem() {
		int randDay = (int)Math.floor(Math.random()*(30)+1);
		int randMonth = (int)Math.floor(Math.random()*(12)+1);
		String dayStr = randDay + "";
		String monthStr = randMonth + "";
		if (randDay <= 9)
			dayStr = "0" + randDay;
		if (randMonth <= 9)
			monthStr = "0" + randMonth;
		String date = dayStr + "/" + monthStr + "/20";
		
		int max = 0; // TODO
		// max references to the scanner input of the total possible producers when the program is first run
		int storeid = (int)Math.floor(Math.random()*(max)+1);
		
		int register = (int)Math.floor(Math.random()*(6)+1);
			
		Random r = new Random();
	    float saleamt = (float) ((r.nextInt((int)((999.99-0.5)*10+1))+0.5*10) / 100.0);
	
		BufferItem item = new BufferItem(date, storeid, register, saleamt);
		return item;
	}
}

class Producer extends Thread{      
	private ArrayList<BufferItem> buffer;
	public Producer(ArrayList<BufferItem> buffer) {
		this.buffer = buffer;
	}
	
	@Override
	public void run() {
		int counter = 0;
		PRODCONS proc = new PRODCONS();
		while (counter < 10000) {
			while (buffer.size() < proc.MAXBUFFSIZE) { // TODO here there is an error thrown because there is no mutex lock put into place when the max buffer size has been reached; has to do with number of producers available (line 100)
	//			while(mutex unlock) {
				try {
					Thread.sleep((int)Math.floor(Math.random()*(6)+1));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Generating new buffer item . . . "  + counter);
				BufferItem newItem = new BufferItem().generateBufferItem();
				proc.insertItem(newItem);
				System.out.println("Created new buffer item number: " + counter);
				counter++;
			}
			
		}
			
		// This is where new buffer items will randomly be created with sleeping the thread
		
	}	
}

class Consumer extends Thread {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
}













//class Producer implements Runnable {
//    private List<String> buffer;
//
//    public Producer(List<String> buffer) {
//        this.buffer = buffer;
//    }
//
//
//    @Override
//    public void run() {
//        String numbers[] = {"1", "2", "3"};
//        for (String number : numbers) {
//            synchronized (buffer) {
//                buffer.add(number);
//                try {
//                    Random random = new Random();
//                    Thread.sleep(random.nextInt(2000));
//                } catch (InterruptedException e) {
//                    System.out.println(Thread.currentThread().getName() + " interrupted.");
//                }
//            }
//            System.out.println(Thread.currentThread().getName() + " added " + number);
//        }
//        System.out.println(Thread.currentThread().getName() + " added " + PRODCONS.EOF);
//        synchronized (buffer) {
//            buffer.add(PRODCONS.EOF);
//        }
//    }
//}
//
//class Consumer implements Runnable {
//    private List<String> buffer;
//
//    public Consumer(List<String> buffer) {
//        this.buffer = buffer;
//    }
//
//    @Override
//    public void run() {
//
//        while (true) {
//            synchronized (buffer) {
//                if (buffer.isEmpty()) {
//                    continue;
//                }
//                if (buffer.get(0).equals(PRODCONS.EOF)) {
//                    System.out.println(Thread.currentThread().getName() + " exiting.");
//                    break;
//                } else {
//                    System.out.println(Thread.currentThread().getName() + " removed " + buffer.remove(0));
//                    try {
//                        Random random = new Random();
//                        Thread.sleep(random.nextInt(2000));
//                    } catch (InterruptedException e) {
//                        System.out.println(Thread.currentThread().getName() + " interrupted.");
//                    }
//                }
//            }
//        }
//    }
//}
//
//public class PRODCONS {
//    public static final String EOF = "EOF";
//    public static void main(String[] args) {
//        List<String> buffer = new ArrayList<>();
//
//        Thread producerThread = new Thread(new Producer(buffer));
//        producerThread.setName("producerThread");
//
//        Thread consumerThread1 = new Thread(new Consumer(buffer));
//        consumerThread1.setName("consumerThread1");
//
//        Thread consumerThread2 = new Thread(new Consumer(buffer));
//        consumerThread2.setName("consumerThread2");
//
//        producerThread.start();
//        consumerThread1.start();
//        consumerThread2.start();
//    }
//}















































/**
//Java implementation of a producer and consumer
//that use semaphores to control synchronization.

import java.util.concurrent.Semaphore;

class Q {
 // an item
 int item;

 // semCon initialized with 0 permits
 // to ensure put() executes first
 static Semaphore semCon = new Semaphore(0);

 static Semaphore semProd = new Semaphore(1);

 // to get an item from buffer
 void get()
 {
     try {
         // Before consumer can consume an item,
         // it must acquire a permit from semCon
         semCon.acquire();
     }
     catch (InterruptedException e) {
         System.out.println("InterruptedException caught");
     }

     // consumer consuming an item
     System.out.println("Consumer consumed item : " + item);

     // After consumer consumes the item,
     // it releases semProd to notify producer
     semProd.release();
 }

 // to put an item in buffer
 void put(int item)
 {
     try {
         // Before producer can produce an item,
         // it must acquire a permit from semProd
         semProd.acquire();
     }
     catch (InterruptedException e) {
         System.out.println("InterruptedException caught");
     }

     // producer producing an item
     this.item = item;

     System.out.println("Producer produced item : " + item);

     // After producer produces the item,
     // it releases semCon to notify consumer
     semCon.release();
 }
}

//Producer class
class Producer implements Runnable {
 Q q;
 Producer(Q q)
 {
     this.q = q;
     new Thread(this, "Producer").start();
 }

 public void run()
 {
     for (int i = 0; i < 5; i++)
         // producer put items
         q.put(i);
 }
}

//Consumer class
class Consumer implements Runnable {
 Q q;
 Consumer(Q q)
 {
     this.q = q;
     new Thread(this, "Consumer").start();
 }

 public void run()
 {
     for (int i = 0; i < 5; i++)
         // consumer get items
         q.get();
 }
}

//Driver class
class PC {
 public static void main(String args[])
 {
     // creating buffer queue
     Q q = new Q();

     // starting consumer thread
     new Consumer(q);

     // starting producer thread
     new Producer(q);
 }
}
*/