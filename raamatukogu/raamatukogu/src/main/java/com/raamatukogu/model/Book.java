package com.raamatukogu.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="book_name", nullable = false)
    private String name;

    @Column(name = "author")
    private String author;

    @Column(name="isbn")
    private String isbn;

    @Column(name="books_available")
    private long booksAvailable;

    @Column(name="publication_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate publicationDate;

    @Column(name = "location")
    private String location;
}
