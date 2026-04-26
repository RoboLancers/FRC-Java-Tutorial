#!/bin/bash
# Checks that all placeholder text in student-notes.md has been replaced.

PASS=1

if [ ! -f student-notes.md ]; then
  echo "FAIL: student-notes.md not found. Did you delete the file?"
  exit 1
fi

check_placeholder() {
  local placeholder="$1"
  local label="$2"
  if grep -qF "$placeholder" student-notes.md; then
    echo "FAIL ($label): placeholder still present -- \"$placeholder\""
    PASS=0
  else
    echo "PASS ($label)"
  fi
}

check_placeholder "_Your name here_"                                          "Name field"
check_placeholder "_(Write your answer here)_"                                "Question 1 and/or Question 5"
check_placeholder "_(Write your commit message here)_"                        "Question 2"
check_placeholder "_(your answer)_"                                           "Question 3"
check_placeholder "_(Write your answer here — aim for 3–5 sentences)_"        "Question 4"

if [ $PASS -eq 1 ]; then
  echo ""
  echo "All student notes checks passed."
  exit 0
else
  echo ""
  echo "One or more answers are still blank. Fill in every placeholder in student-notes.md."
  exit 1
fi
