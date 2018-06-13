package com.ryanchapin.util;

import static com.ryanchapin.util.HashGenerator.DataType.*;
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

   private void hashArrayValues() throws Exception {
      try {
         List<HashTestDataList<? extends Object>> list =
            HashGeneratorTestData.testDataListMap.get(type);
         String hash = null;
//         HashAlgorithm algo = null;
//         List<? extends Object> data = null;

//         m.put(DataType.BYTE_ARRAY, (byte[] arr) -> HashGenerator.createHash(arr, null));

         for (HashTestDataList<? extends Object> htdl : list) {
            final HashAlgorithm algo = htdl.getAlgo();
            final List<? extends Object> data = htdl.getData();

            Map<DataType, SupplierThrows<String>> map = new HashMap<>();
            map.put(BYTE_ARRAY, () -> HashGenerator.createHash(
               (byte[]) ListConverter.get(BYTE_ARRAY).apply(data), algo));
            map.put(CHARACTER_ARRAY, () -> HashGenerator.createHash(
               (char[]) ListConverter.get(CHARACTER_ARRAY).apply(data), algo));
            map.put(SHORT_ARRAY, () -> HashGenerator.createHash(
               (short[]) ListConverter.get(SHORT_ARRAY).apply(data), algo));
            map.put(INTEGER_ARRAY, () -> HashGenerator.createHash(
               (int[]) ListConverter.get(INTEGER_ARRAY).apply(data), algo));
            map.put(LONG_ARRAY, () -> HashGenerator.createHash(
               (long[]) ListConverter.get(LONG_ARRAY).apply(data), algo));
            map.put(DataType.FLOAT_ARRAY, () -> HashGenerator.createHash(
               (float[]) ListConverter.get(FLOAT_ARRAY).apply(data), algo));
            map.put(DataType.DOUBLE_ARRAY, () -> HashGenerator.createHash(
               (double[]) ListConverter.get(DOUBLE_ARRAY).apply(data), algo));
            map.put(DataType.STRING_ARRAY, () -> HashGenerator.createHash(
               (String[]) ListConverter.get(STRING_ARRAY).apply(
                  data),
                  HashGeneratorTest.DEFAULT_CHAR_ENCODING,
                  algo));

            // FIXME:  Add the rest of the arrays
            for (int j = 0; j < numIter; j++) {

               hash = map.get(type).get();

//               switch (type) {
//               case BYTE_ARRAY:
//                  byte[] arr = (byte[]) ListConverter.get(type).apply(data);
//                  hash = HashGenerator.createHash(arr, algo);
//                  break;
//               case CHARACTER_ARRAY:
//                  char[] charArray = (char[]) ListConverter.get(type).apply(data);
//                  hash = HashGenerator.createHash(charArray, algo);
//                  break;
//               case STRING_ARRAY:
//                  String[] strArray = (String[]) ListConverter.get(type).apply(data);
//                  hash = HashGenerator.createHash(
//                     strArray, TestDataGenerator.DEFAULT_CHAR_ENCODING, algo);
//                  break;
//               default:
//               }
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
      try {
         if (type.isArray()) {
            hashArrayValues();
         } else {
            hashScalarValues();
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

//      switch (type) {
//         // Any array types.  If additional types are added, add them here as such:
//         // case FOO_ARRAY:
//         // case BAZ_ARRAY:
//         // etc....
//         case CHARACTER_ARRAY:
//            hashArrayValues();
//            break;
//
//         // Default is any other value which is currently all of the
//         // scalar values
//         default:
//            hashScalarValues();
//      }
      /*
       * After hashing all of our values return the result to the ClientTest
       * instance
       */
      parent.putResult(type, results);
   }
}
