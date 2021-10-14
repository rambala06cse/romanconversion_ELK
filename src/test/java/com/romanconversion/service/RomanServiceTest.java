package com.romanconversion.service;

import java.util.*;
import java.util.concurrent.*;

import com.romanconversion.model.RomanResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
/**
 * @author rambala
 *
 *   Unit Test case specifically for RomonCalculator
 *
 *   Test 1 -> Input a given Number and Assert against its corresponding Roman Value;
 *
 *   Test 2 -> Input a range of numbers.
 *          -> Assert the sorted output
 *          -> Assert each input and its corresponding output
 *
 *
 *
 */

public class RomanServiceTest
{
    private static final Logger logger = LoggerFactory.getLogger(RomanServiceTest.class);

    @Test
    public void test_single_number_execution() throws InterruptedException, ExecutionException {

        ExecutorService service = Executors.newFixedThreadPool(2);
        List<RomanResponse> listRoman = new ArrayList<RomanResponse>();

        RomanService caluculator = new RomanService(75);
        Future<RomanResponse> future = service.submit(caluculator);
        RomanResponse response = null;
        try
        {
            while(!future.isDone()) {
                logger.info("Calculating Thread Id : " + Thread.currentThread().getId() );
            }
            response = future.get();
        }
        catch (Exception e)
        {
            logger.error("Error:" + e.getMessage());
        }

        service.shutdown();

        Assertions.assertEquals("LXXV",response.getOutput());

        }

    @Test
    public void test_range_parallel_execution() throws InterruptedException, ExecutionException {

        final int threadCount = 3;
        int minimum = 10;
        int maximum = 15;
        HashMap<Integer,String> expectedMap = new HashMap<>();
        expectedMap.put(10,"X");
        expectedMap.put(11,"XI");
        expectedMap.put(12,"XII");
        expectedMap.put(13,"XIII");
        expectedMap.put(14,"XIV");
        expectedMap.put(15,"XV");

        ExecutorService service = Executors.newFixedThreadPool(threadCount);
        List<RomanResponse> listResponse = new ArrayList<RomanResponse>();
        List<Future<RomanResponse>> futureList = new ArrayList<>();

        for (int current = minimum; current<= maximum; current++) {
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
                listResponse.add(future.get());

            } catch (InterruptedException | ExecutionException e) {
                logger.error("Range Path Error:" + e.getMessage());
            }
        }

        listResponse.sort((RomanResponse s1, RomanResponse s2)->s1.getInput()-s2.getInput());

        for(RomanResponse obj : listResponse) {
            Assertions.assertEquals(expectedMap.get(obj.getInput()),obj.getOutput());
            Assertions.assertNotNull(obj.getOutput());
        }
    }
}
