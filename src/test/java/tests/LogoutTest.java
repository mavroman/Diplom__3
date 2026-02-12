package tests;

import client.ApiClient;
import driver.WebDriverCreator;
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

public class LogoutTest {

    private WebDriver driver;
    private ApiClient apiClient;
    private User user;
    private String accessToken;
    private String email;
    private String password;

    private MainPage mainPage;
    private LoginPage loginPage;
    private ProfilePage profilePage;

    @BeforeEach
    public void setUp() {
        driver = createWebDriver();
        driver.manage().deleteAllCookies();

        apiClient = new ApiClient();

        String timestamp = String.valueOf(System.currentTimeMillis());
        password = "12345678";
        email = "romatest-" + timestamp + "-" + Thread.currentThread().getId() + "@yandex.ru";
        user = new User(email, password, "Roms");

        // Создание пользователя через API
        Response createResponse = apiClient.createUser(user);
        assertEquals(200, createResponse.statusCode(),
                "Не удалось создать тестового пользователя. Ответ: " + createResponse.getBody().asString());

        accessToken = createResponse.body().jsonPath().getString("accessToken");
        assertNotNull(accessToken, "Access token не должен быть null");

        mainPage = new MainPage(driver);
        loginPage = new LoginPage(driver);
        profilePage = new ProfilePage(driver);
    }

    @Test
    @DisplayName("Выход из аккаунта")
    @Description("Проверка выхода пользователя из аккаунта")
    public void testLogout() {
        mainPage.open();
        mainPage.waitForLoad();

        mainPage.clickLoginButton();
        assertTrue(loginPage.waitForLoad(), "Страница логина должна загрузиться");
        loginPage.login(email, password);

        mainPage.waitForLoadAuth();
        assertTrue(mainPage.isAuthorized(), "Пользователь должен быть авторизован");

        mainPage.clickPersonalAccountButton();
        profilePage.waitForLoad(); // Важно: дождаться загрузки страницы профиля

        profilePage.clickLogoutButton();

        loginPage.waitForLoad();

        assertTrue(loginPage.waitForLoad(), "Страница логина должна загрузиться после выхода");

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