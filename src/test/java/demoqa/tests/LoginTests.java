package demoqa.tests;

import demoqa.api.Account;
import demoqa.models.LoginResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static demoqa.tests.TestData.*;
import static io.qameta.allure.Allure.step;

@Tag("all_bookstore_tests")
public class LoginTests extends TestBase {

  @DisplayName("Successful login with UI")
  @Test
  void successfulLoginWithUiTest() {
    step("Open login page", () ->
            open("/login"));
    step("Fill login form", () -> {
      $("#userName").setValue(login);
      $("#password").setValue(password);
      $("#login").click();
    });
    step("Verify successful authorization", () ->
            $("#userName-value").shouldHave(text(login)));
  }

  @DisplayName("Successful login with API")
  @Test
  void successfulLoginWithApiTest() {

    LoginResponseModel authResponse = step("Get authorization cookie by api", () ->
            Account.getAuthorizationResponse());

    step("Set authorization cookie to browser", () -> {
      open("/favicon.ico");
      getWebDriver().manage().addCookie(new Cookie("userID", authResponse.getUserId()));
      getWebDriver().manage().addCookie(new Cookie("expires", authResponse.getExpires()));
      getWebDriver().manage().addCookie(new Cookie("token", authResponse.getToken()));
    });

    step("Open profile page", () ->
            open("/profile"));

    step("Verify successful authorization", () ->
            $("#userName-value").shouldHave(text(login)));
  }
}