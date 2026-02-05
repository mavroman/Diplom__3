package tests;

import client.ApiClient;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import model.User;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import page.LoginPage;
import page.MainPage;
import page.ProfilePage;

import static driver.WebDriverCreator.createWebDriver;
import static org.junit.jupiter.api.Assertions.*;

public class ConstructorTest {

    private WebDriver driver;
    private ApiClient apiClient;
    private User user;
    private String accessToken;

    private MainPage mainPage;
    private LoginPage loginPage;
    private ProfilePage profilePage;

    @BeforeEach
    public void setUp() {
        // Инициализация API клиента для каждого теста
        apiClient = new ApiClient();

        // Создание уникального пользователя для каждого теста
        String timestamp = String.valueOf(System.currentTimeMillis());
        String password = "12345678";
        String email = "romatest-" + timestamp + "-" + Thread.currentThread().getId() + "@yandex.ru";
        user = new User(email, password, "Roms");

        // Создание пользователя через API
        Response createResponse = apiClient.createUser(user);
        assertEquals(200, createResponse.statusCode(),
                "Не удалось создать тестового пользователя. Ответ: " + createResponse.getBody().asString());

        accessToken = createResponse.body().jsonPath().getString("accessToken");
        assertNotNull(accessToken, "Access token не должен быть null");

        driver = createWebDriver();
        driver.manage().deleteAllCookies();

        // Инициализация страниц для каждого теста
        mainPage = new MainPage(driver);
        loginPage = new LoginPage(driver);
        profilePage = new ProfilePage(driver);
    }

    @Test
    @DisplayName("Переход к разделу 'Булки'")
    @Description("Проверка перехода к разделу с булками в конструкторе")
    public void testNavigateToBunsSection() {
        mainPage.open();
        mainPage.waitForLoad();

        mainPage.clickSaucesSection();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Клик на раздел "Булки"
        mainPage.clickBunsSection();

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

        // Кликнуть на раздел "Соусы"
        mainPage.clickSaucesSection();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String selectedSection = mainPage.getSelectedSectionText();
        assertNotNull(selectedSection, "Текст выбранного раздела не должен быть null");
        assertEquals("Начинки", selectedSection, "Должен быть выбран раздел 'Начинки'");
    }

    @AfterEach
    public void tearDown() {
        if (accessToken != null && !accessToken.isEmpty()) {
            try {
                Response deleteResponse = apiClient.deleteUser(accessToken);
                System.out.println("Пользователь удален. Статус: " + deleteResponse.getStatusCode());
            } catch (Exception e) {
                System.err.println("Ошибка при удалении пользователя: " + e.getMessage());
            }
        }

        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                System.err.println("Ошибка при закрытии драйвера: " + e.getMessage());
            }
        }
    }
}