#!/bin/bash
# Checks that at least one commit message in the student's history references a Question.
# The commit-msg hook enforces this locally; this script verifies it server-side.

if git log --format="%s %b" | grep -qiE "question [1-5]"; then
  echo "PASS: Found a commit message that references a Question."
  echo ""
  echo "Qualifying commits:"
  git log --oneline | grep -iE "question [1-5]" | head -5
  exit 0
else
  echo "FAIL: No commit message references a Question (e.g., 'Question 1')."
  echo ""
  echo "Every commit should describe which questions you worked on."
  echo "Example: \"Answer Question 1 and Question 3 -- clone vs fork and branch naming\""
  echo ""
  echo "Your commit history:"
  git log --oneline | head -10
  exit 1
fi
