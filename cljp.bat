@echo off
:: palisades.lakes (at) gmail (dot) com
:: 2017-10-07

::set GC=-XX:+AggressiveHeap -XX:+UseStringDeduplication 
set GC=

set COMPRESSED=
::set COMPRESSED=-XX:CompressedClassSpaceSize=3g 

set TRACE=
::set TRACE=-XX:+PrintGCDetails -XX:+TraceClassUnloading -XX:+TraceClassLoading

::set PROF=
::set PROF=-agentlib:hprof=cpu=samples,interval=1,depth=64,thread=n,doe=y
set PROF=-agentlib:hprof=cpu=samples,interval=1,depth=128

::set THRUPUT=-d64 -server -XX:+AggressiveOpts 
set THRUPUT=-d64 -server
::set THRUPUT=

::set XMX=-Xms29g -Xmx29g -Xmn11g 
set XMX=-Xms12g -Xmx12g -Xmn5g 

set CP=-cp ./src/scripts/clojure;lib/*
set JAVA="%JAVA_HOME%\bin\java"

set CMD=%JAVA% %THRUPUT% -ea -dsa -Xbatch %GC% %PROF% %XMX% %COMPRESSED% %TRACE% %CP% clojure.main %*
::echo %CMD%
%CMD%
