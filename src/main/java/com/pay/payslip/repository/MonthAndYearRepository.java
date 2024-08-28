/**
 * 
 */
package com.pay.payslip.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pay.payslip.model.MonthAndYear;

/**
 * @author Leo Navin
 *
 */
public interface MonthAndYearRepository extends JpaRepository<MonthAndYear, Long> {

	@Query("SELECT monthyear FROM  MonthAndYear monthyear JOIN  monthyear.month mon Where mon.id=:id AND monthyear.year=:year")
	Optional<MonthAndYear> findByMonthAndYear(@Param("id") Long id,@Param("year") String year);

	List<MonthAndYear> findAllByOrderByIdDesc();




}
