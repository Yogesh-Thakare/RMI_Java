package dsms.rmi.objects;

/**
 * Stores Doctor's record
 */
public class DoctorRecord extends Practitioner
{
	String address;
	String phone;
	String specialization;
	String location;

	/**
	 * Constructor
	 * @param recordID
	 * @param firstName
	 * @param lastName
	 * @param address
	 * @param phone
	 * @param specialization
	 * @param location
	 */
	public DoctorRecord(String recordID,String firstName, String lastName, String address, String phone, String specialization,
			String location) 
	{	
		super(recordID,firstName,lastName);
		this.firstName=firstName;
		this.lastName=lastName;
		this.address=address;
		this.phone=phone;
		this.specialization=specialization;
		this.location=location;
	}

	/** 
	 * @return address
	 */
	public String getAddress() 
	{
		return address;
	}


	/**
	 * Set address method
	 * @param address
	 */
	public void setAddress(String address) 
	{
		this.address = address;
	}

	/**
	 * @return phone no.
	 */
	public String getPhone() 
	{
		return phone;
	}

	/**
	 * Set phone no.
	 * @param phone
	 */
	public void setPhone(String phone) 
	{
		this.phone = phone;
	}


	/**
	 * Get specialization
	 * @return
	 */
	public String getSpecialization() 
	{
		return specialization;
	}


	/**
	 * Set specialization method
	 * @param specialization
	 */
	public void setSpecialization(String specialization) 
	{
		this.specialization = specialization;
	}


	/**
	 * Get location
	 * @return
	 */
	public String getLocation() 
	{
		return location;
	}


	/**
	 * set location method
	 * @param location
	 */
	public void setLocation(String location) 
	{
		this.location = location;
	}
}
