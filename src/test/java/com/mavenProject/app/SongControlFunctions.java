package com.mavenProject.app;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import static com.mavenProject.app.DriverManager.getDriver;


public class SongControlFunctions {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        driver = getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void playNextSong() throws InterruptedException {
        Thread.sleep(5000);

        //grab current song title
        WebElement titleShadowHost = driver.findElement(By.cssSelector("amp-lcd"));
        SearchContext titleShadowRoot = expandRootElement(titleShadowHost);
        String currTitle = titleShadowRoot.findElement(By.cssSelector("div.lcd__track-info-container.lcd__active > amp-lcd-metadata > div > div > amp-marquee-text > div > div > div > div > div > div:nth-child(1) > span > span:nth-child(1)")).getText();

        // Access the first shadow DOM
        WebElement shadowHost1 = driver.findElement(By.cssSelector("amp-chrome-player"));
        SearchContext shadowRoot1 = expandRootElement(shadowHost1);

        // Access the nested shadow DOM from within the first shadow root
        WebElement shadowHost2 = shadowRoot1.findElement(By.cssSelector("apple-music-playback-controls"));
        SearchContext shadowRoot2 = expandRootElement(shadowHost2);

        //Access third nested shadow DOM
        WebElement shadowHost3 = shadowRoot2.findElement(By.cssSelector("div > div.music-controls__main > amp-playback-controls-item-skip.next"));
        SearchContext shadowRoot3 = expandRootElement(shadowHost3);

        //click next button
        WebElement nextButton = shadowRoot3.findElement(By.cssSelector("button"));
        nextButton.click();
        Thread.sleep(4000);
        String nextTitle = titleShadowRoot.findElement(By.cssSelector("div.lcd__track-info-container.lcd__active > amp-lcd-metadata > div > div > amp-marquee-text > div > div > div > div > div > div:nth-child(1) > span > span:nth-child(1)")).getText();

        Assert.assertNotEquals(currTitle, nextTitle, "The same song is still playing");
    }

    @Test
    public void testGoBack() throws InterruptedException{
        Thread.sleep(2000);

        //grab current song title
        WebElement titleShadowHost = driver.findElement(By.cssSelector("amp-lcd"));
        SearchContext titleShadowRoot = expandRootElement(titleShadowHost);
        String currTitle = titleShadowRoot.findElement(By.cssSelector("div.lcd__track-info-container.lcd__active > amp-lcd-metadata > div > div > amp-marquee-text > div > div > div > div > div > div:nth-child(1) > span > span:nth-child(1)")).getText();

        // Access the first shadow DOM
        WebElement shadowHost1 = driver.findElement(By.cssSelector("amp-chrome-player"));
        SearchContext shadowRoot1 = expandRootElement(shadowHost1);

        // Access the nested shadow DOM from within the first shadow root
        WebElement shadowHost2 = shadowRoot1.findElement(By.cssSelector("apple-music-playback-controls"));
        SearchContext shadowRoot2 = expandRootElement(shadowHost2);

        //Access third nested shadow DOM
        WebElement shadowHost3 = shadowRoot2.findElement(By.cssSelector("div > div.music-controls__main > amp-playback-controls-item-skip.previous"));
        SearchContext shadowRoot3 = expandRootElement(shadowHost3);

        //click next button
        WebElement backButton = shadowRoot3.findElement(By.cssSelector("button"));
        backButton.click();
        Thread.sleep(2000);
        backButton.click();
        Thread.sleep(3000);
        String previousTitle = titleShadowRoot.findElement(By.cssSelector("div.lcd__track-info-container.lcd__active > amp-lcd-metadata > div > div > amp-marquee-text > div > div > div > div > div > div:nth-child(1) > span > span:nth-child(1)")).getText();

        Assert.assertNotEquals(currTitle, previousTitle, "The same song is still playing");

    }

    @Test
    public void openLyrics() throws InterruptedException{
        Thread.sleep(2000);

        //click lyrics button
        WebElement lyricsButton = driver.findElement(By.cssSelector("button[aria-label='Lyrics"));
        lyricsButton.click();
        Thread.sleep(3000);

        // Wait until the 'aria-expanded' attribute of the lyrics button is 'true'
        boolean isLyricsExpanded = wait.until(ExpectedConditions.attributeToBe(lyricsButton, "aria-expanded", "true"));

        // Assert that the lyrics are expanded
        Assert.assertTrue(isLyricsExpanded, "Lyrics were not expanded as expected");

        //close lyrics
        lyricsButton.click();
        Thread.sleep(2000);

        //muting music for presentation
        // Access the first shadow DOM
        WebElement shadowHost1 = driver.findElement(By.cssSelector("amp-chrome-player"));
        SearchContext shadowRoot1 = expandRootElement(shadowHost1);
        shadowRoot1.findElement(By.cssSelector("button")).click();
    }

    private SearchContext expandRootElement (WebElement element) {
        return (SearchContext) ((JavascriptExecutor) driver).executeScript (
                "return arguments[0].shadowRoot", element);
    }
}
