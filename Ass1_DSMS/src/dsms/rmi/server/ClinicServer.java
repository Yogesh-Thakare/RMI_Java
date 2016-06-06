package dsms.rmi.server;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import dsms.rmi.intermediate.ManagerInterface;
import dsms.rmi.objects.DoctorRecord;
import dsms.rmi.objects.NurseRecord;
import dsms.rmi.objects.Practitioner;

/**
 * Server acts as a thread object and performs different operation requested by client
 *
 */
public class ClinicServer extends Thread implements ManagerInterface 
{
	private HashMap<Character, ArrayList<Practitioner>> practitionerRecords = 
			new HashMap<Character, ArrayList<Practitioner>>();
	private String clinicName;
	private static String staticRecordType;
	private int UDPPort;
	private static ArrayList<ClinicServer> clinicServers = null;
	private Logger logger;
	static int drRecord=10000;
	static int nrRecord=10000;
	
	/**
	 * default ctor
	 */
	public ClinicServer()
	{}
	
	/**
	 * @param create clinic with clinicName
	 */
	public ClinicServer(String clinicName) 
	{
		this.clinicName = clinicName;
		this.setLogger("logs/clinic/"+clinicName+".txt");
	}
	
	/**
	 * @param create clinic with clinicName
	 * @param portNumber UDP port for current clinic 
	 */
	public ClinicServer(String clinicName, int portNumber)  
	{
		this.clinicName = clinicName;
		UDPPort = portNumber;
		this.setLogger("logs/clinic/"+clinicName+".txt");
	}
	
	/**
	 * @param clinicName for which seperate log file created
	 */
	private void setLogger(String clinicName) 
	{
		try
		{
			this.logger = Logger.getLogger(this.clinicName);
			FileHandler fileTxt 	 = new FileHandler(clinicName);
			SimpleFormatter formatterTxt = new SimpleFormatter();
			fileTxt.setFormatter(formatterTxt);
			logger.addHandler(fileTxt);
		}
		catch(Exception err) 
		{
			System.out.println("Failed to initiate logger. Please check file permission");
		}
	}

	/**
	 * @return current server's UDP port
	 */
	public int getUDPPort()
	{
		return this.UDPPort;
	}
	
	/* Automatically called by jvm for current thread instance
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run()
	{
		DatagramSocket datagramSocket=null;
		try 
		{
			datagramSocket = new DatagramSocket(UDPPort);
			byte [] buffer = new byte [1000];
			DatagramPacket request;
			DatagramPacket reply;
			while(true)
			{
				request = new DatagramPacket(buffer, buffer.length);
				datagramSocket.receive(request);
				buffer = (clinicName + ":" + getRecordsFromServer(staticRecordType)).getBytes();
				reply = new DatagramPacket(buffer, buffer.length, request.getAddress(), request.getPort());
				datagramSocket.send(reply);
			}
		} 
		catch (SocketException e) 
		{
			System.out.println("SocketException : " + e.getMessage());
		} 
		catch (IOException e) 
		{
			System.out.println("IOException : " + e.getMessage());
		}
		finally
		{
			datagramSocket.close();
		}
	}
	
	/* 
	 * create new Doctor Record
	 * (non-Javadoc)
	 * @see dsms.rmi.intermediate.ManagerInterface#createDRecord(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean createDRecord(String firstName, String lastName, String address, String phone, String specialization,
			String location) throws RemoteException 
	{
		++drRecord;
		Practitioner Practitioner=new DoctorRecord("DR"+drRecord,firstName,lastName,address,phone,specialization,location);
		
		//use of synchronized block here lock is achieved on each record inside list 
		synchronized(practitionerRecords) 
		{
			
			ArrayList<Practitioner> practitionerList = practitionerRecords.get(lastName.charAt(0));
			if(practitionerList == null && checkUniqueRecord(Practitioner.getRecordID(),Practitioner.getFirstName(),Practitioner.getLastName(),lastName.charAt(0)))
			{
				practitionerList = new ArrayList<Practitioner>();
				practitionerList.add(Practitioner);
				practitionerRecords.put(lastName.charAt(0), practitionerList);
			}
			else if(checkUniqueRecord(Practitioner.getRecordID(),Practitioner.getFirstName(),Practitioner.getLastName(),lastName.charAt(0)))
			{
			practitionerList.add(Practitioner);
			}
			else
			{
				logger.info("Failed to add Doctor Record with record ID : "+Practitioner.getRecordID()+" duplicate record ID");
				return false;
			}
			logger.info("Doctor Record created :\nRecordID \"" +  "DR"+drRecord +  "\", FirstName \"" +  firstName + 
					"\", LastName \"" +  lastName +  "\", Address \"" +  address +  "\", Phone \"" + phone + "\", Specialization \"" + 
					specialization + "\", Location \""+location+"\"");
			
			return true;
		}	
	}
	
	/* 
	 * create new Nurse Record
	 * (non-Javadoc)
	 * @see dsms.rmi.intermediate.ManagerInterface#createNRecord(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Date)
	 */
	@Override
	public boolean createNRecord(String firstName, String lastName, String designation, String status, Date statusDate)
			throws RemoteException 
	{
		++nrRecord;
		Practitioner Practitioner=new NurseRecord("NR"+nrRecord,firstName,lastName,designation,status,statusDate);
		
		//use of synchronized block here lock is achieved on each record inside list 
		synchronized(practitionerRecords) 
		{
			ArrayList<Practitioner> practitionerList = practitionerRecords.get(lastName.charAt(0));
			if(practitionerList == null && checkUniqueRecord(Practitioner.getRecordID(),Practitioner.getFirstName(),Practitioner.getLastName(),lastName.charAt(0))) 
			{
				practitionerList = new ArrayList<Practitioner>();
				practitionerList.add(Practitioner);
				practitionerRecords.put(lastName.charAt(0), practitionerList);
			
			}
			else if(checkUniqueRecord(Practitioner.getRecordID(),Practitioner.getFirstName(),Practitioner.getLastName(),lastName.charAt(0)))
			{
			practitionerList.add(Practitioner);
			
			}
			else
			{
				logger.info("Failed to add Nurse Record with record ID : "+Practitioner.getRecordID()+" duplicate record ID");
				return false;
			}
			logger.info("Nurse Record created :\nRecordID \"" +  "NR"+nrRecord +  "\", FirstName \"" +  firstName + 
					"\", LastName \"" +  lastName +  "\", Designation \"" +  designation +  "\", status \"" + status + "\", StatusDate \"" + 
					statusDate+ "\"" );
			return true;
		}	
	}
	
	/**
	 * checks if record already present with given recordID
	 * @param recordID
	 * @param fName
	 * @param lName
	 * @param chactr
	 * @return true or false
	 */
	public boolean checkUniqueRecord( String recordID,String fName,String lName,Character chactr)
	{
		boolean isUnique=true;
		
		if(!practitionerRecords.isEmpty()&&practitionerRecords.get(chactr)!=null)
		for(Practitioner practitioner:practitionerRecords.get(chactr))
		{
			if(practitioner.getRecordID().equals(recordID) && practitioner.getFirstName().equals(fName)&& practitioner.getLastName().equals(lName))
			isUnique=false;
			break;
		}
		return isUnique;
	}

	/* This method runs on UDP implementation here server acts as a client
	 * (non-Javadoc)
	 * @see dsms.rmi.intermediate.ManagerInterface#getRecordCounts(java.lang.String)
	 */
	@Override
	public String getRecordCounts(String recordType) throws RemoteException 
	{
		DatagramSocket datagramSocket=null;
		staticRecordType=recordType;
		
		try 
		{
			// Create a Datagram Socket and bind it to a local port
			datagramSocket = new DatagramSocket();
			// Place data in byte array
			String data;
			byte [] message;
			InetAddress host = InetAddress.getByName("localhost");
			DatagramPacket request1;
			DatagramPacket request2;
			DatagramPacket reply1;
			DatagramPacket reply2;
			// Create a Datagram Packet to send the request to the server
			if(UDPPort == 6001)
			{
				data = "MTL";
				message = data.getBytes();
				request1 = new DatagramPacket(message, data.length(), host, 6002);
				request2 = new DatagramPacket(message, data.length(), host, 6003);
			}
			else if(UDPPort == 6002)
			{
				data = "LVL";
				message = data.getBytes();
				request1 = new DatagramPacket(message, data.length(), host, 6001);
				request2 = new DatagramPacket(message, data.length(), host, 6003);
			}
			else if(UDPPort == 6003)
			{
				data = "DDO";
				message = data.getBytes();
				request1 = new DatagramPacket(message, data.length(), host, 6001);
				request2 = new DatagramPacket(message, data.length(), host, 6002);
			}
			else
			{
				logger.info("Error getting record counts : Invalid Port number");
				return "Error getting record counts : Invalid Port number";
			}
			datagramSocket.send(request1);
			message = new byte [1000];
			reply1 = new DatagramPacket(message, message.length);
			datagramSocket.receive(reply1);
			datagramSocket.send(request2);
			message = new byte [1000];
			reply2 = new DatagramPacket(message, message.length);
			datagramSocket.receive(reply2);
			String formattedReply1=new String(reply1.getData(), reply1.getOffset(), reply1.getLength());
			String formattedReply2=new String(reply2.getData(), reply2.getOffset(), reply2.getLength());
			
			logger.info(clinicName + ":" + getRecordsFromServer(staticRecordType)+","+ formattedReply1 + "," + formattedReply2);
			return clinicName + ":" + getRecordsFromServer(staticRecordType)+","+ formattedReply1 + "," + formattedReply2;
					
		} 
		catch (SocketException e) 
		{
			System.out.println("SocketException : " + e.getMessage());
		} 
		catch (IOException e) 
		{
			System.out.println("IOException : " + e.getMessage());
		}
		finally
		{
			datagramSocket.close();
		}
		logger.info("Error getting record counts : Socket Excpetion");
		return "Error getting record counts : Socket Excpetion";
	}
	
	/**
	 * gets record count from current server
	 * @param recordType "DR" for Doctor Record "NR" for Nurse Record
	 * @return count of records
	 */
	private String getRecordsFromServer(String recordType)
	{
/*		int recordCount=0;
		StringBuilder recordString = new StringBuilder();
		recordString.append(clinicName+" ");
		Iterator<?> it = practitionerRecords.entrySet().iterator();
		while(it.hasNext())
		{
			@SuppressWarnings("rawtypes")
			Map.Entry pair = (Map.Entry)it.next();
			@SuppressWarnings("unchecked")
			ArrayList<Practitioner> practitionerList = (ArrayList<Practitioner>) pair.getValue();
			if(!practitionerList.isEmpty())
			{					
				for(Practitioner practitioner : practitionerList)
				{
					if(practitioner.getRecordID().startsWith(recordType))
					{
						recordCount++;
					}
				}
			}
		}
		recordString.append(recordCount);
		return recordString.toString();*/
		
		int recordCount=0;
		StringBuilder recordString = new StringBuilder();
		Iterator<?> it = practitionerRecords.entrySet().iterator();
		while(it.hasNext())
		{
			@SuppressWarnings("rawtypes")
			Map.Entry pair = (Map.Entry)it.next();
			@SuppressWarnings("unchecked")
			ArrayList<Practitioner> practitionerList = (ArrayList<Practitioner>) pair.getValue();
			if(!practitionerList.isEmpty())
			{					
				for(Practitioner practitioner : practitionerList)
				{
					if(practitioner.getRecordID().startsWith(recordType))
					{
						recordCount++;
					}
				}
			}
		}
		recordString.append(recordCount);
		return recordString.toString();
	}

	/* Edit record with recordID checks if fieldName is invalid or not.
	 * The fields that should be allowed to change are address, phone and location (for
	 * DoctorRecord), and designation, status and status date (for NurseRecord).
	 * (non-Javadoc)
	 * @see dsms.rmi.intermediate.ManagerInterface#editRecord(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean editRecord(String recordID, String fieldName, String newValue) throws RemoteException 
	{
	
		boolean isSucess=false;
		if(recordID.startsWith("DR")&&fieldName.equalsIgnoreCase("location") && !(newValue.equalsIgnoreCase("mtl")||newValue.equalsIgnoreCase("lvl")||newValue.equalsIgnoreCase("ddo")))
		{
			logger.info(" could not update doctor record with record ID: "+recordID+" Because of Invalid data for field name: "+fieldName);			
		}
		else if(recordID.startsWith("NR")&& fieldName.equalsIgnoreCase("designation") && !(newValue.equalsIgnoreCase("junior")|| newValue.equalsIgnoreCase("senior")))
		{
			logger.info(" could not update nurse record with record ID: "+recordID+" Because of Invalid data for field name: "+fieldName);
		}
		else if(recordID.startsWith("NR")&& fieldName.equalsIgnoreCase("status") && !(newValue.equalsIgnoreCase("terminated")|| newValue.equalsIgnoreCase("active")))
		{
			logger.info(" could not update nurse record with record ID: "+recordID+" Because of Invalid data for field name: "+fieldName);
		}
		else if(fieldName.equalsIgnoreCase("address")||fieldName.equalsIgnoreCase("phone")||fieldName.equalsIgnoreCase("location")||
				fieldName.equalsIgnoreCase("designation")||fieldName.equalsIgnoreCase("status")||fieldName.equalsIgnoreCase("statusDate"))
		{
			Practitioner practitionerUpdate=null;
			synchronized(practitionerRecords) 
			{
				boolean recordFound=false;
			    for (Map.Entry<Character, ArrayList<Practitioner>> entry : practitionerRecords.entrySet())
		        {
			    	ArrayList<Practitioner> practitionerList = entry.getValue();
			    	for(Practitioner practitioner:practitionerList)
			    	{
			    		if(recordID.startsWith("DR")&& practitioner instanceof DoctorRecord)
			    		{
			    			practitionerUpdate=practitioner;
			    			if(recordID.equals(practitioner.getRecordID()))
			    			{
			    				if(fieldName.equalsIgnoreCase("address")){((DoctorRecord)practitioner).setAddress(newValue); recordFound=true; break;}
			    				if(fieldName.equalsIgnoreCase("phone")){((DoctorRecord)practitioner).setPhone(newValue);recordFound=true; break;}
			    				if(fieldName.equalsIgnoreCase("location")){((DoctorRecord)practitioner).setLocation(newValue);recordFound=true; break;}
			    			}
			    		}
			    		else if(recordID.startsWith("NR")&& practitioner instanceof NurseRecord)
			    		{
			    			practitionerUpdate=practitioner;
			    			if(recordID.equals(practitioner.getRecordID()))
			    			{
			    				if(fieldName.equalsIgnoreCase("designation")){((NurseRecord)practitioner).setDesignation(newValue);recordFound=true; break;}
			    				if(fieldName.equalsIgnoreCase("status")){((NurseRecord)practitioner).setStatus(newValue);recordFound=true; break;}
			    				if(fieldName.equalsIgnoreCase("statusDate")){((NurseRecord)practitioner).setStatusDate(getFormattedDate(newValue));recordFound=true; break;}
			    			}
			    		}
			    	}
			    	if(recordFound) break;
		        }
			}
			if(practitionerUpdate instanceof DoctorRecord)
				logger.info("!!!Doctor Record updated :\nRecordID \"" +  practitionerUpdate.getRecordID() +  "\", FirstName \"" +  practitionerUpdate.getFirstName() + 
						"\", LastName \"" +  practitionerUpdate.getLastName() +  "\", Address \"" +  ((DoctorRecord)practitionerUpdate).getAddress() +  "\", Phone \"" + ((DoctorRecord)practitionerUpdate).getPhone() + "\", Specialization \"" + 
						((DoctorRecord)practitionerUpdate).getSpecialization() + "\", Location \""+((DoctorRecord)practitionerUpdate).getLocation()+"\"");
			if(practitionerUpdate instanceof NurseRecord)
				logger.info("!!!Nurse Record updated :\nRecordID \"" +  practitionerUpdate.getRecordID() +  "\", FirstName \"" +   practitionerUpdate.getFirstName()  + 
						"\", LastName \"" +  practitionerUpdate.getLastName() +  "\", Designation \"" +  ((NurseRecord)practitionerUpdate).getDesignation() +  "\", status \"" + ((NurseRecord)practitionerUpdate).getStatus() + "\", StatusDate \"" + 
						((NurseRecord)practitionerUpdate).getStatusDate()+ "\"" );	
			isSucess=true;
		}
		else
		{
			logger.info(" could not update record with record ID: "+recordID+"Invalid field name: "+fieldName);
		}
		return isSucess;
	}
	
	
	/**
	 * Initializes database for each server with dummy records
	 * @param server
	 * @throws RemoteException
	 */
	public static void loadData(ClinicServer server) throws RemoteException
	{
		//load database for Montreal server
		if(server.clinicName.equals("MTL"))
		{
		server.createDRecord("adoctor", "adoctor", "2150,st-hubert", "5145645655", "orthopaedic", "mtl");
		server.createDRecord("bdoctor", "bdoctor", "5750,st-laurent", "5145645655", "surgeon", "mtl");
		server.createDRecord("ydoctor", "ydoctor", "3150,st-marc", "5145645611", "orthopaedic", "mtl");
		}
		//load database for Laval server
		else if (server.clinicName.equals("LVL"))
		{
			
			server.createDRecord("adoctor", "adoctor", "2150,st-hubert", "5145645655", "orthopaedic", "lvl");
			//server.createDRecord("bdoctor", "bdoctor", "5750,st-laurent", "5145645655", "surgeon", "lvl");
			server.createDRecord("ydoctor", "ydoctor", "3150,st-marc", "5145645611", "orthopaedic", "lvl");
		}
		//load database for Dollard server
		else
		{
			server.createDRecord("adoctor", "adoctor", "2150,st-hubert", "5145645655", "orthopaedic", "ddo");
			//server.createDRecord("bdoctor", "bdoctor", "5750,st-laurent", "5145645655", "surgeon", "ddo");
			//server.createDRecord("ydoctor", "ydoctor", "3150,st-marc", "5145645611", "orthopaedic", "ddo");
		}
		server.createNRecord("anurse", "anurse", "junior", "active",getFormattedDate("20-05-2016"));
		server.createNRecord("ynurse", "ynurse", "senior", "terminated",getFormattedDate("24-05-2015"));
		//server.createNRecord("bnurse", "bnurse", "junior", "active",getFormattedDate("21-05-2016"));			
	}
	
	/**
	 * @param dateStr
	 * @return Date in "dd-MM-yyyy" format
	 */
	public static Date getFormattedDate(String dateStr)
	{
		Date formattedDate=null;
		try 
		{
			DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			formattedDate =sdf.parse(dateStr);
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
		}
		return formattedDate;
	}
	
	/**
	 * execution of thread starts here.thread for all 3 servers being created 
	 * @param args
	 * @throws RemoteException
	 * @throws AlreadyBoundException
	 */
	public static void main(String[] args) throws RemoteException, AlreadyBoundException 
	{
		int defaultRegistryPort = 1099;
		Registry rmiRegistry = LocateRegistry.createRegistry(defaultRegistryPort);

		ClinicServer MTLServer = new ClinicServer("MTL",6001);
		ClinicServer LVLServer = new ClinicServer("LVL",6002);
		ClinicServer DDOServer = new ClinicServer("DDO",6003);
		
		Remote objremote1 = UnicastRemoteObject.exportObject(MTLServer,defaultRegistryPort);
		rmiRegistry.bind("MTL", objremote1);
		Remote objremote2 = UnicastRemoteObject.exportObject(LVLServer,defaultRegistryPort);
		rmiRegistry.bind("LVL", objremote2);
		Remote objremote3 = UnicastRemoteObject.exportObject(DDOServer,defaultRegistryPort);
		rmiRegistry.bind("DDO", objremote3);

		MTLServer.start();
		System.out.println("Montreal server up and running!");
		LVLServer.start();
		System.out.println("Laval server up and running!");
		DDOServer.start();
		System.out.println("Dollard server up and running!");

		loadData(MTLServer);
		loadData(LVLServer);
		loadData(DDOServer);

		clinicServers = new ArrayList<ClinicServer>();
		clinicServers.add(MTLServer);
		clinicServers.add(LVLServer);
		clinicServers.add(DDOServer);

	}
}
