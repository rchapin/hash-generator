#!/bin/bash

# #############################################################################
#
# Will compile and run the TestDataGenerator.java class which will generate
# binary data files for various data types and values and then generate a hash
# for each of the supported hash algorithms.  The output will be used to build
# the test cases
#
# name:     gen_hashes.sh
# author:   Ryan Chapin
# created:  2015-03-12
# modified: 2016-12-07
# version:  1.00
#
# #############################################################################
# CONFIGURATIONS
#
# Binary data files extension
BIN_FILE_EXT=".bin"

# Text data files extensin
TXT_FILE_EXT=".txt"

# Array of hash commands to run
HASH_ALGOS="md5sum sha1sum sha256sum sha384sum sha512sum"

# Java source file path
JAVA_SRC_PATH=com/ryanchapin/util

# Java source file
JAVA_SRC_FILE=TestDataGenerator.java

# Java package
JAVA_PKG=com.ryanchapin.util

# Java source class name
JAVA_CLASS_NAME=TestDataGenerator

# Hashes outfile
CODE_OUTFILE=generated_test_data.java

# #############################################################################

# First delete any existing binary and text data files
rm *${BIN_FILE_EXT}
rm *${TXT_FILE_EXT}

# compile the java class
javac $JAVA_SRC_PATH/$JAVA_SRC_FILE

# ensure that the java program exited properly
JAVAC_RETVAL=$?

if [ 0 -ne $JAVAC_RETVAL ];
then
   echo "Unable to compile java program, javac exited with $JAVAC_RETVAL"
   exit
fi

# run the class
java $JAVA_PKG.$JAVA_CLASS_NAME

# ensure that the java program exited properly
JAVA_RETVAL=$?

if [ 0 -ne $JAVA_RETVAL ];
then
   echo "Java program $JAVA_CLASS_NAME exited with $JAVA_RETVAL"
   echo "Check for failures and re-run"
   exit
fi


exit


# Now generate hashes for each of the bin files
if [ -e "$CODE_OUTFILE" ];
then
   rm $CODE_OUTFILE
fi

# Counter for each HashTestData instance
INSTANCE_COUNTER=0

for FILE in `ls *${BIN_FILE_EXT}`
do
   for HASH_ALGO in $HASH_ALGOS
   do
      echo "-------------------------------------------"
      echo "Hashing file '$FILE' with '$HASH_ALGO' algo"

      # The output of this command will be similar to:
      # f1d3ff8443297732862df21dc4e57262 *double_5.bin
      HASH_OUTPUT=`$HASH_ALGO -b $FILE`

      echo "HASH_OUTPUT = $HASH_OUTPUT"

      # Split the output with the default IFS as the delimiter into the hashed
      # value and the class name with the identifier for the data
      read -r HASH TYPE_VAR_TYPE_ID <<< "$HASH_OUTPUT"


      # Trim the '*' from the front and the '.bin' from the end of the TYPE_VALUE String
      TYPE_VAR_TYPE_ID=$(echo "$TYPE_VAR_TYPE_ID" | cut -c 2- | sed 's/....$//g')
      echo "HASH = $HASH, TYPE_VAR_TYPE_ID = $TYPE_VAR_TYPE_ID"


      # Split the TYPE_VAR_TYPE_ID with the '_' delimiter to glean the data type,
      # the var_type, and the id of the file
      OIFS="$IFS"
      IFS="_"
      read -r TYPE VAR_TYPE ID <<< "$TYPE_VAR_TYPE_ID"
      IFS="$OIFS"

      echo "TYPE     = $TYPE"
      echo "VAR_TYPE = $VAR_TYPE"
      echo "ID       = $ID"

      TYPE_LC=$(echo "$TYPE" | tr [:upper:] [:lower:] )

      LIST_NAME="${TYPE_LC}${VAR_TYPE}List"
      echo "LIST_NAME = $LIST_NAME"

      HTD_NAME="htd${TYPE}${VAR_TYPE}${INSTANCE_COUNTER}"
      echo "HTD_NAME = $HTD_NAME"

      HASH_ALGO_ENUM=$(echo "$HASH_ALGO" | tr [:lower:] [:upper:])
      echo "HASH_ALGO_ENUM = $HASH_ALGO_ENUM"

      # Extract the ASCII value of the data
      ASCII_FILE=${TYPE_VAR_TYPE_ID}${TXT_FILE_EXT}
      ASCII_VALUE=$(cat $ASCII_FILE)
      echo "ASCII_VALUE = $ASCII_VALUE"

      #
      # Write out the Java code to instantiate this test data object
      #
      case $VAR_TYPE in

      "Scalar")
         cat <<EOF >> $CODE_OUTFILE

HashTestData<? extends Object> $HTD_NAME = new HashTestData<$TYPE>(
   new ${TYPE}(${ASCII_VALUE}),
   "$HASH",
   HashAlgorithm.${HASH_ALGO_ENUM});
${LIST_NAME}.add($HTD_NAME);
EOF
         ;;

      "Array")
         cat <<EOF >> $CODE_OUTFILE

HashTestDataList<? extends Object> $HTD_NAME = new HashTestDataList<$TYPE>(
   ${ASCII_VALUE},
   "$HASH",
   HashAlgorithm.${HASH_ALGO_ENUM});
${LIST_NAME}.add($HTD_NAME);
EOF
         ;;
      esac

      # Increment the counter
      INSTANCE_COUNTER=$(($INSTANCE_COUNTER+1))

   done

done

exit 0














