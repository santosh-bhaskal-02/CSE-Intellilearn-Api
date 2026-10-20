package com.cseintellilearnapi.service;

import com.cseintellilearnapi.entity.*;
import com.cseintellilearnapi.exception.BadRequestException;
import com.cseintellilearnapi.exception.ResourceNotFoundException;
import com.cseintellilearnapi.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AcademicService {

    private final AcademicYearRepository yearRepository;
    private final SemesterRepository semesterRepository;
    private final SubjectRepository subjectRepository;
    private final UnitRepository unitRepository;
    private final TopicRepository topicRepository;

    public AcademicService(AcademicYearRepository yearRepository,
                           SemesterRepository semesterRepository,
                           SubjectRepository subjectRepository,
                           UnitRepository unitRepository,
                           TopicRepository topicRepository) {
        this.yearRepository = yearRepository;
        this.semesterRepository = semesterRepository;
        this.subjectRepository = subjectRepository;
        this.unitRepository = unitRepository;
        this.topicRepository = topicRepository;
    }

    private static final List<Semester> SEMESTERS = List.of(
        new Semester(1L, 1L, 1, "Semester 1"),
        new Semester(2L, 1L, 2, "Semester 2"),
        new Semester(3L, 2L, 3, "Semester 3"),
        new Semester(4L, 2L, 4, "Semester 4"),
        new Semester(5L, 3L, 5, "Semester 5"),
        new Semester(6L, 3L, 6, "Semester 6"),
        new Semester(7L, 4L, 7, "Semester 7"),
        new Semester(8L, 4L, 8, "Semester 8")
    );

    // ─── Query / Read Operations ─────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<AcademicYear> getAllYears() {
        return yearRepository.findAllByOrderByYearIdAsc();
    }

    public List<Semester> getAllSemesters() {
        return SEMESTERS;
    }

    public List<Semester> getSemestersByYear(Long yearId) {
        if (yearId == null) {
            return SEMESTERS;
        }
        List<Semester> filtered = SEMESTERS.stream()
                .filter(s -> s.getYearId().equals(yearId))
                .toList();
        return filtered.isEmpty() ? SEMESTERS : filtered;
    }

    @Transactional(readOnly = true)
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAllByOrderBySubjectIdAsc();
    }

    @Transactional(readOnly = true)
    public List<Subject> getSubjectsBySemester(Long semesterId) {
        return subjectRepository.findBySemesterIdOrderBySubjectIdAsc(semesterId);
    }

    @Transactional(readOnly = true)
    public List<Unit> getAllUnits() {
        return unitRepository.findAllByOrderByUnitIdAsc();
    }

    @Transactional(readOnly = true)
    public List<Unit> getUnitsBySubject(Long subjectId) {
        return unitRepository.findBySubjectIdOrderByUnitNumberAsc(subjectId);
    }

    @Transactional(readOnly = true)
    public List<Topic> getAllTopics() {
        return topicRepository.findAllByOrderByTopicIdAsc();
    }

    @Transactional(readOnly = true)
    public List<Topic> getTopicsByUnit(Long unitId) {
        return topicRepository.findByUnitIdOrderByTopicNumberAsc(unitId);
    }

    @Transactional(readOnly = true)
    public Topic getTopicById(Long topicId) {
        return topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic", "id", topicId));
    }

    // ─── Admin Master Mutation Operations ───────────────────────────────────

    public AcademicYear createYear(AcademicYear year) {
        if (year.getYearName() == null || year.getYearName().trim().isEmpty()) {
            throw new BadRequestException("Year name is required");
        }
        return yearRepository.save(year);
    }

    public void deleteYear(Long id) {
        if (!yearRepository.existsById(id)) {
            throw new ResourceNotFoundException("AcademicYear", "id", id);
        }
        yearRepository.deleteById(id);
    }

    public Semester createSemester(Semester semester) {
        if (semester.getYearId() == null || semester.getSemesterNumber() == null) {
            throw new BadRequestException("Year ID and semester number are required");
        }
        return semesterRepository.save(semester);
    }

    public void deleteSemester(Long id) {
        if (!semesterRepository.existsById(id)) {
            throw new ResourceNotFoundException("Semester", "id", id);
        }
        semesterRepository.deleteById(id);
    }

    public Subject createSubject(Subject subject) {
        if (subject.getSemesterId() == null || subject.getSubjectName() == null || subject.getSubjectName().trim().isEmpty()) {
            throw new BadRequestException("Semester ID and subject name are required");
        }
        return subjectRepository.save(subject);
    }

    public void deleteSubject(Long id) {
        if (!subjectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Subject", "id", id);
        }
        subjectRepository.deleteById(id);
    }

    public Unit createUnit(Unit unit) {
        if (unit.getSubjectId() == null || unit.getUnitNumber() == null || unit.getUnitName() == null) {
            throw new BadRequestException("Subject ID, unit number, and unit name are required");
        }
        return unitRepository.save(unit);
    }

    public void deleteUnit(Long id) {
        if (!unitRepository.existsById(id)) {
            throw new ResourceNotFoundException("Unit", "id", id);
        }
        unitRepository.deleteById(id);
    }

    public Topic createTopic(Topic topic) {
        if (topic.getUnitId() == null || topic.getTopicName() == null) {
            throw new BadRequestException("Unit ID and topic name are required");
        }
        return topicRepository.save(topic);
    }

    public void deleteTopic(Long id) {
        if (!topicRepository.existsById(id)) {
            throw new ResourceNotFoundException("Topic", "id", id);
        }
        topicRepository.deleteById(id);
    }
}
