package tools;

import org.openqa.selenium.By;

public class LocatorParser {
    public static By parseLocator(By by, Object... args) {
        String locatorFormat = locatorPattern(by, args);
        return getByWithType(locatorFormat, by);
    }

    private static String locatorPattern(By by, Object... args) {
        String str = by.toString();
        return String.format(str.replaceAll("By\\.[^:]*:", "").trim(), args);
    }

    private static By getByWithType(String locatorFormat, By by) {
        String locatorType = by.getClass().getSimpleName();
        return switch (locatorType) {
            case "CssSelector" -> By.cssSelector(locatorFormat);
            case "Id" -> By.id(locatorFormat);
            case "Name" -> By.name(locatorFormat);
            case "ClassName" -> By.className(locatorFormat);
            case "TagName" -> By.tagName(locatorFormat);
            default -> By.xpath(locatorFormat);
        };
    }
}
