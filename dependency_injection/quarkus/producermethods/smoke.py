#!/usr/bin/env python3
"""Smoke test for producermethods-quarkus"""

import os
import sys
from datetime import datetime
from playwright.sync_api import sync_playwright


DEFAULT_BASE = "http://localhost:8080"
BASE_URL = os.getenv("PRODUCER_METHODS_BASE_URL", DEFAULT_BASE)
DEFAULT_ENDPOINT = "/producermethods"
HOME_URI = os.getenv("PRODUCER_METHODS_HOME_URI", DEFAULT_ENDPOINT)


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
        if "String Encoder" in page.content():
            print("[PASS] Page loaded successfully and contains expected text.")
            passed_tests += 1
        else:
            print("[FAIL] Page did not contain expected text.", file=sys.stderr)

        num_tests += 1
        # Fill fields and shift letters
        page.get_by_label("Shift Letters").check()
        page.get_by_label("Enter a string:").fill("aa2")
        page.get_by_label("Enter the number of letters to shift by:").fill("2")
        with page.expect_navigation():
            page.get_by_role("button", name="Encode").click()

        # Assert we got the correct encoding on page
        if "cc2" in page.content():
            print("[PASS] Shift letters encode displayed correctly.")
            passed_tests += 1
        else:
            print(
                "[FAIL] Shift letters encode not displayed as expected.",
                file=sys.stderr,
            )

        num_tests += 1
        # Fill fields and use test encoder
        page.get_by_label("Test").check()
        page.get_by_label("Enter a string:").fill("aa2")
        page.get_by_label("Enter the number of letters to shift by:").fill("2")
        with page.expect_navigation():
            page.get_by_role("button", name="Encode").click()

        # Assert we got the correct encoding on page
        if "input string is aa2, shift value is 2" in page.content():
            print("[PASS] Test encode displayed correctly.")
            passed_tests += 1
        else:
            print("[FAIL] Test encode not displayed as expected.", file=sys.stderr)

        num_tests += 1

        # Validate number of shifts
        page.get_by_label("Enter the number of letters to shift by:").fill("33")
        with page.expect_navigation():
            page.get_by_role("button", name="Encode").click()

        # Assert we have an error on page
        value = page.get_by_label("Enter a string:").input_value()
        shift = page.get_by_label(
            "Enter the number of letters to shift by:"
        ).input_value()
        if (
            "must be less than or equal to 26" in page.content()
            and "aa2" == value
            and "33" == shift
        ):
            print("[PASS] Error displayed correctly.")
            passed_tests += 1
        else:
            print("[FAIL] Error not displayed as expected.", file=sys.stderr)

        num_tests += 1
        # Click the "Reset" button
        # JSF does the validation on reset
        page.get_by_label("Enter the number of letters to shift by:").fill("2")
        with page.expect_navigation():
            page.get_by_role("button", name="Reset").click()

        value = page.get_by_label("Enter a string:").input_value()
        shift = page.get_by_label(
            "Enter the number of letters to shift by:"
        ).input_value()
        if "" == value and "0" == shift:
            print("[PASS] Reset successful.")
            passed_tests += 1
        else:
            print("[FAIL] Reset failed.", file=sys.stderr)

        print(f"Summary: {passed_tests}/{num_tests} tests passed.")
        print(f"---[ {datetime.now().strftime('%H:%M:%S')} - Smoke test complete ]---")
        return 0


if __name__ == "__main__":  # pragma: no cover
    sys.exit(main())
