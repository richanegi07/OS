import java.util.Scanner;
class Resource{
    int data = 0,readers = 0;
    boolean writing = false;

    synchronized void read() throws InterruptedException{
        while(writing){
            wait();
        }
        readers++;
        System.out.println("Reader is reading: " + data);
        Thread.sleep(500);
        readers--;
        if(readers == 0)
        notifyAll();
    }

    synchronized void write(int value) throws InterruptedException{
        while(writing || readers > 0){
            wait();
        }
        writing = true;
        data = value;
        System.out.println("Writer wrote: " + data);
        Thread.sleep(500);
        writing = false;
        notifyAll();
    }
}

class Reader extends Thread{
    Resource r;
    int n;
    Reader(Resource r,int n){
        this.r=r;
        this.n=n;
    }
    public void run(){
        try{
            for(int i=1;i<=n;i++)
            r.read();
        }
        catch(InterruptedException e){
        System.out.println(e);
        }
    }
}

class Writer extends Thread{
    Resource r;
    int n;
    Writer(Resource r,int n){
        this.r=r;
        this.n=n;
    }
    public void run(){
        try{
            for(int i=1;i<=n;i++)
            r.write(i);
        }
        catch(InterruptedException e){
        System.out.println(e);
        }
    }
}

public class ReaderWriter{
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        System.out.print("Enter number of values: ");
        int n=sc.nextInt();
        Resource r = new Resource();
        Reader r1 = new Reader(r,n);
        Reader r2 = new Reader(r,n);
        Writer w1 = new Writer(r,n);
        r1.start();
        r2.start();
        w1.start();
    }
}