#!/bin/bash

pip install --break-system-packages -r requirements.txt
npm install -g @anthropic-ai/claude-code
curl -fsSL https://opencode.ai/install | bash
pip install --break-system-packages uv
cd /opt/agentic-csa && uv sync --all-extras && uv run python scripts/build_index.py all
