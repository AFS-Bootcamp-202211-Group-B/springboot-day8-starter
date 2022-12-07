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
    void should_get_all_companies_when_perform_get_given_companies() throws Exception {
        // given
        Company company = new Company(1, "spring", null);
        companyRepository.create(company);

        // when & then
        client.perform(MockMvcRequestBuilders.get("/companies"))
                // 1. assert response status
                .andExpect(MockMvcResultMatchers.status().isOk())
                // 2. assert response data
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("spring"));

    }

    @Test
    void should_get_company_by_id_when_perform_get_given_companies() throws Exception {
        // given
        Company company1 = new Company(1, "one", null);
        Company company2 = new Company(2, "two", null);
        companyRepository.create(company1);
        companyRepository.create(company2);

        // when & then
        client.perform(MockMvcRequestBuilders.get("/companies/{id}",1))
                // 1. assert response status
                .andExpect(MockMvcResultMatchers.status().isOk())
                // 2. assert response data
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("one"));

    }
    @Test
    void should_get_list_of_employees_when_perform_get_given_companies() throws Exception {
        // given
        List<Employee> employees = new ArrayList<>();
        Employee employee1 = new Employee(10,"one",22,"female",10000);
        Employee employee2 = new Employee(10,"two",21,"male",11000);
        employees.add(employee1);
        employees.add(employee2);
        Company company1 = new Company(1, "one", employees);
        Company company2 = new Company(2, "two", null);
        companyRepository.create(company1);
        companyRepository.create(company2);

        // when & then
        client.perform(MockMvcRequestBuilders.get("/companies/{id}/employees",1))
                // 1. assert response status
                .andExpect(MockMvcResultMatchers.status().isOk())
                // 2. assert response data
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("one"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value("female"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(10000));

    }
}
