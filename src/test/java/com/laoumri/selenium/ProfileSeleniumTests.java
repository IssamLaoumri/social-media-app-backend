package com.laoumri.selenium;

import com.laoumri.shared.DoLogin;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ProfileSeleniumTests extends BaseSeleniumTest{
    //@Test
    public void testUploadProfilePhoto(){
        DoLogin.login(driver, wait);
        driver.get("http://localhost:4200/profile");
        driver.manage().addCookie(new Cookie("JWT-SESSION", "eyJhbGciOiJIUzI1NiJ9.eyJsYXN0UmVmcmVzaFRpbWUiOjE3MzM1OTQyNTExMjMsInhzcmZUb2tlbiI6InZ1YTV2bm9naHY5MnY5ZHI4MGhxamNvdHEwIiwianRpIjoiQVpPaUpoVkt5cEJTeUhCT3Z1U2IiLCJzdWIiOiJBWkthOURkVzJSREczYWhJZktlQiIsImlhdCI6MTczMzU5MjIyNSwiZXhwIjoxNzMzODUzNDUxfQ.Z1JiXSETubIeXVd--bvG1pY5uko0MvTbswdhdX-R0P8"));
        WebElement uploadProfileBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("upload_profile_btn")));
        uploadProfileBtn.click();
        WebElement chooseBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[type=file]")));
        chooseBtn.sendKeys("C:\\Users\\Admin\\Downloads\\projets\\Social-Networking-App-Using-Spring-Boot-And-Angular-main\\backend\\uploads\\user\\940312f6-f900-4ec6-a478-1efc4af33487.webp");

        WebElement savePhotoBtn = driver.findElement(By.id("savePhoto"));
        savePhotoBtn.click();
    }
}
