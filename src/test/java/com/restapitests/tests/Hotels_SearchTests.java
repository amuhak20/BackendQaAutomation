package com.restapitests.tests;

import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.restapitests.common.BaseApiTest;
import com.restapitests.common.RestExecutor;
import com.restapitests.utilities.RequestBody;

import static io.restassured.RestAssured.*;

import java.util.HashMap;
import java.util.Map;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class Hotels_SearchTests extends BaseApiTest {

	JsonObject jsonBody;

	@BeforeClass(alwaysRun = true)
	public void setup() throws ParseException {
		logger = Logger.getLogger(Hotels_SearchTests.class);
		logger.info("************************************************...");
		logger.info("Executing Hotels Lists API Tests started...");

		reqBuilder = new RequestSpecBuilder();

		reqBuilder.addHeader("token", readProperties("api.token"));
		reqBuilder.setContentType(ContentType.JSON);
		reqBuilder.setBaseUri(readProperties("base.uri"));
		requestSpec = reqBuilder.build();
		
		logger.info("Getting Checkin Date by adding 30 days to current date...");
		String getCheckinDate = getFutureDateAddingDays(30);
		logger.info("Checkin Date after adding 30 days to current date..." +getCheckinDate);

		logger.info("Getting Checkout Date by adding 32 days to current date...");
		String getCheckoutDate = getFutureDateAddingDays(32);
		logger.info("Checkout Date after adding 32 days to current date..." +getCheckoutDate);

		jsonBody = convertToJson(
				RequestBody.getRequestBodyHotelSearch(getCheckinDate, getCheckoutDate));
		resp = RestExecutor.executePost("/enigma/search/async", jsonBody, requestSpec);

	}

	@Test(priority = 0, description = "Test case to validate request body after POST Call")
	public void validateRequestBody() {
		logger.info("Vaildating Request body after POST Request..");
		Assert.assertNotNull(resp);
		Assert.assertTrue(resp.body().asString().contains("sId"));
		logger.info("Vaildation of Request body after POST Request successful..");
	}

	@Test(priority = 1, description = "Test case to validate Status Code and Status Line")
	public void validateStatusCodeandLine() {
		logger.info("Vaildating Status code and Status line..");
		Assert.assertEquals(resp.getStatusCode(), Integer.parseInt(readProperties("status.code.ok")));
		Assert.assertEquals(resp.getStatusLine(), readProperties("status.line.ok"));
		logger.info("Vaildation of Status Code and Status Line successful..");
	}
	
	@Test(priority = 2, description = "Test case to validate Content Type and Server Name")
	public void validateHeaderResp() {
		logger.info("Vaildating Content Type and Server Name in Header Resp..");
		Assert.assertEquals(resp.getHeader("Content-Type"), readProperties("content.type"));
		Assert.assertEquals(resp.getHeader("Server"), readProperties("server.name"));
		logger.info("Vaildation of Header Response successful..");
	}
	
	@Test(priority = 3, description = "Test case to validate error code without token")
	public void validateWithoutToken() {
        logger.info("Vaildating for 401 Unauthorized Status code if request sent without token..");
        Response response = given()
        .when()
        .post("https://www.almosafer.com/api/enigma/search/async")
        .then()
        .extract()
        .response();
        Assert.assertEquals(response.getStatusCode(), Integer.parseInt(readProperties("status.code.unauthorized")));
        logger.info("Vaildation of 401 Unauthorized Status code successful.." +response.getStatusCode());
	}
	
	@Test(priority = 4, description = "Test case to validate error code with invalid token")
	public void validateWithInvalidToken() {
        logger.info("Vaildating for 401 Unauthorized Status code if request sent with invalid token..");
        Response response = given()
        		.header("token","skq3131214")
        .when()
        .post("https://www.almosafer.com/api/enigma/search/async")
        .then()
        .extract()
        .response();
        Assert.assertEquals(response.getStatusCode(), Integer.parseInt(readProperties("status.code.unauthorized")));
        logger.info("Vaildation of 401 Unauthorized Status code with invalid token successful.." +response.getStatusCode());
	}
	
	@Test(priority = 5, description = "Test case to validate error code with invalid token")
	public void validateWithInvalidHeader() {
        logger.info("Vaildating for Unasupported error code with invalid header..");
        Map<String, String> map = new HashMap<String, String>();
        map.put("token", readProperties("api.token"));
        map.put("Content-Type", "application/text");
        Response response = given()
        		.headers(map)
        .when()
        .post("https://www.almosafer.com/api/enigma/search/async")
        .then()
        .extract()
        .response();
        Assert.assertEquals(response.getStatusCode(), Integer.parseInt(readProperties("status.code.unsupported")));
        logger.info("Vaildation for unsupported error code successful.." +response.getStatusCode());
	}

	@AfterClass(alwaysRun = true)
	public void cleanUp() {
		logger.info("Executing Hotels List API Tests end...");

	}
}
