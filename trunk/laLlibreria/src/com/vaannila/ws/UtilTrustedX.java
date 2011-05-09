package com.vaannila.ws;

import com.safelayer.trustedx.client.smartwrapper.SmartChain;
import com.safelayer.trustedx.client.smartwrapper.SmartCrl;
import com.safelayer.trustedx.client.smartwrapper.SmartDecryptResponse;
import com.safelayer.trustedx.client.smartwrapper.SmartEncryptResponse;
import com.safelayer.trustedx.client.smartwrapper.SmartOcsp;
import com.safelayer.trustedx.client.smartwrapper.SmartSignResponse;
import com.safelayer.trustedx.client.smartwrapper.SmartSignatureResult;
import com.safelayer.trustedx.client.smartwrapper.SmartStamp;
import com.safelayer.trustedx.client.smartwrapper.SmartVerifyResponse;

public class UtilTrustedX {
	private static final String RMAJOR_SUCCESS = "urn:oasis:names:tc:dss:1.0:resultmajor:Success";
	private static final String RMINOR_SUCCESS = "urn:oasis:names:tc:dss:1.0:resultminor:ValidSignature_OnAllDocuments";

	// Check if the request was success and print of the SOAP results
	// (SmartWrapper)
	public static boolean checkSW(String major, String minor, String message) {
		boolean rmajor = major.equals(RMAJOR_SUCCESS);
		if (rmajor && ((minor == null) || (minor.equals(RMINOR_SUCCESS)))) {
			//System.out.println("Success!");
			return true;
		} else {
			System.out.println("Major: " + major);
			System.out.println("Minor: " + minor);
			if(message != null)
				System.out.println("Message: " + message);
			return false;
		}
	}

	// Prints the Information from a SmartResponse (SmartWrapper)
	public static void printResponse(SmartVerifyResponse svr) throws Exception {
		System.out.println("** RESPONSE **");
		System.out.println("---------------------");
		for (int sig = 0; sig < svr.getNumberSignatures(); sig++) {
			System.out.println("Signature num " + sig);
			System.out.println("---------------------");
			SmartSignatureResult signat = svr.getSignature(sig);
			printCert(signat);

			// Print of Signature Credentials Information
			for (int crlv = 0; crlv < signat.getNumberCrls(); crlv++) {
				SmartCrl crl = signat.getCrl(crlv);
				printCRL(crl);
			}
			for (int ocspv = 0; ocspv < signat.getNumberOcsp(); ocspv++) {
				SmartOcsp ocsp = signat.getOcsp(ocspv);
				printOCSP(ocsp);
			}
			for (int tstamp = 0; tstamp < signat.getNumberTimeStamps(); tstamp++) {
				SmartStamp stamp = signat.getStamp(tstamp);
				printTimestamp(stamp);
			}
			for (int atstamp = 0; atstamp < signat.getArchiveNumberTimeStamps(); atstamp++) {
				SmartStamp stamp = signat.getArchiveStamp(atstamp);
				printArchiveTimestamp(stamp);
			}

			System.out.println("---------------------");
		}
		System.out.println("---------------------");
	}
	
	// Prints the Information from a SmartResponse (SmartWrapper)
	public static void printResponse(SmartSignResponse ssr) throws Exception {
		System.out.println("** RESPONSE **");
		System.out.println("---------------------");
		System.out.println("Major: " + ssr.getResultMajor());
		System.out.println("Minor: " + ssr.getResultMinor());
		if(ssr.getResultMessage() != null)
			System.out.println("Message: " + ssr.getResultMessage());
		System.out.println("Profile: " + ssr.getProfile());
		System.out.println("TimeStamp: " + ssr.getTimeStamp());
		System.out.println("** SIGNATURE **");
		System.out.println("---------------------");
		if(ssr.getDocumentWithSignaturePdf() != null) System.out.println("Signature: " + ssr.getDocumentWithSignaturePdf());
		if(ssr.getDocumentWithSignatureXml() != null) System.out.println("Signature: " + ssr.getDocumentWithSignatureXml());
		if(ssr.getDocumentWithSignatureXmlBase64() != null) System.out.println("Signature: " + ssr.getDocumentWithSignatureXmlBase64());
		if(ssr.getSignatureBase64() != null) {
			System.out.println("Signature: " + ssr.getSignatureBase64());
			System.out.println("Type: " + ssr.getSignatureBase64Type());
		}
		if(ssr.getSignatureMime() != null){
			System.out.println("Signature: " + ssr.getSignatureMime());
			System.out.println("Type: " + ssr.getSignatureMimeType());
		}
		if(ssr.getSignatureXml() != null) System.out.println("Signature: " + ssr.getSignatureXml());
		if(ssr.getSignatureXmlBase64() != null) System.out.println("Signature: " + ssr.getSignatureXmlBase64());
		System.out.println("---------------------");
	}
	
	//Prints the Information of a SmartResponse (DE)
	public static void printResponse(SmartDecryptResponse sdr) throws Exception {
		System.out.println("** RESPONSE **");
		System.out.println("---------------------");
		System.out.println("Major: " + sdr.getResultMajor());
		System.out.println("Minor: " + sdr.getResultMinor());
		if(sdr.getResultMessage() != null)
			System.out.println("Message: " + sdr.getResultMessage());
		
		
		if(sdr.getDocumentMime() != null)
			System.out.println("Document: " + sdr.getDocumentMime());
		if(sdr.getDocumentXmlBase64() != null)
			System.out.println("Document: " + sdr.getDocumentXmlBase64());
		if(sdr.getDocumentXmlData() != null)
			System.out.println("Document: " + sdr.getDocumentXmlData());
	}
	
	// Prints the Information of a SmartResponse (DE)
	public static void printResponse(SmartEncryptResponse ser) throws Exception {
		System.out.println("** RESPONSE **");
		System.out.println("---------------------");
		System.out.println("Major: " + ser.getResultMajor());
		System.out.println("Minor: " + ser.getResultMinor());
		if(ser.getResultMessage() != null)
			System.out.println("Message: " + ser.getResultMessage());

		if(ser.getEnvelopeBase64()!= null){
			System.out.println("Encrypted data: " + ser.getEnvelopeBase64());
			System.out.println("Type: " + ser.getEnvelopeBase64Type());
		}
		if(ser.getEnvelopeMime() != null){
			System.out.println("Encrypted data: " + ser.getEnvelopeMime());
			System.out.println("Type: " + ser.getEnvelopeMimeType());
		}
		if(ser.getEnvelopeXmlBase64() != null)
			System.out.println("Encrypted data: " + ser.getEnvelopeXmlBase64());
		if(ser.getEnvelopeXmlData()!= null)
			System.out.println("Encrypted data: " + ser.getEnvelopeXmlData());
		
	}

	// Print of Certificate Information
	private static void printCert(SmartSignatureResult ssr) throws Exception {
		System.out.println("** Certificate **");
		System.out.println("Major: " + ssr.getResultMajor());
		System.out.println("Minor: " + ssr.getResultMinor());
		if(ssr.getResultMessage() != null)
			System.out.println("Message: " + ssr.getResultMessage());
		System.out.println("DN: " + ssr.getSignerIdentity());
		System.out.println("Issuer Trust Level: "
				+ ssr.getSignerIssuerTrustLevel());
		System.out.println("Issuer Trust Label: "
				+ ssr.getSignerIssuerTrustLabel());
		if(ssr.getSignerCertificateXml() != null)
			System.out.println("XML Cert: " + ssr.getSignerCertificateXml());
		if(ssr.getSigningAdditionalInfo() != null)
			System.out.println("Additional Info: " + ssr.getSigningAdditionalInfo());
	}

	// Print of CRL Information
	private static void printCRL(SmartCrl sc) throws Exception {
		System.out.println("** CRL **");
		System.out.println("Trust Level: " + sc.getTrustLevel());
		System.out.println("Trust Label: " + sc.getTrustLabel());
		System.out.println("XML CRL: " + sc.getCrlXml());
	}

	// Print of OCSP Information
	private static void printOCSP(SmartOcsp so) throws Exception {
		System.out.println("** OCSP **");
		System.out.println("Trust Level: " + so.getTrustLevel());
		System.out.println("Trust Label: " + so.getTrustLabel());
		System.out.println("XML OCSP: " + so.getOcspXml());
	}

	// Print of Archived CRL Information
	private static void printArchivedCRL(SmartCrl sac) throws Exception {
		System.out.println("** Archived CRL **");
		System.out.println("Trust Level: " + sac.getTrustLevel());
		System.out.println("Trust Label: " + sac.getTrustLabel());
		System.out.println("XML Archived CRL: " + sac.getCrlXml());
	}

	// Print of Archived OCSP Information
	private static void printArchivedOCSP(SmartOcsp sao) throws Exception {
		System.out.println("** Archived OCSP **");
		System.out.println("Trust Level: " + sao.getTrustLevel());
		System.out.println("Trust Label: " + sao.getTrustLabel());
		System.out.println("XML Archived OCSP: " + sao.getOcspXml());
	}

	// Print of Chain Information
	private static void printChain(SmartChain sch) throws Exception {
		System.out.println("** Chain **");
		System.out.println("XML Chain: " + sch.getSignerCertificateXml());

		// Print of Revocation Information
		for (int crl = 0; crl < sch.getNumberCrls(); crl++) {
			printCRL(sch.getCrl(crl));
		}
		for (int ocsp = 0; ocsp < sch.getNumberOcsp(); ocsp++) {
			printOCSP(sch.getOcsp(ocsp));
		}
	}

	// Print of Timestamp Information
	private static void printTimestamp(SmartStamp sst) throws Exception {
		System.out.println("** TimeStamp **");
		System.out.println("Major: " + sst.getResultMajor());
		System.out.println("Minor: " + sst.getResultMinor());
		if(sst.getResultMessage() != null)
			System.out.println("Message: " + sst.getResultMessage());
		System.out.println("Trust Level: " + sst.getTrustLevel());
		if(sst.getTrustLabel() != null)
			System.out.println("Trust Label: " + sst.getTrustLabel());
		if(sst.getInfoXml() != null)
			System.out.println("Info XML: " + sst.getInfoXml());
		if(sst.getTsaCertificateXml() != null)
			System.out.println("TSA Cert XML: " + sst.getTsaCertificateXml());

		// Print of Chain Certificate in XML
		if (sst.getNumberChainCas() > 0) {
			System.out.println("Chain Cert XML: " + sst.getChainCasXml());
			// Print of Chain Information
			for (int chain = 0; chain < sst.getNumberChainCas(); chain++) {
				printChain(sst.getChain(chain));
			}
		}

		// Print of Revocation Values
		for (int crl = 0; crl < sst.getNumberCrls(); crl++) {
			printCRL(sst.getCrl(crl));
		}
		for (int ocsp = 0; ocsp < sst.getNumberOcsp(); ocsp++) {
			printOCSP(sst.getOcsp(ocsp));
		}
		for (int acrl = 0; acrl < sst.getNumberArchivedCrls(); acrl++) {
			printArchivedCRL(sst.getCrl(acrl));
		}
		for (int aocsp = 0; aocsp < sst.getNumberArchivedOcsp(); aocsp++) {
			printArchivedOCSP(sst.getOcsp(aocsp));
		}
	}

	// Print of Archived Timestamp Information
	private static void printArchiveTimestamp(SmartStamp ssta) throws Exception {
		System.out.println("** Archive TimeStamp **");
		System.out.println("Major: " + ssta.getResultMajor());
		System.out.println("Minor: " + ssta.getResultMinor());
		System.out.println("Message: " + ssta.getResultMessage());
		System.out.println("Trust Level: " + ssta.getTrustLevel());
		System.out.println("Trust Label: " + ssta.getTrustLabel());
		System.out.println("Info XML: " + ssta.getInfoXml());
		System.out.println("TSA Cert XML: " + ssta.getTsaCertificateXml());

		// Print of Chain Certificate in XML
		if (ssta.getNumberChainCas() > 0) {
			System.out.println("Chain Cert XML: " + ssta.getChainCasXml());
			// Print of Chain Information
			for (int chain = 0; chain < ssta.getNumberChainCas(); chain++) {
				printChain(ssta.getChain(chain));
			}
		}

		// Print of Revocation Values
		for (int crl = 0; crl < ssta.getNumberCrls(); crl++) {
			printCRL(ssta.getCrl(crl));
		}
		for (int ocsp = 0; ocsp < ssta.getNumberOcsp(); ocsp++) {
			printOCSP(ssta.getOcsp(ocsp));
		}
		for (int acrl = 0; acrl < ssta.getNumberArchivedCrls(); acrl++) {
			printArchivedCRL(ssta.getCrl(acrl));
		}
		for (int aocsp = 0; aocsp < ssta.getNumberArchivedOcsp(); aocsp++) {
			printArchivedOCSP(ssta.getOcsp(aocsp));
		}
	}
	
	
}