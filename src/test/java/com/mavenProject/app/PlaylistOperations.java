package com.mavenProject.app;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.time.Duration;
import org.openqa.selenium.By;
import static com.mavenProject.app.DriverManager.getDriver;

public class PlaylistOperations {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        driver = getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void navPlaylistPg() throws InterruptedException{
        Thread.sleep(1000);
        driver.findElement(By.linkText("All Playlists")).click();

        Thread.sleep(4000);

        String url = driver.getCurrentUrl();
        String expectedUrl = "https://music.apple.com/us/library/all-playlists/?l=en-US";

        Assert.assertEquals(expectedUrl, url,"The page did not change to the all playlist page");
    }

    @Test
    public void selectPlaylist() throws InterruptedException{
        Thread.sleep(2000);

        driver.findElement(By.xpath("/html/body/div[1]/div[4]/main/div/div/div/div/div/div[3]/div[1]/div[1]/div[3]")).click();

        Thread.sleep(4000);

        String url = driver.getCurrentUrl();
        String expectedUrl = "https://music.apple.com/us/library/playlist/p.6xZagKdIvzg9plv?l=en-US";

        Assert.assertEquals(expectedUrl, url,"The page did not change to the correct playlist page");

    }

    @Test
    public void playPlaylist() throws InterruptedException {
        Thread.sleep(2000);

        //checking current song name
        WebElement titleShadowHost = driver.findElement(By.cssSelector("amp-lcd"));
        SearchContext titleShadowRoot = expandRootElement(titleShadowHost);
        String currTitle = titleShadowRoot.findElement(By.cssSelector("div.lcd__track-info-container.lcd__active > amp-lcd-metadata > div > div > amp-marquee-text > div > div > div > div > div > div:nth-child(1) > span > span:nth-child(1)")).getText();

        driver.findElement(By.xpath("/html/body/div[1]/div[4]/main/div/div[1]/div/div/div[2]/div[1]/div/button")).click();
        Thread.sleep(3000);

        String newTitle = titleShadowRoot.findElement(By.cssSelector("div.lcd__track-info-container.lcd__active > amp-lcd-metadata > div > div > amp-marquee-text > div > div > div > div > div > div:nth-child(1) > span > span:nth-child(1)")).getText();

        Assert.assertNotEquals(currTitle, newTitle, "The same song is still playing");

    }
    private SearchContext expandRootElement (WebElement element) {
        return (SearchContext) ((JavascriptExecutor) driver).executeScript (
                "return arguments[0].shadowRoot", element);
    }
}
