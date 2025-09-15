#!/usr/bin/env python3
"""Smoke test for taskcreator-quarkus.

Checks:
  1) POST a log line to /api/taskinfo and expect 200 or 204.

Env:
    BASE_URL    (default: http://localhost:8080)
    VERBOSE=1   enables verbose output

Exit: 0 on success, non-zero otherwise.
"""
from __future__ import annotations

import argparse
import os
import sys
from datetime import datetime
from urllib.error import HTTPError
from urllib.request import Request, urlopen


DEFAULT_BASE = os.getenv("BASE_URL", "http://localhost:8080")
DEFAULT_ENDPOINT = "/taskinfo"


def post_log(base_url: str, message: str) -> int:
    url = f"{base_url.rstrip('/')}{DEFAULT_ENDPOINT}"
    req = Request(url, data=message.encode(), headers={"Content-Type": "text/plain"}, method="POST")
    print(f"POST {url} :: {message}")
    try:
        with urlopen(req, timeout=10) as resp:
            status = resp.getcode()
            body = resp.read().decode("utf-8", "replace")
    except HTTPError as e:
        status = e.code
        body = e.read().decode("utf-8", "replace")
    except Exception as e:  # network failure
        print(f"[FAIL] POST failed: {e}", file=sys.stderr)
        return 1

    print(f"RESP {status}\n{body.strip()}")

    if status not in (200, 204):
        print(f"[FAIL] Unexpected HTTP status {status}", file=sys.stderr)
        return 1

    print(f"[PASS] POST {DEFAULT_ENDPOINT} -> {status}")
    return 0

def parse_args() -> argparse.Namespace:
    p = argparse.ArgumentParser(description="Smoke test for taskcreator-quarkus")
    p.add_argument("--base-url", default=DEFAULT_BASE, help=f"Base URL (env BASE_URL or {DEFAULT_BASE})")
    return p.parse_args()


def main() -> int:
    args = parse_args()
    msg = f"{datetime.now().strftime('%H:%M:%S')} - Smoke test"
    if post_log(args.base_url, msg) != 0:
        return 1
    print("[PASS] Smoke tests complete")
    return 0


if __name__ == "__main__":  # pragma: no cover
    sys.exit(main())