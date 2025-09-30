#!/usr/bin/env python3
"""Smoke test for simplegreeting-quarkus"""

import os
import sys
from datetime import datetime
from playwright.sync_api import sync_playwright


DEFAULT_BASE = "http://localhost:8080"
BASE_URL = os.getenv("SIMPLE_GREETING_BASE_URL", DEFAULT_BASE)
DEFAULT_ENDPOINT = "/simplegreeting"
HOME_URI = os.getenv("SIMPLE_GREETING_HOME_URI", DEFAULT_ENDPOINT)


def main() -> int:
    print(f"---[ {datetime.now().strftime('%H:%M:%S')} - Smoke test ]---")
    with sync_playwright() as p:
        browser = p.chromium.launch(headless=True)
        page = browser.new_page()
        page.goto(BASE_URL + HOME_URI)
        num_tests = 0
        passed_tests = 0
        num_tests += 1
        # Ensure that the page loads successfully
        if "Simple Greeting" in page.content():
            print("[PASS] Page loaded successfully and contains expected text.")
            passed_tests += 1
        else:
            print("[FAIL] Page did not contain expected text.", file=sys.stderr)

        num_tests += 1
        # Say hello test
        page.get_by_label("Enter your name:").fill("John")
        with page.expect_navigation():
            page.get_by_role("button", name="Say Hello").click()

        # Assert we got the correct greeting
        if "Hi, John!" in page.content():
            print("[PASS] Greeting displayed correctly.")
            passed_tests += 1
        else:
            print(
                "[FAIL] Greeting not displayed as expected.",
                file=sys.stderr,
            )

        print(f"Summary: {passed_tests}/{num_tests} tests passed.")
        print(f"---[ {datetime.now().strftime('%H:%M:%S')} - Smoke test complete ]---")
        return 0


if __name__ == "__main__":  # pragma: no cover
    sys.exit(main())
