/**
 * 
 */
package com.pay.payslip.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pay.payslip.model.MonthAndYear;
import com.pay.payslip.model.PaySlip;

/**
 * @author Leo Navin
 *
 */
public interface PaySlipRepository extends JpaRepository<PaySlip, Long> {
	


	@Query("Select pay from PaySlip pay Join pay.user u Where u.associateId=:associateId ")
	List<PaySlip> findByAssociateId(@Param("associateId") String associateId);

	@Query("Select pay from PaySlip pay Join pay.monthAndYear m Where m.id=:id ")
	List<PaySlip> findByMonthAndYearId(@Param("id") Long id);

//	@Query("Select pay from PaySlip pay Join pay.monthAndYear m Where m.month.id=:monthAndYear.month.id ")
//	List<PaySlip> findByMonthAndYear(MonthAndYear monthAndYear);


	@Query("Select pay from PaySlip pay Join pay.user u  Join pay.monthAndYear mon Where u.associateId=:associateId AND mon.id=:id")
	PaySlip findByAssociateIdAndMonthandYearId(@Param("associateId")String associateId,@Param("id") Long id);

	@Query("Select pay from PaySlip pay Where pay.id=:paySlipId And pay.monthAndYear.id=:monthAndYearID")
	PaySlip findByIdAndMonthandYaerById(Long paySlipId, Long monthAndYearID);

	



	



}
