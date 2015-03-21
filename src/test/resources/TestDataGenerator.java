import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.BufferedWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;

/**
 * Utility for binary data files on the host OS with which we can then get a
 * baseline of the variety of hash algos that we are going to test.
 * 
 * @author Ryan Chapin
 * @since  2015-03-11
 */
public class TestDataGenerator {
   
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
   
   public TestDataGenerator() {}
   
   /**
    * Write out binary data and a text file containing the data in ASCII
    * representation of the data.
    * 
    * @param data - The data to be written out
    * @throws IOException
    */
   public void writeData(Object data) throws IOException {
      
      // Get the unprefixed class name
      String[] classNameArr = data.getClass().getName().split("[.]");
      String className = classNameArr[((classNameArr.length)-1)];
      String dataId = className + "_" + SCALAR + "_" + outputCounter;
      System.out.println("dataId = " + dataId);
      
      // Binary data
      OutputStream out = getOutputStream(dataId + FILE_BIN_SUFFIX);
      // ASCII data
      BufferedWriter writer = getBufferedWriter(dataId + FILE_ASCII_SUFFIX);

      if (data instanceof Byte) {
         ((DataOutputStream)out).writeByte(((Byte)data).byteValue());
         writer.write("(byte)" + ((Byte)data).toString());
      } else if (data instanceof Character) {
         ((DataOutputStream)out).writeChar(((Character)data).charValue());
         writer.write("(char)0x" + Integer.toHexString((int)(Character)data));
      } else if (data instanceof Short) {
         ((DataOutputStream)out).writeShort(((Short)data).shortValue());
         writer.write("(short)" + ((Short)data).toString());
      } else if (data instanceof Integer) {
         ((DataOutputStream)out).writeInt(((Integer)data).intValue());
         writer.write(((Integer)data).toString());
      } else if (data instanceof Long){
         ((DataOutputStream)out).writeLong(((Long)data).longValue());
         BigInteger bi = new BigInteger(((Long)data).toString());
         writer.write(bi.toString() + "L");
      } else if (data instanceof Double){
         ((DataOutputStream)out).writeDouble(((Double)data).doubleValue());
         BigDecimal bd = new BigDecimal(((Double)data).doubleValue());
         writer.write(bd.toPlainString() + "D");
      } else if (data instanceof Float){
         ((DataOutputStream)out).writeFloat(((Float)data).floatValue());
         BigDecimal bd = new BigDecimal(((Float)data).toString());
         writer.write(bd.toPlainString() + "F");
      } else if (data instanceof String){
         byte[] byteArr = ((String)data).getBytes(DEFAULT_CHAR_ENCODING);
         ((DataOutputStream)out).write(byteArr);
         writer.write("\"" + data.toString() + "\"");   
      } else {
         // NoOp as we do not know how to write out this data.
      }

      closeOutputStream(out);      
      closeBufferedWriter(writer);
      outputCounter++;
   }
   
   public void writeDataArray(Object[] data) throws IOException {
      
      // Get the unprefixed class name,
      String[] classNameArr = data.getClass().getName().split("[.]");
      
      // Get the last token, with is the unqualified class name, and remove
      // and ';' if they exist.
      String className = classNameArr[((classNameArr.length)-1)]
            .replace(";", "");
      String dataId = className + "_" + ARRAY + "_" + outputCounter;
      System.out.println("dataId = " + dataId);
      
      // Binary data
      OutputStream out = getOutputStream(dataId + FILE_BIN_SUFFIX);
      // ASCII data
      BufferedWriter writer = getBufferedWriter(dataId + FILE_ASCII_SUFFIX);

      if (data[0] instanceof Character) {
         int arrLen           = data.length;
         int delimLenBoundary = arrLen - 1;
         
         // Write the prefix for the instantiation of the List
         writer.write("Arrays.asList(new Character[] {");
         
         for (int i = 0; i < arrLen; i++) {
            Character charData   = (Character)data[i];
            ((DataOutputStream)out).writeChar(charData.charValue());
            
            // Write out the ASCII representation in hex
            writer.write("(char)0x" + Integer.toHexString((int)charData));
            if (i < delimLenBoundary) {
               writer.write(", ");
            }
         }
         // Write the suffix for the instantiation of the list
         writer.write("})");
      } else {
         // NoOp as we do not know how to write out this data.
      }
      
      closeOutputStream(out);      
      closeBufferedWriter(writer);
      outputCounter++;
   }

   private OutputStream getOutputStream(String fileName) throws IOException {
      System.out.println("fileName = " + fileName);
      OutputStream out = new DataOutputStream (
            new BufferedOutputStream(
                  new FileOutputStream(fileName)));
      return out;
   }
   
   private void closeOutputStream(OutputStream out) throws IOException {
      out.flush();
      out.close();
   }
   
   private BufferedWriter getBufferedWriter(String fileName) throws IOException {
      Path path = FileSystems.getDefault().getPath(fileName);
      BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
      return writer;
   }
   
   private void closeBufferedWriter(BufferedWriter writer) throws IOException {
      writer.close();
   }
   
   public static void main(String[] args) throws IOException {
      
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
      

      Character[][] charArrayArr = new Character[2][];
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
            (char)0xfc, (char)0xfd, (char)0xfe, (char)0xff};
      // TODO:  Add a few more test data sets.
      
      Object[][][] testDataArray = new Object[1][][];
      testDataArray[0] = charArrayArr;
      
      TestDataGenerator tdg = new TestDataGenerator();
      
      // Write out the arrays of objects
      for (Object[] arr : testData) {
         for (Object obj : arr) {
            tdg.writeData(obj);
         }
      }
      
      // Write out the arrays of arrays of objects
      for (Object[][] arr : testDataArray) {
         for (Object[] objArr : arr) {
            tdg.writeDataArray(objArr);
         }
      }
   }
}
