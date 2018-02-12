/**
 * This software is released under the Revised BSD License.
 *
 * Copyright (c) 2015, Ryan Chapin, http:www.ryanchapin.com
 * All rights reserved.
 *
 * Redistribution  and  use  in  source  and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 * -   Redistributions  of  source  code  must  retain  the  above
 * copyright  notice,  this  list  of conditions and the following
 * disclaimer.
 *
 * -  Redistributions  in  binary  form  must  reproduce the above
 * copyright  notice,  this  list  of conditions and the following
 * disclaimer in the documentation and or other materials provided
 * with the distribution.
 *
 * -  Neither  the  name  of  Ryan  Chapin  nor  the  names of its
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * THIS   SOFTWARE  IS  PROVIDED  BY  THE  COPYRIGHT  HOLDERS  AND
 * CONTRIBUTORS  "AS  IS"  AND  ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING,  BUT  NOT  LIMITED  TO,  THE  IMPLIED  WARRANTIES OF
 * MERCHANTABILITY  AND  FITNESS  FOR  A  PARTICULAR  PURPOSE  ARE
 * DISCLAIMED.   IN   NO  EVENT  SHALL  RYAN  CHAPIN,  ANY  HEIRS,
 * SUCCESSORS,  EXECUTORS AND OR ASSIGNS BE LIABLE FOR ANY DIRECT,
 * INDIRECT,  INCIDENTAL,  SPECIAL,  EXEMPLARY,  OR  CONSEQUENTIAL
 * DAMAGES   (INCLUDING,   BUT  NOT  LIMITED  TO,  PROCUREMENT  OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS  INTERRUPTION)  HOWEVER  CAUSED  AND  ON ANY THEORY OF
 * LIABILITY,  WHETHER  IN  CONTRACT,  STRICT  LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE  OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */

package com.ryanchapin.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The <code>HashGenerator</code> is a class used for creating hexadecimal
 * hashes for multiple types of input data.
 * <p>
 * Supported input formats:
 * <ul>
 *    <li>byte</li>
 *    <li>{@link java.lang.Byte}</li>
 *    <li>char</li>
 *    <li>{@link java.lang.Character}</li>
 *    <li>short</li>
 *    <li>{@link java.lang.Short}</li>
 *    <li>int</li>
 *    <li>{@link java.lang.Integer}</li>
 *    <li>long</li>
 *    <li>{@link java.lang.Long}</li>
 *    <li>float</li>
 *    <li>{@link java.lang.Float}</li>
 *    <li>double</li>
 *    <li>{@link java.lang.Double}</li>
 *    <li>String</li>
 *    # TODO:  Add the additional arrays that are now supported
 *    <li>char[]</li>
 * </ul>
 * <p>
 * It supports any of the hash algorithms that are supported by the Java SE 8
 * {@link java.security.MessageDigest#digest()} class/method.  See the
 * MessageDigest section in the
 * <a href="http://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#MessageDigest" target="_blank">Java Cryptography Architecture Standard Algorithm Name Documentation</a>
 * for information about standard algorithm names.
 * <p>
 * NOTE: that the unit tests in this project <b>DO NOT</b> test the usage of the
 * <code>MD2</code> digest algorithm as it has not been included in openssl
 * since openssl-0.9.8m (2010-02-25), and is not in general use anymore.
 * <p>
 * The class is thread safe <b>depending on how it is instantiated and/or
 * called</b>.  Used in the following manner it is thread safe:
 *    <blockquote><pre>
 *    // Calling static methods
 *    String sha1Hash = HashGenerator.createHash("This is a test", "UTF-8", HashAlgorithm.SHA1SUM);
 * </pre></blockquote>
 * <p>
 * Used in the following manner thread safety must be taken into account by
 * the calling code:
 *    <blockquote><pre>
 *    // Calling member methods on a HashGenerator Instances
 *    HashGenerator hashGenerator = new HashGenerator(HashAlgorithm.SHA1SUM);
 *    String sha1Hash = hashGenerator.createHash("This is a test", "UTF-8");
 * </pre></blockquote>
 * <p>
 * When the <code>createHash</code> methods are called on a
 * <code>HashGenerator</code> instance, synchronization must be handled by the
 * calling code or their must only be a single thread making calls into the
 * <code>HashGenerator</code> instance.
 * <p>
 * The reason for this design is to enable the user to optimize for either
 * "built-in" synchronization (usage of the static methods), or optimize for
 * fewer Objects on the heap to be garbage collected.
 * <p>
 * In the case where there is a high rate and volume of calls to the
 * <code>HashGenerator</code> static methods, resulting in garbage collection
 * causing performance issues, the programmer can opt to instantiate a
 * <code>HashGenerator</code>.  Then calls to the instance can be limited to
 * a single thread, or the calling code can wrap the <code>HashGenerator</code>
 * in synchronized methods.
 * <p>
 * The <code>HashGenerator</code> can be used to hash sensitive data as
 * all intermediary data generated internally is explicitly wiped before the
 * method returns to the calling code.
 * <p>
 * <b>To use the HashGenerator to hash passwords</b>, use the methods
 * {@link HashGenerator#createHash(char[])} or
 * {@link HashGenerator#createHash(char[], HashAlgorithm)} as this enables the
 * caller to wipe the character array input by overwriting every element in the
 * array with <code>0x0</code> after creating a hash.
 * <p>
 * <b>DO NOT USE {@link java.lang.String}</b> as input data for hashing
 * passwords as <code>String</code> objects cannot be deterministically
 * overwritten or garbage collected by the JVM.
 * <p>
 * To hash PINs or other sensitive numeric data use any of the methods which
 * accept primitive types as input and make sure to use and pass in primitive
 * types and not their corollary wrapper classes.
 *
 * @since   1.0.0
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

   // TODO:  Change these to n.BYTES
   private static final int CHAR_BYTES_SIZE    = Character.BYTES;
   private static final int SHORT_BYTES_SIZE   = Short.BYTES;
   private static final int INTEGER_BYTES_SIZE = Integer.BYTES;
   private static final int LONG_BYTES_SIZE    = Long.BYTES;
   private static final int FLOAT_BYTES_SIZE   = Float.BYTES;
   private static final int DOUBLE_BYTES_SIZE  = Double.BYTES;

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

//   private static Map<DataType, Supplier<ByteBuffer>> bufferSuppliers = getBufferSuppliers();

   private static Map<DataType, Function<? extends Object[], ByteBuffer>> getBufferSuppliers() {
      Map<DataType, Function<? extends Object[], ByteBuffer>> bufferSuppliers = new HashMap<>();

      Function<Character[], ByteBuffer> buffFunct = (c) -> {
         ByteBuffer b = ByteBuffer.allocate(c.length);
         for (int i = 0; i < c.length; i++) {
            b.putChar(c[i]);
         }
         return b;
      };

      bufferSuppliers.put(DataType.CHARACTER_ARRAY, buffFunct);

//      Supplier<ByteBuffer> bufferSupplier = () -> {
//         ByteBuffer byteBuffer = ByteBuffer.allocate(elementLength);
//         // ByteBuffer byteBuffer = getByteBuffer(DataType.CHARACTER,
//         // elementLength);
//         for (int i = 0; i < input.length; i++) {
//            byteBuffer.putChar(input[i]);
//         }
//         return byteBuffer;

      return bufferSuppliers;
   }

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
    *         if the <code>HashGenerator</code> instance has not yet been
    *         configured with a valid {@link HashAlgorithm} enum.
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
    *         if the hashAlgo argument is an invalid {@link HashAlgorithm}.
    */
   private static void checkHashAlgoInput(HashAlgorithm hashAlgo)
         throws IllegalArgumentException
   {
      // TODO:  Shouldn't we actually validate the algo?
      if (null == hashAlgo) {
         throw new IllegalArgumentException("No hashing algorithm was provided.");
      }
   }

   /**
    *
    * @param type
    * @param size
    * @return
    */
   private Function<ByteBuffer, byte[]> createByteArrayFunction(DataType type, int size) {
     Function<ByteBuffer, byte[]> retVal = (buffer) -> {
       // We have to determine if the DataType for which we are hashing is an
       // array of values.  If so, we will not attempt to re-use a byte array
       // of a fixed size, since, in this case the number of elements that
       // we will be hashing is variable.  If we need to optimize that we can
       // later.
       byte[] arr = null;
       if (type.isArray()) {
         arr = new byte[size];
       } else {
         arr = getByteArray(type, size);
       }
       buffer.get(arr);
       return arr;
     };
     return retVal;
   }

   /**
    *
    * @param size
    * @return
    */
   private static Function<ByteBuffer, byte[]> createByteArrayFunction(int size) {
     Function<ByteBuffer, byte[]> retVal = (buffer) -> {
       // TODO: Determine if we need to do type checking if the input is an array
       // probably not...
       byte[] arr = new byte[size];
       buffer.get(arr);
       return arr;
     };
     return retVal;
   }

   /**
    *
    * @param buffSupplier
    * @param byteArrFunction
    * @param hashAlgorithm
    * @return
    * @throws NoSuchAlgorithmException
    * @throws IllegalStateException
    */
   private static String createHash(Supplier<ByteBuffer> buffSupplier,
                                   Function<ByteBuffer, byte[]> byteArrFunction,
                                   HashAlgorithm hashAlgorithm)
       throws NoSuchAlgorithmException, IllegalStateException
   {
     checkHashAlgoInput(hashAlgorithm);

     ByteBuffer buffer = buffSupplier.get();
     buffer.rewind();
     byte[] bytes = byteArrFunction.apply(buffer);
     String retVal = bytesToHex(computeHashBytes(bytes, hashAlgorithm));
     clearByteArray(bytes);

     return retVal;
   }

   /**
    *
    * @param buffSupplier
    * @param byteArrFunction
    * @param type
    * @return
    * @throws NoSuchAlgorithmException
    * @throws IllegalStateException
    */
   private String createHash(Supplier<ByteBuffer> buffSupplier,
                             Function<ByteBuffer, byte[]> byteArrFunction,
                             DataType type)
       throws NoSuchAlgorithmException, IllegalStateException
   {
     checkHashAlgoField();

     ByteBuffer buffer = buffSupplier.get();
     buffer.rewind();
     byte[] bytes = byteArrFunction.apply(buffer);
     String retVal = bytesToHex(computeHashBytes(bytes));
     if (type.isArray()) {
        clearByteArray(bytes);
     } else {
        clearByteArray(type);
     }

     return retVal;
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
    * @throws IllegalArgumentException
    *         if the hashAlgo argument is an invalid {@link HashAlgorithm}.
    * @throws NoSuchAlgorithmException
    *         if the hashAlgo argument is an invalid {@link HashAlgorithm}.
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
    * @throws IllegalStateException
    *         if the <code>HashGenerator</code> instance has not yet been
    *         configured with a valid {@link HashAlgorithm} enum.
    * @throws NoSuchAlgorithmException
    *         if the <code>HashGenerator</code> instance has not yet been
    *         configured with a valid {@link HashAlgorithm} enum.
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
    * @throws IllegalArgumentException
    *         if the hashAlgo argument is an invalid {@link HashAlgorithm}.
    * @throws NoSuchAlgorithmException
    *         if the hashAlgo argument is an invalid {@link HashAlgorithm}.
    */
   public static String createHash(char input, HashAlgorithm hashAlgorithm)
       throws NoSuchAlgorithmException, IllegalStateException
   {
     Supplier<ByteBuffer> bufferSupplier = () -> {
       ByteBuffer byteBuffer = ByteBuffer.allocate(CHAR_BYTES_SIZE);
       byteBuffer.putChar(input);
       return byteBuffer;
     };

     return createHash(
        bufferSupplier,
        createByteArrayFunction(CHAR_BYTES_SIZE),
        hashAlgorithm);
   }

   /**
    * Generates a hexadecimal hash of a char and/or its wrapper class.
    *
    * @param  input
    *         char to be hashed.
    * @return hexadecimal hash of the input data.
    * @throws IllegalStateException
    *         if the <code>HashGenerator</code> instance has not yet been
    *         configured with a valid {@link HashAlgorithm} enum.
    * @throws NoSuchAlgorithmException
    *         if the <code>HashGenerator</code> instance has not yet been
    *         configured with a valid {@link HashAlgorithm} enum.
    */
   public String createHash(char input)
       throws NoSuchAlgorithmException, IllegalStateException
   {
     Supplier<ByteBuffer> bufferSupplier = () -> {
       ByteBuffer byteBuffer = getByteBuffer(DataType.CHARACTER, CHAR_BYTES_SIZE);
       byteBuffer.putChar(input);
       return byteBuffer;
     };

     return createHash(
        bufferSupplier,
        createByteArrayFunction(DataType.CHARACTER, CHAR_BYTES_SIZE),
        DataType.CHARACTER);
   }


   public static String createHash(char[] input, HashAlgorithm hashAlgorithm)
      throws IllegalArgumentException, NoSuchAlgorithmException
   {
      // Calculate the length of the required ByteBuffer
      final int elementLength = input.length * CHAR_BYTES_SIZE;

      Supplier<ByteBuffer> bufferSupplier = () -> {
         ByteBuffer byteBuffer = ByteBuffer.allocate(elementLength);

         // For each char in the input array, add it's bytes to the buffer
         for (int i = 0; i < input.length; i++) {
            byteBuffer.putChar(input[i]);
         }
         return byteBuffer;
      };

      return createHash(
         bufferSupplier,
         createByteArrayFunction(elementLength),
         hashAlgorithm);
   }

   public String createHash(char[] input)
      throws NoSuchAlgorithmException, IllegalStateException
   {
      final int elementLength = input.length * CHAR_BYTES_SIZE;

      Supplier<ByteBuffer> bufferSupplier = () -> {
         // We cannot reuse any existing array as each call to this
         // method can pass in a different sized array. We could always
         // extend the class and add a method that takes a size argument
         // but that can wait for future development as needed.
         ByteBuffer byteBuffer = ByteBuffer.allocate(elementLength);
         for (int i = 0; i < input.length; i++) {
            byteBuffer.putChar(input[i]);
         }
         return byteBuffer;
      };

      return createHash(
         bufferSupplier,
         createByteArrayFunction(DataType.CHARACTER_ARRAY, elementLength),
         DataType.CHARACTER_ARRAY);
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
    * @throws IllegalArgumentException
    *         if the hashAlgo argument is an invalid {@link HashAlgorithm}.
    * @throws NoSuchAlgorithmException
    *         if the hashAlgo argument is an invalid {@link HashAlgorithm}.
    */
   public static String createHash(short input, HashAlgorithm hashAlgorithm)
      throws NoSuchAlgorithmException, IllegalArgumentException
   {
      checkHashAlgoInput(hashAlgorithm);

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
    * @throws IllegalStateException
    *         if the <code>HashGenerator</code> instance has not yet been
    *         configured with a valid {@link HashAlgorithm} enum.
    * @throws NoSuchAlgorithmException
    *         if the <code>HashGenerator</code> instance has not yet been
    *         configured with a valid {@link HashAlgorithm} enum.
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
    * @throws IllegalArgumentException
    *         if the hashAlgo argument is an invalid {@link HashAlgorithm}.
    * @throws NoSuchAlgorithmException
    *         if the hashAlgo argument is an invalid {@link HashAlgorithm}.
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
    * @throws IllegalStateException
    *         if the <code>HashGenerator</code> instance has not yet been
    *         configured with a valid {@link HashAlgorithm} enum.
    * @throws NoSuchAlgorithmException
    *         if the <code>HashGenerator</code> instance has not yet been
    *         configured with a valid {@link HashAlgorithm} enum.
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
    * @throws IllegalArgumentException
    *         if the hashAlgo argument is an invalid {@link HashAlgorithm}.
    * @throws NoSuchAlgorithmException
    *         if the hashAlgo argument is an invalid {@link HashAlgorithm}.
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
    * @throws IllegalStateException
    *         if the <code>HashGenerator</code> instance has not yet been
    *         configured with a valid {@link HashAlgorithm} enum.
    * @throws NoSuchAlgorithmException
    *         if the <code>HashGenerator</code> instance has not yet been
    *         configured with a valid {@link HashAlgorithm} enum.
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
    * @throws IllegalArgumentException
    *         if the hashAlgo argument is an invalid {@link HashAlgorithm}.
    * @throws NoSuchAlgorithmException
    *         if the hashAlgo argument is an invalid {@link HashAlgorithm}.
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
    * @throws IllegalStateException
    *         if the <code>HashGenerator</code> instance has not yet been
    *         configured with a valid {@link HashAlgorithm} enum.
    * @throws NoSuchAlgorithmException
    *         if the <code>HashGenerator</code> instance has not yet been
    *         configured with a valid {@link HashAlgorithm} enum.
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
    * @throws IllegalArgumentException
    *         if the hashAlgo argument is an invalid {@link HashAlgorithm}.
    * @throws NoSuchAlgorithmException
    *         if the hashAlgo argument is an invalid {@link HashAlgorithm}.
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
    * @throws IllegalStateException
    *         if the <code>HashGenerator</code> instance has not yet been
    *         configured with a valid {@link HashAlgorithm} enum.
    * @throws NoSuchAlgorithmException
    *         if the <code>HashGenerator</code> instance has not yet been
    *         configured with a valid {@link HashAlgorithm} enum.
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
    * Generates a hexadecimal hash of a String.
    *
    * @param  input
    *         String to be hashed.
    * @param  encoding
    *         valid {@link java.nio.charset.StandardCharsets} constant to be
    *         used when generating byte array from the
    *         {@link java.lang.String#getBytes(String)} method.
    * @param  hashAlgorithm
    *         {@link HashAlgorithm} to be used to generate the hash.
    * @return hexadecimal hash of the input data.
    * @throws UnsupportedEncodingException
    *         if the encoding <code>String</code> argument is not a valid
    *         encoding.
    * @throws IllegalArgumentException
    *         if the hashAlgo argument is an invalid {@link HashAlgorithm}.
    * @throws NoSuchAlgorithmException
    *         if the hashAlgo argument is an invalid {@link HashAlgorithm}.
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
    * Generates a hexadecimal hash of a String.
    *
    * @param  input
    *         String to be hashed.
    * @param  encoding
    *         valid {@link java.nio.charset.Charset} to be used when
    *         generating byte array from the
    *         {@link java.lang.String#getBytes(String)} method.
    * @return hexadecimal hash of the input data.
    * @throws UnsupportedEncodingException
    *         if the encoding <code>String</code> argument is not a valid
    *         encoding.
    * @throws IllegalStateException
    *         if the <code>HashGenerator</code> instance has not yet been
    *         configured with a valid {@link HashAlgorithm} enum.
    * @throws NoSuchAlgorithmException
    *         if the <code>HashGenerator</code> instance has not yet been
    *         configured with a valid {@link HashAlgorithm} enum.
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

   /**
    * Generates a hexadecimal hash of a character array.
    *
    * @param  input
    *         char[] to be hashed
    * @param  hashAlgorithm
    *         {@link HashAlgorithm} to be used to generate the hash.
    * @return hexadecimal hash of the input data.
    * @throws IllegalArgumentException
    *         if the hashAlgo argument is an invalid {@link HashAlgorithm}.
    * @throws NoSuchAlgorithmException
    *         if the hashAlgo argument is an invalid {@link HashAlgorithm}.
    */
//   public static String createHash(char[] input, HashAlgorithm hashAlgorithm)
//      throws IllegalArgumentException, NoSuchAlgorithmException
//   {
//      checkHashAlgoInput(hashAlgorithm);
//
//      // Calculate the length of the required ByteBuffer
//      int elementLength = input.length * CHAR_BYTES_SIZE;
//      ByteBuffer byteBuffer = ByteBuffer.allocate(elementLength);
//
//      // For each char in the input array, add it's bytes to the buffer
//      for (int i = 0; i < input.length; i++) {
//         byteBuffer.putChar(input[i]);
//      }
//
//      byteBuffer.rewind();
//
//      byte[] byteArray = new byte[elementLength];
//      byteBuffer.get(byteArray);
//
//      String retVal = bytesToHex(computeHashBytes(byteArray, hashAlgorithm));
//      clearByteArray(byteArray);
//      return retVal;
//   }

   /**
    * Generates a hexadecimal hash of a character array.
    *
    * @param  input
    *         char[] to be hashed
    * @return hexadecimal hash of the input data.
    * @throws IllegalStateException
    *         if the <code>HashGenerator</code> instance has not yet been
    *         configured with a valid {@link HashAlgorithm} enum.
    * @throws NoSuchAlgorithmException
    *         if the <code>HashGenerator</code> instance has not yet been
    *         configured with a valid {@link HashAlgorithm} enum.
    */
//   public String createHash(char[] input)
//         throws IllegalStateException, NoSuchAlgorithmException
//   {
//      checkHashAlgoField();
//
//      // Calculate the length of the required ByteBuffer
//      int elementLength = input.length * CHAR_BYTES_SIZE;
//
//      // We cannot reuse any existing array as each call to this
//      // method can pass in a different sized array.  We could always
//      // extend the class and add a method that takes a size argument
//      // but that can wait for future development as needed.
//      ByteBuffer byteBuffer = ByteBuffer.allocate(elementLength);
//
//      // For each char in the input array, add it's bytes to the buffer
//      for (int i = 0; i < input.length; i++) {
//         byteBuffer.putChar(input[i]);
//      }
//
//      byteBuffer.rewind();
//
//      // We will not be re-using the byte[] for the same reason that we are not
//      // re-using the ByteBuffer instance above.
//      byte[] byteArray = new byte[elementLength];
//      byteBuffer.get(byteArray);
//
//      String retVal = bytesToHex(computeHashBytes(byteArray));
//      clearByteArray(byteArray);
//      return retVal;
//   }

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
    * @throws NoSuchAlgorithmException
    *         if the <code>HashGenerator</code> instance has not yet been
    *         configured with a valid {@link HashAlgorithm} enum.
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
    * Computes the hashed bytes for the byte array representation of the input
    * data.
    *
    * @param  inputBytes
    *         byte array of the data to be hashed.
    * @param  hashAlgorithm
    *         algorithm to be used to calculate the hash.
    * @return the array of bytes representing the hashed data.
    * @throws NoSuchAlgorithmException
    *         if the hashAlgo argument is an invalid {@link HashAlgorithm}.
    */
   private static byte[] computeHashBytes(byte[] inputBytes, HashAlgorithm hashAlgorithm)
      throws NoSuchAlgorithmException
   {
      MessageDigest msgDigest = MessageDigest.getInstance(hashAlgorithm.getAlgo());
      return msgDigest.digest(inputBytes);
   }

   /**
    * Generates a hexadecimal String representation of the hashed bytes.
    *
    * @param  hashBytes
    *         byte[] output from the
    *         {@link java.security.MessageDigest#digest()} method.
    * @return hexadecimal representation of the hashed bytes.
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
    * @since  1.0.0
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
      BYTE_ARRAY(true),
      CHARACTER_ARRAY(true),
      SHORT_ARRAY(true),
      INTEGER_ARRAY(true),
      LONG_ARRAY(true),
      FLOAT_ARRAY(true),
      DOUBLE_ARRAY(true),
      STRING_ARRAY(true);

      private final boolean array;

      public boolean isArray() {
         return array;
      }

      DataType() {
         this.array = false;
      }

      DataType(boolean array) {
         this.array = array;
      }
   }

   /**
    * Supported hashing algorithms.
    *
    * @since  1.0.0
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
