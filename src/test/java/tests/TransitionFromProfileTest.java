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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransitionFromProfileTest {

    private static WebDriver driver;
    private static ApiClient apiClient;
    private static User user;
    private static String email;
    private static String name;
    private static String password;
    private static String accessToken;

    private MainPage mainPage;
    private LoginPage loginPage;
    private ProfilePage profilePage;


    @BeforeEach
    public void setUp() {
        driver = createWebDriver();
        apiClient = new ApiClient();

        String timestamp = String.valueOf(System.currentTimeMillis());
        password = "12345678";
        email = "romatest-" + timestamp + "@yandex.ru";
        User user = new User(email, password, "Roms");

        Response createResponse = apiClient.createUser(user);
        assertEquals(200, createResponse.statusCode());
        accessToken = createResponse.body().jsonPath().getString("accessToken");

        try {
            driver.manage().deleteAllCookies();
        } catch (Exception e) {
        }

        mainPage = new MainPage(driver);
        loginPage = new LoginPage(driver);
        profilePage = new ProfilePage(driver);
    }


    @Test
    @DisplayName("Переход из личного кабинета в конструктор по кнопке")
    @Description("Проверка перехода из личного кабинета в конструктор по клику на кнопку 'Конструктор'")
    public void testNavigateFromProfileToConstructorButton() {
        mainPage.open();
        mainPage.waitForLoad();
        mainPage.clickLoginButton();
        loginPage.waitForLoad();
        loginPage.login(email, password);
        mainPage.waitForLoadAuth();

        mainPage.clickPersonalAccountButton();

        profilePage.clickConstructorButton();

        assertTrue(mainPage.isUserLoggedIn(), "Должна отображаться кнопка 'Оформить заказ'");
    }

    @Test
    @DisplayName("Переход из личного кабинета в конструктор по логотипу")
    @Description("Проверка перехода из личного кабинета в конструктор по клику на логотип")
    public void testNavigateFromProfileToConstructorLogo() {
        mainPage.open();
        mainPage.waitForLoad();
        mainPage.clickLoginButton();
        loginPage.waitForLoad();
        loginPage.login(email, password);
        mainPage.waitForLoadAuth();

        mainPage.clickPersonalAccountButton();

        profilePage.clickLogo();

        assertTrue(mainPage.isUserLoggedIn(), "Должна отображаться кнопка 'Оформить заказ'");
    }


    @AfterEach
    public void tearDown() {
        driver.quit();

        if (accessToken != null && !accessToken.isEmpty()) {
            apiClient.deleteUser(accessToken);
        }
    }

}
