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
# modified: 2018-01-16
# version:  2.00
#
# #############################################################################
# CONFIGURATIONS
#
# Default output path parent dir
DEFAULT_OUTPUT_PATH_PARENT_DIR=/var/tmp

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
# CODE_OUTFILE=generated_test_data.java
CODE_OUTFILE_SCALAR=generated_test_data_scalar.java
CODE_OUTFILE_ARRAY=generated_test_data_array.java

################################################################################
#
ME=`basename "$0"`

################################################################################
# USAGE:

function about {
   cat << EOF
$ME - generate test data for the HashGenerator tests.
EOF
}

function usage {
   cat << EOF
Usage: $ME [OPTIONS]

By default it will automatically delete the intermediary binary and ASCII files
generated.  This step can be bypassed by using the -l option.

Options:

  -o OUTPUT_PATH_PARENT_DIR
      override the default parent dir changing it from
      $DEFAULT_OUTPUT_PATH_PARENT_DIR to a directory of your choice.

  -h HELP
      Outputs this basic usage information.

  -v VERBOSE
      Output additional feedback/information during runtime.
EOF
}

################################################################################
#
# A function to provide verbose output during runtime.
#
function ddt {
   if [ "$VERBOSE" -eq 1 ];
   then
      echo "$1"
   fi
}

################################################################################
#
# Here we define variables to store the input from the command line arguments as
# well as define the default values.
#
OUTPUT_PATH_PARENT_DIR=0
HELP=0
VERBOSE=0

PARSED_OPTIONS=`getopt -o hvo: -- "$@"`

# Check to see if the getopts command failed
if [ $? -ne 0 ];
then
   echo "Failed to parse arguments"
   exit 1
fi

eval set -- "$PARSED_OPTIONS"

# Loop through all of the options with a case statement
while true; do
   case "$1" in
      -o)
         OUTPUT_PATH_PARENT_DIR=$2
         shift 2
         ;;

      -h)
         HELP=1
         shift
         ;;

      -v)
         VERBOSE=1
         shift
         ;;

      --)
         shift
         break
         ;;
   esac
done

if [ "$HELP" -eq 1 ];
then
   usage
   exit
fi

ddt "Running $ME with options:"
ddt "  OUTPUT_PATH_PARENT_DIR = $OUTPUT_PATH_PARENT_DIR"

if [ "$OUTPUT_PATH_PARENT_DIR" == 0 ]
then
 OUTPUT_PATH_PARENT_DIR=$DEFAULT_OUTPUT_PATH_PARENT_DIR
fi

#
# Check that the output path arg is an actual directory and is writable
#
if [[ ! -d "$OUTPUT_PATH_PARENT_DIR" || ! -w "$OUTPUT_PATH_PARENT_DIR" ]];
then
   echo "ERROR: OUTPUT_PATH_PARENT_DIR [$OUTPUT_PATH_PARENT_DIR] did not point to a writable directory"
   usage
   exit 1
fi

# #############################################################################
#
# Create the output directory
NOW=$(date +%Y-%d-%m_%H%M%S_%N)
output_dir=$OUTPUT_PATH_PARENT_DIR/gen_hashes_output_$NOW
tmp_dir=$output_dir/tmp
code_output_file=$output_dir/$CODE_OUTFILE
code_output_file_scalar=$output_dir/$CODE_OUTFILE_SCALAR
code_output_file_array=$output_dir/$CODE_OUTFILE_ARRAY
ddt "tmp_dir = $tmp_dir"
ddt "output_dir = $output_dir"
mkdir -p $tmp_dir

echo "Generating test data, writing output to $output_dir"

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
java $JAVA_PKG.$JAVA_CLASS_NAME $tmp_dir

# ensure that the java program exited properly
JAVA_RETVAL=$?

if [ 0 -ne $JAVA_RETVAL ];
then
   echo "Java program $JAVA_CLASS_NAME exited with $JAVA_RETVAL"
   echo "Check for failures and re-run"
   exit
fi

# Now generate hashes for each of the bin files
# if [ -e "$code_output_file" ];
# then
#    rm $code_output_file
# fi

# Counter for each HashTestData instance
INSTANCE_COUNTER=0

for FILE in `ls $tmp_dir/*${BIN_FILE_EXT}`
do
   for HASH_ALGO in $HASH_ALGOS
   do
      echo -n "."
      ddt "-------------------------------------------"
      ddt "Hashing file '$FILE' with '$HASH_ALGO' algo"

      # The output of this command will be similar to:
      # f1d3ff8443297732862df21dc4e57262 */var/tmp/gen_hashes_output_2018-17-01_044821_503953361/tmp/double_5.bin
      HASH_OUTPUT=`$HASH_ALGO -b $FILE`

      ddt "HASH_OUTPUT = $HASH_OUTPUT"

      # Split the output with the default IFS as the delimiter into the hashed
      # value and the class name with the identifier for the data
      read -r HASH TYPE_VAR_TYPE_ID <<< "$HASH_OUTPUT"

      # Trim the path from the front and the '.bin' from the end of the TYPE_VALUE String
      TYPE_VAR_TYPE_ID=$(echo "$TYPE_VAR_TYPE_ID" | awk -F\/ '{print $NF}' | sed 's/....$//g')
      ddt "HASH = $HASH, TYPE_VAR_TYPE_ID = $TYPE_VAR_TYPE_ID"

      # Split the TYPE_VAR_TYPE_ID with the '_' delimiter to glean the data type,
      # the var_type, and the id of the file
      OIFS="$IFS"
      IFS="_"
      read -r TYPE VAR_TYPE ID <<< "$TYPE_VAR_TYPE_ID"
      IFS="$OIFS"

      ddt "TYPE     = $TYPE"
      ddt "VAR_TYPE = $VAR_TYPE"
      ddt "ID       = $ID"

      TYPE_LC=$(echo "$TYPE" | tr [:upper:] [:lower:] )

      LIST_NAME="${TYPE_LC}${VAR_TYPE}List"
      ddt "LIST_NAME = $LIST_NAME"

      HTD_NAME="htd${TYPE}${VAR_TYPE}${INSTANCE_COUNTER}"
      ddt "HTD_NAME = $HTD_NAME"

      HASH_ALGO_ENUM=$(ddt "$HASH_ALGO" | tr [:lower:] [:upper:])
      ddt "HASH_ALGO_ENUM = $HASH_ALGO_ENUM"

      # Extract the ASCII value of the data
      ASCII_FILE=$tmp_dir/${TYPE_VAR_TYPE_ID}${TXT_FILE_EXT}
      ddt "ASCII_FILE = $ASCII_FILE"
      ASCII_VALUE=$(cat $ASCII_FILE)
      ddt "ASCII_VALUE = $ASCII_VALUE"

      #
      # Write out the Java code to instantiate this test data object
      #
      case $VAR_TYPE in

      "Scalar")
         cat <<EOF >> $code_output_file_scalar

HashTestData<? extends Object> $HTD_NAME = new HashTestData<$TYPE>(
   new ${TYPE}(${ASCII_VALUE}),
   "$HASH",
   HashAlgorithm.${HASH_ALGO_ENUM});
${LIST_NAME}.add($HTD_NAME);
EOF
         ;;

      "Array")
         cat <<EOF >> $code_output_file_array

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

echo "."
echo " ========================================================================"
echo " Finished generating test data to $code_output_file"

