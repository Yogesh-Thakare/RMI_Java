package dsms.rmi.objects;


/**
 * Parent class for storing object in HashMap
 */
public class Practitioner {

	String recordID;
	String firstName;
	String lastName;

	/**
	 * Constructor
	 * @param recordID
	 * @param firstName
	 * @param lastName
	 */
	public Practitioner(String recordID,String firstName,String lastName)
	{
		this.recordID=recordID;
		this.firstName=firstName;
		this.lastName=lastName;
	}

	/**
	 * Get RecordID method
	 * @return
	 */
	public String getRecordID() 
	{
		return recordID;
	}


	/**
	 * 
	 * Set RecordID
	 * @param recordID
	 */
	public void setRecordID(String recordID) 
	{
		this.recordID = recordID;
	}


	/**
	 * Get first name method
	 * @return
	 */
	public String getFirstName() 
	{
		return firstName;
	}

	/**
	 * Set First Name method
	 * @param firstName
	 */
	public void setFirstName(String firstName) 
	{
		this.firstName = firstName;
	}


	/**
	 * Get Last name method
	 * @return
	 */
	public String getLastName() 
	{
		return lastName;
	}


	/**
	 * Set last name method
	 * @param lastName
	 */
	public void setLastName(String lastName) 
	{
		this.lastName = lastName;
	}
}
