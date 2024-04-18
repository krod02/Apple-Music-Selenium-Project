package com.mavenProject.app;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverManager {
    private static WebDriver driver;

    public static WebDriver getDriver() {
        if (driver == null) {
            System.out.println("Creating new WebDriver instance");
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
            driver.manage().window().maximize();
        } else {
            System.out.println("Reusing existing WebDriver instance");
        }
        return driver;
    }

    public static void closeDriver() {
        if (driver != null) {
            System.out.println("Closing WebDriver instance");
            driver.quit();
            driver = null;
        }
    }
}
