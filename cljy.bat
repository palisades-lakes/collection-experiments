@echo off
:: palisades dot lakes (at) gmail (dot) com
:: 2017-12-15

::set GC=-XX:+AggressiveHeap -XX:+UseStringDeduplication 
set GC=

set COMPRESSED=
::set COMPRESSED=-XX:CompressedClassSpaceSize=3g 

set TRACE=
::set TRACE=-XX:+PrintGCDetails -XX:+TraceClassUnloading -XX:+TraceClassLoading

::set PROF=
set PROF=-agentpath:"C:\Program Files\YourKit Java Profiler 2017.02-b66\bin\win64\yjpagent.dll"

::set THRUPUT=-d64 -server -XX:+AggressiveOpts 
set THRUPUT=-d64 -server
::set THRUPUT=

set XMX=-Xms12g -Xmx12g -Xmn5g 

set OPENS=--add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/java.util.concurrent.atomic=ALL-UNNAMED
set CP=-cp ./src/scripts/clojure;lib/*

set JAVA_HOME=%JAVA9%
set JAVA="%JAVA_HOME%\bin\java"

set CMD=%JAVA% %THRUPUT% -ea -dsa -Xbatch %GC% %PROF% %XMX% %COMPRESSED% %TRACE% %OPENS% %CP% clojure.main %*
::echo %CMD%
%CMD%
