package com.restapitests.tests;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.restapitests.common.BaseApiTest;
import com.restapitests.common.RestExecutor;
import com.restapitests.utilities.RequestBody;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;

public class FlightsGetFaresCaledarTests extends BaseApiTest {

	JsonObject jsonBody;

	@BeforeClass(alwaysRun = true)
	public void setup() {
		logger = Logger.getLogger(FlightsGetFaresCaledarTests.class);
		logger.info("************************************************...");
		logger.info("Executing Get Fares Calendar API Tests started...");
		reqBuilder = new RequestSpecBuilder();

		reqBuilder.setContentType(ContentType.JSON);
		reqBuilder.setBaseUri(readProperties("base.uri"));
		requestSpec = reqBuilder.build();
		
		logger.info("Getting Departure From date by adding 30 days to current date...");
		String getDateAddingMonth = getFutureDateAddingDays(30);
		logger.info("Departure From date after adding 30 days to current date..." +getDateAddingMonth);
		
		logger.info("Getting Departure To date by adding 32 days to current date...");
		String getDateAddingMonthTowDays = getFutureDateAddingDays(32);
		logger.info("Departure To date after adding 32 days to current date..." +getDateAddingMonthTowDays);

		jsonBody = convertToJson(
				RequestBody.getRequestBodyGetFaresCalendar(getDateAddingMonth, getDateAddingMonthTowDays));
		resp = RestExecutor.executePost("/v3/flights/flight/get-fares-calender", jsonBody, requestSpec);

	}

	@Test(priority = 0, description = "Validation of Status codes of Get Fares Calendar API")
	public void validateStatusCodes() {
		logger.info("Validating Status codes for Get Fares Calendar API...");
		Assert.assertEquals(resp.getStatusCode(), Integer.parseInt(readProperties("status.code.ok")),
				"Assertion for Status codes failed");
		logger.info("Validation of Status codes successful...");

	}

	@Test(priority = 0, description = "Validation of Response body of Get Fares Calendar API")
	public void validateResponseBody() {
		logger.info("Validating Response body for Get Fares Calendar API...");
		Assert.assertNotNull(resp.body().asString());
		Assert.assertTrue(resp.body().asString().contains("price"), "Assertion for Response body failed");
		logger.info("Validation of Response body successful...");

	}

	@AfterClass(alwaysRun = true)
	public void cleanUp() {
		logger.info("Executing Get Fares Calendar API Tests end...");

	}

}
