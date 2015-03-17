package com.ryanchapin.util.hashgenerator;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HashGeneratorTest {
   
   private static final Logger LOGGER = LoggerFactory.getLogger(HashGeneratorTest.class);
   
   @Rule
   public TestName testName = new TestName();
   
   
   
   
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
      LOGGER.debug("test = {}", hexVal00);
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
      LOGGER.debug("test = {}", hexVal01);
      assertEquals("hexVal01 was not the correct hex value",
            expectedOutput01, hexVal01);
   }
   
   @Test
   public void shouldCorrectlyHashStringInputUTF8() {
      LOGGER.info("Running test: {}", testName.getMethodName());
      
//      String hashUTF8 = HashGenerator.createHash(STRINGS_MAP.get(1), "UTF-8", "MD5");
//      LOGGER.debug("hashUTF8    = {}", hashUTF8);
//      
//      String hashUSASCII = HashGenerator.createHash(STRINGS_MAP.get(1), "US-ASCII", "MD5");
//      LOGGER.debug("hashUSASCII = {}", hashUSASCII);
//      
//      String hashUTF16LE = HashGenerator.createHash(STRINGS_MAP.get(1), "UTF-16LE", "MD5");
//      LOGGER.debug("hashUTF16LE = {}", hashUTF16LE);
      
   }
   
   
   @Test(expected = IllegalArgumentException.class)
   public void shouldThrowIllegalStateExceptionHashLongStaticNullAlgo()
         throws IllegalStateException, NoSuchAlgorithmException, IOException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      
      List<HashTestData<? extends Object>> longList =
            HashGeneratorTestData.testDataMap.get(HashGenerator.DataType.LONG);
      HashTestData<? extends Object> htd = longList.get(0);
      
      HashGenerator.createHash((Long)htd.getData(), "");
   }
   
   @Test
   public void shouldCorrectlyHashLongStatic()
         throws IllegalStateException, NoSuchAlgorithmException, IOException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      
      List<HashTestData<? extends Object>> longList = 
            HashGeneratorTestData.testDataMap.get(HashGenerator.DataType.LONG);
      for (HashTestData<? extends Object> htd : longList) {
         Long testData       = (Long) htd.getData();
         String expectedHash = htd.getHash();
         HashAlgo algo       = htd.getAlgo();
         
         String hash = HashGenerator.createHash(testData, algo.getAlgo());
         assertEquals("Returned hash does not match expected", expectedHash, hash);
      }
   }
   
   @Test
   public void shouldCorrectlyHashLong()
      throws IllegalStateException, NoSuchAlgorithmException, IOException
   {
      LOGGER.info("Running test: {}", testName.getMethodName());
      
      HashGenerator hg = new HashGenerator();
      
      List<HashTestData<? extends Object>> longList = 
            HashGeneratorTestData.testDataMap.get(HashGenerator.DataType.LONG);
      for (HashTestData<? extends Object> htd : longList) {
         Long testData       = (Long) htd.getData();
         String expectedHash = htd.getHash();
         HashAlgo algo       = htd.getAlgo();
      
         hg.setHashAlgo(algo.getAlgo());
         String hash = hg.createHash(testData);
         assertEquals("Returned hash does not match expected", expectedHash, hash);
      }
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
      private HashAlgo algo;
      
      public T getData() {
         return data;
      }

      public String getHash() {
         return hash;
      }

      public HashAlgo getAlgo() {
         return algo;
      }

      public HashTestData(T data, String hash, HashAlgo algo) {
         this.data = data;
         this.hash = hash;
         this.algo = algo;
      }
   }
   
   public static enum HashAlgo {
      MD5SUM("MD5"),
      SHA1SUM("SHA-1"),
      SHA256SUM("SHA-256"),
      SHA384SUM("SHA-384"),
      SHA512SUM("SHA-512");
      
      private String algo;
      
      public String getAlgo() {
         return algo;
      }
      
      private HashAlgo(String algo) {
         this.algo = algo;
      }
   }
}
