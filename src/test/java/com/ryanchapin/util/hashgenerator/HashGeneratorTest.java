package com.ryanchapin.util.hashgenerator;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ryanchapin.util.hashgenerator.HashGenerator.DataType;
import com.ryanchapin.util.hashgenerator.HashGenerator.HashAlgorithm;

public class HashGeneratorTest {
   
   private static final Logger LOGGER = LoggerFactory.getLogger(HashGeneratorTest.class);
   
   /** 
    * The default char encoding we will pass into <code>HashGenerator</code>
    * calls when hashing <code>Strings</code>.
    * <p>
    * IF THIS IS CHANGED, ENSURE TO CHANGE THE SAME FIELD IN THE
    * TestDataGenerator class and then re-generate the test data.
    */
   public static final String DEFAULT_CHAR_ENCODING = "US-ASCII";
   
   @Rule
   public TestName testName = new TestName();
   
   @Test
   public void shouldCorrectlySetHashAlgorithm() {
      LOGGER.info("Running test: {}", testName.getMethodName());
      
      HashGenerator hg = new HashGenerator();
      assertEquals("hashAlgo field was not null in a newly instantiated " +
            "HashGenerator instance", null, hg.getHashAlgo());
      
      hg.setHashAlgo(HashAlgorithm.MD2SUM);
      assertEquals("HashAlgorithm.setHashAlgo did not properly set a given " +
            "algorithm", HashAlgorithm.MD2SUM, hg.getHashAlgo());
      
      hg.setHashAlgo(HashAlgorithm.SHA512SUM);
      assertEquals("HashAlgorithm.setHashAlgo did not properly set a given " +
            "algorithm", HashAlgorithm.SHA512SUM, hg.getHashAlgo());
   }
   
   @Test
   public void shouldCorrectlyConvertBytesToHex() {
      LOGGER.info("Running test: {}", testName.getMethodName());
      
      String expectedOutput00 = "010203180fab9b80";
      byte[] input00 = new byte[] {
            (byte)0b00000001, // 01
            (byte)0b00000010, // 02
            (byte)0b00000011, // 03
            (byte)0b00011000, // 18
            (byte)0b00001111, // 0f
            (byte)0b10101011, // ab
            (byte)0b10011011, // 9b
            (byte)0b10000000  // 80
      };
      String hexVal00 = HashGenerator.bytesToHex(input00);
      LOGGER.debug("hexVal00 = {}", hexVal00);
      assertEquals("hexVal01 was not the correct hex value",
            expectedOutput00, hexVal00);
      
      String expectedOutput01 = "00fffefdfcfbfaf9f8f7f6f5f4f3f2f1f000";
      byte[] input01 = new byte[] {
            (byte)0b00000000, // 00
            (byte)0b11111111, // ff
            (byte)0b11111110, // fe
            (byte)0b11111101, // fd
            (byte)0b11111100, // fc
            (byte)0b11111011, // fb
            (byte)0b11111010, // fa
            (byte)0b11111001, // f9
            (byte)0b11111000, // f8
            (byte)0b11110111, // f7
            (byte)0b11110110, // f6
            (byte)0b11110101, // f5
            (byte)0b11110100, // f4
            (byte)0b11110011, // f3
            (byte)0b11110010, // f2
            (byte)0b11110001, // f1
            (byte)0b11110000, // f0
            (byte)0b00000000  // 00
      };
      String hexVal01 = HashGenerator.bytesToHex(input01);
      LOGGER.debug("hexVal01 = {}", hexVal01);
      assertEquals("hexVal01 was not the correct hex value",
            expectedOutput01, hexVal01);
   }
   
   /** -- Byte Tests ------------------------------------------------------- */
   
   @Test
   public void shouldCorrectlyHashByteInstance()
      throws NoSuchAlgorithmException, IllegalStateException,
      UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashUsingInstanceMethod(DataType.BYTE);
   }
   
   @Test
   public void shouldCorrectlyHashByteStatic()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashUsingStaticMethod(DataType.BYTE);
   }
   
   @Test
   public void shouldCorrectlyHashByteReusingInternalInstances()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashReusingInternalInstances(DataType.BYTE);
   }
   
   /** -- Short Tests ------------------------------------------------------ */
   
   @Test
   public void shouldCorrectlyHashShortInstance()
      throws NoSuchAlgorithmException, IllegalStateException,
      UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashUsingInstanceMethod(DataType.SHORT);
   }
   
   @Test
   public void shouldCorrectlyHashShortStatic()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashUsingStaticMethod(DataType.SHORT);
   }
   
   @Test
   public void shouldCorrectlyHashShortReusingInternalInstances()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashReusingInternalInstances(DataType.SHORT);
   }
   
   /** -- Character Tests -------------------------------------------------- */
   
   @Test
   public void shouldCorrectlyHashCharacterInstance()
      throws NoSuchAlgorithmException, IllegalStateException,
      UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashUsingInstanceMethod(DataType.CHARACTER);
   }
   
   @Test
   public void shouldCorrectlyHashCharacterStatic()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashUsingStaticMethod(DataType.CHARACTER);
   }
   
   @Test
   public void shouldCorrectlyHashCharacterReusingInternalInstances()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashReusingInternalInstances(DataType.CHARACTER);
   }
   
   /** -- Integer Tests ---------------------------------------------------- */

   @Test
   public void shouldCorrectlyHashIntegerInstance()
      throws NoSuchAlgorithmException, IllegalStateException,
      UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashUsingInstanceMethod(DataType.INTEGER);
   }
   
   @Test
   public void shouldCorrectlyHashIntegerStatic()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashUsingStaticMethod(DataType.INTEGER);
   }
   
   @Test
   public void shouldCorrectlyHashIntegerReusingInternalInstances()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashReusingInternalInstances(DataType.INTEGER);
   }
   
   /** -- Long Tests ------------------------------------------------------- */
   
   @Test
   public void shouldCorrectlyHashLongInstance()
      throws NoSuchAlgorithmException, IllegalStateException,
      UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashUsingInstanceMethod(DataType.LONG);
   }
   
   @Test
   public void shouldCorrectlyHashLongStatic()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashUsingStaticMethod(DataType.LONG);
   }
   
   @Test
   public void shouldCorrectlyHashLongReusingInternalInstances()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashReusingInternalInstances(DataType.LONG);
   }

   /** -- Float Tests ------------------------------------------------------ */
   
   @Test
   public void shouldCorrectlyHashFloatInstance()
      throws NoSuchAlgorithmException, IllegalStateException,
      UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashUsingInstanceMethod(DataType.FLOAT);
   }
   
   @Test
   public void shouldCorrectlyHashFloatStatic()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashUsingStaticMethod(DataType.FLOAT);
   }
   
   @Test
   public void shouldCorrectlyHashFloatReusingInternalInstances()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashReusingInternalInstances(DataType.FLOAT);
   }
   
   /** -- Double Tests ----------------------------------------------------- */ 
   
   @Test
   public void shouldCorrectlyHashDoubleInstance()
      throws NoSuchAlgorithmException, IllegalStateException,
      UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashUsingInstanceMethod(DataType.DOUBLE);
   }
   
   @Test
   public void shouldCorrectlyHashDoubleStatic()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashUsingStaticMethod(DataType.DOUBLE);
   }
   
   @Test
   public void shouldCorrectlyHashDoubleReusingInternalInstances()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashReusingInternalInstances(DataType.DOUBLE);
   }
   
   /** -- String Tests ----------------------------------------------------- */
   
   @Test
   public void shouldCorrectlyHashStringInstance()
      throws NoSuchAlgorithmException, UnsupportedEncodingException,
      IllegalStateException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashUsingInstanceMethod(DataType.STRING);
   }
   
   @Test
   public void shouldCorrectlyHashStringStatic()
         throws NoSuchAlgorithmException, UnsupportedEncodingException,
         IllegalStateException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashUsingStaticMethod(DataType.STRING);
   }

   @Test
   public void shouldCorrectlyHashStringReusingInternalInstances()
         throws UnsupportedEncodingException, IllegalStateException,
         NoSuchAlgorithmException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashReusingInternalInstances(DataType.STRING);
   }
   
   @Test(expected = IllegalArgumentException.class)
   public void shouldThrowIllegalArgExptnWhenPassedEmptyEncodingHashString()
         throws UnsupportedEncodingException, IllegalStateException,
         NoSuchAlgorithmException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      DataType type = DataType.STRING;  
      HashTestData<? extends Object> htd = getSingleHashTestDataObject(type);
      
      String data = (String) htd.getData();
      HashAlgorithm algo   = htd.getAlgo();
      HashGenerator hg     = new HashGenerator(algo);
      @SuppressWarnings("unused")
      String hash          = hg.createHash(data, "");
   }
   
   @Test(expected = IllegalArgumentException.class)
   public void shouldThrowIllegalArgExptnWhenPassedNullEncodingHashString()
         throws UnsupportedEncodingException, IllegalStateException,
         NoSuchAlgorithmException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      DataType type = DataType.STRING;
      HashTestData<? extends Object> htd = getSingleHashTestDataObject(type);

      String data = (String) htd.getData();
      HashAlgorithm algo = htd.getAlgo();
      HashGenerator hg = new HashGenerator(algo);
      @SuppressWarnings("unused")
      String hash = hg.createHash(data, null);
   }   
      
   /** -- Consolidated Tests ----------------------------------------------- */
   
   /**
    * Consolidated set of tests for each of the createHash member methods that
    * tests that the HashGenerator should throw an IllegalStateException
    * when a <code>HashGenerator</code> instance was instantiated passing
    * a null HashAlgorithm.
    * 
    * @throws NoSuchAlgorithmException
    */
   @Test
   @SuppressWarnings("unused")
   public void shouldThrowIllegalStateExceptionHashSetToNull()
         throws NoSuchAlgorithmException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      
      // Map to keep track of all of the expected types for which
      // we should get an IllegalArgumentException thrown.
      Map<DataType, Boolean> exceptionMap = getDataTypeBooleanMap();
      
      HashTestData<? extends Object> htd = null;
      DataType type                      = null;
      HashGenerator hg                   = null;
      
      type = DataType.BYTE;
      htd  = getSingleHashTestDataObject(type);
      hg   = new HashGenerator(null);
      try {
         String hash = hg.createHash((Byte)htd.getData());
      } catch (IllegalStateException e) {
         LOGGER.info(type.toString() + " threw expected IllegalStateException");
         exceptionMap.put(type, true);
      }
      
      type = DataType.CHARACTER;
      htd  = getSingleHashTestDataObject(type);
      hg   = new HashGenerator(null);
      try {
         String hash = hg.createHash((Character)htd.getData());
      } catch (IllegalStateException e) {
         LOGGER.info(type.toString() + " threw expected IllegalStateException");
         exceptionMap.put(type, true);
      }
      
      type = DataType.SHORT;
      htd  = getSingleHashTestDataObject(type);
      hg   = new HashGenerator(null);
      try {
         String hash = hg.createHash((Short)htd.getData());
      } catch (IllegalStateException e) {
         LOGGER.info(type.toString() + " threw expected IllegalStateException");
         exceptionMap.put(type, true);
      }
      
      type = DataType.INTEGER;
      htd  = getSingleHashTestDataObject(type);
      hg   = new HashGenerator(null);
      try {
         String hash = hg.createHash((Integer)htd.getData());
      } catch (IllegalStateException e) {
         LOGGER.info(type.toString() + " threw expected IllegalStateException");
         exceptionMap.put(type, true);
      }
      
      type = DataType.LONG;
      htd  = getSingleHashTestDataObject(type);
      hg   = new HashGenerator(null);
      try {
         String hash = hg.createHash((Long)htd.getData());
      } catch (IllegalStateException e) {
         LOGGER.info(type.toString() + " threw expected IllegalStateException");
         exceptionMap.put(type, true);
      }
      
      type = DataType.FLOAT;
      htd  = getSingleHashTestDataObject(type);
      hg   = new HashGenerator(null);
      try {
         String hash = hg.createHash((Float)htd.getData());
      } catch (IllegalStateException e) {
         LOGGER.info(type.toString() + " threw expected IllegalStateException");
         exceptionMap.put(type, true);
      }
      
      type = DataType.DOUBLE;
      htd  = getSingleHashTestDataObject(type);
      hg   = new HashGenerator(null);
      try {
         String hash = hg.createHash((Double)htd.getData());
      } catch (IllegalStateException e) {
         LOGGER.info(type.toString() + " threw expected IllegalStateException");
         exceptionMap.put(type, true);
      }
      
      type = DataType.STRING;
      htd  = getSingleHashTestDataObject(type);
      hg   = new HashGenerator(null);
      try {
         String hash = hg.createHash((String)htd.getData(), "UTF-8");
      } catch (IllegalStateException e) {
         LOGGER.info(type.toString() + " threw expected IllegalStateException");
         exceptionMap.put(type, true);
      } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
      }
      
      String errMsg = "createHash method did NOT throw a " +
            "IllegalStateException when the method was invoked on an " +
            "instance when the HashAlgorithm was instantiated with a " +
            "null HashAlgorithm argument";
      validateDataTypeBooleanMap(errMsg, exceptionMap);
      
   }
   
   /**
    * Consolidated set of tests for each of the createHash member methods that
    * tests that the HashGenerator should throw an IllegalStateException
    * when a <code>HashGenerator</code> instance was not instantiated passing
    * a HashAlgorithm and the instance was not configured properly by
    * invoking {@link HashGenerator#setHashAlgo(HashAlgorithm)} with a valid
    * HashAlgorithm.
    * 
    * @throws NoSuchAlgorithmException
    */
   @Test
   @SuppressWarnings("unused")
   public void shouldThrowIllegalStateExceptionHashNeverSetAlgo()
         throws NoSuchAlgorithmException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
            
      // Map to keep track of all of the expected types for which
      // we should get an IllegalArgumentException thrown.
      Map<DataType, Boolean> exceptionMap = getDataTypeBooleanMap();
      
      HashTestData<? extends Object> htd = null;
      DataType type                      = null;
      HashGenerator hg                   = null;
      
      type = DataType.BYTE;
      htd  = getSingleHashTestDataObject(type);
      hg   = new HashGenerator();
      try {
         String hash = hg.createHash((Byte)htd.getData());
      } catch (IllegalStateException e) {
         LOGGER.info(type.toString() + " threw expected IllegalStateException");
         exceptionMap.put(type, true);
      }
      
      type = DataType.CHARACTER;
      htd  = getSingleHashTestDataObject(type);
      hg   = new HashGenerator();
      try {
         String hash = hg.createHash((Character)htd.getData());
      } catch (IllegalStateException e) {
         LOGGER.info(type.toString() + " threw expected IllegalStateException");
         exceptionMap.put(type, true);
      }
      
      type = DataType.SHORT;
      htd  = getSingleHashTestDataObject(type);
      hg   = new HashGenerator();
      try {
         String hash = hg.createHash((Short)htd.getData());
      } catch (IllegalStateException e) {
         LOGGER.info(type.toString() + " threw expected IllegalStateException");
         exceptionMap.put(type, true);
      }
      
      type = DataType.INTEGER;
      htd  = getSingleHashTestDataObject(type);
      hg   = new HashGenerator();
      try {
         String hash = hg.createHash((Integer)htd.getData());
      } catch (IllegalStateException e) {
         LOGGER.info(type.toString() + " threw expected IllegalStateException");
         exceptionMap.put(type, true);
      }
      
      type = DataType.LONG;
      htd  = getSingleHashTestDataObject(type);
      hg   = new HashGenerator();
      try {
         String hash = hg.createHash((Long)htd.getData());
      } catch (IllegalStateException e) {
         LOGGER.info(type.toString() + " threw expected IllegalStateException");
         exceptionMap.put(type, true);
      }
      
      type = DataType.FLOAT;
      htd  = getSingleHashTestDataObject(type);
      hg   = new HashGenerator();
      try {
         String hash = hg.createHash((Float)htd.getData());
      } catch (IllegalStateException e) {
         LOGGER.info(type.toString() + " threw expected IllegalStateException");
         exceptionMap.put(type, true);
      }
      
      type = DataType.DOUBLE;
      htd  = getSingleHashTestDataObject(type);
      hg   = new HashGenerator();
      try {
         String hash = hg.createHash((Double)htd.getData());
      } catch (IllegalStateException e) {
         LOGGER.info(type.toString() + " threw expected IllegalStateException");
         exceptionMap.put(type, true);
      }
      
      type = DataType.STRING;
      htd  = getSingleHashTestDataObject(type);
      hg   = new HashGenerator();
      try {
         String hash = hg.createHash((String)htd.getData(), "UTF-8");
      } catch (IllegalStateException e) {
         LOGGER.info(type.toString() + " threw expected IllegalStateException");
         exceptionMap.put(type, true);
      } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
      }
      
      String errMsg = "createHash method did NOT throw a " +
            "IllegalStateException when the method was invoked on an " +
            "instance with a null HashAlgorithm";
      validateDataTypeBooleanMap(errMsg, exceptionMap);
   }
   
   /**
    * Consolidated set of test for each of the static methods to test that they
    * each should throw and IllegalArgumentException when passed a null
    * HashAlgorithm argument.
    * 
    * @throws IllegalStateException
    * @throws NoSuchAlgorithmException
    */
   @Test
   public void shouldThrowIllegalArgumentExceptionStaticNullAlgo()
         throws NoSuchAlgorithmException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      
      // Map to keep track of all of the expected types for which
      // we should get an IllegalArgumentException thrown.
      Map<DataType, Boolean> exceptionMap = getDataTypeBooleanMap();
      
      HashTestData<? extends Object> htd = null;
      DataType type = null;

      type = DataType.BYTE;
      htd  = getSingleHashTestDataObject(type);
      try {
         HashGenerator.createHash((Byte)htd.getData(), null);
      } catch (IllegalArgumentException e) {
         LOGGER.info(type.toString() + " threw expected IllegalArgumentException");
         exceptionMap.put(type, true);
      }
      
      type = DataType.CHARACTER;
      htd  = getSingleHashTestDataObject(type);
      try {
         HashGenerator.createHash((Character)htd.getData(), null);
      } catch (IllegalArgumentException e) {
         LOGGER.info(type.toString() + " threw expected IllegalArgumentException");
         exceptionMap.put(type, true);
      }
      
      type = DataType.SHORT;
      htd  = getSingleHashTestDataObject(type);
      try {
         HashGenerator.createHash((Short)htd.getData(), null);
      } catch (IllegalArgumentException e) {
         LOGGER.info(type.toString() + " threw expected IllegalArgumentException");
         exceptionMap.put(type, true);
      }
      
      type = DataType.INTEGER;
      htd  = getSingleHashTestDataObject(type);
      try {
         HashGenerator.createHash((Integer)htd.getData(), null);
      } catch (IllegalArgumentException e) {
         LOGGER.info(type.toString() + " threw expected IllegalArgumentException");
         exceptionMap.put(type, true);
      }

      type = DataType.LONG;
      htd  = getSingleHashTestDataObject(type);
      try {
         HashGenerator.createHash((Long)htd.getData(), null);
      } catch (IllegalArgumentException e) {
         LOGGER.info(type.toString() + " threw expected IllegalArgumentException");
         exceptionMap.put(type, true);
      }
      
      type = DataType.FLOAT;
      htd  = getSingleHashTestDataObject(type);
      try {
         HashGenerator.createHash((Float)htd.getData(), null);
      } catch (IllegalArgumentException e) {
         LOGGER.info(type.toString() + " threw expected IllegalArgumentException");
         exceptionMap.put(type, true);
      }
      
      type = DataType.DOUBLE;
      htd  = getSingleHashTestDataObject(type);
      try {
         HashGenerator.createHash((Double)htd.getData(), null);
      } catch (IllegalArgumentException e) {
         LOGGER.info(type.toString() + " threw expected IllegalArgumentException");
         exceptionMap.put(type, true);
      }
      
      type = DataType.STRING;
      htd  = getSingleHashTestDataObject(type);
      try {
         HashGenerator.createHash((String)htd.getData(), "UTF-8", null);
      } catch (IllegalArgumentException e) {
         LOGGER.info(type.toString() + " threw expected IllegalArgumentException");
         exceptionMap.put(type, true);
      } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
      }
      
      String errMsg = "createHash method did NOT throw an " +
            "IllegalArgumentException when invoked with a null HashAlgorithm";
      validateDataTypeBooleanMap(errMsg, exceptionMap);
   }
   
   /** -- Utility Methods -------------------------------------------------- */

   public void hashReusingInternalInstances(DataType type)
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {   
      // Single hash algorithm to be used
      HashAlgorithm algo = HashAlgorithm.MD5SUM;
      HashGenerator hg = new HashGenerator(algo);

      List<HashTestData<? extends Object>> list =
            HashGeneratorTestData.testDataMap.get(type);
      String expectedHash = null;
      String hash         = null;
      Object data         = null;
      
      for (HashTestData<? extends Object> htd : list) {
         if (algo == htd.getAlgo()) {
            expectedHash = htd.getHash();
            data         = htd.getData();
            
            switch (type) {
            
               case BYTE:
                  Byte byteData = (Byte) data;
                  hash = hg.createHash(byteData);
                  break;
               case CHARACTER:
                  Character characterData = (Character) data;
                  hash = hg.createHash(characterData);
                  break;
               case SHORT:
                  Short shortData = (Short) data;
                  hash = hg.createHash(shortData);
                  break;
               case INTEGER:
                  Integer integerData = (Integer) data;
                  hash = hg.createHash(integerData);
                  break;
               case LONG:
                  Long longData = (Long) data;
                  hash = hg.createHash(longData);
                  break;
               case FLOAT:
                  Float floatData = (Float) data;
                  hash = hg.createHash(floatData);
                  break;
               case DOUBLE:
                  Double doubleData = (Double) data;
                  hash = hg.createHash(doubleData);
                  break;
               case STRING:
                  String stringData = (String) data;
                  hash = hg.createHash(stringData, DEFAULT_CHAR_ENCODING);
                  break;
               default:
                  break;   
            }

            LOGGER.info("'{}' hash for type '{}', '{}' = {}",
                  algo, type, data, hash);
            String errMsg = "Returned hash for type '" + type +
                  "' for value '" + data + "' does not match expected";
            assertEquals(errMsg, expectedHash, hash);
         }
      } 
   }
   
   public void hashUsingStaticMethod(DataType type)
         throws UnsupportedEncodingException, IllegalStateException,
         NoSuchAlgorithmException
   {
      List<HashTestData<? extends Object>> list =
            HashGeneratorTestData.testDataMap.get(type);
      
      for (HashTestData<? extends Object> htd : list) {
         String expectedHash = htd.getHash();
         HashAlgorithm algo  = htd.getAlgo();
         Object data         = htd.getData();
         String hash         = null;
         
         switch (type) {
            case BYTE:
               Byte byteData = (Byte) data;
               hash = HashGenerator.createHash(byteData, algo);
               break;
            case CHARACTER:
               Character characterData = (Character) data;
               hash = HashGenerator.createHash(characterData, algo);
               break;
            case SHORT:
               Short shortData = (Short) data;
               hash = HashGenerator.createHash(shortData, algo);
               break;
            case INTEGER:
               Integer integerData = (Integer) data;
               hash = HashGenerator.createHash(integerData, algo);
               break;
            case LONG:
               Long longData = (Long) data;
               hash = HashGenerator.createHash(longData, algo);
               break;
            case FLOAT:
               Float floatData = (Float) data;
               hash = HashGenerator.createHash(floatData, algo);
               break;
            case DOUBLE:
               Double doubleData = (Double) data;
               hash = HashGenerator.createHash(doubleData, algo);
               break;
            case STRING:
               String stringData = (String) data;
               hash = HashGenerator.createHash(
                     stringData, DEFAULT_CHAR_ENCODING, algo);
               break;
            default:
         }

         LOGGER.info("'{}' hash for type '{}', '{}' = {}",
               algo, type, data, hash);
         String errMsg = "Returned hash for type '" + type +
               "' for value '" + data + "' does not match expected";
         assertEquals(errMsg, expectedHash, hash);
      }
   }
   
   public void hashUsingInstanceMethod(DataType type)
         throws UnsupportedEncodingException, IllegalStateException,
         NoSuchAlgorithmException
   {
      HashGenerator hg = new HashGenerator();
      
      List<HashTestData<? extends Object>> list =
            HashGeneratorTestData.testDataMap.get(type);
      
      for (HashTestData<? extends Object> htd : list) {
         String expectedHash = htd.getHash();
         HashAlgorithm algo  = htd.getAlgo();
         Object data         = htd.getData();
         String hash         = null;
         
         hg.setHashAlgo(algo);

         switch (type) {
            case BYTE:
               Byte byteData = (Byte) data;
               hash = hg.createHash(byteData);
               break;
            case CHARACTER:
               Character characterData = (Character) data;
               hash = hg.createHash(characterData);
               break;
            case SHORT:
               Short shortData = (Short) data;
               hash = hg.createHash(shortData);
               break;
            case INTEGER:
               Integer integerData = (Integer) data;
               hash = hg.createHash(integerData);
               break;
            case LONG:
               Long longData = (Long) data;
               hash = hg.createHash(longData);
               break;
            case FLOAT:
               Float floatData = (Float) data;
               hash = hg.createHash(floatData);
               break;
            case DOUBLE:
               Double doubleData = (Double) data;
               hash = hg.createHash(doubleData);
               break;
            case STRING:
               String stringData = (String) data;
               hash = hg.createHash(stringData, DEFAULT_CHAR_ENCODING);
               break;
            default:
         }
         
         LOGGER.info("'{}' hash for type '{}', '{}' = {}",
               algo, type, data, hash);
         String errMsg = "Returned hash for type '" + type +
               "' for value '" + data + "' does not match expected";
         assertEquals(errMsg, expectedHash, hash);
      }
   }
   
   /**
    * Instantiates and configures a Map<DataType, Boolean> instance pre-
    * set with all Boolean values to null.
    * 
    * @return  map instance used to keep track of boolean flags for each
    *          element in the @link {@link DataType} enum.
    */
   private Map<DataType, Boolean> getDataTypeBooleanMap() {
      Map<DataType, Boolean> retVal = new HashMap<>();
      for (DataType t : DataType.values()) {
         retVal.put(t, false);
      }
      return retVal;
   }
   
   private void validateDataTypeBooleanMap(String errMsg,
         Map<DataType, Boolean> map)
   {
      // Confirm that for each element in the map that we have a true
      // value set.
      for (Map.Entry<DataType, Boolean> entry : map.entrySet()) {
         DataType dt = entry.getKey();
         Boolean val = entry.getValue();
         assertEquals(dt + ": " + errMsg, true, val);
      }
   }
   
   /**
    * Utility for getting a single HashTestData instance of a given type.
    * 
    * @param  type
    *         The {@link HashGenerator.DataType} to be returned.
    * @return a {@link HashGeneratorTestData} instance of the requested type.
    */
   private HashTestData<? extends Object>
      getSingleHashTestDataObject(HashGenerator.DataType type)
   {
      List<HashTestData<? extends Object>> longList =
            HashGeneratorTestData.testDataMap.get(type);
      return longList.get(0);
   }
   
   /**
    * Data container object to store data and its expected hash value.
    * 
    * @author Ryan Chapin
    * @since  2015-03-12
    * @param <T> - data type that will be hashed
    */
   public static class HashTestData<T> {
      private T data;
      private String hash;
      private HashAlgorithm algo;
      
      public T getData() {
         return data;
      }

      public String getHash() {
         return hash;
      }

      public HashAlgorithm getAlgo() {
         return algo;
      }

      public HashTestData(T data, String hash, HashAlgorithm algo) {
         this.data = data;
         this.hash = hash;
         this.algo = algo;
      }
   }
}
