# Java Hash Generation Library

**By:** Ryan Chapin [Contact Info](http://www.ryanchapin.com/contact.html)

Hash-Generator is a Java library for generating hashes for various types of input data.

It supports any of the hash algorithms that are supported by the Java SE 7 [MessageDigest.getInstance(String algorithm)](http://docs.oracle.com/javase/7/docs/api/java/security/MessageDigest.html#getInstance%28java.lang.String%29) class/method.  See the MessageDigest section in the [Java Cryptography Architecture Standard Algorithm Name Documentation](http://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#MessageDigest) for information about standard algorithm names.

## Building a Distribution

To build, simply run the following command in the hash-generator directory

```
# mvn clean package
```

This will build the project and create a distribution jar in the target/ directory as expected.

## To Include In Your Project

(add pom declaration)

## Development Environment Set-up

This project was developed with Eclipse Kepler, but can be compiled from the command line with maven.

The pom specifies _Java 1.7_.

### To set up to develop, test, and run from within Eclipse (requires m2e and m2e slf4j Eclipse plugins)

1. From within Eclipse, go to _File -> Import_ and select, _Maven -> Existing Maven Projects_ and click _Next_.
2. Then browse to the cloned hash-generator/ dir and select it.
3. Set up the log4j configurations:
	- Go to _Window -> Preferences_ and click on _Run/Debug -> String Substitution_.
	- Add the following String variable
		- **HASHGEN_LOG_PATH** The fully qualified path to the directory into which you want to write log files while running from eclipse.
	- From within Eclipse, go to _Window -> Preferences -> Java -> Installed JREs_
	- Select your current default 1.7 JRE and copy it
	- Give it some unique name that allows you to associate it with this project.
	- Add the following to the Default VM arguments: `-Dlog.file.path=${HASHGEN_LOG_PATH}`
	- Click _Finish_
	- Right-click on the ddiff project in the Package Explorer and select _Build Path -> Configure Build Path_
	- Click on the _Libraries tab_ and then click on the _Add Library_ button
	- Select _JRE System Library_ and then _Next_
	- Select the radio button next to _Alternate JRE:_ and select the ddiff configured JRE
	- Click _Finish_, and then _OK_

### Running from within Eclipse

TBD

