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
   
//   private static final String LONG_FILE_PREFIX   = "long_data";
//   private static final String INT_FILE_PREFIX    = "int_data";
//   private static final String FLOAT_FILE_PREFIX  = "float_data";
//   private static final String DOUBLE_FILE_PREFIX = "double_data";
//   private static final String STRING_FILE_PREFIX = "string_data";
   
   private int outputCounter;
   
   private static final String FILE_BIN_SUFFIX    = ".bin";
   private static final String FILE_ASCII_SUFFIX  = ".txt";
   
   public TestDataGenerator() {}
   
   /**
    * Write out binary data and a text file containing the data in ASCII.
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
         ((DataOutputStream)out).writeInt(((Byte)data).byteValue());
         writer.write("(byte)" + ((Byte)data).toString());
      } else if (data instanceof Character) {
         ((DataOutputStream)out).writeInt(((Character)data).charValue());
         writer.write("'" +  ((Character)data).toString()  + "'");
      } else if (data instanceof Short) {
         ((DataOutputStream)out).writeInt(((Short)data).shortValue());
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
         ((DataOutputStream)out).writeBytes((String)data);
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
            Long.MAX_VALUE,
            -36028797018963968L,
            0L,
            2305843009213693952L,
            Long.MAX_VALUE
      };
      
      Float[] floatArr = new Float[] {
            Float.MAX_VALUE,
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
         "Here is a String that is human readable.  It is a lot easier to read than the random Strings that comprise the rest of this map, no?",
         "8EOTMO,9<R*.s[e3s;n.I/ipJsAkedF>i82]ezG$BzJ9/c`kQw\\\"07ByH#zR\\\"~xo3x#7&iEdQ\\\"Lf>C#YHqMG.@$J7GHlb3.2J(bT@N4fLq?pqkCz`uDsWW;,FBo#_1a&",
         "L`%xA9CT|Wj|HpER|~rv]wc`qI#z*i&{'14GP4Yr*IE>#8ipXhH>Z|_1@FyKawbHcaIkI0#JHkzb*&[`vDJl`[_r$H1$T_?@\\\"(GR@(,I^U9+A0_]w*PqD5:,Is1'@u#z",
         "et83qQS&T{Ask{'!3$/RnLzn<DHGtBmQRq?Uf~j)9]1JbRJd/.|55BY{8o0c/u&?q>5<BosYlT/sk8x#(:$u5c!Vlsw\\\"^_`G`7]]b`3N+q$OV..6]HSpA3srw*jq]`(",
         "g3&ElAY%T(JQEH|k{D~zDXoxPqA\\\"R<y5oE>w#IOc4%%xD'x|y9@OM;_F/gi1<_}]#7%\\\"Y'P8wyjARsn2+E.~7lAf{>o(Z|?EbP&rF^>>Q3wiyP,}sas1\\\"OSi^2W(J)t$"
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
