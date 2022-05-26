mkdir out
cd out
javac -d . -cp ../lib/gnu-crypto-2.0.1.jar ../src/de/paulwolf/passwordmanager/*.java ../src/de/paulwolf/passwordmanager/ui/*.java ../src/de/paulwolf/passwordmanager/ui/tetris/*.java ../src/de/paulwolf/passwordmanager/information/*.java ../src/de/paulwolf/passwordmanager/wizards/*.java
cp ../rsc/* .
cp ../lib/gnu-crypto-2.0.1.jar .
jar xf gnu-crypto-2.0.1.jar
rm -f gnu-crypto-2.0.1.jar
jar cmf ../rsc/META-INF/MANIFEST.MF ../PasswordManager.jar ./*
cd ..
rm -rf out