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
      String dataId = className + "_" + outputCounter;
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
         writer.write("'" +  ((Character)data).toString()  + "'");
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
         // NoOp as we don't know how to write out this data
      }

      closeOutputStream(out);      
      closeBufferedWriter(writer);
      outputCounter++;
   }
   
   private OutputStream getOutputStream(String fileName) throws IOException {
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
         
      TestDataGenerator tdg = new TestDataGenerator();
      
      for (Object[] arr : testData) {
         for (Object obj : arr) {
            tdg.writeData(obj);
         }
      }
   }
}
