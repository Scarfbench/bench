#!/usr/bin/env python3
"""Smoke test for producerfields-jakarta"""

import os
import sys
from datetime import datetime
from playwright.sync_api import sync_playwright


DEFAULT_BASE = "http://localhost:8084"
BASE_URL = os.getenv("PRODUCER_FIELDS_BASE_URL", DEFAULT_BASE)
DEFAULT_ENDPOINT = "/producerfields"
HOME_URI = os.getenv("PRODUCER_FIELDS_HOME_URI", DEFAULT_ENDPOINT)


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
        if "Create To Do List" in page.content():
            print("[PASS] Page loaded successfully and contains expected text.")
            passed_tests += 1
        else:
            print("[FAIL] Page did not contain expected text.", file=sys.stderr)

        num_tests += 1
        # Add a new to do item
        page.get_by_label("Enter a string:").fill("Smoke Test")
        with page.expect_navigation():
            page.get_by_role("button", name="Submit").click()

        todo = page.get_by_label("Enter a string:").input_value()
        # Assert we're still on the same page
        if "Create To Do List" in page.content() and todo == "Smoke Test":
            print("[PASS] Page displayed correctly after submit.")
            passed_tests += 1
        else:
            print(
                "[FAIL] Page not displayed as expected after submit.", file=sys.stderr
            )

        num_tests += 1

        # Show the list of registered todos
        with page.expect_navigation():
            page.get_by_role("button", name="Show Items").click()

        # Assert page content contains previously added todo
        if "To Do List" in page.content() and "Smoke Test" in page.content():
            print("[PASS] Todo list page displayed correctly.")
            passed_tests += 1
        else:
            print("[FAIL] Todo list page not displayed as expected.", file=sys.stderr)

        num_tests += 1
        # Go back to main page
        with page.expect_navigation():
            page.get_by_role("button", name="Back").click()

        # should be main page
        if "Create To Do List" in page.content():
            print("[PASS] Back successful.")
            passed_tests += 1
        else:
            print("[FAIL] Back failed.", file=sys.stderr)

        print(f"Summary: {passed_tests}/{num_tests} tests passed.")
        print(f"---[ {datetime.now().strftime('%H:%M:%S')} - Smoke test complete ]---")
        return 0


if __name__ == "__main__":  # pragma: no cover
    sys.exit(main())
