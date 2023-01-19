package com.raamatukogu.model.entities;

import com.raamatukogu.model.Rental;
import lombok.*;

import java.time.LocalDate;

@Data
public class OverdueRental {

    private long id;
    private long reader_id;
    private String renterName;
    private long book_id;
    private String bookName;
    private String bookAuthor;
    private LocalDate deadline;
    private long overdueDays;

    public OverdueRental(Rental rental, String bookName, String bookAuthor, long overdueDays) {
        this.id = rental.getId();
        this.reader_id = rental.getReader_id();
        this.renterName = rental.getRenterName();
        this.book_id = rental.getBook_id();
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.deadline = rental.getDeadline();
        this.overdueDays = overdueDays;
    }
}
