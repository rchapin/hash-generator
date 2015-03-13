package com.ryanchapin.util.hashgenerator;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HashGeneratorTest {
   
   private static final Logger LOGGER = LoggerFactory.getLogger(HashGeneratorTest.class);
   
   @Rule
   public TestName testName = new TestName();
   
   /** Map of all of the currently supported hash algorithms */
   private static Map<String, String> HASH_ALGOS_MAP;
   
   static {
      HASH_ALGOS_MAP.put("md5",    "MD5");
      HASH_ALGOS_MAP.put("sha1",   "SHA-1");
      HASH_ALGOS_MAP.put("sha256", "SHA-256");
      HASH_ALGOS_MAP.put("sha384", "SHA-384");
      HASH_ALGOS_MAP.put("sha512", "SHA-512");
   }
   
   /** Maps to contain test data for each data type */
   public static Map<Integer, Byte>      BYTES_MAP;
   public static Map<Integer, Character> CHARS_MAP;
   public static Map<Integer, Short>     SHORTS_MAP;
   public static Map<Integer, Integer>   INTEGERS_MAP;
   public static Map<Integer, Long>      LONGS_MAP;
   public static Map<Integer, Float>     FLOATS_MAP;
   public static Map<Integer, Double>    DOUBLES_MAP;
   public static Map<Integer, String>    STRINGS_MAP;
   
   public static Object[][] testData;
   
   public static String[] testDataStrings;
   
   static {
      BYTES_MAP = new HashMap<Integer, Byte>(5);
      BYTES_MAP.put(1, Byte.MIN_VALUE);
      BYTES_MAP.put(2, (byte)-23);
      BYTES_MAP.put(3, (byte)0);
      BYTES_MAP.put(4, (byte)87);
      BYTES_MAP.put(5, Byte.MAX_VALUE);
      
      CHARS_MAP = new HashMap<Integer, Character>(5);
      CHARS_MAP.put(1, 'a');
      CHARS_MAP.put(2, 'B');
      CHARS_MAP.put(3, 'F');
      CHARS_MAP.put(4, '#');
      CHARS_MAP.put(5, '~');
      
      SHORTS_MAP = new HashMap<Integer, Short>(5);
      SHORTS_MAP.put(1, Short.MIN_VALUE);
      SHORTS_MAP.put(1, (short)-647);
      SHORTS_MAP.put(1, (short)0);
      SHORTS_MAP.put(1, (short)6487);
      SHORTS_MAP.put(1, Short.MAX_VALUE);
      
      INTEGERS_MAP = new HashMap<Integer, Integer>(5);
      INTEGERS_MAP.put(1, Integer.MIN_VALUE);
      INTEGERS_MAP.put(1, -256);
      INTEGERS_MAP.put(1, 0);
      INTEGERS_MAP.put(1, 191867248);
      INTEGERS_MAP.put(1, Integer.MAX_VALUE);
      
      LONGS_MAP = new HashMap<Integer, Long>(5);
      LONGS_MAP.put(1, Long.MAX_VALUE);
      LONGS_MAP.put(2, -36028797018963968L);
      LONGS_MAP.put(3, 0L);
      LONGS_MAP.put(4, 2305843009213693952L);
      LONGS_MAP.put(5, Long.MAX_VALUE);

      FLOATS_MAP = new HashMap<Integer, Float>(5);
      FLOATS_MAP.put(1, Float.MAX_VALUE);
      FLOATS_MAP.put(2, -234.7234621F);
      FLOATS_MAP.put(3, 0F);
      FLOATS_MAP.put(4, 232864343.23468F);
      FLOATS_MAP.put(5, Float.MAX_VALUE);
      
      DOUBLES_MAP = new HashMap<Integer, Double>(5);
      DOUBLES_MAP.put(1, Double.MIN_VALUE);
      DOUBLES_MAP.put(2, -9082741083.082348D);
      DOUBLES_MAP.put(3, 0D);
      DOUBLES_MAP.put(4, 2340823.9875672394D);
      DOUBLES_MAP.put(5, Double.MAX_VALUE);
      
      STRINGS_MAP = new HashMap<Integer, String>(5);
      STRINGS_MAP.put(1, "Here is a String that is human readable.  It is a lot easier to read than the random Strings that comprise the rest of this map, no?");
      STRINGS_MAP.put(1, "8EOTMO,9<R*.s[e3s;n.I/ipJsAkedF>i82]ezG$BzJ9/c`kQw\"07ByH#zR\"~xo3x#7&iEdQ\"Lf>C#YHqM\\G.@$J7GHlb3.2J(bT@N4fLq?pqkCz`uDsWW;,FBo#_1a&");
      STRINGS_MAP.put(1, "L`%xA9CT|Wj|HpER|~rv]wc`qI#z*i&{'14GP4Yr*IE>#8ipXhH>Z|_1@FyKawbHcaIkI0#JHkzb*&[`vDJl`[_r$H1$T_?@\"(GR@(,I^U9+A0_]w*PqD5:,Is1'@u#z");
      STRINGS_MAP.put(1, "et83qQS&T{Ask{'!3$/RnLzn<DH\\GtBmQRq?Uf~j)9]1JbRJd/.|55BY{8o0c/u&?q>5<BosYlT/sk8x#(:$u5c!Vlsw\"^_`G`7]]b`3N+q$OV..6]HSpA3srw*jq]`(");
      STRINGS_MAP.put(1, "g3&ElAY%T(JQEH|k{D~zDXoxPqA\"R<y5oE>w#IOc4%%xD'x|y9@OM;_F/gi1<_}]#7%\"Y'P8wyjARsn2+E.~7lAf{>o(Z|?EbP&rF^>>Q3wiyP,}sas1\"OSi^2W(J)t$");

      
      testDataStrings = new String[]{
         "Here is a String that is human readable.  It is a lot easier to read than the random Strings that comprise the rest of this map, no?",
         "8EOTMO,9<R*.s[e3s;n.I/ipJsAkedF>i82]ezG$BzJ9/c`kQw\"07ByH#zR\"~xo3x#7&iEdQ\"Lf>C#YHqM\\G.@$J7GHlb3.2J(bT@N4fLq?pqkCz`uDsWW;,FBo#_1a&",
         "L`%xA9CT|Wj|HpER|~rv]wc`qI#z*i&{'14GP4Yr*IE>#8ipXhH>Z|_1@FyKawbHcaIkI0#JHkzb*&[`vDJl`[_r$H1$T_?@\"(GR@(,I^U9+A0_]w*PqD5:,Is1'@u#z",
         "et83qQS&T{Ask{'!3$/RnLzn<DH\\GtBmQRq?Uf~j)9]1JbRJd/.|55BY{8o0c/u&?q>5<BosYlT/sk8x#(:$u5c!Vlsw\"^_`G`7]]b`3N+q$OV..6]HSpA3srw*jq]`(",
         "g3&ElAY%T(JQEH|k{D~zDXoxPqA\"R<y5oE>w#IOc4%%xD'x|y9@OM;_F/gi1<_}]#7%\"Y'P8wyjARsn2+E.~7lAf{>o(Z|?EbP&rF^>>Q3wiyP,}sas1\"OSi^2W(J)t$"
      };

      testData = new Object[1][];
      testData[0] = testDataStrings;
      
      Map<String, List<HashTestData<? extends Object>>> foo = new HashMap<String, List<HashTestData<? extends Object>>>();
      List<HashTestData<? extends Object>> stringList = new ArrayList<HashTestData<? extends Object>>();
      
      HashTestData<? extends Object> htdString1 = new HashTestData<String>(
            "data",
            "hash",
            HashAlgo.MD5);
      
      stringList.add(htdString1);
      foo.put("String", stringList);
      
      
      
      HashTestData<String> htdStringA = new HashTestData<String>("some data", "asljdflsdfj", HashAlgo.MD5);
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
      
      String hashUTF8 = HashGenerator.createHash(STRINGS_MAP.get(1), "UTF-8", "MD5");
      LOGGER.debug("hashUTF8    = {}", hashUTF8);
      
      String hashUSASCII = HashGenerator.createHash(STRINGS_MAP.get(1), "US-ASCII", "MD5");
      LOGGER.debug("hashUSASCII = {}", hashUSASCII);
      
      String hashUTF16LE = HashGenerator.createHash(STRINGS_MAP.get(1), "UTF-16LE", "MD5");
      LOGGER.debug("hashUTF16LE = {}", hashUTF16LE);
      
   }
   
   @Test
   public void shouldCorrectlyHashLong() {
      LOGGER.info("Running test: {}", testName.getMethodName());
      
      String long01Hash = HashGenerator.createHash(9223372036854775807L, "MD5" );
      LOGGER.debug("long01Hash  = {}", long01Hash);
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
      MD5("MD5"),
      SHA1("SHA-1"),
      SHA256("SHA-256"),
      SHA384("SHA-384"),
      SHA512("SHA-512");
      
      private String algo;
      
      public String getAlgo() {
         return algo;
      }
      
      private HashAlgo(String algo) {
         this.algo = algo;
      }
   }
}
