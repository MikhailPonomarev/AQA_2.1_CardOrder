import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CardOrderTest {
    public WebDriver driver;

    @BeforeAll
    public static void setupWebDriverManager() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp () {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
        driver = null;
    }


    @Test
    public void HappyPathTestV1() {
        driver.get("http://localhost:9999");
        List<WebElement> inputField = driver.findElements(By.className("input__control"));
        inputField.get(0).sendKeys("Иванов Иван");
        inputField.get(1).sendKeys("+79001000110");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();
        String actual = driver.findElement(By.tagName("p")).getText();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        assertEquals(expected, actual.strip());
    }

    @Test
    public void HappyPathTestV2() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Ива-нов Иван");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79001000110");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("[role='button']")).click();
        String actual = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        assertEquals(expected, actual.strip());
    }

    //Тесты на проверку валидации полей
    @Test
    public void OrderWithEmptyFields() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[role='button']")).click();
        String actual = driver.findElement(By.cssSelector(".input__sub")).getText();
        String expected = "Поле обязательно для заполнения";
        assertEquals(expected, actual);
    }

    @Test
    public void OrderOnlyWithCheckbox() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("[role='button']")).click();
        String actual = driver.findElement(By.cssSelector(".input__sub")).getText();
        String expected = "Поле обязательно для заполнения";
        assertEquals(expected, actual);
    }

    @Test
    public void EnteringNameInEnglish() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Ivanov Ivan");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79001000110");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("[role='button']")).click();
        String actual = driver.findElement(By.cssSelector(".input__sub")).getText();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        assertEquals(expected, actual);
    }

    @Test
    public void EnteringNameWithSpecialChars() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("!@#$%^&*()");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79001000110");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("[role='button']")).click();
        String actual = driver.findElement(By.cssSelector(".input__sub")).getText();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        assertEquals(expected, actual);
    }

    @Test
    public void EnteringNameUsingNumbers() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("12345678");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79001000110");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("[role='button']")).click();
        String actual = driver.findElement(By.cssSelector(".input__sub")).getText();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        assertEquals(expected, actual);
    }

    @Test
    public void OrderWithEmptyTelephoneField() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("[role='button']")).click();
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'] [class='input__sub']")).getText();
        String expected = "Поле обязательно для заполнения";
        assertEquals(expected, actual);
    }

    @Test
    public void OrderWithWrongNumber() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("89001000110");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("[role='button']")).click();
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'] [class='input__sub']")).getText();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        assertEquals(expected, actual);
    }

    @Test
    public void OrderWithoutClickingCheckbox() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("[role='button']")).click();
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'] [class='input__sub']")).getText();
        String expected = "Поле обязательно для заполнения";
        assertEquals(expected, actual);
    }
}
