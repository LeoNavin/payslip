/**
 * 
 */
package com.pay.payslip.servieImpli;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pay.payslip.model.PaySlip;
import com.pay.payslip.repository.PaySlipRepository;
import com.pay.payslip.servie.PaySlipService;

/**
 * @author Leo Navin
 *
 */
@Service
public class PaySlipServiceImpli  implements PaySlipService{
	
	@Autowired
	private PaySlipRepository paySlipRepository;

	@Override
	public void addPaySlip(PaySlip employeePaySlip) {
		paySlipRepository.saveAndFlush(employeePaySlip);
		
	}

	@Override
	public PaySlip findByAssociateIdAndMonthId(String associateId, Long id) {
		return 	paySlipRepository.findByAssociateIdAndMonthandYearId(associateId,id);

	}

	
	@Override
	public List<PaySlip> findByAssociateId(String associateId) {
		return 	paySlipRepository.findByAssociateId(associateId);
	}
	
	
	@Override
	public List<PaySlip> findByAllPaySlip( Long id) {
		return paySlipRepository.findByMonthAndYearId(id);
	}
	

	@Override
	public PaySlip findByIdAndMonthandYaerById(Long paySlipId, Long monthAndYearID) {
		return paySlipRepository.findByIdAndMonthandYaerById(paySlipId,monthAndYearID);
	}

	@Override
	public void deletePayslipById(Long id) {
		paySlipRepository.deleteById(id);		
	}

	

//	@Override
//	public List<PaySlip> getByMonthAndYear(MonthAndYear monthAndYear) {
//		return paySlipRepository.findByMonthAndYear(monthAndYear);
//		
//	}
	

	
	
	


}
