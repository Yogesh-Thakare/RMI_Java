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

public class ManagerClient extends Thread
{
	private static Logger log;
	private Scanner scanner;
	static ManagerInterface MTLServer;
	static ManagerInterface LVLServer;
	static ManagerInterface DDOServer;
	static final String MTL = "Montreal", LVL = "Laval",
			DDO = "Dollard-des-Ormeaux";
	protected static String ManagerID;

	public ManagerClient(String string) 
	{
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
		ManagerClient managerMTL = new ManagerClient("MTLManager");
		ManagerClient managerLVL = new ManagerClient("LVLManager");
		ManagerClient managerDDO = new ManagerClient("DDOManager");
		
		testClients.add(managerMTL);
		testClients.add(managerLVL);
		testClients.add(managerDDO);
		
		return testClients;	
	}

	public void InitializeServer(String server) throws Exception 
	{
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
		return server;
	}

	// Get Server Connection
	public static ManagerInterface LocateServer(String locationName) {
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
		try 
		{
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			formattedDate =sdf.parse(dateStr);
		} 
		catch (ParseException e) 
		{
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
			runTerminal();	
			}
			catch(Exception e)
			{
			e.printStackTrace();
			}		
}
	
	public static void runTerminal ()
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
			//objServer = objClient.ServerValidation(keyboard);
			Boolean valid = false;
			ManagerInterface server = null;
			System.out.println("Enter Manager ID");
			while (!valid) 
			{
				try 
				{
					ManagerID = keyboard.nextLine();
					String locationName = ManagerID.substring(0, 3);
					System.out.println(locationName);
					objServer = LocateServer(locationName);
					if ( objServer!= null) 
					{
						valid = true;
					} 
					else 
					{
						System.out.println("Invalid Manager ID");
						keyboard.nextLine();
					}
				} 
				catch (Exception e) 
				{
					System.out.println("Invalid Manager ID");
					valid = false;
					keyboard.nextLine();
				}
			}
		
			Integer userInput = 0;
			showMenu();
			userInput = Integer.parseInt(InputStringValidation(keyboard));
			String firstName, lastName,address,phone,specialization,location;
			String designation,status,statusdate,recordType,recordID,fieldName,newValue;
	
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
					boolean resultDRecord;
					resultDRecord = objServer.createDRecord (firstName,lastName,address,phone,specialization,location);
					if(resultDRecord)
					System.out.println("Doctor  Record Created Successfully by " + ManagerID);
					ManagerClient.log.info("Doctor  Record Created Successfully by " + ManagerID);
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
					try 
					{
					    //Parsing the String
					    date2 = dateFormat.parse(statusdate);
					} 
					catch (ParseException e) 
					{
					    // TODO Auto-generated catch block
					    e.printStackTrace();
					}
	
					boolean resultNRecord;
					resultNRecord = objServer.createNRecord (firstName,lastName,designation,status,date2);
					if(resultNRecord)
					System.out.println("Nurse Record Created Successfully by" + ManagerID);
					ManagerClient.log.info("Doctor  Record Created Successfully by " + ManagerID);
					showMenu();
					userInput = Integer.parseInt(InputStringValidation(keyboard));
					break;
				case 3:
					System.out.println("Please Enter below information: ");
					System.out.println("Record Type: ");
					recordType = InputStringValidation(keyboard);
					String count=objServer.getRecordCounts (recordType);
					System.out.println("The Count for the " + recordType + " Records is :" + count );
					ManagerClient.log.info("The Count for the " + recordType + " Records is :" + count);
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
					ManagerClient.log.info("Edit Record successfully by " + ManagerID);
					showMenu();
					userInput = Integer.parseInt(InputStringValidation(keyboard));
					break;
				case 5:
					System.out.println("Have a nice day!");
					ManagerClient.log.info("Manager opted for EXIT option");
					keyboard.close();
					System.exit(0);
				default:
					System.out.println("Invalid Input, please try again.");
					ManagerClient.log.info("Invalid Input entered by user");
				}
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
		
	public void createLog()
	{
		try {
			log = Logger.getLogger(this.getName());
			FileHandler fileHandler = new FileHandler(this.getName() + ".log");
			log.addHandler(fileHandler);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fileHandler.setFormatter(formatter);
	        log.info("Log file created for ManagerClient " + this.getName() );
		} catch (SecurityException | IOException e) {
			log.info("Log File Error: " + e.getMessage());
		}
	}
}
