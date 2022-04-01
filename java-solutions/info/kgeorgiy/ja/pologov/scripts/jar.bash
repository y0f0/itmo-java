#!/bin/bash
JAVA_ADVANCED_2022=$PWD/../../../../../../../java-advanced-2022
MODULES_PATH="$JAVA_ADVANCED_2022"/modules/info.kgeorgiy.java.advanced.implementor/info/kgeorgiy/java/advanced/implementor
IMPLEMENTOR=$PWD/../implementor/Implementor.java

javac "$IMPLEMENTOR" "$MODULES_PATH"/ImplerException.java "$MODULES_PATH"/JarImpler.java "$MODULES_PATH"/Impler.java
echo "Manifest-Version: 1.0
      Created-By: Pologov Nikita
      Main-Class: info.kgeorgiy.ja.pologov.implementor.Implementor
      Class-Path: java-advanced-2022\artifacts\info.kgeorgiy.java.advanced.implementor.Implementor
" > manifest.fm
jar cmf manifest.fm Implementor.jar ../implementor/Implementor.class
#jar -cf ../scripts/Implementor.jar "$IMPLEMENTOR"