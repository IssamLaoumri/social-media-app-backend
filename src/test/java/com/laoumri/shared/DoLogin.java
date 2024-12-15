package com.laoumri.shared;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DoLogin {
    public static void login(WebDriver driver, WebDriverWait wait) {
        // Open the login page
        driver.get("http://localhost:4200/login");

        // Locate and fill the email field
        WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("email")));
        emailField.sendKeys("abdo@gmail.com");

        // Locate and fill the password field
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("Mq6?W4sd");

        // Locate and click the login button
        WebElement loginButton = driver.findElement(By.id("login_btn"));
        loginButton.click();

        // Wait for the redirect to profile page
        wait.until(ExpectedConditions.urlToBe("http://localhost:4200/profile"));
        System.out.println("HERE the cookies : "+driver.manage().getCookies());
    }
}
