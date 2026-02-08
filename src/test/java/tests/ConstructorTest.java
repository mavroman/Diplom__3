package tests;

import io.qameta.allure.Description;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import page.MainPage;

import java.time.Duration;

import static driver.WebDriverCreator.createWebDriver;
import static org.junit.jupiter.api.Assertions.*;

public class ConstructorTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private MainPage mainPage;

    @BeforeEach
    public void setUp() {
        driver = createWebDriver();
        driver.manage().deleteAllCookies();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        mainPage = new MainPage(driver);
    }

    @Test
    @DisplayName("Переход к разделу 'Булки'")
    @Description("Проверка перехода к разделу с булками в конструкторе")
    public void testNavigateToBunsSection() {
        mainPage.open();
        mainPage.waitForLoad();

        // Проверяем начальное состояние (по умолчанию должны быть Булки)
        String initialSection = mainPage.getSelectedSectionText();
        assertEquals("Булки", initialSection, "Изначально должен быть выбран раздел 'Булки'");

        // Переходим на соусы
        mainPage.clickSaucesSection();

        // Ждем переключения на соусы
        wait.until(driver -> {
            String currentSection = mainPage.getSelectedSectionText();
            return "Соусы".equals(currentSection);
        });

        // Переходим обратно на булки
        mainPage.clickBunsSection();

        // Ожидание переключения на булки
        wait.until(driver -> {
            String currentSection = mainPage.getSelectedSectionText();
            return "Булки".equals(currentSection);
        });

        String selectedSection = mainPage.getSelectedSectionText();
        assertNotNull(selectedSection, "Текст выбранного раздела не должен быть null");
        assertEquals("Булки", selectedSection, "Должен быть выбран раздел 'Булки'");
    }

    @Test
    @DisplayName("Переход к разделу 'Соусы'")
    @Description("Проверка перехода к разделу с соусами в конструкторе")
    public void testNavigateToSaucesSection() {
        mainPage.open();
        mainPage.waitForLoad();

        mainPage.clickSaucesSection();

        wait.until(driver -> {
            String currentSection = mainPage.getSelectedSectionText();
            return "Соусы".equals(currentSection);
        });

        String selectedSection = mainPage.getSelectedSectionText();
        assertNotNull(selectedSection, "Текст выбранного раздела не должен быть null");
        assertEquals("Соусы", selectedSection, "Должен быть выбран раздел 'Соусы'");
    }

    @Test
    @DisplayName("Переход к разделу 'Начинки'")
    @Description("Проверка перехода к разделу с начинками в конструкторе")
    public void testNavigateToFillingsSection() {
        mainPage.open();
        mainPage.waitForLoad();

        mainPage.clickFillingsSection();

        wait.until(driver -> {
            String currentSection = mainPage.getSelectedSectionText();
            return "Начинки".equals(currentSection);
        });

        String selectedSection = mainPage.getSelectedSectionText();
        assertNotNull(selectedSection, "Текст выбранного раздела не должен быть null");
        assertEquals("Начинки", selectedSection, "Должен быть выбран раздел 'Начинки'");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                System.err.println("Ошибка при закрытии драйвера: " + e.getMessage());
            }
        }
    }
}