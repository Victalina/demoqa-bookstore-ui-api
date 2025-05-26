package demoqa.api;

import demoqa.models.GetBookResponseModel;
import demoqa.models.LoginBodyModel;
import demoqa.models.LoginResponseModel;

import static demoqa.specs.Spec.requestSpec;
import static demoqa.specs.Spec.responseSpecStatusCode;
import static demoqa.tests.TestData.login;
import static demoqa.tests.TestData.password;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class Account {

  public static LoginResponseModel getAuthorizationResponse() {
     LoginBodyModel authData = new LoginBodyModel(login, password);

    LoginResponseModel authResponse = given(requestSpec)
            .contentType(JSON)
            .body(authData)
            .when()
            .post("/Account/v1/Login")
            .then()
            .spec(responseSpecStatusCode(200))
            .extract().as(LoginResponseModel.class);

    return authResponse;
  }

  public static GetBookResponseModel getBooksFromCollection(String token, String userId) {

    GetBookResponseModel response = given(requestSpec)
            .contentType(JSON)
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/Account/v1/User/" + userId)
            .then()
            .spec(responseSpecStatusCode(200))
            .extract().as(GetBookResponseModel.class);

    return response;
  }
}
