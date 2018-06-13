package com.ryanchapin.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.ryanchapin.util.HashGenerator.DataType;

public class ListConverter {

   public static final Map<DataType, Function<List<? extends Object>, ?>> LIST_CONVERTERS =
      getListConverters();

   public static Function<List<? extends Object>, ?> get(DataType type) {
      return LIST_CONVERTERS.get(type);
   }

   public static Map<DataType, Function<List<? extends Object>, ?>> getListConverters() {
      Map<DataType, Function<List<? extends Object>, ?>> converters = new HashMap<>();

      Function<List<? extends Object>, byte[]> byteConvertor = (list) -> {
         byte[] retVal = new byte[list.size()];
         for (int i = 0; i < list.size(); i++) {
            retVal[i] = ((Byte) list.get(i)).byteValue();
         }
         return retVal;
      };
      converters.put(DataType.BYTE_ARRAY, byteConvertor);

      Function<List<? extends Object>, char[]> charConvertor = (list) -> {
         char[] retVal = new char[list.size()];
         for (int i = 0; i < list.size(); i++) {
            retVal[i] = ((Character) list.get(i)).charValue();
         }
         return retVal;
      };
      converters.put(DataType.CHARACTER_ARRAY, charConvertor);

      Function<List<? extends Object>, short[]> shortConvertor = (list) -> {
         short[] retVal = new short[list.size()];
         for (int i = 0; i < list.size(); i++) {
            retVal[i] = ((Short) list.get(i)).shortValue();
         }
         return retVal;
      };
      converters.put(DataType.SHORT_ARRAY, shortConvertor);

      Function<List<? extends Object>, int[]> intConvertor = (list) -> {
         int[] retVal = new int[list.size()];
         for (int i = 0; i < list.size(); i++) {
            retVal[i] = ((Integer) list.get(i)).intValue();
         }
         return retVal;
      };
      converters.put(DataType.INTEGER_ARRAY, intConvertor);

      Function<List<? extends Object>, long[]> longConvertor = (list) -> {
         long[] retVal = new long[list.size()];
         for (int i = 0; i < list.size(); i++) {
            retVal[i] = ((Long) list.get(i)).longValue();
         }
         return retVal;
      };
      converters.put(DataType.LONG_ARRAY, longConvertor);

      Function<List<? extends Object>, float[]> floatConvertor = (list) -> {
         float[] retVal = new float[list.size()];
         for (int i = 0; i < list.size(); i++) {
            retVal[i] = ((Float) list.get(i)).floatValue();
         }
         return retVal;
      };
      converters.put(DataType.FLOAT_ARRAY, floatConvertor);

      Function<List<? extends Object>, double[]> doubleConvertor = (list) -> {
         double[] retVal = new double[list.size()];
         for (int i = 0; i < list.size(); i++) {
            retVal[i] = ((Double) list.get(i)).doubleValue();
         }
         return retVal;
      };
      converters.put(DataType.DOUBLE_ARRAY, doubleConvertor);

      Function<List<? extends Object>, String[]> stringConvertor = (list) -> {
         String[] retVal = new String[list.size()];
         for (int i = 0; i < list.size(); i++) {
            retVal[i] = ((String) list.get(i));
         }
         return retVal;
      };
      converters.put(DataType.STRING_ARRAY, stringConvertor);

      return converters;
   }
}
