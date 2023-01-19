package com.raamatukogu.repository;

import com.raamatukogu.model.Book;
import com.raamatukogu.model.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = "Select * FROM books b WHERE LOWER(book_name) LIKE %?1% OR LOWER(author) LIKE %?1% "
            + "OR CONCAT(b.isbn, '') LIKE %?1%", nativeQuery = true)
    List<Book> searchBooks( String keyword);
}
