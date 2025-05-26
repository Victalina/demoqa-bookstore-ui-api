package demoqa.api;

import demoqa.models.AddBookBodyModel;
import demoqa.models.AddBookResponseModel;
import demoqa.models.CollectionOfIsbnsModel;
import java.util.List;
import static demoqa.specs.Spec.requestSpec;
import static demoqa.specs.Spec.responseSpecStatusCode;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class BookStore {

  public static void deleteAllBooksFromCollection(String token, String userId){
    given(requestSpec)
            .contentType(JSON)
            .header("Authorization", "Bearer " + token)
            .queryParams("UserId", userId)
            .when()
            .delete("/BookStore/v1/books")
            .then()
            .spec(responseSpecStatusCode(204));
  }

  public static AddBookResponseModel addBookToCollection(String token, String userId, String isbn) {
    List<CollectionOfIsbnsModel> collectionOfIsbns = List.of(new CollectionOfIsbnsModel(isbn));
    AddBookBodyModel bookData = new AddBookBodyModel(userId, collectionOfIsbns);

    AddBookResponseModel response = given(requestSpec)
            .contentType(JSON)
            .header("Authorization", "Bearer " + token)
            .body(bookData)
            .when()
            .post("/BookStore/v1/books")
            .then()
            .spec(responseSpecStatusCode(201))
            .extract().as(AddBookResponseModel.class);

    return response;
  }
}


