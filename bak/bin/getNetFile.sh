CLASS_NAME=com.codeprimate.tools.GetNetFile

JAVA_CMD="$JAVA_HOME/bin/java"

LOCAL_CLASSPATH=".:dist/cp-common.jar:lib/commons-beanutils.jar:lib/commons-collections.jar:lib/commons-logging.jar:lib/junit.jar:lib/log4j-1.2.13.jar:lib/servlet.jar:lib/struts.jar"

#echo $JAVA_CMD -Xmx512M -classpath "$LOCAL_CLASSPATH" $CLASS_NAME "$1" "$2"

$JAVA_CMD -Xmx512M -cp "$LOCAL_CLASSPATH" $CLASS_NAME "$1" "$2"
