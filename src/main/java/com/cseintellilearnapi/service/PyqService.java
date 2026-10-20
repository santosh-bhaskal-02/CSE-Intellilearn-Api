package com.cseintellilearnapi.service;

import com.cseintellilearnapi.entity.PreviousYearQuestion;
import com.cseintellilearnapi.exception.BadRequestException;
import com.cseintellilearnapi.repository.PreviousYearQuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PyqService {

    private final PreviousYearQuestionRepository pyqRepository;

    public PyqService(PreviousYearQuestionRepository pyqRepository) {
        this.pyqRepository = pyqRepository;
    }

    @Transactional(readOnly = true)
    public List<PreviousYearQuestion> getPyqsBySubject(Long subjectId) {
        return pyqRepository.findBySubjectIdOrderByExamYearDesc(subjectId);
    }

    public PreviousYearQuestion addPyq(PreviousYearQuestion pyq) {
        if (pyq.getSubjectId() == null || pyq.getExamYear() == null) {
            throw new BadRequestException("Subject and exam year are required");
        }
        return pyqRepository.save(pyq);
    }
}
