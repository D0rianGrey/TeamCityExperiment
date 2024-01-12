import {test} from "@playwright/test";
import {ai} from '@zerostep/playwright';
import {auto} from 'auto-playwright';

require('dotenv').config();


test('ai', async ({page}) => {
    await page.goto('https://playwright.dev/');
    await ai("Click on Get Started button", {page, test});
    await ai("Hover over the 'Search' placeholder", {page, test});
    await ai("Click on the field with 'Search' placeholder", {page, test});
    await ai("Enter 'Tania the best' text to input field", {page, test});
    await ai("Press 'Enter' button on the keyboard", {page, test});
    await ai("Open webpage 'https://www.google.com.ua/'", {page, test});
    await ai("Scroll page to the bottom", {page, test});
    await ai("Click button with 'alle akzeptieren' text", {page, test});
    // await ai("Enter 'Tania' text to search field", {page, test});
    // await ai("Press 'Enter' button on the keyboard", {page, test});
    await ai("Click 'Anmelden' button on the top right", {page, test});
    await sleep(10000);
});

test('auto', async ({page}) => {

    const options = {
        // If true, debugging information is printed in the console.
        // debug: true,
        // The OpenAI model (https://platform.openai.com/docs/models/overview)
        model: "gpt-4-1106-preview",
        // The OpenAI API key
        // openaiApiKey: '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',
    };


    await page.goto('https://playwright.dev/');
    await auto("Click on Get Started button", {page, test}, options);
    await auto("is link = https://playwright.dev/docs/intro?", {page, test}, options);
    // await auto("Click on the field with 'Search' placeholder", {page, test}, options);
    // await auto("Enter 'Tania the best' text to input field", {page, test}, options);
    // await auto("Press 'Enter' button on the keyboard", {page, test}, options);
    // await auto("Open webpage 'https://www.google.com.ua/'", {page, test}, options);
    // await auto("Scroll page to the bottom", {page, test}, options);
    // await auto("Click button with 'alle akzeptieren' text", {page, test}, options);
    // // await ai("Enter 'Tania' text to search field", {page, test});
    // // await ai("Press 'Enter' button on the keyboard", {page, test});
    // await auto("Click 'Anmelden' button on the top right", {page, test}, options);
    // await sleep(10000);
});

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}
