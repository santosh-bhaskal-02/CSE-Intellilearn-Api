package com.cseintellilearnapi.controller;

import com.cseintellilearnapi.entity.Assignment;
import com.cseintellilearnapi.entity.AssignmentSubmission;
import com.cseintellilearnapi.entity.Feedback;
import com.cseintellilearnapi.service.AssignmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    // ─── ASSIGNMENTS ──────────────────────────────────────────────────────────

    @GetMapping("/assignments")
    public ResponseEntity<List<Assignment>> getAllAssignments() {
        return ResponseEntity.ok(assignmentService.getAllAssignments());
    }

    @GetMapping("/assignments/details")
    public ResponseEntity<List<Assignment>> getAssignmentDetails() {
        return ResponseEntity.ok(assignmentService.getAllAssignments());
    }

    @GetMapping("/assignments/topic/{topic_id}")
    public ResponseEntity<List<Assignment>> getAssignmentsByTopic(@PathVariable("topic_id") Long topicId) {
        return ResponseEntity.ok(assignmentService.getAssignmentsByTopic(topicId));
    }

    @PostMapping("/assignments")
    public ResponseEntity<?> createAssignment(@RequestBody Assignment assignment) {
        Assignment saved = assignmentService.createAssignment(assignment);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "success", true,
                "message", "Assignment created successfully",
                "assignment_id", saved.getAssignmentId()
        ));
    }

    // ─── SUBMISSIONS ──────────────────────────────────────────────────────────

    @GetMapping("/submissions/{assignment_id}")
    public ResponseEntity<List<AssignmentSubmission>> getSubmissionsByAssignment(@PathVariable("assignment_id") Long assignmentId) {
        return ResponseEntity.ok(assignmentService.getSubmissionsByAssignment(assignmentId));
    }

    @GetMapping("/student-submissions/{student_id}")
    public ResponseEntity<List<AssignmentSubmission>> getStudentSubmissions(@PathVariable("student_id") Long studentId) {
        return ResponseEntity.ok(assignmentService.getStudentSubmissions(studentId));
    }

    @PostMapping("/submissions")
    public ResponseEntity<?> submitAssignment(@RequestBody AssignmentSubmission submission) {
        AssignmentSubmission saved = assignmentService.submitAssignment(submission);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "success", true,
                "message", "Assignment submitted successfully",
                "submission_id", saved.getSubmissionId()
        ));
    }

    @PutMapping("/submissions/{submission_id}/status")
    public ResponseEntity<?> updateSubmissionStatus(@PathVariable("submission_id") Long submissionId, @RequestBody Map<String, String> body) {
        assignmentService.updateSubmissionStatus(submissionId, body.get("status"));
        return ResponseEntity.ok(Map.of("success", true, "message", "Submission status updated successfully"));
    }

    // ─── FEEDBACK ─────────────────────────────────────────────────────────────

    @GetMapping("/feedback/submission/{submission_id}")
    public ResponseEntity<List<Feedback>> getFeedbackBySubmission(@PathVariable("submission_id") Long submissionId) {
        return ResponseEntity.ok(assignmentService.getFeedbackBySubmission(submissionId));
    }

    @PostMapping("/feedback")
    public ResponseEntity<?> addFeedback(@RequestBody Feedback feedback) {
        Feedback saved = assignmentService.addFeedback(feedback);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "success", true,
                "message", "Feedback added and submission checked",
                "feedback_id", saved.getFeedbackId()
        ));
    }
}
