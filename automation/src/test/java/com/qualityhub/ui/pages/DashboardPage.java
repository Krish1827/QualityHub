package com.qualityhub.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class DashboardPage {

    private final Page page;

    public DashboardPage(Page page) {
        this.page = page;
    }

    public void open() {
        page.navigate("http://localhost:5173");
    }

    public Locator getDashboardHeading() {
        return page.locator("h1");
    }

    public Locator getWelcomeMessage() {
        return page.getByText("Welcome to QualityHub");
    }

    public void clickNewTask() {
        page.getByRole(
                com.microsoft.playwright.options.AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("New task")).click();
    }

    public void createTask(String title, String description, String priority) {
        page.getByPlaceholder("e.g. Test user login").fill(title);
        page.getByPlaceholder("Describe the task...").fill(description);

        page.locator(".modal select").selectOption(priority);

        page.getByRole(
                com.microsoft.playwright.options.AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Create task")).click();
    }

    public Locator getTask(String title) {
        return page.locator("tbody tr").filter(
                new Locator.FilterOptions().setHasText(title));
    }

    public void changeTaskStatus(String title, String newStatus) {
        Locator statusDropdown = getTask(title)
                .getByLabel("Status for " + title);

        statusDropdown.selectOption(newStatus);

        page.waitForTimeout(1000);

        statusDropdown = getTask(title)
                .getByLabel("Status for " + title);

        statusDropdown.waitFor(
                new Locator.WaitForOptions()
                        .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE)
                        .setTimeout(10000));
    }

    public Locator getTaskStatus(String title) {
        return getTask(title).getByLabel("Status for " + title);
    }

    public Locator getSearchInput() {
        return page.getByPlaceholder("Search tasks...");
    }

    public Locator getStatusFilter() {
        return page.getByLabel("Filter tasks by status");
    }

    public Locator getEmptyState() {
        return page.getByText("No tasks found");
    }

    public Locator getFormError(String message) {
        return page.getByText(message);
    }

    public void waitForTask(String title) {
    getTask(title).waitFor(
            new Locator.WaitForOptions()
                    .setState(
                            com.microsoft.playwright.options.WaitForSelectorState.VISIBLE
                    )
                    .setTimeout(10000)
    );
}
}