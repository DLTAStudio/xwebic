package com.dltastudio.services;


import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Provider;
import java.security.Security;


/**
 * Basic crypto provider helper methods.
 * 
 * @version $Id$
 */
public class BCProvider {
	
			
    /** Parameters used when generating or verifying ECDSA keys/certs using the "implicitlyCA" key encoding.
     * The curve parameters is then defined outside of the key and configured in the BC provider.
     */

    /** System provider used to circumvent a bug in Glassfish. Should only be used by 
     * X509CAInfo, OCSPCAService, XKMSCAService, CMSCAService. 
     * Defaults to SUN but can be changed to IBM by the installBCProvider method.
     */
    public static String SYSTEM_SECURITY_PROVIDER = "SUN";
    
    public static synchronized void installBCProviderIfNotAvailable() 
    {
    	if (Security.getProvider("BC") == null) 
    	{
    		installBCProvider();
    	}
    }

   
    public static synchronized void removeBCProvider() 
    {
        Security.removeProvider("BC");  
     }
    
    @SuppressWarnings("unchecked")
	public static synchronized void installBCProvider() {
     	
        // A flag that ensures that we install the parameters for implcitlyCA only when we have installed a new provider
        if (Security.addProvider(new BouncyCastleProvider()) < 0)
        {
                 removeBCProvider();
                if (Security.addProvider(new BouncyCastleProvider()) < 0) 
                {
                  // log.error("Cannot even install BC provider again!");
                }
        }

         X509Name.DefaultSymbols.put(X509Name.SN, "SN");
        
        // We hard specify the system security provider in a few cases (see SYSTEM_SECURITY_PROVIDER). 
        // If the SUN provider does not exist, we will always use BC.
        final Provider p = Security.getProvider(SYSTEM_SECURITY_PROVIDER);
        if (p == null) 
        {
        //	log.debug("SUN security provider does not exist, using BC as system default provider.");
        	SYSTEM_SECURITY_PROVIDER = "BC";
        }
        
    }

}
