package com.ryanchapin.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ryanchapin.util.HashGenerator.DataType;
import com.ryanchapin.util.HashGenerator.HashAlgorithm;

public class HashGeneratorTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(HashGeneratorTest.class);

   private static final String NUM_THREADS_PROP_KEY = "hashGen.multithread.test.numThreads";
//   private static final int NUM_THREADS_DEFAULT     = 8;
   private static final int NUM_THREADS_DEFAULT     = 1;

   private static final String NUM_ITER_PROP_KEY    = "hashGen.multithread.test.numIter";
   private static final int NUM_ITER_DEFAULT        = 32;

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

   @Test
   public void shouldClearByteArray()
         throws IllegalStateException, NoSuchAlgorithmException,
         NoSuchFieldException, SecurityException,
         IllegalArgumentException, IllegalAccessException
   {
      HashGenerator hg = new HashGenerator(HashAlgorithm.SHA1SUM);
      HashTestData<? extends Object> htd =
            getSingleHashTestDataObject(DataType.DOUBLE);
      @SuppressWarnings("unused")
      String hash = hg.createHash((Double) htd.getData());

      // Use reflection to get access to the private byteArrayMap
      Class<HashGenerator> hashGeneratorClazz = HashGenerator.class;
      Field byteArrayMapField = hashGeneratorClazz.getDeclaredField("byteArrayMap");
      byteArrayMapField.setAccessible(true);
      @SuppressWarnings("unchecked")
      Map<DataType, byte[]> byteArrayMap = (Map<DataType, byte[]>) byteArrayMapField.get(hg);

      byte[] arr = byteArrayMap.get(DataType.DOUBLE);
      for (int i = 0; i < arr.length; i++) {
         assertEquals("Byte in array to be cleared was NOT set to 0x00",
               0x00, arr[i]);
      }
   }

   @Test
   public void shouldClearByteArrayStatic()
         throws NoSuchMethodException, SecurityException,
         IllegalAccessException, IllegalArgumentException,
         InvocationTargetException
   {
      byte[] arr = new byte[] {-128, 72, 64, 0, 15 -6};

      // Use reflection to access and invoke the private static method
      Class<HashGenerator> hashGeneratorClazz = HashGenerator.class;

      Method clearByteArrayMethod =
            hashGeneratorClazz.getDeclaredMethod("clearByteArray", new Class[]{byte[].class});
      clearByteArrayMethod.setAccessible(true);
      clearByteArrayMethod.invoke(null, new Object[]{arr});

      for (int i = 0; i < arr.length; i++) {
         assertEquals("Byte in array to be cleared was NOT set to 0x00",
               0x00, arr[i]);
      }
   }

   /** -- Byte Tests ------------------------------------------------------- */

   @Test
   public void shouldCorrectlyHashByteInstance()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashScalarUsingInstanceMethod(DataType.BYTE);
   }

   @Test
   public void shouldCorrectlyHashByteStatic()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashScalarUsingStaticMethod(DataType.BYTE);
   }

   @Test
   public void shouldCorrectlyHashByteReusingInternalInstances()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashScalarReusingInternalInstances(DataType.BYTE);
   }

   /** -- Short Tests ------------------------------------------------------ */

   @Test
   public void shouldCorrectlyHashShortInstance()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashScalarUsingInstanceMethod(DataType.SHORT);
   }

   @Test
   public void shouldCorrectlyHashShortStatic()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashScalarUsingStaticMethod(DataType.SHORT);
   }

   @Test
   public void shouldCorrectlyHashShortReusingInternalInstances()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashScalarReusingInternalInstances(DataType.SHORT);
   }

   /** -- Character Tests -------------------------------------------------- */

   @Test
   public void shouldCorrectlyHashCharacterInstance()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashScalarUsingInstanceMethod(DataType.CHARACTER);
   }

   @Test
   public void shouldCorrectlyHashCharacterStatic()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashScalarUsingStaticMethod(DataType.CHARACTER);
   }

   @Test
   public void shouldCorrectlyHashCharacterReusingInternalInstances()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashScalarReusingInternalInstances(DataType.CHARACTER);
   }

   /** -- Integer Tests ---------------------------------------------------- */

   @Test
   public void shouldCorrectlyHashIntegerInstance()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashScalarUsingInstanceMethod(DataType.INTEGER);
   }

   @Test
   public void shouldCorrectlyHashIntegerStatic()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashScalarUsingStaticMethod(DataType.INTEGER);
   }

   @Test
   public void shouldCorrectlyHashIntegerReusingInternalInstances()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashScalarReusingInternalInstances(DataType.INTEGER);
   }

   /** -- Long Tests ------------------------------------------------------- */

   @Test
   public void shouldCorrectlyHashLongInstance()
      throws NoSuchAlgorithmException, IllegalStateException,
      UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashScalarUsingInstanceMethod(DataType.LONG);
   }

   @Test
   public void shouldCorrectlyHashLongStatic()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashScalarUsingStaticMethod(DataType.LONG);
   }

   @Test
   public void shouldCorrectlyHashLongReusingInternalInstances()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashScalarReusingInternalInstances(DataType.LONG);
   }

   /** -- Float Tests ------------------------------------------------------ */

   @Test
   public void shouldCorrectlyHashFloatInstance()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashScalarUsingInstanceMethod(DataType.FLOAT);
   }

   @Test
   public void shouldCorrectlyHashFloatStatic()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashScalarUsingStaticMethod(DataType.FLOAT);
   }

   @Test
   public void shouldCorrectlyHashFloatReusingInternalInstances()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashScalarReusingInternalInstances(DataType.FLOAT);
   }

   /** -- Double Tests ----------------------------------------------------- */

   @Test
   public void shouldCorrectlyHashDoubleInstance()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashScalarUsingInstanceMethod(DataType.DOUBLE);
   }

   @Test
   public void shouldCorrectlyHashDoubleStatic()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashScalarUsingStaticMethod(DataType.DOUBLE);
   }

   @Test
   public void shouldCorrectlyHashDoubleReusingInternalInstances()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashScalarReusingInternalInstances(DataType.DOUBLE);
   }

   /** -- String Tests ----------------------------------------------------- */

   @Test
   public void shouldCorrectlyHashStringInstance()
      throws NoSuchAlgorithmException, UnsupportedEncodingException,
      IllegalStateException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashScalarUsingInstanceMethod(DataType.STRING);
   }

   @Test
   public void shouldCorrectlyHashStringStatic()
         throws NoSuchAlgorithmException, UnsupportedEncodingException,
         IllegalStateException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashScalarUsingStaticMethod(DataType.STRING);
   }

   @Test
   public void shouldCorrectlyHashStringReusingInternalInstances()
         throws UnsupportedEncodingException, IllegalStateException,
         NoSuchAlgorithmException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashScalarReusingInternalInstances(DataType.STRING);
   }

   @Test(expected = UnsupportedEncodingException.class)
   public void shouldThrowIllegalArgExptnWhenPassedEmptyEncodingHashString()
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
      String hash = hg.createHash(data, "");
   }

   @Test(expected = UnsupportedEncodingException.class)
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

   /** -- Character Array Tests -------------------------------------------- */
   @Test
   public void shouldCorrectlyHashByteArrayInstance()
       throws NoSuchAlgorithmException, IllegalStateException,
       UnsupportedEncodingException
   {
     LOGGER.info("Running test: {}", testName.getMethodName());
     hashArrayUsingInstanceMethod(DataType.BYTE_ARRAY);
    }


   /** -- Character Array Tests -------------------------------------------- */

   @Test
   public void shouldCorrectlyHashCharArrayInstance()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashArrayUsingInstanceMethod(DataType.CHARACTER_ARRAY);
   }

   @Test
   public void shouldCorrectlyHashCharArrayStatic()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashArrayUsingStaticMethod(DataType.CHARACTER_ARRAY);
   }

   @Test
   public void shouldCorrectlyHashCharArrayReusingInternalInstances()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashArrayReusingInternalInstances(DataType.CHARACTER_ARRAY);
   }



   @Test
   public void shouldCorrectlyHashStringArrayStatic()
         throws NoSuchAlgorithmException, IllegalStateException,
         UnsupportedEncodingException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      hashArrayUsingStaticMethod(DataType.STRING_ARRAY);
   }



   /** -- Multi-threaded Tests --------------------------------------------- */

   @Test
   public void shouldCorrectlyHashScalarStaticMultiThreaded()
         throws InterruptedException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      // Create an object in which will then instantiate multiple threads.
      // Each thread will hash each type n number of times in succession
      // When finished, each thread will write back it's result to
      // the parent object which will then aggregate the results.

      // Read, if set, the system properties to set the numThreads and
      // numIter values for the test
      //
      // Number of threads to instantiate for each DataType to test
      int numThreads = getSystemPropertyInt(NUM_THREADS_PROP_KEY, NUM_THREADS_DEFAULT);

      // Number of times each thread will each thread will loop through the
      // test data set, each time hashing the configured type.
      int numIter = getSystemPropertyInt(NUM_ITER_PROP_KEY, NUM_ITER_DEFAULT);

      LOGGER.info("numThreads = {}, numIter = {}", numThreads, numIter);

      Map<DataType, Map<String, Integer>> expectedResults =
            buildExpectedResultsMap(numThreads, numIter);

      ClientTest clientTest = new ClientTest(numThreads, numIter);
      clientTest.init();

      Thread t = new Thread(clientTest);
      t.start();
      t.join();

      // Now we need to compare the expected results against the hashes
      // and their counts that we collected from our ClientTest class.
      Map<DataType, Map<String, Integer>> actualResults =
            clientTest.getResultMap();

      Iterator<Map.Entry<DataType, Map<String, Integer>>> itr =
            expectedResults.entrySet().iterator();
      while (itr.hasNext()) {
         Map.Entry<DataType, Map<String, Integer>> entry = itr.next();
         DataType expectedType               = entry.getKey();
         Map<String, Integer> expectedCounts = entry.getValue();

         if (actualResults.containsKey(expectedType)) {
            Map<String, Integer> actualCounts = actualResults.get(expectedType);
            boolean countsMatch = expectedCounts.equals(actualCounts);
            String errMsg = "For type '" + expectedType + "', actualCounts " +
                  "did not match expectedCounts";
            assertEquals(errMsg, true, countsMatch);
         } else {
            fail("Expected type/key was not present in actualResultMap");
         }

         // Remove both of the maps from the parent map
         itr.remove();
         actualResults.remove(expectedType);
      }

      // At this point, both of the Maps should be empty
      assertEquals("expectedResults map was not empty",
            0, expectedResults.size());
      assertEquals("actualResults map was not empty",
            0, actualResults.size());
   }

   private Map<DataType, Map<String, Integer>> buildExpectedResultsMap(
         int numThreads, int numIter)
   {
      int expectedInstances = numThreads * numIter;
      Map<DataType, Map<String, Integer>> expectedResultsMap = new HashMap<>();

      // Generate expectations for Scalar values.
      for (Map.Entry<DataType, List<HashTestData<? extends Object>>> entry :
           HashGeneratorTestData.testDataMap.entrySet())
      {
         DataType type = entry.getKey();
         List<HashTestData<? extends Object>> list = entry.getValue();

         Map<String, Integer> typeResultsMap = null;
         if (expectedResultsMap.containsKey(type)) {
            typeResultsMap = expectedResultsMap.get(type);
         } else {
            typeResultsMap = new HashMap<>();
            expectedResultsMap.put(type, typeResultsMap);
         }

         for (HashTestData<? extends Object> htd : list) {
            String hash = htd.getHash();
            // Add the hash and the number of expected instances we will
            // be looking for.
            typeResultsMap.put(hash, expectedInstances);
         }
         expectedResultsMap.put(type, typeResultsMap);
      }

      // Generate expectations for Array values.
      for (Map.Entry<DataType, List<HashTestDataList<? extends Object>>> entry :
           HashGeneratorTestData.testDataListMap.entrySet())
      {
         DataType type = entry.getKey();
         List<HashTestDataList<? extends Object>> list = entry.getValue();

         Map<String, Integer> typeResultsMap = null;
         if (expectedResultsMap.containsKey(type)) {
            typeResultsMap = expectedResultsMap.get(type);
         } else {
            typeResultsMap = new HashMap<>();
            expectedResultsMap.put(type, typeResultsMap);
         }

         for (HashTestDataList<? extends Object> htdl : list) {
            String hash = htdl.getHash();
            // Add the hash and the number of expected instances we will be
            // looking for.
            typeResultsMap.put(hash, expectedInstances);
         }
         expectedResultsMap.put(type, typeResultsMap);
      }

      return expectedResultsMap;
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

      /*
       * Map to keep track of all of the expected types for which an Exception
       * should be thrown.
       */
      Map<DataType, Boolean> exceptionMap = buildDataTypeBooleanMap();

      DataType type = null;
      HashGenerator hg = null;
      String hash = null;

      type = DataType.BYTE;
      hg = new HashGenerator(null);
      try {
         hash = hg.createHash((byte)1);
      } catch (IllegalStateException e) {
         LOGGER.info(type.toString() + " threw expected IllegalStateException");
         exceptionMap.put(type, true);
      }

      type = DataType.CHARACTER;
      hg = new HashGenerator(null);
      try {
         hash = hg.createHash('a');
      } catch (IllegalStateException e) {
         LOGGER.info(type.toString() + " threw expected IllegalStateException");
         exceptionMap.put(type, true);
      }

      type = DataType.SHORT;
      hg = new HashGenerator(null);
      try {
         hash = hg.createHash((short)1);
      } catch (IllegalStateException e) {
         LOGGER.info(type.toString() + " threw expected IllegalStateException");
         exceptionMap.put(type, true);
      }

      type = DataType.INTEGER;
      hg = new HashGenerator(null);
      try {
         hash = hg.createHash(1);
      } catch (IllegalStateException e) {
         LOGGER.info(type.toString() + " threw expected IllegalStateException");
         exceptionMap.put(type, true);
      }

      type = DataType.LONG;
      hg = new HashGenerator(null);
      try {
         hash = hg.createHash(1L);
      } catch (IllegalStateException e) {
         LOGGER.info(type.toString() + " threw expected IllegalStateException");
         exceptionMap.put(type, true);
      }

      type = DataType.FLOAT;
      hg = new HashGenerator(null);
      try {
         hash = hg.createHash(1F);
      } catch (IllegalStateException e) {
         LOGGER.info(type.toString() + " threw expected IllegalStateException");
         exceptionMap.put(type, true);
      }

      type = DataType.DOUBLE;
      hg = new HashGenerator(null);
      try {
         hash = hg.createHash(1D);
      } catch (IllegalStateException e) {
         LOGGER.info(type.toString() + " threw expected IllegalStateException");
         exceptionMap.put(type, true);
      }

      type = DataType.STRING;
      hg = new HashGenerator(null);
      try {
          hash = hg.createHash("hello world", "UTF-8");
      } catch (IllegalStateException e) {
         LOGGER.info(type.toString() + " threw expected IllegalStateException");
         exceptionMap.put(type, true);
      } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
      }

      type = DataType.BYTE_ARRAY;
      hg = new HashGenerator(null);
      try {
         hash = hg.createHash(new byte[] {(byte)1, (byte)2});
      } catch (IllegalStateException e) {
         LOGGER.info(type.toString() + " threw expected IllegalStateException");
         exceptionMap.put(type, true);
      }

      type = DataType.CHARACTER_ARRAY;
      hg  = new HashGenerator(null);
      try {
         hash = hg.createHash(new char[] {'a', 'b'});
      } catch (IllegalStateException e) {
         LOGGER.info(type.toString() + " threw expected IllegalStateException");
         exceptionMap.put(type, true);
      }

      type = DataType.SHORT_ARRAY;
      hg = new HashGenerator(null);
      try {
         hash = hg.createHash(new short[] {(short)1, (short)10});
      } catch (IllegalStateException e) {
         LOGGER.info(type.toString() + " threw expected IllegalStateException");
         exceptionMap.put(type, true);
      }

      type = DataType.INTEGER_ARRAY;
      hg = new HashGenerator(null);
      try {
         hash = hg.createHash(new int[] {1, 2});
      } catch (IllegalStateException e) {
         LOGGER.info(type.toString() + " threw expected IllegalStateException");
         exceptionMap.put(type, true);
      }

      type = DataType.LONG_ARRAY;
      hg = new HashGenerator(null);
      try {
         hash = hg.createHash(new long[] {0L, 1L});
      } catch (IllegalStateException e) {
         LOGGER.info(type.toString() + " threw expected IllegalStateException");
         exceptionMap.put(type, true);
      }

      type = DataType.FLOAT_ARRAY;
      hg = new HashGenerator(null);
      try {
         hash = hg.createHash(new float[] {0F, 1F});
      } catch (IllegalStateException e) {
         LOGGER.info(type.toString() + " threw expected IllegalStateException");
         exceptionMap.put(type, true);
      }

      type = DataType.DOUBLE_ARRAY;
      hg = new HashGenerator(null);
      try {
         hash = hg.createHash(new double[] {1D, 2D});
      } catch (IllegalStateException e) {
         LOGGER.info(type.toString() + " threw expected IllegalStateException");
         exceptionMap.put(type, true);
      }

      type = DataType.STRING_ARRAY;
      hg = new HashGenerator(null);
      try {
         hash = hg.createHash(new String[] {"hello world", "don't panic!"}, "UTF-8");
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

//   /**
//    * Consolidated set of tests for each of the createHash member methods that
//    * tests that the HashGenerator should throw an IllegalStateException
//    * when a <code>HashGenerator</code> instance was not instantiated passing
//    * a HashAlgorithm and the instance was not configured properly by
//    * invoking {@link HashGenerator#setHashAlgo(HashAlgorithm)} with a valid
//    * HashAlgorithm.
//    *
//    * @throws NoSuchAlgorithmException
//    */
//   @Test
//   @SuppressWarnings("unused")
//   public void shouldThrowIllegalStateExceptionHashNeverSetAlgo()
//         throws NoSuchAlgorithmException
//   {
//      LOGGER.info("Running test: {}", testName.getMethodName());
//
//      // Map to keep track of all of the expected types for which
//      // we should get an IllegalArgumentException thrown.
//      Map<DataType, Boolean> exceptionMap = buildDataTypeBooleanMap();
//
//      HashTestData<? extends Object> htd = null;
//      DataType type                      = null;
//      HashGenerator hg                   = null;
//      String hash                        = null;
//
//      type = DataType.BYTE;
//      htd  = getSingleHashTestDataObject(type);
//      hg   = new HashGenerator();
//      try {
//         hash = hg.createHash((Byte)htd.getData());
//      } catch (IllegalStateException e) {
//         LOGGER.info(type.toString() + " threw expected IllegalStateException");
//         exceptionMap.put(type, true);
//      }
//
//      type = DataType.CHARACTER;
//      htd  = getSingleHashTestDataObject(type);
//      hg   = new HashGenerator();
//      try {
//         hash = hg.createHash((Character)htd.getData());
//      } catch (IllegalStateException e) {
//         LOGGER.info(type.toString() + " threw expected IllegalStateException");
//         exceptionMap.put(type, true);
//      }
//
//      type = DataType.SHORT;
//      htd  = getSingleHashTestDataObject(type);
//      hg   = new HashGenerator();
//      try {
//         hash = hg.createHash((Short)htd.getData());
//      } catch (IllegalStateException e) {
//         LOGGER.info(type.toString() + " threw expected IllegalStateException");
//         exceptionMap.put(type, true);
//      }
//
//      type = DataType.INTEGER;
//      htd  = getSingleHashTestDataObject(type);
//      hg   = new HashGenerator();
//      try {
//         hash = hg.createHash((Integer)htd.getData());
//      } catch (IllegalStateException e) {
//         LOGGER.info(type.toString() + " threw expected IllegalStateException");
//         exceptionMap.put(type, true);
//      }
//
//      type = DataType.LONG;
//      htd  = getSingleHashTestDataObject(type);
//      hg   = new HashGenerator();
//      try {
//         hash = hg.createHash((Long)htd.getData());
//      } catch (IllegalStateException e) {
//         LOGGER.info(type.toString() + " threw expected IllegalStateException");
//         exceptionMap.put(type, true);
//      }
//
//      type = DataType.FLOAT;
//      htd  = getSingleHashTestDataObject(type);
//      hg   = new HashGenerator();
//      try {
//         hash = hg.createHash((Float)htd.getData());
//      } catch (IllegalStateException e) {
//         LOGGER.info(type.toString() + " threw expected IllegalStateException");
//         exceptionMap.put(type, true);
//      }
//
//      type = DataType.DOUBLE;
//      htd  = getSingleHashTestDataObject(type);
//      hg   = new HashGenerator();
//      try {
//         hash = hg.createHash((Double)htd.getData());
//      } catch (IllegalStateException e) {
//         LOGGER.info(type.toString() + " threw expected IllegalStateException");
//         exceptionMap.put(type, true);
//      }
//
//      type = DataType.STRING;
//      htd  = getSingleHashTestDataObject(type);
//      hg   = new HashGenerator();
//      try {
//         hash = hg.createHash((String)htd.getData(), "UTF-8");
//      } catch (IllegalStateException e) {
//         LOGGER.info(type.toString() + " threw expected IllegalStateException");
//         exceptionMap.put(type, true);
//      } catch (UnsupportedEncodingException e) {
//         e.printStackTrace();
//      }
//
//      type = DataType.CHARACTER_ARRAY;
//      HashTestDataList<? extends Object> htdl =
//            getSingleHashTestDataListObject(type);
//
//      @SuppressWarnings("unchecked")
//      List<Character> charList = (List<Character>) htdl.getData();
//      char[] charArray = convertListToArray(charList, new char[0]);
//      try {
//         hash = hg.createHash(charArray);
//      } catch (IllegalStateException e) {
//         LOGGER.info(type.toString() + " threw expected IllegalArgumentException");
//         exceptionMap.put(type, true);
//      }
//
//      String errMsg = "createHash method did NOT throw a " +
//            "IllegalStateException when the method was invoked on an " +
//            "instance with a null HashAlgorithm";
//      validateDataTypeBooleanMap(errMsg, exceptionMap);
//   }

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
      Map<DataType, Boolean> exceptionMap = buildDataTypeBooleanMap();

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

      type = DataType.CHARACTER_ARRAY;
      HashTestDataList<? extends Object> htdl =
            getSingleHashTestDataListObject(type);

      @SuppressWarnings("unchecked")
      List<Character> charList = (List<Character>) htdl.getData();
      char[] charArray = convertListToArray(charList, new char[0]);
      try {
         HashGenerator.createHash(charArray, null);
      } catch (IllegalArgumentException e) {
         LOGGER.info(type.toString() + " threw expected IllegalArgumentException");
         exceptionMap.put(type, true);
      }

      String errMsg = "createHash method did NOT throw an " +
            "IllegalArgumentException when invoked with a null HashAlgorithm";
      validateDataTypeBooleanMap(errMsg, exceptionMap);
   }

   /** -- Utility Methods -------------------------------------------------- */

   public void hashScalarReusingInternalInstances(DataType type)
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

   public void hashScalarUsingStaticMethod(DataType type)
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

   private void hashScalarUsingInstanceMethod(DataType type)
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

   private void hashArrayReusingInternalInstances(DataType type)
      throws IllegalStateException, NoSuchAlgorithmException
   {
      // Single hash algorithm to be used
      HashAlgorithm algo = HashAlgorithm.MD5SUM;
      HashGenerator hg = new HashGenerator(algo);

      // Get list for the given DataType
      List<HashTestDataList<? extends Object>> list =
         HashGeneratorTestData.testDataListMap.get(type);

      String expectedHash             = null;
      String hash                     = null;
      List<? extends Object>  objList = null;

      for (HashTestDataList<? extends Object> htdl : list) {

         if (algo == htdl.getAlgo()) {
            expectedHash = htdl.getHash();

            switch (type) {
               case CHARACTER_ARRAY:
                  objList          = htdl.getData();
                  char[] charArray = convertListToArray(objList, new char[0]);
                  hash             = hg.createHash(charArray);
                  break;
               default:
            }
         }
         String arrayString = listToString(objList);

         LOGGER.info("'{}' hash for type '{}', '{}' = {}",
               algo, type, arrayString, hash);
         String errMsg = "Returned hash for type '" + type +
               "' for value '" + arrayString + "' does not match expected";
         assertEquals(errMsg, expectedHash, hash);
      }
   }

   private void hashArrayUsingInstanceMethod(DataType type)
         throws IllegalStateException, NoSuchAlgorithmException
   {
      HashGenerator hg = new HashGenerator();

      // Get list for the given DataType
      List<HashTestDataList<? extends Object>> list =
         HashGeneratorTestData.testDataListMap.get(type);

      for (HashTestDataList<? extends Object> htdl : list) {
         String expectedHash             = htdl.getHash();
         HashAlgorithm algo              = htdl.getAlgo();
         String hash                     = null;
         StringBuilder data              = new StringBuilder();
         List<? extends Object>  objList = null;

         hg.setHashAlgo(algo);

         switch (type) {
            case CHARACTER_ARRAY:
               objList          = htdl.getData();
               char[] charArray = convertListToArray(objList, new char[0]);
               hash             = hg.createHash(charArray);
               break;
            default:
         }

         data.delete(0, data.length());
         for (Object o : objList) {
            data.append(o.toString() + ", ");
         }
         data.delete(data.length()-2, data.length());

         LOGGER.info("'{}' hash for type '{}', '{}' = {}",
               algo, type, data, hash);
         String errMsg = "Returned hash for type '" + type +
               "' for value '" + data + "' does not match expected";
         assertEquals(errMsg, expectedHash, hash);
      }
   }

   private void hashArrayUsingStaticMethod(DataType type)
         throws IllegalArgumentException, NoSuchAlgorithmException,
         UnsupportedEncodingException
   {
      // Get list for the given DataType
      List<HashTestDataList<? extends Object>> list =
         HashGeneratorTestData.testDataListMap.get(type);

      for (HashTestDataList<? extends Object> htdl : list) {
         String expectedHash             = htdl.getHash();
         HashAlgorithm algo              = htdl.getAlgo();
         String hash                     = null;
         List<? extends Object>  objList = null;

         switch (type) {
            case CHARACTER_ARRAY:
               objList          = htdl.getData();
               char[] charArray = convertListToArray(objList, new char[0]);
               hash = HashGenerator.createHash(charArray, algo);
               break;
            case STRING_ARRAY:
               objList = htdl.getData();
               String[] stringArray = convertListToArray(objList, new String[0]);
               hash = HashGenerator.createHash(stringArray, DEFAULT_CHAR_ENCODING, algo);
            default:
         }
         String arrayString = listToString(objList);
         LOGGER.info("'{}' hash for type '{}', '{}' = {}",
               algo, type, arrayString, hash);
         String errMsg = "Returned hash for type '" + type +
               "' for value '" + arrayString + "' does not match expected";
         assertEquals(errMsg, expectedHash, hash);
      }
   }

   /**
    * Instantiates and configures a Map<DataType, Boolean> instance pre-
    * set with all Boolean values to false.
    *
    * @return  map instance used to keep track of boolean flags for each
    *          element in the @link {@link DataType} enum.
    */
   private Map<DataType, Boolean> buildDataTypeBooleanMap() {
      Map<DataType, Boolean> retVal = new HashMap<>(DataType.values().length);
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
      getSingleHashTestDataObject(DataType type)
   {
      List<HashTestData<? extends Object>> list =
         HashGeneratorTestData.testDataMap.get(type);
      return list.get(0);
   }

   private HashTestDataList<? extends Object>
      getSingleHashTestDataListObject(DataType type)
   {
      List<HashTestDataList<? extends Object>> list =
         HashGeneratorTestData.testDataListMap.get(type);
      return list.get(0);
   }

   public static char[] convertListToArray(List<? extends Object> list, char[] arr) {
      char[] retVal = new char[list.size()];
      for (int i = 0; i < list.size(); i++) {
         retVal[i] = ((Character) list.get(i)).charValue();
      }
      return retVal;
   }

   public static String[] convertListToArray(List<? extends Object> list, String[] arr) {
      String[] retVal = new String[list.size()];
      for (int i = 0; i < list.size(); i++) {
         retVal[i] = ((String) list.get(i)).toString();
      }
      return retVal;
   }





   public static Map<DataType, Function<List<? extends Object>, ?>> bar =
      new HashMap<>();
   static {

      Function<List<? extends Object>, char[]> charConvertor = (list) -> {
         char[] retVal = new char[list.size()];
         for (int i = 0; i < list.size(); i++) {
            retVal[i] = ((Character) list.get(i)).charValue();
         }
         return retVal;
      };

      Function<List<? extends Object>, String[]> stringConvertor = (list) -> {
         String[] retVal = new String[list.size()];
         for (int i = 0; i < list.size(); i++) {
            retVal[i] = ((String) list.get(i));
         }
         return retVal;
      };

      bar.put(DataType.CHARACTER_ARRAY, charConvertor);
      bar.put(DataType.STRING_ARRAY, stringConvertor);
   }






   public static String listToString(List<? extends Object> list) {
      StringBuilder retVal = new StringBuilder();
      for (Object o : list) {
         retVal.append(o.toString() + ", ");
      }
      retVal.delete(retVal.length()-2, retVal.length());
      return retVal.toString();
   }

   private int getSystemPropertyInt(String propName, int defaultValue) {
      int retVal = 0;
      String propValString = System.getProperty(propName);
      if (null == propValString || propValString.isEmpty()) {
         LOGGER.info(propName
               + " property is not set, using default " + "value: "
               + defaultValue);
         retVal = defaultValue;
      } else {
         try {
            retVal = Integer.parseInt(propValString);
         } catch (NumberFormatException e) {
            e.printStackTrace();
            LOGGER.error(propName+ " was set to an invalid int " +
                  "value.  Using default value of '" +
                  defaultValue + "', instead");
         }
      }
      return retVal;
   }

   /**
    * Data container object to store scalar data and its expected hash value.
    *
    * @author Ryan Chapin
    * @since  2015-03-12
    * @param <T>
    *        data type that will be hashed.
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

   /**
    * Data container object to store arrays of data and the expected hash
    * value.
    *
    * @author Ryan Chapin
    * @since  2015-03-19
    * @param <T>
    *        data type that will be hashed.
    */
   public static class HashTestDataList<T> {
      private List<T> data;
      private String hash;
      private HashAlgorithm algo;

      public List<T> getData() {
         return data;
      }

      public String getHash() {
         return hash;
      }

      public HashAlgorithm getAlgo() {
         return algo;
      }

      public HashTestDataList(List<T> data, String hash, HashAlgorithm algo) {
         this.data = data;
         this.hash = hash;
         this.algo = algo;
      }
   }
}
