#!/usr/bin/env python3
"""Smoke test for guessnumber-quarkus"""

import os
import sys
from datetime import datetime
from playwright.sync_api import sync_playwright


DEFAULT_BASE = "http://localhost:8080"
BASE_URL = os.getenv("GUESS_NUMBER_BASE_URL", DEFAULT_BASE)
DEFAULT_ENDPOINT = "/guessnumber"
HOME_URI = os.getenv("GUESS_NUMBER_HOME_URI", DEFAULT_ENDPOINT)


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
        if "Guess My Number" in page.content():
            print("[PASS] Page loaded successfully and contains expected text.")
            passed_tests += 1
        else:
            print("[FAIL] Page did not contain expected text.", file=sys.stderr)

        num_tests += 1
        # Guess 1 and hope it's not the selected number
        page.get_by_label("Number:").fill("1")
        with page.expect_navigation():
            page.get_by_role("button", name="Guess").click()

        # Assert we got 9 guesses now, the HTML is annoying
        if ">9<" in page.content():
            print("[PASS] Encode displayed correctly.")
            passed_tests += 1
        else:
            print("[FAIL] Encode not displayed as expected.", file=sys.stderr)

        num_tests += 1

        # Try number out of range, since we selected 1 before let's do it again
        page.get_by_label("Number:").fill("1")
        with page.expect_navigation():
            page.get_by_role("button", name="Guess").click()

        # Assert we have an error on page
        number = page.get_by_label("Number:").input_value()
        if "Invalid guess" in page.content() and "1" == number:
            print("[PASS] Error displayed correctly.")
            passed_tests += 1
        else:
            print("[FAIL] Error not displayed as expected.", file=sys.stderr)

        num_tests += 1
        # Click the "Reset" button
        with page.expect_navigation():
            page.get_by_role("button", name="Reset").click()

        # should have 10 guesses
        if ">10<" in page.content():
            print("[PASS] Reset successful.")
            passed_tests += 1
        else:
            print("[FAIL] Reset failed.", file=sys.stderr)

        print(f"Summary: {passed_tests}/{num_tests} tests passed.")
        print(f"---[ {datetime.now().strftime('%H:%M:%S')} - Smoke test complete ]---")
        return 0


if __name__ == "__main__":  # pragma: no cover
    sys.exit(main())
