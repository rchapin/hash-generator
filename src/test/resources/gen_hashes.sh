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

# Counter used to increment the variable names generated
VAR_COUNTER=0

for FILE in `ls *${BIN_FILE_EXT}`
do
   for HASH_ALGO in $HASH_ALGOS
   do
      echo "Hashing file '$FILE' with '$HASH_ALGO' algo"
      VAR_COUNTER=$(($VAR_COUNTER+1))
      echo "VAR_COUNTER = $VAR_COUNTER"

      # The output of this command will be similar to:
      # f1d3ff8443297732862df21dc4e57262 *java.lang.Integer.0.bin
      HASH_OUTPUT=`$HASH_ALGO -b $FILE`

      # Split the output with the default IFS as the delimiter into the hashed
      # value and the class name with the identifier for the data
      read -r HASH TYPE_VALUE <<< "$HASH_OUTPUT"

      # Get the 3rd and 2nd to the last tokens in the string, delimited by '.'
      TYPE_ID=$(echo "$TYPE_VALUE" | awk -F\. '{
         type_element=NF - 2
         id_element=NF - 1
         print $type_element " " $id_element
      }')

      read -r TYPE ID <<< "$TYPE_ID"
      echo "TYPE = $TYPE"
      echo "ID = $ID"

      LIST_NAME=$(PREFIX=$(echo "$TYPE" | tr [:upper:] [:lower:]); echo "${PREFIX}List")
      echo "LIST_NAME = $LIST_NAME"

      HTD_NAME="htd${TYPE}${VAR_COUNTER}"
      echo "HTD_NAME = $HTD_NAME" 

      HASH_ALGO_ENUM=$(echo "$HASH_ALGO" | tr [:lower:] [:upper:])

      cat <<EOF >> $CODE_OUTFILE

HashTestData<? extends Object> htdString1 = new HashTestData<String>(
   $ID,
   $HASH,
   HashAlgo.${HASH_ALGO_ENUM});

EOF

   done
   
done

exit 0














