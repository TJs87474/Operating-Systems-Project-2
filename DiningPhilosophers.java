import java.util.concurrent.locks.ReentrantLock;

class Philosopher extends Thread {
    private int id;
    private ReentrantLock leftFork;
    private ReentrantLock rightFork;

    public Philosopher(int id, ReentrantLock leftFork, ReentrantLock rightFork) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    private void think() {
        System.out.println("[Philosopher " + id + "] Thinking...");
        try { Thread.sleep((int)(Math.random() * 1000)); } catch (InterruptedException e) {}
    }

    private void eat() {
        System.out.println("[Philosopher " + id + "] Eating...");
        try { Thread.sleep((int)(Math.random() * 1000)); } catch (InterruptedException e) {}
    }

    public void run() {
        int eatCount = 0;
        while (eatCount < 3) {  // let each philosopher eat 3 times
            think();

            ReentrantLock firstFork = leftFork;
            ReentrantLock secondFork = rightFork;
            int firstForkNum = id;
            int secondForkNum = (id + 1) % 5;

            if (firstFork.hashCode() > secondFork.hashCode()) {
                firstFork = rightFork;
                secondFork = leftFork;
                firstForkNum = (id + 1) % 5;
                secondForkNum = id;
            }

            System.out.println("[Philosopher " + id + "] Waiting for forks...");

            firstFork.lock();
            System.out.println("[Philosopher " + id + "] Picked up fork " + firstForkNum);
            secondFork.lock();
            System.out.println("[Philosopher " + id + "] Picked up fork " + secondForkNum);

            try {
                eat();
                eatCount++;
            } finally {
                secondFork.unlock();
                firstFork.unlock();
                System.out.println("[Philosopher " + id + "] Released forks");
            }
        }
        System.out.println("[Philosopher " + id + "] Done eating.");
    }
}

public class DiningPhilosophers {
    public static void main(String[] args) {
        ReentrantLock[] forks = new ReentrantLock[5];
        Philosopher[] philosophers = new Philosopher[5];

        for (int i = 0; i < 5; i++) {
            forks[i] = new ReentrantLock();
        }

        for (int i = 0; i < 5; i++) {
            ReentrantLock leftFork = forks[i];
            ReentrantLock rightFork = forks[(i + 1) % 5];
            philosophers[i] = new Philosopher(i, leftFork, rightFork);
            philosophers[i].start();
        }

        // Wait for all philosophers to finish
        for (int i = 0; i < 5; i++) {
            try {
                philosophers[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("All philosophers are done.");
    }
}
