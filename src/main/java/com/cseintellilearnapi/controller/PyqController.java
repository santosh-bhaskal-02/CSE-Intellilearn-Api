package com.cseintellilearnapi.controller;

import com.cseintellilearnapi.entity.PreviousYearQuestion;
import com.cseintellilearnapi.service.PyqService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pyqs")
public class PyqController {

    private final PyqService pyqService;

    public PyqController(PyqService pyqService) {
        this.pyqService = pyqService;
    }

    @GetMapping("/{subject_id}")
    public ResponseEntity<List<PreviousYearQuestion>> getPyqsBySubject(@PathVariable("subject_id") Long subjectId) {
        return ResponseEntity.ok(pyqService.getPyqsBySubject(subjectId));
    }

    @PostMapping
    public ResponseEntity<?> addPyq(@RequestBody PreviousYearQuestion pyq) {
        PreviousYearQuestion saved = pyqService.addPyq(pyq);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "success", true,
                "message", "PYQ added successfully",
                "pyq_id", saved.getPyqId()
        ));
    }
}
