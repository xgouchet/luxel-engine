#!/usr/bin/env sh

set -e

echo "  ┏━━━━━━━━━━━━┓"
echo "  ┃   LUXELS   ┃"
echo "  ┗━━━━━━━━━━━━┛"

echo " ··· Clean"
./gradlew :graphikio:clean
./gradlew :core:clean
./gradlew :components:clean
./gradlew :cli:clean
echo "   ✔ Clean"
echo ""

echo " ··· KtLint"
./gradlew :graphikio:ktlintCheck
./gradlew :core:ktlintCheck
./gradlew :components:ktlintCheck
./gradlew :cli:ktlintCheck
echo "   ✔ KtLint"
echo ""

echo " ··· Detekt [jvm]"
./gradlew :graphikio:detektJvmMain
./gradlew :core:detektJvmMain
./gradlew :components:detektJvmMain
./gradlew :cli:detektJvmMain
echo "   ✔ Detekt [jvm]"
echo ""

echo " ··· Test [jvm]"
./gradlew :graphikio:jvmTest
./gradlew :core:jvmTest
./gradlew :components:jvmTest
./gradlew :cli:jvmTest
echo "   ✔ Test [jvm]"
echo ""
