package page;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RegistrationPage {
    private final WebDriver driver;

    // Локаторы
    private final By nameField = By.xpath(".//fieldset[1]//input[@name='name']");
    private final By emailField = By.xpath(".//fieldset[2]//input[@name='name']");
    private final By passwordField = By.xpath(".//input[@name='Пароль']");
    private final By registerButton = By.xpath(".//button[text()='Зарегистрироваться']");
    private final By loginLink = By.xpath(".//a[text()='Войти']");
    private final By passwordError = By.xpath(".//p[contains(@class, 'input__error') and contains(text(), 'Некорректный пароль')]");

    public RegistrationPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Ввести имя: {name}")
    public void enterName(String name) {
        driver.findElement(nameField).sendKeys(name);
    }

    @Step("Ввести email: {email}")
    public void enterEmail(String email) {
        driver.findElement(emailField).sendKeys(email);
    }

    @Step("Ввести пароль: {password}")
    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    @Step("Кликнуть на кнопку 'Зарегистрироваться'")
    public void clickRegisterButton() {
        driver.findElement(registerButton).click();
    }

    @Step("Кликнуть на ссылку 'Войти'")
    public void clickLoginLink() {
        driver.findElement(loginLink).click();
    }

    @Step("Выполнить регистрацию с именем: {name}, email: {email}, паролем: {password}")
    public void register(String name, String email, String password) {
        enterName(name);
        enterEmail(email);
        enterPassword(password);
        clickRegisterButton();
    }

    @Step("Проверить наличие ошибки пароля")
    public boolean isPasswordErrorDisplayed() {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(passwordError));
        return driver.findElement(passwordError).isDisplayed();
    }

    @Step("Дождаться загрузки страницы регистрации")
    public boolean waitForLoad() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(nameField));
        return true;
    }

}
