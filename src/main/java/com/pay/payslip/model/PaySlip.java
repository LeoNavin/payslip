/**
 * 
 */
package com.pay.payslip.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Leo Navin
 *
 */
@Entity
@Table(name = "tbl_payslip")
public class PaySlip extends AbstractAuditingEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
//    @Column(name = "gross", columnDefinition="LONGBLOB")
//	private String gross;
//	private Blob gross;
    @Column(name = "basic", columnDefinition="LONGBLOB")
	private String basic;
    @Column(name = "hRA", columnDefinition="LONGBLOB")
	private String hRA;/* House Rent Allowance*/
    @Column(name = "lTA", columnDefinition="LONGBLOB")
	private String lTA;/* Leave Travel Allowance*/
    @Column(name = "medicalAll", columnDefinition="LONGBLOB")
	private String medicalAll;
    @Column(name = "conveyance", columnDefinition="LONGBLOB")
	private String conveyance;
    @Column(name = "splAllow", columnDefinition="LONGBLOB")
	private String splAllow;
    @Column(name = "grossEarning", columnDefinition="LONGBLOB")
	private String grossEarning;
    @Column(name = "daysMonth", columnDefinition="LONGBLOB")
	private String daysMonth;
    @Column(name = "daysWorked", columnDefinition="LONGBLOB")
	private String daysWorked;
    @Column(name = "lop", columnDefinition="LONGBLOB")
	private String lop;/* Loss of pay*/
    @Column(name = "pf", columnDefinition="LONGBLOB")
	private String pf;/*  Provident Fund */
    @Column(name = "tDS", columnDefinition="LONGBLOB")
	private String tDS;/* Tax Deducted at Source */
    @Column(name = "mess", columnDefinition="LONGBLOB")
	private String mess;
    @Column(name = "hostel", columnDefinition="LONGBLOB")
	private String hostel;
    @Column(name = "sBF", columnDefinition="LONGBLOB")
	private String sBF;/* Staff Benefit Fund */
    @Column(name = "advanceLoan", columnDefinition="LONGBLOB")
	private String advanceLoan;
    @Column(name = "insurance", columnDefinition="LONGBLOB")
	private String insurance;
//    @Column(name = "cTC", columnDefinition="LONGBLOB")
//	private String cTC;/*Cost To Company */
    @Column(name = "totalDeduction", columnDefinition="LONGBLOB")
	private String totalDeduction;
    @Column(name = "netPay", columnDefinition="LONGBLOB")
	private String netPay;
    @Column(name = "cityAllowance", columnDefinition="LONGBLOB")
	private String cityAllowance;

//	private String epfGross;
//	private String pfEmployerContribution;
//	private String part1;
//	private String part2;
//	private String amount;
	
	@ManyToOne
	private User user;
	
	@Transient
	private Long userId;
	
	@ManyToOne
	private MonthAndYear monthAndYear;
	
	@Transient
	private Long monthAndYearId;

	
	@Transient
	private boolean deleteStatus;
	
	@Override
	public String toString() {
		return "PaySlip [id=" + id + ", basic=" + basic + ", hRA=" + hRA + ", lTA=" + lTA + ", medicalAll=" + medicalAll
				+ ", conveyance=" + conveyance + ", splAllow=" + splAllow + ", grossEarning=" + grossEarning
				+ ", daysMonth=" + daysMonth + ", daysWorked=" + daysWorked + ", lop=" + lop + ", pf=" + pf + ", tDS="
				+ tDS + ", mess=" + mess + ", hostel=" + hostel + ", sBF=" + sBF + ", advanceLoan=" + advanceLoan
				+ ", insurance=" + insurance + ", totalDeduction=" + totalDeduction + ", netPay=" + netPay
				+ ", cityAllowance=" + cityAllowance + ", user=" + user + ", userId=" + userId + ", monthAndYear="
				+ monthAndYear + ", monthAndYearId=" + monthAndYearId + ", deleteStatus=" + deleteStatus + "]";
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public boolean isDeleteStatus() {
		return deleteStatus;
	}


	public void setDeleteStatus(boolean deleteStatus) {
		this.deleteStatus = deleteStatus;
	}


	public String getBasic() {
		return basic;
	}


	


	public void setBasic(String basic) {
		this.basic = basic;
	}


	public String gethRA() {
		return hRA;
	}


	public void sethRA(String hRA) {
		this.hRA = hRA;
	}


	public String getlTA() {
		return lTA;
	}


	public void setlTA(String lTA) {
		this.lTA = lTA;
	}


	public String getMedicalAll() {
		return medicalAll;
	}


	public void setMedicalAll(String medicalAll) {
		this.medicalAll = medicalAll;
	}


	public String getConveyance() {
		return conveyance;
	}


	public void setConveyance(String conveyance) {
		this.conveyance = conveyance;
	}


	public String getSplAllow() {
		return splAllow;
	}


	public void setSplAllow(String splAllow) {
		this.splAllow = splAllow;
	}


	public String getGrossEarning() {
		return grossEarning;
	}


	public void setGrossEarning(String grossEarning) {
		this.grossEarning = grossEarning;
	}


	public String getDaysMonth() {
		return daysMonth;
	}


	public void setDaysMonth(String daysMonth) {
		this.daysMonth = daysMonth;
	}


	public String getDaysWorked() {
		return daysWorked;
	}


	public void setDaysWorked(String daysWorked) {
		this.daysWorked = daysWorked;
	}


	public String getLop() {
		return lop;
	}


	public void setLop(String lop) {
		this.lop = lop;
	}


	

	public String getPf() {
		return pf;
	}


	public void setPf(String pf) {
		this.pf = pf;
	}


	

	


	public String gettDS() {
		return tDS;
	}


	public void settDS(String tDS) {
		this.tDS = tDS;
	}


	public String getMess() {
		return mess;
	}


	public void setMess(String mess) {
		this.mess = mess;
	}


	public String getHostel() {
		return hostel;
	}


	public void setHostel(String hostel) {
		this.hostel = hostel;
	}


	public String getsBF() {
		return sBF;
	}


	public void setsBF(String sBF) {
		this.sBF = sBF;
	}


	public String getAdvanceLoan() {
		return advanceLoan;
	}


	public void setAdvanceLoan(String advanceLoan) {
		this.advanceLoan = advanceLoan;
	}





	public String getInsurance() {
		return insurance;
	}


	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}


	public String getTotalDeduction() {
		return totalDeduction;
	}


	public void setTotalDeduction(String totalDeduction) {
		this.totalDeduction = totalDeduction;
	}


	public String getNetPay() {
		return netPay;
	}


	public void setNetPay(String netPay) {
		this.netPay = netPay;
	}


	


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public MonthAndYear getMonthAndYear() {
		return monthAndYear;
	}


	public void setMonthAndYear(MonthAndYear monthAndYear) {
		this.monthAndYear = monthAndYear;
	}


	public Long getMonthAndYearId() {
		return monthAndYearId;
	}


	public void setMonthAndYearId(Long monthAndYearId) {
		this.monthAndYearId = monthAndYearId;
	}


	public String getCityAllowance() {
		return cityAllowance;
	}


	public void setCityAllowance(String cityAllowance) {
		this.cityAllowance = cityAllowance;
	}
	
	
	
	
	

}
