#!/bin/sh
#  This file is part of the DITA Open Toolkit project hosted on 
#  Sourceforge.net. See the accompanying license.txt file for 
#  applicable licenses.
#  (c) Copyright IBM Corp. 2006 All Rights Reserved.

set -e

BUILD_DIR="`pwd`"
DITA_BIN_DIR="DITA-OT1.5.4"
DITA_BIN_DOWNLOAD_URL="http://downloads.sourceforge.net/project/dita-ot/DITA-OT%20Stable%20Release/DITA%20Open%20Toolkit%201.5.4/DITA-OT1.5.4_full_easy_install_bin.zip?r=http%3A%2F%2Fsourceforge.net%2Fprojects%2Fdita-ot%2Ffiles%2FDITA-OT%2520Stable%2520Release%2FDITA%2520Open%2520Toolkit%25201.5.4%2FDITA-OT1.5.4_full_easy_install_bin.zip%2Fdownload&ts=1344888729&use_mirror=kent"
DITA_BIN_DOWNLOAD_NAME="DITA-OT1.5.4_full_easy_install_bin.zip"
TEMP_DIR="temp"
OUTPUT_FILE="$TEMP_DIR/build-output.txt"

if [ ! -d "$DITA_BIN_DIR" ]; then
    echo "Downloading DITA bin..."
    curl -L -o $DITA_BIN_DOWNLOAD_NAME $DITA_BIN_DOWNLOAD_URL
    unzip $DITA_BIN_DOWNLOAD_NAME
fi

cd "$DITA_BIN_DIR"

# Get the absolute path of DITAOT's home directory
DITA_DIR="`pwd`"

if [ -f "$DITA_DIR"/tools/ant/bin/ant ] && [ ! -x "$DITA_DIR"/tools/ant/bin/ant ]; then
    chmod +x "$DITA_DIR"/tools/ant/bin/ant
fi

export ANT_OPTS="-Xmx512m $ANT_OPTS"
export ANT_OPTS="$ANT_OPTS -Djavax.xml.transform.TransformerFactory=net.sf.saxon.TransformerFactoryImpl"
export ANT_HOME="$DITA_DIR"/tools/ant
export PATH="$DITA_DIR"/tools/ant/bin:"$PATH"

NEW_CLASSPATH="$DITA_DIR/lib:$DITA_DIR/lib/dost.jar:$DITA_DIR/lib/commons-codec-1.4.jar:$DITA_DIR/lib/resolver.jar:$DITA_DIR/lib/icu4j.jar"
NEW_CLASSPATH="$DITA_DIR/lib/saxon/saxon9.jar:$DITA_DIR/lib/saxon/saxon9-dom.jar:$NEW_CLASSPATH"
if test -n "$CLASSPATH"
then
export CLASSPATH="$NEW_CLASSPATH":"$CLASSPATH"
else
export CLASSPATH="$NEW_CLASSPATH"
fi

cd "$BUILD_DIR"

TRANSFORM_TYPE="$1"
if [ -z "$TRANSFORM_TYPE" ]
then
	TRANSFORM_TYPE="xhtml"
fi

mkdir -p $TEMP_DIR
ant mm.$TRANSFORM_TYPE > $OUTPUT_FILE
