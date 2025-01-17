package com.bls.demo.service;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;

@Service
public class BLSDataFetcher {

    private static final String URL = "https://download.bls.gov/pub/time.series/cu/cu.data.15.USMedical";
    private static final String API_KEY = "0e8f069aa32b424ea62c17a1680ab549";

    public void fetchDataAndSaveToFile() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(URL);

            // Mimic a human user with enhanced headers
            request.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36");
            request.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8");
            request.addHeader("Accept-Language", "en-US,en;q=0.5");
            request.addHeader("Accept-Encoding", "gzip, deflate, br");
            request.addHeader("Connection", "keep-alive");
            request.addHeader("Referer", "https://www.google.com/");
            request.addHeader("Authorization", "Bearer " + API_KEY);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int statusCode = response.getCode();

                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        String content = EntityUtils.toString(entity);
                        saveToFile(content);
                        System.out.println("Data saved successfully!");
                    }
                } else {
                    System.out.println("Failed to fetch data. HTTP Status Code: " + statusCode);
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            Thread.sleep(2000); // Wait for 2 seconds between requests
        } catch (InterruptedException e) {
            e.printStackTrace();

        }
    }

    private void saveToFile(String content) {
        try (FileWriter writer = new FileWriter("bls_data.txt")) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
