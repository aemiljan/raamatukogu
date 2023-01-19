package com.raamatukogu.controller;

import com.raamatukogu.model.entities.OverdueRental;
import com.raamatukogu.model.Rental;
import com.raamatukogu.service.RentalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/rentals")
public class RentalController {
    private final RentalService rentalService;

    public RentalController(RentalService rentalService){
        super();
        this.rentalService = rentalService;
    }
    // REST api laenutamiste lisamiseks
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Rental> saveRental(@RequestBody Rental rental){
        return new ResponseEntity<Rental>(rentalService.saveRental(rental), HttpStatus.CREATED);
    }
    //REST api kõikide laenutuste kuvamiseks
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Rental> getAllRentals(){
        return rentalService.getAllRentals();
    }
    //REST api laenutuse leidmiseks id põhjal
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("{id}")
    public ResponseEntity<Rental> getRentalById(@PathVariable("id") long rentalId){
        return new ResponseEntity<Rental>(rentalService.getRentalById(rentalId), HttpStatus.OK);
    }
    /*REST api laenutuse tähtaja uuendamiseks
    Json sisendi näidis:
    {
        "deadline": "yyyy-MM-dd"
    }*/
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<Rental> updateRentalDeadline(@PathVariable("id") long id, @RequestBody Rental rental){
        return new ResponseEntity<Rental>(rentalService.updateRentalDeadline(rental, id), HttpStatus.OK);
    }
    //REST api laenutuse lõpetamiseks, tagastab laenutuse isActive: false tunnusega
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/close/{id}")
    public ResponseEntity<Rental> closeRental(@PathVariable("id") long id){
        return new ResponseEntity<Rental>(rentalService.returnBook(id), HttpStatus.OK);
    }
    //REST api tähtaja ületanud laenutuste raporti genereerimiseks
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/overdue")
    public List<OverdueRental> getOverdueReport(){
        return rentalService.getOverdueReport();
    }
    //REST api aktiivsete laenutuste nägemiseks
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/active")
    public List<Rental> getAllActiveRentals(){
        return rentalService.getAllActiveRentals();
    }
}
