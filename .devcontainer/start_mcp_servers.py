#!/usr/bin/env python3
"""
Start MCP servers configured in .devcontainer/mcpServers.json
Reads the config and launches each server in the background with logging.
"""
import json
import subprocess
import os
import shlex
from pathlib import Path

def main():
    cfg_path = Path(__file__).parent / "mcpServers.json"
    if not cfg_path.exists():
        print(f"[INFO] No mcpServers.json found at {cfg_path}, skipping MCP server startup")
        return 0

    try:
        with cfg_path.open() as f:
            cfg = json.load(f)
    except Exception as e:
        print(f"[ERROR] Failed to read {cfg_path}: {e}")
        return 1

    if not cfg:
        print("[INFO] mcpServers.json is empty, no servers to start")
        return 0

    for name, info in cfg.items():
        pidfile = Path(f"/tmp/mcp-{name}.pid")
        logfile = Path(f"/tmp/mcp-{name}.log")

        # Check if already running
        if pidfile.exists():
            try:
                pid = int(pidfile.read_text().strip())
                os.kill(pid, 0)  # Signal 0 = check if process exists
                print(f"[INFO] {name} already running (pid {pid}), skipping")
                continue
            except (ValueError, ProcessLookupError, OSError):
                # stale PID file, remove and continue
                pidfile.unlink(missing_ok=True)

        cmd = [info.get("command")] + info.get("args", [])
        cwd = info.get("cwd", None)
        
        print(f"[INFO] Starting {name}: {' '.join(shlex.quote(str(p)) for p in cmd)} (cwd={cwd})")
        
        try:
            with logfile.open("ab") as lf:
                proc = subprocess.Popen(
                    cmd,
                    cwd=cwd,
                    stdout=lf,
                    stderr=subprocess.STDOUT,
                    close_fds=True,
                )
            pidfile.write_text(str(proc.pid))
            print(f"[INFO] Started {name} (pid {proc.pid}), logs -> {logfile}")
        except Exception as e:
            print(f"[ERROR] Failed to start {name}: {e}")
            return 1

    return 0

if __name__ == "__main__":
    exit(main())
