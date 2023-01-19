package com.raamatukogu.repository;

import com.raamatukogu.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    List<Rental> findByDeadlineBeforeAndIsActive(LocalDate deadline, boolean isActive);
    List<Rental> findByIsActive(boolean isActive);

}
