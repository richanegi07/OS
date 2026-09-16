import java.util.Scanner;
import java.util.concurrent.Semaphore;

class Buffer {
    int[] buffer;
    int n;
    int in = 0, out = 0;

    Semaphore empty;
    Semaphore full;
    Semaphore mutex;

    Buffer(int n) {
        this.n = n;
        buffer = new int[n];
        empty = new Semaphore(n);
        full = new Semaphore(0);
        mutex = new Semaphore(1);
    }

    void produce(int item) throws InterruptedException {
        empty.acquire();
        mutex.acquire();
        buffer[in] = item;
        System.out.println("Produced: " + item);
        in = (in + 1) % n;
        mutex.release();
        full.release();
    }

    void consume() throws InterruptedException {
        full.acquire();
        mutex.acquire();
        int item = buffer[out];
        System.out.println("Consumed: " + item);
        out = (out + 1) % n;
        mutex.release();
        empty.release();
    }
}

class Producer extends Thread {
    Buffer b;
    Producer(Buffer b) {
        this.b = b;
    }
    public void run() {
        try {
            for (int i = 1; i <= b.n; i++) {
                b.produce(i);
            }
        } 
        catch (InterruptedException e) {
            System.out.println(e);
        }
    }
}

class Consumer extends Thread {
    Buffer b;
    Consumer(Buffer b) {
        this.b = b;
    }
    public void run() {
        try {
            for (int i = 1; i <= b.n; i++) {
                b.consume();
            }
        } 
        catch (InterruptedException e) {
            System.out.println(e);
        }
    }
}

public class ProducerConsumer {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter buffer size: ");
        int n = sc.nextInt();
        Buffer b = new Buffer(n);
        Producer p = new Producer(b);
        Consumer c = new Consumer(b);
        p.start();
        c.start();
    }
}