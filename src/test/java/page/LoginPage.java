package page;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private final WebDriver driver;

    // Локаторы
    private final By emailField = By.xpath(".//input[@name='name']");
    private final By passwordField = By.xpath(".//input[@name='Пароль']");
    private final By loginButton = By.xpath(".//button[text()='Войти']");
    private final By registerLink = By.xpath(".//a[text()='Зарегистрироваться']");
    private final By errorMessage = By.xpath(".//p[contains(@class, 'input__error')]");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Ввести email: {email}")
    public void enterEmail(String email) {
        driver.findElement(emailField).sendKeys(email);
    }

    @Step("Ввести пароль: {password}")
    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    @Step("Кликнуть на кнопку 'Войти'")
    public void clickLoginButton() {
        driver.findElement(loginButton).click();
    }

    @Step("Кликнуть на ссылку 'Зарегистрироваться'")
    public void clickRegisterLink() {
        driver.findElement(registerLink).click();
    }

    @Step("Выполнить вход с email: {email} и паролем: {password}")
    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
    }

    @Step("Дождаться загрузки страницы входа")
    public boolean waitForLoad() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(emailField));
        return true;
    }

    @Step("Проверить, что страница входа загружена")
    public boolean isLoginPageLoaded() {
        try {
            return driver.findElement(loginButton).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }


}
