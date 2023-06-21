package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ReadThread implements Runnable {
    private final BlockingQueue<Future<String>> queue;
    private final ExecutorService executor;
    private final long taskMaxTime;

    public ReadThread(BlockingQueue<Future<String>> queue, int poolSize) {
        this.queue = queue;
        executor = Executors.newFixedThreadPool(poolSize);
        this.taskMaxTime = (long) Constants.TROTTLING * poolSize / Constants.MAX_HANDLED_NUMBERS;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            long number;
            //START
            System.out.println(new Date());

            while ((line = reader.readLine()) != null) {
                try {
                    number = Long.parseLong(line);
                } catch (NumberFormatException e) {
                    continue;
                }
                queue.put(executor.submit(new FactorialTask(number, taskMaxTime)));
            }
            queue.put(executor.submit(new FactorialTask(Constants.FLAG_END_OF_FILE, taskMaxTime)));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        executor.shutdown();
    }
}
