package com.romanconversion.service;

import com.romanconversion.model.RomanResponse;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rambala
 *
 * input :  number for which roman numeral has to be calculated
 * output : return response object
 *
 * Approach:
 *      - Roman numerals are usually written from largest to smallest numbers from left to right.
 *      - Roman numerals are represented in 1's, 10's, 100's, 1000's *
 *      - Step 1: Roman Numerals can have 13 possible symbols. We store these numbers and values in fixed array.
 *        Step 2: We start iterating from largest number and check if the input number is less than the largest number in array.
 *               - If the input number is less, we move on to the next smallest number.
 *               - Else we get corresponding roman value and get the remainder.
 *        Step 3: Iterate over the above steps until the remainder is 0. Append each matching numeral to a string and return the result.
 *
 *
 * Example Input : 58
 * Output: LVIII
 *   L - 50
 *   V - 5
 *   I - 1
 * 1. We start checking from numeral[0] and keep iterating until the input number 58 is greater than numeral[6] 50
 * 2. Now we get the matching index from numeral[6] which is L and add it to string builder
 * 3. Subtract the input number from the matching numeral to get remainder 8 = (58 - 50)
 * 4. Iterate the remainder through above steps until the remainder is 0.
 * 5. Keep appending the matching roman charcters and return the final result.
 *
 * Time Complexity O(1)  - fixed number of steps to compute the result
 * Space Complexity O(1) - fixed array for computation
 *
 */
public class RomanService implements Callable<RomanResponse>{

    private static final Logger logger = LoggerFactory.getLogger(RomanService.class);
    private int inputNum;

    String[] roman = {"M",  "CM", "D", "CD",  "C",  "XC", "L", "XL", "X", "IX", "V", "IV", "I" };
    int[] numeral =  {1000, 900, 500,  400,   100,   90,   50,  40,  10,   9,    5,   4,    1  };


    public RomanService(Integer inputNum) {
        this.inputNum = inputNum;
    }

    @Override
    public RomanResponse call() throws Exception {

        StringBuilder sb = new StringBuilder();
        int i = 0;
        int value = inputNum;
        while(value > 0 && i < numeral.length ) {
            if (value < numeral[i]) {
                i ++;
                continue;
            }
            sb.append(roman[i]);
            value = value - numeral[i];
        }
        logger.info("Inside Service Call  [input]: " + this.inputNum + " [output]:"+sb.toString());
        return new RomanResponse(this.inputNum,sb.toString());

    }
}