import { expect, Locator, Page } from '@playwright/test';

export class FirstPage {
    readonly page: Page;
    readonly startedLink: Locator;
    readonly heading: Locator;

    constructor(page: Page) {
        this.page = page;
        this.startedLink = page.getByRole('link', { name: 'Get started' });
        this.heading = page.getByRole('heading', { name: 'Installation' });
    }

    async goToWebsite() {
        await this.page.goto('https://playwright.dev/');
    }

    async clickStartedLink() {
        await this.startedLink.click();
    }

    async checkHeading() {
        await expect(this.heading).toBeVisible();
    }
}