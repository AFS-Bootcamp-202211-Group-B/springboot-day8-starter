package com.rest.springbootemployee;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.awt.*;

import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {
    @Autowired
    MockMvc client;

    @Autowired
    EmployeeRepository employeeRepository;

    @BeforeEach
    void cleanRepository(){
        employeeRepository.clearAll();
    }

    @Test
    void should_get_all_employees_when_perform_get_given_employees() throws Exception {
        // given
        employeeRepository.create(new Employee(10,"hi",22,"female",10000));
//        employeeRepository.create(new Employee(11,"hii",21,"male",11000));

        // when & then
        client.perform(MockMvcRequestBuilders.get("/employees"))
                // 1. assert response status
                .andExpect(MockMvcResultMatchers.status().isOk())
                // 2. assert response data
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(1)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name",containsInAnyOrder("hi","hii"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("hi"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value("female"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(10000));

    }
    @Test
    void should_get_employee_by_id_when_perform_get_given_employees() throws Exception {
        // given
        Employee hi = employeeRepository.create(new Employee(10,"hi",22,"female",10000));
        employeeRepository.create(new Employee(11,"hii",21,"male",11000));

        // when & then
        client.perform(MockMvcRequestBuilders.get("/employees/{id}",hi.getId()))
                // 1. assert response status
                .andExpect(MockMvcResultMatchers.status().isOk())
                // 2. assert response data
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("hi"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("female"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(10000));

    }

    @Test
    void should_get_employee_by_gender_when_perform_get_given_employees() throws Exception {
        // given
        Employee hi = employeeRepository.create(new Employee(10,"hi",22,"female",10000));
        employeeRepository.create(new Employee(11,"hii",21,"male",11000));

        // when & then
        client.perform(MockMvcRequestBuilders.get("/employees?gender={gender}","female"))
                // 1. assert response status
                .andExpect(MockMvcResultMatchers.status().isOk())
                // 2. assert response data
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("hi"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value("female"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(10000));

    }
    @Test
    void should_add_employee_when_perform_post_given_employees() throws Exception {
        // given
        Employee hi = new Employee(10,"hi",22,"female",10000);
        ObjectMapper objectMapper = new ObjectMapper();
        // when & then
        client.perform(MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(hi)))
                // 1. assert response status
                .andExpect(MockMvcResultMatchers.status().isCreated())
                // 2. assert response data
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("hi"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("female"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(10000));

    }
    @Test
    void should_get_employees_by_page_when_perform_get_given_employees() throws Exception {
        // given
        employeeRepository.create(new Employee(11,"one",21,"male",11000));
        employeeRepository.create(new Employee(10,"two",22,"female",10000));
        employeeRepository.create(new Employee(12,"three",21,"male",11000));
        employeeRepository.create(new Employee(12,"four",21,"male",11000));

        // when & then
        client.perform(MockMvcRequestBuilders.get("/employees?page={page}&pageSize={pageSize}",2,2))
                // 1. assert response status
                .andExpect(MockMvcResultMatchers.status().isOk())
                // 2. assert response data
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("three"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(21))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value("male"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(11000));

    }

}
