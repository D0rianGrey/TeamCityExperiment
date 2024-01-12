import {expect, test} from "@playwright/test";
import { ai } from '@zerostep/playwright'

test('eugene 1', async ({page}) => {
    await page.goto('https://playwright.dev/');
});
