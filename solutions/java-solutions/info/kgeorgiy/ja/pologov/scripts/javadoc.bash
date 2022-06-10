#!/bin/bash
JAVA_ADVANCED_2022=$PWD/../../../../../../../java-advanced-2022
MODULES_PATH="$JAVA_ADVANCED_2022"/modules/info.kgeorgiy.java.advanced.implementor/info/kgeorgiy/java/advanced/implementor
JAVADOC_PATH="$PWD"/javadoc

rm -rf "$JAVADOC_PATH"
javadoc \
  -private \
  -link https://docs.oracle.com/en/java/javase/11/docs/api/ \
  -d "$JAVADOC_PATH" \
  -cp "$JAVA_ADVANCED_2022"/artifacts/JarImplementorTest.jar: \
    "$PWD"/../implementor/Implementor.java \
    "$MODULES_PATH"/Impler.java \
    "$MODULES_PATH"/JarImpler.java \
    "$MODULES_PATH"/ImplerException.java