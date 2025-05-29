# Testing Guidelines

## 1. Overview of Testing Strategy

This project uses a combination of UI and API tests to ensure application quality. Our goal is to have a comprehensive suite encompassing different types of tests, including unit, integration, UI (End-to-End), and API tests, to catch issues at various levels of the application stack.

## 2. Testing Frameworks Used

### Java (Selenide + TestNG + REST Assured)

*   **Purpose:**
    *   Backend API testing (using REST Assured).
    *   Potentially for certain types of UI tests (using Selenide) or integration tests that benefit from the Java ecosystem.
*   **Key Features:**
    *   Robust environment configuration for test execution (local, remote, Selenoid).
    *   Integrated with Allure for comprehensive test reporting.
    *   Encourages Page Object Model (POM) for UI tests to enhance maintainability.

### Playwright (TypeScript)

*   **Purpose:**
    *   Primary framework for End-to-End (E2E) UI testing.
*   **Key Features:**
    *   Strong cross-browser testing capabilities (currently configured primarily for Chromium).
    *   Built-in API testing utilities.
    *   Integrated with Allure for test reporting.
    *   Encourages Page Object Model (POM) for UI tests.
    *   Conditional headless execution (headless in CI, headed locally).

## 3. Environment Setup

### Java/Maven

*   **Prerequisites:**
    *   JDK 17 (or as specified in `pom.xml`).
    *   Apache Maven installed.
*   **Dependencies:** Managed in the root `pom.xml` file.
*   **Configuration:**
    *   Base test configurations (like URL, environment type) are managed via `src/main/resources/default.properties`.
    *   These properties can be overridden by Java System Properties at runtime (e.g., `-Denv=LOCAL_CHROME`), as handled by `org.example.utils.PropertiesConfigurator.java`.

### Playwright/TypeScript

*   **Prerequisites:**
    *   Node.js (check `Playwright/package.json` for specific engine versions if any).
    *   npm (usually comes with Node.js).
*   **Location:** All Playwright-related code (tests, page objects, configuration) is within the `Playwright/` directory.
*   **Installation:**
    1.  Navigate to the `Playwright/` directory: `cd Playwright`
    2.  Install project dependencies: `npm install` (reads `Playwright/package.json`)
    3.  Install Playwright browser binaries: `npx playwright install`
    4.  Install OS-level dependencies for browsers (especially on Linux/CI): `npx playwright install-deps` (may require `sudo` on local Linux machines).

## 4. Writing New Tests

### General Guidelines

*   **Clarity & Maintainability:** Write tests that are easy to understand and maintain.
*   **Descriptive Names:** Use clear and descriptive names for test files, test suites (describe blocks), and test methods/cases (test blocks).
*   **Independence:** Aim for tests that can run independently of each other to avoid cascading failures and enable parallel execution.

### Java API Tests (REST Assured)

*   **Location:** `src/test/java/apiTests/`
*   **Structure:**
    *   Use TestNG annotations (`@Test`, `@BeforeClass`, etc.).
    *   Utilize REST Assured's BDD-style syntax (`given().when().then()`).
    *   Employ Hamcrest matchers for assertions on response body and status.
*   **Example:** Refer to `src/test/java/apiTests/UserAPITests.java`.

### Java UI Tests (Selenide)

*   **Location:** `src/test/java/` (for main test classes, e.g., `GoogleTests.java`).
*   **Page Object Model (POM):**
    *   **Strongly Recommended.**
    *   Create page-specific classes in `src/test/java/pages/`.
    *   Encapsulate element locators (`SelenideElement`) and interaction methods within these page classes.
    *   **Example:** Refer to `src/test/java/pages/GoogleSearchPage.java`.
*   **Base Class:** Tests should extend `BaseTest.java` to inherit common setup (like browser configuration and environment handling).

### Playwright UI Tests (TypeScript)

*   **Location:** `Playwright/tests/`
*   **Structure:**
    *   Use Playwright Test's `test` and `expect` functions.
*   **Page Object Model (POM):**
    *   **Strongly Recommended.**
    *   Create page-specific classes in `Playwright/page-object/` (e.g., `Playwright/page-object/google/GoogleSearchPage.ts`).
    *   Encapsulate locators and interaction methods within these page classes.
*   **Example:** Refer to `Playwright/tests/ai.spec.ts` and its associated (though not explicitly named here) page objects if it were fully refactored to POM. The `Playwright/tests/api.spec.ts` uses `Playwright/page-object/google/GoogleSearchPage.ts` for its UI part.

### Playwright API Tests (TypeScript)

*   **Location:** Can be included in `Playwright/tests/` (e.g., `api.spec.ts`).
*   **Usage:**
    *   Utilize the `request` fixture provided by Playwright Test for making API calls.
    *   Use `expect` for assertions on the API response (status, body).
*   **Example:** Refer to the API tests within `Playwright/tests/api.spec.ts`.

## 5. Running Tests

### Java/Selenide/REST Assured (Maven)

*   **From Project Root:**
    *   Run all tests: `mvn clean test`
    *   **Specify Environment:** Use Java System Properties to set the execution environment. The `env` property is key.
        *   Example: `mvn clean test -Denv=LOCAL_CHROME`
        *   Available environments are defined in `org.example.utils.Environment.java` (e.g., `LOCAL_CHROME`, `LOCAL_FIREFOX`, `SELENOID_CHROME`, `SELENOID_FIREFOX`). These are read by `BaseTest.java`.

### Playwright (TypeScript)

*   **Navigate to `Playwright/` directory first:** `cd Playwright`
*   **Run All Tests:**
    *   `npm test` (if `scripts.test` in `package.json` is configured for `playwright test`)
    *   OR `npx playwright test`
*   **Run a Specific Test File:**
    *   `npm test -- tests/your-spec-file.spec.ts` (Note the `--` to pass args to the npm script)
    *   OR `npx playwright test tests/your-spec-file.spec.ts`
*   **Execution Mode:**
    *   **Local:** By default (when `CI` environment variable is not set or falsy), tests run in **headed** mode due to `headless: !!process.env.CI` in `Playwright/playwright.config.ts`.
    *   **CI:** When a `CI` environment variable is set to a truthy value (e.g., `CI=true`), tests run in **headless** mode.

## 6. Viewing Reports

### Allure Reports

Both Java/Maven and Playwright test suites are configured (or can be easily configured) to generate Allure test reports.

#### Java/Maven (TestNG + REST Assured + Selenide)

*   **Results Directory:** Allure results are typically generated in the `allure-results` directory in the project root.
*   **Generate and Serve Report (after `mvn clean test`):**
    *   `mvn allure:serve`
*   **Generate HTML Report Only (after `mvn clean test`):**
    *   `mvn allure:report` (This generates the report in `target/site/allure-maven-plugin/`)
    *   You would then need to open the `index.html` from that directory manually or serve it with a local web server.

#### Playwright (TypeScript)

*   **Results Directory:** Allure results are generated in `Playwright/allure-results/`.
*   **Generate and Open Report (from `Playwright/` directory):**
    1.  Ensure `allure-commandline` is installed globally or available in your PATH.
    2.  Run the following script (can be added to `Playwright/package.json`):
        *   `npm run allure-report`
        *   (This assumes a script like `"allure-report": "allure generate allure-results --clean -o allure-report && allure open allure-report"` is added to `Playwright/package.json`)
    3.  **Manual Generation (from `Playwright/` directory):**
        *   Generate: `allure generate allure-results --clean -o allure-report`
        *   Open: `allure open allure-report`

---
This document provides a starting point and should be updated as the testing strategy and frameworks evolve.
