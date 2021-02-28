package com.mitsioulis.socialplatform.testingP;

import java.time.LocalDate;

public class RuleModel {

	protected boolean   expectingError;
	protected int       employeeId;
	protected String    sectorId;
	protected LocalDate businessDate;

	public RuleModel(int employeeId, String sectorId, LocalDate businessDate, boolean expectingError) {
		super();
		this.expectingError = expectingError;
		this.employeeId = employeeId;
		this.sectorId = sectorId;
		this.businessDate = businessDate;
	}

	public RuleModel() {
		super();
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getSectorId() {
		return sectorId;
	}

	public void setSectorId(String sectorId) {
		this.sectorId = sectorId;
	}

	public LocalDate getBusinessDate() {
		return businessDate;
	}

	public void setBusinessDate(LocalDate businessDate) {
		this.businessDate = businessDate;
	}

	public boolean isExpectingError() {
		return expectingError;
	}

	public void setExpectingError(boolean expectingError) {
		this.expectingError = expectingError;
	}

}