#!/bin/bash
set -e

spinner_pid=

TEMPLATE_APP_NAME="My application"
TEMPLATE_NAMESPACE="com.myapplication"

app_name=""
namespace=""

namespace_files=()
package_name_files=()
app_name_files=()

function start_spinner() {
  spinchars=("⠋" "⠙" "⠹" "⠸" "⠼" "⠴" "⠦" "⠧" "⠇" "⠏")

  { while :; do for X in "${spinchars[@]}"; do
    echo -en "\r$1 $X"
    sleep 0.1
  done; done & } 2>/dev/null
  spinner_pid=$!
}

function stop_spinner() {
  { kill -9 $spinner_pid && wait; } 2>/dev/null
  echo -en "\033[2K\r"
}

function main() {
  chmod +x cleanup-open.sh
  chmod +x close.sh

  ask_namespace
  ask_app_name
  rename_namespace
  rename_package_name
  rename_app_name
  find_and_move_namespace
  ending
}

function ask_namespace() {
  read -e -p "> Write your namespace or bundle id (e.g. com.utsman.ganteng): " namespace
  if [[ $namespace =~ ^[a-z]+\.[a-z]+\.[a-z]+$ ]]; then
    return
  else
    echo "Invalid package name."
    ask_namespace
  fi
}

function ask_app_name() {
  read -e -p "> Write your app name id (e.g. Utsman ganteng): " app_name
  if echo "$app_name" | grep -qE '^[A-Za-z ]+$'; then
    return
  else
    echo "Invalid app name. Only alphabets and spaces are allowed."
    ask_app_name
  fi
}

function rename_app_name() {
  start_spinner "> Find and replace app name"
  sleep 2

  while IFS= read -r line; do
    app_name_files+=("$line")
  done < <(grep -r "$TEMPLATE_APP_NAME" . | cut -d ":" -f1)

  for i in "${app_name_files[@]}"; do
    if [ "$i" != "./setup.sh" ]; then
      fixPath=$(echo "$i" | sed 's/^.\///')
      sed -i -E "s/$TEMPLATE_APP_NAME/$app_name/g" "$fixPath"
      rm "$fixPath-E"
    fi
  done

  sed -i -E "s/MyApplication/$app_name/g" "settings.gradle.kts"

  stop_spinner
}

function rename_namespace() {
  start_spinner "> Find and replace namespace"
  sleep 2

  while IFS= read -r line; do
    namespace_files+=("$line")
  done < <(grep -r com.myapplication.MyApplication . | cut -d ":" -f1)

  for i in "${namespace_files[@]}"; do
    if [ "$i" != "./setup.sh" ]; then
      fixPath=$(echo "$i" | sed 's/^.\///')
      sed -i -E "s/com\.myapplication\.MyApplication/$namespace/g" "$fixPath"
      rm "$fixPath-E"
    fi
  done

  stop_spinner
}

function rename_package_name() {
  start_spinner "> Find and replace package name"
  sleep 2

  while IFS= read -r line; do
    package_name_files+=("$line")
  done < <(grep -r com.myapplication . | cut -d ":" -f1)

  for i in "${package_name_files[@]}"; do
    if [ "$i" != "./setup.sh" ]; then
      fixPath=$(echo "$i" | sed 's/^.\///')
      if [[ "$i" != *".git"* ]]; then
        sed -i -E "s/com\.myapplication/$namespace/g" "$fixPath"
        rm "$fixPath-E"
      fi
    fi
  done

  stop_spinner
}

# shellcheck disable=SC2001
function find_and_move_namespace() {
  start_spinner "> Find and replace directory"
  sleep 2
  stop_spinner

  last_template_namespace=$(echo "$TEMPLATE_NAMESPACE" | cut -d'.' -f2)
  find_template_dir_namespace=$(find . -type d -name "com" -exec test -d "{}/$last_template_namespace" \; -print)
  template_dir_namespace="$find_template_dir_namespace/$last_template_namespace"

  replacement_namespace=$(echo "$namespace" | sed 's#\.#/#g')
  replacement_directory=$(echo "$template_dir_namespace" | sed "s#com/$last_template_namespace#$replacement_namespace#")

  fixPath1=$(echo "$template_dir_namespace" | sed 's/^.\///')
  fixPath2=$(echo "$replacement_directory" | sed 's/^.\///')

  mkdir -p "$fixPath2"
  cp -R "$fixPath1/." "$fixPath2"
  rm -rf "$fixPath1"
}

# shellcheck disable=SC2162
function ending() {
  read -p "> The template success to rename, Android Studio will be restart and cleanup, please ENTER to continue... "
  ./close.sh
}

main "$@"
exit
