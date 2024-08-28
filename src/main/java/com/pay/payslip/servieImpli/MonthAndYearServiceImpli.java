/**
 * 
 */
package com.pay.payslip.servieImpli;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pay.payslip.model.MonthAndYear;
import com.pay.payslip.repository.MonthAndYearRepository;
import com.pay.payslip.servie.MonthAndYearService;

/**
 * @author Leo Navin
 *
 */
@Service
public class MonthAndYearServiceImpli implements MonthAndYearService {

	@Autowired
	private MonthAndYearRepository monthAndYearRepository;

	@Override
	public void saveMonthAndYear(MonthAndYear monthAndYear) {
		monthAndYearRepository.saveAndFlush(monthAndYear);
	}

	@Override
	public Optional<MonthAndYear> getByMonthAndYear(Long id, String year) {
		return monthAndYearRepository.findByMonthAndYear(id, year);
	}

	@Override
	public List<MonthAndYear> findAllMonthAndYear() {
		return monthAndYearRepository.findAllByOrderByIdDesc();
	}

	@Override
	public void deleteById(Long id) {
		monthAndYearRepository.deleteById(id);
	}

	@Override
	public MonthAndYear getMonthById(Long id) {
		return monthAndYearRepository.findById(id).get();
	}

	


}
