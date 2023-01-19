package com.raamatukogu.repository;

import com.raamatukogu.model.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReaderRepository extends JpaRepository<Reader, Long> {

    @Query(value = "Select * FROM readers r WHERE LOWER(first_name) LIKE %?1% OR LOWER(last_name) LIKE %?1%",
            nativeQuery = true)
    List<Reader> searchReaders(String keyword);
}
