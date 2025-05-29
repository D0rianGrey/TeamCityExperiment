import {test} from "@playwright/test";

// No longer using ai or auto, so imports and dotenv are removed.

test('ai', async ({page}) => {
    await page.goto('https://playwright.dev/');
    
    // Original: await ai("Click on Get Started button", {page, test});
    await page.getByRole('link', { name: 'Get started' }).click(); // Revert to lowercase 's'
    await page.waitForURL('**/docs/intro'); // Verify navigation to the docs page
    
    // Click the search button/area first to activate/open the input field
    await page.getByRole('button', { name: 'Search' }).click(); 
    
    // Now wait for the actual input field to be ready and interact with it
    await page.getByPlaceholder('Search docs').waitFor(); 

    // Original: await ai("Hover over the 'Search' placeholder", {page,test});
    // Hovering might not be necessary if the click to activate focuses the input, or if fill handles it.
    // await page.getByPlaceholder('Search docs').hover(); 

    // Original: await ai("Click on the field with 'Search' placeholder", {page, test});
    // Clicking the input after it appears might also be redundant if it's already focused.
    // await page.getByPlaceholder('Search docs').click();

    // Original: await ai("Enter 'Tania the best' text to input field", {page, test});
    await page.getByPlaceholder('Search docs').fill('Tania the best');

    // Original: await ai("Press 'Enter' button on the keyboard", {page, test});
    await page.keyboard.press('Enter');

    // Original: await ai("Open webpage 'https://www.google.com.ua/'", {page, test});
    await page.goto('https://www.google.com.ua/');

    // Original: await ai("Scroll page to the bottom", {page, test});
    await page.evaluate(() => window.scrollTo(0, document.body.scrollHeight));

    // Original: await ai("Click button with 'alle akzeptieren' text", {page, test});
    // Using a more general selector, as the exact text might depend on language settings.
    const acceptButtonSelector = page.getByRole('button', { name: /Alle akzeptieren|Accept all|Прийняти все|Принять все/i });
    try {
        await acceptButtonSelector.waitFor({ timeout: 5000 }); // Wait for a short period
        await acceptButtonSelector.click();
    } catch (e) {
        console.log("Google consent button not found or timed out, continuing...");
        // If the button isn't found after a short wait, assume it's not needed and continue.
    }


    // Original: await ai("Click 'Anmelden' button on the top right", {page, test});
    await page.locator('a').filter({ hasText: /Увійти|Войти|Anmelden|Sign in/i }).first().click();

    await page.waitForLoadState('networkidle');
});

// test('auto', async ({page}) => {

//     const options = {
//         // If true, debugging information is printed in the console.
//         // debug: true,
//         // The OpenAI model (https://platform.openai.com/docs/models/overview)
//         model: "gpt-4-1106-preview",
//         // The OpenAI API key
//         // openaiApiKey: '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',
//     };


//     await page.goto('https://playwright.dev/');
//     await auto("Click on Get Started button", {page, test}, options);
//     await auto("is link = https://playwright.dev/docs/intro?", {page, test}, options);
//     // await auto("Click on the field with 'Search' placeholder", {page, test}, options);
//     // await auto("Enter 'Tania the best' text to input field", {page, test}, options);
//     // await auto("Press 'Enter' button on the keyboard", {page, test}, options);
//     // await auto("Open webpage 'https://www.google.com.ua/'", {page, test}, options);
//     // await auto("Scroll page to the bottom", {page, test}, options);
//     // await auto("Click button with 'alle akzeptieren' text", {page, test}, options);
//     // // await ai("Enter 'Tania' text to search field", {page, test});
//     // // await ai("Press 'Enter' button on the keyboard", {page, test});
//     // await auto("Click 'Anmelden' button on the top right", {page, test}, options);
//     // await sleep(10000);
// });
