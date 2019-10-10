

rem delete dcop.jx.test dir ... 
rd /s /q "../../bin/com/dcop/jx/test"

rem build test ... 
javac -encoding UTF-8 ../../test/*.java -cp ../../release/dcop.jx.entry.jar;../../release/dcop.jx.core.jar -d ../../bin
jar cvfM ../../bin/dcop.jx.test.jar -C ../../bin ./com
rd /s /q ../../bin/com

rem run test ... 
cd "../../bin"
copy /Y ../release/*.jar ./
java -Djava.ext.dirs=./ -cp ./;"./dcop.jx.test.jar";"%JAVA_HOME%/jre/lib/ext/nashorn.jar" com.dcop.jx.test.Test
cd "../proj/build"

pause
