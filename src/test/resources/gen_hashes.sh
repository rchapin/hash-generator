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

# Text data files extension
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

CODE_OUT_FILE_NAME_PREFIX="TestData"

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
now=$(date +%Y-%d-%m_%H%M%S_%N)
output_dir=$OUTPUT_PATH_PARENT_DIR/gen_hashes_output_$now
tmp_dir=$output_dir/tmp
code_output_file=$output_dir/$CODE_OUTFILE
ddt "tmp_dir = $tmp_dir"
ddt "output_dir = $output_dir"
mkdir -p $tmp_dir

declare -A code_output_files

echo "Generating test data, writing output to $output_dir"

# compile the java class
javac $JAVA_SRC_PATH/$JAVA_SRC_FILE

# ensure that the java program exited properly
javac_retval=$?

if [ 0 -ne $javac_retval ];
then
   echo "Unable to compile java program, javac exited with $javac_retval"
   exit
fi

# run the class
java $JAVA_PKG.$JAVA_CLASS_NAME $tmp_dir

# ensure that the java program exited properly
java_retval=$?

if [ 0 -ne $java_retval ];
then
   echo "Java program $JAVA_CLASS_NAME exited with $java_retval"
   echo "Check for failures and re-run"
   exit
fi

# Counter for each HashTestData instance
instance_counter=0

for in_file in `ls $tmp_dir/*${BIN_FILE_EXT}`
do
   for hash_algo in $HASH_ALGOS
   do
      echo -n "."
      ddt "-------------------------------------------"
      ddt "Hashing file '$in_file' with '$hash_algo' algo"

      # The output of this command will be similar to:
      # f1d3ff8443297732862df21dc4e57262 */var/tmp/gen_hashes_output_2018-17-01_044821_503953361/tmp/double_5.bin
      hash_output=`$hash_algo -b $in_file`

      ddt "hash_output = $hash_output"

      # Split the output with the default IFS as the delimiter into the hashed
      # value and the class name with the identifier for the data
      read -r hash ctype_var_type_id <<< "$hash_output"

      # Trim the path from the front and the '.bin' from the end of the TYPE_VALUE String
      ctype_var_type_id=$(echo "$ctype_var_type_id" | awk -F\/ '{print $NF}' | sed 's/....$//g')
      ddt "hash = $hash, ctype_var_type_id = $ctype_var_type_id"

      # Split the ctype_var_type_id with the '_' delimiter to glean the data type,
      # the var_type, and the id of the file
      OIFS="$IFS"
      IFS="_"
      read -r ctype var_type id <<< "$ctype_var_type_id"
      IFS="$OIFS"

      ddt "ctype     = $ctype"
      ddt "var_type = $var_type"
      ddt "id       = $id"

      # Generate a file name and path to the output file for this ctype/var_type
      # combination.
      class_suffix=${ctype}${var_type}
      code_output_tmp_file_name="$CODE_OUT_FILE_NAME_PREFIX${class_suffix}.tmp"
      code_output_tmp_file_path=$tmp_dir/$code_output_tmp_file_name
      code_output_file_name="$CODE_OUT_FILE_NAME_PREFIX${class_suffix}.java"
      code_output_file_path=$tmp_dir/$code_output_file_name
      code_output_import_file_path=$tmp_dir/imports_${class_suffix}
    
      htd_name="htd${ctype}${var_type}${instance_counter}"
      ddt "htd_name = $htd_name"

      hash_algo_enum=$(echo "$hash_algo" | tr [:lower:] [:upper:])
      ddt "hash_algo_enum = $hash_algo_enum"

      # Extract the ASCII value of the data
      ascii_file=$tmp_dir/${ctype_var_type_id}${TXT_FILE_EXT}
      ddt "ascii_file = $ascii_file"
      ascii_value=$(cat $ascii_file)
      ddt "ascii_value = $ascii_value"

      ddt "outputting code to $code_output_tmp_file_path"
      # Generate the Java code to instantiate this test data object
      case $var_type in

      "Scalar")
        htd_type="HashTestData"

        cat <<EOF > $code_output_import_file_path

import java.util.ArrayList;
import java.util.List;
EOF

        cat <<EOF >> $code_output_tmp_file_path

      $htd_type<? extends Object> $htd_name = new $htd_type<$ctype>(
         new ${ctype}(${ascii_value}),
         "$hash",
         HashAlgorithm.${hash_algo_enum});
      list.add($htd_name);
EOF
         ;;

      "Array")
        htd_type="HashTestDataList"

        cat <<EOF > $code_output_import_file_path

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
EOF

        cat <<EOF >> $code_output_tmp_file_path

      $htd_type<? extends Object> $htd_name = new $htd_type<$ctype>(
         ${ascii_value},
         "$hash",
         HashAlgorithm.${hash_algo_enum});
      list.add($htd_name);
EOF
         ;;

      esac

      # If we have not already started code of this type create the entry in
      # the associative array.
      ddt "class_suffix = $class_suffix"
      if [ ! ${code_output_files[$class_suffix]+_} ];
      then
        ddt "We have not yet started outputting code for $class_suffix"
        # We will split tuple on the '|' when outputting the java code
        code_output_files[$class_suffix]="$code_output_tmp_file_path|$htd_type|$code_output_import_file_path"
      fi

      # Increment the counter
      instance_counter=$(($instance_counter+1))

   done

done

#
# For each of the types of code, we have written, compose that into the
# Java class.
#
ddt "code_output_files = ${!code_output_files[@]}"
for test_data_type in "${!code_output_files[@]}";
do
   val="${code_output_files[$test_data_type]}"
   tmp_file_path=$(echo "$val" | awk -F\| '{print $1}')
   htd_type=$(echo "$val" | awk -F\| '{print $2}')
   imports=$(echo "$val" | awk -F\| '{print $3}')
   code_output_file_name="$CODE_OUT_FILE_NAME_PREFIX${test_data_type}.java"
   code_output_file_path=$output_dir/$code_output_file_name
   ddt "test_data_type = $test_data_type, tmp_file_path = $tmp_file_path"
   ddt "code_output_file_path = $code_output_file_path"
   ddt "htd_type = $htd_type"
   ddt "imports = $imports"
 
   code=$(cat $tmp_file_path)
   
   echo "package com.ryanchapin.util;" >> $code_output_file_path
   cat $imports >> $code_output_file_path
   cat <<EOF >> $code_output_file_path

import com.ryanchapin.util.HashGenerator.HashAlgorithm;
import com.ryanchapin.util.HashGeneratorTest.$htd_type;

public class TestData${test_data_type} { 

   public static List<$htd_type<? extends Object>> list =
      new ArrayList<$htd_type<? extends Object>>();

   static {
EOF

  cat $tmp_file_path >> $code_output_file_path

  cat <<EOF >> $code_output_file_path
   }
}
EOF

done

echo "."
echo " ========================================================================"
echo " Finished generating test data to $code_output_file"
