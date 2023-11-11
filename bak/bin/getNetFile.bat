@echo off

setlocal

set CLASS_NAME=com.codeprimate.tools.GetNetFile

set LOCAL_CLASSPATH=".;dist/cp-common.jar;lib/commons-beanutils.jar;lib/commons-collections.jar;lib/commons-logging.jar;lib/junit.jar;lib/log4j-1.2.13.jar;lib/servlet.jar;lib/struts.jar"

set JAVA_CMD=%JAVA_HOME%\bin\java

rem echo %JAVA_CMD% -Xmx512M -classpath %LOCAL_CLASSPATH% %CLASS_NAME% "%1" "%2"

%JAVA_CMD% -Xmx512M -classpath "%LOCAL_CLASSPATH%" %CLASS_NAME% "%1" "%2"

endlocal
