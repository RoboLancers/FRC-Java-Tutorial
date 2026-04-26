#!/bin/bash
set -e

# Point git at the project's hooks directory
git config core.hooksPath .githooks
chmod +x .githooks/commit-msg

echo ""
echo "Git hooks configured."
echo "Commit messages must reference at least one Question (e.g., 'Answer Question 1 and Question 3')."
echo ""
