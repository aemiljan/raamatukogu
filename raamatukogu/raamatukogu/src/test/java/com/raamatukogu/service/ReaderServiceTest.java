package com.raamatukogu.service;

import com.raamatukogu.model.Reader;
import com.raamatukogu.repository.ReaderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReaderServiceTest {
    @Mock
    private ReaderRepository readerRepository;
    @InjectMocks
    private ReaderService readerService;



    @Test
    public void getReaders_shouldReturnAllReaders() throws Exception{
        Reader r1 = Reader.builder()
                .id(1L)
                .firstName("John")
                .lastName("Smith")
                .build();
        Reader r2 = Reader.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Smith")
                .build();
        Reader r3 = Reader.builder()
                .id(3L)
                .firstName("Tony")
                .lastName("Hawk")
                .build();

        given(readerRepository.findAll()).willReturn(List.of(r1, r2, r3));

        List<Reader> readers = readerService.getAllReaders();

        assertThat(readers).hasSize(3);
        assertThat(readers.get(0).getId()).isEqualTo(1L);
        assertThat(readers.get(0).getFirstName()).isEqualTo("John");
        assertThat(readers.get(0).getLastName()).isEqualTo("Smith");
        assertThat(readers.get(1).getId()).isEqualTo(2L);
        assertThat(readers.get(1).getFirstName()).isEqualTo("Jane");
        assertThat(readers.get(1).getLastName()).isEqualTo("Smith");
        assertThat(readers.get(2).getId()).isEqualTo(3L);
        assertThat(readers.get(2).getFirstName()).isEqualTo("Tony");
        assertThat(readers.get(2).getLastName()).isEqualTo("Hawk");
    }
    @Test
    public void getReader_shouldReturnReaderWithMatchingId() throws Exception {
        when(readerRepository.findById(2L)).thenReturn(Optional.ofNullable(Reader.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Smith")
                .build()));

        Reader reader = readerService.getReaderById(2L);
        assertThat(reader.getId()).isEqualTo(2L);
        assertThat(reader.getFirstName()).isEqualTo("Jane");
        assertThat(reader.getLastName()).isEqualTo("Smith");
    }

    //update
    @Test
    public void updateReader_shouldUpdateReaderInRepository() throws Exception{
        Reader reader = Reader.builder()
                .id(2L)
                .firstName("Jamie")
                .lastName("Stone")
                .build();

        when(readerRepository.findById(2L)).thenReturn(Optional.ofNullable(Reader.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Smith")
                .build()));

        Reader updatedReader = readerService.updateReader(reader, 2);
        assertEquals("Jamie", updatedReader.getFirstName());
        assertEquals("Stone", updatedReader.getLastName());
    }

    //delete
    @Test
    public void deleteReader_shouldDeleteReaderInRepository() throws Exception{
        when(readerRepository.findById(2L)).thenReturn(Optional.ofNullable(Reader.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Smith")
                .build()));

        readerService.deleteReader(2);
        assertThat(readerRepository.findAll()).hasSize(0);
    }

}
