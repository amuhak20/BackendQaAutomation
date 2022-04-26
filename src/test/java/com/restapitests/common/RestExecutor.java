package com.restapitests.common;

import static io.restassured.RestAssured.*;

import com.google.gson.JsonObject;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestExecutor {
	/* Method to executed GET request without RequestSpecification */
	public static Response executeGet(String uri) {
		Response response = given()
		.when()
		  .get(uri)
		  .then()
		  .extract()
		  .response();
		return response;
	}
	
	/* Method to executed GET request with RequestSpecification */
	public static Response executeGet(String uri, RequestSpecification requestSpec) {
		Response response = given()
				.spec(requestSpec)
		.when()
		  .get(uri)
		  .then()
		  .extract()
		  .response();
		return response;
	}
	
	/* Method to executed POST request */
	public static Response executePost(String uri, JsonObject body, RequestSpecification requestSpec) {
		Response response = given()
				.spec(requestSpec)
		.when()
		.body(body)
		  .post(uri)
		  .then()
		  .extract()
		  .response();
		return response;
	}
}
