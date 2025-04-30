package tools;

import com.codeborne.selenide.*;
import org.openqa.selenium.*;

import java.io.File;
import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class PageTools {

    private static String getPreviousMethodNameAsText() {
        String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        String replacedMethodName = methodName.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        );
        return replacedMethodName.substring(0, 1).toUpperCase() + replacedMethodName.substring(1).toLowerCase();
    }

    private By byLocator(By by, Object... args) {
        return LocatorParser.parseLocator(by, args);
    }

    /**
     * Typing actions
     **/
    //simple type
    protected void type(By by, String text, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        $(byLocator(by, args)).shouldBe(Condition.visible).sendKeys(text);
    }

    public void typeIntoHiddenElement(String text, By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        String script = "arguments[0].setAttribute('value', arguments[1]);";
        Selenide.executeJavaScript(script, byLocator(by, args), text);
    }

    //со скрытыми элементами или сложными динамическими страницами
    protected void jsType(String text, By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        waitForElementEnabled(byLocator(by, args));
        Selenide.executeJavaScript("arguments[0].value = '" + text + "';", $(byLocator(by, args)).shouldBe(Condition.visible));
    }

    //элемент скрыт или нужно принудительно задать значение
    protected void jsSetAttributeValue(String text, By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        Selenide.executeJavaScript("arguments[0].setAttribute('value', '" + text + "');", $(byLocator(by, args)).shouldBe(Condition.exist));
    }

    //необходимо наведение курсора или нажатие юзером
    protected void typeBySelenideActions(String text, By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        WebElement target = getWebElement(byLocator(by, args));
        Selenide.actions().moveToElement(target).sendKeys(target, text).build().perform();
    }

    protected void uploadFile(String filePath, By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        $(byLocator(by, args)).shouldBe(Condition.enabled).uploadFile(new File(filePath));
    }

    //добавить текст к уже существующему (без замены)
    protected void appendText(String text, By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        $(byLocator(by, args)).shouldBe(Condition.visible).append(text);
    }

    protected void clearText(By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));

        int stringSize = $(byLocator(by, args)).shouldBe(Condition.enabled).getWrappedElement().getAttribute("value").length();

        for (int i = 0; i < stringSize; i++) {
            $(byLocator(by, args)).shouldBe(Condition.enabled).sendKeys(Keys.BACK_SPACE);
        }
    }

    /**
     * Clicking actions
     **/
    protected void click(By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        $(byLocator(by, args)).shouldBe(Condition.visible).click();
    }

    //элемент перекрыт, динам. стр, нестандартные элементы
    protected void jsClick(By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        Selenide.executeJavaScript("arguments[0].click();", $(byLocator(by, args)).shouldBe(Condition.visible));
    }

    //требуется движение курсора мыши перед кликом
    protected void clickByActions(By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        Selenide.actions().moveToElement(getWebElement(byLocator(by, args))).click().perform();
    }

    /**
     * Dropdown actions
     **/
    protected void selectOption(String option, By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        $(byLocator(by, args)).shouldBe(Condition.visible).selectOption(option);
    }

    /**
     * Is-conditions
     */
    protected boolean isElementVisible(By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        return $(byLocator(by, args)).is(Condition.visible);
    }

    protected boolean isElementExist(By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        return $(byLocator(by, args)).is(Condition.exist);
    }

    protected boolean isElementEnabled(By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        return $(byLocator(by, args)).is(Condition.enabled);
    }

    protected boolean isElementDisabled(By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        return $(byLocator(by, args)).is(Condition.disabled);
    }

    //чекбокс или радиокнопка
    protected boolean isElementChecked(By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        return $(byLocator(by, args)).is(Condition.checked);
    }

    protected boolean isElementUnchecked(By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        return !$(byLocator(by, args)).is(Condition.checked);
    }

    /**
     * Waiting actions
     */
    protected void waitForElementClickable(By by, Object... args) {
        $(byLocator(by, args)).shouldBe(Condition.visible).shouldBe(Condition.enabled);
    }

    protected void waitForElementVisible(By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        $(byLocator(by, args)).shouldBe(Condition.visible);
    }

    protected void waitForElementExist(By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        $(byLocator(by, args)).shouldBe(Condition.exist);
    }

    protected void waitForElementEnabled(By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        $(byLocator(by, args)).shouldBe(Condition.enabled);
    }

    protected void waitForElementDisabled(By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        $(byLocator(by, args)).shouldBe(Condition.disabled);
    }

    protected void waitForElementChecked(By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        $(byLocator(by, args)).shouldBe(Condition.checked);
    }

    //загрузка с задержкой
    protected void waitForElementVisibleUntil(By by, long seconds, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        $(byLocator(by, args)).shouldBe(Condition.visible, Duration.ofSeconds(seconds));
    }

    protected void waitForElementInvisibleUntil(By by, long seconds, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        $(byLocator(by, args)).shouldNotBe(Condition.visible, Duration.ofSeconds(seconds));
    }

    /**
     * Get elements actions
     */
    protected SelenideElement getSelenideElement(By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        return $(byLocator(by, args)).shouldBe(Condition.visible);
    }

    protected String getElementText(By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        return $(byLocator(by, args)).shouldBe(Condition.enabled).text();
    }

    protected String getElementAttributeValue(String attr, By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        return $(byLocator(by, args)).shouldBe(Condition.exist).attr(attr);
    }

    protected String getHiddenElementAttributeValue(String attr, By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        return $(byLocator(by, args)).shouldBe(Condition.hidden).attr(attr);
    }

    protected String getDisabledElementAttributeValue(String attr, By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        return $(byLocator(by, args)).shouldBe(Condition.disabled).attr(attr);
    }

    protected List<String> getElementsText(By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        return $$(byLocator(by, args)).texts();
    }

    protected List<String> getElementsTextWithWait(By by, int waitTimeout, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        Selenide.sleep(waitTimeout * 1000L);
        return $$(byLocator(by, args)).texts();
    }

    protected WebElement getWebElement(By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        return WebDriverRunner.getWebDriver().findElement(byLocator(by, args));
    }

    /**
     * Browser actions
     */
    protected void scrollToPlaceElementInCenter(By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        $(byLocator(by, args)).shouldBe(Condition.visible).scrollTo();
//        waitForElementVisible(by, args);
//        Selenide.executeJavaScript("arguments[0].scrollIntoView({block: \"center\"});", getWebElement(byLocator(by, args)));
    }

    protected void switchToIframe(By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        Selenide.switchTo().frame(getWebElement(byLocator(by, args)));
    }

    protected void switchToWindowByIndex(int index) {
        Selenide.switchTo().window(index);
    }

    /**
     * Mine
     */
    protected void clickElement(SelenideElement element) {
        element.shouldBe(visible).click();
    }

    protected void getSelenideElementInFieldset(By fieldset, By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        fieldset.findElement((SearchContext) byLocator(by, args));
    }

    protected void typeIntoElementInFieldset(By fieldset, By by, String text, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        SelenideElement input = $(fieldset).$(by);
        input.shouldBe(Condition.visible).sendKeys(text);
    }

    protected void clearInputFieldset(By fieldset, By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        SelenideElement input = $(fieldset).$(by);
        input.shouldBe(Condition.visible).clear();
    }

    protected boolean isElementInFieldsetVisible(By fieldset, By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        SelenideElement element = $(fieldset).$(byLocator(by, args));
        return element.is(Condition.visible);
    }

    protected void moveToElement(By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        SelenideElement element = $(byLocator(by, args));
        Selenide.actions().moveToElement(element.shouldBe(visible)).perform();
        sleep(500);
    }
    protected void jsSetAttribute(By fieldset, String attributeName, String value, By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        SelenideElement element = $(fieldset).$(by);
        Selenide.executeJavaScript("arguments[0].setAttribute(arguments[1], arguments[2]);", element.shouldBe(visible), attributeName, value);
    }

    protected String getElementFieldsetText(By fieldset, By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        SelenideElement element = $(fieldset).$(byLocator(by, args));
        return element.shouldBe(Condition.enabled).text();
    }



    ///By index
    /**
     *
     * @param index Started from 1
     */
    protected void clickElementByIndex(By by, int index, Object... args){
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        By locator = By.id(("(" + byLocator(by, args) + ")[" + index + "]"));
        click(locator);
        }
    /**
     *
     * @param index Started from 1
     */
    protected String getElementTextByIndex(By by, int index, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        By locator = By.id(("(" + byLocator(by, args) + ")[" + index + "]"));
        return getElementText(locator);
    }
    /**
     *
     * @param index Started from 1
     */
    protected void jsTypeByIndex(String text, int index, By by, Object... args){
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        By locator = By.id(("(" + byLocator(by, args) + ")[" + index + "]"));
        jsType(text, locator);
    }
    /**
     *
     * @param index Started from 1
     */
    protected void jsSetAttributeValueByIndex(String text, int index, By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        By locator = By.id(("(" + byLocator(by, args) + ")[" + index + "]"));
        jsSetAttributeValue(text, locator);
    }
    /**
     *
     * @param index Started from 1
     */
    protected String getElementAttributeValueByIndex(String attr, int index, By by, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        By locator = By.id(("(" + byLocator(by, args) + ")[" + index + "]"));
        return getElementAttributeValue(attr, locator);
    }
    /**
     *
     * @param index Started from 1
     */
    protected void waitForElementHasUntilByIndex(By by, int index, String text, long seconds, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        By locator = By.id("(" + byLocator(by, args) + ")[" + index + "]");
        $(byLocator(locator)).shouldBe(Condition.visible).shouldHave(Condition.text(text), Duration.ofSeconds(seconds));
    }

    protected void waitForElementUntilByIndex(By by, int index, long seconds, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        By locator = By.id("(" + byLocator(by, args) + ")[" + index + "]");
        $(byLocator(locator)).shouldBe(Condition.visible,Duration.ofSeconds(seconds));
    }
    /**
     *
     * @param index Started from 1
     */
    protected void moveToElementByIndex(By by, int index, Object... args) {
        InfoLogger.logInfo(getPreviousMethodNameAsText() + ", element --> " + byLocator(by, args));
        By locator = By.id("(" + byLocator(by, args) + ")[" + index + "]");
        SelenideElement element = $(locator);
        Selenide.actions().moveToElement(element.shouldBe(visible)).perform();
        sleep(500);
    }









}
