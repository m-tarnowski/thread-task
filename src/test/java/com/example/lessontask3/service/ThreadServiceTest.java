package com.example.lessontask3.service;

import com.example.lessontask3.exceptions.PrintingThreadNotFoundException;
import com.example.lessontask3.model.PrintingThread;
import com.example.lessontask3.model.command.CreateThreadCommand;
import com.example.lessontask3.respository.PrintingThreadRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ThreadServiceTest {

    private PrintingThreadRepository printingThreadRepository;

    private ThreadService underTest;

    @BeforeEach
    void init() {
        printingThreadRepository = mock(PrintingThreadRepository.class);
        underTest = new ThreadService(printingThreadRepository);
    }

    @Test
    void shouldCorrectCreateThread() {
        CreateThreadCommand command = new CreateThreadCommand();
        command.setAmount(3);
        command.setInterval(7000);
        command.setWord("test1");

        underTest.createThread(command);

        verify(printingThreadRepository, times(1)).saveThread(any(), any());
    }

    @Test
    void shouldCorrectGetPrintingThreadById() {
        PrintingThread printingThread = new PrintingThread();
        when(printingThreadRepository.getValueById(50)).thenReturn(Optional.of(printingThread));

        PrintingThread expected = underTest.findPrintingThread(50);

        Assertions.assertThat(expected).isSameAs(printingThread);
        verify(printingThreadRepository).getValueById(50);
    }

    @Test
    void shouldPrintingThreadNotFoundExceptionWhenGetThread() {
        PrintingThreadNotFoundException thrown = assertThrows(
                PrintingThreadNotFoundException.class,
                () -> underTest.findPrintingThread(52));

        assertEquals(thrown.getId(), 52);
        assertEquals(thrown.getMessage(), "THREAD_NOT_FOUND");
    }
}