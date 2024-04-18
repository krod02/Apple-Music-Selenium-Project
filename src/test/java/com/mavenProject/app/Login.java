package com.mavenProject.app;
import org.testng.annotations.*;
import org.testng.Assert;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

public class Login {

    private WebDriver driver;
    private Properties properties;

    @BeforeMethod
    public void setUp() {
        driver = DriverManager.getDriver();
        driver.get("https://music.apple.com/us/login");
    }

    @BeforeMethod
    public void ConfigLoader() {// Method to load the config file
        properties = new Properties();
        try {
            properties.load(new FileInputStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to get a property from the config file
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    @Test
    public void testLogin() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Wait for the page to load
        wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));

        // Switch to the first iframe
        try {
            WebElement firstIframe = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//iframe")));
            driver.switchTo().frame(firstIframe);
        } catch (Exception e) {
            System.out.println("No iframe found, continuing with default content.");
        }

        // Enter the username
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("accountName")));
        usernameField.sendKeys(getProperty("USERNAME"));

        // Click on the continue button
        WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Continue']")));
        continueButton.click();

        Thread.sleep(4000);

        // switching to second iframe
        try {
            WebElement secondIframe = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("aid-auth-widget-iFrame")));
            driver.switchTo().frame(secondIframe);
        } catch (Exception e) {
            throw new AssertionError("Second iframe with ID 'aid-auth-widget-iFrame' not found within the timeout period.", e);
        }

        // Click on the continue with password button
        WebElement continueWithPasswordButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("continue-password")));
        continueWithPasswordButton.click();

        // Enter the password
        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password_text_field")));
        passwordField.sendKeys(getProperty("PASSWORD"));

        // Click on the login button
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("sign-in")));
        loginButton.click();

         //Checking to see if two-factor authentication appears
         WebElement specificDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/apple-auth/div/div[1]/div/div/hsa2-sk7/div/div[1]")));
         Assert.assertTrue(specificDiv.isDisplayed(), "Login was not successful as two-factor authentication did not appear");

        driver.switchTo().defaultContent();

    }

    @AfterMethod
    private void waitForTwoFactor() {
        try {
            // Wait for 15 seconds to allow manual entry of two-factor authentication
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Interrupted while waiting for two-factor authentication");
        }
    }
}
