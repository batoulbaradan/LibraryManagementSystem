package com.example.Library.restController.table;

import com.example.Library.classes.dto.PatronDTO;
import com.example.Library.classes.response.ApiResponse;
import com.example.Library.service.table.PatronService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/patrons")
public class PatronRestController {
    @Autowired
    private  PatronService patronService;


    @GetMapping
    public ResponseEntity<ApiResponse<List<PatronDTO>>> getAllPatrons() {
        List<PatronDTO> patrons = patronService.getAllPatrons();
        return ResponseEntity.ok(new ApiResponse<>(true, "Patrons retrieved successfully", patrons));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PatronDTO>> getPatronById(@PathVariable Long id) {
        PatronDTO patron = patronService.getPatronById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Patron retrieved successfully", patron));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PatronDTO>> createPatron(@Valid @RequestBody PatronDTO patronDTO) {
        PatronDTO createdPatron = patronService.savePatron(patronDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Patron created successfully", createdPatron));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PatronDTO>> updatePatron(@PathVariable Long id,
                                                               @Valid @RequestBody PatronDTO patronDTO) {
        PatronDTO updatedPatron = patronService.updatePatron(id, patronDTO);
        return ResponseEntity.ok(new ApiResponse<>(true, "Patron updated successfully", updatedPatron));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePatron(@PathVariable Long id) {
        patronService.deletePatron(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Patron deleted successfully", null));
    }
}

