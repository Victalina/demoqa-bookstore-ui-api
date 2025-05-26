package demoqa.tests;

import demoqa.api.Account;
import demoqa.api.BookStore;
import demoqa.extensions.WithLogin;
import demoqa.models.GetBookResponseModel;
import demoqa.models.LoginResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static demoqa.tests.TestData.isbn;
import static io.qameta.allure.Allure.step;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

@Tag("all_bookstore_tests")
public class DeleteBookFromCollectionTests extends TestBase {

  @DisplayName("Delete book from collection")
  @WithLogin
  @Test
  void deleteBookFromCollectionTest() {

    LoginResponseModel authResponse = step("Get authorization cookie by api", () ->
            Account.getAuthorizationResponse());

    String token = authResponse.getToken();
    String userId = authResponse.getUserId();

    step("Delete all books from collection by api", () ->
            BookStore.deleteAllBooksFromCollection(token, userId));

    step("Add book with isbn = " + isbn + " to collection by api", () ->
            BookStore.addBookToCollection(token, userId, isbn));

    step("Delete book in UI", () -> {
      open("/profile");
      $("#delete-record-undefined").click();
      $(".modal-content").$("#closeSmallModal-ok").click();
    });
    step("Check book deletion in UI", () ->
            $(".ReactTable").shouldNotHave(text("Git Pocket Guide")));

    step("Check book deletion by API", () -> {
      GetBookResponseModel responseBooks = Account.getBooksFromCollection(token, userId);
      assertThat(responseBooks.getBooks(), hasSize(0));
    });
  }
}