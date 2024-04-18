package com.mavenProject.app;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import static com.mavenProject.app.DriverManager.getDriver;



public class PlayPauseSong {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        driver = getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testPlay() throws InterruptedException {
        // Click the play button to start playing the song
        WebElement playButton = driver.findElement(By.xpath("/html/body/div/div[4]/main/div/div[3]/div[2]/div[2]/section/div[1]/ul/li[1]/div/div/div[1]/div[2]/div/button"));
        playButton.click();
        
        // Wait for the music to start playing and the UI to update
        Thread.sleep(5000);  // Consider replacing with a more robust wait

        // Access the first shadow DOM
        WebElement shadowHost1 = driver.findElement(By.cssSelector("amp-chrome-player"));
        SearchContext shadowRoot1 = expandRootElement(shadowHost1);

        // Access the nested shadow DOM from within the first shadow root
        WebElement shadowHost2 = shadowRoot1.findElement(By.cssSelector("apple-music-playback-controls"));
        SearchContext shadowRoot2 = expandRootElement(shadowHost2);

        // Finally, access the span element within the second shadow root
        WebElement pauseButton = shadowRoot2.findElement(By.cssSelector("div > div.music-controls__main > amp-playback-controls-play > button.playback-play__pause > span"));
        String isSongPlaying = pauseButton.getText();
        // Get the text from the playback button to verify it indicates "PAUSE"
        String expectedText = "PAUSE";
        Assert.assertEquals(isSongPlaying, expectedText, "The expected song is not playing");
    }

    @Test
    public void testPause() throws InterruptedException{
        Thread.sleep(3000);
        // Access the first shadow DOM
        WebElement shadowHost1 = driver.findElement(By.cssSelector("amp-chrome-player"));
        SearchContext shadowRoot1 = expandRootElement(shadowHost1);

        // Access the nested shadow DOM from within the first shadow root
        WebElement shadowHost2 = shadowRoot1.findElement(By.cssSelector("apple-music-playback-controls"));
        SearchContext shadowRoot2 = expandRootElement(shadowHost2);

        //click on pause button
        WebElement pauseButton = shadowRoot2.findElement(By.cssSelector("div > div.music-controls__main > amp-playback-controls-play > button.playback-play__pause"));
        pauseButton.click();

        WebElement playButton = shadowRoot2.findElement(By.cssSelector("div > div.music-controls__main > amp-playback-controls-play > button.playback-play__play > span"));
        String isSongPaused = playButton.getText();
        String expectedText = "PLAY";
        Assert.assertEquals(isSongPaused, expectedText, "The expected song is still playing");
    }


    private SearchContext expandRootElement (WebElement element) {
        return (SearchContext) ((JavascriptExecutor) driver).executeScript (
                "return arguments[0].shadowRoot", element);
    }




}
