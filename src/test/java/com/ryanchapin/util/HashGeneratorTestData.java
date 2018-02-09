package com.ryanchapin.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ryanchapin.util.HashGenerator.HashAlgorithm;
import com.ryanchapin.util.HashGeneratorTest.HashTestData;
import com.ryanchapin.util.HashGeneratorTest.HashTestDataList;

/**
 * Auto-generated test data
 *
 * @author Ryan Chapin
 * @since  2015-03-14
 */
public class HashGeneratorTestData {

   /** Map to contain the List {@link HashTestData} instances for each data type. */
   public Map<HashGenerator.DataType,
         List<HashTestData<? extends Object>>> testDataMap;

   /**
    * Map to contain the List {@link HashTestDataList} instance for each data
    * array (or list) of data types
    */
   public Map<HashGenerator.DataType,
         List<HashTestDataList<? extends Object>>> testDataListMap;

   private List<HashTestData<? extends Object>> byteScalarList;
   private List<HashTestData<? extends Object>> characterScalarList;
   private List<HashTestData<? extends Object>> shortScalarList;
   private List<HashTestData<? extends Object>> integerScalarList;
   private List<HashTestData<? extends Object>> longScalarList;
   private List<HashTestData<? extends Object>> floatScalarList;
   private List<HashTestData<? extends Object>> doubleScalarList;
   private List<HashTestData<? extends Object>> stringScalarList;
   private List<HashTestDataList<? extends Object>> byteArrayList;
   private List<HashTestDataList<? extends Object>> characterArrayList;
   private List<HashTestDataList<? extends Object>> shortArrayList;
   private List<HashTestDataList<? extends Object>> integerArrayList;
   private List<HashTestDataList<? extends Object>> longArrayList;
   private List<HashTestDataList<? extends Object>> floatArrayList;
   private List<HashTestDataList<? extends Object>> doubleArrayList;
   private List<HashTestDataList<? extends Object>> stringArrayList;

   public HashGeneratorTestData() {
      testDataMap = new HashMap<HashGenerator.DataType,
            List<HashTestData<? extends Object>>>();

      // Each of the following lists contains a HashTestData instance with
      // the source value, it's hash, and the algorithm used to generate that
      // hash.
      byteScalarList = new ArrayList<HashTestData<? extends Object>>();
      characterScalarList = new ArrayList<HashTestData<? extends Object>>();
      shortScalarList = new ArrayList<HashTestData<? extends Object>>();
      integerScalarList = new ArrayList<HashTestData<? extends Object>>();
      longScalarList = new ArrayList<HashTestData<? extends Object>>();
      floatScalarList = new ArrayList<HashTestData<? extends Object>>();
      doubleScalarList = new ArrayList<HashTestData<? extends Object>>();
      stringScalarList = new ArrayList<HashTestData<? extends Object>>();

      testDataMap.put(HashGenerator.DataType.BYTE,      byteScalarList);
      testDataMap.put(HashGenerator.DataType.SHORT,     shortScalarList);
      testDataMap.put(HashGenerator.DataType.CHARACTER, characterScalarList);
      testDataMap.put(HashGenerator.DataType.INTEGER,   integerScalarList);
      testDataMap.put(HashGenerator.DataType.LONG,      longScalarList);
      testDataMap.put(HashGenerator.DataType.FLOAT,     floatScalarList);
      testDataMap.put(HashGenerator.DataType.DOUBLE,    doubleScalarList);
      testDataMap.put(HashGenerator.DataType.STRING,    stringScalarList);

      testDataListMap = new HashMap<HashGenerator.DataType,
            List<HashTestDataList<? extends Object>>>();

      byteArrayList = new ArrayList<HashTestDataList<? extends Object>>();
      characterArrayList = new ArrayList<HashTestDataList<? extends Object>>();
      shortArrayList = new ArrayList<HashTestDataList<? extends Object>>();
      integerArrayList = new ArrayList<HashTestDataList<? extends Object>>();
      longArrayList = new ArrayList<HashTestDataList<? extends Object>>();
      floatArrayList = new ArrayList<HashTestDataList<? extends Object>>();
      doubleArrayList = new ArrayList<HashTestDataList<? extends Object>>();
      stringArrayList = new ArrayList<HashTestDataList<? extends Object>>();

      testDataListMap.put(HashGenerator.DataType.BYTE_ARRAY, byteArrayList);
      testDataListMap.put(HashGenerator.DataType.CHARACTER_ARRAY, characterArrayList);
      testDataListMap.put(HashGenerator.DataType.SHORT_ARRAY, shortArrayList);
      testDataListMap.put(HashGenerator.DataType.INTEGER_ARRAY, integerArrayList);
      testDataListMap.put(HashGenerator.DataType.LONG_ARRAY, longArrayList);
      testDataListMap.put(HashGenerator.DataType.FLOAT_ARRAY, floatArrayList);
      testDataListMap.put(HashGenerator.DataType.DOUBLE_ARRAY, doubleArrayList);
      testDataListMap.put(HashGenerator.DataType.STRING_ARRAY, stringArrayList);
   }

   /*
    * ----------------------------------------------------------------------
    * All of the code for each of the following test data struct instances
    * are dynamically generated by the gen_hashes.sh script in the
    * src/test/resources directory.  See the README.md for full details.
    */

   private void addScalarTestData() {

   }

   private void addArrayTestData() {


HashTestDataList<? extends Object> htdByteArray0 = new HashTestDataList<Byte>(
   Arrays.asList(new Byte[] {(byte)-128, (byte)0, (byte)2, (byte)7}),
   "e028df60a85e8e467918e1e0a0e8cdd5",
   HashAlgorithm.MD5SUM);
byteArrayList.add(htdByteArray0);

HashTestDataList<? extends Object> htdByteArray1 = new HashTestDataList<Byte>(
   Arrays.asList(new Byte[] {(byte)-128, (byte)0, (byte)2, (byte)7}),
   "850e8361212d577c695f87feee4ab24c6d89d936",
   HashAlgorithm.SHA1SUM);
byteArrayList.add(htdByteArray1);

HashTestDataList<? extends Object> htdByteArray2 = new HashTestDataList<Byte>(
   Arrays.asList(new Byte[] {(byte)-128, (byte)0, (byte)2, (byte)7}),
   "c095be74975eaba646090a1233190185bd4af7b1f02a94081b0a3775baf359e5",
   HashAlgorithm.SHA256SUM);
byteArrayList.add(htdByteArray2);

HashTestDataList<? extends Object> htdByteArray3 = new HashTestDataList<Byte>(
   Arrays.asList(new Byte[] {(byte)-128, (byte)0, (byte)2, (byte)7}),
   "adeb75f0d1e0273d3c1ba1362f72866ddb74d0284d53ecf2ccdb8e106a02a59e855bd4eeaa94deaedb60ea14cebc18af",
   HashAlgorithm.SHA384SUM);
byteArrayList.add(htdByteArray3);

HashTestDataList<? extends Object> htdByteArray4 = new HashTestDataList<Byte>(
   Arrays.asList(new Byte[] {(byte)-128, (byte)0, (byte)2, (byte)7}),
   "29a5a17e2bf50e51e41f53838a0133c4b3369e5e43d084047155f06956b23790f1194a0b2c4e519d5385a455ab462b510930be52c2ce3209e995a9687aa074b3",
   HashAlgorithm.SHA512SUM);
byteArrayList.add(htdByteArray4);

HashTestDataList<? extends Object> htdByteArray5 = new HashTestDataList<Byte>(
   Arrays.asList(new Byte[] {(byte)-128, (byte)-127, (byte)-126, (byte)-125, (byte)-124, (byte)-123, (byte)-122, (byte)-121, (byte)-120, (byte)-119, (byte)-118, (byte)-117, (byte)-116, (byte)-115, (byte)-114, (byte)-113, (byte)-112, (byte)-111, (byte)-110, (byte)-109, (byte)-108, (byte)-107, (byte)-106, (byte)-105, (byte)-104, (byte)-103, (byte)-102, (byte)-101, (byte)-100, (byte)-99, (byte)-98, (byte)-97, (byte)-96, (byte)-95, (byte)-94, (byte)-93, (byte)-92, (byte)-91, (byte)-90, (byte)-89, (byte)-88, (byte)-87, (byte)-86, (byte)-85, (byte)-84, (byte)-83, (byte)-82, (byte)-81, (byte)-80, (byte)-79, (byte)-78, (byte)-77, (byte)-76, (byte)-75, (byte)-74, (byte)-73, (byte)-72, (byte)-71, (byte)-70, (byte)-69, (byte)-68, (byte)-67, (byte)-66, (byte)-65, (byte)-64, (byte)-63, (byte)-62, (byte)-61, (byte)-60, (byte)-59, (byte)-58, (byte)-57, (byte)-56, (byte)-55, (byte)-54, (byte)-53, (byte)-52, (byte)-51, (byte)-50, (byte)-49, (byte)-48, (byte)-47, (byte)-46, (byte)-45, (byte)-44, (byte)-43, (byte)-42, (byte)-41, (byte)-40, (byte)-39, (byte)-38, (byte)-37, (byte)-36, (byte)-35, (byte)-34, (byte)-33, (byte)-32, (byte)-31, (byte)-30, (byte)-29, (byte)-28, (byte)-27, (byte)-26, (byte)-25, (byte)-24, (byte)-23, (byte)-22, (byte)-21, (byte)-20, (byte)-19, (byte)-18, (byte)-17, (byte)-16, (byte)-15, (byte)-14, (byte)-13, (byte)-12, (byte)-11, (byte)-10, (byte)-9, (byte)-8, (byte)-7, (byte)-6, (byte)-5, (byte)-4, (byte)-3, (byte)-2, (byte)-1, (byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)5, (byte)6, (byte)7, (byte)8, (byte)9, (byte)10, (byte)11, (byte)12, (byte)13, (byte)14, (byte)15, (byte)16, (byte)17, (byte)18, (byte)19, (byte)20, (byte)21, (byte)22, (byte)23, (byte)24, (byte)25, (byte)26, (byte)27, (byte)28, (byte)29, (byte)30, (byte)31, (byte)32, (byte)33, (byte)34, (byte)35, (byte)36, (byte)37, (byte)38, (byte)39, (byte)40, (byte)41, (byte)42, (byte)43, (byte)44, (byte)45, (byte)46, (byte)47, (byte)48, (byte)49, (byte)50, (byte)51, (byte)52, (byte)53, (byte)54, (byte)55, (byte)56, (byte)57, (byte)58, (byte)59, (byte)60, (byte)61, (byte)62, (byte)63, (byte)64, (byte)65, (byte)66, (byte)67, (byte)68, (byte)69, (byte)70, (byte)71, (byte)72, (byte)73, (byte)74, (byte)75, (byte)76, (byte)77, (byte)78, (byte)79, (byte)80, (byte)81, (byte)82, (byte)83, (byte)84, (byte)85, (byte)86, (byte)87, (byte)88, (byte)89, (byte)90, (byte)91, (byte)92, (byte)93, (byte)94, (byte)95, (byte)96, (byte)97, (byte)98, (byte)99, (byte)100, (byte)101, (byte)102, (byte)103, (byte)104, (byte)105, (byte)106, (byte)107, (byte)108, (byte)109, (byte)110, (byte)111, (byte)112, (byte)113, (byte)114, (byte)115, (byte)116, (byte)117, (byte)118, (byte)119, (byte)120, (byte)121, (byte)122, (byte)123, (byte)124, (byte)125, (byte)126, (byte)127}),
   "03f9522e6aa992641525359b6c67cb55",
   HashAlgorithm.MD5SUM);
byteArrayList.add(htdByteArray5);

HashTestDataList<? extends Object> htdByteArray6 = new HashTestDataList<Byte>(
   Arrays.asList(new Byte[] {(byte)-128, (byte)-127, (byte)-126, (byte)-125, (byte)-124, (byte)-123, (byte)-122, (byte)-121, (byte)-120, (byte)-119, (byte)-118, (byte)-117, (byte)-116, (byte)-115, (byte)-114, (byte)-113, (byte)-112, (byte)-111, (byte)-110, (byte)-109, (byte)-108, (byte)-107, (byte)-106, (byte)-105, (byte)-104, (byte)-103, (byte)-102, (byte)-101, (byte)-100, (byte)-99, (byte)-98, (byte)-97, (byte)-96, (byte)-95, (byte)-94, (byte)-93, (byte)-92, (byte)-91, (byte)-90, (byte)-89, (byte)-88, (byte)-87, (byte)-86, (byte)-85, (byte)-84, (byte)-83, (byte)-82, (byte)-81, (byte)-80, (byte)-79, (byte)-78, (byte)-77, (byte)-76, (byte)-75, (byte)-74, (byte)-73, (byte)-72, (byte)-71, (byte)-70, (byte)-69, (byte)-68, (byte)-67, (byte)-66, (byte)-65, (byte)-64, (byte)-63, (byte)-62, (byte)-61, (byte)-60, (byte)-59, (byte)-58, (byte)-57, (byte)-56, (byte)-55, (byte)-54, (byte)-53, (byte)-52, (byte)-51, (byte)-50, (byte)-49, (byte)-48, (byte)-47, (byte)-46, (byte)-45, (byte)-44, (byte)-43, (byte)-42, (byte)-41, (byte)-40, (byte)-39, (byte)-38, (byte)-37, (byte)-36, (byte)-35, (byte)-34, (byte)-33, (byte)-32, (byte)-31, (byte)-30, (byte)-29, (byte)-28, (byte)-27, (byte)-26, (byte)-25, (byte)-24, (byte)-23, (byte)-22, (byte)-21, (byte)-20, (byte)-19, (byte)-18, (byte)-17, (byte)-16, (byte)-15, (byte)-14, (byte)-13, (byte)-12, (byte)-11, (byte)-10, (byte)-9, (byte)-8, (byte)-7, (byte)-6, (byte)-5, (byte)-4, (byte)-3, (byte)-2, (byte)-1, (byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)5, (byte)6, (byte)7, (byte)8, (byte)9, (byte)10, (byte)11, (byte)12, (byte)13, (byte)14, (byte)15, (byte)16, (byte)17, (byte)18, (byte)19, (byte)20, (byte)21, (byte)22, (byte)23, (byte)24, (byte)25, (byte)26, (byte)27, (byte)28, (byte)29, (byte)30, (byte)31, (byte)32, (byte)33, (byte)34, (byte)35, (byte)36, (byte)37, (byte)38, (byte)39, (byte)40, (byte)41, (byte)42, (byte)43, (byte)44, (byte)45, (byte)46, (byte)47, (byte)48, (byte)49, (byte)50, (byte)51, (byte)52, (byte)53, (byte)54, (byte)55, (byte)56, (byte)57, (byte)58, (byte)59, (byte)60, (byte)61, (byte)62, (byte)63, (byte)64, (byte)65, (byte)66, (byte)67, (byte)68, (byte)69, (byte)70, (byte)71, (byte)72, (byte)73, (byte)74, (byte)75, (byte)76, (byte)77, (byte)78, (byte)79, (byte)80, (byte)81, (byte)82, (byte)83, (byte)84, (byte)85, (byte)86, (byte)87, (byte)88, (byte)89, (byte)90, (byte)91, (byte)92, (byte)93, (byte)94, (byte)95, (byte)96, (byte)97, (byte)98, (byte)99, (byte)100, (byte)101, (byte)102, (byte)103, (byte)104, (byte)105, (byte)106, (byte)107, (byte)108, (byte)109, (byte)110, (byte)111, (byte)112, (byte)113, (byte)114, (byte)115, (byte)116, (byte)117, (byte)118, (byte)119, (byte)120, (byte)121, (byte)122, (byte)123, (byte)124, (byte)125, (byte)126, (byte)127}),
   "eb38c997fb698b7e65330d45cbc28435f5e96bb8",
   HashAlgorithm.SHA1SUM);
byteArrayList.add(htdByteArray6);

HashTestDataList<? extends Object> htdByteArray7 = new HashTestDataList<Byte>(
   Arrays.asList(new Byte[] {(byte)-128, (byte)-127, (byte)-126, (byte)-125, (byte)-124, (byte)-123, (byte)-122, (byte)-121, (byte)-120, (byte)-119, (byte)-118, (byte)-117, (byte)-116, (byte)-115, (byte)-114, (byte)-113, (byte)-112, (byte)-111, (byte)-110, (byte)-109, (byte)-108, (byte)-107, (byte)-106, (byte)-105, (byte)-104, (byte)-103, (byte)-102, (byte)-101, (byte)-100, (byte)-99, (byte)-98, (byte)-97, (byte)-96, (byte)-95, (byte)-94, (byte)-93, (byte)-92, (byte)-91, (byte)-90, (byte)-89, (byte)-88, (byte)-87, (byte)-86, (byte)-85, (byte)-84, (byte)-83, (byte)-82, (byte)-81, (byte)-80, (byte)-79, (byte)-78, (byte)-77, (byte)-76, (byte)-75, (byte)-74, (byte)-73, (byte)-72, (byte)-71, (byte)-70, (byte)-69, (byte)-68, (byte)-67, (byte)-66, (byte)-65, (byte)-64, (byte)-63, (byte)-62, (byte)-61, (byte)-60, (byte)-59, (byte)-58, (byte)-57, (byte)-56, (byte)-55, (byte)-54, (byte)-53, (byte)-52, (byte)-51, (byte)-50, (byte)-49, (byte)-48, (byte)-47, (byte)-46, (byte)-45, (byte)-44, (byte)-43, (byte)-42, (byte)-41, (byte)-40, (byte)-39, (byte)-38, (byte)-37, (byte)-36, (byte)-35, (byte)-34, (byte)-33, (byte)-32, (byte)-31, (byte)-30, (byte)-29, (byte)-28, (byte)-27, (byte)-26, (byte)-25, (byte)-24, (byte)-23, (byte)-22, (byte)-21, (byte)-20, (byte)-19, (byte)-18, (byte)-17, (byte)-16, (byte)-15, (byte)-14, (byte)-13, (byte)-12, (byte)-11, (byte)-10, (byte)-9, (byte)-8, (byte)-7, (byte)-6, (byte)-5, (byte)-4, (byte)-3, (byte)-2, (byte)-1, (byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)5, (byte)6, (byte)7, (byte)8, (byte)9, (byte)10, (byte)11, (byte)12, (byte)13, (byte)14, (byte)15, (byte)16, (byte)17, (byte)18, (byte)19, (byte)20, (byte)21, (byte)22, (byte)23, (byte)24, (byte)25, (byte)26, (byte)27, (byte)28, (byte)29, (byte)30, (byte)31, (byte)32, (byte)33, (byte)34, (byte)35, (byte)36, (byte)37, (byte)38, (byte)39, (byte)40, (byte)41, (byte)42, (byte)43, (byte)44, (byte)45, (byte)46, (byte)47, (byte)48, (byte)49, (byte)50, (byte)51, (byte)52, (byte)53, (byte)54, (byte)55, (byte)56, (byte)57, (byte)58, (byte)59, (byte)60, (byte)61, (byte)62, (byte)63, (byte)64, (byte)65, (byte)66, (byte)67, (byte)68, (byte)69, (byte)70, (byte)71, (byte)72, (byte)73, (byte)74, (byte)75, (byte)76, (byte)77, (byte)78, (byte)79, (byte)80, (byte)81, (byte)82, (byte)83, (byte)84, (byte)85, (byte)86, (byte)87, (byte)88, (byte)89, (byte)90, (byte)91, (byte)92, (byte)93, (byte)94, (byte)95, (byte)96, (byte)97, (byte)98, (byte)99, (byte)100, (byte)101, (byte)102, (byte)103, (byte)104, (byte)105, (byte)106, (byte)107, (byte)108, (byte)109, (byte)110, (byte)111, (byte)112, (byte)113, (byte)114, (byte)115, (byte)116, (byte)117, (byte)118, (byte)119, (byte)120, (byte)121, (byte)122, (byte)123, (byte)124, (byte)125, (byte)126, (byte)127}),
   "2bae3a9530e35152c19d73f13f6c0e22cb92f22ce8aa895796711f52b8f7f516",
   HashAlgorithm.SHA256SUM);
byteArrayList.add(htdByteArray7);

HashTestDataList<? extends Object> htdByteArray8 = new HashTestDataList<Byte>(
   Arrays.asList(new Byte[] {(byte)-128, (byte)-127, (byte)-126, (byte)-125, (byte)-124, (byte)-123, (byte)-122, (byte)-121, (byte)-120, (byte)-119, (byte)-118, (byte)-117, (byte)-116, (byte)-115, (byte)-114, (byte)-113, (byte)-112, (byte)-111, (byte)-110, (byte)-109, (byte)-108, (byte)-107, (byte)-106, (byte)-105, (byte)-104, (byte)-103, (byte)-102, (byte)-101, (byte)-100, (byte)-99, (byte)-98, (byte)-97, (byte)-96, (byte)-95, (byte)-94, (byte)-93, (byte)-92, (byte)-91, (byte)-90, (byte)-89, (byte)-88, (byte)-87, (byte)-86, (byte)-85, (byte)-84, (byte)-83, (byte)-82, (byte)-81, (byte)-80, (byte)-79, (byte)-78, (byte)-77, (byte)-76, (byte)-75, (byte)-74, (byte)-73, (byte)-72, (byte)-71, (byte)-70, (byte)-69, (byte)-68, (byte)-67, (byte)-66, (byte)-65, (byte)-64, (byte)-63, (byte)-62, (byte)-61, (byte)-60, (byte)-59, (byte)-58, (byte)-57, (byte)-56, (byte)-55, (byte)-54, (byte)-53, (byte)-52, (byte)-51, (byte)-50, (byte)-49, (byte)-48, (byte)-47, (byte)-46, (byte)-45, (byte)-44, (byte)-43, (byte)-42, (byte)-41, (byte)-40, (byte)-39, (byte)-38, (byte)-37, (byte)-36, (byte)-35, (byte)-34, (byte)-33, (byte)-32, (byte)-31, (byte)-30, (byte)-29, (byte)-28, (byte)-27, (byte)-26, (byte)-25, (byte)-24, (byte)-23, (byte)-22, (byte)-21, (byte)-20, (byte)-19, (byte)-18, (byte)-17, (byte)-16, (byte)-15, (byte)-14, (byte)-13, (byte)-12, (byte)-11, (byte)-10, (byte)-9, (byte)-8, (byte)-7, (byte)-6, (byte)-5, (byte)-4, (byte)-3, (byte)-2, (byte)-1, (byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)5, (byte)6, (byte)7, (byte)8, (byte)9, (byte)10, (byte)11, (byte)12, (byte)13, (byte)14, (byte)15, (byte)16, (byte)17, (byte)18, (byte)19, (byte)20, (byte)21, (byte)22, (byte)23, (byte)24, (byte)25, (byte)26, (byte)27, (byte)28, (byte)29, (byte)30, (byte)31, (byte)32, (byte)33, (byte)34, (byte)35, (byte)36, (byte)37, (byte)38, (byte)39, (byte)40, (byte)41, (byte)42, (byte)43, (byte)44, (byte)45, (byte)46, (byte)47, (byte)48, (byte)49, (byte)50, (byte)51, (byte)52, (byte)53, (byte)54, (byte)55, (byte)56, (byte)57, (byte)58, (byte)59, (byte)60, (byte)61, (byte)62, (byte)63, (byte)64, (byte)65, (byte)66, (byte)67, (byte)68, (byte)69, (byte)70, (byte)71, (byte)72, (byte)73, (byte)74, (byte)75, (byte)76, (byte)77, (byte)78, (byte)79, (byte)80, (byte)81, (byte)82, (byte)83, (byte)84, (byte)85, (byte)86, (byte)87, (byte)88, (byte)89, (byte)90, (byte)91, (byte)92, (byte)93, (byte)94, (byte)95, (byte)96, (byte)97, (byte)98, (byte)99, (byte)100, (byte)101, (byte)102, (byte)103, (byte)104, (byte)105, (byte)106, (byte)107, (byte)108, (byte)109, (byte)110, (byte)111, (byte)112, (byte)113, (byte)114, (byte)115, (byte)116, (byte)117, (byte)118, (byte)119, (byte)120, (byte)121, (byte)122, (byte)123, (byte)124, (byte)125, (byte)126, (byte)127}),
   "a7902aa7f28885d54c4dadbff0f721cd5532b1e56e6f7a4bb2baad0229e576da5902c1bf0cc809fa3efa6e6476e62696",
   HashAlgorithm.SHA384SUM);
byteArrayList.add(htdByteArray8);

HashTestDataList<? extends Object> htdByteArray9 = new HashTestDataList<Byte>(
   Arrays.asList(new Byte[] {(byte)-128, (byte)-127, (byte)-126, (byte)-125, (byte)-124, (byte)-123, (byte)-122, (byte)-121, (byte)-120, (byte)-119, (byte)-118, (byte)-117, (byte)-116, (byte)-115, (byte)-114, (byte)-113, (byte)-112, (byte)-111, (byte)-110, (byte)-109, (byte)-108, (byte)-107, (byte)-106, (byte)-105, (byte)-104, (byte)-103, (byte)-102, (byte)-101, (byte)-100, (byte)-99, (byte)-98, (byte)-97, (byte)-96, (byte)-95, (byte)-94, (byte)-93, (byte)-92, (byte)-91, (byte)-90, (byte)-89, (byte)-88, (byte)-87, (byte)-86, (byte)-85, (byte)-84, (byte)-83, (byte)-82, (byte)-81, (byte)-80, (byte)-79, (byte)-78, (byte)-77, (byte)-76, (byte)-75, (byte)-74, (byte)-73, (byte)-72, (byte)-71, (byte)-70, (byte)-69, (byte)-68, (byte)-67, (byte)-66, (byte)-65, (byte)-64, (byte)-63, (byte)-62, (byte)-61, (byte)-60, (byte)-59, (byte)-58, (byte)-57, (byte)-56, (byte)-55, (byte)-54, (byte)-53, (byte)-52, (byte)-51, (byte)-50, (byte)-49, (byte)-48, (byte)-47, (byte)-46, (byte)-45, (byte)-44, (byte)-43, (byte)-42, (byte)-41, (byte)-40, (byte)-39, (byte)-38, (byte)-37, (byte)-36, (byte)-35, (byte)-34, (byte)-33, (byte)-32, (byte)-31, (byte)-30, (byte)-29, (byte)-28, (byte)-27, (byte)-26, (byte)-25, (byte)-24, (byte)-23, (byte)-22, (byte)-21, (byte)-20, (byte)-19, (byte)-18, (byte)-17, (byte)-16, (byte)-15, (byte)-14, (byte)-13, (byte)-12, (byte)-11, (byte)-10, (byte)-9, (byte)-8, (byte)-7, (byte)-6, (byte)-5, (byte)-4, (byte)-3, (byte)-2, (byte)-1, (byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)5, (byte)6, (byte)7, (byte)8, (byte)9, (byte)10, (byte)11, (byte)12, (byte)13, (byte)14, (byte)15, (byte)16, (byte)17, (byte)18, (byte)19, (byte)20, (byte)21, (byte)22, (byte)23, (byte)24, (byte)25, (byte)26, (byte)27, (byte)28, (byte)29, (byte)30, (byte)31, (byte)32, (byte)33, (byte)34, (byte)35, (byte)36, (byte)37, (byte)38, (byte)39, (byte)40, (byte)41, (byte)42, (byte)43, (byte)44, (byte)45, (byte)46, (byte)47, (byte)48, (byte)49, (byte)50, (byte)51, (byte)52, (byte)53, (byte)54, (byte)55, (byte)56, (byte)57, (byte)58, (byte)59, (byte)60, (byte)61, (byte)62, (byte)63, (byte)64, (byte)65, (byte)66, (byte)67, (byte)68, (byte)69, (byte)70, (byte)71, (byte)72, (byte)73, (byte)74, (byte)75, (byte)76, (byte)77, (byte)78, (byte)79, (byte)80, (byte)81, (byte)82, (byte)83, (byte)84, (byte)85, (byte)86, (byte)87, (byte)88, (byte)89, (byte)90, (byte)91, (byte)92, (byte)93, (byte)94, (byte)95, (byte)96, (byte)97, (byte)98, (byte)99, (byte)100, (byte)101, (byte)102, (byte)103, (byte)104, (byte)105, (byte)106, (byte)107, (byte)108, (byte)109, (byte)110, (byte)111, (byte)112, (byte)113, (byte)114, (byte)115, (byte)116, (byte)117, (byte)118, (byte)119, (byte)120, (byte)121, (byte)122, (byte)123, (byte)124, (byte)125, (byte)126, (byte)127}),
   "f91a8584486a5f167ca103e390444e52fd294e10d43af7bd94402876954ae9b1d0ec65ab9aaf47a7ab7f8733a8d111c038ff78d1238e3aa32b58e9b63767f7d3",
   HashAlgorithm.SHA512SUM);
byteArrayList.add(htdByteArray9);

   }
}
