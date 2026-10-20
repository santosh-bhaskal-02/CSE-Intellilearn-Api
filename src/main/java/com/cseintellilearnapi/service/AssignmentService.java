package com.cseintellilearnapi.service;

import com.cseintellilearnapi.entity.Assignment;
import com.cseintellilearnapi.entity.AssignmentSubmission;
import com.cseintellilearnapi.entity.Feedback;
import com.cseintellilearnapi.exception.BadRequestException;
import com.cseintellilearnapi.exception.ConflictException;
import com.cseintellilearnapi.exception.ResourceNotFoundException;
import com.cseintellilearnapi.repository.AssignmentRepository;
import com.cseintellilearnapi.repository.AssignmentSubmissionRepository;
import com.cseintellilearnapi.repository.FeedbackRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AssignmentService {

    private static final List<String> ALLOWED_STATUSES = List.of("Pending", "Submitted", "Checked");

    private final AssignmentRepository assignmentRepository;
    private final AssignmentSubmissionRepository submissionRepository;
    private final FeedbackRepository feedbackRepository;

    public AssignmentService(AssignmentRepository assignmentRepository,
                             AssignmentSubmissionRepository submissionRepository,
                             FeedbackRepository feedbackRepository) {
        this.assignmentRepository = assignmentRepository;
        this.submissionRepository = submissionRepository;
        this.feedbackRepository = feedbackRepository;
    }

    // ─── Assignment Operations ───────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public List<Assignment> getAssignmentsByTopic(Long topicId) {
        return assignmentRepository.findByTopicIdOrderByCreatedAtDesc(topicId);
    }

    public Assignment createAssignment(Assignment assignment) {
        if (assignment.getTitle() == null || assignment.getTitle().trim().isEmpty() ||
            assignment.getSubjectId() == null || assignment.getTeacherId() == null ||
            assignment.getDueDate() == null) {
            throw new BadRequestException("Title, subject, teacher and due date are required");
        }
        return assignmentRepository.save(assignment);
    }

    // ─── Submission Operations ───────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<AssignmentSubmission> getSubmissionsByAssignment(Long assignmentId) {
        return submissionRepository.findByAssignmentIdOrderBySubmittedAtDesc(assignmentId);
    }

    @Transactional(readOnly = true)
    public List<AssignmentSubmission> getStudentSubmissions(Long studentId) {
        return submissionRepository.findByStudentIdOrderBySubmittedAtDesc(studentId);
    }

    public AssignmentSubmission submitAssignment(AssignmentSubmission submission) {
        if (submission.getAssignmentId() == null || submission.getStudentId() == null) {
            throw new BadRequestException("Assignment and student are required");
        }

        Optional<AssignmentSubmission> existing = submissionRepository.findByAssignmentIdAndStudentId(
                submission.getAssignmentId(), submission.getStudentId()
        );

        if (existing.isPresent()) {
            throw new ConflictException("You have already submitted this assignment");
        }

        submission.setStatus("Submitted");
        return submissionRepository.save(submission);
    }

    public void updateSubmissionStatus(Long submissionId, String status) {
        if (status == null || !ALLOWED_STATUSES.contains(status)) {
            throw new BadRequestException("Invalid status: " + status);
        }

        AssignmentSubmission sub = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new ResourceNotFoundException("AssignmentSubmission", "id", submissionId));

        sub.setStatus(status);
        submissionRepository.save(sub);
    }

    // ─── Feedback Operations ─────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<Feedback> getFeedbackBySubmission(Long submissionId) {
        return feedbackRepository.findBySubmissionIdOrderByReviewedAtDesc(submissionId);
    }

    public Feedback addFeedback(Feedback feedback) {
        if (feedback.getSubmissionId() == null || feedback.getTeacherId() == null) {
            throw new BadRequestException("Submission and teacher are required");
        }

        AssignmentSubmission sub = submissionRepository.findById(feedback.getSubmissionId())
                .orElseThrow(() -> new ResourceNotFoundException("AssignmentSubmission", "id", feedback.getSubmissionId()));

        Feedback saved = feedbackRepository.save(feedback);

        // Mark associated submission as Checked within the same transactional boundary
        sub.setStatus("Checked");
        submissionRepository.save(sub);

        return saved;
    }
}
