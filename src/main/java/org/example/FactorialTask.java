package org.example;

import java.math.BigInteger;
import java.util.Date;
import java.util.concurrent.Callable;

public class FactorialTask implements Callable<String> {
    private final long number;
    private final long taskMaxTime;

    public FactorialTask(long number, long taskMaxTime) {
        this.number = number;
        this.taskMaxTime = taskMaxTime;
    }

    @Override
    public String call() {
        if (number == Constants.FLAG_END_OF_FILE) {
            //FINISH
            System.out.println(new Date());
            return Constants.END_OF_FILE;
        }
        long startTime = System.currentTimeMillis();
        BigInteger factorial = calculateFactorial(BigInteger.valueOf(number));
        long elapsedTime = System.currentTimeMillis() - startTime;

        if (elapsedTime < taskMaxTime) {
            try {
                Thread.sleep(taskMaxTime - elapsedTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //System.out.println(Thread.currentThread().getName());
        return number + " = " + factorial;
    }

    private BigInteger calculateFactorial(BigInteger number) {
        if (number.compareTo(BigInteger.ZERO) <= 0) {
            return BigInteger.ONE;
        } else {
            return number.multiply(calculateFactorial(number.subtract(BigInteger.ONE)));
        }
    }
}
