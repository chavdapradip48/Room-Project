package com.pradip.roommanagementsystem.repository;

import com.pradip.roommanagementsystem.dto.PaymentMode;
import com.pradip.roommanagementsystem.dto.projection.ExpenseProjection;
import com.pradip.roommanagementsystem.entity.Expense;
import com.pradip.roommanagementsystem.entity.ExpenseCalculator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ExpenseCalculatorRepository extends JpaRepository<ExpenseCalculator,Long> {
}