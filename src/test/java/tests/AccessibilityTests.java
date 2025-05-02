package tests;

import baseTest.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import testData.GlobalData;

import java.util.Arrays;

public class AccessibilityTests extends BaseTest {

    @BeforeMethod
    public void startTest() {
        start(GlobalData.mainURL);
    }

    @Test(groups = {"accessibility"})
    @Description("Check all elements with accessibility")
    @Severity(SeverityLevel.MINOR)
    public void simpleCheck() {
        axeCore.simpleCheckAccessibility();
    }

    @Test(groups = {"accessibility"})
    @Description("Check Category menu with accessibility")
    @Severity(SeverityLevel.MINOR)
    public void checkCategoryMenu() {
        axeCore.checkAccessibilityBySelector(".section-items", null);
    }

    @Test(groups = {"accessibility"})
    @Description("Check all elements without '.product-item-details'")
    @Severity(SeverityLevel.MINOR)
    public void checkWithoutItemDetails() {
        axeCore.checkAccessibilityBySelector(null, ".product-item-details");
    }

    @Test(groups = {"accessibility"})
    @Description("Check all elements with color contrast rule")
    @Severity(SeverityLevel.MINOR)
    public void checkOnlyColorContrast() {
        axeCore.checkAccessibilityByRule("color-contrast");
    }

    @Test(groups = {"accessibility"})
    @Description("Check all elements with color contrast and alt text rules")
    @Severity(SeverityLevel.MINOR)
    public void checkOnlyColorContrastAndAltText() {
        axeCore.checkAccessibilityByRules(Arrays.asList("color-contrast", "image-alt"));
    }

    @Test(groups = {"accessibility"})
    @Description("Check all elements with 'best-practice' accessibility")
    @Severity(SeverityLevel.MINOR)
    public void checkByTagBestPractice() {
        axeCore.checkAccessibilityByTag("best-practice");
    }

    @Test(groups = {"accessibility"})
    @Description("Check all elements with 'section508', 'wcag2aa' accessibility")
    @Severity(SeverityLevel.MINOR)
    public void checkByTags() {
        axeCore.checkAccessibilityByTags(Arrays.asList("section508", "wcag2aa"));
    }

}
