mkdir out
cd out || exit
javac -d . -cp ../lib/gnu-crypto-2.0.1.jar:../lib/json-20220320.jar:../lib/zxcvbn-1.7.0.jar ../src/de/paulwolf/passwordmanager/*.java ../src/de/paulwolf/passwordmanager/ui/*.java ../src/de/paulwolf/passwordmanager/ui/tetris/*.java ../src/de/paulwolf/passwordmanager/information/*.java ../src/de/paulwolf/passwordmanager/wizards/*.java ../src/de/paulwolf/passwordmanager/utility/*.java
cp ../rsc/* .
cp ../lib/gnu-crypto-2.0.1.jar .; cp ../lib/json-20220320.jar .; cp ../lib/zxcvbn-1.7.0.jar .
jar xf gnu-crypto-2.0.1.jar; jar xf json-20220320.jar; jar xf zxcvbn-1.7.0.jar
rm -f gnu-crypto-2.0.1.jar json-20220320.jar zxcvbn-1.7.0.jar
jar cmf ../rsc/META-INF/MANIFEST.MF ../PasswordManager.jar ./*
cd ..
rm -rf out