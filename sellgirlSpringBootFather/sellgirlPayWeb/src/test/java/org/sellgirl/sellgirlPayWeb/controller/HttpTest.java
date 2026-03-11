//package org.sellgirl.sellgirlPayWeb.controller;
//
//
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//import com.sellgirl.sgJavaHelper.PFRequestResult;
//import com.sellgirl.sgJavaHelper.config.PFDataHelper;
//
///**
// * Unit test for simple App.
// */
//public class HttpTest 
//    extends TestCase
//{
//    /**
//     * Create the test case
//     *
//     * @param testName name of the test case
//     */
//    public HttpTest( String testName )
//    {
//        super( testName );
//    }
//
//    /**
//     * @return the suite of tests being tested
//     */
//    public static Test suite()
//    {
//        return new TestSuite( HttpTest.class );
//    }
//
//    /**
//     * Rigourous Test :-)
//     */
//    public void testApp()
//    {
//        assertTrue( true );
//    }
//    public void testHttpGet()
//    {
////		PFRequestResult r=PFDataHelper.HttpGet("https://pay.sellgirl.com:44303/gettestbycode?code=ben", null);
//		PFRequestResult r=PFDataHelper.HttpGet("https://pay.sellgirl.com:44303/gettest", null);
//		
//		assertTrue(r.success);
//
//    }
//
//	public void testHttpPost() {
//		PFRequestResult r=PFDataHelper.HttpPost("https://pay.sellgirl.com:44303/posttestbycode?code=ben","{\"code\":\"benjamin\"}");
//		assertTrue(r.success);
//	}
//}
