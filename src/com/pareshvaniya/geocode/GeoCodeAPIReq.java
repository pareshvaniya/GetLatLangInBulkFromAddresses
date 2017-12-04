package com.pareshvaniya.geocode;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.google.gson.Gson;

public class GeoCodeAPIReq {

	public String[] getLatLongPositions(String address, String pincode, String city) throws Exception {
		String latitude = "";
		String longitude = "";
		String Key = "XXXXXXXXXX";
		try {
			URL url = new URL(
					"https://maps.googleapis.com/maps/api/geocode/json?key="+Key+"&address="
							+ URLEncoder.encode(address, "UTF-8") + "&sensor=false");

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output = "", full = "";
			while ((output = br.readLine()) != null) {
				full += output;
			}

			GeoCodeAPIRespVerify gson = new Gson().fromJson(full, GeoCodeAPIRespVerify.class);

			if (gson.getStatus().equals("OK")) {
				Results[] results = gson.getResults();

				String addressformatted = results[0].getFormatted_address();
				
				if (addressformatted.contains(pincode) || addressformatted.toLowerCase().contains(city.toLowerCase())) {
					latitude = results[0].getGeometry().getLocation().getLat();
					longitude = results[0].getGeometry().getLocation().getLng();
				}

			}

			conn.disconnect();

		} catch (Exception e) {
			System.err.println("Exception occured while getting LatLang from this "+address+ " : " +e.getMessage());
		}

		return new String[] { latitude, longitude };

	}
}