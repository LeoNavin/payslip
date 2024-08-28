/**
 * 
 */
package com.pay.payslip.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pay.payslip.model.Month;

/**
 * @author Leo Navin
 *
 */
public interface MonthRepositary extends JpaRepository<Month, Long> {

	Optional<Month> findByMonth(String monthValue);

}
