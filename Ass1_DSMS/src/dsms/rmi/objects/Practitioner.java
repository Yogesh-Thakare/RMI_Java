package dsms.rmi.objects;


/**
 * Parent class for storing object in HashMap
 *
 */
public class Practitioner {

	String recordID;
	String firstName;
	String lastName;
	
	public Practitioner(String recordID,String firstName,String lastName)
	{
		this.recordID=recordID;
		this.firstName=firstName;
		this.lastName=lastName;
	}
	
	public String getRecordID() 
	{
		return recordID;
	}
	public void setRecordID(String recordID) 
	{
		this.recordID = recordID;
	}
	public String getFirstName() 
	{
		return firstName;
	}
	public void setFirstName(String firstName) 
	{
		this.firstName = firstName;
	}
	public String getLastName() 
	{
		return lastName;
	}
	public void setLastName(String lastName) 
	{
		this.lastName = lastName;
	}
}
