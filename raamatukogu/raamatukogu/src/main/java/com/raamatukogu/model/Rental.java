package com.raamatukogu.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "rentals")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "reader_id", nullable = false)
    private long reader_id;

    @Column(name = "book_id", nullable = false)
    private long book_id;

    @Column(name = "deadline")
    private LocalDate deadline;

    @Column(name = "renter_name")
    private String renterName;

    @Column(name = "is_active")
    private boolean isActive;
}
