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
    @Autowired
    EmployeeRepository employeeRepository;

    @BeforeEach
    void cleanRepository() {
        companyRepository.cleanAll();
        employeeRepository.cleanAll();
    }

    @Test
    void should_get_all_companies_when_perform_get_given_companies() throws Exception {
        //given
        Employee susan = employeeRepository.create(new Employee(10, "Susan", 22, "Female", 10000));
        Employee bob = employeeRepository.create(new Employee(11, "Bob", 23, "Male", 10000));
        List<Employee> employees = new ArrayList<>();
        employees.add(susan);
        employees.add(bob);
        companyRepository.create(new Company(10,"abc company",employees));
        companyRepository.create(new Company(11, "def company",employees));

        //when & then
        client.perform(MockMvcRequestBuilders.get("/companies"))
                // 1. assert response status
                .andExpect(MockMvcResultMatchers.status().isOk())
                // 2. assert response data

                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("abc company"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].employees").isArray());
    }

    @Test
    void should_get_companies_by_id_when_perform_get_by_id_given_companies() throws Exception {
        //given
        Employee susan = employeeRepository.create(new Employee(10, "Susan", 22, "Female", 10000));
        Employee bob = employeeRepository.create(new Employee(11, "Bob", 23, "Male", 10000));
        List<Employee> employees = new ArrayList<>();
        employees.add(susan);
        employees.add(bob);
        Company company = companyRepository.create(new Company(10,"abc company",employees));

        //when & then
        client.perform(MockMvcRequestBuilders.get("/companies/{id}",company.getId()))
                // 1. assert response status
                .andExpect(MockMvcResultMatchers.status().isOk())
                // 2. assert response data


                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("abc company"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees").isArray());
    }

    @Test
    void should_get_employees_by_company_id_when_perform_get_Employees_given_Companies_with_employees() throws Exception {
        //given
        Employee susan = employeeRepository.create(new Employee(10, "Susan", 22, "Female", 10000));
        List<Employee> employees = new ArrayList<>();
        employees.add(susan);
        Company company = companyRepository.create(new Company(10,"abc company",employees));
        //when & Then
        client.perform(MockMvcRequestBuilders.get("/companies/{id}/employees",company.getId()))
                // 1. assert response status
                .andExpect(MockMvcResultMatchers.status().isOk())
                // 2. assert response data
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Susan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value("Female"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(10000));

    }
}
