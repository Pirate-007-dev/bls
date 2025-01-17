package com.bls.demo.controller;

import com.bls.demo.service.BLSDataFetcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BLSController {
    private final BLSDataFetcher dataFetcher;

    public BLSController(BLSDataFetcher dataFetcher) {
        this.dataFetcher = dataFetcher;
    }

    @GetMapping("/fetch-bls-data")
    public String fetchBLSData() {
        dataFetcher.fetchDataAndSaveToFile();
        return "Data fetching initiated. Check the local file once completed.";
    }
}
