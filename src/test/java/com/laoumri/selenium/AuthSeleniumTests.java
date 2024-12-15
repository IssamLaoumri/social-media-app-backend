package com.laoumri.selenium;

import com.laoumri.shared.DoLogin;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthSeleniumTests extends BaseSeleniumTest{
    @Test
    @Order(2)
    public void testLogin() {
        DoLogin.login(driver, wait);

        // Verify the redirected URL
        String currentUrl = driver.getCurrentUrl();
        assertEquals("http://localhost:4200/profile", currentUrl, "User should be redirected to the profile after login.");
    }

    @Test
    @Order(1)
    public void testSignUp(){
        driver.get("http://localhost:4200/signup");

        WebElement firstnameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("firstname")));
        firstnameField.sendKeys("Abdellah");


        WebElement lastnameField = driver.findElement(By.id("lastname"));
        lastnameField.sendKeys("Smini");

        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys("abdo@gmail.com");

        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("Mq6?W4sd");

        WebElement repeatPasswordField = driver.findElement(By.id("passwordRepeat"));
        repeatPasswordField.sendKeys("Mq6?W4sd");

        // Locate and click the login button
        WebElement signUpButton = driver.findElement(By.id("register_btn"));
        signUpButton.click();
        wait.until(ExpectedConditions.urlToBe("http://localhost:4200/message"));

        String currentUrl = driver.getCurrentUrl();
        assertEquals("http://localhost:4200/message", currentUrl, "User should be redirected to the message after register.");
    }


}
