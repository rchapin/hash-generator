package com.ryanchapin.util.hashgenerator;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HashGeneratorTest {
   
   private static final Logger LOGGER = LoggerFactory.getLogger(HashGeneratorTest.class);
   
   @Rule
   public TestName testName = new TestName();
   
   private static final String INPUT_STRING_A = "This is a test string";
   private static final String INPUT_STRING_B = "GeNvUM^$$j,gMPO@7s0O@";
   
   /** Array of all of the currently supported hash algorithms */
   private static final String[] HASH_ALGOS = {"MD2", "MD5", "SHA-1", "SHA-256", "SHA-384", "SHA-512"};
   
   // Need to test each of the methods for generating a hash
   // Do I mock the MessageDigest class or test it as well?
   // For it to be a true unit test, I should mock that class and not
   // rely on it
   // Or is it integral, and do I just test that hashes against know input
   // and what should be output?
   //
   // I think that is the way to go, and to test all of the error
   // conditions that should be handled.
   

   
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
      
      String hashUTF8 = HashGenerator.createHash(INPUT_STRING_A, "UTF-8", "MD5");
      LOGGER.debug("hashUTF8    = {}", hashUTF8);
      
      String hashUSASCII = HashGenerator.createHash(INPUT_STRING_A, "US-ASCII", "MD5");
      LOGGER.debug("hashUSASCII = {}", hashUSASCII);
      
      String hashUTF16LE = HashGenerator.createHash(INPUT_STRING_A, "UTF-16LE", "MD5");
      LOGGER.debug("hashUTF16LE = {}", hashUTF16LE);
      
   }
   
   // 1256987432 = 1001010111011000001101100101000
   // 9223372036854775807L = 0x7FFFFFFFFFFFFFFF
   
   @Test
   public void testTestGen() {
      TestDataGenerator.writeLongData(9223372036854775807L);
   }
   
}
