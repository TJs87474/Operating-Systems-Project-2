import java.io.*;
import java.util.*;

class ProcessThread extends Thread {
    int pid, burstTime;

    public ProcessThread(int pid, int burstTime) {
        this.pid = pid;
        this.burstTime = burstTime;
    }

    public void run() {
        System.out.println("Process " + pid + " started.");
        try {
            Thread.sleep(burstTime * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Process " + pid + " finished.");
    }
}

public class ProcessSimulator {
    public static void main(String[] args) {
        String fileName = "processes.txt";
        List<ProcessThread> threads = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                int pid = Integer.parseInt(parts[0]);
                int arrivalTime = Integer.parseInt(parts[1]);
                int burstTime = Integer.parseInt(parts[2]);

                ProcessThread pt = new ProcessThread(pid, burstTime);
                threads.add(pt);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Start all threads
        for (ProcessThread pt : threads) {
            pt.start();
        }

        // Wait for all threads to finish
        for (ProcessThread pt : threads) {
            try {
                pt.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("All processes finished.");
    }
}
