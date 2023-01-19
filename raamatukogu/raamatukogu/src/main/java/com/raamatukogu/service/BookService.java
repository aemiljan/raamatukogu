package com.raamatukogu.service;

import com.raamatukogu.exception.NotEnoughUnitsException;
import com.raamatukogu.exception.ResourceNotFoundException;
import com.raamatukogu.model.Book;
import com.raamatukogu.model.Reader;
import com.raamatukogu.model.entities.PublicBook;
import com.raamatukogu.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private static final Logger logger = Logger.getLogger(BookService.class.getName());

    public BookService(BookRepository bookRepository){
        this.bookRepository =bookRepository;
    }
    public Book saveBook(Book book){
        logger.info("Saving new book.");
        return bookRepository.save(book);
    }
    public List<Book> getAllBooks() {
        logger.info("Fetching list of all books.");
        return bookRepository.findAll();
    }
    public Book getBookById(long id){
        logger.info("Searching for a book with id:" + id);
        return bookRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Book", "Id", id));
    }
    public Book updateBook(Book book, long id){
        logger.info("Updating book with id:" + id);
        //check if book with given id exists
        Book existingBook = bookRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Book", "Id", id));

        existingBook.setName(book.getName());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setBooksAvailable(book.getBooksAvailable());
        existingBook.setPublicationDate(book.getPublicationDate());
        existingBook.setIsbn(book.getIsbn());
        existingBook.setLocation(book.getLocation());

        //save new book info to DB
        bookRepository.save(existingBook);
        return existingBook;
    }

    public void deleteBook(long id){
        //check if book exists
        logger.info("Deleting book with id:" + id);
        bookRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Book", "Id", id));
        bookRepository.deleteById(id);
        logger.info("Book deleted.");
    }
    // method used when creating a new rental to subtract the book from available books
    public void outgoingBook(long id) {
        Book book = getBookById(id);
        if(book.getBooksAvailable() < 1){
            throw new NotEnoughUnitsException(id);
        }
        book.setBooksAvailable(book.getBooksAvailable() - 1);
        bookRepository.save(book);
    }
    // method used when returning a book to add the book to available books
    public void incomingBook(long id){
        Book book = getBookById(id);
        book.setBooksAvailable(book.getBooksAvailable() + 1);
        bookRepository.save(book);
    }

    public List<PublicBook> getPublicView() {
        logger.info("Generating public view of books in library.");
        List<Book> allBooks = bookRepository.findAll();
        List<PublicBook> publicBooks = new ArrayList<>();
        for (Book book : allBooks){
            if(book.getBooksAvailable() < 5){
                //deadline 1 week from now
                publicBooks.add(new PublicBook(book, 1));
            } else if (ChronoUnit.MONTHS.between(book.getPublicationDate(), LocalDate.now()) < 3) {
                //deadline 1 week from now
                publicBooks.add(new PublicBook(book, 1));
            } else {
                //deadline 4 weeks from now
                publicBooks.add(new PublicBook(book, 4));
            }
        }
        return publicBooks;
    }

    public List<Book> searchBooks(String keyword) {
        return bookRepository.searchBooks(keyword.toLowerCase());
    }
}
