package com.cseintellilearnapi.controller;

import com.cseintellilearnapi.entity.Doubt;
import com.cseintellilearnapi.exception.BadRequestException;
import com.cseintellilearnapi.service.DoubtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/doubts")
public class DoubtController {

    private final DoubtService doubtService;

    public DoubtController(DoubtService doubtService) {
        this.doubtService = doubtService;
    }

    @GetMapping
    public ResponseEntity<List<Doubt>> getAllDoubts() {
        return ResponseEntity.ok(doubtService.getAllDoubts());
    }

    @GetMapping("/student/{student_id}")
    public ResponseEntity<List<Doubt>> getDoubtsByStudent(@PathVariable("student_id") Long studentId) {
        return ResponseEntity.ok(doubtService.getDoubtsByStudent(studentId));
    }

    @PostMapping
    public ResponseEntity<?> submitDoubt(@RequestBody Doubt doubt) {
        Doubt saved = doubtService.submitDoubt(doubt);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "success", true,
                "message", "Doubt submitted successfully",
                "doubt_id", saved.getDoubtId()
        ));
    }

    @PutMapping("/{doubt_id}/answer")
    public ResponseEntity<?> answerDoubt(@PathVariable("doubt_id") Long doubtId, @RequestBody Map<String, Object> body) {
        String answer = (String) body.get("answer");
        Object teacherIdObj = body.get("teacher_id");
        if (teacherIdObj == null) {
            teacherIdObj = body.get("teacherId");
        }

        if (answer == null || teacherIdObj == null) {
            throw new BadRequestException("Answer and teacher are required");
        }

        try {
            Long teacherId = Long.parseLong(teacherIdObj.toString());
            doubtService.answerDoubt(doubtId, answer, teacherId);
            return ResponseEntity.ok(Map.of("success", true, "message", "Doubt answered successfully"));
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid teacher ID");
        }
    }
}
