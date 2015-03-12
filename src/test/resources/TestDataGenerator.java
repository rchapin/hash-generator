import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.IOException;

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
   private static final String FILE_SUFFIX        = ".out";
   
   
   public static void writeData(Object data) throws IOException {
      String className = data.getClass().getName();
      OutputStream out = getOutputStream(className + "." + data + FILE_SUFFIX);
      
      if (data instanceof Integer) {
         ((DataOutputStream)out).writeInt(((Integer)data).intValue());
      } else if (data instanceof Long){
         ((DataOutputStream)out).writeLong(((Long)data).longValue());
      } else if (data instanceof Double){
         ((DataOutputStream)out).writeDouble(((Double)data).doubleValue());
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
   
   private static void closeOutputStream(OutputStream out) {
      try {
         out.flush();
         out.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
   
   public static void main(String[] args) throws IOException {
      
      // Generate some binary files with the following data/data types
      Long[] longArr    = new Long[] {123456789L, 9223372036854775807L};
      Integer[] intArr  = new Integer[] {191867248, 108208310};
      
      Object[][] objArr = new Object[2][];
      objArr[0] = longArr;
      objArr[1] = intArr;
      
      for (Object[] arr : objArr) {
         for (Object obj : arr) {
            writeData(obj);
         }
      }
   }
}
