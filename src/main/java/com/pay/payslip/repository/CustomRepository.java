/**
 * 
 */
package com.pay.payslip.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.pay.payslip.model.MonthAndYear;

/**
 * @author Leo Navin
 *
 */
@Repository
public class CustomRepository {

	
	@Autowired
	private EntityManager entityManager;
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	
	@SuppressWarnings("unchecked")
	public  List<MonthAndYear> getMonthAndYearList(String orderCompletionReportQuery,int page){
		Query query = getEntityManager().createQuery(orderCompletionReportQuery).setMaxResults(page);
		List<MonthAndYear> orderCompletionList = query.getResultList();
		return orderCompletionList;
	}
	
	
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    public List<MonthAndYear> getMonthAndYearList(int page) {
//        return entityManager.createQuery("SELECT p FROM MonthAndYear p ORDER BY p.id DESC",
//        		MonthAndYear.class).setMaxResults(page).getResultList();
//    }
	
}
