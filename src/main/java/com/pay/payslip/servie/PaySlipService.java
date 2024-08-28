/**
 * 
 */
package com.pay.payslip.servie;

import java.util.List;

import com.pay.payslip.model.MonthAndYear;
import com.pay.payslip.model.PaySlip;

/**
 * @author Leo Navin
 *
 */
public interface PaySlipService {

	void addPaySlip(PaySlip employeePaySlip);

	PaySlip findByAssociateIdAndMonthId(String associateId, Long id);
	
	List<PaySlip> findByAssociateId(String associateId);


	List<PaySlip> findByAllPaySlip(Long id);

	PaySlip findByIdAndMonthandYaerById(Long paySlipId, Long monthAndYearID);

	void deletePayslipById(Long id);




//	PaySlip findByMonthandYear(MonthAndYear monthandYear);

//	List<PaySlip> getByMonthAndYear(MonthAndYear monthandYear);



}
