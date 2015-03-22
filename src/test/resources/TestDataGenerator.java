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
            (char)0xfc, (char)0xfd, (char)0xfe, (char)0xff};
      
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
