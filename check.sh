#!/usr/bin/env sh

set -e

echo "  ┏━━━━━━━━━━━━┓"
echo "  ┃   LUXELS   ┃"
echo "  ┗━━━━━━━━━━━━┛"

echo " ··· KtLint"
./gradlew :imageio:ktlintCheck
./gradlew :core:ktlintCheck
./gradlew :engine:ktlintCheck
./gradlew :components:ktlintCheck
./gradlew :scenes:ktlintCheck
./gradlew :cli:ktlintCheck
echo "   ✔ KtLint"
echo ""

echo " ··· Detekt [jvm]"
./gradlew :imageio:detektJvmMain
./gradlew :core:detektJvmMain
./gradlew :engine:detektJvmMain
./gradlew :components:detektJvmMain
./gradlew :scenes:detektJvmMain
./gradlew :cli:detektJvmMain
echo "   ✔ Detekt [jvm]"
echo ""

echo " ··· Test [jvm]"
./gradlew :imageio:jvmTest
./gradlew :core:jvmTest
./gradlew :engine:jvmTest
./gradlew :components:jvmTest
echo "   ✔ Test [jvm]"
echo ""

echo " ··· Dependency upgrades"
./gradlew :imageio:dependencyUpdates
./gradlew :core:dependencyUpdates
./gradlew :engine:dependencyUpdates
./gradlew :components:dependencyUpdates
./gradlew :scenes:dependencyUpdates
./gradlew :cli:dependencyUpdates
echo "   ✔ Dependency upgrades"
echo ""
