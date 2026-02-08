package page;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MainPage {
    private final WebDriver driver;
    private WebDriverWait wait;

    public static final String BASE_URL = "https://stellarburgers.education-services.ru/";
    public static final String REGISTER_URL = BASE_URL + "register";
    public static final String FORGOT_PASSWORD_URL = BASE_URL + "forgot-password";

    // Локаторы
    private final By loginButton = By.xpath(".//button[text()='Войти в аккаунт']");
    private final By personalAccountButton = By.xpath(".//p[text()='Личный Кабинет']");
    private final By constructorButton = By.xpath(".//p[text()='Конструктор']");
    private final By logo = By.className("AppHeader_header__logo__2D0X2");
    private final By bunsSection = By.xpath(".//span[text()='Булки']/parent::div");
    private final By saucesSection = By.xpath(".//span[text()='Соусы']/parent::div");
    private final By fillingsSection = By.xpath(".//span[text()='Начинки']/parent::div");
    private final By selectedSection = By.xpath(".//div[contains(@class, 'current')]/span");
    private final By orderButton = By.xpath(".//button[text()='Оформить заказ']");

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5)); // Инициализируйте здесь
    }

    @Step("Открыть главную страницу")
    public void open() {
        driver.get(BASE_URL);
    }

    @Step("Открыть страницу регистрации")
    public void openRegisterPage() {
        driver.get(REGISTER_URL);
    }

    @Step("Открыть страницу восстановления пароля")
    public void openForgotPasswordPage() {
        driver.get(FORGOT_PASSWORD_URL);
    }

    @Step("Кликнуть на кнопку 'Войти в аккаунт'")
    public void clickLoginButton() {
        WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        loginBtn.click();
    }

    @Step("Кликнуть на 'Личный кабинет'")
    public void clickPersonalAccountButton() {
        driver.findElement(personalAccountButton).click();
    }

    @Step("Перейти в раздел 'Булки'")
    public void clickBunsSection() {
        driver.findElement(bunsSection).click();
    }

    @Step("Перейти в раздел 'Соусы'")
    public void clickSaucesSection() {
        driver.findElement(saucesSection).click();
    }

    @Step("Перейти в раздел 'Начинки'")
    public void clickFillingsSection() {
        driver.findElement(fillingsSection).click();
    }

    @Step("Получить текст выбранного раздела")
    public String getSelectedSectionText() {
        return driver.findElement(selectedSection).getText();
    }

    @Step("Проверить, что пользователь авторизован")
    public boolean isUserLoggedIn() {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(orderButton));
        return driver.findElement(orderButton).isDisplayed();
    }

    @Step("Проверить, что пользователь авторизован")
    public boolean isAuthorized() {
        try {
            return driver.findElement(orderButton).isDisplayed() &&
                    driver.findElement(personalAccountButton).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Дождаться загрузки главной страницы без авторизации")
    public void waitForLoad() {
        // Ожидаем загрузки DOM и наличие body
        wait.until(driver -> driver.findElement(By.tagName("body")).isDisplayed());

        // Ожидаем появления одной из ключевых кнопок (для авторизованного или неавторизованного пользователя)
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(loginButton),
                ExpectedConditions.visibilityOfElementLocated(orderButton)
        ));

        // Ожидаем кликабельность элемента
        wait.until(ExpectedConditions.or(
                ExpectedConditions.elementToBeClickable(loginButton),
                ExpectedConditions.elementToBeClickable(orderButton)
        ));
    }

    @Step("Дождаться загрузки главной страницы с авторизацией")
    public void waitForLoadAuth() {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(orderButton));
    }

}
