package com.restapitests.utilities;

public class RequestBody {

	public static String getRequestBodyHotelSearch(String checkinDate, String checkoutDate) {
		String jsonBody = "{\"checkIn\":\"" + checkinDate + "\",\"checkOut\":\"" + checkoutDate + "\","
				+ "\"roomsInfo\":[{\"adultsCount\":2,\"kidsAges\":[]}],"
				+ "\"placeId\":\"ChIJmZNIDYkDLz4R1Z_nmBxNl7o\"}";

		return jsonBody;
	}

	public static String getRequestBodyGetFaresCalendar(String departureFrom, String departureTo) {
		String jsonBody = "{\"leg\":[{\"originId\":\"GIZ\",\"destinationId\":\"SHJ\",\"departureFrom\":\""
				+ departureFrom + "\"," + "\"departureTo\":\"" + departureTo
				+ "\"}],\"cabin\":\"Economy\",\"pax\":{\"adult\":2,\"child\":0,"
				+ "\"infant\":0},\"stops\":[],\"airline\":[],\"timeSlots\":{},\"airports\":{}}";
		return jsonBody;
	}
}
