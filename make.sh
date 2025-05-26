#!/usr/bin/env sh

set -e

echo "  ┏━━━━━━━━━━━━┓"
echo "  ┃   LUXELS   ┃"
echo "  ┗━━━━━━━━━━━━┛"

current_os=`uname -s`
current_arch=`uname -m`

target_variant=""

if [ $current_os = "Linux" ]; then
  if [ $current_arch = "x86_64" ]; then
    target_variant="LinuxX64"
  else
    echo "Unknown Arch: $current_arch"
    exit 1
  fi
elif [ $current_os = "Darwin" ]; then
  if [ $current_arch = "x86_64" ]; then
    target_variant="MacosX64"
  else
    echo "Unknown Arch: $current_arch"
    exit 1
  fi
else
   echo "Unknown OS: $current_os"
   exit 1
fi


echo ""
echo " ··· Compiling libraries ($current_os $current_arch)"
./gradlew ":imageio:compileKotlin$target_variant"
./gradlew ":core:compileKotlin$target_variant"
./gradlew ":engine:compileKotlin$target_variant"
./gradlew ":components:compileKotlin$target_variant"

echo ""
echo " ··· Building debug executable ($current_os $current_arch)"
./gradlew ":cli:linkDebugExecutable$target_variant"

echo ""
echo "✓ Compiling done"
$target_path $@
