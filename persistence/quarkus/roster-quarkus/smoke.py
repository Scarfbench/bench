#!/usr/bin/env python3
"""
Smoke tests for roster-quarkus.
"""
import asyncio
import aiohttp
import sys
import time
import os
from playwright.async_api import async_playwright

BASE_URL = "http://localhost:8080/"
START_TIMEOUT = int(os.getenv("START_TIMEOUT", "90"))


async def wait_for_http(host: str, port: int, timeout: int):
    url = f"http://{host}:{port}/roster/players/league/L1"
    async with aiohttp.ClientSession() as session:
        end = time.time() + timeout
        while time.time() < end:
            try:
                async with session.get(url, timeout=5) as resp:
                    print(f"[DEBUG] GET {url} → {resp.status}")
                    if resp.status == 200:
                        print(f"[DEBUG] Application is ready ({url})")
                        return
            except (aiohttp.ClientError, asyncio.TimeoutError) as e:
                print(f"[DEBUG] Waiting for {url} – {e}")
            await asyncio.sleep(0.5)
        raise TimeoutError(f"Timed out waiting for HTTP port {port} after {timeout}s")


async def create_league(session: aiohttp.ClientSession):
    payload = {"id": "L1", "name": "Mountain", "sport": "soccer"}
    async with session.post(f"{BASE_URL}roster/league", json=payload) as resp:
        assert resp.status == 204, f"League creation failed: {resp.status}"
        print("[PASS] League L1 created")

async def create_player_form(session: aiohttp.ClientSession):
    params = {
        "id": "P1",
        "name": "Phil Jones",
        "position": "goalkeeper",
        "salary": "100.00"
    }
    async with session.post(
        f"{BASE_URL}roster/player",
        params=params,
        headers={"Content-Type": "application/x-www-form-urlencoded"}
    ) as resp:
        print(f"[DEBUG] POST /roster/player → {resp.status}")
        assert resp.status in (204, 200), f"Player creation failed: {resp.status}"
        print("[PASS] Player P1 created")

async def create_team(session: aiohttp.ClientSession):
    payload = {"id": "T1", "name": "Honey Bees", "city": "Visalia"}
    async with session.post(f"{BASE_URL}roster/team/league/L1", json=payload) as resp:
        assert resp.status == 204, f"Team creation failed: {resp.status}"
        print("[PASS] Team T1 created")

async def assign_player_to_team(session: aiohttp.ClientSession):
    async with session.post(f"{BASE_URL}roster/player/P1/team/T1") as resp:
        assert resp.status == 204, f"Assign failed: {resp.status}"
        print("[PASS] Player P1 assigned to Team T1")

async def verify_players_list(session: aiohttp.ClientSession):
    async with session.get(f"{BASE_URL}roster/players/league/L1") as resp:
        assert resp.status == 200, f"List failed: {resp.status}"
        text = await resp.text()
        assert "Phil Jones" in text, "Player Phil Jones not found in league L1"
        print("[PASS] Player list contains Phil Jones")


async def test_players_page(page):
    await page.goto(f"{BASE_URL}roster/players/league/L1")
    content = await page.content()
    assert "Phil Jones" in content, "UI does not contain Phil Jones"
    print("[PASS] UI shows Phil Jones")


async def main():
    await wait_for_http("localhost", 8080, START_TIMEOUT)

    async with aiohttp.ClientSession() as session:
        await create_league(session)
        await create_player_form(session)
        await create_team(session)
        await assign_player_to_team(session)
        await verify_players_list(session)

    async with async_playwright() as p:
        browser = await p.chromium.launch(headless=True)
        page = await browser.new_page()
        try:
            await test_players_page(page)
        finally:
            await browser.close()

    print("[PASS] All smoke tests passed")
    return 0


if __name__ == "__main__":
    sys.exit(asyncio.run(main()))