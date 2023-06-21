package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class OutputThread implements Runnable {
    private final BlockingQueue<Future<String>> queue;

    public OutputThread(BlockingQueue<Future<String>> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
            String resultString;

            while (!Thread.currentThread().isInterrupted()) {
                Future<String> processedData = queue.take();

                resultString = processedData.get();
                if (resultString.equals(Constants.END_OF_FILE)) {
                    Thread.currentThread().interrupt();
                    System.out.println("END");
                    continue;
                }
                writer.write(resultString);
                writer.newLine();
                writer.flush();

                //System.out.println(processedData.get());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
