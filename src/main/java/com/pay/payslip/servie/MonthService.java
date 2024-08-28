/**
 * 
 */
package com.pay.payslip.servie;

import java.util.List;
import java.util.Optional;

import com.pay.payslip.model.Month;

/**
 * @author Leo Navin
 *
 */
public interface MonthService {

	List<Month> findByAllMonth();

	Optional<Month> findByMonthId(Long monthId);

}
