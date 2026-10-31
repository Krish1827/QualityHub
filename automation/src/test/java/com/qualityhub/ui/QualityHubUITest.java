
package com.qualityhub.ui;

import com.microsoft.playwright.*;
import com.qualityhub.ui.pages.DashboardPage;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;

import org.junit.jupiter.api.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Epic("QualityHub")
@Feature("Dashboard UI")
class QualityHubUITest {

    private static Playwright playwright;
    private static Browser browser;

    private BrowserContext context;
    private Page page;
    private DashboardPage dashboardPage;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();

        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(true));
    }

    @BeforeEach
    void setUp() {
        context = browser.newContext();
        page = context.newPage();
        dashboardPage = new DashboardPage(page);
        dashboardPage.open();
    }

    @Test
    @Story("Dashboard loading")
    @Description("Verify that the QualityHub dashboard loads and displays its welcome message.")
    void shouldLoadDashboard() {
        assertTrue(page.locator("body").isVisible());
        assertEquals("Overview", dashboardPage.getDashboardHeading().innerText());
        assertTrue(dashboardPage.getWelcomeMessage().isVisible());
    }

    @Test
    @Story("Task creation")
    @Description("Verify that a user can create a task and see it displayed with the selected priority.")
    void shouldCreateTaskSuccessfully() {
        String taskTitle = "UI Test Task " + UUID.randomUUID();

        dashboardPage.clickNewTask();

        dashboardPage.createTask(
                taskTitle,
                "Created using Playwright UI automation.",
                "HIGH");

        dashboardPage.waitForTask(taskTitle);

        assertTrue(dashboardPage.getTask(taskTitle).isVisible());

        assertEquals(
                "HIGH",
                dashboardPage.getTask(taskTitle)
                        .locator("td")
                        .nth(1)
                        .innerText()
                        .trim());
    }

    @Test
    @Story("Task validation")
    @Description("Verify that the task creation form displays a validation error when the title is empty.")
    void shouldShowValidationWhenTaskTitleIsEmpty() {
        dashboardPage.clickNewTask();

        page.getByRole(
                com.microsoft.playwright.options.AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Create task")).click();

        assertTrue(
                dashboardPage.getFormError("Task title is required.").isVisible());
    }

    @Test
    @Story("Task status management")
    @Description("Verify that a task status can be changed from TODO to IN_PROGRESS.")
    void shouldUpdateTaskStatus() {
        String taskTitle = "Status Test " + UUID.randomUUID();

        dashboardPage.clickNewTask();

        dashboardPage.createTask(
                taskTitle,
                "Testing task status updates.",
                "MEDIUM");

        dashboardPage.waitForTask(taskTitle);

        assertTrue(dashboardPage.getTask(taskTitle).isVisible());

        dashboardPage.changeTaskStatus(taskTitle, "IN_PROGRESS");

        assertEquals(
                "IN_PROGRESS",
                dashboardPage.getTaskStatus(taskTitle).inputValue());
    }

    @AfterEach
    void tearDown() {
        if (context != null) {
            context.close();
        }
    }

    @AfterAll
    static void closeBrowser() {
        if (browser != null) {
            browser.close();
        }

        if (playwright != null) {
            playwright.close();
        }
    }
}
