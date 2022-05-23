package com.restapitests.common;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BaseApiTest {
	public static RequestSpecification requestSpec;
	public static Response resp;
	public static RequestSpecBuilder reqBuilder;

	public static Logger logger;
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	/* Method to read values from Properties file */
	public String readProperties(String key) {
		Properties prop = new Properties();
		InputStream inStream;
		String propValue = "";
		try {
			inStream = getClass().getClassLoader().getResourceAsStream("config.properties");
			prop.load(inStream);
			propValue = prop.getProperty(key);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return propValue;

	}

	/* Method to convert String to JSON */
	public static JsonObject convertToJson(String jsonString) {
		JsonObject obj = new JsonParser().parse(jsonString).getAsJsonObject();
		return obj;
	}


	/* Method to get future date by adding number of days */
	public static String getFutureDateAddingDays(int days) {
		Date currentDate = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		c.add(Calendar.DATE, days);
		Date futureDateAfterAddingDays = c.getTime();
		String dateAfterAddingDays = dateFormat.format(futureDateAfterAddingDays);
		return dateAfterAddingDays;
	}
	
	public RequestSpecification reqSpecification() throws FileNotFoundException {
		reqBuilder = new RequestSpecBuilder();
		PrintStream log = new PrintStream(new FileOutputStream("logging.txt"));
		reqBuilder.setContentType(ContentType.JSON);
		reqBuilder.setBaseUri(readProperties("base.uri"));
		reqBuilder.addFilter(RequestLoggingFilter.logRequestTo(log));
		reqBuilder.addFilter(ResponseLoggingFilter.logResponseTo(log));
		requestSpec = reqBuilder.build();
		return requestSpec;
	}

}
