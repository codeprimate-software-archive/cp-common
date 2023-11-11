@echo off
rem build.bat to run ANT and setup environment variables.
setlocal enableextensions

rem Check for the JAVA_HOME environment variable.
if not exits %JAVA_HOME% goto: :no_java_home

rem Check for the ANT_HOME environment variable.
if not exist %ANT_HOME% goto :no_ant_home

set ANT_ARGS=%ANT_ARGS%"-lib ./lib"
set ANT_CMD=%ANT_HOME%\bin\ant
set ANT_BUILD_FILE=cp-common-build.xml
set ANT_TARGET=%1

rem Specifies the fully-qualified class name of Java class to run.
if "%ANT_TARGET%"=="run" set RUN_CLASS_NAME=%2
if "%ANT_TARGET%"=="run" if "%RUN_CLASS_NAME%"=="" goto :invalid_run_class_name

rem Get the name of the JUnit test class if it exists and set the QUICK_TEST_NAME environment variable.
if "%ANT_TARGET%"=="test" set TEST_CLASS_NAME=%2

rem Run ANT
:run_ant_cmd
%ANT_CMD% -buildfile %ANT_BUILD_FILE% %ANT_TARGET%
goto :exit

:no_java_home
echo Please set the JAVA_HOME environment variable!
goto :exit

:no_ant_home
echo Please set the ANT_HOME environment variable!
goto :exit

:invalid_run_class_name
echo "%RUN_CLASS_NAME%" is not the name of a valid Java class to run.
echo Please specify a fully-qualified class name to run!
goto :exit

:exit
endlocal
