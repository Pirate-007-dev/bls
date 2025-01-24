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
    private static final String EMAIL = "ananthbr23@gmail.com"; // Replace with your email address

    public void fetchDataAndSaveToFile() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(URL);

            // Use email address as User-Agent as requested
            request.addHeader("User-Agent", EMAIL);

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
                System.err.println("Failed to parse response: " + e.getMessage());
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            System.err.println("An IO error occurred: " + e.getMessage());
            throw new RuntimeException(e);
        }

        try {
            Thread.sleep(2000); // Wait for 2 seconds between requests
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    private void saveToFile(String content) {
        try (FileWriter writer = new FileWriter("bls_data.txt")) {
            writer.write(content);
        } catch (IOException e) {
            System.err.println("Failed to save data to file: " + e.getMessage());
        }
    }
}
