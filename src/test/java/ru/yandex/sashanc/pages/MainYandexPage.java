package ru.yandex.sashanc.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.sashanc.uitests.KeyPercentTest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class MainYandexPage {
    private static final Logger log = LoggerFactory.getLogger(KeyPercentTest.class);
    private static final String YANDEX_URL = System.getProperty("base.url");
    private final SelenideElement searchField = $x("//input[@id=\"text\"]");
    private final SelenideElement findButton = $x("//button[contains(.,'Найти')]");
    private final SelenideElement targetLink = $x("//ul[@id='search-result']//a[contains(@class, 'OrganicTitle-Link')]");
    private final ElementsCollection links = $$x("//ul[@id='search-result']//a[contains(@class, 'OrganicTitle-Link')]");

    public void open() {
        Selenide.open(YANDEX_URL);
    }

    public void search(String text){
        searchField.shouldBe(Condition.visible).setValue(text);
        findButton.shouldBe(Condition.visible).click();
    }

    public void clickOnTargetLink(){
        targetLink.shouldBe(Condition.visible).click();
    }

    public void saveTopicToFile(Path file){
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            lines.add(links.get(i).shouldBe(Condition.visible).text());
            lines.add(links.get(i).shouldBe(Condition.visible).getAttribute("href"));
        }
        try {
            if (Files.deleteIfExists(file)) {
                System.out.println("Old file was deleted!");
            }
            Files.createFile(file);
            if (Files.isWritable(file)) {
                Files.write(file, lines, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            log.error("Ошибка создания или записи в файл", e);
        }
    }   
}
