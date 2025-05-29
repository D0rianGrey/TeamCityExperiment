import { type Page, type Locator } from '@playwright/test';

export class GoogleSearchPage {
    readonly page: Page;
    readonly acceptCookiesButton: Locator;
    readonly searchInput: Locator;
    readonly searchButton: Locator;

    constructor(page: Page) {
        this.page = page;
        // Using a broader regex for cookie button as it can change based on region/language
        this.acceptCookiesButton = page.getByRole('button', { name: /Alle akzeptieren|Accept all|Прийняти все|Принять все/i });
        this.searchInput = page.locator('input[name="q"], textarea[name="q"]'); // More robust selector for Google search input
        // The search button name might also vary by language. "Google Suche" is German.
        // "Google Search" is English. "Пошук Google" (Ukrainian), "Поиск в Google" (Russian)
        this.searchButton = page.getByRole('button', { name: /Google Suche|Google Search|Пошук Google|Поиск в Google/i });
    }

    async goto() {
        await this.page.goto('https://www.google.com');
    }

    async acceptCookiesIfPresent() {
        try {
            await this.acceptCookiesButton.waitFor({ timeout: 5000 }); // Wait for a short period
            await this.acceptCookiesButton.click();
        } catch (e) {
            console.log("Google consent button not found or timed out, continuing...");
            // If the button isn't found after a short wait, assume it's not needed or already handled.
        }
    }

    async performSearch(searchTerm: string) {
        await this.searchInput.fill(searchTerm);
        await this.searchButton.click();
    }
}
