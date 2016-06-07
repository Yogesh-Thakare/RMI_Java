package dsms.rmi.client;


import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
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

/**
 * This represents client which is itself thread object for implementing multithreading demo
 *
 */
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

	/**
	 * This is a constructor to create a log file
	 * @param string
	 */
	public ManagerClient(String string) 
	{
		
		this.setName(string);
		scanner = new Scanner(System.in);
		createLog();
		System.setSecurityManager(new RMISecurityManager());
		this.start();
		
	}

	/**
	 * Returns different clients
	 * @return clients as a objects of thread
	 */
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

	/**
	 * This method returns the server name on the basis of ManagerID entered by user
	 * @param manager
	 * @return
	 */
	public ManagerInterface ServerAccess(String manager) 
	{
		ManagerInterface server = null;
		String locationName = manager.substring(0, 3);
		System.out.println(locationName);
		server = LocateServer(locationName);
		return server;
	}

	/**
	 * connects to one of three server instances
	 * @param server to which connection required
	 * @throws Exception
	 */
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

	/**
	 * validates input from client
	 * @param keyboard
	 * @return
	 */
	public ManagerInterface ServerValidation(Scanner keyboard) 
	{
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
				server = LocateServer(locationName);
				if (server != null) 
				{
					valid = true;
				} 
				else 
				{
					System.out.println("Invalid Manager ID");
					keyboard.nextLine();
				}
			} catch (Exception e) 
			{
				System.out.println("Invalid Manager ID");
				valid = false;
				keyboard.nextLine();
			}
		}
		return server;
	}


	/**
	 * Get server connection
	 * @param locationName
	 * @return
	 */
	public static ManagerInterface LocateServer(String locationName) 
	{
		if (locationName.equals("MTL")) {
			return MTLServer;
		} else if (locationName.equals("LVL")) {
			return LVLServer;
		} else if (locationName.equals("DDO")) {
			return DDOServer;
		}
		return null;
	}

	/**
	 * Menu which will display on the console
	 */
	public static void showMenu() {
		System.out.println("Distributed Staff Management System \n");
		System.out.println("Please select an option");
		System.out.println("1. Create Doctor Record.");
		System.out.println("2. Create Nurse Record.");
		System.out.println("3. Get Record count");
		System.out.println("4. Edit record");
		System.out.println("5. Show Concurrency Test");
		System.out.println("6. Exit");
	}
	
	
	public static void runMultithreadDemo()
	{
	
		new ManagerClient("MTLThread").start();
	
		new ManagerClient("LVLThread").start();

		new ManagerClient("DDOThread").start();
		
	}
	
	public void run()
	{
		if(getName().equals("MTLThread"))
		{
		for (int i = 0; i < 100; i++) 
		{
			try 
			{
				ManagerInterface serverAccess=LocateServer("MTL");
				serverAccess.editRecord("DR10001", "location", "lvl");
				sleep((int) (Math.random() * 2000));
			} 
			catch (InterruptedException e) 
			{
			}
			catch (RemoteException e) 
			{
			}
		}
		System.out.println("Test Finished for: " + getName());
		}
		else if(getName().equals("LVLThread"))
		{
		for (int i = 0; i < 100; i++) 
		{
			try 
			{
				
				ManagerInterface serverAccess=LocateServer("LVL");
				serverAccess.editRecord("DR10002", "location", "ddo");
				sleep((int) (Math.random() * 2000));
			} 
			catch (InterruptedException e) 
			{
			}
			catch (RemoteException e) 
			{
			}
		}
		System.out.println("Test Finished for: " + getName());
		}
		else if(getName().equals("DDOThread"))
		{
		for (int i = 0; i < 100; i++) 
		{
			try 
			{
				
				ManagerInterface serverAccess=LocateServer("DDO");
				serverAccess.editRecord("DR10003", "location", "mtl");
				sleep((int) (Math.random() * 2000));
			} 
			catch (InterruptedException e) 
			{
			}
			catch (RemoteException e) 
			{
			}
		}
		System.out.println("Test Finished for: " + getName());
		}
	}

	/**
	 * Method to validate input string 
	 * @param keyboard
	 * @return
	 */
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

	/**
	 * This method will return the formatted date from string
	 * @param dateStr
	 * @return
	 */
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

	/**
	 * runs console to accept input from manager
	 * 
	 */
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
					{
						//System.out.println("Doctor  Record Created Successfully by " + ManagerID);
						ManagerClient.log.info("Doctor  Record Created Successfully by " + ManagerID);
					}
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
					{
						//System.out.println("Nurse Record Created Successfully by" + ManagerID);
						ManagerClient.log.info("Doctor  Record Created Successfully by " + ManagerID);
					}
					showMenu();
					userInput = Integer.parseInt(InputStringValidation(keyboard));
					break;
				case 3:
					System.out.println("Please Enter below information: ");
					System.out.println("Record Type: ");
					recordType = InputStringValidation(keyboard);
					String count=objServer.getRecordCounts (recordType);
					//System.out.println("The Count for the " + recordType + " Records is :" + count );
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
					{
						//System.out.println("Edit Record successfully by " + ManagerID);
						ManagerClient.log.info("Edit Record successfully by " + ManagerID);
					}
					showMenu();
					userInput = Integer.parseInt(InputStringValidation(keyboard));
					break;
				case 5:
					System.out.println("Concurrency Test has began....");
					runMultithreadDemo();
					
				case 6:
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

	/**
	 * creates log file for each manager who gets in to system
	 */
	public void createLog()
	{
		try 
		{
			log = Logger.getLogger(this.getName());
			FileHandler fileHandler = new FileHandler(this.getName() + ".log");
			log.addHandler(fileHandler);
			SimpleFormatter formatter = new SimpleFormatter();  
			fileHandler.setFormatter(formatter);
			log.info("Log file created for ManagerClient " + this.getName() );
		} 
		catch (SecurityException | IOException e) 
		{
			log.info("Log File Error: " + e.getMessage());
		}
	}
}
