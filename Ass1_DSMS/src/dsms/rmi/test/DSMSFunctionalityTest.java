package dsms.rmi.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import dsms.rmi.client.ManagerClient;
import dsms.rmi.intermediate.ManagerInterface;


public class DSMSFunctionalityTest {
	
	private static ManagerClient managerMTL;
	private static ManagerClient managerLVL;
	private static ManagerClient managerDDO;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		List<ManagerClient> testClients=ManagerClient.getClients();
		
		 managerMTL = testClients.get(0);
		 managerLVL = testClients.get(1);
		 managerDDO = testClients.get(2);
		
		managerMTL.InitializeServer("MTL");
		managerLVL.InitializeServer("LVL");
		managerDDO.InitializeServer("DDO");
	}
	
	@Test
	public void testServerAccess() throws Exception {
		ManagerClient a = new ManagerClient("testClient");

		assertNull(a.LocateServer("MTL222"));
	}

	@Test
	public void testCreateDRecord() throws Exception {
		boolean result1;
		boolean result2;
		boolean result3;
		
		
		ManagerInterface objServer1= managerMTL.ServerAccess("MTL1111");
		result1 = objServer1.createDRecord("akash", "akash", "2150,st-hubert", "5145645655", "orthopaedic", "mtl");
		assertTrue(result1);
		
		
		ManagerInterface objServer2= managerLVL.ServerAccess("LVL1111");
		result2 = objServer2.createDRecord("bhuvan", "bhuvan", "2150,st-hubert", "5145645655", "orthopaedic", "lvl");
		assertTrue(result2);
		
		
		ManagerInterface objServer3= managerDDO.ServerAccess("DDO1111");
		result3 = objServer3.createDRecord("yogesh", "yogesh", "2150,st-hubert", "5145645655", "orthopaedic", "ddo");
		assertTrue(result3);
	}
	
	@Test
	public void testCreateNRecord() throws Exception {
		boolean result1;
		boolean result2;
		boolean result3;
		
	
		ManagerInterface objServer1= managerMTL.ServerAccess("MTL1111");
		result1 = objServer1.createNRecord("arushi", "arushi", "junior", "active",ManagerClient.getFormattedDate("20-05-2015"));
		assertTrue(result1);
		
	
		ManagerInterface objServer2= managerLVL.ServerAccess("LVL1111");
		result2 = objServer2.createNRecord("bela", "bela", "junior", "terminated",ManagerClient.getFormattedDate("10-05-2015"));
		assertTrue(result2);
		
	
		ManagerInterface objServer3= managerDDO.ServerAccess("DDO1111");
		result3 = objServer3.createNRecord("yamini", "yamini", "junior", "active",ManagerClient.getFormattedDate("01-05-2015"));
		assertTrue(result3);
		
	}
	

	@Test
	public void testEditRecord() throws Exception {
		boolean result1;
	
		
		ManagerInterface objServer1= managerMTL.ServerAccess("MTL1111");
		result1=objServer1.editRecord("DR10001","lastName","zdoctor");
		assertFalse(result1);
		
		ManagerInterface objServer2= managerMTL.ServerAccess("MTL1111");
		result1=objServer2.editRecord("DR10001","address","123,rue mathiue");
		assertTrue(result1);
		
		ManagerInterface objServer3= managerLVL.ServerAccess("LVL1111");
		result1=objServer3.editRecord("DR10002","firstName","hdoctor");
		assertFalse(result1);
		
		ManagerInterface objServer4= managerMTL.ServerAccess("LVL1111");
		result1=objServer4.editRecord("DR10002","location","ddo");
		assertTrue(result1);
		
		ManagerInterface objServer5= managerMTL.ServerAccess("DDO1111");
		result1=objServer5.editRecord("DR10003","specialization","abc");
		assertFalse(result1);
		
		ManagerInterface objServer6= managerMTL.ServerAccess("DDO1111");
		result1=objServer6.editRecord("NR10008","status","active");
		assertTrue(result1);
		
		ManagerInterface objServer7= managerMTL.ServerAccess("DDO1111");
		result1=objServer7.editRecord("NR10009","status","xyz");
		assertFalse(result1);
	}
	

	@Test
	public void testGetRecordCount() throws Exception {
		
		
		//String expected="MTL 3, LVL 3, DDO 3";
		
		ManagerInterface objServer1= managerMTL.ServerAccess("MTL1111");
		assertTrue(objServer1.getRecordCounts("DR") instanceof String);
		
		ManagerInterface objServer2= managerLVL.ServerAccess("LVL1111");
		assertTrue(objServer2.getRecordCounts("DR") instanceof String);
		
		ManagerInterface objServer3= managerDDO.ServerAccess("DDO1111");
		assertTrue(objServer3.getRecordCounts("NR") instanceof String);
		
		//String response =objServer1.getRecordCounts("DR");
		//assertEquals(expected, response);
		
	
		
	}
	
	
}
