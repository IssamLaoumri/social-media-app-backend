package com.laoumri.selenium;

import com.laoumri.SocialAppApplication;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.SpringApplication;

import java.time.Duration;

public abstract class BaseSeleniumTest {
    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeAll
    public static void startApplicationWithTestProfile() {
        System.setProperty("spring.profiles.active", "test");
        new Thread(() -> SpringApplication.run(SocialAppApplication.class)).start();
    }

    @BeforeEach
    public void setUp() {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(3).getSeconds());
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
