package com.example.lessontask3.service;

import com.example.lessontask3.exceptions.PrintingThreadNotFoundException;
import com.example.lessontask3.exceptions.ThreadCannotCanceledException;
import com.example.lessontask3.model.PrintingThread;
import com.example.lessontask3.model.ThreadState;
import com.example.lessontask3.model.command.CreateThreadCommand;
import com.example.lessontask3.respository.PrintingThreadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ThreadService {

    private final PrintingThreadRepository printingThreadRepository;

    public long createThread(CreateThreadCommand command) {
        PrintingThread printingThread = createPrintingThread(command);
        Thread newPrintingThread = new Thread(printingThread);
        newPrintingThread.start();
        printingThreadRepository.saveThread(newPrintingThread, printingThread);

        return newPrintingThread.getId();
    }

    public PrintingThread findPrintingThread(long id) {
        return printingThreadRepository.getValueById(id).orElseThrow(() -> new PrintingThreadNotFoundException(id, "THREAD_NOT_FOUND"));
    }

    public long cancelThread(long id) {
        PrintingThread printingThread = findPrintingThread(id);

        if (printingThread.getState() == ThreadState.CANCELLED) {
            throw new ThreadCannotCanceledException(id, ThreadState.ALREADY_CANCELLED.toString());
        } else if (printingThread.getState() == ThreadState.DONE) {
            throw new ThreadCannotCanceledException(id, ThreadState.DONE.toString());
        }

        Thread thread = printingThreadRepository.getKeyById(id).orElseThrow(() -> new PrintingThreadNotFoundException(id, "THREAD_NOT_FOUND"));

        thread.interrupt();
        printingThread.setState(ThreadState.CANCELLED);

        return thread.getId();
    }

    private PrintingThread createPrintingThread(CreateThreadCommand command) {
        return new PrintingThread(command.getWord(), command.getAmount(), command.getInterval());
    }

}
