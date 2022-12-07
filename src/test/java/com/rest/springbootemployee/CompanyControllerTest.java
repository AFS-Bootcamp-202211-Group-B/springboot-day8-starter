package com.rest.springbootemployee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyControllerTest {

    @Autowired
    MockMvc client;

    @Autowired
    CompanyRepository companyRepository;

    @BeforeEach
    void cleanRepository(){
        companyRepository.clearAll();
    }

    @Test
    void should_get_all_companies_when_perform_get_given_companies() throws Exception{
        //given
        companyRepository.create(new Company(100, "spring", new ArrayList<>()));

        //when
        client.perform(MockMvcRequestBuilders.get("/companies"))
                //Then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("spring"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].employees",hasSize(0)));

    }

    @Test
    void should_get_companies_by_id_when_perform_get_by_id_given_companies() throws Exception{
        //given
        Company test1=companyRepository.create(new Company(100, "spring", new ArrayList<>()));
        Company test2=companyRepository.create(new Company(101, "spring", new ArrayList<>()));
        //when
        client.perform(MockMvcRequestBuilders.get("/companies/{id}",test1.getId()))
                //Then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("spring"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees",hasSize(0)));
    }
}
