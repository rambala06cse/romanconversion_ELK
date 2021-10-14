package com.romanconversion.controller;

import com.romanconversion.service.RomanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author rambala
 */

@WebMvcTest(RomanController.class)
public class ControllerTest {
        @MockBean
        RomanService calculator;
        @Autowired
        MockMvc mockMvc;

        @Test
        public void success() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders
                            .get("/romannumeral?query=58"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("LVIII")));
        }

        @Test
        public void success_range() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/romannumeral?min=1&max=3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("conversions")));
        }


        @Test
        public void failure_out_of_range() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                            .get("/romannumeral?query=4001"))
                    .andDo(print())
                    .andExpect(status().is4xxClientError())
                    .andExpect(content().string(containsString("ERROR: number should be between 1 and 3999")));
        }

        @Test
        public void failure_String() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders
                                .get("/romannumeral?query=4001"))
                        .andDo(print())
                        .andExpect(status().is4xxClientError());
        }


        @Test
        public void failure_multiple_inputs() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders
                                .get("/romannumeral?min=10&max=20&query=400"))
                        .andDo(print())
                        .andExpect(status().is4xxClientError())
                        .andExpect(content().string(containsString("ERROR: Only Single Number or Range(1 to 3999) should be Provided")));
        }


        @Test
        public void failure_incorrect_range() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders
                                .get("/romannumeral?min=100&max=50"))
                        .andDo(print())
                        .andExpect(status().is4xxClientError())
                        .andExpect(content().string(containsString("Minium Number should be Lesser than Maximum Number")));
        }

        @Test
        public void home_page() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders
                                .get("/"))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("Welcome to Roman Conversion !!!")));
        }
    }
