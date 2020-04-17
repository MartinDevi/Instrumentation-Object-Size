#!/usr/bin/env sh

DOT_FILES=$(ls instrumentation/build/outputs/dot/)

mkdir instrumentation/build/outputs/png/

echo "$DOT_FILES" | while read DOT_FILE ; do
  NAME=$(basename "$DOT_FILE" ".dot" )
  echo "File: $NAME"
  dot -Tpng "instrumentation/build/outputs/dot/$DOT_FILE" > "instrumentation/build/outputs/png/$NAME.png"
done




