import java.util.Scanner;
class Buffer{
    int[] buffer;
    int n;
    int in=0,out=0;
    int count=0;

    Buffer(int n){
        this.n=n;
        buffer=new int[n];
    }

    synchronized void produce(int item) throws InterruptedException{
        while(count==n)
        wait();

        buffer[in]=item;
        System.out.println("Produced: " + item);
        in=(in+1)%n;
        count++;
        notify();
    }

    synchronized void consume() throws InterruptedException{
        while(count==0)
        wait();

        int item=buffer[out];
        System.out.println("Consumed: " + item);
        out=(out+1)%n;
        count--;
        notify();
    }
}

class Producer extends Thread{
    Buffer b;
    Producer(Buffer b){
        this.b=b;
    }
    public void run(){
        try{
            for(int i=1;i<=b.n;i++){
                b.produce(i);
            }
        }
            catch(InterruptedException e){
                System.out.println(e);
            }
        }
}

    class Consumer extends Thread{
        Buffer b;
        Consumer(Buffer b){
        this.b=b;
        }
        public void run(){
            try{
                for(int i=1;i<=b.n;i++){
                    b.consume();
                }
            }
            catch(InterruptedException e){
                System.out.println(e);
            }
        }
    }

public class ProducerConsumer{
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        System.out.print("Enter the buffer size: ");
        int n=sc.nextInt();
        Buffer b=new Buffer(n);
        Producer p=new Producer(b);
        Consumer c=new Consumer(b);

        p.start();
        c.start();
    }
}

