package com.example.lessontask3.controller;

import com.example.lessontask3.Lessontask3Application;
import com.example.lessontask3.model.PrintingThread;
import com.example.lessontask3.model.ThreadState;
import com.example.lessontask3.model.command.CreateThreadCommand;
import com.example.lessontask3.respository.PrintingThreadRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Lessontask3Application.class)
@AutoConfigureMockMvc
class ThreadControllerTest {

    @Autowired
    private MockMvc postman;

    @Autowired
    private PrintingThreadRepository printingThreadRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCorrectCreateNewThread() throws Exception {
        CreateThreadCommand command = new CreateThreadCommand();
        command.setAmount(3);
        command.setInterval(7000);
        command.setWord("test1");

        String commandJson = objectMapper.writeValueAsString(command);

        postman.perform(post("/api/v1/threads")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commandJson))
                .andExpect(status().isCreated());

        Map<Thread, PrintingThread> allThreads = printingThreadRepository.getAllThreads();
        assertEquals(1, allThreads.size());
    }

    @Test
    void shouldGetThreadByIdWhenStateIsNew() throws Exception {
        PrintingThread printingThread = new PrintingThread("test2", 5, 2000);
        Thread thread = new Thread(printingThread);
        printingThreadRepository.saveThread(thread, printingThread);

        postman.perform(get("/api/v1/threads/{id}", thread.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.word").value("test2"))
                .andExpect(jsonPath("$.amount").value(5))
                .andExpect(jsonPath("$.interval").value(2000))
                .andExpect(jsonPath("$.state").value("NEW"));
    }

    @Test
    void shouldGetThreadByIdWhenStateIsRunning() throws Exception {
        PrintingThread printingThread = new PrintingThread("test3", 5, 2000);
        Thread thread = new Thread(printingThread);
        thread.start();
        printingThreadRepository.saveThread(thread, printingThread);

        postman.perform(get("/api/v1/threads/{id}", thread.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.word").value("test3"))
                .andExpect(jsonPath("$.amount").value(5))
                .andExpect(jsonPath("$.interval").value(2000))
                .andExpect(jsonPath("$.state").value("RUNNING"));
    }

    @Test
    void shouldGetThreadByIdWhenStateIsDone() throws Exception {
        PrintingThread printingThread = new PrintingThread("test4", 2, 200);
        Thread thread = new Thread(printingThread);
        thread.start();
        printingThreadRepository.saveThread(thread, printingThread);

        Thread.sleep(1500);

        postman.perform(get("/api/v1/threads/{id}", thread.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.word").value("test4"))
                .andExpect(jsonPath("$.amount").value(2))
                .andExpect(jsonPath("$.interval").value(200))
                .andExpect(jsonPath("$.state").value("DONE"));
    }

    @Test
    void shouldFailWhenGetThreadThenThreadNotExist() throws Exception {
        postman.perform(get("/api/v1/threads/{id}", 120)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.id").value(120))
                .andExpect(jsonPath("$.message").value("THREAD_NOT_FOUND"));
    }

    @Test
    void shouldFailWhenPutCancelledThreadThenThreadNotExist() throws Exception {
        postman.perform(put("/api/v1/threads/{id}/cancel", 120)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.id").value(120))
                .andExpect(jsonPath("$.message").value("THREAD_NOT_FOUND"));
    }

    @Test
    void shouldCorrectCancelledThread() throws Exception {
        PrintingThread printingThread = new PrintingThread("test5", 5, 5000);
        Thread thread = new Thread(printingThread);
        thread.start();
        printingThreadRepository.saveThread(thread, printingThread);

        postman.perform(get("/api/v1/threads/{id}", thread.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.word").value("test5"))
                .andExpect(jsonPath("$.amount").value(5))
                .andExpect(jsonPath("$.interval").value(5000))
                .andExpect(jsonPath("$.state").value("RUNNING"));

        postman.perform(put("/api/v1/threads/{id}/cancel", thread.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(ThreadState.CANCELLED, printingThread.getState());
    }

    @Test
    void shouldFailCancelledWhenThreadCancelBefore() throws Exception {
        PrintingThread printingThread = new PrintingThread("test6", 5, 5000);
        Thread thread = new Thread(printingThread);
        thread.start();
        printingThreadRepository.saveThread(thread, printingThread);

        postman.perform(put("/api/v1/threads/{id}/cancel", thread.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        postman.perform(put("/api/v1/threads/{id}/cancel", thread.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").value(thread.getId()))
                .andExpect(jsonPath("$.message").value("ALREADY_CANCELLED"));
    }

    @Test
    void shouldFailCancelledWhenThreadIsDone() throws Exception {
        PrintingThread printingThread = new PrintingThread("test7", 2, 200);
        Thread thread = new Thread(printingThread);
        thread.start();
        printingThreadRepository.saveThread(thread, printingThread);

        Thread.sleep(1500);

        postman.perform(get("/api/v1/threads/{id}", thread.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.word").value("test7"))
                .andExpect(jsonPath("$.amount").value(2))
                .andExpect(jsonPath("$.interval").value(200))
                .andExpect(jsonPath("$.state").value("DONE"));

        postman.perform(put("/api/v1/threads/{id}/cancel", thread.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").value(thread.getId()))
                .andExpect(jsonPath("$.message").value("DONE"));
    }

}