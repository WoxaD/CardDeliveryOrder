import org.junit.jupiter.api.*;
import org.openqa.selenium.*;

import java.time.*;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

public class CardDeliveryOrderTest {

    String date(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldFillTheFormWithNormalData() {
        $("[data-test-id=city] input").setValue("Казань");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, date(3));
        $("[data-test-id=name] input").setValue("Петров Иван");
        $("[data-test-id=phone] input").setValue("+79600000000");
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id=notification]")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("успешно забронирована на " + date(3)));
    }

    @Test
    void shouldFillTheFormWithADoubleName() {
        $("[data-test-id=city] input").setValue("Казань");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, date(3));
        $("[data-test-id=name] input").setValue("Петрова Анна-Мария");
        $("[data-test-id=phone] input").setValue("+79600000000");
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id=notification]")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("успешно забронирована на " + date(3)));
    }

    @Test
    void shouldFillTheFormWithNotInTheListCity() {
        $("[data-test-id=city] input").setValue("Иннополис");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, date(3));
        $("[data-test-id=name] input").setValue("Петров Иван");
        $("[data-test-id=phone] input").setValue("+79600000000");
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id=city]")
                .shouldBe(visible)
                .shouldHave(text("город недоступна"));
    }

    @Test
    void shouldFillTheFormWithoutCity() {
        $("[data-test-id=city] input").setValue("");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, date(3));
        $("[data-test-id=name] input").setValue("Петров Иван");
        $("[data-test-id=phone] input").setValue("+79600000000");
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id=city]")
                .shouldBe(visible)
                .shouldHave(text("обязательно для"));
    }

    @Test
    void shouldFillTheFormWithWrongDate() {
        $("[data-test-id=city] input").setValue("Казань");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, date(2));
        $("[data-test-id=name] input").setValue("Петров Иван");
        $("[data-test-id=phone] input").setValue("+79600000000");
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id=date]")
                .shouldBe(visible)
                .shouldHave(text("дату невозможен"));
    }

    @Test
    void shouldFillTheFormWithoutDate() {
        $("[data-test-id=city] input").setValue("Казань");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=name] input").setValue("Петров Иван");
        $("[data-test-id=phone] input").setValue("+79600000000");
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id=date]")
                .shouldBe(visible)
                .shouldHave(exactText("Неверно введена дата"));
    }

    @Test
    void shouldFillTheFormWithLatinLettersInName() {
        $("[data-test-id=city] input").setValue("Казань");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, date(3));
        $("[data-test-id=name] input").setValue("Petrov Ivan");
        $("[data-test-id=phone] input").setValue("+79600000000");
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id=name]")
                .shouldBe(visible)
                .shouldHave(text("Допустимы только русские буквы"));
    }

    @Test
    void shouldFillTheFormWithInvalidSymbolsInName() {
        $("[data-test-id=city] input").setValue("Казань");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, date(3));
        $("[data-test-id=name] input").setValue("Петров1 Иван!");
        $("[data-test-id=phone] input").setValue("+79600000000");
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id=name]")
                .shouldBe(visible)
                .shouldHave(text("Допустимы только русские буквы"));
    }

    @Test
    void shouldFillTheFormWithoutAName() {
        $("[data-test-id=city] input").setValue("Казань");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, date(3));
        $("[data-test-id=name] input").setValue("");
        $("[data-test-id=phone] input").setValue("+79600000000");
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id=name]")
                .shouldBe(visible)
                .shouldHave(text("Поле обязательно для"));
    }

    @Test
    void shouldFillTheFormWithNInvalidPhone() {
        $("[data-test-id=city] input").setValue("Казань");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, date(3));
        $("[data-test-id=name] input").setValue("Петров Иван");
        $("[data-test-id=phone] input").setValue("+79600000000000000");
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id=phone]")
                .shouldBe(visible)
                .shouldHave(text("Телефон указан неверно"));
    }

    @Test
    void shouldFillTheFormWithoutAPhone() {
        $("[data-test-id=city] input").setValue("Казань");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, date(3));
        $("[data-test-id=name] input").setValue("Петров Иван");
        $("[data-test-id=phone] input").setValue("");
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id=phone]")
                .shouldBe(visible)
                .shouldHave(text("Поле обязательно для"));
    }

    @Test
    void shouldFillTheFormWithoutACheckBox() {
        $("[data-test-id=city] input").setValue("Казань");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE, date(3));
        $("[data-test-id=name] input").setValue("Петров Иван");
        $("[data-test-id=phone] input").setValue("+79600000000");
        $x("//*[contains(text(),'Забронировать')]").click();
        WebElement check = $("[data-test-id=agreement].input_invalid");
        assertTrue(check.isDisplayed());
    }
}