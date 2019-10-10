#!/bin/bash


echo "delete dcop.jx dir ... "
rm -rf ../../release/com





echo "delete dcop.jx.entry.jar ... "
rm ../../release/dcop.jx.entry.jar

echo "build entry class ... "
javac -encoding UTF-8 ../../interface/*.java -cp ../../release -d ../../release
javac -encoding UTF-8 ../../interface/base/*.java -cp ../../release -d ../../release
javac -encoding UTF-8 ../../interface/kernel/*.java -cp ../../release -d ../../release
javac -encoding UTF-8 ../../interface/extend/access/*.java -cp ../../release -d ../../release
javac -encoding UTF-8 ../../interface/extend/jjs/*.java -cp ../../release -d ../../release

echo "make entry jar ... "
jar cvfM ../../release/dcop.jx.entry.jar -C ../../release ./com

echo "delete all release ... "
rm -rf ../../release/com






echo "delete dcop.jx.core.jar ... "
rm ../../release/dcop.jx.core.jar

echo "build core class ... "
javac -encoding UTF-8 ../../src/base/msg/*.java -cp ../../release:../../release/dcop.jx.entry.jar -d ../../release
javac -encoding UTF-8 ../../src/base/pkg/*.java -cp ../../release:../../release/dcop.jx.entry.jar -d ../../release
javac -encoding UTF-8 ../../src/base/log/*.java -cp ../../release:../../release/dcop.jx.entry.jar -d ../../release
javac -encoding UTF-8 ../../src/base/sock/*.java -cp ../../release:../../release/dcop.jx.entry.jar -d ../../release
javac -encoding UTF-8 ../../src/base/file/*.java -cp ../../release:../../release/dcop.jx.entry.jar -d ../../release
javac -encoding UTF-8 ../../src/kernel/*.java -cp ../../release:../../release/dcop.jx.entry.jar -d ../../release
javac -encoding UTF-8 ../../src/extend/access/*.java -cp ../../release:../../release/dcop.jx.entry.jar -d ../../release
javac -encoding UTF-8 ../../src/extend/jjs/*.java -cp ../../release:../../release/dcop.jx.entry.jar -d ../../release
javac -encoding UTF-8 ../../src/*.java -cp ../../release:../../release/dcop.jx.entry.jar -d ../../release

echo "make core jar ... "
jar cvfM ../../release/dcop.jx.core.jar -C ../../release ./com

echo "delete all release ... "
rm -rf ../../release/com
