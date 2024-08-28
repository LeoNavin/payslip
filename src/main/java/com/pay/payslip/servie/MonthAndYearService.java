/**
 * 
 */
package com.pay.payslip.servie;

import java.util.List;
import java.util.Optional;

import com.pay.payslip.model.MonthAndYear;

/**
 * @author Leo Navin
 *
 */
public interface MonthAndYearService {

	void saveMonthAndYear(MonthAndYear monthAndYear);

	Optional<MonthAndYear> getByMonthAndYear(Long id, String year);

	List<MonthAndYear> findAllMonthAndYear();

	void deleteById(Long id);

	MonthAndYear getMonthById(Long id);

//	MonthAndYear findById(Long monthId);





}
