#!/usr/bin/env python3
"""Smoke test for cargotracker-spring

Performs:
 - UI:
   1) Visit and validate contents of Base Page
   2) Navigate to public cargo tracking page
   3) Track a package
   4) Go back to main page
   5) Go to administration dashboard
   6) View Cargo itinerary
   7) Go back to administration dashboard
   8) View cargo route selection page
   9) Go back to administration dashboard
  10) View claimed cargo details
  11) Go back to administration dashboard
  12) Track cargo using administration page
  13) Go back to administration dashboard and count not routed cargo
  14) Book new cargo and validate it's displayed
  15) Navigate to the live map page
  16) Go back to main page
  17) Update cargo details
 - REST:
  1) GET /rest/graph-traversal/shortest-path
  2) POST /rest/handling/reports

Exit codes:
  0 success
  1 failure
"""

import json
import os
import sys
import time
import urllib.parse
from datetime import datetime, timedelta
from playwright.sync_api import Page, expect, sync_playwright
from urllib.request import Request, urlopen
from urllib.error import HTTPError, URLError


DEFAULT_BASE = "http://localhost:8080/cargo-tracker"
BASE_URL = os.getenv("CARGO_TRACKER_BASE_URL", DEFAULT_BASE)
DEFAULT_ENDPOINT = "/index.xhtml"
HOME_URI = os.getenv("CARGO_TRACKER_HOME_URI", DEFAULT_ENDPOINT)


def visit_main_page(page: Page) -> int:
    passed = 0
    page.goto(BASE_URL + HOME_URI)
    # Ensure that the page loads successfully
    if "Eclipse Cargo Tracker" in page.content():
        print("[PASS] Main page loaded successfully and contains expected text.")
        passed = 1
    else:
        print("[FAIL] Main page did not contain expected text.", file=sys.stderr)

    return passed


def visit_link(page: Page, link_text: str, expected_content: str) -> int:
    passed = 0

    with page.expect_navigation():
        page.get_by_text(link_text).click()

    # Ensure page loads
    if expected_content in page.content():
        print(
            f"[PASS] {link_text} page loaded successfully and contains expected text."
        )
        passed = 1
    else:
        print(
            f"[FAIL] {link_text} page did not contain expected text.",
            file=sys.stderr,
        )

    return passed


def track_package(page: Page, package_id: str = "ABC123") -> int:
    passed = 0

    page.get_by_title("Valid tracking ID").fill(package_id)
    page.get_by_role("button", name="Track!").click()

    page.wait_for_selector('div[id$="result"]', timeout=1000)

    # Ensure page contents
    if "Loaded onto voyage 0100S in Hong Kong" in page.content():
        print(
            "[PASS] Package tracking page loaded successfully and contains expected text."
        )
        passed = 1
    else:
        print(
            "[FAIL] Package tracking page did not contain expected text.",
            file=sys.stderr,
        )

    return passed


def track_admin_package(page: Page, package_id: str = "ABC123") -> int:
    passed = 0

    input = page.get_by_title("Valid tracking ID")
    input.click()
    input.type(package_id[0])

    try:
        page.wait_for_selector("#trackingForm\\:trackingIdInput_panel", timeout=2000)
    except Exception:
        print("[FAIL] Autocomplete panel did not appear.", file=sys.stderr)
        return passed

    panel = page.locator("#trackingForm\\:trackingIdInput_panel")

    firstOption = panel.locator("li", has_text=package_id)
    firstOption.click()

    page.get_by_role("button", name="Track!").click()

    page.wait_for_selector('div[id$="result"]', timeout=1000)

    # Ensure page contents
    if "Loaded onto voyage 0100S in Hong Kong" in page.content():
        print(
            "[PASS] Package tracking page loaded successfully and contains expected text."
        )
        passed = 1
    else:
        print(
            "[FAIL] Package tracking page did not contain expected text.",
            file=sys.stderr,
        )

    return passed


def view_itinerary(page: Page, package_id: str = "ABC123") -> int:
    passed = 0

    with page.expect_navigation():
        page.get_by_text(package_id).click()

    # Ensure page contents
    if f"Routing Details for Cargo {package_id}" in page.content():
        print("[PASS] Itinerary page loaded successfully and contains expected text.")
        passed = 1
    else:
        print(
            "[FAIL] Itinerary page did not contain expected text.",
            file=sys.stderr,
        )

    return passed


def view_set_cargo_route(page: Page, package_id: str = "DEF789") -> int:
    passed = 0

    with page.expect_navigation():
        page.get_by_text(package_id).click()

    # Ensure page contents
    if f"Set Route for Cargo {package_id}" in page.content():
        print("[PASS] Set route page loaded successfully and contains expected text.")
        passed = 1
    else:
        print(
            "[FAIL] Set route page did not contain expected text.",
            file=sys.stderr,
        )

    return passed


def count_not_routed_packages(page: Page) -> int:
    selector = "#mainDash\\:tableNotRouted_data"
    page.wait_for_selector(selector)
    rows = page.locator(f"{selector} tr")

    return rows.count()


def select_option_and_continue(page: Page, option_text: str, expected_text: str):
    combobox = page.get_by_role("combobox")
    combobox.click()

    for _ in range(10):
        option = page.get_by_role("option", name=option_text, exact=True)
        if option.is_visible():
            # the dropdown is flaky, so we add a delay
            time.sleep(0.5)
            option.click()
            break
        time.sleep(0.5)
    else:
        raise RuntimeError(f"Option '{option_text}' not visible after retries.")

    page.get_by_role("button", name="Next").click()

    page.wait_for_selector(f"text={expected_text}", timeout=3000)


def select_first_of_next_month_and_book(page: Page):
    # Step 1. Go to the next month
    next_button = page.locator(".ui-datepicker-next:not(.ui-state-disabled)")
    next_button.click()
    page.wait_for_timeout(500)  # allow the calendar to update

    # Step 2. Select the 1st day of the visible month
    page.locator("a.ui-state-default", has_text="1").first.click()

    # Step 3. Wait for the "Book Cargo" button to become enabled
    book_button = page.get_by_role("button", name="Book Cargo")
    expect(book_button).to_be_enabled(timeout=5000)

    # Step 4. Click "Book Cargo"
    book_button.click()

    page.wait_for_selector("text=Routed Cargo", timeout=5000)


def add_package(page: Page, previous_package_count: int) -> int:
    passed = 0

    page.get_by_text("Book").click()
    page.wait_for_selector("text=1. Choose the origin of the new cargo", timeout=5000)

    select_option_and_continue(
        page=page,
        option_text="Tokyo",
        expected_text="2. Set the destination for this new cargo coming from Tokyo",
    )
    select_option_and_continue(
        page=page,
        option_text="Hong Kong",
        expected_text="3. Set the arrival deadline for this new Tokyo-Hong Kong cargo",
    )
    select_first_of_next_month_and_book(page)

    packages = count_not_routed_packages(page)

    if previous_package_count + 1 == packages:
        print("[PASS] Package added correctly.")
        passed = 1
    else:
        print("[FAIL] Package adding failed.", file=sys.stderr)

    return passed


def update_cargo(page: Page) -> int:
    passed = 0

    with page.context.expect_page() as new_page_info:
        page.get_by_text("Event Logging Interface").click()

    update_page = new_page_info.value
    update_page.wait_for_selector("text=Tracking ID", timeout=2000)

    select_option_and_continue(
        page=update_page, option_text="ABC123", expected_text="Location"
    )

    select_option_and_continue(
        page=update_page, option_text="New York (USNYC)", expected_text="Event Type"
    )

    select_option_and_continue(
        page=update_page, option_text="LOAD", expected_text="Voyage"
    )

    select_option_and_continue(
        page=update_page, option_text="0200T", expected_text="Completion Date"
    )

    # keep the date, too much work to simulate it
    update_page.get_by_role("button", name="Next").click()
    update_page.wait_for_selector("text=Confirmation", timeout=2000)

    update_page.get_by_role("button", name="Submit").click()

    growl_container = update_page.locator("[id$='growl_container']")
    expect(growl_container).to_be_visible(timeout=2000)

    message_text = growl_container.inner_text()

    if "Event submitted" in message_text:
        print("[PASS] Event submitted correctly.")
        passed = 1
    else:
        print("[FAIL] Event submission failed.", file=sys.stderr)

    update_page.close()
    page.bring_to_front()

    return passed


def validate_origin_and_destination(origin: str, destination: str, data: dict) -> bool:
    edges = data.get("transitEdges")
    first_origin = edges[0].get("fromUnLocode")
    last_destination = edges[-1].get("toUnLocode")

    return first_origin == origin and last_destination == destination


def get_shortest_path(
    origin: str = "CNHKG", destination: str = "USNYC", deadline: str = "10102021"
) -> bool:
    params = {
        "origin": origin,
        "destination": destination,
        "deadline": deadline,
    }

    query = urllib.parse.urlencode(params)
    url = f"{BASE_URL}/rest/graph-traversal/shortest-path?{query}"
    headers = {"Accept": "application/json"}

    req = Request(url, headers=headers, method="GET")

    try:
        with urlopen(req) as resp:
            status = resp.getcode()
            body = resp.read().decode("utf-8")
    except HTTPError as e:
        status = e.code
        body = e.read().decode("utf-8")
    except URLError as e:
        print(f"[FAIL] GET {url} failed: {e}", file=sys.stderr)
        return False

    if status != 200:
        print(f"[FAIL] Unexpected HTTP status {status}", file=sys.stderr)
        return False

    try:
        # The quarkus XML serialization did not work without a wrapper object
        body = json.loads(body).get("transitPaths")
    except Exception:
        print("[FAIL] Serialization failed.", file=sys.stderr)
        return False

    if not all(
        validate_origin_and_destination(
            origin=origin, destination=destination, data=elem
        )
        for elem in body
    ):
        print("[FAIL] Origin and destination validation failed.", file=sys.stderr)
        return False

    print(f"[PASS] GET {url} -> {status}")
    return True


def post_update_cargo():
    url = f"{BASE_URL}/rest/handling/reports"

    completion_time = (datetime.now() - timedelta(hours=2)).strftime(
        "%-m/%-d/%Y %-I:%M %p"
    )

    payload = {
        "completionTime": completion_time,
        "trackingId": "ABC123",
        "eventType": "LOAD",
        "unLocode": "USNYC",
        "voyageNumber": "0200T",
    }

    data = json.dumps(payload).encode("utf-8")
    headers = {
        "Content-Type": "application/json",
        "Accept": "application/json",
    }

    req = Request(url, data=data, headers=headers, method="POST")

    try:
        with urlopen(req) as resp:
            status = resp.getcode()
    except HTTPError as e:
        status = e.code
    except URLError as e:
        print(f"[FAIL] POST {url} failed: {e}", file=sys.stderr)
        return False

    if status not in (200, 204):
        print(f"[FAIL] Unexpected HTTP status {status}", file=sys.stderr)
        return False

    print(f"[PASS] POST {url} -> {status}")
    return True


def main() -> int:
    print(f"---[ {datetime.now().strftime('%H:%M:%S')} - Smoke test ]---")
    num_tests = 0
    passed_tests = 0
    with sync_playwright() as p:
        browser = p.chromium.launch(headless=True)
        page = browser.new_page()

        num_tests += 1
        passed_tests += visit_main_page(page)

        num_tests += 1
        # Navigate to public tracking page
        passed_tests += visit_link(
            page,
            link_text="Public Tracking Interface",
            expected_content="Enter your tracking ID:",
        )

        num_tests += 1
        # Track package
        passed_tests += track_package(page)

        num_tests += 1
        # Back to main page
        passed_tests += visit_main_page(page)

        num_tests += 1
        # Navigate to admin dashboard page
        passed_tests += visit_link(
            page,
            link_text="Administration",
            expected_content="Routed Cargo",
        )

        num_tests += 1
        # View itinerary
        passed_tests += view_itinerary(page)

        num_tests += 1
        # Use side menu to go to admin dashboard page
        passed_tests += visit_link(
            page,
            link_text="Dashboard",
            expected_content="Routed Cargo",
        )

        num_tests += 1
        # View set cargo route page
        passed_tests += view_set_cargo_route(page)

        num_tests += 1
        # Use side menu to go to admin dashboard page
        passed_tests += visit_link(
            page,
            link_text="Dashboard",
            expected_content="Routed Cargo",
        )

        num_tests += 1
        # View itinerary
        passed_tests += view_itinerary(page, package_id="MNO456")

        num_tests += 1
        # Navigate to admin tracking page
        passed_tests += visit_link(
            page,
            link_text="Track",
            expected_content="Enter your tracking ID:",
        )

        num_tests += 1
        # Navigate to admin tracking page
        passed_tests += track_admin_package(page)

        num_tests += 1
        # Use side menu to go to admin dashboard page
        passed_tests += visit_link(
            page,
            link_text="Dashboard",
            expected_content="Routed Cargo",
        )

        not_routed_packages = count_not_routed_packages(page)

        num_tests += 1
        # Book new cargo
        passed_tests += add_package(page, previous_package_count=not_routed_packages)

        num_tests += 1
        # Navigate to live map page
        passed_tests += visit_link(
            page,
            link_text="Live",
            expected_content="iframe",
        )

        num_tests += 1
        # go back to main page
        passed_tests += visit_main_page(page)

        num_tests += 1
        # update cargo
        passed_tests += update_cargo(page)

    num_tests += 1
    passed_tests += int(get_shortest_path())

    num_tests += 1
    passed_tests += int(post_update_cargo())

    print(f"Summary: {passed_tests}/{num_tests} tests passed.")
    print(f"---[ {datetime.now().strftime('%H:%M:%S')} - Smoke test complete ]---")
    return 0 if num_tests == passed_tests else 1


if __name__ == "__main__":
    sys.exit(main())
