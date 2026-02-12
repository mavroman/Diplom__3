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

public class ProfileTest {
    private WebDriver driver;
    private static User user;
    private MainPage mainPage;
    private LoginPage loginPage;
    private ProfilePage profilePage;
    private static ApiClient apiClient;
    private static String email;
    private static String password;
    private static String accessToken;

    @BeforeAll
    public static void setUpAll() {
        apiClient = new ApiClient();
        String timestamp = String.valueOf(System.currentTimeMillis());
        password = "12345678";
        email = "romatest-" + timestamp + "@yandex.ru";
        user = new User(email, password, "Roms");

        Response createResponse = apiClient.createUser(user);
        assertTrue(createResponse.statusCode() == 200 || createResponse.statusCode() == 201,
                "Не удалось создать тестового пользователя. Код ответа: " + createResponse.statusCode());

        accessToken = createResponse.body().jsonPath().getString("accessToken");
        assertNotNull(accessToken, "Access token не должен быть null");
    }

    @BeforeEach
    public void setUp() {
        driver = createWebDriver();
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize(); // Рекомендуется для стабильности

        mainPage = new MainPage(driver);
        loginPage = new LoginPage(driver);
        profilePage = new ProfilePage(driver);
    }

    @Test
    @DisplayName("Переход в личный кабинет")
    @Description("Проверка перехода в личный кабинет после авторизации")
    public void testNavigateToPersonalAccount() {
        mainPage.open();
        mainPage.waitForLoad();
        mainPage.clickLoginButton();

        loginPage.waitForLoad();
        loginPage.login(email, password);

        mainPage.waitForLoadAuth();
        mainPage.clickPersonalAccountButton();

        assertTrue(profilePage.isProfilePageLoaded(), "Страница профиля должна быть загружена");

    }

    @Test
    @DisplayName("Переход в личный кабинет без авторизации")
    @Description("Проверка, что при попытке перейти в личный кабинет без авторизации пользователь перенаправляется на страницу логина")
    public void testNavigateToPersonalAccountWithoutAuth() {
        mainPage.open();
        mainPage.waitForLoad();
        mainPage.clickPersonalAccountButton();

        assertTrue(loginPage.isLoginPageLoaded(), "Должна открыться страница логина");
    }

    @AfterAll
    public static void cleanUp() {
        if (accessToken != null && !accessToken.isEmpty()) {
            apiClient.deleteUser(accessToken);
        }
    }
    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }


}