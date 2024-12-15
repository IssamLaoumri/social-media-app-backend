package com.laoumri.selenium;

import com.laoumri.shared.DoLogin;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AProfileSeleniumTests extends BaseSeleniumTest{
    @Test
    @Order(3)
    public void testUploadProfilePhoto(){
        DoLogin.login(driver, wait);
        driver.get("http://localhost:4200/profile");
        driver.manage().addCookie(new Cookie("JWT-SESSION", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyczFAZXhhbXBsZS5jb20iLCJleHAiOjE3MzUzOTYxNjAsImlhdCI6MTczNDE4NjU2MCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl19.4oABQd8gcBIrSeKc2zpbBZnaV-RYNkblpgojzn6m6Nsdw-MyFO7zpKWqIzZE3ay-KhmKGs8Z2h3r9E_Ryw8X9Q"));
        WebElement uploadProfileBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("upload_profile_btn")));
        uploadProfileBtn.click();
        WebElement chooseBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[type=file]")));
        chooseBtn.sendKeys("C:\\Users\\Admin\\Downloads\\projets\\Social-Networking-App-Using-Spring-Boot-And-Angular-main\\backend\\uploads\\user\\940312f6-f900-4ec6-a478-1efc4af33487.webp");

        WebElement savePhotoBtn = driver.findElement(By.id("savePhoto"));
        savePhotoBtn.click();
    }
}
