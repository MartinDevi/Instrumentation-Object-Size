#!/usr/bin/env sh

DOT_FILES=$(ls build/outputs/dot/)

mkdir -p build/outputs/png/

echo "$DOT_FILES" | while read DOT_FILE ; do
  NAME=$(basename "$DOT_FILE" ".dot" )
  echo "File: $NAME"
  dot -Tpng "build/outputs/dot/$DOT_FILE" > "build/outputs/png/$NAME.png"
done




