package com.ryanchapin.util.hashgenerator;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The <code>Hash-Generator</code> is a class used for generating hashes for
 * various types of input data.
 * <p>
 * It supports any of the hash algorithms that are supported by the
 * {@link java.security.MessageDigest} class.
 * <p>
 * NOTE: that the unit tests in this project DO NOT test the usage of the
 * <code>MD2</code> digest algorithm as it has not been included in the openssl
 * since openssl-0.9.8m (2010-02-25).
 * <p>
 * The class is thread safe <b>depending on how it is instantiated and/or
 * called</b>.  Used in the following manner it is thread safe:
 * <p><blockquote><pre>
 *    HashGenerator hashGenerator = new HashGenerator();
 *    String sha1Hash = hashGenerator.createHash("This is a test", "SHA-1"); 
 * </pre></blockquote>
 * 
 * 
  
 * 
 * @author Ryan Chapin
 * @since  2015-03-01
 *
 */
public class HashGenerator {
   
   /**
    * Hash algorithm to be used to generate hashes for subsequent calls to
    * any of the overloaded <code>createHash</code> methods. 
    */
   private String hashAlgo;
   
   private ByteArrayOutputStream bos;
   
   private DataOutputStream dos;
   
   private MessageDigest md;
   
   // -------------------------------------------------------------------------
   // Accessor/Mutators:
   //
   
   
   // -------------------------------------------------------------------------
   // Constructor:
   //
   
   /**
    * Initializes a new {@code HashGenerator} instance such that subsequent
    * calls can be made passing in only the data to be hashed.
    * <p>
    * In this context the instance is <b>NOT</b> thread safe and thread safety
    * should be managed by the client.  This constructor should be used in
    * situations where client code is making large number of calls to this
    * class and there are issues arising from garbage collection of objects
    * that are instantiated on the stack.
    * <p>
    * In that case, the developer may opt to instantiate a new instance per
    * thread, or simply synchronize calls to the class if that will not
    * cause a performance bottleneck.
    * 
    * @param hashAlgo
    *        Hash algorithm to be used to create hashes.
    */
   public HashGenerator(String hashAlgo) {
      this.hashAlgo = hashAlgo;
   }
   
   
   // -------------------------------------------------------------------------
   // Member Methods:
   //
   
   private void checkHashAlgoField() throws IllegalStateException {
      if (null == this.hashAlgo || this.hashAlgo.isEmpty()) {
         throw new IllegalStateException("No hashing algorithm was set for this HashGenerator instance.");
      }  
   }
   
   private static void checkHashAlgoField(String hashAlgo) throws IllegalArgumentException {
      if (null == hashAlgo || hashAlgo.isEmpty()) {
         throw new IllegalStateException("No hashing algorithm was provided.");
      }
   }
   
   private static void checkStringEncoding(String encoding) {
      
   }
   
   public String createHash(String input, String encoding) {
      checkHashAlgoField();
      
      // Generate a byte array from the input String.
      byte[] inByteArray = null;
      try {
         inByteArray = input.getBytes(encoding);
      } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
      }
      return bytesToHex(computeHashBytes(inByteArray));
   }
   
   public static String createHash(String input, String encoding, String hashAlgorithm) {
      checkHashAlgoField(hashAlgorithm);
      
      // Generate a byte array from the input String.
      byte[] inByteArray = null;
      try {
         inByteArray = input.getBytes(encoding);
      } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
      }
      return bytesToHex(computeHashBytes(inByteArray, hashAlgorithm));
   }
   
   
   public static String createHash(long input, String hashAlgorithm) {
      // Generate a byte array from the long
      // Extract the byte value of the long
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            dos.writeLong(input);
        } catch (IOException e) {
           e.printStackTrace();
        }
        try {
            dos.flush();
        } catch (IOException e) {
           e.printStackTrace();
        }

        byte[] byteArray = bos.toByteArray();
        
        try {
           dos.close();
        } catch (IOException e) {
           
        }
        return bytesToHex(computeHashBytes(byteArray, hashAlgorithm));
   }
   
   
   /**
    * Computes the hashed bytes for the byte array representation of the input
    * data.  To be used when thread safety is handled by the client code.
    * 
    * @param inputBytes
    *        byte array representing the data to be hashed.
    *        
    * @return the array of bytes representing the hashed data.
    */
   public byte[] computeHashBytes(byte[] inputBytes) {
      if (null == md) {
         try {
            md = MessageDigest.getInstance(hashAlgo);
         } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
         }
      }
      md.reset();
      md.update(inputBytes);
      return md.digest();
   }
   
   /**
    * 
    * @param inputBytes
    * @param hashAlgorithm
    * @return
    */
   public static byte[] computeHashBytes(byte[] inputBytes, String hashAlgorithm) {
      // Instantiate a MessageDigest instance configured with the desired
      // algorithm.
      MessageDigest msgDigest = null;
      try {
         msgDigest = MessageDigest.getInstance(hashAlgorithm);
      } catch(NoSuchAlgorithmException e) {
         e.printStackTrace();
      }
      return msgDigest.digest(inputBytes);
   }
   
   /**
    * 
    * @param hashBytes
    * @return
    */
   public static String bytesToHex(byte[] hashBytes) {
      
      // Convert the hashBytes to a String of hex values
      StringBuilder retVal   = new StringBuilder();
      StringBuilder hexValue = new StringBuilder();
      
      for (byte hashByte : hashBytes) {
         // Flush our StringBuilder to be used as a container for the
         // hex value for each byte as it is read.
         hexValue.delete(0, hexValue.length());
         hexValue.append(Integer.toHexString(0xFF & hashByte));
         
         // Add a trailing '0' if our hexValue is only 1 char long
         if (hexValue.length() == 1) {
            hexValue.insert(0, '0');
         }
         retVal.append(hexValue);
      }
      return retVal.toString();
   }
   
   /*
   public String createHash(long input, String hashAlgorithm) {
      // Generate a byte array from the long
      // Extract the byte value of the long
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            dos.writeLong(input);
        } catch (IOException e) {
           e.printStackTrace();
        }
        try {
            dos.flush();
        } catch (IOException e) {
           e.printStackTrace();
        }

        byte[] byteArray = bos.toByteArray();
        return bytesToHex(computeHashBytes(byteArray, hashAlgorithm));
   }
   

   
   private static byte[] computeHashBytes(byte[] inputBytes, String hashAlgorithm) {
      // Instantiate a MessageDigest instance configured with the desired
      // algorithm.
      MessageDigest md = null;
      try {
         md = MessageDigest.getInstance(hashAlgorithm);
      } catch(NoSuchAlgorithmException e) {
         e.printStackTrace();
      }
      
      // This isn't necessary in this context, but should this
      // be refactored to use the MessageDigest as a member this
      // enables the reuse of the same MessageDigest instance.
      md.reset();
      md.update(inputBytes);
      return md.digest();
   }
   
   
   */
}
