package com.example.Library.service.table;

import com.example.Library.classes.dto.PatronDTO;
import com.example.Library.classes.mapper.PatronMapper;
import com.example.Library.exception.ResourceNotFoundException;
import com.example.Library.model.table.Patron;
import com.example.Library.repository.table.PatronRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatronService {
    private final PatronRepository patronRepository;
    private final PatronMapper patronMapper;

    public PatronService(PatronRepository patronRepository, PatronMapper patronMapper) {
        this.patronRepository = patronRepository;
        this.patronMapper = patronMapper;
    }

    //    @Cacheable(value = "patrons")
    public List<PatronDTO> getAllPatrons() {
        List<Patron> patrons = patronRepository.findAll();

        List<PatronDTO> patronDTOs = patrons.stream()
                .map(patronMapper::toDTO)
                .collect(Collectors.toList());

        return patronDTOs;
    }

    //    @Cacheable(value = "patrons", key = "#id")
    public PatronDTO getPatronById(Long id) {
        Patron patron = patronRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patron with ID " + id + " not found"));
        return patronMapper.toDTO(patron); // Convert Patron entity to DTO
    }

    //@CacheEvict(value = "patrons", allEntries = true)
    @Transactional
    public PatronDTO savePatron(PatronDTO patronDTO) {
        Patron patron = patronMapper.toEntity(patronDTO); // Convert PatronDTO to Patron entity
        Patron savedPatron = patronRepository.save(patron);
        try {
            return patronMapper.toDTO(savedPatron);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("Failed to save patron due to database constraints." + ex.getRootCause().getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("An unexpected error occurred while saving the patron: " + ex.getMessage());
        }
    }

    //    @CachePut(value = "patrons", key = "#id")
    @Transactional
    public PatronDTO updatePatron(Long id, PatronDTO patronDTO) {
        Patron patron = patronRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patron with ID " + id + " not found"));
        patron.setName(patronDTO.getName());
        patron.setEmail(patron.getEmail());
        patron.setPhoneNumber(patron.getPhoneNumber());
        Patron updatedPatron = patronRepository.save(patron);

        try {
            return patronMapper.toDTO(updatedPatron);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("Failed to save patron due to database constraints." + ex.getRootCause().getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("An unexpected error occurred while saving the patron: " + ex.getMessage());
        }
    }

    //    @CacheEvict(value = "patrons", key = "#id")
    @Transactional
    public void deletePatron(Long id) {
        Patron patron = patronRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patron with ID " + id + " not found"));

        try {
            patronRepository.delete(patron);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("Failed to delete patron due to database constraints.");
        }
    }
}
