package org.example;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;

public class Main {
    private static final int capacity = 100;

    private static BlockingQueue<Future<String>> queue = new ArrayBlockingQueue<>(capacity);

    public static void main(String[] args) {
        int poolSize = 10; //By Default

        if (args.length == 1) {
            try {
                Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid parameter");
                return;
            }
        } else if (args.length > 1) {
            System.out.println("Too many parameters");
            return;
        }

        new Thread(new ReadThread(queue, poolSize)).start();
        new Thread(new OutputThread(queue)).start();
    }
}
