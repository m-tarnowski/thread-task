package com.example.lessontask3.respository;

import com.example.lessontask3.model.PrintingThread;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class PrintingThreadRepository {

    private final Map<Thread, PrintingThread> allThreads = Collections.synchronizedMap(new HashMap<>());

    public void saveThread(Thread thread, PrintingThread printingThread) {
        allThreads.put(thread, printingThread);
    }

    public Optional<PrintingThread> getValueById(long id) {
        return allThreads.entrySet().stream()
                .filter(t -> id == t.getKey().getId())
                .map(Map.Entry::getValue)
                .findFirst();
    }

    public Optional<Thread> getKeyById(long id) {
        return allThreads.keySet().stream()
                .filter(pT -> id == pT.getId())
                .findFirst();
    }

    public Map<Thread, PrintingThread> getAllThreads() {
        return allThreads;
    }
}
