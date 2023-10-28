import {expect, test} from "@playwright/test";

test('eugene 1', async ({page}) => {
    await page.goto('https://playwright.dev/');
});
