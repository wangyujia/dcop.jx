

rem delete dcop.jx dir ... 
rd /s /q "../../release/com"





rem delete dcop.jx.entry.jar ... 
del /s /q ..\..\release\dcop.jx.entry.jar

rem build entry class ... 
javac -encoding UTF-8 ../../interface/*.java -cp ../../release -d ../../release
javac -encoding UTF-8 ../../interface/base/*.java -cp ../../release -d ../../release
javac -encoding UTF-8 ../../interface/kernel/*.java -cp ../../release -d ../../release
javac -encoding UTF-8 ../../interface/extend/access/*.java -cp ../../release -d ../../release
javac -encoding UTF-8 ../../interface/extend/jjs/*.java -cp ../../release -d ../../release

rem make entry jar ... 
jar cvfM ../../release/dcop.jx.entry.jar -C ../../release ./com

rem delete all release ... 
rd /s /q "../../release/com"






rem delete dcop.jx.core.jar ... 
del /s /q ..\..\release\dcop.jx.core.jar

rem build core class ... 
javac -encoding UTF-8 ../../src/base/msg/*.java -cp ../../release;../../release/dcop.jx.entry.jar -d ../../release
javac -encoding UTF-8 ../../src/base/pkg/*.java -cp ../../release;../../release/dcop.jx.entry.jar -d ../../release
javac -encoding UTF-8 ../../src/base/log/*.java -cp ../../release;../../release/dcop.jx.entry.jar -d ../../release
javac -encoding UTF-8 ../../src/base/sock/*.java -cp ../../release;../../release/dcop.jx.entry.jar -d ../../release
javac -encoding UTF-8 ../../src/base/file/*.java -cp ../../release;../../release/dcop.jx.entry.jar -d ../../release
javac -encoding UTF-8 ../../src/kernel/*.java -cp ../../release;../../release/dcop.jx.entry.jar -d ../../release
javac -encoding UTF-8 ../../src/extend/access/*.java -cp ../../release;../../release/dcop.jx.entry.jar -d ../../release
javac -encoding UTF-8 ../../src/extend/jjs/*.java -cp ../../release;../../release/dcop.jx.entry.jar -d ../../release
javac -encoding UTF-8 ../../src/*.java -cp ../../release;../../release/dcop.jx.entry.jar -d ../../release

rem make core jar ... 
jar cvfM ../../release/dcop.jx.core.jar -C ../../release ./com

rem delete all release ... 
rd /s /q "../../release/com"




pause
