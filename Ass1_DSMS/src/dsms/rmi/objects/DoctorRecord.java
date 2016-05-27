package dsms.rmi.objects;

public class DoctorRecord extends Practitioner{

	String address;
	String phone;
	String specialization;
	String location;
	
	
	public DoctorRecord(String recordID,String firstName, String lastName, String address, String phone, String specialization,
			String location) {
		
		super(recordID,firstName,lastName);
		this.firstName=firstName;
		this.lastName=lastName;
		this.address=address;
		this.phone=phone;
		this.specialization=specialization;
		this.location=location;
	}

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getSpecialization() {
		return specialization;
	}
	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}

}
