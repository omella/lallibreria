package com.vaannila.ws;

import java.io.File;

import com.safelayer.trustedx.client.smartwrapper.Constants;
import com.safelayer.trustedx.client.smartwrapper.SmartHeader;
import com.safelayer.trustedx.client.smartwrapper.SmartSignRequest;
import com.safelayer.trustedx.client.smartwrapper.SmartSignResponse;


/*
 * XML Signature Generation
 */

public class GestorFirma {

	
	private static final String host = "https://labs.safelayer.com/demo/services/DigitalSignature";
   
	private static final String user = "dave";
	private static final String password = "trustedx";

	private static final String distinguishedName = "CN=Dave, OU=Demo, O=TrustedX, C=ES";

	public static String signar(String doc) {
        File dir1 = new File(".");
        File dir2 = new File("..");
		try {
			System.out.println("Current dir : " + dir1.getCanonicalPath());
	        System.out.println("Parent  dir : " + dir2.getCanonicalPath());
			// Definition of Signature Request endpoint
			SmartSignRequest ssr = new SmartSignRequest(host);

			// User & Password Header authenticate
			SmartHeader sh = new SmartHeader();
			sh.setUsername(user);
			sh.setPassword(password);			
			ssr.setHeader(sh);

			// XML Signature (XAdES)
			ssr.setProfile(Constants.Profile.XADES);
			// Read of XML data
			//ssr.setInputXmlBase64(Util.readBinaryFileB64(path_in + filename));
			ssr.setInputXmlBase64(doc);
			// Key selection using Distinguished Name
			ssr.setKeySubjectName(distinguishedName);
			
			ssr.setXmlReturnBase64(false);

			// Sending request
			SmartSignResponse ssrs = ssr.send();

			if (UtilTrustedX.checkSW(ssrs.getResultMajor(), ssrs.getResultMinor(), ssrs.getResultMessage())) {

				return ssrs.getSignatureXml();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ERROR";
	}
	

}