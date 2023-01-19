package com.raamatukogu.controller;

import com.raamatukogu.model.Book;
import com.raamatukogu.model.entities.PublicBook;
import com.raamatukogu.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;


     public BookController(BookService bookService){
         super();
         this.bookService = bookService;
     }

    // REST api raamatute lisamiseks
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Book> saveBook(@RequestBody Book book){
        return new ResponseEntity<Book>(bookService.saveBook(book), HttpStatus.CREATED);
    }
    //REST api kõikide raamatute kuvamiseks
    @GetMapping
    public List<Book> getAllBooks(){
         return bookService.getAllBooks();
    }
    //REST api raamatu leidmiseks id põhjal
    @GetMapping("{id}")
    public ResponseEntity<Book> getBookById(@PathVariable("id") long bookId){
        return new ResponseEntity<Book>(bookService.getBookById(bookId), HttpStatus.OK);
    }
    //REST api raamatu andmete uuendamiseks
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<Book> updateBook(@PathVariable("id") long id, @RequestBody Book book){
        return new ResponseEntity<Book>(bookService.updateBook(book, id), HttpStatus.OK);
    }

    //REST api raamatu kustutamiseks
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteBook(@PathVariable("id") long id){
         bookService.deleteBook(id);
         return new ResponseEntity<String>("Raamat kustutati süsteemist edukalt.", HttpStatus.OK);
    }

    // avalik vaade raamatukogu raamatutest
    @PreAuthorize("isAnonymous()")
    @GetMapping("/public")
    public List<PublicBook> getPublicView() {
         return bookService.getPublicView();
    }

    //REST api raamatu otsimiseks
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search/{keyword}")
    public List<Book> searchBooks(@PathVariable("keyword") String keyword){
        return bookService.searchBooks(keyword);
    }
}
