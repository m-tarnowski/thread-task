package com.example.lessontask3.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PrintingThreadNotFoundException extends RuntimeException {

    private final long id;
    private final String message;

}
