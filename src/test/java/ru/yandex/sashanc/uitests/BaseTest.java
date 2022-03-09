package ru.yandex.sashanc.uitests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;

abstract public class BaseTest {

    public void setUp() throws IOException {
        System.getProperties().load(ClassLoader.getSystemResourceAsStream("test.properties"));
        WebDriverManager.firefoxdriver().setup();
        Configuration.browser = "firefox";
        Configuration.driverManagerEnabled = true;
        Configuration.browserSize = "1536x920";
        Configuration.reportsFolder = "test-result/reports";
    }

    @Before
    public void init() throws IOException {
        setUp();
    }

    @After
    public void tearDown(){
        Selenide.closeWebDriver();
    }
}
