package com.ryanchapin.util;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ryanchapin.util.HashGenerator.DataType;
import com.ryanchapin.util.HashGenerator.HashAlgorithm;
import com.ryanchapin.util.HashGeneratorTest.HashTestData;
import com.ryanchapin.util.HashGeneratorTest.HashTestDataList;

public class ClientRunnable implements Runnable {

   private ClientTest parent;

   private DataType type;

   /**
    * Number of times to loop through the given data type list.
    */
   private int numIter;

   private Map<String, Integer> results;

   public ClientRunnable(
         ClientTest parent, HashGenerator.DataType type, int numIter)
   {
      this.parent    = parent;
      this.type      = type;
      this.numIter = numIter;
      this.results = new HashMap<>();
   }

   private void hashScalarValues() {
      try {
         List<HashTestData<? extends Object>> list =
               HashGeneratorTestData.testDataMap.get(type);
         String hash        = null;
         HashAlgorithm algo = null;
         Object data        = null;

         for (HashTestData<? extends Object> htd : list) {
            algo = htd.getAlgo();
            data = htd.getData();

            for (int j = 0; j < numIter; j++) {
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
                           stringData, HashGeneratorTest.DEFAULT_CHAR_ENCODING, algo);
                     break;
                  default:
               }
               // Store the hash result locally in this instance.
               addResult(type, hash);
            }
         }
      } catch (NoSuchAlgorithmException | IllegalArgumentException |
            UnsupportedEncodingException  | IllegalStateException e) {
         e.printStackTrace();
      }
   }

   private void hashArrayValues() {
      try {
         List<HashTestDataList<? extends Object>> list = HashGeneratorTestData.testDataListMap
               .get(type);
         String hash = null;
         HashAlgorithm algo = null;
         List<? extends Object> data = null;

         for (HashTestDataList<? extends Object> htdl : list) {
            algo = htdl.getAlgo();
            data = htdl.getData();

            for (int j = 0; j < numIter; j++) {
               switch (type) {
               case CHARACTER_ARRAY:
                  char[] charArray = HashGeneratorTest.convertListToArray(data,
                        new char[0]);
                  hash = HashGenerator.createHash(charArray, algo);
                  break;
               default:
               }
               // Store the hash result locally, in this instance
               addResult(type, hash);
            }
         }
      } catch (IllegalArgumentException | NoSuchAlgorithmException e) {
         e.printStackTrace();
      }
   }

   private void addResult(DataType type, String hash) {
      int currentCount = 0;
      if (results.containsKey(hash)) {
         currentCount = results.get(hash).intValue();
      }
      Integer updatedValue = new Integer(currentCount+1);
      results.put(hash, updatedValue);
   }


   @Override
   public void run() {
      switch (type) {
         // Any array types.  If additional types are added, add them here as such:
         // case FOO_ARRAY:
         // case BAZ_ARRAY:
         // etc....
         case CHARACTER_ARRAY:
            hashArrayValues();
            break;

         // Default is any other value which is currently all of the
         // scalar values
         default:
            hashScalarValues();
      }
      // After hashing all of our values return the result to the ClientTest
      // instance
      parent.putResult(type, results);
   }
}
