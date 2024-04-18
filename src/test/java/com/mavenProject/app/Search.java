package com.mavenProject.app;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.time.Duration;
import org.openqa.selenium.WebDriver;



public class Search{
    private WebDriver driver;

    @BeforeMethod
    public void setUp() {

        driver = DriverManager.getDriver();
    }

    @Test
    public void testSearchFunctionality() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement searchBar = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type='search'][placeholder='Search']")));
        searchBar.sendKeys("Drake" + Keys.ENTER);

        // Wait for the search results URL to load
        wait.until(ExpectedConditions.urlToBe("https://music.apple.com/us/search?term=Drake"));

        // Assert the URL to confirm the search page loaded
        String expectedUrl = "https://music.apple.com/us/search?term=Drake";
        String actualUrl = driver.getCurrentUrl();
        Assert.assertEquals(actualUrl, expectedUrl, "The URL after search did not match the expected URL.");

        Thread.sleep(2000);

        // Click the first search result
        WebElement clickSearch = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@href, '/artist/drake')]")));
        clickSearch.click();

        // Wait for the artist page to load
        String expectedUrl2 = "https://music.apple.com/us/artist/drake/271256";
        wait.until(ExpectedConditions.urlToBe(expectedUrl2));  // Ensures the URL is as expected

        String actualUrl2 = driver.getCurrentUrl();
        Assert.assertEquals(actualUrl2, expectedUrl2, "The URL after clicking on the artist did not match the expected URL.");

        Thread.sleep(4000);
    }
}
