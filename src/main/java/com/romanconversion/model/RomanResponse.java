package com.romanconversion.model;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author rambala
 *  *
 * Response in Json Format
 *  {
 *      “input” : “3”,
 *      “output” : “III”
 *   }
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RomanResponse implements Serializable{
    private int input;
    private String output;

    public RomanResponse(int input, String output) {
        this.input = input;
        this.output = output;
    }

    public int getInput() {
        return input;
    }

    public String getOutput() {
        return output;
    }
}
