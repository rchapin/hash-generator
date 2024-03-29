# Java Hash Generation Library

**By:** Ryan Chapin [Contact Info](http://www.ryanchapin.com/contact.html)

The HashGenerator is a Java library for creating hexadecimal hashes for multiple types of input data.

[Version 1.1.0 is available via The Central Repository](http://search.maven.org/#artifactdetails%7Ccom.ryanchapin.util%7Chashgenerator%7C1.1.0%7Cjar).

Supported input formats:

- byte
- Byte
- char
- Character
- short
- Short
- int
- Integer
- long
- Long
- float
- Float
- double
- Double
- String
- char[]

It supports any of the hash algorithms that are supported by the Java SE 8 [MessageDigest.getInstance(String algorithm)](http://docs.oracle.com/javase/8/docs/api/java/security/MessageDigest.html#getInstance%28java.lang.String%29) class/method.  See the MessageDigest section in the [Java Cryptography Architecture Standard Algorithm Name Documentation](http://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#MessageDigest) for information about standard algorithm names.


NOTE: that the unit tests in this project DO NOT test the usage of the MD2 digest algorithm as it has not been included in openssl since openssl-0.9.8m (2010-02-25), and is not in general use anymore.

The class is thread safe depending on how it is instantiated and/or called. Used in the following manner it is thread safe:

```
// Calling static methods
String sha1Hash = HashGenerator.createHash(
                    "This is a test", "UTF-8", HashAlgorithm.SHA1SUM);
```

Used in the following manner thread safety must be taken into account by the calling code:

```
// Calling member methods on a HashGenerator Instance
HashGenerator hashGenerator = new HashGenerator(HashAlgorithm.SHA1SUM);
String sha1Hash = hashGenerator.createHash("This is a test", "UTF-8");
```

When the createHash methods are called on a HashGenerator instance, synchronization must be handled by the calling code or their must only be a single thread making calls into the HashGenerator instance.

The reason for this design is to enable the user to optimize for either "built-in" synchronization (usage of the static methods), or optimize for fewer Objects on the heap to be garbage collected.

In the case where there is a high rate and volume of calls to the HashGenerator static methods, resulting in garbage collection causing performance issues, the programmer can opt to instantiate a HashGenerator. Then calls to the instance can be limited to a single thread, or the calling code can wrap the HashGenerator in synchronized methods.

The HashGenerator can be used to hash sensitive data as all intermediary data generated internally is explicitly wiped before the method returns to the calling code.

**To use the HashGenerator to hash passwords**, use the methods ```createHash(char[])``` or ```createHash(char[], HashAlgorithm)``` as this enables the caller to wipe the character array input by overwriting every element in the array with ```0x0``` after creating a hash.

**DO NOT USE String as input data for hashing passwords** as String objects cannot be deterministically overwritten or garbage collected by the JVM.

To hash PINs or other sensitive numeric data use any of the methods which accept primitive types as input and make sure to use and pass in primitive types and not their corollary wrapper classes.


## Building a Distribution

To build, simply run the following command in the hash-generator directory

```
# mvn clean package
```

This will build the project and create the jar in the target/ directory as expected.  There is a profile, ```release```, which is activeByDefault and which will build javadoc and source jars.  To disable, and speed up builds during development build as follows:

```
# mvn -P\!release clean package
```

### Running Tests

#### Dependency Check

The `org.owasp:depedency-check-maven` plugin is included in the pom.  To run the dependency check, execute the following
```
mvn dependency-check:check
```

#### Running Tests
```
mvn test
```
If you want to see the log output from the test
```
mvn -Dlog4j2.configurationFile=log4j.xml test
```

#### Failures running test ```shouldCorrectlyHashScalarStaticMultiThreaded```

If you encounter problems with resource starvation while running the aforementioned test you can add the following JVM properties to your ```mvn test|compile|package``` invocation to reduce the number of threads instantiated for the test, and/or the number of iterations that each thread runs for the test:

```-DhashGen.multithread.test.numThreads=n -DhashGen.multithread.test.numIter=n```

For more details about this test, check the source for the ```shouldCorrectlyHashScalarStaticMultiThreaded``` method.

## To Include In Your Project

Either execute a ```maven build install``` if you do not have access to the Internet, or [version 1.1.1 is available via The Central Repository](http://search.maven.org/#artifactdetails%7Ccom.ryanchapin.util%7Chashgenerator%7C1.0.0%7Cjar).

## Development Environment Set-up

This project was developed with Eclipse Kepler, but can be compiled from the command line with maven.

The pom specifies _Java 1.7_, and the project required JDK 1.7 for development.

### To set up to develop, test, and run from within Eclipse (requires m2e and m2e slf4j Eclipse plugins)

1. From within Eclipse, go to _File -> Import_ and select, _Maven -> Existing Maven Projects_ and click _Next_.
2. Then browse to the cloned hash-generator/ dir and select it.

### Modifying or Adding Additional Test Data

All of the test data is generated via a combination of a Java program and a shell script.

Test data generation requirements:

- Linux, \*nix, or other systems with bash
- The following hash programs installed, callable by the following strings:
   - md5sum
   - sha1sum
   - sha256sum
   - sha384sum
   - sha512sum

The test data is generated by the following:

```src/test/resources/TestDataGenerator.java``` : This class defines the test data that will be generated.  It will then write out the test data in both binary and in ASCII text.

```src/test/resources/gen_hashes.sh```: This shell script with compile and run the aformentioned java class, generating hashes of the binary data files against each of the hash algorithms listed above, and will then dynamically generate the Java code to instantiate each of the test data instances to then be copied into the ```src/test/java/com/ryanchapin/util/hashgenerator/HashGeneratorTestData.java``` class.

