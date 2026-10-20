package com.cseintellilearnapi.repository;

import com.cseintellilearnapi.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {
    List<Unit> findAllByOrderByUnitIdAsc();
    List<Unit> findBySubjectIdOrderByUnitNumberAsc(Long subjectId);
}
