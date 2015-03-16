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
# version:  0.01
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

# Java source file
JAVA_SRC_FILE=TestDataGenerator.java

# Java source class name
JAVA_CLASS_NAME=TestDataGenerator

# Hashes outfile
CODE_OUTFILE=generated_test_data.java

# #############################################################################

# First delete any existing binary and text data files
rm *${BIN_FILE_EXT}
rm *${TXT_FILE_EXT}

# compile the java class
javac $JAVA_SRC_FILE

# ensure that the java program exited properly
JAVAC_RETVAL=$?

if [ 0 -ne $JAVAC_RETVAL ];
then
   echo "Unable to compile java program, javac exited with $JAVAC_RETVAL"
   exit
fi

# run the class
java $JAVA_CLASS_NAME

# ensure that the java program exited properly
JAVA_RETVAL=$?

if [ 0 -ne $JAVA_RETVAL ];
then
   echo "Java program $JAVA_CLASS_NAME exited with $JAVA_RETVAL"
   echo "Check for failures and re-run"
   exit
fi


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
      echo "Hashing file '$FILE' with '$HASH_ALGO' algo"

      # The output of this command will be similar to:
      # f1d3ff8443297732862df21dc4e57262 *double_5.bin
      HASH_OUTPUT=`$HASH_ALGO -b $FILE`

      echo "HASH_OUTPUT = $HASH_OUTPUT"

      # Split the output with the default IFS as the delimiter into the hashed
      # value and the class name with the identifier for the data
      read -r HASH TYPE_AND_ID <<< "$HASH_OUTPUT"


      # Trim the '*' from the front and the '.bin' from the end of the TYPE_VALUE String
      TYPE_AND_ID=$(echo "$TYPE_AND_ID" | cut -c 2- | sed 's/....$//g')
      echo "HASH = $HASH, TYPE_AND_ID = $TYPE_AND_ID"


      # Split the TYPE_AND_ID with the '_' delimiter to glean the type by itself
      OIFS="$IFS"
      IFS="_"
      read -r TYPE ID <<< "$TYPE_AND_ID"
      IFS="$OIFS"

      echo "TYPE = $TYPE"
      echo "ID = $ID"

      TYPE_LC=$(echo "$TYPE" | tr [:upper:] [:lower:] )

      LIST_NAME="${TYPE_LC}List"
      echo "LIST_NAME = $LIST_NAME"

      HTD_NAME="htd_${TYPE}_${INSTANCE_COUNTER}"
      echo "HTD_NAME = $HTD_NAME" 

      HASH_ALGO_ENUM=$(echo "$HASH_ALGO" | tr [:lower:] [:upper:])
      echo "HASH_ALGO_ENUM = $HASH_ALGO_ENUM"

      # Extract the ASCII value of the data
      ASCII_FILE=${TYPE_AND_ID}${TXT_FILE_EXT}
      ASCII_VALUE=$(cat $ASCII_FILE)

      cat <<EOF >> $CODE_OUTFILE

HashTestData<? extends Object> $HTD_NAME = new HashTestData<$TYPE>(
   new ${TYPE}(${ASCII_VALUE}),
   "$HASH",
   HashAlgo.${HASH_ALGO_ENUM});
${LIST_NAME}.add($HTD_NAME);
EOF

      # Increment the counter
      INSTANCE_COUNTER=$(($INSTANCE_COUNTER+1))

   done
   
done

exit 0














