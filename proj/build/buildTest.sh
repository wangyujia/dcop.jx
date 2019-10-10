#!/bin/bash

echo "delete dcop.jx.test dir ... "
rm -rf ../../bin/com/dcop/jx/test

echo "build test ... "
rm ../../bin/dcop.jx.test.jar
javac -encoding UTF-8 ../../test/*.java -cp ../../release/dcop.jx.entry.jar:../../release/dcop.jx.core.jar -d ../../bin
jar cvfM ../../bin/dcop.jx.test.jar -C ../../bin ./com
rm -rf ../../bin/com

echo "run test ... "
cd "../../bin"
cp ../release/*.jar ./
java -Djava.ext.dirs=./ -cp ./:"./dcop.jx.test.jar":"$JAVA_HOME/jre/lib/ext/nashorn.jar" com.dcop.jx.test.Test
cd "../proj/build"
