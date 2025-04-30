package tools;

import com.codeborne.selenide.Configuration;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.Properties;

public class BrowserFactory {

    protected ThreadLocal<Properties> properties = new ThreadLocal<>();

    public synchronized void setUpBrowser(String browserName, Boolean headlessMode, Boolean holdBrowserOpen) {

        properties.set(new Properties());

        switch (browserName.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                properties.get().setProperty("Chrome.Browser.Version", "last");
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headlessMode != null && headlessMode) {
                    chromeOptions.addArguments("--headless");
                }
                //для повышения стабильности браузера при запуске в Docker c ограниченными ресурсами
                chromeOptions.addArguments("--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage");
                Configuration.browser = "chrome";
                Configuration.browserCapabilities = chromeOptions;
                Configuration.holdBrowserOpen = holdBrowserOpen;
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                properties.get().setProperty("Firefox.Browser.Version", "last");
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headlessMode != null && headlessMode) {
                    firefoxOptions.addArguments("--headless");
                }
                System.out.println("Operating System: " + System.getProperty("os.name"));
                if (System.getProperty("os.name").equalsIgnoreCase("ubuntu")) {
                    firefoxOptions.addArguments("--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage");
                }
                Configuration.browser = "firefox";
                Configuration.browserCapabilities = firefoxOptions;
                Configuration.holdBrowserOpen = holdBrowserOpen;
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                properties.get().setProperty("Edge.Browser.Version", "last");
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headlessMode != null && headlessMode) {
                    edgeOptions.addArguments("--headless");
                }
                edgeOptions.addArguments("--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage");
                Configuration.browser = "edge";
                Configuration.browserCapabilities = edgeOptions;
                Configuration.holdBrowserOpen = holdBrowserOpen;
                break;

            default:
                System.out.println("Browser name is not correct. Please pass a valid browser name!");
                break;
        }
    }
}
