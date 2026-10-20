package com.cseintellilearnapi.controller;

import com.cseintellilearnapi.entity.*;
import com.cseintellilearnapi.service.AcademicService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class AcademicController {

    private final AcademicService academicService;

    public AcademicController(AcademicService academicService) {
        this.academicService = academicService;
    }

    // ─── Query Endpoints ──────────────────────────────────────────────────────

    @GetMapping("/years")
    public ResponseEntity<List<AcademicYear>> getAllYears() {
        return ResponseEntity.ok(academicService.getAllYears());
    }

    @GetMapping("/semesters")
    public ResponseEntity<List<Semester>> getAllSemesters() {
        return ResponseEntity.ok(academicService.getAllSemesters());
    }

    @GetMapping("/semesters/{year_id}")
    public ResponseEntity<List<Semester>> getSemestersByYear(@PathVariable("year_id") Long yearId) {
        return ResponseEntity.ok(academicService.getSemestersByYear(yearId));
    }

    @GetMapping("/subjects")
    public ResponseEntity<List<Subject>> getAllSubjects() {
        return ResponseEntity.ok(academicService.getAllSubjects());
    }

    @GetMapping("/subjects/{semester_id}")
    public ResponseEntity<List<Subject>> getSubjectsBySemester(@PathVariable("semester_id") Long semesterId) {
        return ResponseEntity.ok(academicService.getSubjectsBySemester(semesterId));
    }

    @GetMapping("/units")
    public ResponseEntity<List<Unit>> getAllUnits() {
        return ResponseEntity.ok(academicService.getAllUnits());
    }

    @GetMapping("/units/{subject_id}")
    public ResponseEntity<List<Unit>> getUnitsBySubject(@PathVariable("subject_id") Long subjectId) {
        return ResponseEntity.ok(academicService.getUnitsBySubject(subjectId));
    }

    @GetMapping("/topics")
    public ResponseEntity<List<Topic>> getAllTopics() {
        return ResponseEntity.ok(academicService.getAllTopics());
    }

    @GetMapping("/topics/{unit_id}")
    public ResponseEntity<List<Topic>> getTopicsByUnit(@PathVariable("unit_id") Long unitId) {
        return ResponseEntity.ok(academicService.getTopicsByUnit(unitId));
    }

    @GetMapping("/topic/{topic_id}")
    public ResponseEntity<Topic> getTopicById(@PathVariable("topic_id") Long topicId) {
        return ResponseEntity.ok(academicService.getTopicById(topicId));
    }

    // ─── Admin Master: POST Endpoints ─────────────────────────────────────────

    @PostMapping("/admin/years")
    public ResponseEntity<AcademicYear> createYear(@RequestBody AcademicYear year) {
        return ResponseEntity.status(HttpStatus.CREATED).body(academicService.createYear(year));
    }

    @PostMapping("/admin/semesters")
    public ResponseEntity<Semester> createSemester(@RequestBody Semester semester) {
        return ResponseEntity.status(HttpStatus.CREATED).body(academicService.createSemester(semester));
    }

    @PostMapping("/admin/subjects")
    public ResponseEntity<Subject> createSubject(@RequestBody Subject subject) {
        return ResponseEntity.status(HttpStatus.CREATED).body(academicService.createSubject(subject));
    }

    @PostMapping("/admin/units")
    public ResponseEntity<Unit> createUnit(@RequestBody Unit unit) {
        return ResponseEntity.status(HttpStatus.CREATED).body(academicService.createUnit(unit));
    }

    @PostMapping("/admin/topics")
    public ResponseEntity<Topic> createTopic(@RequestBody Topic topic) {
        return ResponseEntity.status(HttpStatus.CREATED).body(academicService.createTopic(topic));
    }

    // ─── Admin Master: DELETE Endpoints ───────────────────────────────────────

    @DeleteMapping("/admin/years/{id}")
    public ResponseEntity<Void> deleteYear(@PathVariable Long id) {
        academicService.deleteYear(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/admin/semesters/{id}")
    public ResponseEntity<Void> deleteSemester(@PathVariable Long id) {
        academicService.deleteSemester(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/admin/subjects/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        academicService.deleteSubject(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/admin/units/{id}")
    public ResponseEntity<Void> deleteUnit(@PathVariable Long id) {
        academicService.deleteUnit(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/admin/topics/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable Long id) {
        academicService.deleteTopic(id);
        return ResponseEntity.noContent().build();
    }
}
