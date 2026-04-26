#!/bin/bash
# Checks that the three deliberate errors in wpilib.gitignore have been corrected.
#
# The three problems students must fix:
#   1. Missing "build/" entry
#   2. ".idea/" (IntelliJ) instead of ".vscode/" (VS Code)
#   3. "vendordeps/" incorrectly listed as ignored (it should be committed)

PASS=1

if [ ! -f wpilib.gitignore ]; then
  echo "FAIL: wpilib.gitignore not found. Did you delete the file?"
  exit 1
fi

# Problem 1: build/ must be present as an uncommented entry
if grep -qE "^build/$" wpilib.gitignore; then
  echo "PASS: 'build/' entry is present"
else
  echo "FAIL: 'build/' is missing. Add 'build/' to ignore compiled WPILib output."
  PASS=0
fi

# Problem 2: .idea/ must be gone; .vscode/ must be present
if grep -qE "^\.idea/" wpilib.gitignore; then
  echo "FAIL: '.idea/' is still present. That is for IntelliJ IDEA — replace it with '.vscode/'."
  PASS=0
elif grep -qE "^\.vscode/" wpilib.gitignore; then
  echo "PASS: '.vscode/' is present and '.idea/' has been removed"
else
  echo "FAIL: '.vscode/' is missing. Add it to ignore VS Code local settings."
  PASS=0
fi

# Problem 3: vendordeps/ must NOT appear as an uncommented entry
if grep -qE "^vendordeps/" wpilib.gitignore; then
  echo "FAIL: 'vendordeps/' is being ignored. Remove that line — vendordeps must be committed."
  PASS=0
else
  echo "PASS: 'vendordeps/' is correctly not ignored"
fi

if [ $PASS -eq 1 ]; then
  echo ""
  echo "All .gitignore checks passed."
  exit 0
else
  echo ""
  echo "One or more .gitignore problems remain. Review the hints in the file and fix them."
  exit 1
fi
