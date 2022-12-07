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

import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
public class SpringBootCompanyControllerTests {

    @Autowired
    MockMvc client;
    @Autowired
    CompanyRepository companyRepository;

    @BeforeEach
    void clean_repos(){
        companyRepository.clearAll();}


    @Test
    void should_get_all_company_when_perform() throws Exception{
        companyRepository.create(new Company(100, "spring", new ArrayList<>()));


        client.perform(MockMvcRequestBuilders.get("/companies"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("spring"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].employees",hasSize(0)));


    }


    @Test
    void should_get_company_by_id_when_perform() throws Exception{
        Company spring = companyRepository.create(new Company(100, "spring", new ArrayList<>()));
        Company summer = companyRepository.create(new Company(101, "summer", new ArrayList<>()));


        client.perform(MockMvcRequestBuilders.get("/companies/{id}",spring.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("spring"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees",hasSize(0)));

    }


    @Test
    void should_get_employees_by_id_when_perform() throws Exception{
        Company spring = companyRepository.create(new Company(100, "spring", new ArrayList<>()));
        Company summer = companyRepository.create(new Company(101, "summer", new ArrayList<>()));


        client.perform(MockMvcRequestBuilders.get("/companies/{id}",spring.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("spring"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees",hasSize(0)));

    }
}
