package dsms.rmi.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * This is a test suite class for all tests suits
 */
@RunWith(Suite.class)
@SuiteClasses({ DSMSConcurrencyTest.class, DSMSFunctionalityTest.class})
public class DSMSTestSuite 
{
}