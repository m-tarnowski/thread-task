package com.example.lessontask3.model.command;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateThreadCommand {

    @NotNull(message = "WORD_NOT_NULL")
    private String word;
    @NotNull(message = "AMOUNT_NOT_NULL")
    private int amount;
    @NotNull(message = "INTERVAL_NOT_NULL")
    private long interval;

}
