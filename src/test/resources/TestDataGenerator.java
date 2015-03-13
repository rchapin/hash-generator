import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.BufferedWriter;
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

   private static final String LONG_FILE_PREFIX   = "long_data";
   private static final String INT_FILE_PREFIX    = "int_data";
   private static final String FLOAT_FILE_PREFIX  = "float_data";
   private static final String DOUBLE_FILE_PREFIX = "double_data";
   private static final String STRING_FILE_PREFIX = "string_data";
   private static final String FILE_BIN_SUFFIX        = ".bin";
   private static final String FILE_ASCII_SUFFIX  = ".txt";
   
   
   /**
    * Write out binary data and a text file containing the data in ASCII.
    * 
    * @param data - The data to be written out
    * @throws IOException
    */
   public static void writeData(Object data) throws IOException {
      String className = data.getClass().getName();
      String dataId = null;
      
      if (data instanceof String) {
         String[] tokens = ((String)data).split(":", 2);
         dataId = tokens[0];
      } else {
         dataId = data.toString();
      }
      
      // Binary data
      OutputStream out = getOutputStream(className + "." + dataId + FILE_BIN_SUFFIX);
      
      
      if (data instanceof Integer) {
         ((DataOutputStream)out).writeInt(((Integer)data).intValue());
      } else if (data instanceof Long){
         ((DataOutputStream)out).writeLong(((Long)data).longValue());
      } else if (data instanceof Double){
         ((DataOutputStream)out).writeDouble(((Double)data).doubleValue());
      } else if (data instanceof Float){
         ((DataOutputStream)out).writeFloat(((Float)data).floatValue());
      } else if (data instanceof String){
         ((DataOutputStream)out).writeBytes((String)data);
      
         // Write out ASCII data
         BufferedWriter writer = getBufferedWriter(className + "." + dataId + FILE_ASCII_SUFFIX);
         writer.write((String)data);
         closeBufferedWriter(writer); 
         
      } else {
         // NoOp as we don't know how to write out this data
      }
      
      closeOutputStream(out);
   }
   
   private static OutputStream getOutputStream(String fileName) throws IOException {
      OutputStream out = new DataOutputStream (
            new BufferedOutputStream(
                  new FileOutputStream(fileName)));
      return out;
   }
   
   private static void closeOutputStream(OutputStream out) throws IOException {
      out.flush();
      out.close();
   }
   
   private static BufferedWriter getBufferedWriter(String fileName) throws IOException {
      Path path = FileSystems.getDefault().getPath(fileName);
      BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
      return writer;
   }
   
   private static void closeBufferedWriter(BufferedWriter writer) throws IOException {
      writer.close();
   }
   
   public static void main(String[] args) throws IOException {
      
      Integer[] intArr = new Integer[] {
         Integer.MIN_VALUE,
         -256,
         0,
         191867248,
         Integer.MAX_VALUE
      };
      
      String[] stringArr = new String[] {
         "string1: Here is a String that is human readable.  It is a lot easier to read than the random Strings that comprise the rest of this map, no?",
         "string2: 8EOTMO,9<R*.s[e3s;n.I/ipJsAkedF>i82]ezG$BzJ9/c`kQw\"07ByH#zR\"~xo3x#7&iEdQ\"Lf>C#YHqM\\G.@$J7GHlb3.2J(bT@N4fLq?pqkCz`uDsWW;,FBo#_1a&",
         "string3: L`%xA9CT|Wj|HpER|~rv]wc`qI#z*i&{'14GP4Yr*IE>#8ipXhH>Z|_1@FyKawbHcaIkI0#JHkzb*&[`vDJl`[_r$H1$T_?@\"(GR@(,I^U9+A0_]w*PqD5:,Is1'@u#z",
         "string4: et83qQS&T{Ask{'!3$/RnLzn<DH\\GtBmQRq?Uf~j)9]1JbRJd/.|55BY{8o0c/u&?q>5<BosYlT/sk8x#(:$u5c!Vlsw\"^_`G`7]]b`3N+q$OV..6]HSpA3srw*jq]`(",
         "string5: g3&ElAY%T(JQEH|k{D~zDXoxPqA\"R<y5oE>w#IOc4%%xD'x|y9@OM;_F/gi1<_}]#7%\"Y'P8wyjARsn2+E.~7lAf{>o(Z|?EbP&rF^>>Q3wiyP,}sas1\"OSi^2W(J)t$"
      };

      Object[][]   testData = new Object[2][];
      testData[0] = intArr;
      testData[1] = stringArr;
         
      for (Object[] arr : testData) {
         for (Object obj : arr) {
            writeData(obj);
         }
      }
   }
}
