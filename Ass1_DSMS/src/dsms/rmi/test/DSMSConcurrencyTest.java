package dsms.rmi.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import dsms.rmi.client.ManagerClient;
import dsms.rmi.intermediate.ManagerInterface;

/**
 * Tests for multiple thread concurrent access
 */
public class DSMSConcurrencyTest 
{
	private static ManagerClient client1 ;
	private static ManagerClient client2 ;
	private static ManagerClient client3 ;
	private static ManagerClient client4 ;
	private static ManagerClient client5 ;
	private static ManagerClient client6 ;

	ManagerInterface serverAccess;

	/**
	 * Setup Before class
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		List<ManagerClient> testClients=ManagerClient.getClients();

		client1 = testClients.get(0);
		client2 = testClients.get(0);
		client3 = testClients.get(0);
		client4 = testClients.get(0);
		client5 = testClients.get(0);
		client6 = testClients.get(0);

		client1.InitializeServer("MTL");
		client2.InitializeServer("MTL");
		client3.InitializeServer("MTL");
		client4.InitializeServer("MTL");
		client5.InitializeServer("MTL");
		client6.InitializeServer("MTL");
	}

	/**
	 * Test server access for concurrency
	 * @throws Exception
	 */
	@Test
	public void testServerAccess() throws Exception 
	{
		try 
		{
			serverAccess = ManagerClient.LocateServer("MTL");
			assertTrue(serverAccess.editRecord("DR10002", "location", "lvl"));

			serverAccess = ManagerClient.LocateServer("MTL");
			assertTrue(serverAccess.editRecord("DR10002", "location", "ddo"));

			serverAccess = ManagerClient.LocateServer("MTL");
			assertTrue(serverAccess.editRecord("DR10002", "location", "mtl"));

			serverAccess = ManagerClient.LocateServer("MTL");
			assertTrue(serverAccess.editRecord("DR10002", "location", "ddo"));

			serverAccess = ManagerClient.LocateServer("MTL");
			assertTrue(serverAccess.editRecord("DR10002", "location", "mtl"));

			serverAccess = ManagerClient.LocateServer("MTL");
			assertTrue(serverAccess.editRecord("DR10002", "location", "lvl"));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
