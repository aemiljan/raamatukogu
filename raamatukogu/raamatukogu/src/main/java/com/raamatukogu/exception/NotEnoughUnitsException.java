package com.raamatukogu.exception;

import java.io.Serial;

public class NotEnoughUnitsException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;
    private final long id;

    public NotEnoughUnitsException(long id){
        super(String.format("Book with id: %s does not have enough units available.", id));
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
