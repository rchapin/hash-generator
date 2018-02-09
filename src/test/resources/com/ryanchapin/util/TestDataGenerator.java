package com.ryanchapin.util;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Utility for generating binary files with which we can then use to get
 * known good hashes of our input data.  The results of which we will
 * use in our unit tests.
 *
 * @author Ryan Chapin
 * @since  2015-03-11
 */
public class TestDataGenerator {

   private final String outputDir;

   /**
    * The default char encoding we will use to generate byte arrays for
    * <code>Strings</code> test data.
    * <p>
    * IF THIS IS CHANGED, ENSURE TO CHANGE THE SAME FIELD IN THE
    * HashGeneratorTest class and then re-generate the test data.
    */
   public static final String DEFAULT_CHAR_ENCODING = "US-ASCII";

   private int outputCounter;

   private static final String FILE_BIN_SUFFIX    = ".bin";
   private static final String FILE_ASCII_SUFFIX  = ".txt";

   /**
    * String to add to the output file to indicate that the data is for a
    * single scalar variable.
    */
   private static final String SCALAR = "Scalar";

   /**
    * String to add to the output file to indicate that the data is an
    * array (List) of values.
    */
   private static final String ARRAY = "Array";

   private static final String OUTPUT_DATAID_FORMAT = "%s_%s_%d";
   private static final String ARR_STRING_PREFIX_FORMAT = "Arrays.asList(new %s[] {";

   private static Map<String, TriConsumer<DataOutputStream, BufferedWriter, Object>> TYPES = new HashMap<>();
   {
      TriConsumer<DataOutputStream,
            BufferedWriter,
            Object> byteConsumer = (out, writer, o) -> {
         try {
            Byte byteData = (byte) o;
            out.writeByte(byteData.byteValue());
            writer.write("(byte)" + byteData);
         } catch (IOException e) {
            e.printStackTrace();
         }
      };

      TriConsumer<DataOutputStream,
            BufferedWriter,
            Object> charConsumer = (out, writer, o) -> {
         try {
            Character charData = (Character) o;
            out.writeChar(charData.charValue());
            // Write out the ASCII representation in hex
            writer.write("(char)0x" + Integer.toHexString((int) charData));
         } catch (IOException e) {
            e.printStackTrace();
         }
      };

      TriConsumer<DataOutputStream,
            BufferedWriter,
            Object> shortConsumer = (out, writer, o) -> {
         try {
            Short shortData = (Short) o;
            out.writeShort(shortData.shortValue());
            writer.write("(short)" + shortData.toString());
         } catch (IOException e) {
            e.printStackTrace();
         }
      };

      TriConsumer<DataOutputStream,
            BufferedWriter,
            Object> intConsumer = (out, writer, o) -> {
         try {
            Integer intData = (Integer) o;
            out.writeInt(intData.intValue());
            writer.write(intData.toString());
         } catch (IOException e) {
            e.printStackTrace();
         }
      };

      TriConsumer<DataOutputStream,
            BufferedWriter,
            Object> longConsumer = (out, writer, o) -> {
         try {
            Long longData = (Long) o;
            out.writeLong(longData.longValue());
            BigInteger bi = new BigInteger(longData.toString());
            writer.write(bi.toString() + "L");
         } catch (IOException e) {
            e.printStackTrace();
         }
      };

      TriConsumer<DataOutputStream,
            BufferedWriter,
            Object> floatConsumer = (out, writer, o) -> {
         try {
            Float floatData = (Float) o;
            out.writeFloat(floatData.floatValue());
            BigDecimal bd = new BigDecimal(floatData.toString());
            writer.write(bd.toPlainString() + "F");
         } catch (IOException e) {
            e.printStackTrace();
         }
      };

      TriConsumer<DataOutputStream,
            BufferedWriter,
            Object> doubleConsumer = (out, writer, o) -> {
         try {
            Double doubleData = (Double) o;
            out.writeDouble(doubleData.doubleValue());
            BigDecimal bd = new BigDecimal(doubleData.doubleValue());
            writer.write(bd.toPlainString() + "D");
         } catch (IOException e) {
            e.printStackTrace();
         }
      };

      TriConsumer<DataOutputStream,
            BufferedWriter,
            Object> stringConsumer = (out, writer, o) -> {
         try {
            String stringData = (String) o;
            byte[] byteArr = stringData.getBytes(DEFAULT_CHAR_ENCODING);
            out.write(byteArr);
            writer.write("\"" + stringData.toString() + "\"");
         } catch (IOException e) {
            e.printStackTrace();
         }
      };

      TYPES.put("Byte", byteConsumer);
      TYPES.put("Character", charConsumer);
      TYPES.put("Short", shortConsumer);
      TYPES.put("Integer", intConsumer);
      TYPES.put("Long", longConsumer);
      TYPES.put("Float", floatConsumer);
      TYPES.put("Double", doubleConsumer);
      TYPES.put("String", stringConsumer);
   }

   public TestDataGenerator(String outputDir) {
      this.outputDir = outputDir;
   }

//   /**
//    * Write out binary data and a text file containing the data in ASCII
//    * representation of the data.
//    *
//    * @param data
//    *        The data to be written out
//    * @throws IOException
//    */
//   public void writeData(Object data) throws IOException {
//
//      // Get the unprefixed class name
//      String[] classNameArr = data.getClass().getName().split("[.]");
//      String className = classNameArr[((classNameArr.length)-1)];
//      String dataId = className + "_" + SCALAR + "_" + outputCounter;
//      System.out.println("dataId = " + dataId);
//
//      // Binary data
//      OutputStream out = getOutputStream(dataId + FILE_BIN_SUFFIX);
//      // ASCII data
//      BufferedWriter writer = getBufferedWriter(dataId + FILE_ASCII_SUFFIX);
//
//      if (data instanceof Byte) {
//         ((DataOutputStream)out).writeByte(((Byte)data).byteValue());
//         writer.write("(byte)" + ((Byte)data).toString());
//      } else if (data instanceof Character) {
//         ((DataOutputStream)out).writeChar(((Character)data).charValue());
//         writer.write("(char)0x" + Integer.toHexString((int)(Character)data));
//      } else if (data instanceof Short) {
//         ((DataOutputStream)out).writeShort(((Short)data).shortValue());
//         writer.write("(short)" + ((Short)data).toString());
//      } else if (data instanceof Integer) {
//         ((DataOutputStream)out).writeInt(((Integer)data).intValue());
//         writer.write(((Integer)data).toString());
//      } else if (data instanceof Long){
//         ((DataOutputStream)out).writeLong(((Long)data).longValue());
//         BigInteger bi = new BigInteger(((Long)data).toString());
//         writer.write(bi.toString() + "L");
//      } else if (data instanceof Double){
//         ((DataOutputStream)out).writeDouble(((Double)data).doubleValue());
//         BigDecimal bd = new BigDecimal(((Double)data).doubleValue());
//         writer.write(bd.toPlainString() + "D");
//      } else if (data instanceof Float){
//         ((DataOutputStream)out).writeFloat(((Float)data).floatValue());
//         BigDecimal bd = new BigDecimal(((Float)data).toString());
//         writer.write(bd.toPlainString() + "F");
//      } else if (data instanceof String){
//         byte[] byteArr = ((String)data).getBytes(DEFAULT_CHAR_ENCODING);
//         ((DataOutputStream)out).write(byteArr);
//         writer.write("\"" + data.toString() + "\"");
//      } else {
//         // NoOp as we do not know how to write out this data.
//      }
//
//      closeOutputStream(out);
//      closeBufferedWriter(writer);
//      outputCounter++;
//   }

   public void writeData(Object data) throws IOException {
      writeData(new Object[]{data}, true);
   }

   public void writeData(Object[] data) throws IOException {
      writeData(data, false);
   }

   public void writeData(
      Object[] data,
      boolean scalar)
         throws IOException
   {
      System.out.print(".");

      // Get the class name for the data we are writing out.
      String className = data[0].getClass().getSimpleName();
      String dataId = null;

      if (scalar) {
         dataId = className + "_" + SCALAR + "_" + outputCounter;
         dataId = String.format(
            OUTPUT_DATAID_FORMAT,
            className,
            SCALAR,
            outputCounter);
      } else {
         dataId = String.format(
            OUTPUT_DATAID_FORMAT,
            className,
            ARRAY,
            outputCounter);
      }

      TriConsumer<DataOutputStream, BufferedWriter, Object> triConsumer =
         TYPES.get(className);
      if (triConsumer == null) {
         return;
      }

      String binaryFileName = dataId + FILE_BIN_SUFFIX;
      String asciiFileName = dataId + FILE_ASCII_SUFFIX;
      final DataOutputStream out = getOutputStream(binaryFileName);
      final BufferedWriter writer = getBufferedWriter(asciiFileName);

      /*
       * At this point, we determine whether or not we have more than one object
       * to write out, or an actual array of objects.
       */
      if (data.length > 1) {
         int arrLen           = data.length;
         int delimLenBoundary = arrLen - 1;
         writer.write(String.format(ARR_STRING_PREFIX_FORMAT, className));
         for (int i = 0; i < arrLen; i++) {
            triConsumer.accept(out, writer, data[i]);

            if (i < delimLenBoundary) {
               writer.write(", ");
            }
         }
         writer.write("})");

      } else {
         triConsumer.accept(out, writer, data[0]);
      }

      closeOutputStream(out);
      closeBufferedWriter(writer);
      outputCounter++;
   }

   public void writeDataArray(Object[] data) throws IOException {
      // Get the class name for the data we are writing out
      String simpleName = data.getClass().getSimpleName();
      String className = simpleName.substring(0, simpleName.length() - 2);
      String dataId = className + "_" + ARRAY + "_" + outputCounter;
      dataId = String.format(
         OUTPUT_DATAID_FORMAT,
         className,
         ARRAY,
         outputCounter);
      System.out.println("dataId = " + dataId);

      String binaryFileName = dataId + FILE_BIN_SUFFIX;
      String asciiFileName = dataId + FILE_ASCII_SUFFIX;

      final DataOutputStream out = getOutputStream(binaryFileName);
      final BufferedWriter writer = getBufferedWriter(asciiFileName);

      Consumer<Object> consumer = null;

      switch (className) {

         case "Character":
            consumer = (o) -> {
               try {
                  Character charData = (Character) o;
                  // Write out the binary value of the char
                  out.writeChar(charData.charValue());

                  // Write out the ASCII representation in hex
                  writer.write("(char)0x" + Integer.toHexString((int) charData));
               } catch (IOException e) {
                  e.printStackTrace();
               }
            };
            break;

         case "Byte":
            consumer = (o) -> {
               try {
                  Byte byteData = (byte) o;
                  out.writeByte(byteData.byteValue());
                  writer.write("(byte)" + byteData);
               } catch (IOException e) {
                  e.printStackTrace();
               }
            };
            break;

         case "Short":
            consumer = (o) -> {
               try {
                  Short shortData = (short) o;
                  out.writeByte(shortData.shortValue());
                  writer.write("(short)" + shortData);
               } catch (IOException e) {
                  e.printStackTrace();
               }
            };
            break;

         default:
            System.out.printf("Unknown type %s provided, skipping it%n", className);
      }

      if (consumer != null) {
         writeOutValues(data, consumer, writer);
      }

      closeOutputStream(out);
      closeBufferedWriter(writer);

      if (consumer == null) {
         String cwd = System.getProperty("user.dir");
         Path binaryFilePath = Paths.get(cwd, binaryFileName);
         Path asciiFilePath = Paths.get(cwd, asciiFileName);
         Files.delete(binaryFilePath);
         Files.delete(asciiFilePath);
      } else {
         // Only increment the counter if we have written out data.
         outputCounter++;
      }
   }

   /**
    * The executor of the <code>Consumer</code> definition for the specific
    * data type that is to be written out.
    *
    * @param data
    *        An array of Objects to be written out.
    * @param consumer
    *        A <code>Consumer</code> to be executed for each element in the
    *        data array.
    * @param writer
    *        Reference to a BufferedWriter to write out the ASCII
    *        representation of each Object.
    * @throws IOException
    */
   private void writeOutValues(
      Object[] data,
      Consumer<Object> consumer,
      BufferedWriter writer)
         throws IOException
   {
      int arrLen           = data.length;
      int delimLenBoundary = arrLen - 1;

      String type = data.getClass().getSimpleName();
      writer.write(String.format(ARR_STRING_PREFIX_FORMAT, type));
      for (int i = 0; i < arrLen; i++) {
         consumer.accept(data[i]);

         if (i < delimLenBoundary) {
            writer.write(", ");
         }
      }
      writer.write("})");
   }

   private DataOutputStream getOutputStream(String fileName) throws IOException {
      Path outputPath = Paths.get(outputDir, fileName);
      DataOutputStream out = new DataOutputStream (
            new BufferedOutputStream(
                  new FileOutputStream(outputPath.toString())));
      return out;
   }

   private void closeOutputStream(OutputStream out) throws IOException {
      out.flush();
      out.close();
   }

   private BufferedWriter getBufferedWriter(String fileName) throws IOException {
      Path outputPath = Paths.get(outputDir, fileName);
      BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8);
      return writer;
   }

   private void closeBufferedWriter(BufferedWriter writer) throws IOException {
      writer.close();
   }

   public static void main(String[] args) throws IOException {
      String outputDir = args[0];
      System.out.println("Running TestDataGenerator, writing output to " + outputDir);

      Byte[] byteArr = new Byte[] {Byte.MIN_VALUE, (byte)-23, (byte)0,
         (byte)87, Byte.MAX_VALUE};

      Character[] charArr = new Character[] {'a', 'B', 'F', '#', '~'};

      Short[] shortArr = new Short[] {Short.MIN_VALUE, (short)-647, (short)0,
         (short)6487 , Short.MAX_VALUE};

      Integer[] intArr = new Integer[] {
         Integer.MIN_VALUE,
         -256,
         0,
         191867248,
         Integer.MAX_VALUE
      };

      Long[] longArr = new Long[] {
         Long.MIN_VALUE,
         -36028797018963968L,
         0L,
         2305843009213693952L,
         Long.MAX_VALUE
      };

      Float[] floatArr = new Float[] {
         Float.MIN_VALUE,
         -234.7234621F,
         0F,
         232864343.234027846268F,
         Float.MAX_VALUE
      };

      Double[] doubleArr = new Double[] {
         Double.MIN_VALUE,
         -9082741083.082348D,
         0D,
         2340823.9875672394D,
         Double.MAX_VALUE
      };

      String[] stringArr = new String[] {
         "Here is a String that is human readable.  It is a lot easier to read than the random Strings that are in this test data set, no?",
         "%[jG8IuFkuz:2>P8OFHs2[#n)w&KrlXzNy:c2bzg#vGuB6(e9sW$wxr3+AmS]>]AZJA5TZs)l5CYy)<qR!4WQ>#IE&f076N:joF(*lT6E1t$Tr%P<3R$:h#N<YpnQnrh",
         "J!^ktjn@^N1_f33>cJ:iBTR2nH7Q0uaSs35^O0n)%V)MKC[5RBpD_aU%A>VPfFjv8xr+o>!f2<(bqnFKxyhQ<N]fAa52pF>6Hm1G5%[h+vHfomJ)qg)GgoO_v9$#&EL2",
         "V(mzH*!7PoBIgwpft#YX_K[xvo0^Pt33WxVQZVlVu!!JZ!TJ+*h!ePpjt??MPG*mHFpEzKBy:OHBK0DX6jCq%N18sT@X!&Lv$q4E%]>204S$IH[4wXJTYB$jYyfWOG4n",
         "UcPj*cFKrY*^AboKOQ1[>3s%_?b$H0^]C_]eSVt:_$G6arXFDabp>KF[e_58#<EJ0mYt)@89$2o^e!zRgl@ewfyY1iY5zelcFXYhzStD9?*cnplpp8_l(L(A@rKB7^am"
      };

      Object[][]   testData = new Object[8][];
      testData[0] = byteArr;
      testData[1] = charArr;
      testData[2] = shortArr;
      testData[3] = intArr;
      testData[4] = longArr;
      testData[5] = floatArr;
      testData[6] = doubleArr;
      testData[7] = stringArr;

      Byte[][] byteArrayArr = new Byte[2][];
      byteArrayArr[0] = new Byte[] {(byte)-128, (byte)0, (byte)2, (byte)7};
      byteArrayArr[1] = new Byte[] {
         (byte)-128, (byte)-127, (byte)-126, (byte)-125, (byte)-124,
         (byte)-123, (byte)-122, (byte)-121, (byte)-120, (byte)-119,
         (byte)-118, (byte)-117, (byte)-116, (byte)-115, (byte)-114,
         (byte)-113, (byte)-112, (byte)-111, (byte)-110, (byte)-109,
         (byte)-108, (byte)-107, (byte)-106, (byte)-105, (byte)-104,
         (byte)-103, (byte)-102, (byte)-101, (byte)-100, (byte)-99,
         (byte)-98, (byte)-97, (byte)-96, (byte)-95, (byte)-94,
         (byte)-93, (byte)-92, (byte)-91, (byte)-90, (byte)-89,
         (byte)-88, (byte)-87, (byte)-86, (byte)-85, (byte)-84,
         (byte)-83, (byte)-82, (byte)-81, (byte)-80, (byte)-79,
         (byte)-78, (byte)-77, (byte)-76, (byte)-75, (byte)-74,
         (byte)-73, (byte)-72, (byte)-71, (byte)-70, (byte)-69,
         (byte)-68, (byte)-67, (byte)-66, (byte)-65, (byte)-64,
         (byte)-63, (byte)-62, (byte)-61, (byte)-60, (byte)-59,
         (byte)-58, (byte)-57, (byte)-56, (byte)-55, (byte)-54,
         (byte)-53, (byte)-52, (byte)-51, (byte)-50, (byte)-49,
         (byte)-48, (byte)-47, (byte)-46, (byte)-45, (byte)-44,
         (byte)-43, (byte)-42, (byte)-41, (byte)-40, (byte)-39,
         (byte)-38, (byte)-37, (byte)-36, (byte)-35, (byte)-34,
         (byte)-33, (byte)-32, (byte)-31, (byte)-30, (byte)-29,
         (byte)-28, (byte)-27, (byte)-26, (byte)-25, (byte)-24,
         (byte)-23, (byte)-22, (byte)-21, (byte)-20, (byte)-19,
         (byte)-18, (byte)-17, (byte)-16, (byte)-15, (byte)-14,
         (byte)-13, (byte)-12, (byte)-11, (byte)-10, (byte)-9,
         (byte)-8, (byte)-7, (byte)-6, (byte)-5, (byte)-4,
         (byte)-3, (byte)-2, (byte)-1, (byte)0, (byte)1,
         (byte)2, (byte)3, (byte)4, (byte)5, (byte)6,
         (byte)7, (byte)8, (byte)9, (byte)10, (byte)11,
         (byte)12, (byte)13, (byte)14, (byte)15, (byte)16,
         (byte)17, (byte)18, (byte)19, (byte)20, (byte)21,
         (byte)22, (byte)23, (byte)24, (byte)25, (byte)26,
         (byte)27, (byte)28, (byte)29, (byte)30, (byte)31,
         (byte)32, (byte)33, (byte)34, (byte)35, (byte)36,
         (byte)37, (byte)38, (byte)39, (byte)40, (byte)41,
         (byte)42, (byte)43, (byte)44, (byte)45, (byte)46,
         (byte)47, (byte)48, (byte)49, (byte)50, (byte)51,
         (byte)52, (byte)53, (byte)54, (byte)55, (byte)56,
         (byte)57, (byte)58, (byte)59, (byte)60, (byte)61,
         (byte)62, (byte)63, (byte)64, (byte)65, (byte)66,
         (byte)67, (byte)68, (byte)69, (byte)70, (byte)71,
         (byte)72, (byte)73, (byte)74, (byte)75, (byte)76,
         (byte)77, (byte)78, (byte)79, (byte)80, (byte)81,
         (byte)82, (byte)83, (byte)84, (byte)85, (byte)86,
         (byte)87, (byte)88, (byte)89, (byte)90, (byte)91,
         (byte)92, (byte)93, (byte)94, (byte)95, (byte)96,
         (byte)97, (byte)98, (byte)99, (byte)100, (byte)101,
         (byte)102, (byte)103, (byte)104, (byte)105, (byte)106,
         (byte)107, (byte)108, (byte)109, (byte)110, (byte)111,
         (byte)112, (byte)113, (byte)114, (byte)115, (byte)116,
         (byte)117, (byte)118, (byte)119, (byte)120, (byte)121,
         (byte)122, (byte)123, (byte)124, (byte)125, (byte)126,
         (byte)127
      };

      Character[][] charArrayArr = new Character[3][];
      charArrayArr[0] = new Character[] {'t','h','i','s','a','b','a','d','p','a','s','s','w','o','r','d'};
      charArrayArr[1] = new Character[] {
         (char)0x0, (char)0x1, (char)0x2, (char)0x3, (char)0x4, (char)0x5,
         (char)0x6, (char)0x7, (char)0x8, (char)0x9, (char)0xa, (char)0xb,
         (char)0xc, (char)0xd, (char)0xe, (char)0xf, (char)0x10,
         (char)0x11, (char)0x12, (char)0x13, (char)0x14, (char)0x15,
         (char)0x16, (char)0x17, (char)0x18, (char)0x19, (char)0x1a,
         (char)0x1b, (char)0x1c, (char)0x1d, (char)0x1e, (char)0x1f,
         (char)0x20, (char)0x21, (char)0x22, (char)0x23, (char)0x24,
         (char)0x25, (char)0x26, (char)0x27, (char)0x28, (char)0x29,
         (char)0x2a, (char)0x2b, (char)0x2c, (char)0x2d, (char)0x2e,
         (char)0x2f, (char)0x30, (char)0x31, (char)0x32, (char)0x33,
         (char)0x34, (char)0x35, (char)0x36, (char)0x37, (char)0x38,
         (char)0x39, (char)0x3a, (char)0x3b, (char)0x3c, (char)0x3d,
         (char)0x3e, (char)0x3f, (char)0x40, (char)0x41, (char)0x42,
         (char)0x43, (char)0x44, (char)0x45, (char)0x46, (char)0x47,
         (char)0x48, (char)0x49, (char)0x4a, (char)0x4b, (char)0x4c,
         (char)0x4d, (char)0x4e, (char)0x4f, (char)0x50, (char)0x51,
         (char)0x52, (char)0x53, (char)0x54, (char)0x55, (char)0x56,
         (char)0x57, (char)0x58, (char)0x59, (char)0x5a, (char)0x5b,
         (char)0x5c, (char)0x5d, (char)0x5e, (char)0x5f, (char)0x60,
         (char)0x61, (char)0x62, (char)0x63, (char)0x64, (char)0x65,
         (char)0x66, (char)0x67, (char)0x68, (char)0x69, (char)0x6a,
         (char)0x6b, (char)0x6c, (char)0x6d, (char)0x6e, (char)0x6f,
         (char)0x70, (char)0x71, (char)0x72, (char)0x73, (char)0x74,
         (char)0x75, (char)0x76, (char)0x77, (char)0x78, (char)0x79,
         (char)0x7a, (char)0x7b, (char)0x7c, (char)0x7d, (char)0x7e,
         (char)0x7f, (char)0x80, (char)0x81, (char)0x82, (char)0x83,
         (char)0x84, (char)0x85, (char)0x86, (char)0x87, (char)0x88,
         (char)0x89, (char)0x8a, (char)0x8b, (char)0x8c, (char)0x8d,
         (char)0x8e, (char)0x8f, (char)0x90, (char)0x91, (char)0x92,
         (char)0x93, (char)0x94, (char)0x95, (char)0x96, (char)0x97,
         (char)0x98, (char)0x99, (char)0x9a, (char)0x9b, (char)0x9c,
         (char)0x9d, (char)0x9e, (char)0x9f, (char)0xa0, (char)0xa1,
         (char)0xa2, (char)0xa3, (char)0xa4, (char)0xa5, (char)0xa6,
         (char)0xa7, (char)0xa8, (char)0xa9, (char)0xaa, (char)0xab,
         (char)0xac, (char)0xad, (char)0xae, (char)0xaf, (char)0xb0,
         (char)0xb1, (char)0xb2, (char)0xb3, (char)0xb4, (char)0xb5,
         (char)0xb6, (char)0xb7, (char)0xb8, (char)0xb9, (char)0xba,
         (char)0xbb, (char)0xbc, (char)0xbd, (char)0xbe, (char)0xbf,
         (char)0xc0, (char)0xc1, (char)0xc2, (char)0xc3, (char)0xc4,
         (char)0xc5, (char)0xc6, (char)0xc7, (char)0xc8, (char)0xc9,
         (char)0xca, (char)0xcb, (char)0xcc, (char)0xcd, (char)0xce,
         (char)0xcf, (char)0xd0, (char)0xd1, (char)0xd2, (char)0xd3,
         (char)0xd4, (char)0xd5, (char)0xd6, (char)0xd7, (char)0xd8,
         (char)0xd9, (char)0xda, (char)0xdb, (char)0xdc, (char)0xdd,
         (char)0xde, (char)0xdf, (char)0xe0, (char)0xe1, (char)0xe2,
         (char)0xe3, (char)0xe4, (char)0xe5, (char)0xe6, (char)0xe7,
         (char)0xe8, (char)0xe9, (char)0xea, (char)0xeb, (char)0xec,
         (char)0xed, (char)0xee, (char)0xef, (char)0xf0, (char)0xf1,
         (char)0xf2, (char)0xf3, (char)0xf4, (char)0xf5, (char)0xf6,
         (char)0xf7, (char)0xf8, (char)0xf9, (char)0xfa, (char)0xfb,
         (char)0xfc, (char)0xfd, (char)0xfe, (char)0xff
      };
      charArrayArr[2] = new Character[] {
         (char)0xffff, (char)0xff7f, (char)0xfeff, (char)0xfe7f,
         (char)0xfdff, (char)0xfd7f, (char)0xfcff, (char)0xfc7f,
         (char)0xfbff, (char)0xfb7f, (char)0xfaff, (char)0xfa7f,
         (char)0xf9ff, (char)0xf97f, (char)0xf8ff, (char)0xf87f,
         (char)0xf7ff, (char)0xf77f, (char)0xf6ff, (char)0xf67f,
         (char)0xf5ff, (char)0xf57f, (char)0xf4ff, (char)0xf47f,
         (char)0xf3ff, (char)0xf37f, (char)0xf2ff, (char)0xf27f,
         (char)0xf1ff, (char)0xf17f, (char)0xf0ff, (char)0xf07f,
         (char)0xefff, (char)0xef7f, (char)0xeeff, (char)0xee7f,
         (char)0xedff, (char)0xed7f, (char)0xecff, (char)0xec7f,
         (char)0xebff, (char)0xeb7f, (char)0xeaff, (char)0xea7f,
         (char)0xe9ff, (char)0xe97f, (char)0xe8ff, (char)0xe87f,
         (char)0xe7ff, (char)0xe77f, (char)0xe6ff, (char)0xe67f,
         (char)0xe5ff, (char)0xe57f, (char)0xe4ff, (char)0xe47f,
         (char)0xe3ff, (char)0xe37f, (char)0xe2ff, (char)0xe27f,
         (char)0xe1ff, (char)0xe17f, (char)0xe0ff, (char)0xe07f,
         (char)0xdfff, (char)0xdf7f, (char)0xdeff, (char)0xde7f,
         (char)0xddff, (char)0xdd7f, (char)0xdcff, (char)0xdc7f,
         (char)0xdbff, (char)0xdb7f, (char)0xdaff, (char)0xda7f,
         (char)0xd9ff, (char)0xd97f, (char)0xd8ff, (char)0xd87f,
         (char)0xd7ff, (char)0xd77f, (char)0xd6ff, (char)0xd67f,
         (char)0xd5ff, (char)0xd57f, (char)0xd4ff, (char)0xd47f,
         (char)0xd3ff, (char)0xd37f, (char)0xd2ff, (char)0xd27f,
         (char)0xd1ff, (char)0xd17f, (char)0xd0ff, (char)0xd07f,
         (char)0xcfff, (char)0xcf7f, (char)0xceff, (char)0xce7f,
         (char)0xcdff, (char)0xcd7f, (char)0xccff, (char)0xcc7f,
         (char)0xcbff, (char)0xcb7f, (char)0xcaff, (char)0xca7f,
         (char)0xc9ff, (char)0xc97f, (char)0xc8ff, (char)0xc87f,
         (char)0xc7ff, (char)0xc77f, (char)0xc6ff, (char)0xc67f,
         (char)0xc5ff, (char)0xc57f, (char)0xc4ff, (char)0xc47f,
         (char)0xc3ff, (char)0xc37f, (char)0xc2ff, (char)0xc27f,
         (char)0xc1ff, (char)0xc17f, (char)0xc0ff, (char)0xc07f,
         (char)0xbfff, (char)0xbf7f, (char)0xbeff, (char)0xbe7f,
         (char)0xbdff, (char)0xbd7f, (char)0xbcff, (char)0xbc7f,
         (char)0xbbff, (char)0xbb7f, (char)0xbaff, (char)0xba7f,
         (char)0xb9ff, (char)0xb97f, (char)0xb8ff, (char)0xb87f,
         (char)0xb7ff, (char)0xb77f, (char)0xb6ff, (char)0xb67f,
         (char)0xb5ff, (char)0xb57f, (char)0xb4ff, (char)0xb47f,
         (char)0xb3ff, (char)0xb37f, (char)0xb2ff, (char)0xb27f,
         (char)0xb1ff, (char)0xb17f, (char)0xb0ff, (char)0xb07f,
         (char)0xafff, (char)0xaf7f, (char)0xaeff, (char)0xae7f,
         (char)0xadff, (char)0xad7f, (char)0xacff, (char)0xac7f,
         (char)0xabff, (char)0xab7f, (char)0xaaff, (char)0xaa7f,
         (char)0xa9ff, (char)0xa97f, (char)0xa8ff, (char)0xa87f,
         (char)0xa7ff, (char)0xa77f, (char)0xa6ff, (char)0xa67f,
         (char)0xa5ff, (char)0xa57f, (char)0xa4ff, (char)0xa47f,
         (char)0xa3ff, (char)0xa37f, (char)0xa2ff, (char)0xa27f,
         (char)0xa1ff, (char)0xa17f, (char)0xa0ff, (char)0xa07f,
         (char)0x9fff, (char)0x9f7f, (char)0x9eff, (char)0x9e7f,
         (char)0x9dff, (char)0x9d7f, (char)0x9cff, (char)0x9c7f,
         (char)0x9bff, (char)0x9b7f, (char)0x9aff, (char)0x9a7f,
         (char)0x99ff, (char)0x997f, (char)0x98ff, (char)0x987f,
         (char)0x97ff, (char)0x977f, (char)0x96ff, (char)0x967f,
         (char)0x95ff, (char)0x957f, (char)0x94ff, (char)0x947f,
         (char)0x93ff, (char)0x937f, (char)0x92ff, (char)0x927f,
         (char)0x91ff, (char)0x917f, (char)0x90ff, (char)0x907f,
         (char)0x8fff, (char)0x8f7f, (char)0x8eff, (char)0x8e7f,
         (char)0x8dff, (char)0x8d7f, (char)0x8cff, (char)0x8c7f,
         (char)0x8bff, (char)0x8b7f, (char)0x8aff, (char)0x8a7f,
         (char)0x89ff, (char)0x897f, (char)0x88ff, (char)0x887f,
         (char)0x87ff, (char)0x877f, (char)0x86ff, (char)0x867f,
         (char)0x85ff, (char)0x857f, (char)0x84ff, (char)0x847f,
         (char)0x83ff, (char)0x837f, (char)0x82ff, (char)0x827f,
         (char)0x81ff, (char)0x817f, (char)0x80ff, (char)0x807f,
         (char)0x7fff, (char)0x7f7f, (char)0x7eff, (char)0x7e7f,
         (char)0x7dff, (char)0x7d7f, (char)0x7cff, (char)0x7c7f,
         (char)0x7bff, (char)0x7b7f, (char)0x7aff, (char)0x7a7f,
         (char)0x79ff, (char)0x797f, (char)0x78ff, (char)0x787f,
         (char)0x77ff, (char)0x777f, (char)0x76ff, (char)0x767f,
         (char)0x75ff, (char)0x757f, (char)0x74ff, (char)0x747f,
         (char)0x73ff, (char)0x737f, (char)0x72ff, (char)0x727f,
         (char)0x71ff, (char)0x717f, (char)0x70ff, (char)0x707f,
         (char)0x6fff, (char)0x6f7f, (char)0x6eff, (char)0x6e7f,
         (char)0x6dff, (char)0x6d7f, (char)0x6cff, (char)0x6c7f,
         (char)0x6bff, (char)0x6b7f, (char)0x6aff, (char)0x6a7f,
         (char)0x69ff, (char)0x697f, (char)0x68ff, (char)0x687f,
         (char)0x67ff, (char)0x677f, (char)0x66ff, (char)0x667f,
         (char)0x65ff, (char)0x657f, (char)0x64ff, (char)0x647f,
         (char)0x63ff, (char)0x637f, (char)0x62ff, (char)0x627f,
         (char)0x61ff, (char)0x617f, (char)0x60ff, (char)0x607f,
         (char)0x5fff, (char)0x5f7f, (char)0x5eff, (char)0x5e7f,
         (char)0x5dff, (char)0x5d7f, (char)0x5cff, (char)0x5c7f,
         (char)0x5bff, (char)0x5b7f, (char)0x5aff, (char)0x5a7f,
         (char)0x59ff, (char)0x597f, (char)0x58ff, (char)0x587f,
         (char)0x57ff, (char)0x577f, (char)0x56ff, (char)0x567f,
         (char)0x55ff, (char)0x557f, (char)0x54ff, (char)0x547f,
         (char)0x53ff, (char)0x537f, (char)0x52ff, (char)0x527f,
         (char)0x51ff, (char)0x517f, (char)0x50ff, (char)0x507f,
         (char)0x4fff, (char)0x4f7f, (char)0x4eff, (char)0x4e7f,
         (char)0x4dff, (char)0x4d7f, (char)0x4cff, (char)0x4c7f,
         (char)0x4bff, (char)0x4b7f, (char)0x4aff, (char)0x4a7f,
         (char)0x49ff, (char)0x497f, (char)0x48ff, (char)0x487f,
         (char)0x47ff, (char)0x477f, (char)0x46ff, (char)0x467f,
         (char)0x45ff, (char)0x457f, (char)0x44ff, (char)0x447f,
         (char)0x43ff, (char)0x437f, (char)0x42ff, (char)0x427f,
         (char)0x41ff, (char)0x417f, (char)0x40ff, (char)0x407f,
         (char)0x3fff, (char)0x3f7f, (char)0x3eff, (char)0x3e7f,
         (char)0x3dff, (char)0x3d7f, (char)0x3cff, (char)0x3c7f,
         (char)0x3bff, (char)0x3b7f, (char)0x3aff, (char)0x3a7f,
         (char)0x39ff, (char)0x397f, (char)0x38ff, (char)0x387f,
         (char)0x37ff, (char)0x377f, (char)0x36ff, (char)0x367f,
         (char)0x35ff, (char)0x357f, (char)0x34ff, (char)0x347f,
         (char)0x33ff, (char)0x337f, (char)0x32ff, (char)0x327f,
         (char)0x31ff, (char)0x317f, (char)0x30ff, (char)0x307f,
         (char)0x2fff, (char)0x2f7f, (char)0x2eff, (char)0x2e7f,
         (char)0x2dff, (char)0x2d7f, (char)0x2cff, (char)0x2c7f,
         (char)0x2bff, (char)0x2b7f, (char)0x2aff, (char)0x2a7f,
         (char)0x29ff, (char)0x297f, (char)0x28ff, (char)0x287f,
         (char)0x27ff, (char)0x277f, (char)0x26ff, (char)0x267f,
         (char)0x25ff, (char)0x257f, (char)0x24ff, (char)0x247f,
         (char)0x23ff, (char)0x237f, (char)0x22ff, (char)0x227f,
         (char)0x21ff, (char)0x217f, (char)0x20ff, (char)0x207f,
         (char)0x1fff, (char)0x1f7f, (char)0x1eff, (char)0x1e7f,
         (char)0x1dff, (char)0x1d7f, (char)0x1cff, (char)0x1c7f,
         (char)0x1bff, (char)0x1b7f, (char)0x1aff, (char)0x1a7f,
         (char)0x19ff, (char)0x197f, (char)0x18ff, (char)0x187f,
         (char)0x17ff, (char)0x177f, (char)0x16ff, (char)0x167f,
         (char)0x15ff, (char)0x157f, (char)0x14ff, (char)0x147f,
         (char)0x13ff, (char)0x137f, (char)0x12ff, (char)0x127f,
         (char)0x11ff, (char)0x117f, (char)0x10ff, (char)0x107f,
         (char)0xfff, (char)0xf7f, (char)0xeff, (char)0xe7f, (char)0xdff,
         (char)0xd7f, (char)0xcff, (char)0xc7f, (char)0xbff, (char)0xb7f,
         (char)0xaff, (char)0xa7f, (char)0x9ff, (char)0x97f, (char)0x8ff,
         (char)0x87f, (char)0x7ff, (char)0x77f, (char)0x6ff, (char)0x67f,
         (char)0x5ff, (char)0x57f, (char)0x4ff, (char)0x47f, (char)0x3ff,
         (char)0x37f, (char)0x2ff, (char)0x27f, (char)0x1ff, (char)0x17f,
         (char)0xff, (char)0x7f
      };

      Short[][] shortArrayArr = new Short[3][];
      shortArrayArr[0] = new Short[] {Short.MIN_VALUE, (short)0, (short)647, Short.MAX_VALUE};
      shortArrayArr[1] = new Short[] {
         (short)-32768, (short)-31909, (short)-31340, (short)-30757, (short)-30175,
         (short)-29542, (short)-28616, (short)-27969, (short)-27293, (short)-26547,
         (short)-25963, (short)-25266, (short)-25006, (short)-24232, (short)-23888,
         (short)-23359, (short)-22581, (short)-21839, (short)-21232, (short)-20407,
         (short)-20154, (short)-19853, (short)-19515, (short)-18678, (short)-18218,
         (short)-17262, (short)-16726, (short)-16020, (short)-15238, (short)-14621,
         (short)-13845, (short)-13479, (short)-12695, (short)-12186, (short)-11190,
         (short)-10383, (short)-9537, (short)-8930, (short)-8016, (short)-7341,
         (short)-6874, (short)-5919, (short)-5599, (short)-4755, (short)-4223,
         (short)-3849, (short)-2892, (short)-2542, (short)-2137, (short)-1374,
         (short)-455, (short)1, (short)895, (short)1158, (short)2096,
         (short)2788, (short)3763, (short)4652, (short)5627, (short)6527,
         (short)6969, (short)7522, (short)8476, (short)8954, (short)9903,
         (short)10201, (short)10871, (short)11215, (short)11570, (short)11950,
         (short)12851, (short)13248, (short)14130, (short)15074, (short)15781,
         (short)16750, (short)17294, (short)17837, (short)18813, (short)19309,
         (short)19932, (short)20657, (short)21551, (short)22082, (short)22549,
         (short)22920, (short)23705, (short)24017, (short)24426, (short)24832,
         (short)25351, (short)25910, (short)26837, (short)27726, (short)28649,
         (short)29276, (short)30215, (short)30910, (short)31353, (short)32014,
         (short)32548
      };
      shortArrayArr[2] = new Short[] {
         (short)-32768, (short)-32225, (short)-31537, (short)-31003, (short)-30472,
         (short)-29951, (short)-29643, (short)-28786, (short)-27864, (short)-27339,
         (short)-26469, (short)-26148, (short)-25673, (short)-25063, (short)-24334,
         (short)-23530, (short)-22606, (short)-22040, (short)-21287, (short)-20615,
         (short)-19718, (short)-18966, (short)-18348, (short)-17547, (short)-16885,
         (short)-16205, (short)-15927, (short)-15542, (short)-14920, (short)-14506,
         (short)-13711, (short)-13099, (short)-12340, (short)-11615, (short)-11271,
         (short)-10784, (short)-9841, (short)-9105, (short)-8805, (short)-8444,
         (short)-7623, (short)-6860, (short)-5876, (short)-5256, (short)-4895,
         (short)-4595, (short)-3673, (short)-3247, (short)-2526, (short)-1753,
         (short)-1072, (short)-159, (short)657, (short)1615, (short)2416,
         (short)3035, (short)3678, (short)4050, (short)4826, (short)5150,
         (short)6139, (short)6669, (short)7205, (short)7483, (short)8349,
         (short)9258, (short)9806, (short)10211, (short)10815, (short)11157,
         (short)11865, (short)12481, (short)13305, (short)13862, (short)14509,
         (short)14905, (short)15559, (short)16548, (short)17447, (short)18219,
         (short)18736, (short)19542, (short)20539, (short)20986, (short)21900,
         (short)22814, (short)23354, (short)24293, (short)24798, (short)25419,
         (short)25792, (short)26483, (short)27096, (short)27554, (short)28279,
         (short)28581, (short)29270, (short)30200, (short)30854, (short)31185,
         (short)31994, (short)32387
      };

      Integer[][] intArrayArr = new Integer[3][];
      intArrayArr[0] = new Integer[] {Integer.MIN_VALUE, 647, Integer.MAX_VALUE};
      intArrayArr[1] = new Integer[] {
         -2147483648, -2105687187, -2103613409, -2065420829, -2021500369,
         -2005179617, -1982698224, -1941858082, -1898564405, -1877685302,
         -1855387713, -1842631570, -1803640941, -1773036636, -1723532801,
         -1714123588, -1681402313, -1655375242, -1621670530, -1578362218,
         -1537316326, -1499774867, -1453688243, -1453357361, -1448673306,
         -1413074611, -1371562123, -1327506656, -1311405520, -1269711662,
         -1239425990, -1205115221, -1198505365, -1161129535, -1116047155,
         -1104707705, -1081692320, -1048254190, -1010938177, -1006896802,
         -995366807, -993515857, -992835529, -960395844, -936846437,
         -901186410, -854048386, -804452099, -792248383, -779557923,
         -761219540, -723509642, -688629197, -647290281, -627752741,
         -586455826, -569628596, -526795515, -522634394, -521236359,
         -520145902, -517952572, -514614224, -512598308, -483631487,
         -454600593, -437031478, -404797447, -360020019, -311639291,
         -263486757, -258317429, -230771336, -197082198, -196383720,
         -195968824, -158103459, -132204222, -82739792, -46898824,
         -35058123, 509294, 42300143, 56141789, 68131249,
         112386972, 122451904, 138193602, 148525768, 170875832,
         183011598, 183224921, 202948837, 224726571, 246529024,
         250756351, 257211187, 285185699, 293477802, 329634348,
         371303639, 412402947, 452307886, 499224665, 534160743,
         546506047, 575309560, 576831002, 624526959, 664672973,
         677774971, 716922828, 739203137, 752544699, 800047501,
         831537994, 842663943, 891000034, 894680652, 895983240,
         919387989, 922956920, 941963624, 944297522, 979310166,
         1000824161, 1010136364, 1028422358, 1065511388, 1071138893,
         1082958108, 1131568637, 1167837586, 1203951888, 1233533626,
         1262001287, 1297126068, 1318333672, 1319376266, 1321794915,
         1370559790, 1414305500, 1452821555, 1477295099, 1491149192,
         1510645238, 1548490324, 1590903635, 1591078876, 1621714150,
         1648386154, 1679781872, 1720960759, 1765755620, 1770500594,
         1790195407, 1812731534, 1816019958, 1856001352, 1896064255,
         1941028832, 1955078351, 1960982594, 1993102008, 2037916451,
         2061529175, 2096009564, 2120407665
      };
      intArrayArr[2] = new Integer[] {
         -2147483648, -2102436830, -2072812283, -2044049138, -2037304707,
         -2029986308, -2008826397, -1989650440, -1983300296, -1939697661,
         -1891880370, -1872828753, -1869058335, -1851065487, -1811875791,
         -1795014257, -1780053001, -1775053364, -1734609517, -1704898388,
         -1685738948, -1678199423, -1658154795, -1611980447, -1572356048,
         -1524693639, -1520822638, -1495392250, -1486974808, -1446652383,
         -1444070407, -1404863350, -1361108929, -1322488957, -1314724165,
         -1269135357, -1222033311, -1179631609, -1165707485, -1120262474,
         -1095897348, -1082927837, -1056203309, -1015294337, -995547860,
         -959577391, -940593485, -934942628, -921713953, -893950260,
         -893724988, -867814819, -845225625, -797980040, -778648567,
         -766525435, -762071971, -746288929, -741127769, -712265154,
         -691397221, -665691577, -624042257, -577997082, -570911028,
         -538641912, -500744431, -482455237, -460525551, -445467065,
         -402754935, -368143935, -335567879, -315157079, -301779661,
         -272056541, -229507172, -216549481, -201662084, -174532865,
         -132126772, -107482955, -69563562, -30483095, 6141329,
         22358327, 30231738, 44500266, 68482940, 86136294,
         120100306, 128989413, 159615735, 176749422, 200206132,
         226622557, 229798611, 279700713, 279924001, 315794183,
         345140066, 350457388, 380647107, 426250053, 452853771,
         486109584, 516558675, 562692513, 583751478, 586833594,
         623987183, 645114283, 651555588, 681781998, 723051487,
         751382194, 770356643, 786501858, 818144778, 849969558,
         889027143, 915590823, 929764998, 958058236, 969538122,
         969645759, 976578079, 1001377364, 1030381950, 1071041812,
         1089155026, 1137375668, 1158527639, 1161913462, 1181992011,
         1214510959, 1244287459, 1274649154, 1315742807, 1348018634,
         1360471497, 1388075176, 1394208457, 1441657003, 1479568924,
         1487569388, 1490392872, 1533845472, 1545370993, 1550004393,
         1598076706, 1633446510, 1681123696, 1707386900, 1728357820,
         1765770836, 1785641682, 1793745752, 1795593760, 1836617931,
         1869347745, 1903939543, 1923140279, 1969335406, 2013395432,
         2014284858, 2043569320, 2084535666, 2110573763, 2125789169
      };

      Long[][] longArrayArr = new Long[3][];
      longArrayArr[0] = new Long[] {Long.MIN_VALUE, 647L, 700L, Long.MAX_VALUE};
      longArrayArr[1] = new Long[] {
         -9194772947804966828L, -9118239784511522572L, -9078575301706132188L,
         -9074756669335204689L, -8942072853338580929L, -8715415131945125249L,
         -8560649462304305409L, -8323782134191917569L, -8081402182679189089L,
         -7898048906574462241L, -7758558411095015585L, -7723540547198751257L,
         -7551323412694297305L, -7340490276328675257L, -7118532882791614713L,
         -7077688001789537129L, -7025622079129048201L, -6811647962434367209L,
         -6640855174899962857L, -6576105869257358433L, -6436084257666189297L,
         -6190164457355593009L, -5982283917838547601L, -5814381859908332497L,
         -5760314092445389393L, -5746303388421235681L, -5704242713479106641L,
         -5640128298873901105L, -5433827823175862513L, -5349600904951029505L,
         -5132198695685316321L, -5097609808435402165L, -4994648677938009909L,
         -4887456730738675253L, -4657788050823068341L, -4451299110374178581L,
         -4348065051647449845L, -4221736076224897029L, -4173779994424338109L,
         -3974225571214130589L, -3969259201639682033L, -3798525715047837841L,
         -3717564431433085809L, -3655231200952753209L, -3509820681551536441L,
         -3417342700494107401L, -3209408380452797705L, -3112410459150323193L,
         -2994135223972007113L, -2987738693112141545L, -2963408020098173017L,
         -2862739264949230073L, -2691441060581109401L, -2470032780897820729L,
         -2288773245615761369L, -2049600811736269369L, -1897627480193967449L,
         -1683497295935673081L, -1558976360600935369L, -1336748634112857001L,
         -1113521246828712457L, -876871106476584777L, -733032161891208809L,
         -585365632953512041L, -573120684258587037L, -485738461917967757L,
         -264658908023330573L, -162938773564943821L, -60402139052083069L,
         63753694626050339L, 226043992129297315L, 294921112162165435L,
         408405322763512219L, 460256082962891635L, 667320721270346547L,
         852420763242062483L, 1096147410707757875L, 1180376777997340307L,
         1388893658194488851L, 1494917684679094035L, 1627791800389107859L,
         1675678588484063123L, 1742232370446300787L, 1744632820978205341L,
         1914784586060156349L, 1994973282222065117L, 2135369368127390813L,
         2338113733474932125L, 2582546217353121245L, 2636991263884396813L,
         2738179200802336349L, 2747822802263405223L, 2844789791771615927L,
         3062666278847084375L, 3172700062384572391L, 3402692428777504807L,
         3592403786852029383L, 3616321929909709403L, 3706493108582611163L,
         3955264135091092219L, 4025999851702267347L, 4031823099811214816L,
         4207047227601169824L, 4446704844279822784L, 4542978627933831632L,
         4599105101967694224L, 4785689918040911984L, 4951665143877042896L,
         5040206183843666688L, 5143010158826124016L, 5342952099044541584L,
         5354704557111337086L, 5379214210273467734L, 5582888017391040086L,
         5754449741792602038L, 5999451971286411478L, 6205832760776356022L,
         6437799831974928118L, 6630932127332366518L, 6712388617636580950L,
         6743428088251220294L, 6855994151773045206L, 7099798410062108758L,
         7280230380681042198L, 7488794607994657878L, 7728696329701746518L,
         7777445843596120870L, 7778689677772377540L, 7880582361905687652L,
         7893760556783420900L, 7984627240681047316L, 8101257983902039428L,
         8321773488294866180L, 8424470896487678212L, 8634406054398089316L,
         8779662331078317764L, 8899996885470145844L, 9043588577359074404L,
         9087649125780173260L, 9139656703666100892L
      };
      longArrayArr[2] = new Long[] {
         -9178402929932208032L, -8962135458163632768L, -8894248495453852288L,
         -8863149866445646096L, -8713952262254021712L, -8558715351911827600L,
         -8405002191770653872L, -8383302992041978868L, -8344426797105096964L,
         -8222655883551053188L, -8207776506988653640L, -8063153746446393928L,
         -8023774472733669808L, -7999270766668261992L, -7811229038094451816L,
         -7777565526133626116L, -7623296755584517764L, -7559391638066660732L,
         -7348121068320252220L, -7273211886973798332L, -7231968076428852196L,
         -7048192896372764292L, -6921423999841363876L, -6900667337610859092L,
         -6750980714422359732L, -6725834998379836520L, -6485618937257989320L,
         -6378490935960509624L, -6310807674013431112L, -6125761248357447784L,
         -6049923960594566856L, -5913327334188454536L, -5823026588988523112L,
         -5602985502347635944L, -5396021037454082024L, -5231208920335323080L,
         -5122942790986383416L, -5003657023528128312L, -4881302883204715912L,
         -4725489095467978376L, -4642470949379875816L, -4448614990432385416L,
         -4228499394044821992L, -3999950783925278696L, -3841063012195595720L,
         -3643580258825230248L, -3433555989064351432L, -3263648111966782920L,
         -3171987125167536568L, -3141357120799894064L, -3050602703153671104L,
         -2825233593125361824L, -2673248780152491136L, -2586714283520251328L,
         -2582054785191638464L, -2520429240506868600L, -2519432963355402894L,
         -2356013013992431438L, -2235354074667485550L, -2029949678676707118L,
         -1817766001647314990L, -1674433457782166814L, -1647626914076493686L,
         -1418609091200980694L, -1257778953957812502L, -1169597703396767750L,
         -1002629754026433766L, -816323401187605574L, -690132562753563142L,
         -680532326314348536L, -534309490728914072L, -435535406135408552L,
         -229217523437659432L, -137784648909049000L, 89347067686841528L,
         196724619526681752L, 421644626433426392L, 504112369529210248L,
         590979677105719864L, 612846450748466584L, 619925320195082385L,
         794211639212605937L, 900368076504623537L, 1048404525864947569L,
         1242443219890490929L, 1441902841216877489L, 1601913393979993393L,
         1820891858810286449L, 1987534266960113489L, 2145641571975287633L,
         2181982236140150033L, 2411165900454110513L, 2544970368613241105L,
         2723727575008635505L, 2729734071442143289L, 2975604611086816473L,
         3031293300625605761L, 3055809608667390213L, 3241949107433573861L,
         3317921336640004053L, 3327669702754643715L, 3502455349186317699L,
         3683160232331563875L, 3696950325326558097L, 3863186317030408305L,
         3967557721576362513L, 4021787454322472289L, 4055959661120691201L,
         4247660315301425985L, 4368023166144454945L, 4614194910805857793L,
         4829894232228368609L, 4981757378194467649L, 5025321903743207289L,
         5081358598643995401L, 5228866275627928265L, 5325707803600523465L,
         5412292987104739369L, 5487115239095813465L, 5558664174814994705L,
         5773907697867296721L, 5805307389876413665L, 5985228500174072737L,
         6046009991665735793L, 6052906900069689581L, 6260219099675315821L,
         6409727018639889229L, 6518231984234169485L, 6598436468813148701L,
         6695945671923402157L, 6839107227320983885L, 6967397937143573053L,
         7170434802652104445L, 7296735069569437053L, 7497772806920452509L,
         7519998267167612089L, 7607711893859551785L, 7696663348142026889L,
         7742469336651344833L, 7915942504648399905L, 8031914408376509201L,
         8267136683832192881L, 8349686859425986417L, 8558908235621130161L,
         8585538938804152757L, 8590110259611638303L, 8673513104207331551L,
         8823926184709159743L, 8825355793465438957L, 8865604146064668093L,
         8964944668614858317L, 9103217975552892829L, 9216969554673426829L
      };

      Float[][] floatArrayArr = new Float[3][];
      floatArrayArr[0] = new Float[] {
         (-1 * Float.MAX_VALUE), 3.14159265358979323846264338327950288F,
         Float.MIN_NORMAL, Float.MIN_VALUE, Float.MAX_VALUE
      };
      floatArrayArr[1] = new Float[] {
         -3.3845302E38F, -3.3354376E38F, -3.2838053E38F, -3.2761286E38F,
         -3.2672177E38F, -3.2344486E38F, -3.228808E38F, -3.1931816E38F,
         -3.189103E38F, -3.1722003E38F, -3.153358E38F, -3.112708E38F,
         -3.0802005E38F, -3.0624956E38F, -3.0548008E38F, -3.0194358E38F,
         -2.9739176E38F, -2.9448416E38F, -2.9169837E38F, -2.9054367E38F,
         -2.8668973E38F, -2.8431405E38F, -2.7971998E38F, -2.76973E38F,
         -2.7176322E38F, -2.674996E38F, -2.6293586E38F, -2.5831354E38F,
         -2.5445607E38F, -2.5216858E38F, -2.4676957E38F, -2.431365E38F,
         -2.407044E38F, -2.4058025E38F, -2.3740285E38F, -2.3247087E38F,
         -2.272692E38F, -2.2437505E38F, -2.2201405E38F, -2.2027086E38F,
         -2.1627916E38F, -2.152178E38F, -2.1242155E38F, -2.115662E38F,
         -2.0859876E38F, -2.04659E38F, -2.0130734E38F, -2.004894E38F,
         -1.9890568E38F, -1.987906E38F, -1.959425E38F, -1.9490274E38F,
         -1.8981818E38F, -1.8936544E38F, -1.8918448E38F, -1.8788894E38F,
         -1.8578673E38F, -1.8472943E38F, -1.8228317E38F, -1.7713137E38F,
         -1.7405715E38F, -1.6950927E38F, -1.6569501E38F, -1.6422773E38F,
         -1.5983914E38F, -1.5526859E38F, -1.5125922E38F, -1.4719676E38F,
         -1.4579763E38F, -1.4231359E38F, -1.3992558E38F, -1.3530183E38F,
         -1.3019594E38F, -1.2514604E38F, -1.2199076E38F, -1.1687584E38F,
         -1.1238228E38F, -1.0832502E38F, -1.0627832E38F, -1.0583307E38F,
         -1.0232546E38F, -9.811742E37F, -9.466244E37F, -9.302361E37F,
         -9.086858E37F, -8.584182E37F, -8.3533854E37F, -8.3436154E37F,
         -7.858905E37F, -7.728394E37F, -7.1914407E37F, -7.0869944E37F,
         -6.9172773E37F, -6.886007E37F, -6.453118E37F, -6.3530434E37F,
         -6.0021517E37F, -5.9882486E37F, -5.7717293E37F, -5.3170945E37F,
         -5.1579456E37F, -5.1413297E37F, -5.0813445E37F, -4.7276056E37F,
         -4.463832E37F, -3.9720536E37F, -3.741136E37F, -3.508229E37F,
         -3.182019E37F, -2.6423487E37F, -2.3696264E37F, -2.2123038E37F,
         -2.2027736E37F, -1.9234864E37F, -1.4946384E37F, -1.4668182E37F,
         -1.4423716E37F, -1.2374904E37F, -1.02872616E37F, -7.737757E36F,
         -4.7487646E36F, -4.08596E36F, -3.6158624E36F, 6.4022345E35F,
         5.7148326E36F, 8.861186E36F, 1.160242E37F, 1.320953E37F,
         1.549836E37F, 1.6355182E37F, 1.9385603E37F, 2.2191671E37F,
         2.4232198E37F, 2.950661E37F, 3.3587168E37F, 3.777544E37F,
         3.845732E37F, 4.093986E37F, 4.4565535E37F, 4.5685017E37F,
         4.7487E37F, 4.8326436E37F, 5.309224E37F, 5.59417E37F,
         5.874653E37F, 6.281282E37F, 6.4666493E37F, 6.676366E37F,
         7.213247E37F, 7.2974817E37F, 7.4529717E37F, 7.8132116E37F,
         7.997865E37F, 8.4110777E37F, 8.528072E37F, 9.016257E37F,
         9.218365E37F, 9.70984E37F, 9.827798E37F, 9.875848E37F,
         9.998123E37F, 1.0103553E38F, 1.0630406E38F, 1.0963299E38F,
         1.1059009E38F, 1.1579591E38F, 1.1874138E38F, 1.194467E38F,
         1.1954191E38F, 1.2342095E38F, 1.2644009E38F, 1.2796142E38F,
         1.307287E38F, 1.3569554E38F, 1.3844074E38F, 1.4224235E38F,
         1.4667858E38F, 1.5152258E38F, 1.5439099E38F, 1.5501056E38F,
         1.5676383E38F, 1.5812497E38F, 1.6029461E38F, 1.6107543E38F,
         1.6170426E38F, 1.6323268E38F, 1.6839599E38F, 1.7246524E38F,
         1.741077E38F, 1.7623838E38F, 1.8112007E38F, 1.8220046E38F,
         1.839404E38F, 1.8638388E38F, 1.9102318E38F, 1.9199128E38F,
         1.968795E38F, 2.0165465E38F, 2.040367E38F, 2.0634792E38F,
         2.1146006E38F, 2.1638078E38F, 2.1871567E38F, 2.2393508E38F,
         2.2893074E38F, 2.327769E38F, 2.3652162E38F, 2.41049E38F,
         2.4233445E38F, 2.4271098E38F, 2.4815883E38F, 2.4913072E38F,
         2.5080226E38F, 2.5163039E38F, 2.5296351E38F, 2.571536E38F,
         2.6064087E38F, 2.6256382E38F, 2.6319974E38F, 2.6546468E38F,
         2.6852996E38F, 2.7039764E38F, 2.7332303E38F, 2.7807721E38F,
         2.7959093E38F, 2.814496E38F, 2.8693663E38F, 2.8870193E38F,
         2.9314783E38F, 2.954733E38F, 2.9905255E38F, 3.0440927E38F,
         3.0817988E38F, 3.119903E38F, 3.1283114E38F, 3.1732442E38F,
         3.2267054E38F, 3.2393016E38F, 3.2638287E38F, 3.2732012E38F,
         3.308665E38F, 3.3106872E38F, 3.3140263E38F, 3.3190042E38F,
         3.3190216E38F, 3.359073E38F
      };
      floatArrayArr[2] = new Float[] {
         -3.3865866E38F, -3.3833144E38F, -3.3527492E38F, -3.3402117E38F,
         -3.3329526E38F, -3.3024412E38F, -3.2930139E38F, -3.2636563E38F,
         -3.220956E38F, -3.1863765E38F, -3.1405425E38F, -3.1117011E38F,
         -3.1079055E38F, -3.0741194E38F, -3.0330015E38F, -3.020598E38F,
         -2.97328E38F, -2.9383494E38F, -2.9199478E38F, -2.9067153E38F,
         -2.8917869E38F, -2.8810439E38F, -2.849742E38F, -2.8372172E38F,
         -2.803305E38F, -2.7802736E38F, -2.7256626E38F, -2.7053357E38F,
         -2.6841502E38F, -2.6475903E38F, -2.6326393E38F, -2.6073666E38F,
         -2.586403E38F, -2.5482047E38F, -2.5460673E38F, -2.5140836E38F,
         -2.4932606E38F, -2.4543432E38F, -2.4185106E38F, -2.4048774E38F,
         -2.3813372E38F, -2.3312754E38F, -2.3268175E38F, -2.2734987E38F,
         -2.262115E38F, -2.2610613E38F, -2.219131E38F, -2.2141335E38F,
         -2.1707154E38F, -2.149038E38F, -2.1447839E38F, -2.0939957E38F,
         -2.0578427E38F, -2.0198625E38F, -1.988142E38F, -1.9507595E38F,
         -1.9388083E38F, -1.8926839E38F, -1.848361E38F, -1.7954445E38F,
         -1.7931806E38F, -1.7767427E38F, -1.7726597E38F, -1.7224337E38F,
         -1.704553E38F, -1.6834987E38F, -1.6300886E38F, -1.6294954E38F,
         -1.5762974E38F, -1.5415953E38F, -1.5253941E38F, -1.4908884E38F,
         -1.4755123E38F, -1.4244073E38F, -1.4169708E38F, -1.4068874E38F,
         -1.3645001E38F, -1.3620087E38F, -1.3299077E38F, -1.321237E38F,
         -1.3200702E38F, -1.3004071E38F, -1.2865277E38F, -1.253599E38F,
         -1.237053E38F, -1.1980969E38F, -1.1436023E38F, -1.1076249E38F,
         -1.0589606E38F, -1.0048618E38F, -9.66303E37F, -9.584732E37F,
         -9.212035E37F, -9.031521E37F, -8.738244E37F, -8.585626E37F,
         -8.2208495E37F, -7.6973073E37F, -7.4992764E37F, -7.296074E37F,
         -7.1231615E37F, -6.8180157E37F, -6.359209E37F, -6.349584E37F,
         -6.0125185E37F, -5.876598E37F, -5.6275807E37F, -5.111241E37F,
         -4.5749084E37F, -4.0731515E37F, -3.8186907E37F, -3.325309E37F,
         -2.97697E37F, -2.6114122E37F, -2.282302E37F, -2.2464034E37F,
         -1.759553E37F, -1.215163E37F, -1.1302366E37F, -7.172977E36F,
         -2.377159E36F, -2.4603846E35F, 3.8788657E36F, 6.537324E36F,
         1.1739135E37F, 1.5185747E37F, 2.0569406E37F, 2.3874138E37F,
         2.5522619E37F, 2.9838885E37F, 3.2450103E37F, 3.4239958E37F,
         3.9288594E37F, 4.4777364E37F, 4.714404E37F, 5.2046397E37F,
         5.417114E37F, 5.7846167E37F, 5.9701694E37F, 6.044925E37F,
         6.063636E37F, 6.5211765E37F, 6.7243403E37F, 6.993056E37F,
         7.4665467E37F, 7.7529693E37F, 8.294048E37F, 8.825416E37F,
         9.3055E37F, 9.564654E37F, 9.959039E37F, 1.0182447E38F,
         1.0560462E38F, 1.0641843E38F, 1.1132334E38F, 1.1200648E38F,
         1.1470504E38F, 1.154146E38F, 1.1988728E38F, 1.235821E38F,
         1.2801183E38F, 1.32361E38F, 1.3595398E38F, 1.397558E38F,
         1.4381644E38F, 1.4647537E38F, 1.4862782E38F, 1.5201771E38F,
         1.5693522E38F, 1.6237092E38F, 1.628328E38F, 1.6479238E38F,
         1.6708366E38F, 1.7096126E38F, 1.7201962E38F, 1.7277982E38F,
         1.749632E38F, 1.7705661E38F, 1.793621E38F, 1.8196037E38F,
         1.8427076E38F, 1.849202E38F, 1.8680835E38F, 1.8774289E38F,
         1.894942E38F, 1.906019E38F, 1.9250264E38F, 1.951988E38F,
         1.968702E38F, 2.0214752E38F, 2.0632281E38F, 2.0994858E38F,
         2.145974E38F, 2.1694651E38F, 2.2049642E38F, 2.2097274E38F,
         2.2521306E38F, 2.258585E38F, 2.3132652E38F, 2.3608611E38F,
         2.3981952E38F, 2.4213074E38F, 2.4644014E38F, 2.4849781E38F,
         2.5081554E38F, 2.525035E38F, 2.533907E38F, 2.5705569E38F,
         2.5745292E38F, 2.598346E38F, 2.6453915E38F, 2.6756127E38F,
         2.7158877E38F, 2.7639294E38F, 2.8062152E38F, 2.8543638E38F,
         2.9071283E38F, 2.935895E38F, 2.987186E38F, 2.9992863E38F,
         3.0346543E38F, 3.0510835E38F, 3.0830569E38F, 3.0980573E38F,
         3.1109705E38F, 3.125902E38F, 3.1804041E38F, 3.2042145E38F,
         3.2212896E38F, 3.250488E38F, 3.3000722E38F, 3.3095487E38F,
         3.3204592E38F, 3.3311375E38F, 3.346968E38F, 3.3538133E38F,
         3.3849486E38F
      };

      Double[][] doubleArrayArr = new Double[3][];
      doubleArrayArr[0] = new Double[] {
         (-1 * Double.MAX_VALUE), 3.14159265358979323846264338327950288D,
         Double.MIN_NORMAL, Double.MIN_VALUE, Double.MAX_VALUE
      };
      doubleArrayArr[1] = new Double[] {
         -1.7713828140915773E308D, -1.7541627000289612E308D, -1.7300050595903683E308D,
         -1.7206080303985985E308D, -1.6998765602621326E308D, -1.6566861007236556E308D,
         -1.6038899136282398E308D, -1.5790768858792197E308D, -1.5338464547751722E308D,
         -1.5215510002859452E308D, -1.4777305179814106E308D, -1.4308198240657349E308D,
         -1.4009524439206173E308D, -1.3576277442121437E308D, -1.3249065185396402E308D,
         -1.2865457015897372E308D, -1.2750679027708566E308D, -1.222553247448897E308D,
         -1.2194034214869299E308D, -1.1853753272939995E308D, -1.1505387548914847E308D,
         -1.1418494664676373E308D, -1.094800289538418E308D, -1.0774811021532017E308D,
         -1.051928534998208E308D, -1.016810806738693E308D, -1.0011959292138006E308D,
         -9.61553865369795E307D, -9.22025737852646E307D, -8.901485125707401E307D,
         -8.782432942233183E307D, -8.294202611993816E307D, -7.747673766111169E307D,
         -7.520867505375053E307D, -7.157241643821298E307D, -6.842410338444272E307D,
         -6.600360263007954E307D, -6.580188317105252E307D, -6.455967671159632E307D,
         -6.028471804934E307D, -5.696417177531666E307D, -5.37577682226068E307D,
         -5.318128943902056E307D, -5.175420730108315E307D, -5.138300052032501E307D,
         -4.996774973505528E307D, -4.570532538118545E307D, -4.3873825912637095E307D,
         -4.1598817317988337E307D, -3.7436824782432304E307D, -3.4949523436011337E307D,
         -3.427328773770468E307D, -3.2047805219077444E307D, -2.8083657823384404E307D,
         -2.5343100639218325E307D, -2.093802869397147E307D, -1.853311345199543E307D,
         -1.4721043721565833E307D, -1.2562089985007122E307D, -1.1301980651037174E307D,
         -9.852475223243347E306D, -9.260572223545512E306D, -6.032229091177908E306D,
         -1.2385186121941351E306D, 2.5658158237398073E306D, 7.428392370705742E306D,
         1.0439718999471527E307D, 1.4801423722564004E307D, 1.744735416318117E307D,
         1.9218303044671067E307D, 2.2254645335743821E307D, 2.2576790707850574E307D,
         2.7679243492003786E307D, 2.830428562816655E307D, 3.030119262515604E307D,
         3.5099378870342735E307D, 3.8950023570546144E307D, 4.150304698510353E307D,
         4.547284810926699E307D, 4.73926439254098E307D, 4.920734022958258E307D,
         5.196252355899803E307D, 5.593043672249764E307D, 6.086749071141292E307D,
         6.300252423603347E307D, 6.511014663174875E307D, 6.790742083907812E307D,
         7.240510291973419E307D, 7.67198961786436E307D, 8.101959636555844E307D,
         8.340808942129526E307D, 8.811131833401644E307D, 9.259968487746343E307D,
         9.638219722203058E307D, 1.0040256001607461E308D, 1.0120455299796432E308D,
         1.042944403911257E308D, 1.0886719474294569E308D, 1.0901503787980634E308D,
         1.1005732284240158E308D, 1.153872382745659E308D, 1.1728253188080337E308D,
         1.2017046189979052E308D, 1.214675016984785E308D, 1.242833121237532E308D,
         1.280419241417395E308D, 1.3064703365408074E308D, 1.3472709441303963E308D,
         1.3810250410409347E308D, 1.3944249971220957E308D, 1.4291565089034686E308D,
         1.449209861720614E308D, 1.4558561154521233E308D, 1.4859754980211382E308D,
         1.5296947753820279E308D, 1.5456336438354258E308D, 1.6002080664645392E308D,
         1.6198849673046723E308D, 1.6249525657049505E308D, 1.656027847153725E308D,
         1.657343310468199E308D, 1.6893655527158198E308D, 1.7265001968819818E308D,
         1.7543568239716574E308D
      };
      doubleArrayArr[2] = new Double[] {
         -1.7475564800324186E308D, -1.7223378465224815E308D, -1.6886158386683863E308D,
         -1.6478967114154253E308D, -1.6031248714535138E308D, -1.5728628085496787E308D,
         -1.5268491084790048E308D, -1.4879062343664464E308D, -1.4639883084116508E308D,
         -1.4488198798722479E308D, -1.4429699948520799E308D, -1.4058682876963274E308D,
         -1.361777459017444E308D, -1.3532233654322164E308D, -1.3296971242888531E308D,
         -1.3089675763874477E308D, -1.2610738411786149E308D, -1.2313572421357835E308D,
         -1.2116283984423755E308D, -1.1707273457790489E308D, -1.148404661526923E308D,
         -1.1388514347328438E308D, -1.106292689595518E308D, -1.0749989854204236E308D,
         -1.061701953311292E308D, -1.0508784948416004E308D, -1.0448139195994947E308D,
         -9.956871108493611E307D, -9.78531482568025E307D, -9.465062609706983E307D,
         -9.170276972991114E307D, -9.162984857453817E307D, -8.836313475352281E307D,
         -8.721767833724502E307D, -8.568458987754063E307D, -8.240117079614063E307D,
         -7.930074132010156E307D, -7.428454010680108E307D, -7.306549340000178E307D,
         -7.122644675008864E307D, -6.66137453862788E307D, -6.166313343196095E307D,
         -5.626839448301752E307D, -5.119715559806084E307D, -4.878634161586408E307D,
         -4.420276069390188E307D, -4.297148672424753E307D, -3.7754452566430456E307D,
         -3.504262947021471E307D, -3.0891840255547016E307D, -3.013520992302138E307D,
         -2.78008045671695E307D, -2.619458882328428E307D, -2.1898118187742408E307D,
         -1.6945048943679395E307D, -1.5382354115167364E307D, -1.385931706571166E307D,
         -1.306340378217707E307D, -7.934153837389781E306D, -6.966270376423843E306D,
         -1.687106963797085E306D, 2.3349208354431008E306D, 6.567604438651025E306D,
         7.955323568153696E306D, 1.2206478699774648E307D, 1.6296774412381165E307D,
         1.9837882509554863E307D, 2.2439276082536912E307D, 2.5412540475684807E307D,
         2.8118301621665173E307D, 2.9421992379491927E307D, 3.021449904330044E307D,
         3.3554182017353595E307D, 3.686364781517637E307D, 4.0258315708752817E307D,
         4.0410781269684176E307D, 4.3983783068120556E307D, 4.4663610052804797E307D,
         4.505670941494743E307D, 4.714097875197365E307D, 5.185509543287463E307D,
         5.254203829317367E307D, 5.475490863007027E307D, 5.856869758481654E307D,
         6.116014735292154E307D, 6.308481068953772E307D, 6.456170550945086E307D,
         6.885890768996717E307D, 7.239827738405243E307D, 7.485477151192851E307D,
         7.661344519438624E307D, 7.674260074386201E307D, 7.779968005096962E307D,
         7.820668985773371E307D, 7.930962457745226E307D, 8.101614663601528E307D,
         8.164614759350102E307D, 8.675657592082952E307D, 9.2141609721068E307D,
         9.332890080224313E307D, 9.690472803436646E307D, 9.889708790273662E307D,
         9.997320446075786E307D, 1.022763713466587E308D, 1.0380328807869993E308D,
         1.0811275631212225E308D, 1.1096049839595742E308D, 1.1229837208836119E308D,
         1.1381797070895334E308D, 1.166228628862098E308D, 1.2179860603565108E308D,
         1.2436956264356247E308D, 1.26498779766204E308D, 1.2675353250182189E308D,
         1.2994207947331653E308D, 1.3115111467798754E308D, 1.348184903965093E308D,
         1.351014188511096E308D, 1.3705142393051877E308D, 1.4183443409855221E308D,
         1.4647929487346632E308D, 1.4868680674696423E308D, 1.5035778551941335E308D,
         1.520384279024605E308D, 1.5403853312070202E308D, 1.5859149681496436E308D,
         1.6309900024176074E308D, 1.6800821483417684E308D, 1.725070320388113E308D,
         1.7572399649802981E308D
      };
      String[][] stringArrayArr = new String[3][];
      stringArrayArr[0] = new String[] {
         "Yo", "man,", "this", "is", "a", "test", "sentence", "that", "we",
         "are", "going", "to", "tokenize", "for", "some", "test", "data."
      };
      stringArrayArr[1] = new String[] {
         ":L_,d?A32kE$xNiJ(5+y@JJF7(2Yq,", "@:.)]i*rmh.fd,3Rnm##aPbLinn39B",
         "=&M$72r(?+YQgVMf-+}V8v&8yJvk_7", "w[A=K]/Yqj]BpY2c5t{X$mN%jB!#?u",
         "VFF]D(Jpdn7u_]5PmzyvnHDvJL3u@", "5DG-RW#!:!+af_YZ=pwAP?pHQEJ){$",
         "Fvy[#Zy%jWi+&xCCEf]H8k*.:BYumN", "TCbG:!QUh+-fS-Z*#WJuTq#u!)87U",
         "ap+tD%}+Va!MWuWirPL?z*Lc!!{uLa", "@-=$8u/@VAR5Wfj64#ChVW+.@-XHz8",
         "DT#CMnCTBT)PcTa7HQvEK3??GAiei/", "dceAeb-n_u&e@z2bD{&7bE%H-(pbv-",
         "wW.S7,ubSWhCVHz,-txPqQwFw+8p,5", "E%}U}_]Z]wf$nnSX_=PRJf2}+Jc@H",
         "NH3&$*GbSq7?X+Y8+h_fKxf)EW*V##", "&MQ@h=4U@*zX(WtYWU]vhnnJ?6g/]A",
         "F!75R@7tyvz&e)r,NHnTr@c65n)MiX", "$SVwa*,tKj3@AX{Xt%iP[c6L}Raqp",
         "(HH9*+DpvjE/Lx6Wqrd8yP*e{rS=J", "irq:q3zh?Xa/}fg$pSe:jX/TEWz(e",
         "{?x=!2}E6u95ebiQXnhA]jr@]%xdm", "fQ83WXPAncKmEHTkX+DZ_YQ!CxiJ!/",
         "r$K)n{cSwV#FUYcF,zqGt.TRbk}]=t", "SSbSDTe*xTdW,/R%h_ymLueH/6Y9$t",
         ",HTnNB#?5.=X[6[M{KFE@t?L7DYy&9", "hXght,L[*m$$RJe97eh=KkH{eQE+.c",
         "izJ&_@}:.J(6GDvL_{77mn:4RMRDS3", "gj)2gZmySBBmu[z,f:J#=ziL6y)vi",
         "4H#QWCma@:iHv={AD}B7]qbyLS:xi#", "DJ{:rw:7q,T]V,_M]bD%j.)5J?!g3b",
         "_6#mE.Zb=yjT?!,nY!RdCYCDxiV{JL", "L9Y9,5YWd=Ud:eZ79]:ap33$xAWXMR",
         "h%$Kn37[/#&/Vtgp$j+c}v-4](WjVH", "r-CGiPCB-H,}@Qz35y-:.iWMmjU6.D",
         "}]RHL]kZ7vHWcP*q)a5F6aXe,53Vww", "wBvGJAMb9+7&P-2_{q@jC-r2n]zn@_",
         "HXCtUb79Qk5j_/qrVRN/$Ac!,6bJKj", "wY{r@+%Y2c2[+Xg2)*rHmcNX_a&{W",
         "Ex8LDa%f9Z8Pn*6ra$:-8w[m3[cgwJ", "2z!+Wx={NA/pbU,c/Y_NVxqe]7:v]W",
         ",k5p+Du/CJM?4hRYr#}EZvLG,ney#", "#RH:PjA-.Gh${E2{e?L(+L)2*NrHrU",
         "Yd[Sf5f&m+(Znuqc7VTYir}yx/%:c3", ":e$rU]K/pc{JLuNX@){C#Hrf37P:Cd",
         "K!jH{6RECJT@z@aw{vpC=pN:_@V:?W", "Ru=mdKSFYiMieA#V{[[TydC],Q6&J",
         "LvVp7iU4M@#RGVPZd3$P5wLk#GtL", "NPj!)}*Jw2a)2?&mj.=[G[$=%VL%$",
         "&6w.t3#V3/NP!QXkzeaaEZ_]z8k2N}", "hu@W%.GTkL,/(eak9&p,+ebWf9QpwJ",
      };
      stringArrayArr[2] = new String[] {
         "-Q_)n$gM&]=rQ?@$#dD$j}=BP+Nrp", "GQnML.MJv@9.wVFL/WP2@VJL,fFYa:",
         "t=hq,+45.LiBERWmke[!$hEt!*aVc@", "CV.CM49hdPXpFi5&pWxY=a,h-7Rwey",
         "$c3bchEjSkVCqti3aVrU)7b_/ezhU_", "5/W-YPf36/E{LC77()jUgh_8QH(YXY",
         "4@+]e6*5,QVGgu*]_Up2?Yt[x%f]3", "(Grw=Dy!(9}p&mi}]DBT?VxEh29d2W",
         "[%wV,#PZ{gT_Bz::rt}}BnJDmvHLX", "m&Gn-c=jD&]?}Vm5=jN*gFg%v!h,",
         "&.bY/Re(wa&Xa-=-WCpiv)bQEFQV", "]fS_xRb8Cz9HFAaqSy.7migV%Rhz)",
         "_.V+JQLu&]xeru{NS{R-ySU58giz", "QFYRb_wxFk!TD$w#7w&eMvWyej",
         "YJ+Z.{}d{@C(Y4AbmB?6fN,U=P.", "4VeR@y%h*nn@&3vzTP9%LpCYg9[9Y",
         "Wvx[{rAmLX.8c!-Uv@i+,A!Ny:)AaX", "/2zuq5gT/pnHG&uup7FtLWczXg&XaL",
         ":czC4L4mmUyRUW-AMg)%.3T]{Q]Yg#", "bB:A2*)dc2{SCL6&?VnTURMJ!C9$#/",
         "[4YCWaFK#=8]3(gUL3X(THh:Y&HHA", "HNeWBXZ(U?a2UbPRgVXCDzeM6X[7]a",
         "5[E5CSa(*:#$82P7WeeQ={D5}!X%m", "kczFx/X)U#ctm3gp7)by4?F$=564",
         "_wXtzXEDwfme}&ceQRTeL3(*q{{(#", "PJ4:U5Ck=:CVS.b&tVvyF6!S!!8iPg",
         "L?gid:65G6Y7gg&RPV8:F6Jj&E&cX8", "pTFRMBPi_U{pAE%B73a6e-rx.9?HXT",
         "PA+Uug3.R&cF7*4.HR2QZu_){$JUWF", "Hj]WD7x8[SBWeU62/xK@R4rayAzv}}",
         "b&4P)a,)bLFaSYmE[t7Ta2MhmUb,PG", "5Jguega@RkKyk8hQE]J@ui&7p&%NQ9",
         "gJ=*ULQG:.mmNiRFMB(hK[69#DL-/", ")+:kC9x4p_CRNJ}j}NfW43!(hZKd_G",
         "8w#H*i3ka+5MTCScg]JdX38NLeB!}X", "&.DhFZp2d-=K5BfggyTFA{6Z/)/34h",
         "6,7*Bbm!!jt{hZ(*2fM)=]wdX/Mr=", "Q[e4DzTDRwErGrBC76)27xjL3c#pb=",
         "MXw.B)wLiPchf68i)W9{Z_)3=gk-H?", "d)Uu?aPEEG?94S@U@#N$:#mW$EZBE",
         "&6x9n:WH/X]E./pP{p,-]RMTj?CFk7", "cUS6{(b5mMGvSHTa+kk8$+Tn38cHL",
         "#$Lef:Yz=]t/uVyS8D-xy:aD4?@{H", "5U(V4}mciX!HiH(ErnCu7c-P.}{nZj",
         "NnQY2x]XeiWwz4tBQT3:c97EEPYeqK", "R7th_-w!/e{/B4Q=e(&kyBf]!Ei]]",
         "9=7Dh7TmR!Vv-9PbC/8WC!tb#c3-+", "A5WF{S8L67!*W]-_E?MY&2xK:z2{-g",
         "DuK%X$TqFezdu:pQ%vqiR3SQwu2c{3", "&*()&C7d7)}vCHmBCefcRb7!PPCf6z",
      };

      Object[][][] testDataArray = new Object[8][][];
      testDataArray[0] = charArrayArr;
      testDataArray[1] = byteArrayArr;
      testDataArray[2] = shortArrayArr;
      testDataArray[3] = intArrayArr;
      testDataArray[4] = longArrayArr;
      testDataArray[5] = floatArrayArr;
      testDataArray[6] = doubleArrayArr;
      testDataArray[7] = stringArrayArr;

      TestDataGenerator tdg = new TestDataGenerator(outputDir);

      // Write out the arrays of objects
      for (Object[] arr : testData) {
         for (Object obj : arr) {
            tdg.writeData(obj);
         }
      }

      // Write out the arrays of arrays of objects
      for (Object[][] arr : testDataArray) {
         for (Object[] objArr : arr) {
            tdg.writeData(objArr);
         }
      }
   }
}
