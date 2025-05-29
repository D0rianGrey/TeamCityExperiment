import {expect, test} from "@playwright/test";
import { GoogleSearchPage } from '../page-object/google/GoogleSearchPage';

test('mocks Google search request', async ({page}) => {
    const googleSearchPage = new GoogleSearchPage(page);

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
    await googleSearchPage.goto();
    await googleSearchPage.acceptCookiesIfPresent();

    // Perform a search
    await googleSearchPage.performSearch("Playwright");

    // Wait for navigation to complete (mocked route might not cause full navigation,
    // so explicit page.waitForNavigation() might be tricky or unnecessary if
    // the fulfillment of the route is what we are testing)
    // For this test, the key is that the route is fulfilled, and content is checked.

    // Other actions and assertions
    const content = await page.content();
    expect(content).toContain('Mock Search Results for Your Query');
});

const BASE_URL = 'https://jsonplaceholder.typicode.com';

test('API: Get all users', async ({ request }) => {
    const response = await request.get(`${BASE_URL}/users`);
    expect(response.status()).toBe(200);
    const responseBody = await response.json();
    expect(Array.isArray(responseBody)).toBe(true);
    expect(responseBody.length).toBeGreaterThan(0);
    // Optional: check for specific fields in the first user
    if (responseBody.length > 0) {
        expect(responseBody[0]).toHaveProperty('id');
        expect(responseBody[0]).toHaveProperty('name');
        expect(responseBody[0]).toHaveProperty('username');
        expect(responseBody[0]).toHaveProperty('email');
    }
});

test('API: Get a single user', async ({ request }) => {
    const response = await request.get(`${BASE_URL}/users/1`);
    expect(response.status()).toBe(200);
    const responseBody = await response.json();
    expect(responseBody).toHaveProperty('id', 1);
    expect(responseBody).toHaveProperty('name', 'Leanne Graham');
    expect(responseBody).toHaveProperty('username', 'Bret');
    expect(responseBody).toHaveProperty('email', 'Sincere@april.biz');
});


