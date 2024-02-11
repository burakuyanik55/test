package com.testinium.test.automation.tests;


import com.testinium.test.automation.Bean;
import com.testinium.test.automation.Utils;
import com.testinium.test.automation.abstracts.TestAbstracts;
import jxl.write.WriteException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class LoginPageTest extends TestAbstracts implements Runnable {

    public LoginPageTest() {
        super(LoginPageTest.class);
    }

    public boolean start() throws InterruptedException, WriteException, IOException {
        this.logger.info("Test Başlatıldı");
        gotoPage();
        setUsername("deneme");
        setpassword("pass");
        return true;
    }

    public void gotoPage() {
        Bean.webDriver.get(Bean.BASE_URI + "/login");
        this.logger.info("Login Sayfasına gidildi");
    }

    public boolean setUsername(String userName) {
        WebElement elem = Bean.webDriver.findElement(By.cssSelector("input[formcontrolname=\"userId\"]"));
        elem.sendKeys(userName);
        logger.info("username yazdırıldı");
        return true;
    }

    public boolean setpassword(String pass) {
        WebElement elem = Bean.webDriver.findElement(By.cssSelector("input[formcontrolname=\"password\"]"));
        elem.sendKeys(pass);
        logger.info("password yazdırıldı");
        return true;
    }

    public static void main(String[] args) {
        LoginPageTest mainPageTest = new LoginPageTest();
        mainPageTest.run();
    }

    @Override
    public void run() {
        try {
            start();
            Bean.webDriver.quit();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (WriteException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
