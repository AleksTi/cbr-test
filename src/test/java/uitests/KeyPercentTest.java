package uitests;

import com.codeborne.selenide.*;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;


import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.*;
import static com.codeborne.selenide.Selenide.screenshot;

public class KeyPercentTest extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(KeyPercentTest.class);

// 1. Открыть браузер и развернуть на весь экран

    @Test
    public void openYandex() {
// 2. Зайти на yandex.ru
        open("https://yandex.ru");
        SelenideElement searchInput = $x("//input[@id=\"text\"]");
        searchInput.shouldBe(Condition.visible);

// 3. В поисковую строку ввести «цб рф».
        searchInput.setValue("цб рф");
        SelenideElement findButton = $x("//button[contains(.,'Найти')]");
        findButton.shouldBe(Condition.visible).click();

// 4. Нажать на 1 результат поиска (ожидаемо что это сайт Банка России).
        SelenideElement targetLink = $x("//ul[@id='search-result']//a[contains(@class, 'OrganicTitle-Link')]");
        targetLink.shouldBe(Condition.visible).click();
        //Время для проверки Капчи
        Selenide.sleep(30000);

// 5. Проверить нахождение на сайте Центрального Банка
        switchTo().window(1);
        Assert.assertTrue("Wrong URL", url().contains("https://cbr.ru"));

// 6. Закрыть вкладку с яндексом
        switchTo().window(0);
        Selenide.closeWindow();
        switchTo().window(0);
        Assert.assertTrue("Wrong URL", url().contains("https://cbr.ru"));
        System.out.println("==> 6. Закрыть вкладку с яндексом ==> текущий URL: " + url());

//        WebDriver driver = WebDriverRunner.getWebDriver();
//        String parentWindow = driver.getWindowHandle();
//        for (String windowHandle :  driver.getWindowHandles()) {
//            if (windowHandle.equals(parentWindow)) {
//                driver.close();
//                continue;
//            }
//            driver.switchTo().window(windowHandle).getWindowHandle();
//        }

// 7. Найти на сайте Центрального Банка и сохранить ключевую ставку
        SelenideElement keyPercent = $x("//div[contains(@class, 'main-indicator_content') and contains(., 'Ключевая ставка')]/../div[2]");
        String keyPercentStr =  keyPercent.shouldBe(Condition.visible).getText();
        System.out.println("==> 7. Найти на сайте Центрального Банка и сохранить ключевую ставку ==> getText: " + keyPercentStr);
        Double keyPercentIndicator = null;
        try {
            keyPercentIndicator = NumberFormat.getInstance(Locale.getDefault()).parse(keyPercentStr).doubleValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("==> Ключевая ставка: " + keyPercentIndicator);
        System.out.println("==> текущий URL: " + WebDriverRunner.url());

// 8. Открыть новую вкладку, зайти на yandex.ru
        open("https://yandex.ru");
        Assert.assertTrue("Wrong URL", url().contains("https://yandex.ru"));

// 9. В поисковую строку ввести сохраненную ключевую ставку
        searchInput.shouldBe(Condition.visible).setValue(keyPercentStr);
        findButton.shouldBe(Condition.visible).click();

// 10. Сохранить заголовки и ссылки первых трёх результатов в текстовый документ (документ назвать «новости.txt»)
        ElementsCollection links = $$x("//ul[@id='search-result']//a[contains(@class, 'OrganicTitle-Link')]");
        System.out.println(links);
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            lines.add(links.get(i).shouldBe(Condition.visible).text());
            lines.add(links.get(i).shouldBe(Condition.visible).getAttribute("href"));
        }
        Path resultFile = Paths.get(System.getProperty("reports.folder"), "новости.txt");
        try {
            if (Files.deleteIfExists(resultFile)) {
                System.out.println("Old file was deleted!");
            }
            Files.createFile(resultFile);
            if (Files.isWritable(resultFile)) {
                Files.write(resultFile, lines, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            log.error("Ошибка создания или записи в файл", e);
        }

// 11. Сделать скриншот страницы, скриншот поместить в ту же папку, что и текстовый документ
        log.info("==> Сделан скриншот " + screenshot("screen"));

// 12.	Закрыть браузер
        Selenide.sleep(3000);
        Assert.assertTrue(true);
    }
}
