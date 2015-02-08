
rem É¾³ýJXÄ¿Â¼
rd /s /q ..\..\release\JX

rem É¾³ýJX.jar
del /s /q ..\..\release\JX.jar

rem ±àÒë
javac ../../res/*.java -classpath ../../release -d ../../release
javac ../../src/*.java -classpath ../../release -d ../../release
javac ../../src/Framework/*.java -classpath ../../release -d ../../release
javac ../../src/Framework/Service/Msg/*.java -classpath ../../release -d ../../release
javac ../../src/Framework/Service/Sock/*.java -classpath ../../release -d ../../release
javac ../../src/Framework/Object/*.java -classpath ../../release -d ../../release
javac ../../src/Extend/Common/User/*.java -classpath ../../release -d ../../release

rem ´ò°ü
jar cvfM ../../release/JX.jar -C ../../release ./JX
