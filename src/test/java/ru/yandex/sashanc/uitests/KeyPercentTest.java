package ru.yandex.sashanc.uitests;

import com.codeborne.selenide.*;
import org.junit.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.sashanc.pages.CbrPage;
import ru.yandex.sashanc.pages.MainYandexPage;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.*;
import static com.codeborne.selenide.Selenide.screenshot;

public class KeyPercentTest extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(KeyPercentTest.class);
    private MainYandexPage mainYandexPage;
    private CbrPage cbrPage;

// 1. Открыть браузер и развернуть на весь экран
// Реализовано в BaseTest.class

    @Test
    public void openYandex() {
        mainYandexPage = new MainYandexPage();
        cbrPage = new CbrPage();

// 2. Зайти на yandex.ru
        mainYandexPage.open();

// 3. В поисковую строку ввести «цб рф».
        mainYandexPage.search("цб рф");

// 4. Нажать на 1 результат поиска (ожидаемо что это сайт Банка России).
        mainYandexPage.clickOnTargetLink();
        //Время для проверки Капчи
        Selenide.sleep(3000);

// 5. Проверить нахождение на сайте Центрального Банка
        switchTo().window(1);
        Assert.assertTrue("Wrong URL", url().contains("https://cbr.ru"));

// 6. Закрыть вкладку с яндексом
        switchTo().window(0);
        Selenide.closeWindow();
        switchTo().window(0);
        Assert.assertTrue("Wrong URL", url().contains("https://cbr.ru"));

// 7. Найти на сайте Центрального Банка и сохранить ключевую ставку
        double keyPercent =  cbrPage.getKeyPercent();
        String keyPercentStr = cbrPage.getKeyPercentAsString();
        log.info("Ключевая ставка " + keyPercent);

// 8. Открыть новую вкладку, зайти на yandex.ru
        mainYandexPage.open();
        Assert.assertTrue("Wrong URL", url().contains("https://yandex.ru"));

// 9. В поисковую строку ввести сохраненную ключевую ставку
        mainYandexPage.search(keyPercentStr);

// 10. Сохранить заголовки и ссылки первых трёх результатов в текстовый документ (документ назвать «новости.txt»)
        Path resultFile = Paths.get(System.getProperty("reports.folder"), "новости.txt");
        mainYandexPage.saveTopicToFile(resultFile);

// 11. Сделать скриншот страницы, скриншот поместить в ту же папку, что и текстовый документ
        log.info("Сделан скриншот " + screenshot("screen"));

// 12.	Закрыть браузер
        Selenide.sleep(3000);
    }
}
