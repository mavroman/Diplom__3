package page;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProfilePage {

    private final WebDriver driver;

    // Локаторы
    private final By profileLink = By.xpath(".//a[text()='Профиль']");
    private final By logoutButton = By.xpath(".//button[text()='Выход']");
    private final By constructorButton = By.xpath(".//p[text()='Конструктор']");
    private final By logo = By.className("AppHeader_header__logo__2D0X2");

    public ProfilePage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Кликнуть на кнопку 'Выход'")
    public void clickLogoutButton() {
        driver.findElement(logoutButton).click();
    }

    @Step("Кликнуть на 'Конструктор' в хедере")
    public void clickConstructorButton() {
        driver.findElement(constructorButton).click();
    }

    @Step("Кликнуть на логотип")
    public void clickLogo() {
        driver.findElement(logo).click();
    }

    @Step("Дождаться загрузки страницы профиля")
    public void waitForLoad() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(profileLink));
    }

    @Step("Проверить, что страница профиля загружена")
    public boolean isProfilePageLoaded() {
        try {
            return driver.findElement(logoutButton).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

}
