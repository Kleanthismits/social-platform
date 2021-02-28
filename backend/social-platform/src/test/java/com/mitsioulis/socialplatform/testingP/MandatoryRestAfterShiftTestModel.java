package com.mitsioulis.socialplatform.testingP;

import java.time.LocalDate;

public class MandatoryRestAfterShiftTestModel extends RuleModel {

	private LocalDate shiftStartDate;
	private LocalDate shiftEndDate;
	private String    absence;

	public MandatoryRestAfterShiftTestModel(int employeeId, String sectorId, LocalDate shiftStartDate,
			LocalDate shiftEndDate, LocalDate businessDate, boolean expectingError) {
		super(employeeId, sectorId, businessDate, expectingError);
		this.shiftStartDate = shiftStartDate;
		this.shiftEndDate = shiftEndDate;
	}

	public LocalDate getShiftStartDate() {
		return shiftStartDate;
	}

	public void setShiftStartDate(LocalDate shiftStartDate) {
		this.shiftStartDate = shiftStartDate;
	}

	public LocalDate getShiftEndDate() {
		return shiftEndDate;
	}

	public void setShiftEndDate(LocalDate shiftEndDate) {
		this.shiftEndDate = shiftEndDate;
	}

	@Override
	public String toString() {
		return "MandatoryRestAfterShiftTestModel [shiftStartDate=" + shiftStartDate + ", shiftEndDate=" + shiftEndDate
				+ ", absence=" + absence + ", expectingError=" + expectingError + ", employeeId=" + employeeId
				+ ", sectorId=" + sectorId + ", businessDate=" + businessDate + "]";
	}

}
