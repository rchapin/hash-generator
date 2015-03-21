package com.ryanchapin.util.hashgenerator;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * The <code>Hash-Generator</code> is a class used for generating hexidecimal
 * hashes for various types of input data.
 * <p>
 * Supported input formats (and their associated wrapper classes) are:
 * <ul>
 *    <li>byte</li>
 *    <li>char</li>
 *    <li>short</li>
 *    <li>int</li>
 *    <li>long</li>
 *    <li>float</li>
 *    <li>double</li>
 *    <li>String</li>
 * </ul>
 * <p>
 * It supports any of the hash algorithms that are supported by the
 * {@link java.security.MessageDigest} class.
 * <p>
 * NOTE: that the unit tests in this project DO NOT test the usage of the
 * <code>MD2</code> digest algorithm as it has not been included in the openssl
 * since openssl-0.9.8m (2010-02-25), and is not in general use anymore.
 * <p>
 * The class is thread safe <b>depending on how it is instantiated and/or
 * called</b>.  Used in the following manner it is thread safe:
 * <p><blockquote><pre>
 *    // Calling static methods
 *    String sha1Hash = HashGenerator.createHash("This is a test", "SHA-1"); 
 * </pre></blockquote>
 * <p>
 * Used in the following manner thread safety must be taken into account by
 * the calling code:
 * <p><blockquote><pre>
 *    // Calling member methods on a HashGenerator Instance
 *    HashGenerator hashGenerator = new HashGenerator();
 *    String sha1Hash = hashGenerator.createHash("This is a test", "SHA-1"); 
 * </pre></blockquote>
 * <p>
 * When the <code>createHash</code> methods are called on a
 * <code>HashGenerator</code> instance synchronization must be handled by the
 * calling code or their must only be a single thread making calls into the
 * <code>HashGenerator</code> instance.
 * <p>
 * The reason for this design is to enable the user to optimize for either
 * "build-in" synchronization (usage of the static methods), or optimize for
 * fewer Objects on the heap to be garbage collected.
 * <p>
 * In the case where there is a high rate and volume of calls to the
 * <code>HashGenerator</code> static methods, resulting in garbage which
 * collection causes performance issues, the programmer can opt to instantiate
 * a <code>HashGenerator</code>.  Then calls to the instance can be limited to
 * a single thread, or the calling code can wrap the <code>HashGenerator</code>
 * in synchronized methods.
 * 
 * @author  Ryan Chapin
 * @since   2015-03-01
 * @version 1.0
 */
public class HashGenerator {
   
   /**
    * Hash algorithm to be used to generate hashes for subsequent calls to
    * any of the overloaded <code>createHash</code> methods. 
    */
   private HashAlgorithm hashAlgo;
      
   /**
    * {@link java.security.MessageDigest} instance instantiated when a
    * <code>HashGenerator</code> instance is used in lieu of the
    * <code>HashGenerator</code> static methods.
    */
   private MessageDigest md;
   
   private static final int CHAR_BYTES_SIZE    = Character.SIZE/8;
   private static final int SHORT_BYTES_SIZE   = Short.SIZE/8;
   private static final int INTEGER_BYTES_SIZE = Integer.SIZE/8;
   private static final int LONG_BYTES_SIZE    = Long.SIZE/8;
   private static final int FLOAT_BYTES_SIZE   = Float.SIZE/8;
   private static final int DOUBLE_BYTES_SIZE  = Double.SIZE/8;
   
   private static final String EMPTY_OR_NULL_ENCODING_ERR =
         "null or empty String passed as encoding argument";
   /**
    * Map of ByteBuffer instances that will be re-used during the life cycle
    * of the HashGenerator instance.  They will NOT be used when the static members
    * are called.
    */
   private Map<DataType, ByteBuffer> byteBufferMap;
   
   /**
    * Map of byte arrays that will be re-used during the life cycle of the
    * HashGenerator instance.  They will NOT be used when the static members
    * are called.
    */
   private Map<DataType, byte[]> byteArrayMap;
   
   // -------------------------------------------------------------------------
   // Accessor/Mutators:
   //

   /**
    * Get the currently configured hash algorithm setting.
    * 
    * @return  the currently configured hash algorithm.
    */
   public HashAlgorithm getHashAlgo() {
      return hashAlgo;
   }

   /**
    * Sets the hash algorithm to be used for the next invocation the overloaded
    * createHash methods.
    * 
    * @param hashAlgo
    *        new hash algorithm to be set.
    */
   public void setHashAlgo(HashAlgorithm hashAlgo) {
      this.hashAlgo = hashAlgo;
      
      // When we update the hashAlgo we must null the existing reference to the
      // MessageDigest instance such that we can regenerate a new instance with
      // the updated algorithm.
      md = null;
   }
   
   
   
   // -------------------------------------------------------------------------
   // Constructor:
   //
   
   /**
    * Initializes a new {@code HashGenerator} instance such that subsequent
    * calls can be made passing in only the data to be hashed.
    * <p>
    * In this context the instance is <b>NOT</b> thread safe and thread safety
    * should be managed by the client.  This constructor should be used in
    * situations where client code is making large number of calls to this
    * class and there are issues arising from garbage collection of objects
    * that are instantiated on the stack.
    * <p>
    * In that case, the developer may opt to instantiate a new instance per
    * thread, or simply synchronize calls to the class if that will not
    * cause a performance bottleneck.
    * 
    * @param hashAlgo
    *        Hash algorithm to be used to create hashes.
    */
   public HashGenerator(HashAlgorithm hashAlgo) {
      this.hashAlgo = hashAlgo;
      byteBufferMap = new HashMap<DataType, ByteBuffer>();
      byteArrayMap  = new HashMap<DataType, byte[]>();
   }
   
   /**
    * Default constructor.  Instantiating in this manner leaves the instance
    * without a configured {@link HashAlgorithm} value, and subsequent calls
    * will result in an {@link IllegalStateException}.
    * <p>
    * Making a subsequent, valid call to {@link #setHashAlgo(HashAlgorithm)}
    * will properly configure the instance.
    */
   public HashGenerator() {
      this(null);
   }
   
   // -------------------------------------------------------------------------
   // Member Methods:
   //

   /**
    * Will check the hashAlgo field ensuring that it contains a valid value.
    * 
    * @throws IllegalStateException
    */
   private void checkHashAlgoField() throws IllegalStateException {
      if (null == this.hashAlgo) {
         throw new IllegalStateException("No hashing algorithm was set for this HashGenerator instance.");
      }  
   }
   
   /**
    * Will check the hashAlgo argument that is passed in to any of the static
    * <code>createHash</code> methods, ensuring that is valid before continuing
    * to generate a hash.
    *  
    * @param hashAlgo
    *        Hash algorithm to be validated.
    * @throws IllegalArgumentException
    */
   private static void checkHashAlgoInput(HashAlgorithm hashAlgo)
         throws IllegalArgumentException
   {
      if (null == hashAlgo) {
         throw new IllegalArgumentException("No hashing algorithm was provided.");
      }
   }
   
   /** -- Bytes ------------------------------------------------------------ */
   
   /**
    * Generates a hexadecimal hash of a byte.
    * 
    * @param  input
    *         byte to be hashed.
    * @param  hashAlgorithm
    *         {@link HashAlgorithm} to be used to generate the hash. 
    * @return hexadecimal hash of the input data.
    * @throws NoSuchAlgorithmException
    * @throws IllegalArgumentException
    */
   public static String createHash(byte input, HashAlgorithm hashAlgorithm)
      throws NoSuchAlgorithmException, IllegalArgumentException
   {
      checkHashAlgoInput(hashAlgorithm);
      byte[] byteArray = new byte[] {input};
      
      String retVal = bytesToHex(computeHashBytes(byteArray, hashAlgorithm));
      clearByteArray(byteArray);
      return retVal;
   }
   
   /**
    * Generates a hexadecimal hash of a byte and/or its wrapper class.
    * 
    * @param  input
    *         byte to be hashed.
    * @return hexadecimal hash of the input data.
    * @throws NoSuchAlgorithmException
    * @throws IllegalStateException
    */
   public String createHash(byte input)
      throws NoSuchAlgorithmException, IllegalStateException
   {
      checkHashAlgoField();
      byte[] byteArray = getByteArray(DataType.BYTE, 1);
      byteArray[0] = input;
      
      String retVal = bytesToHex(computeHashBytes(byteArray));
      clearByteArray(DataType.BYTE);
      return retVal;
   }
   
   /** -- Characters ------------------------------------------------------- */
   
   /**
    * Generates a hexadecimal hash of a char and/or its wrapper class.
    * 
    * @param  input
    *         char to be hashed.
    * @param  hashAlgorithm
    *         {@link HashAlgorithm} to be used to generate the hash.
    * @return hexadecimal hash of the input data.
    * @throws NoSuchAlgorithmException
    * @throws IllegalArgumentException
    */
   public static String createHash(char input, HashAlgorithm hashAlgorithm)
      throws NoSuchAlgorithmException, IllegalArgumentException
   {
      checkHashAlgoInput(hashAlgorithm);
      
      // Extract the byte array of the long
      ByteBuffer byteBuffer = ByteBuffer.allocate(CHAR_BYTES_SIZE);
      byteBuffer.putChar(input);
      byteBuffer.rewind();
      
      byte[] byteArray = new byte[CHAR_BYTES_SIZE];
      byteBuffer.get(byteArray);
      
      String retVal = bytesToHex(computeHashBytes(byteArray, hashAlgorithm));
      clearByteArray(byteArray);
      return retVal;
   }
   
   /**
    * Generates a hexadecimal hash of a char and/or its wrapper class.
    * 
    * @param  input
    *         char to be hashed.
    * @return hexadecimal hash of the input data.
    * @throws NoSuchAlgorithmException
    * @throws IllegalStateException
    */
   public String createHash(char input)
      throws NoSuchAlgorithmException, IllegalStateException
   {
      checkHashAlgoField();
      ByteBuffer byteBuffer = getByteBuffer(DataType.CHARACTER, CHAR_BYTES_SIZE);
      byteBuffer.putChar(input);
      byteBuffer.rewind();
      
      byte[] byteArray = getByteArray(DataType.CHARACTER, CHAR_BYTES_SIZE);
      byteBuffer.get(byteArray);
      
      String retVal = bytesToHex(computeHashBytes(byteArray));
      clearByteArray(DataType.CHARACTER);
      return retVal;
   }

   /** -- Shorts ----------------------------------------------------------- */
   
   /**
    * Generates a hexadecimal hash of a short and/or its wrapper class.
    * 
    * @param  input
    *         short to be hashed.
    * @param  hashAlgorithm
    *         {@link HashAlgorithm} to be used to generate the hash.
    * @return hexadecimal hash of the input data.
    * @throws NoSuchAlgorithmException
    * @throws IllegalArgumentException
    */
   public static String createHash(short input, HashAlgorithm hashAlgorithm)
      throws NoSuchAlgorithmException, IllegalArgumentException
   {
      checkHashAlgoInput(hashAlgorithm);
      
      // Extract the byte array of the long
      ByteBuffer byteBuffer = ByteBuffer.allocate(SHORT_BYTES_SIZE);
      byteBuffer.putShort(input);
      byteBuffer.rewind();
      
      byte[] byteArray = new byte[SHORT_BYTES_SIZE];
      byteBuffer.get(byteArray);
      
      String retVal = bytesToHex(computeHashBytes(byteArray, hashAlgorithm));
      clearByteArray(byteArray);
      return retVal;
   }
   
   /**
    * Generates a hexadecimal hash of a short and/or its wrapper class.
    * 
    * @param  input
    *         short to be hashed.
    * @return hexadecimal hash of the input data.
    * @throws NoSuchAlgorithmException
    * @throws IllegalStateException
    */
   public String createHash(short input)
      throws NoSuchAlgorithmException, IllegalStateException
   {
      checkHashAlgoField();
      ByteBuffer byteBuffer = getByteBuffer(DataType.SHORT, SHORT_BYTES_SIZE);
      byteBuffer.putShort(input);
      byteBuffer.rewind();
      
      byte[] byteArray = getByteArray(DataType.SHORT, SHORT_BYTES_SIZE);
      byteBuffer.get(byteArray);
      
      String retVal = bytesToHex(computeHashBytes(byteArray));
      clearByteArray(DataType.SHORT);
      return retVal;
   }
   
   /** -- Integers --------------------------------------------------------- */
   
   /**
    * Generates a hexadecimal hash of a int and/or its wrapper class.
    * 
    * @param  input
    *         int to be hashed.
    * @param  hashAlgorithm
    *         {@link HashAlgorithm} to be used to generate the hash.
    * @return hexadecimal hash of the input data.
    * @throws NoSuchAlgorithmException
    * @throws IllegalArgumentException
    */
   public static String createHash(int input, HashAlgorithm hashAlgorithm)
      throws NoSuchAlgorithmException, IllegalArgumentException
   {
      checkHashAlgoInput(hashAlgorithm);
      
      // Extract the byte array of the long
      ByteBuffer byteBuffer = ByteBuffer.allocate(INTEGER_BYTES_SIZE);
      byteBuffer.putInt(input);
      byteBuffer.rewind();
      
      byte[] byteArray = new byte[INTEGER_BYTES_SIZE];
      byteBuffer.get(byteArray);
      
      String retVal = bytesToHex(computeHashBytes(byteArray, hashAlgorithm));
      clearByteArray(byteArray);
      return retVal;
   }
   
   /**
    * Generates a hexadecimal hash of a int and/or its wrapper class.
    * 
    * @param  input
    *         int to be hashed.
    * @return hexadecimal hash of the input data.
    * @throws NoSuchAlgorithmException
    * @throws IllegalStateException
    */
   public String createHash(int input)
      throws NoSuchAlgorithmException, IllegalStateException
   {
      checkHashAlgoField();
      ByteBuffer byteBuffer = getByteBuffer(DataType.INTEGER, INTEGER_BYTES_SIZE);
      byteBuffer.putInt(input);
      byteBuffer.rewind();
      
      byte[] byteArray = getByteArray(DataType.INTEGER, INTEGER_BYTES_SIZE);
      byteBuffer.get(byteArray);
      
      String retVal = bytesToHex(computeHashBytes(byteArray));
      clearByteArray(DataType.INTEGER);
      return retVal;
   }
   
   /** -- Longs ------------------------------------------------------------ */
   
   /**
    * Generates a hexadecimal hash of a long and/or its wrapper class.
    * 
    * @param  input
    *         int to be hashed.
    * @param  hashAlgorithm
    *         {@link HashAlgorithm} to be used to generate the hash.
    * @return hexadecimal hash of the input data.
    * @throws NoSuchAlgorithmException
    * @throws IllegalArgumentException
    */
   public static String createHash(long input, HashAlgorithm hashAlgorithm)
      throws NoSuchAlgorithmException, IllegalArgumentException
   {
      checkHashAlgoInput(hashAlgorithm);
      
      // Extract the byte array of the long
      ByteBuffer byteBuffer = ByteBuffer.allocate(LONG_BYTES_SIZE);
      byteBuffer.putLong(input);
      byteBuffer.rewind();
      
      byte[] byteArray = new byte[LONG_BYTES_SIZE];
      byteBuffer.get(byteArray);
      
      String retVal = bytesToHex(computeHashBytes(byteArray, hashAlgorithm));
      clearByteArray(byteArray);
      return retVal;
   }
   
   /**
    * Generates a hexadecimal hash of a long and/or its wrapper class.
    * 
    * @param  input
    *         int to be hashed.
    * @return hexadecimal hash of the input data.
    * @throws NoSuchAlgorithmException
    * @throws IllegalStateException
    */
   public String createHash(long input)
      throws NoSuchAlgorithmException, IllegalStateException
   {
      checkHashAlgoField();
      ByteBuffer byteBuffer = getByteBuffer(DataType.LONG, LONG_BYTES_SIZE);
      byteBuffer.putLong(input);
      byteBuffer.rewind();
      
      byte[] byteArray = getByteArray(DataType.LONG, LONG_BYTES_SIZE);
      byteBuffer.get(byteArray);
      
      String retVal = bytesToHex(computeHashBytes(byteArray));
      clearByteArray(DataType.LONG);
      return retVal;
   }

   /** -- Floats ----------------------------------------------------------- */
   
   /**
    * Generates a hexadecimal hash of a long and/or its wrapper class.
    * 
    * @param  input
    *         long to be hashed.
    * @param  hashAlgorithm
    *         {@link HashAlgorithm} to be used to generate the hash.
    * @return hexadecimal hash of the input data.
    * @throws NoSuchAlgorithmException
    * @throws IllegalArgumentException
    */
   public static String createHash(float input, HashAlgorithm hashAlgorithm)
      throws NoSuchAlgorithmException, IllegalArgumentException
   {
      checkHashAlgoInput(hashAlgorithm);
      
      // Extract the byte array of the long
      ByteBuffer byteBuffer = ByteBuffer.allocate(FLOAT_BYTES_SIZE);
      byteBuffer.putFloat(input);
      byteBuffer.rewind();
      
      byte[] byteArray = new byte[FLOAT_BYTES_SIZE];
      byteBuffer.get(byteArray);
      
      String retVal = bytesToHex(computeHashBytes(byteArray, hashAlgorithm));
      clearByteArray(byteArray);
      return retVal;
   }
   
   /**
    * Generates a hexadecimal hash of a long and/or its wrapper class.
    * 
    * @param  input
    *         long to be hashed.
    * @return hexadecimal hash of the input data.
    * @throws NoSuchAlgorithmException
    * @throws IllegalStateException
    */
   public String createHash(float input)
      throws NoSuchAlgorithmException, IllegalStateException
   {
      checkHashAlgoField();
      ByteBuffer byteBuffer = getByteBuffer(DataType.FLOAT, FLOAT_BYTES_SIZE);
      byteBuffer.putFloat(input);
      byteBuffer.rewind();
      
      byte[] byteArray = getByteArray(DataType.FLOAT, FLOAT_BYTES_SIZE);
      byteBuffer.get(byteArray);
      
      String retVal = bytesToHex(computeHashBytes(byteArray));
      clearByteArray(DataType.FLOAT);
      return retVal;
   }
   
   /** -- Doubles ---------------------------------------------------------- */
   
   /**
    * Generates a hexadecimal hash of a double and/or its wrapper class.
    * 
    * @param  input
    *         double to be hashed.
    * @param  hashAlgorithm
    *         {@link HashAlgorithm} to be used to generate the hash.
    * @return hexadecimal hash of the input data.
    * @throws NoSuchAlgorithmException
    * @throws IllegalArgumentException
    */
   public static String createHash(double input, HashAlgorithm hashAlgorithm)
      throws NoSuchAlgorithmException, IllegalArgumentException
   {
      checkHashAlgoInput(hashAlgorithm);
      
      // Extract the byte array of the long
      ByteBuffer byteBuffer = ByteBuffer.allocate(DOUBLE_BYTES_SIZE);
      byteBuffer.putDouble(input);
      byteBuffer.rewind();
      
      byte[] byteArray = new byte[DOUBLE_BYTES_SIZE];
      byteBuffer.get(byteArray);
      
      String retVal = bytesToHex(computeHashBytes(byteArray, hashAlgorithm));
      clearByteArray(byteArray);
      return retVal;
   }
   
   /**
    * Generates a hexadecimal hash of a double and/or its wrapper class.
    * 
    * @param  input
    *         double to be hashed.
    * @return hexadecimal hash of the input data.
    * @throws NoSuchAlgorithmException
    * @throws IllegalStateException
    */
   public String createHash(double input)
      throws NoSuchAlgorithmException, IllegalStateException
   {
      checkHashAlgoField();
      ByteBuffer byteBuffer = getByteBuffer(DataType.DOUBLE, DOUBLE_BYTES_SIZE);
      byteBuffer.putDouble(input);
      byteBuffer.rewind();
      
      byte[] byteArray = getByteArray(DataType.DOUBLE, DOUBLE_BYTES_SIZE);
      byteBuffer.get(byteArray);
      
      String retVal = bytesToHex(computeHashBytes(byteArray));
      clearByteArray(DataType.DOUBLE);
      return retVal;
   }
   
   /** -- Strings ---------------------------------------------------------- */
   
   /**
    * Generates a hexadecimal hash of a double and/or its wrapper class.
    * 
    * @param  input
    *         String to be hashed.
    * @param  encoding
    *         valid @link {@link java.nio.charset.Charset} to be used when
    *         generating byte array from the
    *         {@link java.lang.String#getBytes(String)} method.
    * @param  hashAlgorithm
    *         {@link HashAlgorithm} to be used to generate the hash.
    * @return hexadecimal hash of the input data.
    * @throws UnsupportedEncodingException
    * @throws IllegalArgumentException
    * @throws NoSuchAlgorithmException
    */
   public static String createHash(String input,
         String encoding, HashAlgorithm hashAlgorithm)
         throws UnsupportedEncodingException, IllegalArgumentException,
         NoSuchAlgorithmException
   {
      checkHashAlgoInput(hashAlgorithm);

      // Generate a byte array from the input String.
      byte[] byteArray = input.getBytes(encoding);
      
      String retVal = bytesToHex(computeHashBytes(byteArray, hashAlgorithm));
      clearByteArray(byteArray);
      return retVal;
   }

   /**
    * Generates a hexadecimal hash of a double and/or its wrapper class.
    * 
    * @param  input
    *         String to be hashed.
    * @param  encoding
    *         valid @link {@link java.nio.charset.Charset} to be used when
    *         generating byte array from the
    *         {@link java.lang.String#getBytes(String)} method.
    * @return hexadecimal hash of the input data.
    * @throws UnsupportedEncodingException
    * @throws IllegalStateException
    * @throws NoSuchAlgorithmException
    */
   public String createHash(String input, String encoding)
         throws UnsupportedEncodingException, IllegalStateException,
         NoSuchAlgorithmException
   {
      checkHashAlgoField();
      if (null == encoding || encoding.isEmpty()) {
         throw new IllegalArgumentException(EMPTY_OR_NULL_ENCODING_ERR);
      }

      // Generate a byte array from the input String.
      byte[] byteArray = input.getBytes(encoding);
      
      String retVal = bytesToHex(computeHashBytes(byteArray));
      clearByteArray(byteArray);
      return retVal;
   }
   
   /** -- Character Arrays ------------------------------------------------- */
   
   public static String createHash(char[] input, HashAlgorithm hashAlgorithm)
      throws IllegalArgumentException, NoSuchAlgorithmException
   {
      checkHashAlgoInput(hashAlgorithm);
      
      // Calculate the length of the required ByteBuffer
      int elementLength = input.length * CHAR_BYTES_SIZE;
      ByteBuffer byteBuffer = ByteBuffer.allocate(elementLength);
      
      // For each char in the input array, add it's bytes to the buffer
      for (int i = 0; i < input.length; i++) {
         byteBuffer.putChar(input[i]);
      }
      
      byteBuffer.rewind();

      byte[] byteArray = new byte[elementLength];
      byteBuffer.get(byteArray);
      
      String retVal = bytesToHex(computeHashBytes(byteArray, hashAlgorithm));
      clearByteArray(byteArray);
      return retVal;
   }
   
   public String createHash(char[] input)
         throws IllegalStateException, NoSuchAlgorithmException
   {
      checkHashAlgoField();

      // Calculate the length of the required ByteBuffer
      int elementLength = input.length * CHAR_BYTES_SIZE;
      
      // We cannot reuse any existing array as each call to this
      // method can pass in a different sized array.  We could always
      // extend the class and add a method that takes a size argument
      // but that can wait for future development as needed.
      ByteBuffer byteBuffer = ByteBuffer.allocate(elementLength);
      
      // For each char in the input array, add it's bytes to the buffer
      for (int i = 0; i < input.length; i++) {
         byteBuffer.putChar(input[i]);
      }
      
      byteBuffer.rewind();
      
      // We will not be re-using the byte[] for the same reason that we are not
      // re-using the ByteBuffer instance above.
      byte[] byteArray = new byte[elementLength];
      byteBuffer.get(byteArray);
      
      String retVal = bytesToHex(computeHashBytes(byteArray));
      clearByteArray(byteArray);
      return retVal;
   }
   
   /** -- Utility Methods -------------------------------------------------- */
   
   /**
    * Returns a reference to a ByteBuffer instance stored in the
    * {@link #byteBufferMap}.  If the instance does not exist, it instantiates
    * it, adding it to the map.  If it already exists it clears the map
    * making it ready for the next iteration of usage.
    * 
    * @param  type
    *         the key in the {@link #byteBufferMap}.
    * @param  size
    *         number of bytes to allocate for a new ByteBuffer if required.
    * @return reference to a ByteBuffer instance
    */
   private ByteBuffer getByteBuffer(DataType type, int size) {
      ByteBuffer buffer = null;
      if (!byteBufferMap.containsKey(type)) {
         // If we do not already have a ByteBuffer instance for this key
         // instantiate one and add it to the map.
         buffer = ByteBuffer.allocate(size);
         byteBufferMap.put(type, buffer);
      } else {
         // Get the reference and prepare it for another use.
         buffer = byteBufferMap.get(type);
         buffer.clear();
      }
      return buffer;
   }
   
   /**
    * Returns a reference to a byte array that is stored in the
    * {@link #byteArrayMap}.  If the instance does not exist, it is allocated
    * and added to the map.  If it already exists, the array is cleared,
    * making it ready for the next iteration of usages.
    * 
    * @param  type
    *         the key in the {@link #byteArrayMap}.
    * @param  size
    *         the number of bytes to allocate for a new array if required.
    * @return reference to a byte[]
    */
   private byte[] getByteArray(DataType type, int size) {
      byte[] byteArray = null;
      if (!byteArrayMap.containsKey(type)) {
         // If we do not already have a byte[] for this key, allocate it and
         // add it to the map.
         byteArray = new byte[size];
         byteArrayMap.put(type, byteArray);
      } else {
         // Get a reference to the array and clear it.
         byteArray = byteArrayMap.get(type);
         
         for (int i = 0; i < size; i++) {
            byteArray[i] = 0x00;
         }
      }
      return byteArray;
   }
      
   /**
    * Computes the hashed bytes for the byte array representation of the input
    * data.  Enables the usage of an existing instance of a
    * {@link java.security.MessageDigest} instance.  To be used when thread
    * safety is handled by the client code.
    * 
    * @param inputBytes
    *        byte array of the data to be hashed.
    *        
    * @return the array of bytes representing the hashed data.
    */
   private byte[] computeHashBytes(byte[] inputBytes) throws NoSuchAlgorithmException {
      if (null == md) {
         md = MessageDigest.getInstance(hashAlgo.getAlgo());
      } else {
         md.reset();
      }
      md.update(inputBytes);
      return md.digest();
   }
   
   /**
    * Computers the hashed bytes for the byte array representation of the input
    * data.
    * 
    * @param  inputBytes
    *         byte array of the data to be hashed.
    * @param  hashAlgorithm
    *         algorithm to be used to calculate the hash.
    * @return the array of bytes representing the hashed data.
    */
   private static byte[] computeHashBytes(byte[] inputBytes, HashAlgorithm hashAlgorithm)
      throws NoSuchAlgorithmException
   {
      // Instantiate a MessageDigest instance configured with the desired
      // algorithm.
      MessageDigest msgDigest = MessageDigest.getInstance(hashAlgorithm.getAlgo());
      return msgDigest.digest(inputBytes);
   }
   
   /**
    * 
    * @param hashBytes
    * @return
    */
   public static String bytesToHex(byte[] hashBytes) {
      
      // Convert the hashBytes to a String of hex values
      StringBuilder retVal   = new StringBuilder();
      StringBuilder hexValue = new StringBuilder();
      
      for (byte hashByte : hashBytes) {
         // Flush our StringBuilder to be used as a container for the
         // hex value for each byte as it is read.
         hexValue.delete(0, hexValue.length());
         hexValue.append(Integer.toHexString(0xFF & hashByte));
         
         // Add a trailing '0' if our hexValue is only 1 char long
         if (hexValue.length() == 1) {
            hexValue.insert(0, '0');
         }
         retVal.append(hexValue);
      }
      return retVal.toString();
   }
   
   /**
    * Will set all of the bytes in the given byte array to 0x00.
    * 
    * @param  arr
    *         byte array to 'reset'.
    */
   private static void clearByteArray(byte[] arr) {
      for (int i = 0; i < arr.length; i++) {
         arr[i] = 0x00;
      }
   }
   
   /**
    * Will set all of the bytes in the byte array that is in the
    * internal byte array map, keyed by the DataType value passed in,
    * to 0x00.
    * 
    * @param  type
    *         value of the array in the byte array map to be 'reset'.
    */
   private void clearByteArray(DataType type) {
      byte[] arr = byteArrayMap.get(type);
      for (int i = 0; i < arr.length; i++) {
         arr[i] = 0x00;
      }
   }
   
   /**
    * Data types supported by the HashGenerator.
    * 
    * @author Ryan Chapin
    * @since  2015-03-16
    */
   public static enum DataType {
      BYTE,
      CHARACTER,
      SHORT,
      INTEGER,
      LONG,
      FLOAT,
      DOUBLE,
      STRING,
      CHARACTER_ARRAY
   }
   
   /**
    * Supported hashing algorithms.
    * 
    * @author Ryan Chapin
    * @since  2015-03-17
    */
   public static enum HashAlgorithm {
      MD2SUM("MD2"),
      MD5SUM("MD5"),
      SHA1SUM("SHA-1"),
      SHA256SUM("SHA-256"),
      SHA384SUM("SHA-384"),
      SHA512SUM("SHA-512");
      
      private String algo;
      
      public String getAlgo() {
         return algo;
      }
      
      private HashAlgorithm(String algo) {
         this.algo = algo;
      }
   }
}
