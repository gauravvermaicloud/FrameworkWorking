package com.boilerplate.framework;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * This class will provide simple encryption wrappers.
 * @author gaurav
 *
 */
public class Encryption {

	private static final String ALGORITHM_USED = "MD5";
	/**
	 * This class is expected to have all members static
	 * hence making the contrsuctor private
	 */
	private Encryption(){
		
	}
	
	/**
	 * This method returns a hashcode for the String
	 * @param string The string whose hash code is to be returned
	 * @return A hash code
	 */
	public static long getHashCode(String string){
		long generatedPassword = 0;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM_USED);
            //Add password bytes to digest
            messageDigest.update(string.getBytes());
            //Get the hash's bytes 
            byte[] bytes = messageDigest.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to long value
            for (int i = 0; i < bytes.length; i++)
            {
            	generatedPassword += ((long) bytes[i] & 0xffL) << (8 * i);
            }
        } 
        catch (NoSuchAlgorithmException e) 
        {
           //This will not e thrown as the algorthm exists
        }
        return generatedPassword;
	}
	
	/**
	 * This method returns a hashcode for the object
	 * @param obj The object whose hash code is to be returned
	 * @return A hash code
	 */
	public static long getHashCode(Object obj){
		return obj.hashCode();
	}
	
	
}