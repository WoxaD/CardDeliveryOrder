import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

public class CardDeliveryOrderTest {

    String date(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    @Test
    void shouldFillTheFormWithNormalData() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Казань");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, date(3));
        $("[data-test-id=name] input").setValue("Петров Иван");
        $("[data-test-id=phone] input").setValue("+79600000000");
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id=notification]")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("успешно забронирована"));
    }

    @Test
    void shouldFillTheFormWithADoubleName() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Казань");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, date(3));
        $("[data-test-id=name] input").setValue("Петрова Анна-Мария");
        $("[data-test-id=phone] input").setValue("+79600000000");
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id=notification]")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("успешно забронирована"));
    }

    @Test
    void shouldFillTheFormWithNotInTheListCity() {
        open("http://localhost:9999");
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
        open("http://localhost:9999");
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
        open("http://localhost:9999");
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
        open("http://localhost:9999");
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
        open("http://localhost:9999");
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
        open("http://localhost:9999");
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
        open("http://localhost:9999");
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
        open("http://localhost:9999");
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
        open("http://localhost:9999");
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
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Казань");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE, date(3));
        $("[data-test-id=name] input").setValue("Петров Иван");
        $("[data-test-id=phone] input").setValue("+79600000000");
        $x("//*[contains(text(),'Забронировать')]").click();
        WebElement check = $("[data-test-id=agreement].input_invalid");
        assertFalse(check.isSelected());
    }
}