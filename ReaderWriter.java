import java.util.Scanner;
import java.util.concurrent.Semaphore;

class Resource {
    int data = 0, readCount = 0;
    Semaphore mutex = new Semaphore(1);
    Semaphore write = new Semaphore(1);

    void read(int id) throws InterruptedException {
        mutex.acquire();
        readCount++;
        if (readCount == 1) {
            write.acquire();
        }
        mutex.release();
        System.out.println("Reader " + id + " is reading: " + data);
        Thread.sleep(500);
        mutex.acquire();
        readCount--;
        if (readCount == 0) {
            write.release();
        }
        mutex.release();
    }

    void write(int id, int value) throws InterruptedException {
        write.acquire();
        data = value;
        System.out.println("Writer " + id + " is writing: " + data);
        Thread.sleep(500);
        write.release();
    }
}

class Reader extends Thread {
    Resource r;
    int id;
    Reader(Resource r, int id) {
        this.r = r;
        this.id = id;
    }
    public void run() {
        try {
            r.read(id);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }
}

class Writer extends Thread {
    Resource r;
    int id;
    Writer(Resource r, int id) {
        this.r = r;
        this.id = id;
    }
    public void run() {
        try {
            r.write(id, id);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }
}

public class ReaderWriter {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of readers: ");
        int nr = sc.nextInt();
        System.out.print("Enter number of writers: ");
        int nw = sc.nextInt();
        Resource r = new Resource();
        for (int i = 1; i <= nr; i++) {
            Reader reader = new Reader(r, i);
            reader.start();
        }
        for (int i = 1; i <= nw; i++) {
            Writer writer = new Writer(r, i);
            writer.start();
        }
    }
}