package com.romanconversion.controller;


import com.romanconversion.model.RomanResponse;
import com.romanconversion.service.RomanService;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class RomanController {

    private static final Logger logger = LoggerFactory.getLogger(RomanController.class);

    @GetMapping("/")
    public ResponseEntity<?>  getWelcomeMessage(){

        return ResponseEntity.ok().body("Welcome to Roman Conversion !!!");
    }

    @GetMapping("/romannumeral")
    public ResponseEntity<?>  getRomanRange(
            @RequestParam(value = "query",required = false) Integer inputNumber,
            @RequestParam(value = "min",required = false) Integer minNumber,
            @RequestParam(value = "max",required = false) Integer maxNumber) {
        try {

            if (inputNumber != null && minNumber == null && maxNumber == null) {
                logger.info("Number Path..");
                ExecutorService service = Executors.newSingleThreadExecutor();
                RomanResponse output = null;
                if (inputNumber > 0 && inputNumber < 4000) {
                    RomanService calculator = new RomanService(inputNumber);
                    Future<RomanResponse> future = service.submit(calculator);
                    try
                    {
                        output = future.get();
                        logger.info(" After Service [Input]  : " + future.get().getInput() + " -> [Output]:" + future.get().getOutput());
                    }
                    catch (Exception e)
                    {
                        logger.error("Number Path Error:" + e.getMessage());
                    }
                    return ResponseEntity.ok(output);
                }

                return ResponseEntity.badRequest().body("ERROR: number should be between 1 and 3999");

            } else if (inputNumber == null && minNumber != null && maxNumber != null) {
                logger.info("Range Method...");
                ExecutorService service = Executors.newFixedThreadPool(5);
                List<RomanResponse> outputRange = new ArrayList<>();
                List<Future<RomanResponse>> futureList = new ArrayList<>();

                if ((minNumber < 0 || minNumber > 3999) || (maxNumber < 0 || maxNumber > 3999) || minNumber > maxNumber) {
                    return ResponseEntity.badRequest().body("Minium Number should be Lesser than Maximum Number");
                }

                for (int current = minNumber; current <= maxNumber; current++) {
                    RomanService calculator = new RomanService(current);
                    Future<RomanResponse> result = service.submit(calculator);
                    futureList.add(result);
                }

                for (Future<RomanResponse> future : futureList) {
                    try {
                        logger.info(" Calculate [Thread Id] : " + Thread.currentThread().getId() + " [Thread Name] : " + Thread.currentThread().getName());
                        while(!future.isDone()) {
                            logger.info("Delay Calculating Thread Name : " + Thread.currentThread().getName());
                        }
                        outputRange.add(future.get());

                    } catch (InterruptedException | ExecutionException e) {
                        logger.error("Range Path Error:" + e.getMessage());
                    }
                }
                outputRange.sort((RomanResponse s1, RomanResponse s2) -> s1.getInput() - s2.getInput());
                outputRange.
                        stream().
                        forEach(s -> logger.info(" After Calculate [Input]  : " + s.getInput() + " -> [Output]:" + s.getOutput()));


                Map<String, List<RomanResponse>> resultRange = new HashMap<>();
                resultRange.put("conversions", outputRange);
                return ResponseEntity.ok(resultRange);

            } else {
                return ResponseEntity.badRequest().body("ERROR: Only Single Number or Range(1 to 3999) should be Provided");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR: Something went wrong. Please try later");
        }
    }
}
