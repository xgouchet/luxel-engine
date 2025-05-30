#!/usr/bin/env sh

set -e

echo "  ┏━━━━━━━━━━━━┓"
echo "  ┃   LUXELS   ┃"
echo "  ┗━━━━━━━━━━━━┛"

current_os=`uname -s`
current_arch=`uname -m`

target_path=""

if [ $current_os = "Linux" ]; then
  if [ $current_arch = "x86_64" ]; then
    target_path="./cli/build/bin/linuxX64/debugExecutable/cli.kexe"
  else
    echo "Unknown Arch: $current_arch"
    exit 1
  fi
elif [ $current_os = "Darwin" ]; then
  if [ $current_arch = "x86_64" ]; then
    target_path="./cli/build/bin/macosX64/debugExecutable/cli.kexe"
  else
    echo "Unknown Arch: $current_arch"
    exit 1
  fi
else
   echo "Unknown OS: $current_os"
   exit 1
fi

echo ""
echo " ··· Running debug executable ($current_os $current_arch)"
$target_path $@
