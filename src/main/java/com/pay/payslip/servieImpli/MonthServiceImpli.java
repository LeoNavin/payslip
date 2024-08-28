/**
 * 
 */
package com.pay.payslip.servieImpli;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pay.payslip.model.Month;
import com.pay.payslip.repository.MonthRepositary;
import com.pay.payslip.servie.MonthService;

/**
 * @author Leo Navin
 *
 */
@Service
public class MonthServiceImpli implements MonthService {
	
	@Autowired
	private MonthRepositary monthRepositary;

	@Override
	public List<Month> findByAllMonth() {
		return monthRepositary.findAll();
	}

	@Override
	public Optional<Month> findByMonthId(Long monthId) {
		return monthRepositary.findById(monthId);
	}

}
