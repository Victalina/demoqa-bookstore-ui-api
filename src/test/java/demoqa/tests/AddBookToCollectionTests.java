package demoqa.tests;

import demoqa.api.Account;
import demoqa.api.BookStore;
import demoqa.extensions.WithLogin;
import demoqa.models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static demoqa.specs.Spec.requestSpec;
import static demoqa.specs.Spec.responseSpecStatusCode;
import static demoqa.tests.TestData.isbn;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@Tag("all_bookstore_tests")
public class AddBookToCollectionTests extends TestBase {

  @DisplayName("Add book to collection")
  @WithLogin
  @Test
  void addBookToCollectionTest() {

    LoginResponseModel authResponse = step("Get authorization cookie by api", () ->
            Account.getAuthorizationResponse());

    String token = authResponse.getToken();
    String userId = authResponse.getUserId();

    step("Delete all books from collection by api", () ->
            BookStore.deleteAllBooksFromCollection(token, userId));

    step("Add book with isbn = " + isbn + " to collection by api", () ->
            BookStore.addBookToCollection(token, userId, isbn));

    step("Check adding book in UI", () -> {
      open("/profile");
      $(".ReactTable").shouldHave(text("Git Pocket Guide"));
    });
  }

  @Test
  void negative400AddBookToCollectionTest() {
    LoginResponseModel authResponse = step("Get authorization cookie by api", () ->
            Account.getAuthorizationResponse());

    String token = authResponse.getToken();
    String userId = authResponse.getUserId();

    step("Delete all books from collection by api", () ->
            BookStore.deleteAllBooksFromCollection(token, userId));

    step("Add book with isbn = " + isbn + " to collection by api", () ->
            BookStore.addBookToCollection(token, userId, isbn));

    List<CollectionOfIsbnsModel> collectionOfIsbns = List.of(new CollectionOfIsbnsModel(isbn));
    AddBookBodyModel bookData = new AddBookBodyModel(userId, collectionOfIsbns);

    ErrorResponseModel response = step("Add another book with isbn = " + isbn + " to collection by api", () ->
            given(requestSpec)
                    .contentType(JSON)
                    .header("Authorization", "Bearer " + token)
                    .body(bookData)
                    .when()
                    .post("/BookStore/v1/ResponseAddBookBooksModel")
                    .then()
                    .spec(responseSpecStatusCode(400))
                    .extract().as(ErrorResponseModel.class));
    step("Check error in response", () -> {
      assertThat(response.getCode(), is(1210));
      assertThat(response.getMessage(), is("ISBN already present in the User's Collection!"));
    });
  }

  @Test
  void negative401AddBookToCollectionTest() {

    String userId = "7fb97b8c-378e-4392-8fd2-e9e12e6e5012";

    List<CollectionOfIsbnsModel> collectionOfIsbns = List.of(new CollectionOfIsbnsModel(isbn));
    AddBookBodyModel bookData = new AddBookBodyModel(userId, collectionOfIsbns);

    ErrorResponseModel response = step("Add book to collection by api without authorization cookie", () ->
            given(requestSpec)
                    .contentType(JSON)
                    .body(bookData)
                    .when()
                    .post("/BookStore/v1/ResponseAddBookBooksModel")
                    .then()
                    .spec(responseSpecStatusCode(401))
                    .extract().as(ErrorResponseModel.class));
    step("Check error in response", () -> {
      assertThat(response.getCode(), is(1200));
      assertThat(response.getMessage(), is("User not authorized!"));
    });
  }
}