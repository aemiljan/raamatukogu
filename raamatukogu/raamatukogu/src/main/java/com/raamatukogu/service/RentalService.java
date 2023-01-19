package com.raamatukogu.service;

import com.raamatukogu.exception.ResourceNotFoundException;
import com.raamatukogu.model.Book;
import com.raamatukogu.model.entities.OverdueRental;
import com.raamatukogu.model.Reader;
import com.raamatukogu.model.Rental;
import com.raamatukogu.repository.RentalRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@Transactional
public class RentalService {
    private final RentalRepository rentalRepository;
    private final BookService bookService;
    private final ReaderService readerService;
    private static final Logger logger = Logger.getLogger(RentalService.class.getName());

    public RentalService(RentalRepository rentalRepository, BookService bookService, ReaderService readerService){
        this.rentalRepository =rentalRepository;
        this.bookService = bookService;
        this.readerService = readerService;
    }
    public Rental saveRental(Rental rental){
        logger.info("Saving new rental.");
        //generating renterName based on renter_id
        Reader reader = readerService.getReaderById(rental.getReader_id());
        rental.setRenterName(reader.getFirstName() + " " + reader.getLastName());
        //deadline calculation if no value provided (1 week from now if <5 books or book <3 months old, else 4 weeks)
        Book book = bookService.getBookById(rental.getBook_id());
        if(rental.getDeadline() == null){
            if(book.getBooksAvailable() < 5){
                //deadline 1 week from now
                rental.setDeadline(LocalDate.now().plusWeeks(1));
            } else if (ChronoUnit.MONTHS.between(book.getPublicationDate(), LocalDate.now()) < 3) {
                //deadline 1 week from now
                rental.setDeadline(LocalDate.now().plusWeeks(1));
            } else {
                //deadline 4 weeks from now
                rental.setDeadline(LocalDate.now().plusWeeks(4));
            }
        }
        rental.setActive(true);
        //Subtracting book from available books
        bookService.outgoingBook(rental.getBook_id());
        return rentalRepository.save(rental);
    }
    //get all rentals (active and inactive)
    public List<Rental> getAllRentals() {
        logger.info("Fetching list of all rentals.");
        return rentalRepository.findAll();
    }

    public Rental getRentalById(long id){
        logger.info("Searching for a rental with id:" + id);
        return rentalRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Rental", "Id", id));
    }

    public Rental updateRentalDeadline(Rental rental, long id){
        logger.info("Updating deadline for rental with id:" + id);
        //check if rental with given id exists
        Rental existingRental = rentalRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Rental", "Id", id));

        existingRental.setDeadline(rental.getDeadline());

        //save new rental info to DB
        rentalRepository.save(existingRental);
        return existingRental;
    }
    //returns list of overdue rentals
    private List<Rental> getOverdueRentals(){
        return rentalRepository.findByDeadlineBeforeAndIsActive(LocalDate.now(), true);
    }
    //transforms list of  overdue rentals into a report
    private List<OverdueRental> getReport(List<Rental> rentals){
        List<OverdueRental> report = new ArrayList<>();
        for (Rental rental : rentals){
            Book book = bookService.getBookById(rental.getBook_id());
            long daysOverdue = DAYS.between(rental.getDeadline(), LocalDate.now());
            report.add(new OverdueRental(rental, book.getName(), book.getAuthor(), daysOverdue));
        }
        return report;
    }

    //returns report of overdue rentals
    public List<OverdueRental> getOverdueReport(){
        logger.info("Generating report of overdue rentals.");
        List<Rental> overdueRentals = getOverdueRentals();
        return getReport(overdueRentals);
    }

    //when the book is returned, makes rental inactive, returns appropriate message
    public Rental returnBook(long id){
        logger.info("Returning book with id:" + id);
        //check if rental with given id exists
        Rental existingRental = rentalRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Rental", "Id", id));
        //set rental as inactive
        existingRental.setActive(false);
        rentalRepository.save(existingRental);
        // make the book available once again
        bookService.incomingBook(existingRental.getBook_id());
        logger.info("Book with id:" + id + " returned successfully.");
        return existingRental;
    }
    // returns list of all rentals currently active
    public List<Rental> getAllActiveRentals(){
        logger.info("Fetching list of all active rentals.");
        return rentalRepository.findByIsActive(true);
    }
}
