# !/bin/bash
# cp-common-build.sh to run ANT and setup environment variables

#Check for the JAVA_HOME environment variable
if [ -z "$JAVA_HOME" ] ; then
  echo Please set the JAVA_HOME environment variable!
  exit -1
fi

# Check for the ANT_HOME environment variable.
if [ -z "$ANT_HOME" ] ; then
  echo Please set the ANT_HOME environment variable!
  exit -2
fi

export ANT_ARGS=$ANT_ARGS"-lib ./lib"

ANT_CMD="$ANT_HOME/bin/ant --noconfig"
ANT_BUILD_FILE=cp-common-build.xml
ANT_TARGET=$1

# Get the fully-qualified Java class name to launch (run).
if [ "$ANT_TARGET" = "run" ] ; then
  export RUN_CLASS_NAME=$2
  # Validate the name of the Java class file.
  if [ -z "$RUN_CLASS_NAME" ] ; then
    echo "$RUN_CLASS_NAME" is not the name of a valid Java class to run.
    echo Please specify a fully-qualified class name to run!
    exit -3
  fi

  shift
  shift

  for arg in "$@"
  do
    COMMAND_LINE_ARGS=$COMMAND_LINE_ARGS" "$arg
  done

  echo "command line args: "$COMMAND_LINE_ARGS
  export COMMAND_LINE_ARGS
fi

# Get the name of the JUnit test class if it exists and set the TEST_CLASS_NAME environment variable.
if [ "$ANT_TARGET" == "test" -a -n "$2" ] ; then
  export TEST_CLASS_NAME=$2
else
  export TEST_CLASS_NAME=" "
fi

# Run ANT command
$ANT_CMD -buildfile $ANT_BUILD_FILE $ANT_TARGET
