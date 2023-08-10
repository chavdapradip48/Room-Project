package com.pradip.roommanagementsystem.repository;

import com.pradip.roommanagementsystem.dto.PaymentMode;
import com.pradip.roommanagementsystem.dto.projection.ExpenseProjection;
import com.pradip.roommanagementsystem.entity.Expense;
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
public interface ExpenseRepository extends JpaRepository<Expense,Long> {

    boolean existsByUserId(Long userId);
    <T> List<T> findAllBy(Class<T> projectionType);
    <T> List<T> findAllByOrderByCreatedAtDesc(Class<T> projectionType);

//    @Query("SELECT NEW com.example.UserWithAddresses(u.id, u.firstName, u.lastName, NEW com.example.AddressDTO(a.street, a.city)) " +
//            "FROM User u LEFT JOIN u.addresses a ORDER BY u.createdDate DESC")
//    List<ExpenseProjection> findAllByOrderByCreatedAtDescQuery();
    <T> Optional<T> findById(Long id, Class<T> type);
//    ExpenseProjection findById(Long id);
    List<ExpenseProjection> findByUserId(Long id);
    List<ExpenseProjection> findByUserIdOrderByCreatedAtDesc(Long id);

    void deleteByUserId(Long userId);

    @Query("SELECT MONTH(e.createdAt) AS month, SUM(e.amount) AS totalAmount FROM Expense e " +
            "WHERE YEAR(e.createdAt) = :year GROUP BY MONTH(e.createdAt)")
    List<Object[]> findMonthlyExpenseDataByYear(@Param("year") int year);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE YEAR(e.createdAt) = :year")
    Long sumAmountByYear(@Param("year") int year);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE YEAR(e.createdAt) = :year AND MONTH(e.createdAt) = :month")
    Long sumAmountByYearAndMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user.email = :email " +
            "AND YEAR(e.createdAt) = :year AND MONTH(e.createdAt) = :month")
    Long sumAmountByUserEmailAndMonth(@Param("email") String email, @Param("year") int year, @Param("month") int month);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.createdAt >= :fromDate " +
            "AND e.createdAt <= :toDate AND e.paymentMode IN :payments")
    Long sumByAmountFromToAndPaymentMode(@Param("fromDate") Date fromDate,
                                         @Param("toDate") Date toDate, @Param("payments") List<PaymentMode> payments);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.createdAt >= :fromDate " +
            "AND e.createdAt <= :toDate AND e.paymentMode IN :payments AND e.user.id = :id")
    int sumByAmountFromToAndPaymentModeMy(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
                                           @Param("payments") List<PaymentMode> payments, @Param("id") Long id);
}