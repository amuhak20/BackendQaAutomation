package com.restapitests.tests;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.restapitests.common.BaseApiTest;
import com.restapitests.common.RestExecutor;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.*;

import org.apache.log4j.Logger;
import org.hamcrest.Matchers;

public class FlightsListTests extends BaseApiTest {

	@BeforeClass(alwaysRun = true)
	public void setup() {
		logger = Logger.getLogger(FlightsListTests.class);
		logger.info("************************************************");
		logger.info("Executing Flights Lists API Tests started...");
		reqBuilder = new RequestSpecBuilder();

		reqBuilder.setContentType(ContentType.JSON);
		reqBuilder.setBaseUri(readProperties("base.uri"));
		requestSpec = reqBuilder.build();

		resp = RestExecutor.executeGet("/system/currency/list", requestSpec);

	}

	@Test(priority = 0, description = "Test case to validate GET response status code ")
	public void validateStatusCode() {
		logger.info("Vaildating response code of GET request");
		Assert.assertEquals(resp.getStatusCode(), Integer.parseInt(readProperties("status.code.ok")),
				"The assertion for status code failed: ");
		logger.info("Verifying Status Code Test Successful: " + resp.getStatusCode());
	}

	@Test(priority = 1, description = "Test case to validate Base Currency from GET Response")
	public void validateBaseCurrency() {
		logger.info("Vaildating Base Currency in GET response");
		String baseCurrncy = resp.jsonPath().get("base.symbol").toString();
		Assert.assertEquals(baseCurrncy, readProperties("base.currency"), "The assertion for Base Currency failed: ");
		logger.info("Vaildating Base Currency successful: " + baseCurrncy);
	}

	@Test(priority = 2, description = "Test case to validate response body")
	public void validateResponseBody() {
		logger.info("Vaildating response body of request");
		String respBody = resp.body().asString();
		Assert.assertTrue(respBody.contains("base"), "The assertion for response body failed");
		Assert.assertTrue(respBody.contains("equivalent"));
		logger.info("Verifying Response body Test Successful: " + respBody);
	}
	
	@Test(priority = 3, description = "Test case to validate Total Number of equivalent currencies")
	public void validateNoOfEquivalentCurrencies() {
		
		logger.info("Vaildating total number of supported currencies...");
		String equCurrSize = readProperties("equilvalent.currency.size");
		resp
		.then()
		.assertThat()
		.body("equivalent", hasSize(Integer.parseInt(equCurrSize)));
		
		logger.info("Vaildating number of supported currencies succesful");
	}
	
	@Test(priority = 4, description = "Test case to verify Status Line Code")
	public void validateStatusLine() {

		logger.info("Vaildating Status Line Code of GET response...");
		Assert.assertEquals(resp.getStatusLine(), readProperties("status.line.ok"),
				"The assertion for status line failed");
		logger.info("Vaildating Status Line of GET response is successful: " + resp.getStatusLine());

	}

	@Test(priority = 5, description = "Test case to verify Server Name in Header Response")
	public void validateServerName() {
		logger.info("Vaildating Server Name in Response Header...");
		String serverName = resp.header("Server");
		Assert.assertEquals(serverName, readProperties("server.name"), "The assertion for Server Name failed");
		logger.info("Vaildating Server Name in Response Header successful: " + serverName);
	}

	@Test(priority = 6, description = "Test to validate Response time")
	public void validateResposeTime() {
		logger.info("Verifying GET request response time not exceeding 5000 milli seconds..");
		resp.then().assertThat().time(Matchers.lessThan(5000L));
		logger.info("GET request completed within 5000 milli seconds.." + resp.getTime());
	}
	
	@Test(priority = 7, description = "Test to validate 404 Error code is displayed when resource not found")
	public void validateResourceNotFound() {
		logger.info("Vaildating response code 404 when resource not available");
		Response response = RestExecutor.executeGet(readProperties("base.uri") + "/system/currency/test");
		Assert.assertEquals(response.getStatusCode(), 404, "The assertion for status code failed: ");
		logger.info("Verifying Status Code 404 Test Successful: " + resp.getStatusCode());
	}
	
	@AfterClass(alwaysRun = true)
	public void cleanUp() {
		logger.info("Executing Flight List API Tests end...");

	}
	
}
