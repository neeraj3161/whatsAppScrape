public class classTwo{
    public static void main(String[] args) {
        ThreadsInJava tj = new ThreadsInJava();
        Thread t = new Thread(tj);
        t.run();

        ThreadsInJava tj3 = new ThreadsInJava();
        Thread t3 = new Thread(tj3);
        t3.run();

        ThreadInJava2 tj2 = new ThreadInJava2();
        Thread t2 = new Thread(tj2);
        t2.start();
    }

}


class ThreadsInJava implements Runnable{

    @Override
    public void run() {
        System.out.println("This is thread running using implement menthod");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}

class ThreadInJava2 extends Thread{
    @Override
    public void run(){
        System.out.println("This is using extends");
    }
}