package dsms.rmi.objects;

import java.util.Date;

public class NurseRecord {
	
	String firstName;
	String lastName;
	String designation;
	String status;
	Date statusDate;
	
	NurseRecord(String firstName, String lastName, String designation, String status, Date statusDate)	{
		this.firstName=firstName;
		this.lastName=lastName;
		this.designation=designation;
		this.status=status;
		this.statusDate=statusDate;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getStatusDate() {
		return statusDate;
	}
	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}

}
