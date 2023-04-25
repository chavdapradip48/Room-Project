package com.pradip.roommanagementsystem.repository;

import com.pradip.roommanagementsystem.dto.projection.ExpenseProjection;
import com.pradip.roommanagementsystem.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ExpenseRepository extends JpaRepository<Expense,Long> {

    boolean existsByUserId(Long userId);

    <T> List<T> findAllBy(Class<T> projectionType);
    <T> Optional<T> findById(Long id, Class<T> type);

    List<ExpenseProjection> findByUserId(Long id);

    void deleteByUserId(Long userId);
}
