package dsms.rmi.objects;

public class DoctorRecord {
	
	String firstName;
	String lastName;
	String address;
	String phone;
	String specialization;
	String location;
	
	DoctorRecord(String firstName, String lastName, String address, String phone, String specialization,
			String location) {
		this.firstName=firstName;
		this.lastName=lastName;
		this.address=address;
		this.phone=phone;
		this.specialization=specialization;
		this.location=location;
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
