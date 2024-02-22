package com.example.FHTest.controller;

import com.example.FHTest.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(ReportController.class)
class ReportControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private TransactionService service;

    @Test
    void test() throws Exception {
        mvc.perform(get("/transaction/search")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}