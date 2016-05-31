package dsms.rmi.objects;

import java.util.Date;

/**
 * /**
 * Stores Nurse's record
 *
 */
 

public class NurseRecord extends Practitioner
{

	String designation;
	String status;
	Date statusDate;
	
	public NurseRecord(String recordID,String firstName, String lastName, String designation, String status, Date statusDate)	
	{
		super(recordID,firstName,lastName);
		this.designation=designation;
		this.status=status;
		this.statusDate=statusDate;
	}

	public String getDesignation() 
	{
		return designation;
	}
	public void setDesignation(String designation) 
	{
		this.designation = designation;
	}
	public String getStatus() 
	{
		return status;
	}
	public void setStatus(String status) 
	{
		this.status = status;
	}
	public Date getStatusDate() 
	{
		return statusDate;
	}
	public void setStatusDate(Date statusDate) 
	{
		this.statusDate = statusDate;
	}
}
