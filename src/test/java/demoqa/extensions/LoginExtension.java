package demoqa.extensions;

import demoqa.models.LoginResponseModel;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.Cookie;
import demoqa.api.Account;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class LoginExtension implements BeforeEachCallback {

  @Override
  public void beforeEach(ExtensionContext context) {
    LoginResponseModel authResponse = Account.getAuthorizationResponse();

    open("/favicon.ico");
    getWebDriver().manage().addCookie(new Cookie("userID", authResponse.getUserId()));
    getWebDriver().manage().addCookie(new Cookie("expires", authResponse.getExpires()));
    getWebDriver().manage().addCookie(new Cookie("token", authResponse.getToken()));
  }
}
