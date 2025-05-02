package baseTest;

import apiTools.AxeCore;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.asserts.SoftAssert;
import tools.BrowserFactory;
import tools.InfoLogger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseTest extends BrowserFactory {

    protected SoftAssert softAssert;
    protected AxeCore axeCore;
    private final ThreadLocal<String> URL = new ThreadLocal<>();
    private final ThreadLocal<String> browserName = new ThreadLocal<>();
    private final ThreadLocal<Boolean> headless = new ThreadLocal<>();
    private final ThreadLocal<Boolean> holdBrowserOpen = new ThreadLocal<>();

    @BeforeTest
    public void setUpDriver() {
        browserName.set(System.getProperty("browserName", "chrome"));
        headless.set(Boolean.parseBoolean("headless"));
        holdBrowserOpen.set((Boolean.parseBoolean("holdBrowserOpen")));

        setUpBrowser(browserName.get(), headless.get(), holdBrowserOpen.get());
    }

    //install in test
    @Step("{url} is opened in browser")
    protected void start(String url) {
        URL.set(url);
        openBrowser();
        setTools();
        printTestInfo();
    }

    private void setTools() {
        softAssert = new SoftAssert();
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        axeCore = new AxeCore();
    }

    private void openBrowser() {
        String urlAddress = URL.get();
        if (urlAddress != null) {
            Selenide.open(urlAddress);
            WebDriverRunner.getWebDriver().manage().window().maximize();
        } else {
            InfoLogger.logInfo("URL is not set. Please use setUrl() method to set the URL before opening the browser.");
        }
    }

    private void printTestInfo() {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String pageTitle = WebDriverRunner.getWebDriver().getTitle();
        System.out.println("🔍 " + currentDate + " - Testing page: " + pageTitle);
    }

    private void cleanWebsiteData() {
        Selenide.clearBrowserLocalStorage();
        Selenide.clearBrowserCookies();
    }

    @AfterMethod
    protected void closeBrowser(ITestContext context) {
        cleanWebsiteData();
        WebDriverRunner.closeWebDriver();
    }
}


