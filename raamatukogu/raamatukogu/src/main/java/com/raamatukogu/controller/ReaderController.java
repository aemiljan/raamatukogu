package com.raamatukogu.controller;

import com.raamatukogu.model.Reader;
import com.raamatukogu.service.ReaderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/readers")
public class ReaderController {
    private final ReaderService readerService;

    public ReaderController(ReaderService readerService){
        super();
        this.readerService = readerService;
    }

    // REST api lugejate lisamiseks
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Reader> saveReader(@RequestBody Reader reader){
        return new ResponseEntity<Reader>(readerService.saveReader(reader), HttpStatus.CREATED);
    }
    //REST api kõikide lugejate kuvamiseks
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Reader> getAllReaders(){
        return readerService.getAllReaders();
    }
    //REST api lugeja leidmiseks id põhjal
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("{id}")
    public ResponseEntity<Reader> getReaderById(@PathVariable("id") long readerId){
        return new ResponseEntity<Reader>(readerService.getReaderById(readerId), HttpStatus.OK);
    }
    //REST api raamatu andmete uuendamiseks
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<Reader> updateReader(@PathVariable("id") long id, @RequestBody Reader reader){
        return new ResponseEntity<Reader>(readerService.updateReader(reader, id), HttpStatus.OK);
    }
    //REST api lugeja kustutamiseks
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteReader(@PathVariable("id") long id){

        readerService.deleteReader(id);

        return new ResponseEntity<String>("Lugeja kustutati süsteemist edukalt.", HttpStatus.OK);
    }

    //REST api lugeja otsimiseks
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search/{keyword}")
    public List<Reader> searchReaders(@PathVariable("keyword") String keyword){
        return readerService.searchReaders(keyword);
    }
}
