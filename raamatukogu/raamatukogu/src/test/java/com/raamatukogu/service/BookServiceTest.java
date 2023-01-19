package com.raamatukogu.service;

import com.raamatukogu.model.Book;
import com.raamatukogu.model.entities.OverdueRental;
import com.raamatukogu.model.entities.PublicBook;
import com.raamatukogu.repository.BookRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookService bookService;
    @Captor
    ArgumentCaptor<Book> bookCaptor;

    //get all books
    @Test
    public void getBooks_shouldReturnAllBooks() throws Exception{
        Book book1 = Book.builder()
                .id(1L)
                .name("Atomic Habits")
                .author("James Clear")
                .isbn("9781847941831")
                .booksAvailable(6)
                .publicationDate(LocalDate.parse("2018-11-27"))
                .location("G6")
                .build();
        Book book2 = Book.builder()
                .id(2L)
                .name("Rich Dad Poor Dad")
                .author("Robert T. Kiyosaki")
                .isbn("9781612680194")
                .booksAvailable(3)
                .publicationDate(LocalDate.parse("2018-10-31"))
                .location("E5")
                .build();
        Book book3 = Book.builder()
                .id(3L)
                .name("The Expectation Effect")
                .author("David Robson")
                .isbn("9781838853303")
                .booksAvailable(8)
                .publicationDate(LocalDate.parse("2022-12-29"))
                .location("A3")
                .build();

        when(bookRepository.findAll()).thenReturn(List.of(book1, book2, book3));

        List<Book> books = bookService.getAllBooks();
        assertEquals(3, books.size());
        assertEquals(1L, books.get(0).getId());
        assertEquals("Atomic Habits", books.get(0).getName());
        assertEquals("James Clear", books.get(0).getAuthor());
        assertEquals("9781847941831", books.get(0).getIsbn());
        assertEquals(6,books.get(0).getBooksAvailable());
        assertEquals(LocalDate.parse("2018-11-27"),books.get(0).getPublicationDate());
        assertEquals("G6", books.get(0).getLocation());
        assertEquals(2L, books.get(1).getId());
        assertEquals("Rich Dad Poor Dad", books.get(1).getName());
        assertEquals("Robert T. Kiyosaki", books.get(1).getAuthor());
        assertEquals("9781612680194", books.get(1).getIsbn());
        assertEquals(3,books.get(1).getBooksAvailable());
        assertEquals(LocalDate.parse("2018-10-31"),books.get(1).getPublicationDate());
        assertEquals("E5", books.get(1).getLocation());
        assertEquals(3L, books.get(2).getId());
        assertEquals("The Expectation Effect", books.get(2).getName());
        assertEquals("David Robson", books.get(2).getAuthor());
        assertEquals("9781838853303", books.get(2).getIsbn());
        assertEquals(8,books.get(2).getBooksAvailable());
        assertEquals(LocalDate.parse("2022-12-29"), books.get(2).getPublicationDate());
        assertEquals("A3", books.get(2).getLocation());
    }
    //get book with correct id
    @Test
    public void getBook_shouldReturnBookWithMatchingId() throws Exception{
        when(bookRepository.findById(2L)).thenReturn(Optional.ofNullable(Book.builder()
                .id(2L)
                .name("Rich Dad Poor Dad")
                .author("Robert T. Kiyosaki")
                .isbn("9781612680194")
                .booksAvailable(3)
                .publicationDate(LocalDate.parse("2018-10-31"))
                .location("E5")
                .build()));

        Book book = bookService.getBookById(2);
        assertEquals(2, book.getId());
        assertEquals("Rich Dad Poor Dad", book.getName());
        assertEquals("Robert T. Kiyosaki", book.getAuthor());
        assertEquals("9781612680194", book.getIsbn());
        assertEquals(3,book.getBooksAvailable());
        assertEquals(LocalDate.parse("2018-10-31"), book.getPublicationDate());
        assertEquals("E5", book.getLocation());
    }
    //adding book
    @Test
    public void addBook_shouldAddBookToRepository() throws Exception{
        Book book = Book.builder()
                .id(4L)
                .name("The Catcher in the Rye")
                .author("J. D. Salinger")
                .isbn("9781612680194")
                .booksAvailable(5)
                .publicationDate(LocalDate.parse("2005-03-18"))
                .location("E2")
                .build();

        given(bookRepository.save(book)).willAnswer(invocation -> invocation.getArgument(0));

        Book newBook = bookService.saveBook(book);

        assertThat(newBook).isNotNull();
        assertEquals(4L, newBook.getId());
        assertEquals("The Catcher in the Rye", newBook.getName());
        assertEquals("J. D. Salinger", newBook.getAuthor());
        assertEquals("9781612680194", newBook.getIsbn());
        assertEquals(5, newBook.getBooksAvailable());
        assertEquals(LocalDate.parse("2005-03-18"), newBook.getPublicationDate());
        assertEquals("E2", newBook.getLocation());
        verify(bookRepository).save(book);
    }
    //update
    @Test
    public void updateBook_shouldUpdateBookInRepository() throws Exception{
        Book book = Book.builder()
                .id(2L)
                .name("Rich Dad Poor Dad")
                .author("Robert Toru Kiyosaki")
                .isbn("9781612680194")
                .booksAvailable(3)
                .publicationDate(LocalDate.parse("2018-10-31"))
                .location("E5")
                .build();

        when(bookRepository.findById(2L)).thenReturn(Optional.ofNullable(Book.builder()
                .id(2L)
                .name("Rich Dad Poor Dad")
                .author("Robert T. Kiyosaki")
                .isbn("9781612680194")
                .booksAvailable(3)
                .publicationDate(LocalDate.parse("2018-10-31"))
                .location("E5")
                .build()));

        Book updatedBook = bookService.updateBook( book, 2);
        assertEquals("Robert Toru Kiyosaki", updatedBook.getAuthor());
    }
    //delete
    @Test
    public void deleteBook_shouldDeleteBookInRepository() throws Exception{
        when(bookRepository.findById(2L)).thenReturn(Optional.ofNullable(Book.builder()
                .id(2L)
                .name("Rich Dad Poor Dad")
                .author("Robert T. Kiyosaki")
                .isbn("9781612680194")
                .booksAvailable(3)
                .publicationDate(LocalDate.parse("2018-10-31"))
                .location("E5")
                .build()));

        bookService.deleteBook(2);
        Assertions.assertThat(bookRepository.findAll()).hasSize(0);
    }

    @Test
    public void outgoingBook_shouldDeductBookFromAvailableBooks() throws Exception{
        when(bookRepository.findById(2L)).thenReturn(Optional.ofNullable(Book.builder()
                .id(2L)
                .name("Rich Dad Poor Dad")
                .author("Robert T. Kiyosaki")
                .isbn("9781612680194")
                .booksAvailable(3)
                .publicationDate(LocalDate.parse("2018-10-31"))
                .location("E5")
                .build()));

        bookService.outgoingBook(2);

        verify(bookRepository).save(bookCaptor.capture());
        Book book = bookCaptor.getValue();
        assertEquals(2, book.getBooksAvailable());
    }

    @Test
    public void incomingBook_shouldAddBookToAvailableBooks() throws Exception{
        when(bookRepository.findById(2L)).thenReturn(Optional.ofNullable(Book.builder()
                .id(2L)
                .name("Rich Dad Poor Dad")
                .author("Robert T. Kiyosaki")
                .isbn("9781612680194")
                .booksAvailable(3)
                .publicationDate(LocalDate.parse("2018-10-31"))
                .location("E5")
                .build()));

        bookService.incomingBook(2);

        verify(bookRepository).save(bookCaptor.capture());
        Book book = bookCaptor.getValue();
        assertEquals(4, book.getBooksAvailable());
    }

    @Test
    public void getPublicView_shouldReturnListOfPublicBooks() throws Exception{
        Book book1 = Book.builder()
                .id(1L)
                .name("Atomic Habits")
                .author("James Clear")
                .isbn("9781847941831")
                .booksAvailable(6)
                .publicationDate(LocalDate.parse("2018-11-27"))
                .location("G6")
                .build();
        Book book2 = Book.builder()
                .id(2L)
                .name("Rich Dad Poor Dad")
                .author("Robert T. Kiyosaki")
                .isbn("9781612680194")
                .booksAvailable(3)
                .publicationDate(LocalDate.parse("2018-10-31"))
                .location("E5")
                .build();
        Book book3 = Book.builder()
                .id(3L)
                .name("The Expectation Effect")
                .author("David Robson")
                .isbn("9781838853303")
                .booksAvailable(8)
                .publicationDate(LocalDate.parse("2022-12-29"))
                .location("A3")
                .build();

        when(bookRepository.findAll()).thenReturn(List.of(book1, book2, book3));
        List<PublicBook> publicBooks = bookService.getPublicView();

        assertEquals(3, publicBooks.size());
        assertEquals(1, publicBooks.get(0).getId());
        assertEquals(4, publicBooks.get(0).getMaxRentalLengthInWeeks());
        assertEquals(2, publicBooks.get(1).getId());
        assertEquals(1, publicBooks.get(1).getMaxRentalLengthInWeeks());
        assertEquals(3, publicBooks.get(2).getId());
        assertEquals(1, publicBooks.get(2).getMaxRentalLengthInWeeks());
    }
}

