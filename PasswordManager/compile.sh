mkdir out
cd out || exit
javac -d . -cp ../lib/gnu-crypto-2.0.1.jar:../lib/json-20220320.jar:../lib/zxcvbn-1.7.0.jar:../lib/jsch-0.1.54.jar ../src/de/paulwolf/passwordmanager/*.java ../src/de/paulwolf/passwordmanager/ui/*.java ../src/de/paulwolf/passwordmanager/ui/windows/*.java  ../src/de/paulwolf/passwordmanager/ui/passwordfields/*.java ../src/de/paulwolf/passwordmanager/ui/tetris/*.java ../src/de/paulwolf/passwordmanager/information/*.java ../src/de/paulwolf/passwordmanager/wizards/*.java ../src/de/paulwolf/passwordmanager/utility/*.java
cp ../rsc/* .
cp ../lib/* .
jar xf gnu-crypto-2.0.1.jar; jar xf json-20220320.jar; jar xf zxcvbn-1.7.0.jar; jar xf jsch-0.1.54.jar
rm -f *.jar
jar cmf ../rsc/META-INF/MANIFEST.MF ../PasswordManager.jar ./*
cd ..
rm -rf out