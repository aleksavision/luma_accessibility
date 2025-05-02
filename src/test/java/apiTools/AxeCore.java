package apiTools;

import com.codeborne.selenide.WebDriverRunner;
import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.selenium.AxeBuilder;
import com.deque.html.axecore.selenium.AxeReporter;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.testng.Assert.assertTrue;

public class AxeCore {

    private Results results;

    @Step("Simple Accessibility check with axe-core")
    public void simpleCheckAccessibility() {
        AxeBuilder axeBuilder = new AxeBuilder();

        results = axeBuilder.analyze(WebDriverRunner.getWebDriver());

        getCriticalErrors(results);

        logViolations(results.getViolations());
        AxeReporter.writeResultsToJsonFile("build/axe-results", results);
        addResultsToAllure();
        assertTrue(
                results.getViolations().isEmpty(),
                "Accessibility violations found:\n" +
                        AxeReporter.getReadableAxeResults("AccessibilityTest", WebDriverRunner.getWebDriver(), results.getViolations())
        );

    }

    @Step("Accessibility check with included/excluded selectors")
    public void checkAccessibilityBySelector(String includeSelectors, String excludeSelectors) {
        AxeBuilder axeBuilder = new AxeBuilder();

        if (includeSelectors != null && !includeSelectors.isEmpty()) {
            axeBuilder.include(includeSelectors);
        }

        if (excludeSelectors != null && !excludeSelectors.isEmpty()) {
            axeBuilder.exclude(excludeSelectors);
        }

        results = axeBuilder.analyze(WebDriverRunner.getWebDriver());
        logViolations(results.getViolations());
        AxeReporter.writeResultsToJsonFile("build/axe-results", results);
        addResultsToAllure();
        assertTrue(
                results.getViolations().isEmpty(),
                "Accessibility violations found:\n" +
                        AxeReporter.getReadableAxeResults("AccessibilityTest", WebDriverRunner.getWebDriver(), results.getViolations())
        );
    }

    @Step("Accessibility check with rule {rule}")
    public void checkAccessibilityByRule(String rule) {
        AxeBuilder axeBuilder = new AxeBuilder();

        if (rule != null && !rule.isEmpty()) {
            axeBuilder.withRules(Collections.singletonList(rule));
        }

        results = axeBuilder.analyze(WebDriverRunner.getWebDriver());
        logViolations(results.getViolations());
        AxeReporter.writeResultsToJsonFile("build/axe-results", results);
        addResultsToAllure();
        assertTrue(
                results.getViolations().isEmpty(),
                "Accessibility violations found:\n" +
                        AxeReporter.getReadableAxeResults("AccessibilityTest", WebDriverRunner.getWebDriver(), results.getViolations())
        );
    }

    @Step("Accessibility check with rule {rule}")
    public void checkAccessibilityByRules(List<String> rules) {
        AxeBuilder axeBuilder = new AxeBuilder();

        if (rules != null && !rules.isEmpty()) {
            axeBuilder.withRules(rules);
        }

        results = axeBuilder.analyze(WebDriverRunner.getWebDriver());
        logViolations(results.getViolations());
        AxeReporter.writeResultsToJsonFile("build/axe-results", results);
        addResultsToAllure();
        assertTrue(
                results.getViolations().isEmpty(),
                "Accessibility violations found:\n" +
                        AxeReporter.getReadableAxeResults("AccessibilityTest", WebDriverRunner.getWebDriver(), results.getViolations())
        );
    }

    @Step("Accessibility check without rule")
    public void checkAccessibilityByNoRule(String rule) {
        AxeBuilder axeBuilder = new AxeBuilder();

        if (rule != null && !rule.isEmpty()) {
            axeBuilder.disableRules(Collections.singletonList(rule));
        }

        results = axeBuilder.analyze(WebDriverRunner.getWebDriver());
        logViolations(results.getViolations());
        AxeReporter.writeResultsToJsonFile("build/axe-results", results);
        addResultsToAllure();
        assertTrue(
                results.getViolations().isEmpty(),
                "Accessibility violations found:\n" +
                        AxeReporter.getReadableAxeResults("AccessibilityTest", WebDriverRunner.getWebDriver(), results.getViolations())
        );
    }

    @Step("Accessibility check with tag {tag}")
    public void checkAccessibilityByTag(String tag) {
        AxeBuilder axeBuilder = new AxeBuilder();

        if (tag != null && !tag.isEmpty()) {
            axeBuilder.disableRules(Collections.singletonList(tag));
        }

        results = axeBuilder.analyze(WebDriverRunner.getWebDriver());
        logViolations(results.getViolations());
        AxeReporter.writeResultsToJsonFile("build/axe-results", results);
        addResultsToAllure();
        assertTrue(
                results.getViolations().isEmpty(),
                "Accessibility violations found:\n" +
                        AxeReporter.getReadableAxeResults("AccessibilityTest", WebDriverRunner.getWebDriver(), results.getViolations())
        );
    }

    @Step("Accessibility check with tags")
    public void checkAccessibilityByTags(List<String> tags) {
        AxeBuilder axeBuilder = new AxeBuilder();

        if (tags != null && !tags.isEmpty()) {
            axeBuilder.disableRules(tags);
        }

        results = axeBuilder.analyze(WebDriverRunner.getWebDriver());
        logViolations(results.getViolations());
        AxeReporter.writeResultsToJsonFile("build/axe-results", results);
        addResultsToAllure();
        assertTrue(
                results.getViolations().isEmpty(),
                "Accessibility violations found:\n" +
                        AxeReporter.getReadableAxeResults("AccessibilityTest", WebDriverRunner.getWebDriver(), results.getViolations())
        );
    }





    private void logViolations(List<?> violations) {
        if (!violations.isEmpty()) {
            System.out.println("❌ Accessibility violations found:");
            results.getViolations().forEach(v -> {
                System.out.println("--- " + v.getHelp());
                System.out.println("  Impact: " + v.getImpact());
                System.out.println("  Description: " + v.getDescription());
                v.getNodes().forEach(node -> {
                    System.out.println("    Affected element: " + node.getTarget());
                    System.out.println("    Failure summary: " + node.getFailureSummary());
                });
            });
        } else {
            System.out.println("✅ No accessibility violations found.");
        }
    }

    private void addResultsToAllure() {
        try {
            byte[] jsonBytes = Files.readAllBytes(Paths.get("build/axe-results.json"));
            Allure.addAttachment("AXE Accessibility Report", "application/json",
                    new ByteArrayInputStream(jsonBytes), ".json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getCriticalErrors(Results results){
        results.getViolations().stream()
                .filter(v -> "critical".equalsIgnoreCase(v.getImpact()) || "serious".equalsIgnoreCase(v.getImpact()))
                .forEach(v -> {
                    System.out.println("⚠️ Critical/Serious Violation Found:");
                    System.out.println("- " + v.getHelp());
                    System.out.println("  Impact: " + v.getImpact());
                    System.out.println("  Description: " + v.getDescription());
                });
    }


}
