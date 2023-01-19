package com.raamatukogu.service;

import com.raamatukogu.exception.ResourceNotFoundException;
import com.raamatukogu.model.Reader;
import com.raamatukogu.repository.ReaderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
@Transactional
public class ReaderService {
    private final ReaderRepository readerRepository;
    private static final Logger logger = Logger.getLogger(ReaderService.class.getName());

    public ReaderService(ReaderRepository readerRepository){
        this.readerRepository = readerRepository;
    }

    public Reader saveReader(Reader reader){
        logger.info("Saving new reader.");
        return readerRepository.save(reader);
    }

    public List<Reader> getAllReaders() {
        logger.info("Fetching list of all readers.");
        return readerRepository.findAll();
    }
    public Reader getReaderById(long id){
        logger.info("Searching for a reader with id:" + id);
        return readerRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Reader", "Id", id));
    }
    public Reader updateReader(Reader reader, long id){
        logger.info("Updating reader with id:" + id);
        //check if reader with given id exists
        Reader existingReader = readerRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Reader", "Id", id));

        existingReader.setFirstName(reader.getFirstName());
        existingReader.setLastName(reader.getLastName());

        //save new reader info to DB
        readerRepository.save(existingReader);
        return existingReader;
    }

    public void deleteReader(long id){
        logger.info("Deleting reader with id:" + id);
        //check if reader exists
        readerRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Reader", "Id", id));
        readerRepository.deleteById(id);
    }

    public List<Reader> searchReaders(String keyword) {
        return readerRepository.searchReaders(keyword);
    }
}
