package ru.yandex.sashanc.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import static com.codeborne.selenide.Selenide.$x;

public class CbrPage {
    private final SelenideElement keyPercent = $x("//div[contains(@class, 'main-indicator_content') " +
            "and contains(., 'Ключевая ставка')]/../div[2]");

    public String getKeyPercentAsString(){
        return keyPercent.shouldBe(Condition.visible).getText();
    }

    public double getKeyPercent(){
        double keyPercentIndicator = 0.0;
        try {
            keyPercentIndicator = NumberFormat.getInstance(Locale.getDefault()).parse(getKeyPercentAsString()).doubleValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return keyPercentIndicator;
    }
}
