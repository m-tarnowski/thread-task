package com.example.lessontask3.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

@Getter
@Setter
@NoArgsConstructor
public class PrintingThread implements Runnable {

    private String word;
    private int amount;
    private long interval;

    private ThreadState state;

    public PrintingThread(String word, int amount, long interval) {
        this.word = word;
        this.amount = amount;
        this.interval = interval;
        this.state = ThreadState.NEW;
    }

    @Override
    public void run() {
        state = ThreadState.RUNNING;

        for (int i = 0; i < amount; i++) {
            System.out.println(word);
            try {
                TimeUnit.MILLISECONDS.sleep(interval);
            } catch (InterruptedException e) {
                throw new RuntimeException("Thread has been interrupted!");
            }
        }

        state = ThreadState.DONE;
    }
}
