package com.restapitests.tests;

import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.restapitests.common.BaseApiTest;
import com.restapitests.common.RestExecutor;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;

public class HotelsLookupTests extends BaseApiTest {

	@BeforeClass(alwaysRun = true)
	public void setup() throws ParseException {
		logger = Logger.getLogger(HotelsLookupTests.class);
		logger.info("************************************************");
		logger.info("Executing Hotels Lookup API Tests started...");

		reqBuilder = new RequestSpecBuilder();

		reqBuilder.addHeader("token", readProperties("api.token"));
		reqBuilder.setContentType(ContentType.JSON);
		reqBuilder.setBaseUri(readProperties("base.uri"));
		requestSpec = reqBuilder.build();

		resp = RestExecutor.executeGet("/enigma/hotel/lookup", requestSpec);
	}

	@Test(priority = 0, description = "Test case to validate Status codes")
	public void validateStatusCodes() {
		logger.info("Validating for Status codes of Hotels Lookup API..");
		Assert.assertEquals(resp.getStatusCode(), Integer.parseInt(readProperties("status.code.ok")),
				"Assertion of Status Code 200 failed");
		Assert.assertEquals(resp.getStatusLine(), readProperties("status.line.ok"), "Assertion of Status Line failed");
		logger.info("Validation of Status codes of Hotels Lookup API successful..");
	}

	@Test(priority = 0, description = "Test case to validate response body")
	public void validateResponseBody() {
		logger.info("Validating for Response body of Hotels Lookup API..");
		Assert.assertNotNull(resp.body().asString(), "Response body not displayed");
		Assert.assertTrue(resp.body().asString().contains("facilityCategory"), "Validation for response body failed");
		logger.info("Validation of Response body of Hotels Lookup API successful..");
	}

	@AfterClass(alwaysRun = true)
	public void cleanUP() {
		logger.info("Executing Hotels Lookup API Tests end...");

	}
}
