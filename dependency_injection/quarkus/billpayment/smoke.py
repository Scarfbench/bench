#!/usr/bin/env python3
"""Smoke test for billpayment-quarkus"""

import os
import sys
from datetime import datetime
from playwright.sync_api import sync_playwright


DEFAULT_BASE = "http://localhost:8080"
BASE_URL = os.getenv("BILLPAYMENT_BASE_URL", DEFAULT_BASE)
DEFAULT_ENDPOINT = "/billpayment"
HOME_URI = os.getenv("BILLPAYMENT_HOME_URI", DEFAULT_ENDPOINT)


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
        if "Bill Payment Options" in page.content():
            print("[PASS] Page loaded successfully and contains expected text.")
            passed_tests += 1
        else:
            print("[FAIL] Page did not contain expected text.", file=sys.stderr)

        num_tests += 1
        # Fill the amount input and pay with debit card
        page.get_by_label("Amount: $").fill("12")
        page.get_by_label("Debit Card").check()
        with page.expect_navigation():
            page.get_by_role("button", name="Pay").click()

        # Assert we're on result page
        page_content = page.content().lower()
        if all(
            elem.lower() in page_content
            for elem in ["Bill Payment: Result", "DEBIT", "12.00"]
        ):
            print("[PASS] Debit payment displayed correctly.")
            passed_tests += 1
        else:
            print("[FAIL] Debit payment not displayed as expected.", file=sys.stderr)

        num_tests += 1

        # Hit the back button and ensure we are back on the form
        with page.expect_navigation():
            page.get_by_role("button", name="Back").click()
        if "Bill Payment Options" in page.content():
            print("[PASS] Back navigation successful.")
            passed_tests += 1
        else:
            print("[FAIL] Back navigation failed.", file=sys.stderr)
        num_tests += 1

        # Fill the amount input and pay with credit card
        page.get_by_label("Amount: $").fill("5")
        page.get_by_label("Credit Card").check()
        with page.expect_navigation():
            page.get_by_role("button", name="Pay").click()

        # Assert we're on result page
        page_content = page.content().lower()
        if all(
            elem.lower() in page_content
            for elem in ["Bill Payment: Result", "CREDIT", "5.00"]
        ):
            print("[PASS] Credit payment displayed correctly.")
            passed_tests += 1
        else:
            print("[FAIL] Credit payment not displayed as expected.", file=sys.stderr)

        num_tests += 1

        # Hit the back button and ensure we are back on the form
        with page.expect_navigation():
            page.get_by_role("button", name="Back").click()
        if "Bill Payment Options" in page.content():
            print("[PASS] Back navigation successful.")
            passed_tests += 1
        else:
            print("[FAIL] Back navigation failed.", file=sys.stderr)

        num_tests += 1

        # Click the "Reset" button (second form)
        page.get_by_label("Amount: $").fill("12")
        with page.expect_navigation():
            page.get_by_role("button", name="Reset").click()

        if "0" == page.get_by_label("Amount: $").input_value():
            print("[PASS] Reset successful.")
            passed_tests += 1
        else:
            print("[FAIL] Reset failed.", file=sys.stderr)

        print(f"Summary: {passed_tests}/{num_tests} tests passed.")
        print(f"---[ {datetime.now().strftime('%H:%M:%S')} - Smoke test complete ]---")
        return 0


if __name__ == "__main__":  # pragma: no cover
    sys.exit(main())
