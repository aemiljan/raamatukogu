package com.raamatukogu.service;

import com.raamatukogu.model.Book;
import com.raamatukogu.model.Reader;
import com.raamatukogu.model.Rental;
import com.raamatukogu.model.entities.OverdueRental;
import com.raamatukogu.repository.RentalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock
    private RentalRepository rentalRepository;
    @Mock
    private ReaderService readerService;
    @Mock
    private BookService bookService;

    @InjectMocks
    private RentalService rentalService;


    //get all rentals
    @Test
    public void getRentals_shouldReturnAllRentals() throws Exception{
        Rental r1 = Rental.builder()
                .id(1L)
                .book_id(1)
                .reader_id(1)
                .renterName("John Smith")
                .isActive(true)
                .deadline(LocalDate.now().plusWeeks(4))
                .build();
        Rental r2 = Rental.builder()
                .id(2L)
                .book_id(2)
                .reader_id(1)
                .renterName("John Smith")
                .isActive(true)
                .deadline(LocalDate.now().plusWeeks(1))
                .build();
        Rental r3 = Rental.builder()
                .id(3L)
                .book_id(2)
                .reader_id(2)
                .renterName("Jane Smith")
                .isActive(true)
                .deadline(LocalDate.now().plusWeeks(1))
                .build();

        when(rentalRepository.findAll()).thenReturn(List.of(r1, r2, r3));

        List<Rental> rentals = rentalService.getAllRentals();
        assertEquals(3, rentals.size());
        assertEquals(1L, rentals.get(0).getId());
        assertEquals(1, rentals.get(0).getBook_id());
        assertEquals(1, rentals.get(0).getReader_id());
        assertEquals("John Smith", rentals.get(0).getRenterName());
        assertTrue(rentals.get(0).isActive());
        assertEquals(LocalDate.now().plusWeeks(4), rentals.get(0).getDeadline());
        assertEquals(2L, rentals.get(1).getId());
        assertEquals(2, rentals.get(1).getBook_id());
        assertEquals(1, rentals.get(1).getReader_id());
        assertEquals("John Smith", rentals.get(1).getRenterName());
        assertTrue(rentals.get(1).isActive());
        assertEquals(LocalDate.now().plusWeeks(1), rentals.get(1).getDeadline());
        assertEquals(3L, rentals.get(2).getId());
        assertEquals(2, rentals.get(2).getBook_id());
        assertEquals(2, rentals.get(2).getReader_id());
        assertEquals("Jane Smith", rentals.get(2).getRenterName());
        assertTrue(rentals.get(2).isActive());
        assertEquals(LocalDate.now().plusWeeks(1), rentals.get(2).getDeadline());
    }
    //get rental with correct id
    @Test
    public void getRental_shouldReturnRentalWithMatchingId() throws Exception{
        when(rentalRepository.findById(2L)).thenReturn(Optional.ofNullable(Rental.builder()
                .id(2L)
                .book_id(2)
                .reader_id(1)
                .renterName("John Smith")
                .isActive(true)
                .deadline(LocalDate.now().plusWeeks(1))
                .build()));

        Rental rental = rentalService.getRentalById(2);
        assertEquals(2, rental.getId());
        assertEquals(1, rental.getReader_id());
        assertEquals(2, rental.getBook_id());
        assertEquals("John Smith", rental.getRenterName());
        assertTrue(rental.isActive());
        assertEquals(LocalDate.now().plusWeeks(1), rental.getDeadline());
    }

    //save new rental - deadlines test, renterName test
    @Test
    public void addRental_shouldAddRentalToRepository() throws Exception{
        Rental r1 = Rental.builder()
                .id(1L)
                .reader_id(2)
                .book_id(1)
                .build();
        Rental r2 = Rental.builder()
                .id(2L)
                .reader_id(2)
                .book_id(2)
                .build();
        Rental r3 = Rental.builder()
                .id(3L)
                .reader_id(2)
                .book_id(3)
                .build();

        given(rentalRepository.save(r1)).willAnswer(invocation -> invocation.getArgument(0));
        given(rentalRepository.save(r2)).willAnswer(invocation -> invocation.getArgument(0));
        given(rentalRepository.save(r3)).willAnswer(invocation -> invocation.getArgument(0));
        //less than 5 books -> deadline in 1 week
        when(bookService.getBookById(1)).thenReturn(Book.builder()
                .id(1L)
                .name("Rich Dad Poor Dad")
                .author("Robert T. Kiyosaki")
                .isbn("9781612680194")
                .booksAvailable(3)
                .publicationDate(LocalDate.parse("2018-10-31"))
                .location("E5")
                .build());
        // no limiting factors - deadline in 4 weeks
        when(bookService.getBookById(2)).thenReturn(Book.builder()
                .id(2L)
                .name("Atomic Habits")
                .author("James Clear")
                .isbn("9781847941831")
                .booksAvailable(6)
                .publicationDate(LocalDate.parse("2018-11-27"))
                .location("G6")
                .build());
        // book released in last 3 months - deadline in 1 week
        when(bookService.getBookById(3)).thenReturn(Book.builder()
                .id(3L)
                .name("The Expectation Effect")
                .author("David Robson")
                .isbn("9781838853303")
                .booksAvailable(8)
                .publicationDate(LocalDate.now().minusMonths(1))
                .location("E5")
                .build());

        when(readerService.getReaderById(2)).thenReturn(Reader.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Smith")
                .build());

        Rental newRental1 = rentalService.saveRental(r1);
        Rental newRental2 = rentalService.saveRental(r2);
        Rental newRental3 = rentalService.saveRental(r3);
        assertTrue(newRental1.isActive());
        verify(bookService).outgoingBook(1);
        assertEquals(LocalDate.now().plusWeeks(1), newRental1.getDeadline());
        assertEquals("Jane Smith", newRental1.getRenterName());
        assertEquals(2, newRental1.getReader_id());
        assertEquals(1, newRental1.getBook_id());
        assertEquals(LocalDate.now().plusWeeks(4), newRental2.getDeadline());
        assertEquals(LocalDate.now().plusWeeks(1), newRental3.getDeadline());
    }

    //ending rental
    @Test
    public void returnBook_shouldEndRental() throws Exception{
        when(rentalRepository.findById(1L)).thenReturn(Optional.ofNullable(Rental.builder()
                .id(1L)
                .book_id(2)
                .reader_id(1)
                .renterName("John Smith")
                .isActive(true)
                .deadline(LocalDate.now().plusWeeks(1))
                .build()));

        Rental rental = rentalService.returnBook(1);

        verify(bookService).incomingBook(2);
        assertFalse(rental.isActive());
        verify(rentalRepository).save(rental);
    }

    //overdue Report test
    @Test
    public void getOverdueReport_shouldReturnOverdueReportOfAllOverdueRentals() throws Exception{
        Rental r1 = Rental.builder()
                .id(1L)
                .book_id(1)
                .reader_id(1)
                .renterName("John Smith")
                .isActive(true)
                .deadline(LocalDate.now().minusDays(222))
                .build();
        Rental r2 = Rental.builder()
                .id(2L)
                .book_id(2)
                .reader_id(1)
                .renterName("John Smith")
                .isActive(true)
                .deadline(LocalDate.now().minusDays(22))
                .build();
        Rental r3 = Rental.builder()
                .id(3L)
                .book_id(2)
                .reader_id(2)
                .renterName("Jane Smith")
                .isActive(true)
                .deadline(LocalDate.now().minusDays(1))
                .build();

        when(rentalRepository.findByDeadlineBeforeAndIsActive(LocalDate.now(),true)).thenReturn(List.of(r1, r2, r3));
        when(bookService.getBookById(1)).thenReturn(Book.builder()
                .id(1L)
                .name("Rich Dad Poor Dad")
                .author("Robert T. Kiyosaki")
                .isbn("9781612680194")
                .booksAvailable(3)
                .publicationDate(LocalDate.parse("2018-10-31"))
                .location("E5")
                .build());
        when(bookService.getBookById(2)).thenReturn(Book.builder()
                .id(2L)
                .name("Atomic Habits")
                .author("James Clear")
                .isbn("9781847941831")
                .booksAvailable(6)
                .publicationDate(LocalDate.parse("2018-11-27"))
                .location("G6")
                .build());

        List<OverdueRental> odr = rentalService.getOverdueReport();

        assertEquals(3, odr.size());
        assertEquals(1, odr.get(0).getBook_id());
        assertEquals(1, odr.get(0).getReader_id());
        assertEquals(222, odr.get(0).getOverdueDays());
        assertEquals("Rich Dad Poor Dad" , odr.get(0).getBookName());
        assertEquals("Robert T. Kiyosaki" , odr.get(0).getBookAuthor());
        assertEquals(2, odr.get(1).getBook_id());
        assertEquals(1, odr.get(1).getReader_id());
        assertEquals(22, odr.get(1).getOverdueDays());
        assertEquals("Atomic Habits" , odr.get(1).getBookName());
        assertEquals("James Clear" , odr.get(1).getBookAuthor());
        assertEquals(2, odr.get(2).getBook_id());
        assertEquals(2, odr.get(2).getReader_id());
        assertEquals(1, odr.get(2).getOverdueDays());
        assertEquals("Atomic Habits" , odr.get(2).getBookName());
        assertEquals("James Clear" , odr.get(2).getBookAuthor());
    }
    // update deadline
    @Test
    public void updateRentalDeadline_shouldUpdateRentalDeadlineInRepository() throws Exception{
        Rental rental = Rental.builder()
                .id(2)
                .deadline(LocalDate.now().plusWeeks(2))
                .build();

        when(rentalRepository.findById(2L)).thenReturn(Optional.ofNullable(Rental.builder()
                .id(2L)
                .book_id(2)
                .reader_id(1)
                .renterName("John Smith")
                .isActive(true)
                .deadline(LocalDate.now().plusWeeks(1))
                .build()));

        Rental updatedRental = rentalService.updateRentalDeadline(rental, 2);
        assertEquals(LocalDate.now().plusWeeks(2), updatedRental.getDeadline());
    }

    //get all active rentals
    @Test
    public void getAllActiveRentals_shouldReturnAllActiveRentalsInRepository() throws Exception{
        Rental r1 = Rental.builder()
                .id(1L)
                .book_id(1)
                .reader_id(1)
                .renterName("John Smith")
                .isActive(true)
                .deadline(LocalDate.now().plusWeeks(2))
                .build();
        Rental r2 = Rental.builder()
                .id(2L)
                .book_id(2)
                .reader_id(1)
                .renterName("John Smith")
                .isActive(true)
                .deadline(LocalDate.now().minusDays(22))
                .build();

        when(rentalRepository.findByIsActive(true)).thenReturn(List.of(r1, r2));

        List<Rental> activeRentals = rentalService.getAllActiveRentals();

        assertEquals(2, activeRentals.size());
        assertEquals(1, activeRentals.get(0).getId());
        assertEquals(2, activeRentals.get(1).getId());
    }
}