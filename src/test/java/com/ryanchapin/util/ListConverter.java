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

      Function<List<? extends Object>, char[]> charConvertor = (list) -> {
         char[] retVal = new char[list.size()];
         for (int i = 0; i < list.size(); i++) {
            retVal[i] = ((Character) list.get(i)).charValue();
         }
         return retVal;
      };
      converters.put(DataType.CHARACTER_ARRAY, charConvertor);

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
