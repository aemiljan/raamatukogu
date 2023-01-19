package com.raamatukogu.model.entities;

import com.raamatukogu.model.Book;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PublicBook {
    private long id;
    private String name;
    private String author;
    private String isbn;
    private long booksAvailable;
    private LocalDate publicationDate;
    private String location;
    private long maxRentalLengthInWeeks;

    public PublicBook(Book book, int maxRentalLengthInWeeks) {
        this.id = book.getId();
        this.name = book.getName();
        this.author = book.getAuthor();
        this.isbn = book.getIsbn();
        this.booksAvailable = book.getBooksAvailable();
        this.publicationDate = book.getPublicationDate();
        this.location = book.getLocation();
        this.maxRentalLengthInWeeks = maxRentalLengthInWeeks;
    }
}
