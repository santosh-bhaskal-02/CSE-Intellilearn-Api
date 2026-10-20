package com.cseintellilearnapi.service;

import com.cseintellilearnapi.entity.Doubt;
import com.cseintellilearnapi.exception.BadRequestException;
import com.cseintellilearnapi.exception.ResourceNotFoundException;
import com.cseintellilearnapi.repository.DoubtRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class DoubtService {

    private final DoubtRepository doubtRepository;

    public DoubtService(DoubtRepository doubtRepository) {
        this.doubtRepository = doubtRepository;
    }

    @Transactional(readOnly = true)
    public List<Doubt> getAllDoubts() {
        return doubtRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public List<Doubt> getDoubtsByStudent(Long studentId) {
        return doubtRepository.findByStudentIdOrderByCreatedAtDesc(studentId);
    }

    public Doubt submitDoubt(Doubt doubt) {
        if (doubt.getStudentId() == null || doubt.getTitle() == null || doubt.getQuestion() == null) {
            throw new BadRequestException("Student, title and question are required");
        }
        doubt.setStatus("Waiting for Answer");
        return doubtRepository.save(doubt);
    }

    public void answerDoubt(Long doubtId, String answer, Long teacherId) {
        if (answer == null || teacherId == null) {
            throw new BadRequestException("Answer and teacher are required");
        }

        Doubt doubt = doubtRepository.findById(doubtId)
                .orElseThrow(() -> new ResourceNotFoundException("Doubt", "id", doubtId));

        doubt.setAnswer(answer);
        doubt.setTeacherId(teacherId);
        doubt.setStatus("Answered");
        doubt.setAnsweredAt(LocalDateTime.now());
        doubtRepository.save(doubt);
    }
}
