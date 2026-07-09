#!/usr/bin/env python3
"""Manage the Supabase `teachers` allowlist for the quiz-progress dashboard.

Grants or revokes class-wide read access to quiz_results (docs/admin.html)
for a given GitHub login. Must be run locally with the Supabase service-role
key -- never commit that key or expose it in client-side code.

Usage:
    SUPABASE_SERVICE_ROLE_KEY=... python3 scripts/invite_teacher.py add octocat
    SUPABASE_SERVICE_ROLE_KEY=... python3 scripts/invite_teacher.py remove octocat
    SUPABASE_SERVICE_ROLE_KEY=... python3 scripts/invite_teacher.py list

    # Review self-service requests submitted via the "Request instructor
    # access" form in docs/admin.html:
    SUPABASE_SERVICE_ROLE_KEY=... python3 scripts/invite_teacher.py requests
    SUPABASE_SERVICE_ROLE_KEY=... python3 scripts/invite_teacher.py approve <request_id>
    SUPABASE_SERVICE_ROLE_KEY=... python3 scripts/invite_teacher.py deny <request_id>

The Supabase project URL defaults to the one baked into docs/admin.html; set
SUPABASE_URL to override it.
"""

import argparse
import json
import os
import sys
import urllib.error
import urllib.parse
import urllib.request
from datetime import datetime, timezone

DEFAULT_SUPABASE_URL = "https://wltmawdleuvcjxqtzkmj.supabase.co"


def request(method, url, api_key, body=None, extra_headers=None):
    headers = {
        "apikey": api_key,
        "Authorization": f"Bearer {api_key}",
        "Content-Type": "application/json",
    }
    if extra_headers:
        headers.update(extra_headers)

    data = json.dumps(body).encode("utf-8") if body is not None else None
    req = urllib.request.Request(url, data=data, headers=headers, method=method)
    try:
        with urllib.request.urlopen(req) as resp:
            raw = resp.read()
            return json.loads(raw) if raw else None
    except urllib.error.HTTPError as e:
        sys.exit(f"Request failed ({e.code}): {e.read().decode('utf-8', 'replace')}")


def add_teacher(base_url, api_key, github_login):
    url = f"{base_url}/rest/v1/teachers"
    request(
        "POST",
        url,
        api_key,
        body={"github_login": github_login},
        extra_headers={"Prefer": "resolution=merge-duplicates,return=minimal"},
    )
    print(f"Granted instructor access to '{github_login}'.")


def remove_teacher(base_url, api_key, github_login):
    url = f"{base_url}/rest/v1/teachers?github_login=eq.{urllib.parse.quote(github_login)}"
    request("DELETE", url, api_key)
    print(f"Revoked instructor access for '{github_login}'.")


def list_teachers(base_url, api_key):
    url = f"{base_url}/rest/v1/teachers?select=github_login,added_at&order=added_at.asc"
    rows = request("GET", url, api_key) or []
    if not rows:
        print("No instructors currently have access.")
        return
    for row in rows:
        print(f"{row['github_login']}\t(added {row['added_at']})")


def list_requests(base_url, api_key):
    url = (
        f"{base_url}/rest/v1/teacher_requests"
        "?select=id,github_login,full_name,reason,requested_at"
        "&status=eq.pending&order=requested_at.asc"
    )
    rows = request("GET", url, api_key) or []
    if not rows:
        print("No pending requests.")
        return
    for row in rows:
        reason = row.get("reason") or "(no reason given)"
        print(
            f"#{row['id']}\t{row['github_login']}\t{row.get('full_name') or ''}\t"
            f"requested {row['requested_at']}\t{reason}"
        )


def _get_request(base_url, api_key, request_id):
    url = f"{base_url}/rest/v1/teacher_requests?id=eq.{request_id}&select=id,github_login,status"
    rows = request("GET", url, api_key) or []
    if not rows:
        sys.exit(f"No request with id {request_id}.")
    return rows[0]


def approve_request(base_url, api_key, request_id):
    req_row = _get_request(base_url, api_key, request_id)
    add_teacher(base_url, api_key, req_row["github_login"])
    request(
        "PATCH",
        f"{base_url}/rest/v1/teacher_requests?id=eq.{request_id}",
        api_key,
        body={"status": "approved", "reviewed_at": datetime.now(timezone.utc).isoformat()},
        extra_headers={"Prefer": "return=minimal"},
    )
    print(f"Approved request #{request_id} for '{req_row['github_login']}'.")


def deny_request(base_url, api_key, request_id):
    req_row = _get_request(base_url, api_key, request_id)
    request(
        "PATCH",
        f"{base_url}/rest/v1/teacher_requests?id=eq.{request_id}",
        api_key,
        body={"status": "denied", "reviewed_at": datetime.now(timezone.utc).isoformat()},
        extra_headers={"Prefer": "return=minimal"},
    )
    print(f"Denied request #{request_id} for '{req_row['github_login']}'.")


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    sub = parser.add_subparsers(dest="command", required=True)

    add_p = sub.add_parser("add", help="Grant an instructor read access")
    add_p.add_argument("github_login")

    remove_p = sub.add_parser("remove", help="Revoke an instructor's read access")
    remove_p.add_argument("github_login")

    sub.add_parser("list", help="List current instructors")

    sub.add_parser("requests", help="List pending 'request instructor access' submissions")

    approve_p = sub.add_parser("approve", help="Approve a pending request and grant access")
    approve_p.add_argument("request_id", type=int)

    deny_p = sub.add_parser("deny", help="Deny a pending request")
    deny_p.add_argument("request_id", type=int)

    args = parser.parse_args()

    api_key = os.environ.get("SUPABASE_SERVICE_ROLE_KEY")
    if not api_key:
        sys.exit("Set SUPABASE_SERVICE_ROLE_KEY in the environment before running this script.")
    base_url = os.environ.get("SUPABASE_URL", DEFAULT_SUPABASE_URL)

    if args.command == "add":
        add_teacher(base_url, api_key, args.github_login)
    elif args.command == "remove":
        remove_teacher(base_url, api_key, args.github_login)
    elif args.command == "list":
        list_teachers(base_url, api_key)
    elif args.command == "requests":
        list_requests(base_url, api_key)
    elif args.command == "approve":
        approve_request(base_url, api_key, args.request_id)
    elif args.command == "deny":
        deny_request(base_url, api_key, args.request_id)


if __name__ == "__main__":
    main()
