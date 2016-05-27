package dsms.rmi.intermediate;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

public interface ManagerInterface extends Remote 
{
	public boolean createDRecord (String firstName, String lastName, String address,String phone,String specialization, String location)throws RemoteException;
	public boolean createNRecord (String firstName, String lastName, String designation, String status, Date statusDate) throws RemoteException;
	public String getRecordCounts (String recordType) throws RemoteException;
	public boolean editRecord (String recordID, String fieldName, String newValue)throws RemoteException;

}
