package com.example.smartstudent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiHelper {

    public static String getQuote() {
        try {
            URL url = new URL("https://api.quotable.io/random");
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(c.getInputStream()));

            String line, result = "";
            while ((line = br.readLine()) != null)
                result += line;

            br.close();
            return result;

        } catch (Exception e) {
            return "خطا در دریافت داده";
        }
    }
}
