package tests;

import client.ApiClient;
import io.qameta.allure.Description;
import model.User;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import page.LoginPage;
import page.MainPage;
import page.ProfilePage;
import page.RegistrationPage;

import static driver.WebDriverCreator.createWebDriver;
import static org.junit.jupiter.api.Assertions.*;

public class RegistrationTest {
    private ApiClient apiClient;
    private WebDriver driver;
    private MainPage mainPage;
    private LoginPage loginPage;
    private RegistrationPage registrationPage;
    private ProfilePage profilePage;
    private User createdUser; // добавили поле для хранения созданного пользователя

    @BeforeEach
    public void setUp() {
        apiClient = new ApiClient();
        driver = createWebDriver();

        mainPage = new MainPage(driver);
        loginPage = new LoginPage(driver);
        registrationPage = new RegistrationPage(driver);
        profilePage = new ProfilePage(driver);
    }

    @Test
    @DisplayName("Успешная регистрация пользователя")
    @Description("Проверка успешной регистрации нового пользователя с корректными данными")
    public void testSuccessfulRegistration() {
        User testUser = User.generateRandomUser();
        String uniqueEmail = "roma" + System.currentTimeMillis() + "_" + Thread.currentThread().getId() + "@yandex.ru";
        testUser.setEmail(uniqueEmail);
        testUser.setPassword("12345678"); // Пароль должен соответствовать требованиям

        createdUser = testUser;

        mainPage.open();
        mainPage.waitForLoad();

        mainPage.clickLoginButton();
        assertTrue(loginPage.waitForLoad(), "Страница входа должна загрузиться");
        loginPage.clickRegisterLink();
        assertTrue(registrationPage.waitForLoad(), "Страница регистрации должна загрузиться");

        registrationPage.register(
                testUser.getName(),
                testUser.getEmail(),
                testUser.getPassword()
        );

        assertTrue(loginPage.waitForLoad(),
                "После регистрации должна открыться страница входа");

    }

    @Test
    @DisplayName("Регистрация с некорректным паролем (менее 6 символов)")
    @Description("Проверка отображения ошибки при регистрации с паролем менее 6 символов")
    public void testRegistrationWithInvalidPassword() {
        String name = "Roms_" + System.currentTimeMillis();
        String email = "roms_" + System.currentTimeMillis() + "@yandex.ru";
        String invalidPassword = "123"; // Менее 6 символов

        mainPage.open();
        mainPage.waitForLoad();

        mainPage.clickLoginButton();
        assertTrue(loginPage.waitForLoad(), "Страница входа должна загрузиться");
        loginPage.clickRegisterLink();
        assertTrue(registrationPage.waitForLoad(), "Страница регистрации должна загрузиться");

        registrationPage.register(name, email, invalidPassword);

        assertTrue(registrationPage.isPasswordErrorDisplayed(),
                "Должна отображаться ошибка при вводе пароля менее 6 символов");
    }

    @Test
    @DisplayName("Переход со страницы регистрации на страницу входа")
    @Description("Проверка перехода на страницу входа по ссылке 'Войти' со страницы регистрации")
    public void testNavigateFromRegistrationToLogin() {
        mainPage.open();
        mainPage.waitForLoad();

        mainPage.clickLoginButton();
        assertTrue(loginPage.waitForLoad(), "Страница входа должна загрузиться");
        loginPage.clickRegisterLink();
        assertTrue(registrationPage.waitForLoad(), "Страница регистрации должна загрузиться");

        registrationPage.clickLoginLink();

        assertTrue(loginPage.waitForLoad(), "После клика на 'Войти' должна открыться страница входа");
        assertTrue(loginPage.isLoginPageLoaded(), "На странице должна быть кнопка 'Войти'");
    }

    @AfterEach
    public void tearDown() {
        if (createdUser != null && createdUser.getEmail() != null) {
            try {
                apiClient.deleteUser(createdUser.getEmail());
            } catch (Exception e) {
                System.err.println("Error deleting user: " + e.getMessage());
            }
        }

        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                System.err.println("Error closing driver: " + e.getMessage());
            }
        }
    }
}