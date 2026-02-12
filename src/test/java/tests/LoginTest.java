package tests;

import client.ApiClient;
import page.MainPage;
import io.qameta.allure.Description;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import page.*;

import static driver.WebDriverCreator.createWebDriver;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    private WebDriver driver;
    private MainPage mainPage;
    private LoginPage loginPage;
    private RegistrationPage registerPage;
    private ForgotPasswordPage forgotPasswordPage;
    private ProfilePage profilePage;
    private ApiClient apiClient;
    private User testUser;
    private String accessToken;

    @BeforeEach
    public void setUp() {
        driver = createWebDriver();

        mainPage = new MainPage(driver);
        loginPage = new LoginPage(driver);
        registerPage = new RegistrationPage(driver);
        forgotPasswordPage = new ForgotPasswordPage(driver);
        profilePage = new ProfilePage(driver);

        apiClient = new ApiClient();

        testUser = User.generateRandomUser();
        var response = apiClient.createUser(testUser);

        if (response.statusCode() == 200) {
            accessToken = response.jsonPath().getString("accessToken");
        } else {
            var loginResponse = apiClient.loginUser(testUser);
            if (loginResponse.statusCode() == 200) {
                accessToken = loginResponse.jsonPath().getString("accessToken");
            } else {
                throw new RuntimeException("Не удалось создать или залогинить пользователя");
            }
        }
    }

    @AfterEach
    public void tearDown() {
        try {
            if (accessToken != null && !accessToken.isEmpty()) {
                apiClient.deleteUser(accessToken);
            }
        } catch (Exception e) {
            System.out.println("Ошибка при удалении пользователя: " + e.getMessage());
        }

        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Авторизация через кнопку 'Войти в аккаунт' на главной странице")
    @Description("Проверка авторизации пользователя при нажатии на кнопку 'Войти в аккаунт' на главной странице")
    public void testLoginMainPageLoginButton() {
        mainPage.open();
        mainPage.waitForLoad();

        // клик на кнопку "Войти в аккаунт"
        mainPage.clickLoginButton();

        // проверка, что открылась страница входа
        assertTrue(loginPage.waitForLoad(), "Страница входа не загрузилась");

        // вход
        loginPage.login(testUser.getEmail(), testUser.getPassword());
        mainPage.waitForLoad();

        assertTrue(mainPage.isAuthorized(),
                "Кнопка 'Оформить заказ' не отображается");
    }

    @Test
    @DisplayName("Авторизация через кнопку 'Личный кабинет'")
    @Description("Проверка авторизации пользователя при нажатии на кнопку 'Личный кабинет' и переход в личный кабинет после авторизации")
    public void testLoginPersonalAccountButton() {
        mainPage.open();
        mainPage.waitForLoad();

        // клик на кнопку "Личный кабинет" (без авторизации)
        mainPage.clickPersonalAccountButton();

        // проверка, что открылась страница входа
        assertTrue(loginPage.waitForLoad(), "Страница входа не загрузилась");

        // вход
        loginPage.login(testUser.getEmail(), testUser.getPassword());
        mainPage.waitForLoad();

        // клик на кнопку "Личный кабинет" (уже с авторизацией)
        mainPage.clickPersonalAccountButton();

        assertTrue(profilePage.isProfilePageLoaded(), "Страница профиля должна быть загружена");
    }

    @Test
    @DisplayName("Авторизация через форму регистрации")
    @Description("Проверка перехода на страницу авторизации из формы регистрации и успешный вход")
    public void testLoginRegisterPage() {
        mainPage.openRegisterPage();
        registerPage.waitForLoad();

        // клик на кнопку "Войти" на странице регистрации
        registerPage.clickLoginLink();

        // проверка, что открылась страница входа
        assertTrue(loginPage.waitForLoad(), "Страница входа не загрузилась");

        // вход
        loginPage.login(testUser.getEmail(), testUser.getPassword());

        mainPage.waitForLoad();
        assertTrue(mainPage.isAuthorized(),
                "Кнопка 'Оформить заказ' не отображается");
    }

    @Test
    @DisplayName("Авторизация через форму восстановления пароля")
    @Description("Проверка перехода на страницу авторизации из формы восстановления пароля и успешный вход")
    public void testLoginForgotPasswordPage() {
        mainPage.openForgotPasswordPage();
        forgotPasswordPage.waitForLoad();

        // клик на кнопку "Войти" на странице восстановления пароля
        forgotPasswordPage.clickLoginLink();

        // проверка, что открылась страница входа
        assertTrue(loginPage.waitForLoad(), "Страница входа не загрузилась");

        // вход
        loginPage.login(testUser.getEmail(), testUser.getPassword());


        mainPage.waitForLoad();
        assertTrue(mainPage.isAuthorized(),
                "Кнопка 'Оформить заказ' не отображается");
    }

}