package driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class WebDriverCreator {

    public static WebDriver createWebDriver() {
        String browser = System.getProperty("browser");
        if (browser == null) {
            return createChromeDriver();
        }

        switch (browser.toLowerCase()) {
            case "yandex":
                return createYandexDriver();
            case "chrome":
            default:
                return createChromeDriver();
        }
    }

    private static WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        return driver;
    }

    private static WebDriver createYandexDriver() {
        // Указываем путь к chromedriver для Яндекс
        System.setProperty("webdriver.chrome.driver",
                "C:\\Users\\Roman\\.cache\\selenium\\chromedriver\\win64\\142.0.7444.175\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();

        // Путь к Яндекс браузеру
        String yandexPath = "C:\\Users\\Roman\\AppData\\Local\\Yandex\\YandexBrowser\\Application\\browser.exe";
        options.setBinary(yandexPath);

        // Важные опции
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");

        // Критически важные опции для Яндекс
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");

        // Отключаем автоматизацию
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("excludeSwitches",
                Arrays.asList("enable-automation", "enable-logging"));

        // Настройки профиля
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.default_content_setting_values.notifications", 2);
        options.setExperimentalOption("prefs", prefs);

        try {
            WebDriver driver = new ChromeDriver(options);
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            return driver;
        } catch (Exception e) {
            System.out.println("Ошибка создания Яндекс драйвера: " + e.getMessage());
            return createChromeDriver(); // fallback на Chrome
        }
    }
}