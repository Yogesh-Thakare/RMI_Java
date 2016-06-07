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
	private static ManagerClient client[] ;

	ManagerInterface serverAccess;

	/**
	 * Setup Before class
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		List<ManagerClient> testClients=ManagerClient.getClients();

		for (int i=1; i<101;i++)
		{
		client[i] = testClients.get(0);
		client[i].InitializeServer("MTL");
		}
		
		for (int i=101; i<201;i++)
		{
		client[i] = testClients.get(0);
		client[i].InitializeServer("LVL");
		}
		
		for (int i=201; i<301;i++)
		{
		client[i] = testClients.get(0);
		client[i].InitializeServer("DDO");
		}

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
			for (int i=1; i<101;i++)
			{
			
				serverAccess = ManagerClient.LocateServer("MTL");
				assertTrue(serverAccess.editRecord("DR10002", "location", "lvl"));
				//assertTrue(serverAccess.editRecord("DR10002", "location", "ddo"));
			}
			
			for (int i=101; i<201;i++)
			{
			
				serverAccess = ManagerClient.LocateServer("LVL");
				assertTrue(serverAccess.editRecord("DR10004", "location", "mtl"));
				//assertTrue(serverAccess.editRecord("DR10004", "location", "ddo"));
			}
			
			for (int i=201; i<301;i++)
			{
			
				serverAccess = ManagerClient.LocateServer("DDO");
				assertTrue(serverAccess.editRecord("DR10007", "location", "mtl"));
				//assertTrue(serverAccess.editRecord("DR10007", "location", "lvl"));
			}
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
