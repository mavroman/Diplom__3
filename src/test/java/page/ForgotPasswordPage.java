package page;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ForgotPasswordPage {
    private final WebDriver driver;

    // Локаторы
    private final By loginLink = By.xpath(".//a[text()='Войти']");
    private final By emailField = By.xpath(".//input[@name='name']");

    public ForgotPasswordPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Кликнуть на ссылку 'Войти'")
    public void clickLoginLink() {
        driver.findElement(loginLink).click();
    }

    @Step("Дождаться загрузки страницы восстановления пароля")
    public boolean waitForLoad() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(emailField));
        return driver.findElement(emailField).isDisplayed();
    }
}