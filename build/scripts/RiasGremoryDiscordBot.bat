@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  RiasGremoryDiscordBot startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and RIAS_GREMORY_DISCORD_BOT_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args
if "%@eval[2+2]" == "4" goto 4NT_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*
goto execute

:4NT_args
@rem Get arguments from the 4NT Shell from JP Software
set CMD_LINE_ARGS=%$

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\RiasGremoryDiscordBot-1.0.jar;%APP_HOME%\lib\JDA-3.6.0_354.jar;%APP_HOME%\lib\lavaplayer-1.2.36.jar;%APP_HOME%\lib\slf4j-nop-1.7.24.jar;%APP_HOME%\lib\spotify-web-api-java-1.5.0.jar;%APP_HOME%\lib\commons-beanutils-20030211.134440.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\slf4j-api-1.7.25.jar;%APP_HOME%\lib\commons-collections4-4.1.jar;%APP_HOME%\lib\json-20160810.jar;%APP_HOME%\lib\trove4j-3.0.3.jar;%APP_HOME%\lib\jna-4.4.0.jar;%APP_HOME%\lib\nv-websocket-client-2.2.jar;%APP_HOME%\lib\okhttp-3.8.1.jar;%APP_HOME%\lib\lavaplayer-common-1.0.4.jar;%APP_HOME%\lib\httpclient-4.5.2.jar;%APP_HOME%\lib\commons-io-2.5.jar;%APP_HOME%\lib\jackson-core-2.8.5.jar;%APP_HOME%\lib\jackson-databind-2.8.5.jar;%APP_HOME%\lib\jsoup-1.10.1.jar;%APP_HOME%\lib\base64-2.3.9.jar;%APP_HOME%\lib\protobuf-java-2.5.0.jar;%APP_HOME%\lib\commons-httpclient-3.1.jar;%APP_HOME%\lib\json-lib-2.4-jdk15.jar;%APP_HOME%\lib\guava-18.0.jar;%APP_HOME%\lib\okio-1.13.0.jar;%APP_HOME%\lib\httpcore-4.4.4.jar;%APP_HOME%\lib\commons-logging-1.2.jar;%APP_HOME%\lib\commons-codec-1.9.jar;%APP_HOME%\lib\jackson-annotations-2.8.0.jar;%APP_HOME%\lib\commons-collections-3.2.1.jar;%APP_HOME%\lib\commons-lang-2.5.jar;%APP_HOME%\lib\ezmorph-1.0.6.jar

@rem Execute RiasGremoryDiscordBot
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %RIAS_GREMORY_DISCORD_BOT_OPTS%  -classpath "%CLASSPATH%" com.knifesurge.riasgremory.RiasGremoryBot %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable RIAS_GREMORY_DISCORD_BOT_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%RIAS_GREMORY_DISCORD_BOT_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
