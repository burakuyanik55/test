package com.testinium.test.automation;

import com.testinium.test.automation.tests.MainPageTest;

public class FullTestStart {



    public static void main(String[] args) throws InterruptedException {
        MainPageTest authTest=new MainPageTest();
        Bean.webDriver.manage().window().maximize();
        // ilkTest
        Thread threadAuthTest =new Thread(authTest);
        threadAuthTest.start();

    }
}
