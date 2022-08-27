package com.example.lessontask3.controller;


import com.example.lessontask3.model.command.CreateThreadCommand;
import com.example.lessontask3.service.ThreadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/threads")
@RequiredArgsConstructor
public class ThreadController {

    private final ThreadService threadService;

    @PostMapping
    public ResponseEntity addThread(@RequestBody @Valid CreateThreadCommand command) {
        return ResponseEntity.status(HttpStatus.CREATED).body(threadService.createThread(command));
    }

    @GetMapping("/{id}")
    public ResponseEntity findOneThread(@PathVariable long id) {
        return ResponseEntity.ok(threadService.findPrintingThread(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity cancelThread(@PathVariable long id) {
        return ResponseEntity.ok(threadService.cancelThread(id));
    }
}
