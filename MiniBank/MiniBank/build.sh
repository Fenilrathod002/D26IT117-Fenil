#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
SOURCE_DIR="$ROOT_DIR/src/main/java"
BUILD_DIR="$ROOT_DIR/build"
CLASSES_DIR="$BUILD_DIR/classes"
JAR_FILE="$BUILD_DIR/minibank.jar"

mkdir -p "$CLASSES_DIR"
find "$CLASSES_DIR" -type f -name '*.class' -delete
find "$SOURCE_DIR" -type f -name '*.java' -print0 \
  | xargs -0 javac --release 17 -d "$CLASSES_DIR"

jar --create --file "$JAR_FILE" --main-class service.MiniBank \
  -C "$CLASSES_DIR" .

echo "Built $JAR_FILE"
