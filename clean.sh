#!/usr/bin/env sh

set -e

echo "  ┏━━━━━━━━━━━━┓"
echo "  ┃   LUXELS   ┃"
echo "  ┗━━━━━━━━━━━━┛"

echo " ··· Clean"
./gradlew :graphikio:clean
./gradlew :core:clean
./gradlew :engine:clean
./gradlew :components:clean
./gradlew :cli:clean
echo "   ✔ Clean"
echo ""
