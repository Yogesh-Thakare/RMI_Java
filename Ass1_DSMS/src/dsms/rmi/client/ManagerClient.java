package dsms.rmi.client;


import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import dsms.rmi.intermediate.ManagerInterface;

public class ManagerClient extends Thread{
	private Logger log;
	private Scanner scanner;
	static ManagerInterface MTLServer;
	static ManagerInterface LVLServer;
	static ManagerInterface DDOServer;
	static final String MTL = "Montreal", LVL = "Laval",
			DDO = "Dollard-des-Ormeaux";
	protected static String ManagerID;

	public ManagerClient(String string) {
		// TODO Auto-generated constructor stub
		this.setName(string);
		scanner = new Scanner(System.in);
		createLog();
		System.setSecurityManager(new RMISecurityManager());
		this.start();
	}
	
	public static List<ManagerClient> getClients()
	{
		System.setProperty("java.security.policy","file:./security.policy");
		List<ManagerClient> testClients=new ArrayList<ManagerClient>();
		ManagerClient manager1 = new ManagerClient("ClientManager1");
		ManagerClient manager2 = new ManagerClient("ClientManager2");
		ManagerClient manager3 = new ManagerClient("ClientManager3");
		
		testClients.add(manager1);
		testClients.add(manager2);
		testClients.add(manager3);
		
		return testClients;
		
	}


	public void InitializeServer(String server) throws Exception {
		System.setSecurityManager(new RMISecurityManager());
		if (server.equals("MTL"))
		{
		MTLServer = (ManagerInterface) Naming
				.lookup("rmi://localhost:1099/MTL");
		}
		if (server.equals("LVL"))
		{
			LVLServer = (ManagerInterface) Naming
					.lookup("rmi://localhost:1099/LVL");
		}
		if (server.equals("DDO"))
		{
			DDOServer = (ManagerInterface) Naming
					.lookup("rmi://localhost:1099/DDO");
		}

	}

	public ManagerInterface ServerValidation(Scanner keyboard) {
		Boolean valid = false;
		ManagerInterface server = null;
		System.out.println("Enter Manager ID");
		while (!valid) {
			try {
				ManagerID = keyboard.nextLine();
				String locationName = ManagerID.substring(0, 3);
				System.out.println(locationName);
				server = LocateServer(locationName);
				if (server != null) {
					valid = true;
				} else {
					System.out.println("Invalid Manager ID");
					keyboard.nextLine();
				}
			} catch (Exception e) {
				System.out.println("Invalid Manager ID");
				valid = false;
				keyboard.nextLine();
			}
		}
		// keyboard.nextLine();
		return server;
	}
	
	public ManagerInterface ServerAccess(String manager) 
	{
		ManagerInterface server = null;
				String locationName = manager.substring(0, 3);
				System.out.println(locationName);
				server = LocateServer(locationName);
		// keyboard.nextLine();
		return server;
	}

	// Get Server Connection
	public ManagerInterface LocateServer(String locationName) {
		if (locationName.equals("MTL")) {
			return MTLServer;
		} else if (locationName.equals("LVL")) {
			return LVLServer;
		} else if (locationName.equals("DDO")) {
			return DDOServer;
		}
		return null;
	}

	public static void showMenu() {
		System.out.println("Distributed Staff Management System \n");
		System.out.println("Please select an option");
		System.out.println("1. Create Doctor Record.");
		System.out.println("2. Create Nurse Record.");
		System.out.println("3. Get Record count");
		System.out.println("4. Edit record");
		System.out.println("5. Exit");
	}
	
	
	
	public static String InputStringValidation(Scanner keyboard) {
		Boolean valid = false;
		String userInput = "";
		while(!valid)
		{
			try{
				userInput=keyboard.nextLine();
				valid=true;
			}
			catch(Exception e)
			{
				System.out.println("Invalid Input, please enter an String");
				valid=false;
				keyboard.nextLine();
			}
		}
		return userInput;
	}
	
	public static Date getFormattedDate(String dateStr)
	{
		Date formattedDate=null;
	try {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		formattedDate =sdf.parse(dateStr);
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return formattedDate;
	}

	public static void main(String[] args)
	{
			try
			{
			System.setProperty("java.security.policy","file:./security.policy");
			ManagerClient manager1 = new ManagerClient("ClientManager1");
			ManagerClient manager2 = new ManagerClient("ClientManager2");
			ManagerClient manager3 = new ManagerClient("ClientManager3");
			
			// TEST CASE 1 for MTL server:
			manager1.InitializeServer("MTL");
			ManagerInterface objServer1= manager1.ServerAccess("MTL1001");
			boolean resultDRecord1,resultNRecord1,editRecord1;
			String count1;
			resultDRecord1 = objServer1.createDRecord("adoctor", "adoctor", "2150,st-hubert", "5145645655", "orthopaedic", "mtl");
			resultNRecord1= objServer1.createNRecord("anurse", "anurse", "junior", "active",getFormattedDate("20-05-2016"));
			count1=objServer1.getRecordCounts("DR");
			editRecord1=objServer1.editRecord("DR10001","adoctor","zdoctor");
			
			// TEST CASE 2 for LVL server:
			manager2.InitializeServer("LVL");
			ManagerInterface objServer2=manager2.ServerAccess("LVL1004");
			boolean resultDRecord2,resultNRecord2,editRecord2;
			String count2;
			resultDRecord2= objServer2.createDRecord("bdoctor", "bdoctor", "5750,st-laurent", "5145645655", "surgeon", "lvl");
			resultNRecord2= objServer2.createNRecord("bnurse", "bnurse", "junior", "active",getFormattedDate("20-07-2016"));
			count2= objServer2.getRecordCounts("NR");
			editRecord2= objServer2.editRecord("DR10001","bdoctor","ydoctor");
			
			// TEST CASE 3 for DDO server:
			manager3.InitializeServer("DDO");
			ManagerInterface objServer3=manager2.ServerAccess("DDO1009");
			boolean resultDRecord3,resultNRecord3,editRecord3;
			String count3;
			resultDRecord3=objServer3.createDRecord("cdoctor", "cdoctor", "3150,st-marc", "5145645611", "orthopaedic", "ddo");
			resultNRecord3=objServer3.createNRecord("cnurse", "cnurse", "junior", "active",getFormattedDate("20-08-2016"));
			count3=objServer3.getRecordCounts("DR");
			editRecord3=objServer3.editRecord("DR10001","cdoctor","edoctor");
			
			manager1.runTerminal();	
			
		}catch(Exception e){
			e.printStackTrace();
		}

		
}
	
	public void runTerminal ()
	{
		try
		{
			ManagerClient objClient = new ManagerClient("ClientManager1");
		//initialize the connections to registry
		objClient.InitializeServer("MTL");
		objClient.InitializeServer("LVL");
		objClient.InitializeServer("DDO");
		ManagerInterface objServer = null;
		Scanner keyboard = new Scanner(System.in);
		//to which server you want to connect
		objServer = objClient.ServerValidation(keyboard);
		Integer userInput = 0;
		showMenu();
		//objClient.setLogger("admin", "logs/admin.txt");
		//objClient.logger.info("admin login");

		userInput = Integer.parseInt(InputStringValidation(keyboard));
		String firstName, lastName,address,phone,specialization,location;
		String designation,status,statusdate,recordType,recordID,fieldName,newValue;
		//boolean success = true;

		while(true)
		{
			switch(userInput)
			{
			case 1: 
				System.out.println("Please Enter below information: ");
				System.out.println("First Name: ");
				firstName = InputStringValidation(keyboard);
				System.out.println("Last Name: ");
				lastName = InputStringValidation(keyboard);
				System.out.println("Address: ");
				address = InputStringValidation(keyboard);
				System.out.println("Phone: ");
				phone = InputStringValidation(keyboard);
				System.out.println("Specialization: ");
				specialization = InputStringValidation(keyboard);
				System.out.println("Location: ");
				location = InputStringValidation(keyboard);
				
				//objClient.logger.info("Non Returner retrieved on :"+ System.currentTimeMillis());
				boolean resultDRecord;
				resultDRecord = objServer.createDRecord (firstName,lastName,address,phone,specialization,location);
				if(resultDRecord)
				System.out.println("Doctor  Record Created Successfully by" + ManagerID);
				//success=false;
				showMenu();
				userInput = Integer.parseInt(InputStringValidation(keyboard));
				break;
			case 2:
				System.out.println("Please Enter below information: ");
				System.out.println("First Name: ");
				firstName = InputStringValidation(keyboard);
				System.out.println("Last Name: ");
				lastName = InputStringValidation(keyboard);
				System.out.println("Designation: ");
				designation = InputStringValidation(keyboard);
				System.out.println("Status: ");
				status = InputStringValidation(keyboard);
				Scanner scanner = new Scanner(System.in);
				System.out.println("Status Date(yyyy-MM-dd): ");
				statusdate = scanner.next();
				
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date2=null;
				try {
				    //Parsing the String
				    date2 = dateFormat.parse(statusdate);
				} catch (ParseException e) {
				    // TODO Auto-generated catch block
				    e.printStackTrace();
				}

				//TODO what to do with institute name
				
				//objClient.logger.info("Non Returner retrieved on :"+ System.currentTimeMillis());
				boolean resultNRecord;
				resultNRecord = objServer.createNRecord (firstName,lastName,designation,status,date2);
				if(resultNRecord)
				System.out.println("Nurse Record Created Successfully by" + ManagerID);
				//success=false;
				showMenu();
				userInput = Integer.parseInt(InputStringValidation(keyboard));
				break;
			case 3:
				System.out.println("Please Enter below information: ");
				System.out.println("Record Type: ");
				recordType = InputStringValidation(keyboard);
				String count=objServer.getRecordCounts (recordType);
				System.out.println("The Count for the " + recordType + " Records is :" + count );
				//success=false;
				showMenu();
				userInput = Integer.parseInt(InputStringValidation(keyboard));
				break;
			case 4:
				System.out.println("Please Enter below information: ");
				System.out.println("Record ID: ");
				recordID = InputStringValidation(keyboard);
				System.out.println("Field Name: ");
				fieldName = InputStringValidation(keyboard);
				System.out.println("New Value: ");
				newValue = InputStringValidation(keyboard);
				
				boolean editRecordResult;
				editRecordResult=objServer.editRecord(recordID,fieldName,newValue);
				if(editRecordResult)
				System.out.println("Edit Record successfully by " + ManagerID);
				//success=false;
				showMenu();
				userInput = Integer.parseInt(InputStringValidation(keyboard));
				break;
			case 5:
				System.out.println("Have a nice day!");
				keyboard.close();
				System.exit(0);
			default:
				System.out.println("Invalid Input, please try again.");
			}
		}
		
	}catch(Exception e){
		e.printStackTrace();
	}
	}
	
	private void createLog()
	{
		try {
			log = Logger.getLogger(this.getName());
			FileHandler fileHandler = new FileHandler(this.getName() + ".log");
			log.addHandler(fileHandler);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fileHandler.setFormatter(formatter);
	        log.info("Log file created for ManagerClient");
		} catch (SecurityException | IOException e) {
			log.info("Log File Error: " + e.getMessage());
		}
	}
}
