import {expect, test} from "@playwright/test";

test('mocks Google search request', async ({page}) => {
    // Intercept Google search requests
    await page.route('**/search*', route => {
        // Check if the intercepted request is a search request
        if (route.request().url().includes('q=')) {
            // Provide a mock response for the search request
            route.fulfill({
                status: 200,
                contentType: 'text/html',
                body: '<html><body><h1>Mock Search Results for Your Query</h1></body></html>' // Mock HTML response
            });
        } else {
            // Allow other requests to proceed normally
            route.continue();
        }
    });

    // Navigate to Google
    await page.goto('https://www.google.com');
    await page.getByRole('button', {name: 'Alle akzeptieren'}).click();

    // Perform a search
    await page.getByLabel('Suche', {exact: true}).fill("Playwright")
    await page.getByRole('button', {name: 'Google Suche'}).click();

    // Wait for navigation to complete
    // await page.waitForNavigation();

    // Other actions and assertions
    const content = await page.content();
    expect(content).toContain('Mock Search Results for Your Query');
});


