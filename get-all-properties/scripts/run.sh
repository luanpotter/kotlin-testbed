#!/usr/bin/env bash
set -uo pipefail

cd "$(dirname "$0")/.."

./gradlew clean :ksp-example:kspKotlin --rerun-tasks 2>&1 | grep --line-buffered '\[ksp\]' | sed 's/^w: \[ksp\] //'

# propagate gradle's exit code, not grep's
exit "${PIPESTATUS[0]}"
